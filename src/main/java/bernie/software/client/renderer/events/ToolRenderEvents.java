package bernie.software.client.renderer.events;

import bernie.software.item.DeepWatersAbstractRuneItem;
import bernie.software.item.tool.DeepWatersShieldItem;
import bernie.software.utils.render.RenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Date;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ToolRenderEvents
{
	@SubscribeEvent
	public static void onRenderItem(RenderItemEvent event)
	{
		try
		{
			if (event.getItem().getItem() instanceof DeepWatersShieldItem && event instanceof RenderItemEvent.Gui.Post)
			{
				Long time = new Date().getTime();
				Long cooldown = (event.getItem().getTag().getLong("COOLDOWN") - (time));
				if (cooldown >= 0)
				{
					RenderSystem.disableTexture();
					RenderSystem.disableLighting();
					RenderSystem.pushMatrix();
					RenderSystem.depthMask(false);
					RenderSystem.disableCull();
					RenderSystem.disableDepthTest();
					RenderSystem.translatef(-0.39f, -0.2f, 0.5f);
					RenderSystem.rotatef(41f, 0, 1, 0);
					RenderHelper.drawRect(0, -0.05, 1.08, 0.1, 0, 0, 0, 1);
					RenderHelper.drawRect(0, 0, 1.08, 0.1, 0.25, 0.5, 1, 1);
					RenderHelper.drawRect(0, 0, ((((float) Math.abs(cooldown) / DeepWatersShieldItem.getEvent(
							(DeepWatersShieldItem) event.getItem().getItem()).cooldown())) + 0.08), 0.1, 0.2, 0.1, 0.1,
							1);
					RenderSystem.popMatrix();
					RenderSystem.enableLighting();
					RenderSystem.enableCull();
					RenderSystem.enableDepthTest();
					RenderSystem.depthMask(true);
					RenderSystem.enableTexture();
				}
			}
			else if (event.getItem().getItem() instanceof DeepWatersAbstractRuneItem)
			{
//				GlStateManager.disableTexture();
				RenderSystem.pushMatrix();
				RenderSystem.disableLighting();
				RenderSystem.translatef(-0.405f, 0.275f, 0);
				RenderSystem.rotatef(180, 1, 0, 0);
				RenderSystem.rotatef(45, 0, 1, 0);
				RenderSystem.rotatef(22.5f, 1, 0, 1);
//				GlStateManager.enableLighting();
				RenderSystem.scalef(0.6f, 0.64f, 0.6f);
				RenderSystem.enableTexture();
//				RenderHelper.drawRect(0,0,1,1,0.9,0.9,0.9,1);
				Minecraft.getInstance().getTextureManager().bindTexture(
						new ResourceLocation("deepwaters:textures/block/limestone.png"));
				try
				{
					if (event.getItem().getTag().contains("north"))
					{
						Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation(
								"deepwaters:textures/block/" + event.getItem().getTag().getString("north") + ".png"));
					}
				}
				catch (Exception err)
				{
				}
				RenderHelper.drawTexturedRect(0, 0, 0, 0, 256, 256, 1, 1, 0.9, 0.9, 0.9, 1);
//				GlStateManager.disableTexture();
				RenderSystem.rotatef(-90f, 0, 1, 0);
				RenderSystem.translatef(0, 0, -1);
				try
				{
					if (event.getItem().getTag().contains("east"))
					{
						Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation(
								"deepwaters:textures/block/" + event.getItem().getTag().getString("east") + ".png"));
					}
				}
				catch (Exception err)
				{
				}
				RenderHelper.drawTexturedRect(0, 0, 0, 0, 256, 256, 1, 1, 0.7, 0.7, 0.7, 1);
				RenderSystem.rotatef(-90f, 1, 0, 0);
				RenderSystem.translatef(0, -1, 0);
//				GlStateManager.disableLighting();
				try
				{
					if (event.getItem().getTag().contains("topbottom"))
					{
						Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation(
								"deepwaters:textures/block/" + event.getItem().getTag().getString(
										"topbottom") + ".png"));
					}
				}
				catch (Exception err)
				{
				}
				RenderHelper.drawTexturedRect(0, 0, 0, 0, 256, 256, 1, 1, 1, 1, 1, 1);
//				GlStateManager.disableLighting();
				RenderSystem.popMatrix();
				RenderSystem.enableLighting();
//				GlStateManager.enableTexture();
			}
		}
		catch (Exception err)
		{
		}
	}
}
