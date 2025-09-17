package com.example.pathology;

import java.io.Serializable;

public class Item implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private int stock;
    private boolean selected;

    public Item(String id, String name, int stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.selected = false;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getStock() { return stock; }
    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }
}
