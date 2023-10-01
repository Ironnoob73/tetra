package se.mickelus.tetra.blocks.workbench;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.advancements.BlockUseCriterion;
import se.mickelus.tetra.items.TetraCreativeTabs;

import java.util.Random;

public class BlockWoodenWorkbench extends BlockNewWorkbenchBase {

    static final String unlocalizedName = "wooden_workbench";

    public static BlockWoodenWorkbench instance;

    public BlockWoodenWorkbench() {
        super(Material.WOOD);

        setRegistryName(unlocalizedName);
        setUnlocalizedName(unlocalizedName);
        setCreativeTab(TetraCreativeTabs.getInstance());

        hasItem = true;

        instance = this;

    }



    public static EnumActionResult upgradeWorkbench(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing) {
        ItemStack itemStack = player.getHeldItem(hand);
        if (!player.canPlayerEdit(pos.offset(facing), facing, itemStack)) {
            return EnumActionResult.FAIL;
        }
        world.playSound(player, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, 0.5F);

        if (!world.isRemote) {
            world.setBlockState(pos, instance.getDefaultState());

            BlockUseCriterion.trigger((EntityPlayerMP) player, instance.getDefaultState(), ItemStack.EMPTY);
        }
        return EnumActionResult.SUCCESS;
    }
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        if (ConfigHandler.workbenchDropTable) {
            return Blocks.CRAFTING_TABLE.getItemDropped(Blocks.CRAFTING_TABLE.getDefaultState(), rand, fortune);
        } else {
            return super.getItemDropped(state, rand, fortune);
        }
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return true;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.SOLID;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return true;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FULL_BLOCK_AABB;
    }

    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        return lightOpacity;
    }

    @Override
    public Material getMaterial(IBlockState state) {
        return Material.WOOD;
    }

    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return 2.5f;
    }

}
