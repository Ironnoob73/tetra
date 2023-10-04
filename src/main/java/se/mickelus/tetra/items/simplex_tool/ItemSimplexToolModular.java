package se.mickelus.tetra.items.simplex_tool;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.registry.GameRegistry;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.TetraMod;
import se.mickelus.tetra.items.BasicMajorModule;
import se.mickelus.tetra.items.BasicModule;
import se.mickelus.tetra.items.ItemModularHandheld;
import se.mickelus.tetra.module.ItemModuleMajor;
import se.mickelus.tetra.module.ItemUpgradeRegistry;
import se.mickelus.tetra.module.Priority;
import se.mickelus.tetra.module.schema.BookEnchantSchema;
import se.mickelus.tetra.module.schema.RemoveSchema;
import se.mickelus.tetra.module.schema.RepairSchema;
import se.mickelus.tetra.network.PacketHandler;

public class ItemSimplexToolModular extends ItemModularHandheld {

    public final static String headKey = "simplex/head";
    public final static String handleKey = "simplex/handle";

    public final static String bindingKey = "simplex/binding";

    static final String unlocalizedName = "simplex_tool_modular";

    private final ItemModuleMajor basicShovelModule;
    private final ItemModuleMajor handleModule;

    @GameRegistry.ObjectHolder(TetraMod.MOD_ID + ":" + unlocalizedName)
    public static ItemSimplexToolModular instance;

    public ItemSimplexToolModular() {
        setUnlocalizedName(unlocalizedName);
        setRegistryName(unlocalizedName);
        setMaxStackSize(1);

        blockDestroyDamage = 2;

        majorModuleKeys = new String[] { headKey, handleKey };
        minorModuleKeys = new String[] { bindingKey };

        requiredModules = new String[] { headKey, handleKey };

        basicShovelModule = new BasicMajorModule(headKey, "simplex/basic_shovel",
                "simplex/improvements/basic_shovel",
                "simplex/improvements/shared_head_hone",
                "simplex/improvements/basic_shovel",
                "settling_improvements",
                "destabilization_improvements");

        handleModule = new BasicMajorModule(handleKey, "simplex/basic_handle",
                "simplex/improvements/basic_handle",
                "simplex/improvements/basic_handle_hone",
                "simplex/improvements/shared_head_hone",
                "settling_improvements",
                "destabilization_improvements")
                .withRenderLayer(Priority.LOWER);

        new BasicModule(bindingKey, "simplex/binding", "simplex/tweaks/binding");
        new BasicModule(bindingKey, "simplex/socket");


        updateConfig(ConfigHandler.honeSimplexBase, ConfigHandler.honeSimplexIntegrityMultiplier);
    }

    @Override
    public void init(PacketHandler packetHandler) {
        ItemUpgradeRegistry.instance.registerConfigSchema("simplex/basic_shovel");
        ItemUpgradeRegistry.instance.registerConfigSchema("simplex/basic_shovel_improvements");
        new BookEnchantSchema(basicShovelModule);

        ItemUpgradeRegistry.instance.registerConfigSchema("simplex/basic_handle");
        ItemUpgradeRegistry.instance.registerConfigSchema("simplex/basic_handle_improvements");
        new BookEnchantSchema(handleModule);

        ItemUpgradeRegistry.instance.registerConfigSchema("simplex/shared_head_hone");

        ItemUpgradeRegistry.instance.registerConfigSchema("simplex/binding");
        ItemUpgradeRegistry.instance.registerConfigSchema("simplex/socket");

        new RepairSchema(this);
        RemoveSchema.registerRemoveSchemas(this);

        ItemUpgradeRegistry.instance.registerReplacementDefinition("sword");
    }

    public void updateConfig(int honeBase, int honeIntegrityMultiplier) {
        this.honeBase = honeBase;
        this.honeIntegrityMultiplier = honeIntegrityMultiplier;
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockState) {
        return blockState.getBlock() == Blocks.WEB;
    }
}
