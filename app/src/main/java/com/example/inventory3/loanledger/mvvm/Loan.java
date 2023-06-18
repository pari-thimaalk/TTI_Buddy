package com.example.inventory3.loanledger.mvvm;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "loan_table")
public class Loan {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String borrowername;

    private String borrowercompany;

    private String issuedate;

    private String returndate;

    private String hpnumber;

    private List<LoanListItem> itemlist;

    private String summary;

    private Boolean isincoming;

    public Loan(String borrowername, String borrowercompany, String issuedate, String returndate, String hpnumber, List<LoanListItem> itemlist, String summary, Boolean isincoming) {
        this.borrowername = borrowername;
        this.borrowercompany = borrowercompany;
        this.issuedate = issuedate;
        this.returndate = returndate;
        this.hpnumber = hpnumber;
        this.itemlist = itemlist;
        this.summary = summary;
        this.isincoming = isincoming;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getBorrowername() {
        return borrowername;
    }

    public String getBorrowercompany() {
        return borrowercompany;
    }

    public String getIssuedate() {
        return issuedate;
    }

    public String getReturndate() {
        return returndate;
    }

    public String getHpnumber() { return hpnumber; }

    public List<LoanListItem> getItemlist() {
        return itemlist;
    }

    public String getSummary() {
        return summary;
    }

    public Boolean getIsincoming() { return isincoming; }
}
