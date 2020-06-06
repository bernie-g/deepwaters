package bernie.software;

import bernie.software.registry.DeepWatersBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
@Mod.EventBusSubscriber(modid = DeepWatersMod.ModID, value = CLIENT)
public class ClientEvents {

    private static void render(Supplier<? extends Block> block, RenderType render) {
        RenderTypeLookup.setRenderLayer(block.get(), render);
    }

    public static void registerBlockRenderers() {
        RenderType cutout = RenderType.getCutout();
        RenderType translucent = RenderType.getTranslucent();

        render(DeepWatersBlocks.AQUA_STONE, cutout);
        render(DeepWatersBlocks.AQUA_TORCH, cutout);
        render(DeepWatersBlocks.AQUA_TORCH_WALL, cutout);
        render(DeepWatersBlocks.AQUA_REPEATER, cutout);
        render(DeepWatersBlocks.AQUA_COMPARE, cutout);
        render(DeepWatersBlocks.SCRAP_DOOR, cutout);
        render(DeepWatersBlocks.DEADWOOD_DOOR, cutout);
        render(DeepWatersBlocks.DEADWOOD_TRAPDOOR, cutout);
        render(DeepWatersBlocks.SCRAP_LADDER, cutout);
        render(DeepWatersBlocks.IRON_HATCH, cutout);

        render(DeepWatersBlocks.BLUE_FORGE_STONE, cutout);
        render(DeepWatersBlocks.GREEN_FORGE_STONE, cutout);
        render(DeepWatersBlocks.ORANGE_FORGE_STONE, cutout);
        render(DeepWatersBlocks.PURPLE_FORGE_STONE, cutout);
        render(DeepWatersBlocks.RED_FORGE_STONE, cutout);
        render(DeepWatersBlocks.YELLOW_FORGE_STONE, cutout);

    }
}
