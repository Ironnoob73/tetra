package se.mickelus.tetra.items.journal.gui.system;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.resources.I18n;
import se.mickelus.tetra.gui.GuiAttachment;
import se.mickelus.tetra.gui.GuiString;
import se.mickelus.tetra.items.journal.GuiJournalRootBase;

public class GuiJournalSystemRoot extends GuiJournalRootBase {

    public GuiJournalSystemRoot(int x, int y) {
        super(x, y);
        GuiString test = new GuiString(0, 0, ChatFormatting.ITALIC+ I18n.format("journal.wip.description"));
        test.setAttachment(GuiAttachment.middleCenter);
        addChild(test);
    }
}
