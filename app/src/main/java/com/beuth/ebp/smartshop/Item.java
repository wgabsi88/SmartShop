package com.beuth.ebp.smartshop;

import java.io.Serializable;

public class Item implements Serializable {

    private static final long serialVersionUID = -7060210544600464481L;
    private String title;
    private String description;
    private String quantity;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
