package net.voxton.mafiacraft.help;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.voxton.mafiacraft.player.MPlayer;
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

    /**
     * The name of the help menu.
     */
    private final String name;

    /**
     * Contains help.
     */
    private Map<String, String> help = new LinkedHashMap<String, String>();

    /**
     * Contains the usage.
     */
    private Map<String, String> usage = new HashMap<String, String>();

    /**
     * The parsed and sorted help menu.
     */
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
     * @param usg The usage of the command.
     */
    protected void addEntry(String command, String description, String usg) {
        command = command.toLowerCase();
        help.put(command, description);
        usage.put(command, usg);
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

    /**
     * Gets the usage of a certain command.
     *
     * @param command The command.
     * @return The usage of the command.
     */
    public String getUsage(String command) {
        return usage.get(command.toLowerCase());
    }

    /**
     * Sends a page to the given player.
     *
     * @param page The page to send.
     * @param player The player to send to.
     */
    public void sendPage(int page, MPlayer player) {
        for (String line : getPage(page)) {
            player.sendMessage(line);
        }
    }

    /**
     * Sends a usage error to the given player.
     *
     * @param command The command.
     * @param player The player.
     */
    public void sendUsageError(String command, MPlayer player) {
        String usg = getCompleteUsage(command);

        if (usg == null) {
            player.sendMessage(MsgColor.ERROR + "Strange argument \"" + command
                    + "\" given.");
            return;
        }

        player.sendMessage(MsgColor.ERROR + "Incorrect usage of the command. "
                + "Usage: " + MsgColor.INFO_HILIGHT + getCompleteUsage(command)
                + MsgColor.ERROR + ".");
    }

    /**
     * Gets the complete formatted usage of the given command.
     *
     * @param command The command to get the usage of.
     * @return The command's usage.
     */
    public String getCompleteUsage(String command) {
        String usg = getUsage(command);
        return "/" + name.toLowerCase() + " " + usg;
    }

    /**
     * Does help for the given player.
     *
     * @param player The player.
     * @param arg The argument that is being parsed.
     */
    public void doHelp(MPlayer player, String arg) {
        int page = -1;
        try {
            page = Integer.parseInt(arg);
        } catch (NumberFormatException ex) {
        }

        HelpMenu menu = MenuType.CWORLD;

        if (page > 0) {
            menu.sendPage(page, player);
            return;
        }

        menu.sendUsageError(arg, player);
    }

    /**
     * Gets the name of the help menu.
     *
     * @return The help menu name.
     */
    protected String getName() {
        return name;
    }

}
