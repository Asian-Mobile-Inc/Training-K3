package com.example.asian;

public enum CaseAction {
    CASE_ADD(0),
    CASE_EDIT(1),
    CASE_DEL(2);

    private final int value;

    CaseAction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}

