package com.example.inventory3.inventory.mvvm;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "item_table")
public class StoreItem {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String category;

    private int quantity;

    private String other_information;

    public StoreItem(String name, String category, int quantity, String other_information) {
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.other_information = other_information;
    }




    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getOther_information() {
        return other_information;
    }
}
