package com.beuth.ebp.smartshop;

import java.io.Serializable;

/**
 * Created by florentchampigny on 05/03/15.
 */
public class Item implements Serializable {

    private static final long serialVersionUID = -7060210544600464481L;
    private String title;
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


}
