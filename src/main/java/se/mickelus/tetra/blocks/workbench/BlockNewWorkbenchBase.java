package se.mickelus.tetra.blocks.workbench;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;
import se.mickelus.tetra.TetraMod;
import se.mickelus.tetra.blocks.ITetraBlock;
import se.mickelus.tetra.blocks.TetraBlock;
import se.mickelus.tetra.blocks.hammer.BlockHammerHead;
import se.mickelus.tetra.blocks.workbench.action.ConfigActionImpl;
import se.mickelus.tetra.blocks.workbench.action.WorkbenchActionPacket;
import se.mickelus.tetra.capabilities.Capability;
import se.mickelus.tetra.data.DataHandler;
import se.mickelus.tetra.items.TetraCreativeTabs;
import se.mickelus.tetra.network.GuiHandlerRegistry;
import se.mickelus.tetra.network.PacketHandler;

import java.util.Collection;
import java.util.Collections;
//Special thanks: bread_nicecat, mouse
public abstract class BlockNewWorkbenchBase extends TetraBlock implements ITileEntityProvider {
    static final String unlocalizedName = "workbench";
    public BlockNewWorkbenchBase(Material material) {
        super(material);
        GameRegistry.registerTileEntity(TileEntityWorkbench.class, new ResourceLocation(TetraMod.MOD_ID, unlocalizedName));
    }
    /**
     * Special item regististration to handle multiple variants registered in the creative menu.
     * @param registry Item registry
     */
    @Override
    public void registerItem(IForgeRegistry<Item> registry) {
        Item item = new ItemBlock(this) {
        };
        item.setRegistryName(getRegistryName());
        registry.register(item);
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            for (BlockWorkbench.Variant variant : BlockWorkbench.Variant.values()) {
                ModelLoader.setCustomModelResourceLocation(item, variant.ordinal(), new ModelResourceLocation(getRegistryName(), "variant=" + variant.toString()));
            }
        }
    }
    @Override
    public void getSubBlocks(CreativeTabs creativeTabs, NonNullList<ItemStack> items) {
        if (TetraCreativeTabs.getInstance().equals(creativeTabs)) {
            items.add(new ItemStack(this, 1));
        }
    }
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityWorkbench();
    }
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        player.openGui(TetraMod.instance, GuiHandlerWorkbench.workbenchId, world, pos.getX(), pos.getY(), pos.getZ());

        return true;
    }
    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tileentity = world.getTileEntity(pos);

        if (tileentity instanceof IInventory) {
            InventoryHelper.dropInventoryItems(world, pos, (IInventory) tileentity);
        }
    }


    @Override
    public Collection<Capability> getCapabilities(World world, BlockPos pos, IBlockState blockState) {
        IBlockState accessoryBlockState = world.getBlockState(pos.offset(EnumFacing.UP));
        if (accessoryBlockState.getBlock() instanceof ITetraBlock) {
            ITetraBlock block = (ITetraBlock) accessoryBlockState.getBlock();
            return block.getCapabilities(world, pos.offset(EnumFacing.UP), accessoryBlockState);
        }
        return Collections.emptyList();
    }

    @Override
    public int getCapabilityLevel(World world, BlockPos pos, IBlockState blockState, Capability capability) {
        IBlockState accessoryBlockState = world.getBlockState(pos.offset(EnumFacing.UP));
        if (accessoryBlockState.getBlock() instanceof ITetraBlock) {
            ITetraBlock block = (ITetraBlock) accessoryBlockState.getBlock();
            return block.getCapabilityLevel(world, pos.offset(EnumFacing.UP), accessoryBlockState, capability);
        }
        return -1;
    }

    @Override
    public ItemStack onCraftConsumeCapability(World world, BlockPos pos, IBlockState blockState, ItemStack targetStack, EntityPlayer player, boolean consumeResources) {
        BlockPos topPos = pos.offset(EnumFacing.UP);
        if (world.getBlockState(topPos).getBlock() instanceof BlockHammerHead) {
            BlockHammerHead hammer = (BlockHammerHead) world.getBlockState(topPos).getBlock();
            return hammer.onCraftConsumeCapability(world, topPos, world.getBlockState(topPos), targetStack, player, consumeResources);
        }
        return targetStack;
    }

    @Override
    public ItemStack onActionConsumeCapability(World world, BlockPos pos, IBlockState blockState, ItemStack targetStack, EntityPlayer player, boolean consumeResources) {
        BlockPos topPos = pos.offset(EnumFacing.UP);
        if (world.getBlockState(topPos).getBlock() instanceof BlockHammerHead) {
            BlockHammerHead hammer = (BlockHammerHead) world.getBlockState(topPos).getBlock();
            return hammer.onActionConsumeCapability(world, topPos, world.getBlockState(topPos), targetStack, player, consumeResources);
        }
        return targetStack;
    }

    @Override
    public void init(PacketHandler packetHandler) {
        super.init(packetHandler);

        TileEntityWorkbench.initConfigActions(DataHandler.instance.getData("actions", ConfigActionImpl[].class));

        GuiHandlerRegistry.instance.registerHandler(GuiHandlerWorkbench.workbenchId, new GuiHandlerWorkbench());
        PacketHandler.instance.registerPacket(UpdateWorkbenchPacket.class, Side.SERVER);
        PacketHandler.instance.registerPacket(CraftWorkbenchPacket.class, Side.SERVER);
        PacketHandler.instance.registerPacket(WorkbenchActionPacket.class, Side.SERVER);
        PacketHandler.instance.registerPacket(TweakWorkbenchPacket.class, Side.SERVER);
    }
    @Override
    public boolean causesSuffocation(IBlockState state) {
        return isFullCube(state);
    }

}
