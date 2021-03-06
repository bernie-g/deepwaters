package blueduck.deepwaters;

import blueduck.deepwaters.client.renderer.model.*;
import blueduck.deepwaters.event.PlayerEventSubscriber;
import blueduck.deepwaters.registry.DeepWatersContainerTypes;
import blueduck.deepwaters.gui.surge.SurgeScreen;
import blueduck.deepwaters.registry.DeepWatersEntities;
import blueduck.deepwaters.registry.DeepWatersStructures;
import blueduck.deepwaters.utils.GeneralUtils;
import blueduck.deepwaters.client.renderer.entity.*;
import blueduck.deepwaters.entity.*;
import blueduck.deepwaters.item.ModdedSpawnEggItem;
import blueduck.deepwaters.event.SwordEventSubscriber;
import blueduck.deepwaters.utils.StructureUtils;
import blueduck.deepwaters.world.DeepWatersModDimension;
import blueduck.deepwaters.listeners.DeepWatersBiomeListener;
import blueduck.deepwaters.world.biome.SunkenWastesBiome;
import blueduck.deepwaters.world.biome.WaterBiomeBase;
import blueduck.deepwaters.world.gen.structures.DeepWatersStructureInit;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = DeepWatersMod.ModID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventSubscriber
{

	@ObjectHolder("deepwaters:deepwatersdimension")
	public static final ModDimension DeepWatersDimension = null;

	@SubscribeEvent
	public static void onDimensionRegistryEvent(final RegistryEvent.Register<ModDimension> event)
	{
		DeepWatersModDimension deepWatersModDimension = new DeepWatersModDimension();
		event.getRegistry().register(
				deepWatersModDimension.setRegistryName(GeneralUtils.Location("deepwatersdimension")));
	}


	@SubscribeEvent
	public static void onWorldCarverRegistryEvent(final RegistryEvent.Register<WorldCarver<?>> event)
	{
		DeepWatersBiomeListener.addCarversToBiomes();
	}

	@SubscribeEvent
	public static void onFeatureRegistryEvent(final RegistryEvent.Register<Feature<?>> event)
	{
		DeepWatersBiomeListener.addFeaturesToBiomes();
	}

	@SubscribeEvent
	public static void onServerInit(final FMLCommonSetupEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new SwordEventSubscriber());
		MinecraftForge.EVENT_BUS.register(new PlayerEventSubscriber());
		for (Biome biome : ForgeRegistries.BIOMES)
		{
			if (biome.getCategory() == Biome.Category.OCEAN && !(biome instanceof WaterBiomeBase))
			{
				StructureUtils.addStructure().accept(biome, DeepWatersStructures.PORTAL_STRUCTURE);
			}
			if(biome instanceof SunkenWastesBiome)
			{
				StructureUtils.addStructure().accept(biome, DeepWatersStructures.SUNKEN_SHIP);
			}
		}
	}

	@SubscribeEvent
	public static void onEntityRegisterEvent(final RegistryEvent.Register<EntityType<?>> event)
	{
		ModdedSpawnEggItem.initUnaddedEggs();
		DeepWatersBiomeListener.addSpawnsToBiomes();
	}


	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event){
		ScreenManager.registerFactory(DeepWatersContainerTypes.SURGE.get(), SurgeScreen::new);
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void doClientStuff(final FMLClientSetupEvent event)
	{
		RenderingRegistry.registerEntityRenderingHandler(DeepWatersEntities.KILLER_WIGGLER.get(),
				manager -> new WormRenderer(manager, new KillerWigglerHead(), new KillerWigglerBody(), new KillerWigglerTail(), KillerWiggler.class,
						new ResourceLocation("deepwaters" + ":textures/model/entity/killerwiggler.png"), true));
		RenderingRegistry.registerEntityRenderingHandler(DeepWatersEntities.BLUFFERFISH.get(),
				manager -> new BlufferFishRenderer(manager));
		RenderingRegistry.registerEntityRenderingHandler(DeepWatersEntities.STING_RAY.get(), manager -> new StingrayRenderer(manager));
		RenderingRegistry.registerEntityRenderingHandler(DeepWatersEntities.BABY_KRACKEN.get(),
				manager -> new BabyKrackenRenderer(manager));
		RenderingRegistry.registerEntityRenderingHandler(DeepWatersEntities.MUCK_GULPER.get(), manager -> new MuckGulperRenderer(manager));
		RenderingRegistry.registerEntityRenderingHandler(DeepWatersEntities.CORAL_CRAWLER.get(),
				manager -> new CoralCrawlerRenderer(manager));
		RenderingRegistry.registerEntityRenderingHandler(DeepWatersEntities.DONUT_FISH.get(), manager -> new DonutFishRenderer(manager));
		RenderingRegistry.registerEntityRenderingHandler(DeepWatersEntities.COLORFUL_FISH.get(),
				manager -> new ColorfulFishRenderer(manager));
		RenderingRegistry.registerEntityRenderingHandler(DeepWatersEntities.LEG_FISH.get(), manager -> new LegFishRenderer(manager));
		RenderingRegistry.registerEntityRenderingHandler(DeepWatersEntities.DEEP_GLIDER.get(), manager -> new DeepGliderRenderer(manager));
		RenderingRegistry.registerEntityRenderingHandler(DeepWatersEntities.SKULL_FISH.get(), manager -> new SkullFishRenderer(manager));
		RenderingRegistry.registerEntityRenderingHandler(DeepWatersEntities.SURGE.get(), manager -> new SurgeRenderer(manager));
		RenderingRegistry.registerEntityRenderingHandler(DeepWatersEntities.JUNGLE_FISH.get(), manager -> new JungleFishRenderer(manager));
		RenderingRegistry.registerEntityRenderingHandler(DeepWatersEntities.SEA_URCHIN.get(), manager -> new SeaUrchinRenderer(manager));
		RenderingRegistry.registerEntityRenderingHandler(DeepWatersEntities.SEA_ANGEL.get(), manager -> new SeaAngelRenderer(manager));
		RenderingRegistry.registerEntityRenderingHandler(DeepWatersEntities.SHARK.get(), manager -> new SharkRenderer(manager));
		RenderingRegistry.registerEntityRenderingHandler(DeepWatersEntities.JELLYFISH.get(), manager -> new JellyfishRenderer(manager));
		RenderingRegistry.registerEntityRenderingHandler(DeepWatersEntities.SNEAGLE.get(), manager -> new SneagleRenderer(manager));
		RenderingRegistry.registerEntityRenderingHandler(DeepWatersEntities.SUNKEN_WANDERER.get(), manager -> new SunkenWandererRenderer(manager));
		RenderingRegistry.registerEntityRenderingHandler(DeepWatersEntities.PHANTOM_STINGRAY.get(), manager -> new PhantomStingrayRenderer(manager));

//		try {
//			ClientRegistry.bindTileEntityRenderer(DeepWatersTileEntities.AQUASTONE_FAN.get(), new Function<TileEntityRendererDispatcher, TileEntityRenderer<? super TileEntity>>() {
//				@Override
//				public TileEntityRenderer<? super TileEntity> apply(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
//					return new aquafan(tileEntityRendererDispatcher);
//				}
//			});
//		} catch (Exception err) {}
	}

	@SubscribeEvent
	public static void onRegisterFeaturesEvent(final RegistryEvent.Register<Feature<?>> event)
	{
		Registry.register(Registry.STRUCTURE_PIECE, "portal_piece", DeepWatersStructureInit.PortalPieceType);
		Registry.register(Registry.STRUCTURE_PIECE, "sunken_ship", DeepWatersStructureInit.SunkenShipPieceType);

	}
}
