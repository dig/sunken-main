package net.sunken.core.inventory;

import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Getter;
import net.sunken.core.inventory.element.ActionableElement;
import net.sunken.core.inventory.element.Element;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class Page {

    @Getter
    private final String id;
    @Getter
    private final String title;
    @Getter
    private final int size;
    @Getter
    private final Map<Integer, Element> elements;

    @Getter
    private Inventory inventory;

    private Page(String id,
                 String title,
                 int size,
                 Map<Integer, Element> elements) {

        this.id = id;
        this.title = title;
        this.size = size;
        this.elements = elements;

        this.inventory = Bukkit.createInventory(null, size, title);
    }

    public void updateInventory(){
        this.inventory.clear();
        elements.forEach((position, element) -> this.inventory.setItem(position, element.getItem()));
    }

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public static class Builder {

        private String id;
        private String title;
        private int size;
        private Map<Integer, Element> elements;

        {
            elements = Maps.newHashMap();
        }

        private Builder(String id) {
            this.id = id;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Builder putElement(int position, Element element) {
            checkState(position >= 0 && position <= 53, "position must be between 0 and 53");
            elements.put(position, element);
            return this;
        }

        public Page build() {
            checkNotNull(id, "id cannot be null");
            checkNotNull(title, "title cannot be null");
            checkState(size <= 54, "size cannot be bigger than 54");

            return new Page(id, title, size, elements);
        }
    }
}
