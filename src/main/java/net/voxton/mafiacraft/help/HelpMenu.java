package net.voxton.mafiacraft.help;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.voxton.mafiacraft.player.MsgColor;

/**
 * Represents a help menu.
 */
public abstract class HelpMenu {

    /**
     * Width of the menu.
     */
    public static final int WIDTH = 55;

    /**
     * Height of the menu.
     */
    public static final int HEIGHT = 10;

    private final String name;

    private Map<String, String> help = new LinkedHashMap<String, String>();

    private List<String> realHelp = new ArrayList<String>();

    /**
     * Constructor.
     * 
     * @param name The name of the help menu.
     */
    public HelpMenu(String name) {
        this.name = name;
        loadMenu();
        initialize();
    }

    /**
     * Loads the help menu.
     */
    public abstract void loadMenu();

    /**
     * Adds an entry to the help menu.
     * 
     * @param command The name of the command to add.
     * @param description The description of the command.
     */
    protected void addEntry(String command, String description) {
        help.put(command.toLowerCase(), description);
    }

    /**
     * Initializes the help menu.
     */
    private void initialize() {
        for (Entry<String, String> entry : help.entrySet()) {
            realHelp.add(MsgColor.HELP_ENTRY + entry.getKey() + ": "
                    + MsgColor.HELP_DEF + entry.getValue());
        }
    }

    /**
     * Gets a page out of the help menu. May be a little expensive. In the
     * future it may be cached.
     *
     * @param page The number of the page.
     * @return An ordered list of the page and what appears on it.
     */
    public List<String> getPage(int page) {
        List<String> pg = new ArrayList<String>();

        String header = getHeader(page);

        //Create border
        int lines = (WIDTH - (header.length()) - 1) / 2;
        StringBuilder borderBuilder = new StringBuilder();
        for (int i = 1; i <= lines; i++) {
            borderBuilder.append('=');
        }
        String border = borderBuilder.toString();

        pg.add(border + ' ' + header + ' ' + border);
        return getPageContent(page);
    }

    /**
     * Gets the amount of help pages to display.
     * 
     * @return The amount of help pages.
     */
    public int getPages() {
        return realHelp.size() / HEIGHT;
    }

    /**
     * Gets the header of the help menu.
     * 
     * @param page The page number.
     * @return The header as a string.
     */
    public String getHeader(int page) {
        StringBuilder headerBuilder = new StringBuilder("[ ");
        headerBuilder.append(name).append(" Help -- Page ").append(page).append(
                " of ").
                append(getPages()).append(" ]");
        return headerBuilder.toString();
    }

    /**
     * Gets the content of a page.
     * 
     * @param page The page number
     * @return The content of the page in a List.
     */
    public List<String> getPageContent(int page) {
        List<String> content = new ArrayList<String>();
        int entries = realHelp.size();

        int lines = HEIGHT - 1;

        int start = (page * lines) - 1;
        int finish = start + lines;

        if (start > entries - 1) {
            return content;
        }

        for (int i = start; (i <= finish) && (i < (entries - 1)); i++) {
            String entry = realHelp.get(i);
            content.add(entry);
        }

        return content;
    }

    /**
     * Gets a command entry.
     * 
     * @param command The command.
     * @return The description of the command.
     */
    public String getEntry(String command) {
        return help.get(command.toLowerCase());
    }

}
