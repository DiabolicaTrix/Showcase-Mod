package xyz.diabolicatrixlab.showcasemod.block;

import java.util.UUID;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.apiimpl.styles.TextStyle;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.server.permission.PermissionAPI;
import xyz.diabolicatrixlab.showcasemod.ItemRegistry;
import xyz.diabolicatrixlab.showcasemod.ShowcaseMod;
import xyz.diabolicatrixlab.showcasemod.compat.top.ITOPInfoProvider;
import xyz.diabolicatrixlab.showcasemod.item.ItemShowcaseModifier;
import xyz.diabolicatrixlab.showcasemod.renderer.RendererShowcase;
import xyz.diabolicatrixlab.showcasemod.tileentity.TileEntityShowcase;
import xyz.diabolicatrixlab.showcasemod.util.InventoryUtil;
import xyz.diabolicatrixlab.showcasemod.util.PlayerUtil;

/**
 * Author: DiabolicaTrix
 * Date: 2017-01-19
 */
public class BlockShowcase extends Block implements ITileEntityProvider, ITOPInfoProvider
{
	public static final int GUI_ID = 0;
	 
    public BlockShowcase(Material material)
    {
        super(material);
        this.setCreativeTab(ShowcaseMod.showcaseTab);
        this.setHardness(1.0f);
        this.setResistance(999.0f);
        this.setRegistryName("showcase_block");
        this.setUnlocalizedName(this.getRegistryName().toString());
    }
    
    @SideOnly(Side.CLIENT)
    public void registerModel(){
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    	ClientRegistry.bindTileEntitySpecialRenderer(TileEntityShowcase.class, new RendererShowcase());
    }
    
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public boolean isFullCube(IBlockState state)
    {
        return true;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState blockState) {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state) {
    	return false;
    }
    
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    	TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof TileEntityShowcase){
			IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			InventoryUtil.dropInventoryItems(worldIn, pos, itemHandler);
		}
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
    		EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    	if(!worldIn.isRemote)
    	{
    		TileEntity te = worldIn.getTileEntity(pos);
    		if(te instanceof TileEntityShowcase){
    			if(playerIn.getHeldItemMainhand().getItem() instanceof ItemShowcaseModifier && PermissionAPI.hasPermission(playerIn, "showcasemod.shops.edit")){
					ItemStack stack = playerIn.getHeldItemMainhand();
					NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
					int mode = nbt.hasKey("Mode") ? nbt.getInteger("Mode") : 0;
					if(mode == 1)
					{
						((TileEntityShowcase) te).toggleAdminShop(playerIn);
						return true;
					}
				}
    			playerIn.openGui(ShowcaseMod.instance, GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
    			return true;
    		}
    		return false;
    	}
    	return true;
    }
    
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
    		ItemStack stack) {
    	if(!worldIn.isRemote && placer instanceof EntityPlayer)
    	{
    		UUID uid = PlayerUtil.getUniqueID((EntityPlayer) placer);
    		TileEntityShowcase te = (TileEntityShowcase) worldIn.getTileEntity(pos);
    		te.setOwner(uid);
    	}
    }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityShowcase();
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
		if (world.getTileEntity(data.getPos()) instanceof TileEntity) {
            TileEntityShowcase te = (TileEntityShowcase) world.getTileEntity(data.getPos());
            if(te.getItemStack() != ItemStack.EMPTY){
	            ItemStack coinStack = new ItemStack(te.coinId == 0 ? ItemRegistry.copperCoin : te.coinId == 1 ? ItemRegistry.silverCoin : ItemRegistry.goldCoin, te.price);
	            ItemStack stack = te.getItemStack();
	            probeInfo.horizontal().text(IProbeInfo.STARTLOC + "showcasemod.message.selling" + IProbeInfo.ENDLOC).item(stack).text(IProbeInfo.STARTLOC + "showcasemod.message.for" + IProbeInfo.ENDLOC).item(coinStack);
	        }
		}
	}

}
