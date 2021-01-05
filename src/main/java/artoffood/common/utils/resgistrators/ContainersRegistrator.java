package artoffood.common.utils.resgistrators;

import artoffood.ArtOfFood;
import artoffood.common.blocks.devices.countertop.CountertopContainer;
import artoffood.common.blocks.devices.kitchen_drawer.KitchenDrawerContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainersRegistrator {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, ArtOfFood.MOD_ID);

    public static final RegistryObject<ContainerType<KitchenDrawerContainer>> KITCHEN_DRAWER = register("kitchen_drawer",
            KitchenDrawerContainer::createClientSide);
    public static final RegistryObject<ContainerType<CountertopContainer>> COUNTERTOP = register("countertop",
            CountertopContainer::createClientSide);

    @FunctionalInterface
    private interface FactoryFunc<C> {
        public C apply(int windowId, PlayerInventory inv, PacketBuffer data);
    }

    private static <T extends Container> RegistryObject<ContainerType<T>> register(String name, FactoryFunc<T> func) {
        IContainerFactory<T> factory = new IContainerFactory<T>() {
            @Override
            public T create(int windowId, PlayerInventory inv, PacketBuffer data) {
                return func.apply(windowId, inv, data);
            }
        };
        return CONTAINERS.register(name, () -> IForgeContainerType.create(factory));
    }
}
