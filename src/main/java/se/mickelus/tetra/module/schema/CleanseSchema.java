package se.mickelus.tetra.module.schema;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.ArrayUtils;
import se.mickelus.tetra.capabilities.Capability;
import se.mickelus.tetra.items.ItemModular;
import se.mickelus.tetra.module.ItemModuleMajor;
import se.mickelus.tetra.module.ItemUpgradeRegistry;
import se.mickelus.tetra.module.data.GlyphData;
import se.mickelus.tetra.module.improvement.DestabilizationEffect;
import se.mickelus.tetra.util.CastOptional;

import java.util.*;
import java.util.stream.Stream;

public class CleanseSchema implements UpgradeSchema {
    private static final String key = "cleanse_schema";

    private static final String nameSuffix = ".name";
    private static final String descriptionSuffix = ".description";
    private static final String slotLabel = "item.dyePowder.blue.name";

    private GlyphData glyph = new GlyphData("textures/gui/workbench.png", 80, 32);

    public CleanseSchema() {

        ItemUpgradeRegistry.instance.registerSchema(this);
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getName() {
        return I18n.format(key + nameSuffix);
    }

    @Override
    public String getDescription(ItemStack itemStack) {
        return I18n.format(key + descriptionSuffix);
    }

    @Override
    public int getNumMaterialSlots() {
        return 1;
    }

    @Override
    public String getSlotName(final ItemStack itemStack, final int index) {
        return I18n.format(slotLabel);
    }

    @Override
    public int getRequiredQuantity(ItemStack itemStack, int index, ItemStack materialStack) {
        return 1;
    }

    @Override
    public boolean acceptsMaterial(ItemStack itemStack, int index, ItemStack materialStack) {
        return Items.DYE.equals(materialStack.getItem()) && materialStack.getItemDamage() == EnumDyeColor.BLUE.getDyeDamage();
    }

    @Override
    public boolean isMaterialsValid(ItemStack itemStack, ItemStack[] materials) {
        return acceptsMaterial(itemStack, 0, materials[0]);
    }

    @Override
    public boolean isApplicableForItem(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean isApplicableForSlot(String slot, ItemStack targetStack) {
        String[] destabilizationKeys = DestabilizationEffect.getKeys();

        return CastOptional.cast(targetStack.getItem(), ItemModular.class)
                .map(item -> item.getModuleFromSlot(targetStack, slot))
                .filter(module -> module instanceof ItemModuleMajor)
                .map(module -> (ItemModuleMajor) module)
                .map(module -> Arrays.stream(module.getImprovements(targetStack)))
                .orElse(Stream.empty())
                .anyMatch(improvement -> ArrayUtils.contains(destabilizationKeys, improvement.key));
    }

    @Override
    public boolean canApplyUpgrade(EntityPlayer player, ItemStack itemStack, ItemStack[] materials, String slot, int[] availableCapabilities) {
        return isMaterialsValid(itemStack, materials) && player.experienceLevel >= getExperienceCost(itemStack, materials, slot);
    }

    @Override
    public boolean isIntegrityViolation(EntityPlayer player, ItemStack itemStack, ItemStack[] materials, String slot) {
        return false;
    }

    @Override
    public ItemStack applyUpgrade(ItemStack itemStack, ItemStack[] materials, boolean consumeMaterials, String slot, EntityPlayer player) {
        ItemStack upgradedStack = itemStack.copy();

        String[] destabilizationKeys = DestabilizationEffect.getKeys();

        CastOptional.cast(itemStack.getItem(), ItemModular.class)
                .map(item -> item.getModuleFromSlot(itemStack, slot))
                .filter(module -> module instanceof ItemModuleMajor)
                .map(module -> (ItemModuleMajor) module)
                .ifPresent(module -> Arrays.stream(destabilizationKeys).forEach(key -> module.removeImprovement(upgradedStack, key)));

        if (consumeMaterials) {
            materials[0].shrink(1);
        }

        return upgradedStack;
    }

    @Override
    public boolean checkCapabilities(ItemStack targetStack, ItemStack[] materials, int[] availableCapabilities) {
        return true;
    }

    @Override
    public Collection<Capability> getRequiredCapabilities(ItemStack targetStack, ItemStack[] materials) {
        return Collections.emptyList();
    }

    @Override
    public int getRequiredCapabilityLevel(ItemStack targetStack, ItemStack[] materials, Capability capability) {
        return 0;
    }

    @Override
    public int getExperienceCost(ItemStack targetStack, ItemStack[] materials, String slot) {
        String[] destabilizationKeys = DestabilizationEffect.getKeys();

        int cost = CastOptional.cast(targetStack.getItem(), ItemModular.class)
                .map(item -> item.getModuleFromSlot(targetStack, slot))
                .filter(module -> module instanceof ItemModuleMajor)
                .map(module -> (ItemModuleMajor) module)
                .map(module -> Arrays.stream(module.getImprovements(targetStack)))
                .orElse(Stream.empty())
                .filter(improvement -> ArrayUtils.contains(destabilizationKeys, improvement.key))
                .mapToInt(improvement -> improvement.level + 1)
                .sum();

        cost += CastOptional.cast(targetStack.getItem(), ItemModular.class)
                .map(item -> item.getModuleFromSlot(targetStack, slot))
                .filter(module -> module instanceof ItemModuleMajor)
                .map(module -> (ItemModuleMajor) module)
                .map(module -> Math.max(3, -module.getMagicCapacity(targetStack)))
                .orElse(3);


        return cost;
    }

    @Override
    public SchemaType getType() {
        return SchemaType.other;
    }

    @Override
    public GlyphData getGlyph() {
        return glyph;
    }

    @Override
    public OutcomePreview[] getPreviews(ItemStack targetStack, String slot) {
        return new OutcomePreview[0];
    }
}
