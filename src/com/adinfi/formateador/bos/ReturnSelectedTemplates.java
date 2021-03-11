/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.bos;

import java.util.List;

public class ReturnSelectedTemplates {

    private List<Item> items;
    private String value = "";

    public ReturnSelectedTemplates() {
    }

    public ReturnSelectedTemplates(List<Item> items, String value) {
        this.items = items;
        this.value = value;
    }

    /**
     * @return the items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        if (value == null || value.isEmpty() == true) {
            this.value = value;
        } else {
            this.value += ", " + value;
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
