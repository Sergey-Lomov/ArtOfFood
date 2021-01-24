package artoffood.common.utils;

import artoffood.common.capabilities.ingredient.IngredientEntityCapability;
import artoffood.common.utils.slots.ConceptResultPreviewSlotConfig;
import artoffood.common.utils.slots.SlotReference;
import artoffood.core.models.ByConceptOrigin;
import artoffood.core.models.Concept;
import artoffood.core.models.ConceptSlot;
import artoffood.core.models.Ingredient;
import artoffood.minebridge.models.MBConcept;
import artoffood.minebridge.models.MBIngredient;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConceptResultsCombinator {

    private static class SlotCandidates {
        final ConceptSlot slot;
        final NonNullList<Slot> candidates;
        final boolean isFilled;

        private SlotCandidates(ConceptSlot slot, NonNullList<Slot> candidates, boolean isFilled) {
            this.slot = slot;
            this.candidates = candidates;
            this.isFilled = isFilled;
        }
    }

    public static NonNullList<ConceptResultPreviewSlotConfig> possbileResults(MBConcept concept,
                                                                              NonNullList<Slot> conceptSlots,
                                                                              NonNullList<Slot> availableSlots,
                                                                              Function<Slot, Integer> containerIdForSlot,
                                                                              Function<Integer, Integer> containerIdForConceptInId) {

        final NonNullList<SlotCandidates> candidates = candidatesMap(concept.core, conceptSlots, availableSlots);
        final NonNullList<NonNullList<Optional<Slot>>> combinations = combinations(candidates);
        return generateResults(concept, combinations, availableSlots, containerIdForSlot, containerIdForConceptInId);
    }

    // Compare itemsStack at slot by item and tag, but ignores count
    protected static boolean compareStacks(ItemStack s1, ItemStack s2) {
        return ItemStack.areItemsEqual(s1, s2) && ItemStack.areItemStackTagsEqual(s1, s2);
    }

    protected static NonNullList<SlotCandidates> candidatesMap(Concept concept,
                                                               NonNullList<Slot> conceptSlots,
                                                               NonNullList<Slot> candidatesSlots) {
        final NonNullList<SlotCandidates> result = NonNullList.create();
        for (int conceptSlotIndex = 0; conceptSlotIndex < concept.slots.size(); conceptSlotIndex++) {
            final ConceptSlot slot = concept.slots.get(conceptSlotIndex);
            NonNullList<Slot> candidates = NonNullList.create();

            final Slot inSlot = conceptSlots.get(conceptSlotIndex);
            if (!inSlot.getStack().isEmpty()) {
                candidates.add(inSlot);
                result.add(new SlotCandidates(slot, candidates, true));
                continue;
            }

            for (Slot candidate: candidatesSlots) {
                candidate.getStack().getCapability(IngredientEntityCapability.INSTANCE).ifPresent(cap -> {
                    if (slot.validate(cap.getIngredient().core)) {
                        Stream<Slot> sameSlots =  candidates.stream().filter(s -> compareStacks(s.getStack(), candidate.getStack()));
                        if (sameSlots.count() == 0)
                            candidates.add(candidate);
                    }
                });
            }

            result.add(new SlotCandidates(slot, candidates, false));
        }

        return result;
    }

    protected static NonNullList<NonNullList<Optional<Slot>>> combinations(NonNullList<SlotCandidates> slotsCandidates) {
        NonNullList<NonNullList<Optional<Slot>>> results = NonNullList.create();
        Optional<SlotCandidates> optionalFirst = slotsCandidates.stream().findFirst();
        if (!optionalFirst.isPresent()) return results;

        ConceptSlot firstSlot = optionalFirst.get().slot;
        List<Optional<Slot>> candidates = optionalFirst.get().candidates.stream().map(Optional::of).collect(Collectors.toList());
        if (firstSlot.optional && !optionalFirst.get().isFilled) candidates.add(Optional.empty());

        if (slotsCandidates.size() == 1) {
            for (Optional<Slot> candidate: candidates) {
                results.add(NonNullList.withSize(1, candidate));
            }

            return results;
        }

        slotsCandidates.remove(0);
        NonNullList<NonNullList<Optional<Slot>>> subcombinations = combinations(slotsCandidates);
        for (Optional<Slot> slot: candidates) {
            for (List<Optional<Slot>> subcombination: subcombinations) {
                NonNullList<Optional<Slot>> combination = NonNullList.create();
                combination.add(slot);
                combination.addAll(subcombination);
                results.add(combination);
            }
        }

        return results;
    }

    protected static NonNullList<ConceptResultPreviewSlotConfig> generateResults(MBConcept concept,
                                                                                 NonNullList<NonNullList<Optional<Slot>>> combinations,
                                                                                 NonNullList<Slot> availableSlots,
                                                                                 Function<Slot, Integer> containerIdForSlot,
                                                                                 Function<Integer, Integer> containerIdForConceptInId) {
        NonNullList<ConceptResultPreviewSlotConfig> results = NonNullList.create();
        Map<ConceptResultPreviewSlotConfig, List<Ingredient>> resultsIngredients = new HashMap<>();

        for (NonNullList<Optional<Slot>> combination: combinations) {
            List<MBIngredient> mbIngredients = combinationIngredients(combination);
            List<Ingredient> ingredients = mbIngredients.stream().map(mbi -> mbi.core).collect(Collectors.toList());

            if (!concept.core.matches(ingredients)) continue;

            List<ConceptResultPreviewSlotConfig> sameResults = results.stream().filter(r -> concept.core.isSimilarIngredients(ingredients, resultsIngredients.get(r))).collect(Collectors.toList());
            boolean existSimilar = false;

            for (ConceptResultPreviewSlotConfig sameResult : sameResults) {
                List<Ingredient> resultIngredients = ((ByConceptOrigin) sameResult.result.core.origin).subingredients;
                if (concept.core.isSimilarIngredients(ingredients, resultIngredients)) {
                    existSimilar = true;
                    break;
                }
            }

            if (existSimilar) continue;

            List<Slot> lessItemsSlots = slotsWithNotEnoughItems(combination);
            if (!lessItemsSlots.isEmpty()) {
                boolean fixed = tryToFixCombination(combination, lessItemsSlots, availableSlots);
                if (!fixed) continue;
            }

            NonNullList<SlotReference> references = NonNullList.create();
            for (int i = 0; i < combination.size(); i++) {
                Integer fromId = null;
                if (combination.get(i).isPresent()) {
                    Slot slot = combination.get(i).get();
                    fromId = containerIdForSlot.apply(slot);
                }
                SlotReference ref = new SlotReference(containerIdForConceptInId.apply(i), fromId);
                references.add(ref);
            }

            MBIngredient resultIngredient = new MBIngredient(concept, mbIngredients);
            ConceptResultPreviewSlotConfig result = new ConceptResultPreviewSlotConfig(
                    resultIngredient,
                    concept.core.resultsCount,
                    references);
            resultsIngredients.put(result, ingredients);
            results.add(result);
        }

        return results;
    }

    // Search slots, which have no enough items. For example some slot may be used in combination 3 times, but have only 2 items.
    protected static List<Slot> slotsWithNotEnoughItems(NonNullList<Optional<Slot>> combination) {
        List<Slot> notEmptySlots = combination.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        Set<Slot> uniqueSlots = new HashSet<>(notEmptySlots);
        return uniqueSlots.stream()
                .filter(s -> Collections.frequency(notEmptySlots, s) > s.getStack().getCount())
                .collect(Collectors.toList());
    }

    // Try to found slots, which provide missing items for each problem slot.
    // If additional slots will not be find, combination will be handled like invalid
    protected static boolean tryToFixCombination(NonNullList<Optional<Slot>> combination,
                                                 List<Slot> lessItemsSlots,
                                                 NonNullList<Slot> availableSlots) {
        for (Slot lessSlot: lessItemsSlots) {
            List<Slot> providers = availableSlots.stream()
                    .filter(s -> compareStacks(s.getStack(), lessSlot.getStack()))
                    .collect(Collectors.toList());
            int providerIndex = 0;
            int providerCredit = 0;
            boolean providersGone = false;
            for (int i = 0; i < combination.size(); i++) {
                if (providersGone) continue;
                if (!combination.get(i).isPresent()) continue;
                if (combination.get(i).get() != lessSlot) continue;

                Slot provider = providers.get(providerIndex);
                combination.set(i, Optional.of(provider));
                providerCredit++;
                if (providerCredit == provider.getStack().getCount()) {
                    if (providerIndex == providers.size() -1)
                        providersGone = true;
                    else {
                        providerIndex++;
                        providerCredit = 0;
                    }
                }
            }

            if (providersGone) return false;
        }

        return true;
    }

    protected static List<MBIngredient> combinationIngredients(NonNullList<Optional<Slot>> combination) {
        return combination.stream().map( os -> {
            if (!os.isPresent()) return MBIngredient.EMPTY;

            AtomicReference<MBIngredient> ingredient = new AtomicReference<>(MBIngredient.EMPTY);
            os.get().getStack().getCapability(IngredientEntityCapability.INSTANCE).ifPresent(
                    cap -> ingredient.set(cap.getIngredient())
            );
            return ingredient.get();
        }).collect(Collectors.toList());
    }
}
