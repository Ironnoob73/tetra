package se.mickelus.tetra.module.improvement;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import se.mickelus.tetra.TetraMod;
import se.mickelus.tetra.blocks.workbench.gui.GuiModuleGlyph;
import se.mickelus.tetra.gui.impl.GuiColors;
import se.mickelus.tetra.items.ItemModular;
import se.mickelus.tetra.module.ItemModule;
import se.mickelus.tetra.module.schema.SchemaRarity;
import se.mickelus.tetra.util.CastOptional;

import java.util.Optional;


public class SettleToast implements IToast {
    private static final ResourceLocation texture = new ResourceLocation(TetraMod.MOD_ID,"textures/gui/toasts.png");

    private boolean hasPlayedSound = false;
    private ItemStack itemStack;
    private String moduleName;
    private GuiModuleGlyph glyph;

    public SettleToast(ItemStack itemStack, String slot) {
        this.itemStack = itemStack;

        ItemModule itemModule = CastOptional.cast(itemStack.getItem(), ItemModular.class)
                .map(item -> item.getModuleFromSlot(itemStack, slot))
                .orElse(null);

        glyph = Optional.ofNullable(itemModule)
                .map(module -> module.getData(itemStack))
                .map(data -> data.glyph)
                .map(glyphData -> new GuiModuleGlyph(0, 0, 16, 16, glyphData).setShift(false))
                .orElse(null);

        moduleName = Optional.ofNullable(itemModule)
                .map(module -> module.getName(itemStack))
                .orElse(slot);
    }

    public Visibility draw(GuiToast toastGui, long delta) {


        if (itemStack != null) {
            toastGui.getMinecraft().getTextureManager().bindTexture(texture);
            GlStateManager.color(1.0F, 1.0F, 1.0F);
            toastGui.drawTexturedModalRect(0, 0, 0, 0, 160, 32);

            if (!this.hasPlayedSound && delta > 0L) {
                this.hasPlayedSound = true;

                toastGui.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getRecord(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 2F, 0.7F));
            }

            if (glyph != null) {
                toastGui.drawTexturedModalRect(20, 14, 160, 0, 15, 15);
                glyph.draw(19, 14, 260, 43, -1, -1, 1);
            }

            toastGui.getMinecraft().fontRenderer.drawString(I18n.format("settled.toast"), 30, 7, SchemaRarity.hone.tint);
            toastGui.getMinecraft().fontRenderer.drawString(toastGui.getMinecraft().fontRenderer.trimStringToWidth(moduleName, 118), 37, 18, GuiColors.muted);


            RenderHelper.enableGUIStandardItemLighting();
            toastGui.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(null, itemStack, 8, 8);

            return delta > 5000 ? Visibility.HIDE : Visibility.SHOW;
        }

        return Visibility.HIDE;
    }
}
