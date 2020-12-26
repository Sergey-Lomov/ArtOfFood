package artoffood.common.utils;

import artoffood.ArtOfFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;

import java.util.List;
import java.util.stream.Collectors;

public class ModNBTHelper {

    public static final String PROCESSING_NBT_KEY = "processings";
    public static final int STRING_NBT_TYPE_ID = 8;

    public static List<String> processingsIds(ItemStack stack) {
        prepareIngredientStruct(stack);
        return listNBTToStrings(processingsNBT(stack));
    }

    public static void addProcessingId(ItemStack stack, String id) {
        prepareIngredientStruct(stack);
        processingsNBT(stack).add(StringNBT.valueOf(id));
    }

    private static ListNBT processingsNBT(ItemStack stack) {
        return stack.getOrCreateTag().getCompound(ArtOfFood.MOD_ID).getList(PROCESSING_NBT_KEY, STRING_NBT_TYPE_ID);
    }

    private static List<String> listNBTToStrings(ListNBT list) {
        return list.stream().map( nbt -> nbt.getString() ).collect(Collectors.toList());
    }

    private static void prepareIngredientStruct(ItemStack stack) {
        if (!stack.hasTag())
            stack.setTag(new CompoundNBT());

        if (!stack.getTag().contains(ArtOfFood.MOD_ID))
            stack.getTag().put(ArtOfFood.MOD_ID, new CompoundNBT());

        CompoundNBT modTag = stack.getTag().getCompound(ArtOfFood.MOD_ID);

        if (!modTag.contains(PROCESSING_NBT_KEY))
            modTag.put(PROCESSING_NBT_KEY, new ListNBT());
    }
}
