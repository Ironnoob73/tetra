package se.mickelus.tetra.gui;

import net.minecraft.client.renderer.GlStateManager;

import java.util.Collections;
import java.util.List;

public class GuiTextSmall extends GuiText {
    private List<String> tooltip;

    public GuiTextSmall(int x, int y, int width, String string) {
        super(x, y, width , string);
        tooltip = Collections.singletonList(string);
    }

    public void setString(String string) {
        this.string = string.replace("\\n", "\n");
        this.tooltip = Collections.singletonList(string);

        height = fontRenderer.getWordWrappedHeight(this.string, width * 2) / 2;
    }

    //Text TOO small, tried to use tooltip.
    @Override
    protected void calculateFocusState(int refX, int refY, int mouseX, int mouseY) {
        boolean gainFocus = mouseX >= getX() + refX
                && mouseX < getX() + refX + getWidth()
                && mouseY >= getY() + refY
                && mouseY < getY() + refY + getHeight();

        if (gainFocus != hasFocus) {
            hasFocus = gainFocus;
            if (hasFocus) {
                onFocus();
            } else {
                onBlur();
            }
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
        calculateFocusState(refX, refY, mouseX, mouseY);
        GlStateManager.pushMatrix();
        GlStateManager.scale(.5, .5, .5);
        fontRenderer.drawSplitString(string, (refX + x) * 2, (refY + y) * 2, width*2, 0xffffffff);
        GlStateManager.popMatrix();
    }
}
