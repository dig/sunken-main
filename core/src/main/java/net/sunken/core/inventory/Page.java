package net.sunken.core.inventory;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class Page {

    @Getter
    private final String id;
    private final String title;
    private final int size;

    public Page(String id, String title, int size) {
        this.id = id;
        this.title = title;
        this.size = size;
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

        public Builder(String id) {
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
            elements.put(position, element);
            return this;
        }

        public Page build() {
            checkNotNull(id, "id cannot be null");
            checkNotNull(title, "title cannot be null");
            checkState(size <= 54, "size cannot be bigger than 54");

            return new Page(id, title, size);
        }
    }
}
