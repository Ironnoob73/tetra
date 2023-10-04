package se.mickelus.tetra;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import se.mickelus.tetra.items.duplex_tool.ItemDuplexToolModular;
import se.mickelus.tetra.items.sword.ItemSwordModular;

@Config(modid = TetraMod.MOD_ID, name = TetraMod.MOD_ID + "/" + TetraMod.MOD_ID)
public class ConfigHandler {

    public static boolean development = false;

    public static boolean geodeGenerate = true;

    public static boolean generateFeatures = true;
    public static int maxFeatureDepth = 8;

    public static boolean workbenchDropTable = true;

    public static boolean moduleProgression = true;
    public static int settleLimitBase = 200;
    public static float settleLimitLevelMultiplier = 3;
    public static float settleLimitDurabilityMultiplier = 0.5f;

    public static int honeSwordBase = 150;
    public static int honeSwordIntegrityMultiplier = 120;

    public static int honeDuplexBase = 260;
    public static int honeDuplexIntegrityMultiplier = 180;
    public static int honeSimplexBase = 260;
    public static int honeSimplexIntegrityMultiplier = 180;

    @Mod.EventBusSubscriber
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(TetraMod.MOD_ID)) {
                ConfigManager.sync(TetraMod.MOD_ID, Config.Type.INSTANCE);

                ItemDuplexToolModular.instance.updateConfig(honeDuplexBase, honeDuplexIntegrityMultiplier);
                ItemSwordModular.instance.updateConfig(honeSwordBase, honeSwordIntegrityMultiplier);
                ItemDuplexToolModular.instance.updateConfig(honeSimplexBase, honeSimplexIntegrityMultiplier);
            }
        }
    }
}
