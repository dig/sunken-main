package net.sunken.core.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;

public class BookUtil {

    private String title = "";
    private String author = "";
    private final ArrayList<ArrayList<String>> pages = new ArrayList<>();

    public BookUtil(String title, String author){
        this.title = title;
        this.author = author;
    }

    /**
     * Adds a line to the end of the specified page. If the page requested does
     * not yet exist, the missing amount of pages will be created with blank
     * contents.
     *
     * Warning: this does not check if the page can fit the extra line.
     *
     * @param line the line to add
     * @param page the page to add the line to
     */
    public void addLine(String line, int page) {
        while (pages.size() < page + 1) {
            pages.add(new ArrayList<String>());
        }

        pages.get(0).add(line + "\n");
    }

    /**
     * Adds a line to the end of the last existing page. This checks if the line
     * fits on the page, and will overflow onto the next page if needed.
     *
     * @param line the line to add
     */
    public void addLine(String line) {
        if (pages.isEmpty()) {
            pages.add(new ArrayList<String>());
        }

        ArrayList<String> lastPage = pages.get(pages.size() - 1);

        if (requiresNewPage(lastPage, line)) {
            pages.add(new ArrayList<String>());
            lastPage = pages.get(pages.size() - 1);
        }

        lastPage.add(line + "\n");
    }

    private Boolean requiresNewPage(ArrayList<String> currentPage, String line) {
        if (currentPage.size() > 13) {
            return true;
        }

        int length = line.length();
        for (String pageLine : currentPage) {
            length += pageLine.length();
        }

        Boolean newLine = length > 255;

        return newLine;
    }

    /**
     * Sets or replaces a line at the specified index. If the page requested
     * does not yet exist, the missing amount of pages will be created with
     * blank contents. If the line requested does not yet exist, the missing
     * amount of lines will be created with blank contents
     *
     * @param text
     * @param line
     * @param page
     */
    public void setLine(String text, int line, int page) {
        while (pages.size() < page + 1) {
            ArrayList<String> placeholder = new ArrayList<>();
            placeholder.add("");
            pages.add(placeholder);
        }

        while (pages.get(page).size() < line + 1) {
            pages.get(page).add("");
        }

        pages.get(page).set(line, text);
    }

    /**
     * Generate a new book-itemstack with the pages and settings defined in this
     * instance.
     *
     * @return the itemstack
     */
    public ItemStack generateBook() {
        ItemStack bi = new ItemStack(Material.WRITTEN_BOOK, 1);
        BookMeta bimeta = (BookMeta) bi.getItemMeta();

        for (ArrayList<String> page : pages) {
            String pageAsString = "";

            for (String line : page) {
                pageAsString += line;
            }

            bimeta.addPage(pageAsString);
        }

        bimeta.setAuthor(getAuthor());
        bimeta.setTitle(getTitle());
        bi.setItemMeta(bimeta);

        return bi;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

}
