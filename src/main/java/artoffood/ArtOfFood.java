package artoffood;

import artoffood.client.rendering.IngredientColors;
import artoffood.client.rendering.IngredientModel;
import artoffood.common.items.IngredientItem;
import artoffood.common.items.ItemsRegistrator;
import artoffood.common.data_providers.ModRecipesProvider;
import artoffood.common.recipies.FoodProcessingRecipe;
import artoffood.common.recipies.RecipeSerializerRegistrator;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ArtOfFood.MOD_ID)
@Mod.EventBusSubscriber(modid = ArtOfFood.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ArtOfFood
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "artoffood";

    public ArtOfFood() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

//        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(IRecipeSerializer.class, this::registerRecipeSerializers);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        RecipeSerializerRegistrator.register(modEventBus);

        ItemsRegistrator.registerIngredients();
        ItemsRegistrator.registerFoodTools();
        ItemsRegistrator.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private void setup(final FMLCommonSetupEvent event)
    {

    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ModelLoader.instance().addSpecialModel(new ResourceLocation(ArtOfFood.MOD_ID, "item/ingredients/sliced"));
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("artoffood", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class DataGenerators {

        @SubscribeEvent
        public static void gatherData(GatherDataEvent event) {
            DataGenerator generator = event.getGenerator();

            if(event.includeServer()) {
                generator.addProvider(new ModRecipesProvider(generator));
            }

            try {
                generator.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientEvents {

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void onModelBakeEvent(final ModelBakeEvent event) {

            Optional<RegistryObject<Item>> first = ItemsRegistrator.ITEMS.getEntries().stream().findFirst();
            ModelResourceLocation location = new ModelResourceLocation(first.get().get().getRegistryName(), "inventory");
            IBakedModel existingModel = event.getModelRegistry().get(location);
            IngredientModel customModel = new IngredientModel(existingModel);
            event.getModelRegistry().put(location, customModel);
        }

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void onColorHandlerEvent(ColorHandlerEvent.Item event)
        {
            ItemColors colors = event.getItemColors();
            Stream<Item> items = ItemsRegistrator.ITEMS.getEntries().stream().map(ro -> ro.get());
            Stream<Item> ingredients = items.filter( i -> i instanceof IngredientItem);
            ingredients.forEach( i -> colors.register(IngredientColors.INSTANCE, i));
        }
    }
}
