package com.example.inventory3.loanledger.mvvm;

public class LoanListItem {
    private int mIndexno;
    private String mItemname;
    private int mQuantity;

    public LoanListItem(int mIndexno) {
        this.mIndexno = mIndexno;
    }
    public LoanListItem(int mIndexno, String mItemname, int mQuantity) {
        this.mIndexno = mIndexno;
        this.mItemname = mItemname;
        this.mQuantity = mQuantity;
    }

    public int getmIndexno() {
        return mIndexno;
    }

    public int getmQuantity() {
        return mQuantity;
    }

    public String getmItemname() {
        return mItemname;
    }

    public void setmItemname(String mItemname) {
        this.mItemname = mItemname;
    }

    public void setmQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }
}
