package com.beuth.ebp.smartshop;

import java.io.Serializable;

/**
 * Created by waelgabsi on 07.02.16.
 */
public class Token implements Serializable {
    private static final long serialVersionUID = -7060210544600464481L;


    String mtitle;



    public Token(String title) {
        this.mtitle = title;
    }

}
