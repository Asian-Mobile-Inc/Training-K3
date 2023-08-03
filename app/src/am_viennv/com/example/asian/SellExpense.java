package com.example.asian;

public class SellExpense {
    private final int month;
    private final float sales;
    private final float expenses;

    public SellExpense(int month, float sales, float expenses) {
        this.month = month;
        this.sales = sales;
        this.expenses = expenses;
    }

    public int getMonth() {
        return month;
    }

    public float getSales() {
        return sales;
    }

    public float getExpenses() {
        return expenses;
    }
}
