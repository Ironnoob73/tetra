package se.mickelus.tetra.blocks.workbench.gui;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.resources.I18n;
import se.mickelus.tetra.capabilities.Capability;
import se.mickelus.tetra.gui.impl.GuiColors;

import java.util.Collections;
import java.util.List;

public class GuiCapabilityRequirement extends GuiCapability {

    int requiredLevel;
    int availableLevel;

    public GuiCapabilityRequirement(int x, int y, Capability capability) {
        super(x, y, capability);
    }

    public void updateRequirement(int requiredLevel, int availableLevel) {
        setVisible(requiredLevel != 0);
        this.requiredLevel = requiredLevel;
        this.availableLevel = availableLevel;

        if (isVisible()) {
            if (requiredLevel > availableLevel) {
                update(requiredLevel, GuiColors.remove);
            } else {
                update(requiredLevel, GuiColors.add);
            }
        }
    }

    @Override
    public List<String> getTooltipLines() {
        if (hasFocus()) {
            return Collections.singletonList(I18n.format("capability." + capability + ".requirement", requiredLevel) + "\n\n"
                    + (requiredLevel > availableLevel ? ChatFormatting.RED : ChatFormatting.GREEN)
                    + I18n.format( "capability.available", availableLevel));
        }
        return super.getTooltipLines();
    }
}
