/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.help;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author simplyianm
 */
public abstract class HelpMenu {
    public static final int WIDTH = 55;

    public static final int HEIGHT = 10;

    private final String name;

    private Map<String, String> help = new LinkedHashMap<String, String>();

    private List<String> realHelp = new ArrayList<String>();

    public HelpMenu(String name) {
        this.name = name;
        loadMenu();
        initialize();
    }

    public abstract void loadMenu();

    public void addEntry(String command, String description) {
        help.put(command, description);
    }

    public void initialize() {
        //TODO: make the menu correctly.
        for (Entry<String, String> entry : help.entrySet()) {
            realHelp.add(entry.getKey() + " -- " + entry.getValue());
        }
    }

    /**
     * Gets a page out of the help menu. May be a little expensive. In the
     * future it may be cached.
     *
     * @param page
     * @return
     */
    public List<String> getPage(int page) {
        List<String> pg = new ArrayList<String>();

        String header = getHeader(page);

        //Create border
        int lines = (WIDTH - (header.length()) - 1) / 2;
        StringBuilder borderBuilder = new StringBuilder();
        for (int i = 1; i <= lines; i++) {
            borderBuilder.append('_');
        }
        String border = borderBuilder.toString();

        pg.add(border + ' ' + header + ' ' + border);
        return getPageContent(page);
    }

    public int getPages() {
        return realHelp.size() / HEIGHT;
    }

    public String getHeader(int page) {
        StringBuilder headerBuilder = new StringBuilder("[ ");
        headerBuilder.append(name).append(" Help -- Page ").append(page).append(" of ").append(getPages()).append(" ]");
        return headerBuilder.toString();
    }

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

}
