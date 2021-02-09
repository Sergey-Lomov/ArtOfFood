package artoffood;

import artoffood.client.rendering.IngredientColors;
import artoffood.client.rendering.IngredientModel;
import artoffood.client.screens.CountertopScreen;
import artoffood.client.screens.KitchenDrawerScreen;
import artoffood.client.screens.slot_prompt.HighlightSlotPrompt;
import artoffood.client.screens.slot_prompt.ReferenceSlotPrompt;
import artoffood.client.screens.slot_prompt.StubSlotPrompt;
import artoffood.client.screens.slot_prompt.TextSlotPrompt;
import artoffood.client.screens.slot_prompt.rendering.*;
import artoffood.client.utils.KeyBindings;
import artoffood.client.utils.KeyInputHandler;
import artoffood.common.capabilities.food_item.FoodItemEntityCapability;
import artoffood.common.capabilities.food_tool.FoodToolEntityCapability;
import artoffood.common.capabilities.ingredient.IngredientEntityCapability;
import artoffood.common.items.FoodIngredientItem;
import artoffood.common.utils.resgistrators.BlocksRegistrator;
import artoffood.common.utils.resgistrators.ContainersRegistrator;
import artoffood.common.utils.resgistrators.ItemsRegistrator;
import artoffood.common.data_providers.ModRecipesProvider;
import artoffood.common.recipies.RecipeSerializerRegistrator;
import artoffood.common.utils.resgistrators.TileEntityRegistrator;
import artoffood.networking.ModNetworking;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
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

        ModNetworking.registerMessages();
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BlocksRegistrator.BLOCKS.register(modEventBus);
        ItemsRegistrator.registerPrototypedIngredients();
        ItemsRegistrator.registerConceptResultIngredients();
        ItemsRegistrator.registerFoodTools();
        ItemsRegistrator.ITEMS.register(modEventBus);

        RecipeSerializerRegistrator.register(modEventBus);
        ContainersRegistrator.CONTAINERS.register(modEventBus);
        TileEntityRegistrator.TILE_ENTITY_TYPES.register(modEventBus);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        FoodToolEntityCapability.register();
        IngredientEntityCapability.register();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // TODO: Remove manually adding and implement cyclic adding of all necessary textures. Or use texture + variants instead different textures, and remove this specials adding at all.
        ModelLoader.addSpecialModel(new ResourceLocation(ArtOfFood.MOD_ID, "item/concepts/processings/sliced"));
        ModelLoader.addSpecialModel(new ResourceLocation(ArtOfFood.MOD_ID, "item/concepts/processings/grated"));
        ModelLoader.addSpecialModel(new ResourceLocation(ArtOfFood.MOD_ID, "item/concepts/blendy_salty_salad_2"));
        ModelLoader.addSpecialModel(new ResourceLocation(ArtOfFood.MOD_ID, "item/concepts/blendy_salty_salad_3"));
        ModelLoader.addSpecialModel(new ResourceLocation(ArtOfFood.MOD_ID, "item/concepts/blendy_salty_salad_4"));

        RenderTypeLookup.setRenderLayer(BlocksRegistrator.KITCHEN_DRAWER.get(), RenderType.getSolid());
        RenderTypeLookup.setRenderLayer(BlocksRegistrator.COUNTERTOP.get(), RenderType.getSolid());

        ScreenManager.registerFactory(ContainersRegistrator.KITCHEN_DRAWER.get(), KitchenDrawerScreen::new);
        ScreenManager.registerFactory(ContainersRegistrator.COUNTERTOP.get(),  CountertopScreen::new);

        SlotPromptRenderingManager.register(TextSlotPrompt.class, new TextPromptRenderer());
        SlotPromptRenderingManager.register(StubSlotPrompt.class, new StubPromptRenderer());
        SlotPromptRenderingManager.register(HighlightSlotPrompt.class, new HighlightPromptRenderer());
        SlotPromptRenderingManager.register(ReferenceSlotPrompt.class, new ReferencePromptRenderer());

        MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
        ClientRegistry.registerKeyBinding(KeyBindings.MEDITATION);
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

            Stream<RegistryObject<Item>> objects = ItemsRegistrator.ITEMS.getEntries().stream();
            Stream<FoodIngredientItem> foodItems = objects.filter(ro -> ro.get() instanceof FoodIngredientItem).map(ro -> (FoodIngredientItem)ro.get());
            foodItems.forEach( item -> {
                ModelResourceLocation location = new ModelResourceLocation(item.getRegistryName(), "inventory");
                IBakedModel existingModel = event.getModelRegistry().get(location);
                IngredientModel customModel = new IngredientModel(existingModel);
                event.getModelRegistry().put(location, customModel);
            });
        }

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void onColorHandlerEvent(ColorHandlerEvent.Item event)
        {
            ItemColors colors = event.getItemColors();
            Stream<Item> items = ItemsRegistrator.ITEMS.getEntries().stream().map(ro -> ro.get());
            Stream<Item> ingredients = items.filter( i -> i instanceof FoodIngredientItem);
            ingredients.forEach( i -> colors.register(IngredientColors.INSTANCE, i));
        }
    }
}
