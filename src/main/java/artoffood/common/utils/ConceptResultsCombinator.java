package artoffood.common.utils;

import artoffood.common.capabilities.food_item.FoodItemEntityCapability;
import artoffood.common.utils.slots.ConceptResultPreviewSlotConfig;
import artoffood.common.utils.slots.SlotReference;
import artoffood.core.models.ByConceptOrigin;
import artoffood.core.models.Concept;
import artoffood.core.models.ConceptSlot;
import artoffood.core.models.FoodItem;
import artoffood.minebridge.models.MBConcept;
import artoffood.minebridge.models.MBFoodItem;
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
                candidate.getStack().getCapability(FoodItemEntityCapability.INSTANCE).ifPresent(cap -> {
                    if (slot.validate(cap.getFoodItem().itemCore())) {
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
        Map<ConceptResultPreviewSlotConfig, List<FoodItem>> resultsFoodItems = new HashMap<>();

        for (NonNullList<Optional<Slot>> combination: combinations) {
            List<MBFoodItem> mbFoodItems = combinationFoodItems(combination);
            List<FoodItem> items = mbFoodItems.stream().map(mbfi -> mbfi.itemCore()).collect(Collectors.toList());

            if (!concept.core.matches(items)) continue;

            List<ConceptResultPreviewSlotConfig> sameResults = results.stream().filter(r -> concept.core.isSimilarFoodItems(items, resultsFoodItems.get(r))).collect(Collectors.toList());
            boolean existSimilar = false;

            for (ConceptResultPreviewSlotConfig sameResult : sameResults) {
                List<FoodItem> resultFoodItems = ((ByConceptOrigin) sameResult.result.core.origin).items;
                if (concept.core.isSimilarFoodItems(items, resultFoodItems)) {
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

            MBIngredient resultIngredient = concept.getIngredient(mbFoodItems);
            ConceptResultPreviewSlotConfig result = new ConceptResultPreviewSlotConfig(
                    resultIngredient,
                    concept.core.resultsCount(items),
                    references);
            resultsFoodItems.put(result, items);
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
            boolean notEnoughProviders = false;
            for (int i = 0; i < combination.size(); i++) {
                if (!combination.get(i).isPresent()) continue;
                if (combination.get(i).get() != lessSlot) continue;

                if (providerIndex == providers.size()) {
                    notEnoughProviders = true;
                    break;
                }

                Slot provider = providers.get(providerIndex);
                combination.set(i, Optional.of(provider));
                providerCredit++;
                if (providerCredit == provider.getStack().getCount()) {
                    providerIndex++;
                    providerCredit = 0;
                }
            }

            if (notEnoughProviders) return false;
        }

        return true;
    }

    protected static List<MBFoodItem> combinationFoodItems(NonNullList<Optional<Slot>> combination) {
        return combination.stream().map( os -> {
            if (!os.isPresent()) return MBFoodItem.EMPTY;

            AtomicReference<MBFoodItem> ingredient = new AtomicReference<>(MBFoodItem.EMPTY);
            os.get().getStack().getCapability(FoodItemEntityCapability.INSTANCE).ifPresent(
                    cap -> ingredient.set(cap.getFoodItem())
            );
            return ingredient.get();
        }).collect(Collectors.toList());
    }
}
