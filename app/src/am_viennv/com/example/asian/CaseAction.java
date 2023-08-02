package com.example.asian;

public enum CaseAction {
    CASE_ADD(0),
    CASE_EDIT(1),
    CASE_DEL(2);

    private final int mValue;

    CaseAction(int value) {
        this.mValue = value;
    }

    public int getValue() {
        return mValue;
    }
}
