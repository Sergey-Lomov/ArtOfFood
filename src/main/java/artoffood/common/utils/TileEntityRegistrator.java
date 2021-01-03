package artoffood.common.utils;

import artoffood.ArtOfFood;
import artoffood.common.blocks.devices.kitchen_drawer.KitchenDrawerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class TileEntityRegistrator {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ArtOfFood.MOD_ID);

    public static final RegistryObject<TileEntityType<KitchenDrawerTileEntity>> KITCHEN_DRAWER = register(
            "kitchen_drawer", KitchenDrawerTileEntity::new, BlocksRegistrator.KITCHEN_DRAWER);

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register (String name, Supplier<T> sup, RegistryObject<Block> block) {
        String fullname = ArtOfFood.MOD_ID + name;
        return TILE_ENTITY_TYPES.register(fullname, () -> TileEntityType.Builder.create(sup, block.get()).build(null));
    }
}
