package se.mickelus.tetra.items.journal;

import net.minecraft.client.resources.I18n;

public enum JournalPage {
    craft(I18n.format("journal.craft.title")),
    structures(I18n.format("journal.structures.title")),
    system(I18n.format("journal.system.title"));

    public String label;

    JournalPage(String label) {
        this.label = label;
    }
}
