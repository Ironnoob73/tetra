package se.mickelus.tetra.blocks.workbench.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import se.mickelus.tetra.gui.GuiAttachment;
import se.mickelus.tetra.gui.GuiElement;
import se.mickelus.tetra.gui.GuiString;
import se.mickelus.tetra.gui.animation.Applier;
import se.mickelus.tetra.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.items.ItemModular;

import java.util.Collections;
import java.util.List;

public class GuiIntegrityBar extends GuiElement {

    private static final int segmentWidth = 8;
    private static final int segmentHeight = 2;
    private static final int segmentOffset = 6;

    private static final int gainColor = 0x22ffffff;
    private static final int costColor = 0xffffffff;
    private static final int overuseColor = 0x88ff5555;

    private int integrityGain;
    private int integrityCost;

    private GuiString label;

    private List<String> tooltip;

    public GuiIntegrityBar(int x, int y) {
        super(x, y, 0, 8);

        label = new GuiString(0, -3, "");
        label.setAttachment(GuiAttachment.topCenter);
        addChild(label);

        setAttachmentPoint(GuiAttachment.topCenter);

        tooltip = Collections.singletonList(I18n.format("stats.integrity_usage.tooltip"));
    }

    public void setItemStack(ItemStack itemStack, ItemStack previewStack) {
        boolean shouldShow = !itemStack.isEmpty() && itemStack.getItem() instanceof ItemModular;
        setVisible(shouldShow);
        if (shouldShow) {
            if (!previewStack.isEmpty()) {
                integrityGain = ItemModular.getIntegrityGain(previewStack);
                integrityCost = ItemModular.getIntegrityCost(previewStack);
            } else {
                integrityGain = ItemModular.getIntegrityGain(itemStack);
                integrityCost = ItemModular.getIntegrityCost(itemStack);
            }

            if (integrityGain + integrityCost < 0) {
                label.setString(TextFormatting.RED + I18n.format("stats.integrity_usage", -integrityCost, integrityGain));
            } else {
                label.setString(I18n.format("stats.integrity_usage", -integrityCost, integrityGain));
            }
            width = integrityGain * ( segmentWidth + 1);
        }
    }

    public void showAnimation() {
        if (isVisible()) {
            new KeyframeAnimation(100, this)
                    .withDelay(200)
                    .applyTo(new Applier.Opacity(0, 1), new Applier.TranslateY(-3, 0, true))
                    .start();
        }
    }

    @Override
    public List<String> getTooltipLines() {
        if (hasFocus()) {
            return tooltip;
        }
        return super.getTooltipLines();
    }

    @Override
    public void draw(int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        super.draw(refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);

        for (int i = 0; i < -integrityCost; i++) {
            drawSegment(refX + x + i * (segmentWidth + 1),refY + y + segmentOffset,
                    colorWithOpacity(i >= integrityGain ? overuseColor : costColor, opacity * getOpacity()));
        }

        for (int i = -integrityCost; i < integrityGain; i++) {
            drawSegment(refX + x + i * (segmentWidth + 1),refY + y + segmentOffset, colorWithOpacity(gainColor, opacity * getOpacity()));
        }
    }

    private void drawSegment(int x, int y, int color) {
        drawRect(x, y,x + segmentWidth, y + segmentHeight, color);
    }

}
