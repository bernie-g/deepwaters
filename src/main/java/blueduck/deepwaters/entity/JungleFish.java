package blueduck.deepwaters.entity;

import blueduck.deepwaters.registry.DeepWatersItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.fish.AbstractGroupFishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class JungleFish extends AbstractGroupFishEntity
{
	public JungleFish(EntityType<? extends AbstractGroupFishEntity> type, World worldIn)
	{
		super(type, worldIn);
	}

	@Override
	protected ItemStack getFishBucket()
	{
		return new ItemStack(DeepWatersItems.JUNGLEFISH_ITEM_BUCKET.get());
	}

	@Override
	protected SoundEvent getFlopSound()
	{
		return null;
	}

	protected void registerAttributes()
	{
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
		this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
	}
}
