package se.mickelus.tetra.items.journal.gui.craft;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import se.mickelus.tetra.gui.GuiAttachment;
import se.mickelus.tetra.gui.GuiElement;
import se.mickelus.tetra.gui.animation.Applier;
import se.mickelus.tetra.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.items.ItemModular;
import se.mickelus.tetra.items.duplex_tool.ItemDuplexToolModular;
import se.mickelus.tetra.items.simplex_tool.ItemSimplexToolModular;
import se.mickelus.tetra.items.sword.ItemSwordModular;
import se.mickelus.tetra.items.toolbelt.ItemToolbeltModular;

import java.util.function.Consumer;

public class GuiJournalItems extends GuiElement {

    private final GuiJournalItem sword;
    private final GuiJournalItem toolbelt;
    private final GuiJournalItem duplexTools;
    private final GuiJournalItem simplexTools;

    public GuiJournalItems(int x, int y, int width, int height, Consumer<ItemModular> onItemSelect, Consumer<String> onSlotSelect) {
        super(x, y, width, height);


        sword = new GuiJournalItem(-80, 0, 3,0,ItemSwordModular.instance, I18n.format("journal.craft.sword"),
                () -> onItemSelect.accept(ItemSwordModular.instance), onSlotSelect);
        sword.setAttachment(GuiAttachment.topCenter);
        addChild(sword);

        toolbelt = new GuiJournalItem(0, -20, 4,0,ItemToolbeltModular.instance, I18n.format("journal.craft.toolbelt"),
                () -> onItemSelect.accept(ItemToolbeltModular.instance), onSlotSelect);
        toolbelt.setAttachment(GuiAttachment.topCenter);
        addChild(toolbelt);

        duplexTools = new GuiJournalItem(-60, -20, 2,0,ItemDuplexToolModular.instance, I18n.format("journal.craft.duplex_tool"),
                () -> onItemSelect.accept(ItemDuplexToolModular.instance), onSlotSelect);
        duplexTools.setAttachment(GuiAttachment.topCenter);
        addChild(duplexTools);

        simplexTools = new GuiJournalItem(-60, 20, 1,0,ItemSimplexToolModular.instance, I18n.format("journal.craft.simplex_tool"),
                () -> onItemSelect.accept(ItemSimplexToolModular.instance), onSlotSelect);
        simplexTools.setAttachment(GuiAttachment.topCenter);
        addChild(simplexTools);
    }

    public void animateOpen() {
        new KeyframeAnimation(200, this)
                .applyTo(new Applier.TranslateY(y - 4, y), new Applier.Opacity(0, 1))
                .withDelay(800)
                .start();
    }

    public void animateBack() {
        new KeyframeAnimation(100, this)
                .applyTo(new Applier.TranslateY(y - 4, y), new Applier.Opacity(0, 1))
                .start();
    }

    public void changeItem(Item item) {
        if (item instanceof ItemSwordModular) {
            toolbelt.setVisible(false);
            duplexTools.setVisible(false);
            simplexTools.setVisible(false);

            sword.setVisible(true);
            sword.setSelected(true);
        } else if (item instanceof ItemToolbeltModular) {
            sword.setVisible(false);
            duplexTools.setVisible(false);
            simplexTools.setVisible(false);

            toolbelt.setVisible(true);
            toolbelt.setSelected(true);
        } else if (item instanceof ItemDuplexToolModular) {
            sword.setVisible(false);
            toolbelt.setVisible(false);
            simplexTools.setVisible(false);

            duplexTools.setVisible(true);
            duplexTools.setSelected(true);
        }else if (item instanceof ItemSimplexToolModular) {
            sword.setVisible(false);
            toolbelt.setVisible(false);
            duplexTools.setVisible(false);

            simplexTools.setVisible(true);
            simplexTools.setSelected(true);
        }else {
            sword.setSelected(false);
            toolbelt.setSelected(false);
            duplexTools.setSelected(false);
            simplexTools.setSelected(false);

            sword.setVisible(true);
            toolbelt.setVisible(true);
            duplexTools.setVisible(true);
            simplexTools.setVisible(true);
        }
    }
}
