package com.example.asian;

public class SellExpense {
    private final int mMonth;
    private final float mSales;
    private final float mExpenses;

    public SellExpense(int mMonth, float mSales, float mExpenses) {
        this.mMonth = mMonth;
        this.mSales = mSales;
        this.mExpenses = mExpenses;
    }

    public int getmMonth() {
        return mMonth;
    }

    public float getmSales() {
        return mSales;
    }

    public float getmExpenses() {
        return mExpenses;
    }
}
