/*
 * This file is part of Mafiacraft.
 * 
 * Mafiacraft is released under the Voxton License version 1.
 *
 * Mafiacraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition to this, you must also specify that this product includes 
 * software developed by Voxton.net and may not remove any code
 * referencing Voxton.net directly or indirectly.
 * 
 * Mafiacraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * and the Voxton license along with Mafiacraft. 
 * If not, see <http://voxton.net/voxton-license-v1.txt>.
 */
package net.voxton.mafiacraft.help;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import net.voxton.mafiacraft.MLogger;
import net.voxton.mafiacraft.locale.Locale;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.player.MsgColor;
import org.bukkit.ChatColor;

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
    private List<String> realHelp = new LinkedList<String>();

    /**
     * Constructor.
     *
     * @param name The name of the help menu.
     */
    public HelpMenu(String name) {
        this.name = name;
        loadMenu();
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

        String entry = MsgColor.HELP_ENTRY + command + ": "
                + MsgColor.HELP_DEF + description;

        if (ChatColor.stripColor(entry).length() > WIDTH) {
            MLogger.log(Level.WARNING, "The help entry for " + ChatColor.RED
                    + "/" + getName() + " " + command + ChatColor.WHITE
                    + " is over the limit of " + WIDTH
                    + ". Unexpected formatting issues may occur!");
        }

        realHelp.add(entry);
    }

    /**
     * Gets a page out of the help menu.
     * 
     * @param page The page to get.
     * @return The page as a List<String>.
     */
    public List<String> getPage(int page) {
        return getPage(page, Locale.getDefault());
    }

    /**
     * Gets a page out of the help menu. May be a little expensive. In the
     * future it may be cached.
     *
     * @param page The number of the page.
     * @param locale The locale of the page.
     * @return An ordered list of the page and what appears on it.
     */
    public List<String> getPage(int page, Locale locale) {
        List<String> pg = new ArrayList<String>();
        pg.add(MsgColor.INFO + buildBorderedHeader(page, locale));
        pg.addAll(getPageContent(page, locale));
        return pg;
    }

    /**
     * Gets the amount of help pages to display.
     *
     * @return The amount of help pages.
     */
    public int getPages() {
        return (int) Math.ceil((double) realHelp.size() / (HEIGHT - 1));
    }

    /**
     * Builds a bordered header for the specified page.
     * 
     * @param page The page to get the header of.
     * @param locale The locale of the page.
     * @return The bordered header.
     */
    public String buildBorderedHeader(int page, Locale locale) {
        String header = getHeader(page);

        int lines = (WIDTH - (header.length()) - 1) / 2;
        StringBuilder borderBuilder = new StringBuilder();
        for (int i = 1; i <= lines; i++) {
            borderBuilder.append('=');
        }
        String border = borderBuilder.toString();

        return new StringBuilder(border).append(' ').append(header).append(' ').
                append(border).toString();
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
     * @param locale The locale of the page.
     * @return The content of the page in a List.
     */
    public List<String> getPageContent(int page, Locale locale) {
        List<String> content = new LinkedList<String>();
        int entries = realHelp.size();

        int lines = HEIGHT - 1;

        int start = ((page - 1) * lines);

        if (start > entries - 1) {
            content.add(MsgColor.ERROR + locale.localize("help.not-found",
                    getPages()));
            return content;
        }

        if (page == 0) {
            content.add(MsgColor.ERROR + locale.localize("help.no-0th-page"));
            return content;
        }

        if (page < 0) {
            content.add(MsgColor.SUCCESS
                    + locale.localize("help.imaginary-page"));
            return content;
        }

        int finish = start + lines;

        for (int i = start; (i <= finish) && (i < (entries - 1)); i++) {
            String entry = realHelp.get(i);
            System.out.println(entry);
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
        for (String line : getPage(page, player.getLocale())) {
            player.sendMessage(line);
        }
    }

    /**
     * Returns true if the given command is registered.
     *
     * @param command The command to check.
     * @return The command.
     */
    public boolean hasCommand(String command) {
        return usage.containsKey(command);
    }

    /**
     * Gets the complete formatted usage of the given command.
     *
     * @param command The command to get the usage of.
     * @return The command's usage.
     */
    public String getCompleteUsage(String command) {
        String usg = getUsage(command);
        return "/" + name.toLowerCase() + " " + command + " " + usg;
    }

    /**
     * Does help for the given player.
     *
     * @param player The player.
     * @param arg The argument that is being parsed.
     */
    public void doHelp(MPlayer player, String arg) {
        int page = 1;
        try {
            page = Integer.parseInt(arg);
            sendPage(page, player);
            return;
        } catch (NumberFormatException ex) {
        }

        if (!hasCommand(arg)) {
            player.sendMessage(MsgColor.ERROR + player.getLocale().localize(
                    "help.strange-argument"));
            return;
        }

        player.sendMessage(MsgColor.ERROR + player.getLocale().localize("help.incorrect-usage",
                getCompleteUsage(arg)));
    }

    /**
     * Gets the name of the help menu.
     *
     * @return The help menu name.
     */
    protected String getName() {
        return name;
    }

    /**
     * Default help command.
     * 
     * @param player The player to send the help menu to.
     */
    public void doHelp(MPlayer player) {
        doHelp(player, "1");
    }

}
