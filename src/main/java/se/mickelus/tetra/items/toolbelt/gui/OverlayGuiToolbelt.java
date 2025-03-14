package se.mickelus.tetra.items.toolbelt.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import se.mickelus.tetra.TetraMod;
import se.mickelus.tetra.gui.*;
import se.mickelus.tetra.items.toolbelt.inventory.InventoryPotions;
import se.mickelus.tetra.items.toolbelt.inventory.InventoryQuickslot;
import se.mickelus.tetra.items.toolbelt.inventory.InventoryQuiver;
import se.mickelus.tetra.items.toolbelt.inventory.ToolbeltSlotType;

public class OverlayGuiToolbelt extends GuiRoot {

    private static final ResourceLocation toolbeltTexture = new ResourceLocation(TetraMod.MOD_ID, "textures/gui/toolbelt-inventory.png");

    private OverlayGuiQuickslotGroup quickslotGroup;
    private OverlayGuiPotionGroup potionGroup;
    private OverlayGuiQuiverGroup quiverGroup;

    private boolean hasMouseMoved = false;
    private ScaledResolution scaledResolution;

    public OverlayGuiToolbelt(Minecraft mc) {
        super(mc);

        scaledResolution = new ScaledResolution(mc);

        quickslotGroup = new OverlayGuiQuickslotGroup(37, 0);
        addChild(quickslotGroup);

        potionGroup = new OverlayGuiPotionGroup(0, 30);
        addChild(potionGroup);

        quiverGroup = new OverlayGuiQuiverGroup(-30, -30);
        addChild(quiverGroup);

    }

    public void setInventories(ItemStack itemStack) {
        quickslotGroup.setInventory(new InventoryQuickslot(itemStack));
        potionGroup.setInventory(new InventoryPotions(itemStack));
        quiverGroup.setInventory(new InventoryQuiver(itemStack));
    }

    public void setVisible(boolean visible) {
        if (visible) {
            scaledResolution = new ScaledResolution(mc);
            hasMouseMoved = false;
            quickslotGroup.setVisible(true);
            potionGroup.setVisible(true);
            quiverGroup.setVisible(true);
        } else {
            quickslotGroup.setVisible(false);
            potionGroup.setVisible(false);
            quiverGroup.setVisible(false);
        }
    }

    @Override
    public void draw() {
        if (isVisible()) {
            int mouseX, mouseY;
            if (!hasMouseMoved && (Mouse.getDX() > 0 || Mouse.getDY() > 0)) {
                hasMouseMoved = true;
            }
            int width = scaledResolution.getScaledWidth();
            int height = scaledResolution.getScaledHeight();
            if (hasMouseMoved) {
                mouseX = Mouse.getX() * width / mc.displayWidth;
                mouseY = height - Mouse.getY() * height / mc.displayHeight - 1;
            } else {
                mouseX = width / 2;
                mouseY = height / 2;
            }
            drawChildren(width/2, height/2, width, height, mouseX, mouseY, 1);
        }
    }

    public ToolbeltSlotType getFocusType() {
        if (quickslotGroup.getFocus() != -1) {
            return ToolbeltSlotType.quickslot;
        }

        if (potionGroup.getFocus() != -1) {
            return ToolbeltSlotType.potion;
        }

        if (quiverGroup.getFocus() != -1) {
            return ToolbeltSlotType.quiver;
        }

        return ToolbeltSlotType.quickslot;
    }

    public int getFocusIndex() {
        int quickslotFocus = quickslotGroup.getFocus();
        if (quickslotFocus != -1) {
            return quickslotFocus;
        }

        int potionFocus = potionGroup.getFocus();
        if (potionFocus != -1) {
            return potionFocus;
        }

        int quiverFocus = quiverGroup.getFocus();
        if (quiverFocus != -1) {
            return quiverFocus;
        }

        return -1;
    }
}
