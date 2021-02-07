package artoffood.common.utils;

import artoffood.common.capabilities.food_item.FoodItemEntityCapability;
import artoffood.common.utils.background_tasks.ConceptResultsCalculationInput;
import artoffood.common.utils.background_tasks.ConceptResultsCalculationOutput;
import artoffood.common.utils.slots.ConceptResultSlotConfig;
import artoffood.common.utils.slots.SlotReference;
import artoffood.core.models.Concept;
import artoffood.core.models.ConceptSlot;
import artoffood.core.models.FoodItem;
import artoffood.minebridge.models.MBConcept;
import artoffood.minebridge.models.MBFoodItem;
import artoffood.minebridge.models.MBIngredient;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
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

    private static class ResultDescription {
        final MBIngredient result;
        final int resultCount;
        final NonNullList<SlotReference> references;
        final List<FoodItem> items;

        private ResultDescription(MBIngredient result, int resultCount, NonNullList<SlotReference> references, List<FoodItem> items) {
            this.result = result;
            this.resultCount = resultCount;
            this.references = references;
            this.items = items;
        }
    }

    public static ConceptResultsCalculationOutput possibleResults(ConceptResultsCalculationInput input)
            throws InterruptedException {
        final NonNullList<SlotCandidates> candidates = candidatesMap(input.concept.core, input.conceptSlots, input.availableSlots);

        NonNullList<ResultDescription> results = generateResults(input, candidates);
        List<ConceptResultSlotConfig> configsList = results.stream()
                .map(r -> new ConceptResultSlotConfig(r.result, r.resultCount, r.references))
                .collect(Collectors.toList());
        NonNullList<ConceptResultSlotConfig> configs = NonNullList.create();
        configs.addAll(configsList);
        return new ConceptResultsCalculationOutput(input.concept, configs);
    }

    // Compare itemsStack at slot by item and tag, but ignores count
    protected static boolean compareStacks(ItemStack s1, ItemStack s2) {
        return ItemStack.areItemsEqual(s1, s2) && ItemStack.areItemStackTagsEqual(s1, s2);
    }

    protected static NonNullList<SlotCandidates> candidatesMap(Concept concept,
                                                               NonNullList<Slot> conceptSlots,
                                                               NonNullList<Slot> candidatesSlots)
            throws InterruptedException {
        final NonNullList<SlotCandidates> result = NonNullList.create();
        for (int conceptSlotIndex = 0; conceptSlotIndex < concept.slots.size(); conceptSlotIndex++) {
            if (Thread.interrupted())
                throw new InterruptedException();

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

    protected static NonNullList<ResultDescription> generateResults(ConceptResultsCalculationInput input,
                                                                    NonNullList<SlotCandidates> slotsCandidates)
            throws InterruptedException {
        NonNullList<ResultDescription> results = NonNullList.create();
        addResults(input, NonNullList.create(), slotsCandidates, results);
        return results;
    }


    protected static void addResults(ConceptResultsCalculationInput input,
                                     List<Optional<Slot>> precombination,
                                     NonNullList<SlotCandidates> slotsCandidates,
                                     NonNullList<ResultDescription> results)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();

        if (results.size() >= input.resultsLimit) return;
        if (precombination.size() == slotsCandidates.size()) {
            ResultDescription result = combinationResult(input, precombination, results);
            if (result != null)
                results.add(result);
            return;
        }

        SlotCandidates slotCandidates = slotsCandidates.get(precombination.size());
        List<Optional<Slot>> candidates = slotCandidates.candidates.stream().map(Optional::of).collect(Collectors.toList());
        if (slotCandidates.slot.optional && !slotCandidates.isFilled) candidates.add(Optional.empty());

        for (Optional<Slot> candidate : candidates) {
            List<Optional<Slot>> currentPrecombination = new ArrayList<>(precombination);
            currentPrecombination.add(candidate);
            addResults(input, currentPrecombination, slotsCandidates, results);
        }
    }

    protected static @Nullable ResultDescription combinationResult(ConceptResultsCalculationInput input,
                                                                   List<Optional<Slot>> combination,
                                                                   NonNullList<ResultDescription> results) {
        MBConcept concept = input.concept;
        List<MBFoodItem> mbFoodItems = combinationFoodItems(combination);
        List<FoodItem> items = mbFoodItems.stream().map(MBFoodItem::itemCore).collect(Collectors.toList());

        if (!concept.core.matches(items)) return null;  // Combination invalid for current concept

        List<ResultDescription> sameResults = results.stream()
                .filter(r -> concept.core.isSimilarFoodItems(items, r.items))
                .collect(Collectors.toList());

        for (ResultDescription sameResult : sameResults) {
            if (concept.core.isSimilarFoodItems(items, sameResult.items)) {
                return null;    // Similar result found
            }
        }

        List<Slot> lessItemsSlots = slotsWithNotEnoughItems(combination);
        if (!lessItemsSlots.isEmpty()) {
            boolean fixed = tryToFixCombination(combination, lessItemsSlots, input.availableSlots);
            if (!fixed) return null; // Candidates slots contains not enough item and another slot, which may provide this item wasn't found
        }

        NonNullList<SlotReference> references = NonNullList.create();
        for (int i = 0; i < combination.size(); i++) {
            Integer fromId = null;
            if (combination.get(i).isPresent()) {
                Slot slot = combination.get(i).get();
                fromId = input.containerIdForSlot.apply(slot);
            }
            SlotReference ref = new SlotReference(input.containerIdForConceptInId.apply(i), fromId);
            references.add(ref);
        }

        MBIngredient resultIngredient = concept.getIngredient(mbFoodItems);

        return new ResultDescription(
                resultIngredient,
                concept.core.resultsCount(items),
                references,
                items);
    }

    // Search slots, which have no enough items. For example some slot may be used in combination 3 times, but have only 2 items.
    protected static List<Slot> slotsWithNotEnoughItems(List<Optional<Slot>> combination) {
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
    protected static boolean tryToFixCombination(List<Optional<Slot>> combination,
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

    protected static List<MBFoodItem> combinationFoodItems(List<Optional<Slot>> combination) {
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
