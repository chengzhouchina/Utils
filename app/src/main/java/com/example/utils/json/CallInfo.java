package com.example.utils.json;

public class CallInfo {
    private String number;
    private long date;
    private int type;

    public CallInfo(String number, long date, int type) {
        this.number = number;
        this.date = date;
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
