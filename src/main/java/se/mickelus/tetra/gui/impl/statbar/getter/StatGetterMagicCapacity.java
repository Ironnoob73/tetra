package se.mickelus.tetra.gui.impl.statbar.getter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import se.mickelus.tetra.items.ItemModular;
import se.mickelus.tetra.util.CastOptional;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class StatGetterMagicCapacity implements IStatGetter {

    public StatGetterMagicCapacity() { }

    @Override
    public double getValue(EntityPlayer player, ItemStack itemStack) {
        return CastOptional.cast(itemStack.getItem(), ItemModular.class)
                .map(item -> item.getMajorModules(itemStack))
                .map(Arrays::stream)
                .orElse(Stream.empty())
                .filter(Objects::nonNull)
                .mapToInt(module -> module.getMagicCapacityGain(itemStack))
                .sum();
    }

    @Override
    public double getValue(EntityPlayer player, ItemStack itemStack, String slot) {
        return CastOptional.cast(itemStack.getItem(), ItemModular.class)
                .map(item -> item.getModuleFromSlot(itemStack, slot).getMagicCapacityGain(itemStack))
                .orElse(0);
    }

    @Override
    public double getValue(EntityPlayer player, ItemStack itemStack, String slot, String improvement) {
        return 0;
    }

    @Override
    public boolean shouldShow(EntityPlayer player, ItemStack currentStack, ItemStack previewStack) {
        return getValue(player, currentStack) > 0 || getValue(player, previewStack) > 0;
    }
}
