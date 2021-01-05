package artoffood.common.utils.resgistrators;

import artoffood.ArtOfFood;
import artoffood.common.blocks.devices.countertop.CountertopBlock;
import artoffood.common.blocks.devices.kitchen_drawer.KitchenDrawerBlock;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class BlocksRegistrator {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ArtOfFood.MOD_ID);

    public static final RegistryObject<Block> KITCHEN_DRAWER = register("kitchen_drawer", new KitchenDrawerBlock(), 64);
    public static final RegistryObject<Block> COUNTERTOP = register("countertop", new CountertopBlock(), 64);

    private static <T extends Block> RegistryObject<T> register(String name, T block, int stackSize) {
        ItemsRegistrator.registerBlockItem(name, block, stackSize);
        return BLOCKS.register(name, () -> block);
    }
}