package com.example.inventory3.directissue.mvvm;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.inventory3.loanledger.mvvm.LoanListItem;

import java.util.List;

@Entity(tableName = "indent_table")
public class Indent {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private Boolean isincoming;

    private String name;

    private String receivercompany;

    private String receiveddate;

    private List<LoanListItem> itemlist;

    private String summary;

    private String hpnumber;

    private String storefnumber;


    public Indent(Boolean isincoming, String name, String receivercompany, String receiveddate, String hpnumber, String storefnumber,
                  List<LoanListItem> itemlist, String summary) {
        this.isincoming = isincoming;
        this.name = name;
        this.receivercompany = receivercompany;
        this.receiveddate = receiveddate;
        this.hpnumber = hpnumber;
        this.storefnumber = storefnumber;
        this.itemlist = itemlist;
        this.summary = summary;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getSummary() {
        return summary;
    }

    public String getName() {
        return name;
    }

    public Boolean getIsincoming() {
        return isincoming;
    }

    public List<LoanListItem> getItemlist() {
        return itemlist;
    }

    public String getReceiveddate() {
        return receiveddate;
    }

    public String getReceivercompany() {
        return receivercompany;
    }

    public String getHpnumber() {
        return hpnumber;
    }

    public String getStorefnumber() {
        return storefnumber;
    }
}
