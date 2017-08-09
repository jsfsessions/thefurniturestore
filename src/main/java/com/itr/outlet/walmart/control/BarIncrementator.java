package com.itr.outlet.walmart.control;

public class BarIncrementator {

    private int value;
    private int product;
    private int rows;

    public BarIncrementator() {
    }

    public BarIncrementator(int value) {
        this.value = value;
    }

    public int getValue() {

        if (rows > 0) {
            float q = rows - 0;
            float p = 100 - 0;

            float r = p / q;
            this.value = (int) (0 + r * product);
        }
        return value;
    }

    public void setRows(int rows) {
        this.rows += rows;
    }

    public void incProduct() {        
        this.product++;
    }
    
    public void resetProduct() {
        this.product = 0;
    }
}
