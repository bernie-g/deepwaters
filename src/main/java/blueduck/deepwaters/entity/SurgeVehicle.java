package blueduck.deepwaters.entity;


import blueduck.deepwaters.client.KeyboardHandler;
import blueduck.deepwaters.gui.surge.SurgeContainer;
import blueduck.deepwaters.registry.DeepWatersBlocks;
import blueduck.deepwaters.registry.DeepWatersItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.item.BlockItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class SurgeVehicle extends AbstractInventoryEntity
{

	private boolean forwardInputDown;
	private int lerpSteps;
	private double lerpX;
	private double lerpY;
	private double lerpZ;
	private double lerpYaw;
	private double lerpPitch;
	public int speedMultiplier;
	public int healthMultiplier;
	public int armorMultiplier;
	public int unknownMultiplier1;
	public int unknownMultiplier2;
	public int unknownMultiplier3;
	public PlayerEntity playerInteracted;

	private static final DataParameter<Float> BATTERY = EntityDataManager.createKey(SurgeVehicle.class, DataSerializers.FLOAT);


	@Override
	protected void registerGoals()
	{
		return;
	}

	public SurgeVehicle(EntityType<? extends WaterMobEntity> type, World worldIn)
	{
		super(type, worldIn);
	}

	@Override
	protected ItemStackHandler initInventory() {
		return new ItemStackHandler(27);
	}

	@Override
	protected void registerData()
	{
		super.registerData();
		this.dataManager.register(BATTERY, 100F);
	}

	public float getBattery()
	{
		return dataManager.get(BATTERY);
	}

	public void setBattery(float level)
	{
		this.dataManager.set(BATTERY, level);
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(5.0D);
	}

	private void resetMultipliers(){
		healthMultiplier = 1;
		armorMultiplier = 1;
		speedMultiplier = 1;
		unknownMultiplier1 = 1;
		unknownMultiplier2 = 1;
		unknownMultiplier3 = 1;
	}

	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
		return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}

	@Override
	public void tick()
	{
		Entity entity = this.getControllingPassenger();
		if (this.getControllingPassenger() != null)
		{
			PlayerEntity player = (PlayerEntity) entity;
            Minecraft mc = Minecraft.getInstance();
			Vec3d lookVec = entity.getLookVec();

			resetMultipliers();
			for(int i = 5;i < 8;i++){
				healthMultiplier = this.inventory.getStackInSlot(i).getItem() == BlockItem.BLOCK_TO_ITEM.get(DeepWatersBlocks.RED_FORGE_STONE.get()) ? healthMultiplier *= 2 : healthMultiplier;
				armorMultiplier = this.inventory.getStackInSlot(i).getItem() == BlockItem.BLOCK_TO_ITEM.get(DeepWatersBlocks.BLUE_FORGE_STONE.get()) ? armorMultiplier *= 2 : armorMultiplier;
				speedMultiplier = this.inventory.getStackInSlot(i).getItem() == BlockItem.BLOCK_TO_ITEM.get(DeepWatersBlocks.GREEN_FORGE_STONE.get()) ? speedMultiplier *= 2 : speedMultiplier;
				unknownMultiplier1 = this.inventory.getStackInSlot(i).getItem() == BlockItem.BLOCK_TO_ITEM.get(DeepWatersBlocks.ORANGE_FORGE_STONE.get()) ? unknownMultiplier1 *= 2 : unknownMultiplier1;
				unknownMultiplier2 = this.inventory.getStackInSlot(i).getItem() == BlockItem.BLOCK_TO_ITEM.get(DeepWatersBlocks.PURPLE_FORGE_STONE.get()) ? unknownMultiplier2 *= 2 : unknownMultiplier2;
				unknownMultiplier3 = this.inventory.getStackInSlot(i).getItem() == BlockItem.BLOCK_TO_ITEM.get(DeepWatersBlocks.YELLOW_FORGE_STONE.get()) ? unknownMultiplier3 *= 2 : unknownMultiplier3;
			}

            this.addPotionEffect(new EffectInstance(Effects.HEALTH_BOOST, 1, healthMultiplier, false, false, false));
            this.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 1, armorMultiplier, false, false, false));

			if(this.inventory.getStackInSlot(17).getItem() == DeepWatersItems.POWER_STONE.get()){
				//to be implemented
			}

			if (this.inWater && KeyboardHandler.isForwardKeyDown)
			{
				if(getBattery() > 0.000){
					setBattery(getBattery() - 0.02F);
					this.setMotion(this.getMotion().add(lookVec.x / 13 * speedMultiplier, lookVec.y / 13 * speedMultiplier, lookVec.z / 13 * speedMultiplier));
				}
				System.out.println(getBattery() + " : " + this.getMotion().getX() + "," + this.getMotion().getY() + "," + this.getMotion().getZ());
			}

			Vec3i directionVec = entity.getHorizontalFacing().getDirectionVec();
			this.prevRotationYawHead = player.prevRotationYawHead;
			this.rotationYaw = entity.getRotationYawHead();
			this.setRotationYawHead(entity.getRotationYawHead());
			float pitch = entity.getPitch(1);
			//this.rotationPitch = pitch;
		}
		super.tick();
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	@Override
	public void livingTick()
	{
		//this.tickLerp();
		super.livingTick();
	}


	private void tickLerp()
	{
		if (this.lerpSteps > 0 && !this.canPassengerSteer())
		{
			double d0 = this.getPosX() + (this.lerpX - this.getPosX()) / (double) this.lerpSteps;
			double d1 = this.getPosY() + (this.lerpY - this.getPosY()) / (double) this.lerpSteps;
			double d2 = this.getPosZ() + (this.lerpZ - this.getPosZ()) / (double) this.lerpSteps;
			double d3 = MathHelper.wrapDegrees(this.lerpYaw - (double) this.rotationYaw);
			this.rotationYaw = (float) ((double) this.rotationYaw + d3 / (double) this.lerpSteps);
			this.rotationPitch = (float) ((double) this.rotationPitch + (this.lerpPitch - (double) this.rotationPitch) / (double) this.lerpSteps);
			--this.lerpSteps;
			this.setPosition(d0, d1, d2);
			this.setRotation(this.rotationYaw, this.rotationPitch);
		}
	}

	public void lerp(float prevRotationYaw, float rotationYaw, float partialTicks)
	{

	}

	public static void KeyInputEvent(final InputEvent.KeyInputEvent event)
	{
	}

	protected void mountTo(PlayerEntity player)
	{
		if (!this.world.isRemote)
		{
			player.rotationYaw = this.rotationYaw;
			player.rotationPitch = this.rotationPitch;
			player.startRiding(this);
		}
	}

	/**
	 * Returns the Y offset from the entity's position for any entity riding this one.
	 */
	@Override
	public double getMountedYOffset()
	{
		return -0.2F;
	}

	@Override
	public void updatePassenger(Entity passenger)
	{
		Entity controllingPassenger = this.getControllingPassenger();
		controllingPassenger.setPosition(this.getPosX(), this.getPosY() + this.getMountedYOffset() + controllingPassenger.getYOffset(), this.getPosZ());
	}

	@Override
	protected boolean processInteract(PlayerEntity player, Hand hand)
	{
		playerInteracted = player;
		World world = player.getEntityWorld();
		if(!world.isRemote){
			if (player.isSneaking()) {
				for(int i = 0; i < this.inventory.getSlots();i++){
					System.out.println(this.inventory.getStackInSlot(i).getItem() + " : " + i);
				}
				player.openContainer(new SimpleNamedContainerProvider((id, inv, plyr) -> new SurgeContainer(id, inv, this), this.getDisplayName()));
//				SimpleNamedContainerProvider containerProvider = new SimpleNamedContainerProvider((id, inv, plyr) -> {
//					return new SurgeContainer(id, inv, this);
//				}, this.getDisplayName());
//				NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider);
			} else {
				mountTo(player);
			}
		}
		return true;
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
			for (int i = 0; i < this.inventory.getSlots(); i++) {
				InventoryHelper.spawnItemStack(this.world, this.getPosX(), this.getPosY(), this.getPosZ(), this.inventory.getStackInSlot(i));
			}
		}
	}

	@Override
	protected void onDeathUpdate() {
		super.onDeathUpdate();
	}

	public void openContainer(final PlayerEntity player) {
		player.openContainer(new SimpleNamedContainerProvider((id, inv, plyr) -> new SurgeContainer(id, inv, this), this.getDisplayName()));
	}

	@Override
	public boolean isPushedByWater()
	{
		return false;
	}


	@Nullable
	public Entity getControllingPassenger()
	{
		return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
	}

	public boolean canBeSteered()
	{
		return this.getControllingPassenger() instanceof LivingEntity;
	}

	public void writeAdditional(CompoundNBT p_213281_1_)
	{
		super.writeAdditional(p_213281_1_);
		p_213281_1_.putFloat("battery", getBattery());
	}

	@Override
	public void readAdditional(CompoundNBT p_70037_1_)
	{
		super.readAdditional(p_70037_1_);
		setBattery(p_70037_1_.getFloat("battery"));
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt)
	{
		readAdditional(nbt);
	}

	@Override
	public CompoundNBT serializeNBT()
	{
		CompoundNBT nbt = new CompoundNBT();
		writeAdditional(nbt);
		return nbt;
	}


}