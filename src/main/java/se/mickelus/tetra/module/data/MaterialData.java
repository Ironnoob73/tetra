package se.mickelus.tetra.module.data;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.ResourceLocation;
import se.mickelus.tetra.module.schematic.OutcomeMaterial;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MaterialData {
    public boolean replace = false;

    /**
     * Innate attributes gained from the material
     */
    public Map<Attribute, AttributeModifier> attributes;

    /**
     * Multiplier for primary attributes, e.g. damage or similar
     */
    public float primary = 1;

    /**
     * Multiplier for secondary attributes, e.g. speed or similar
     */
    public float secondary = 1;

    /**
     * Multiplier for tertiary attributes, not sure which attributes this should map to but I want something more for modpacks/datapacks/compat. Try
     * to keep this balanced so that it can actually be used by modules
     */
    public float tertiary = 1;

    public float durability = 0;
    public int integrity = 0;

    public int magicCapacity = 0;

    /**
     * Innate effects gained from the material
     */
    public EffectData effects = new EffectData();

    /**
     * Multipliers for effects added by modules
     */
    public int effectTier = 0;
    public float effectEfficiency = 0;

    /**
     * Multipliers for tool capabilities added by modules
     */
    public int toolTier = 0;
    public float toolEfficiency = 0;

    public int glyphTint = 0xffffff;
    public int textureTint = 0xffffff;

    /**
     * An array of textures that this material would prefer to use, the first one that matches those available for the module will be used. If none
     * of the textures provided here matches one of the available textures for the module then the first available texture for the module will be used.
     */
    public ResourceLocation[] textures = {};

    /**
     * If true all variants of this material will use the first of it's provided textures rather than one from the textures available from the module.
     * Useful where a modded material should use it's own texture rather than one of the defaults, e.g. metals from twilight forest
     */
    public boolean textureOverride = false;

    public OutcomeMaterial material;
    public ToolData requiredTools = new ToolData();

    /**
     * Innate improvements for the material that should be applied if available, e.g. arrested for diamond
     */
    public Map<String, Integer> improvements;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Non-configurable stuff below
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final MaterialData defaultValues = new MaterialData();

    public static void copyFields(MaterialData from, MaterialData to) {

        if (from.textureOverride != defaultValues.textureOverride) {
            to.textureOverride = from.textureOverride;
        }


        to.textures = Stream.concat(Arrays.stream(to.textures), Arrays.stream(from.textures))
                .distinct()
                .toArray(ResourceLocation[]::new);

        if (from.improvements != null) {
            if (to.improvements != null) {
                Map<String, Integer> merged = new HashMap<>();
                merged.putAll(to.improvements);
                merged.putAll(from.improvements);
                to.improvements = merged;
            } else {
                to.improvements = from.improvements;
            }
        }
    }

    public MaterialData shallowCopy() {
        MaterialData copy = new MaterialData();
        copyFields(this, copy);

        return copy;
    }
}
