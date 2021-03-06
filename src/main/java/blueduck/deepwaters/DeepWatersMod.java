package blueduck.deepwaters;

import blueduck.deepwaters.IMC.DynamicWeaponry.ConfigIntegration;
import blueduck.deepwaters.block.aquastone.AquastoneColor;
import blueduck.deepwaters.client.ClientEvents;
import blueduck.deepwaters.datagen.DeepWatersBlockStates;
import blueduck.deepwaters.datagen.DeepWatersItemModels;
import blueduck.deepwaters.datagen.DeepWatersLootTables;
import blueduck.deepwaters.datagen.DeepWatersRecipes;
import blueduck.deepwaters.registry.DeepWatersContainerTypes;
import blueduck.deepwaters.gui.surge.OpenSurgeGuiPacket;
import blueduck.deepwaters.registry.*;
import blueduck.deepwaters.utils.network.NetBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.File;

@Mod(DeepWatersMod.ModID)
public class DeepWatersMod
{
	public static Logger logger;
	public static final String ModID = "deepwaters";

	public static final SimpleChannel CHANNEL = new NetBuilder(new ResourceLocation(ModID, "main"))
			.version(1).optionalServer().requiredClient()
			.serverbound(OpenSurgeGuiPacket::new).consumer(() -> OpenSurgeGuiPacket::handle)
			.build();

	static File dynamicWeaponryConfigPath;

	public DeepWatersMod()
	{
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		logger = LogManager.getLogger();

		bus.addListener(this::setup);
		bus.addListener(this::gatherData);
		if (Minecraft.getInstance().gameDir!=null)
		{
			if (ModList.get().isLoaded("dynamic_weaponry"))
			{
				dynamicWeaponryConfigPath = new File(Minecraft.getInstance().gameDir + "\\config\\dynamic_weaponry");

			}

			bus.addListener(this::clientSetup);

		} else
		{
			bus.addListener(this::serverSetup);

		}

		DeepWatersBiomes.BIOMES.register(bus);
		DeepWatersBlocks.BLOCKS.register(bus);
		DeepWatersItems.ITEMS.register(bus);
		DeepWatersEntities.ENTITIES.register(bus);
		DeepWatersWorldCarvers.WORLD_CARVERS.register(bus);
		DeepWatersStructures.STRUCTURES.register(bus);
		DeepWatersContainerTypes.CONTAINER_TYPES.register(bus);
		DeepWatersTileEntities.TILE_ENTITIES.register(bus);


		if (ModList.get().isLoaded("dynamic_weaponry"))
		{
			ConfigIntegration.Register(bus);

		}

	}

	@OnlyIn(Dist.CLIENT)
	private void clientSetup(FMLClientSetupEvent event)
	{
		Minecraft.getInstance().getBlockColors().register(new AquastoneColor(), DeepWatersBlocks.AQUA_STONE.get());
		ClientEvents.registerBlockRenderers();
		ClientEvents.registerTESRs();
		if (ModList.get().isLoaded("dynamic_weaponry"))
		{
			ConfigIntegration.GenConfig(dynamicWeaponryConfigPath);

		}

	}

	private void serverSetup(FMLDedicatedServerSetupEvent event)
	{
		if (ModList.get().isLoaded("dynamic_weaponry"))
		{
			dynamicWeaponryConfigPath = new File(event.getServerSupplier().get().getDataDirectory() + "\\config\\dynamic_weaponry");
			ConfigIntegration.GenConfig(dynamicWeaponryConfigPath);

		}

	}

	private void setup(FMLCommonSetupEvent event)
	{
		DeepWatersBiomes.addBiomeTypes();
		DeepWatersEntities.spawnPlacements();

	}


	private void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();

		generator.addProvider(new DeepWatersRecipes(generator));
		generator.addProvider(new DeepWatersBlockStates(generator, event.getExistingFileHelper()));
		generator.addProvider(new DeepWatersItemModels(generator, event.getExistingFileHelper()));
		generator.addProvider(new DeepWatersLootTables(generator));

	}

}