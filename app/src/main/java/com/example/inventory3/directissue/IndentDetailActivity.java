package com.example.inventory3.directissue;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventory3.R;
import com.example.inventory3.directissue.mvvm.Indent;
import com.example.inventory3.directissue.mvvm.IndentViewModel;
import com.example.inventory3.inventory.mvvm.StoreItem;
import com.example.inventory3.inventory.mvvm.StoreItemViewModel;
import com.example.inventory3.loanledger.LoanDetailAdapter;
import com.example.inventory3.loanledger.mvvm.Converters;
import com.example.inventory3.loanledger.mvvm.LoanListItem;

import java.util.List;

public class IndentDetailActivity extends AppCompatActivity {
    public static final String extraname = "EXTRA_LOAN_NAME";
    public static final String extracompany = "EXTRA_LOAN_COMPANY";
    public static final String extraissuedate = "EXTRA_LOAN_ISSUEDATE";
    public static final String extrasummary = "EXTRA_LOAN_SUMMARY";
    public static final String extraid = "EXTRA_LOAN_ID";
    public static final String extraloanitemlist = "EXTRA_LOAN_ITEM_LIST";
    public static final String extraisincoming = "EXTRA_ISINCOMING";
    public static final String extrahpnumber = "EXTRA_HPNUMBER";
    public static final String extrastorefnumber = "EXTRA_STOREFNUMBER";

    String name;
    String company;
    String date;
    String summary;
    List<LoanListItem> itemlist;
    int id;
    Boolean isincoming;
    String hpnumber;
    String storefnumber;

    IndentViewModel indentViewModel;
    StoreItemViewModel inventoryViewModel;
    List<StoreItem> inventorylist;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_detail);

        //setting views we dont need to gone
        TextView datetv = findViewById(R.id.expirydatetv);
        datetv.setVisibility(View.GONE);
        TextView returndate = findViewById(R.id.DisplayReturnDatetv);
        returndate.setVisibility(View.GONE);
        Button button = findViewById(R.id.button);
        button.setVisibility(View.GONE);

        Intent intent = getIntent();
        name = intent.getStringExtra(extraname);
        company = intent.getStringExtra(extracompany);
        date = intent.getStringExtra(extraissuedate);
        summary = intent.getStringExtra(extrasummary);
        itemlist = Converters.from_String(intent.getStringExtra(extraloanitemlist));
        id = intent.getIntExtra(extraid, -1);
        isincoming = intent.getBooleanExtra(extraisincoming, true);
        hpnumber = intent.getStringExtra(extrahpnumber);
        storefnumber = intent.getStringExtra(extrastorefnumber);



        TextView nametv = (TextView) findViewById(R.id.DisplayBorrowerNametv);
        TextView companytv = (TextView) findViewById(R.id.DisplayBorrowerCompanytv);
        TextView issuedatetv = (TextView) findViewById(R.id.DisplayIssueDatetv);
        TextView summarytv = (TextView) findViewById(R.id.DisplaySummaryText);

        TextView hpnumbertv = (TextView) findViewById(R.id.DisplayHPNumbertv);


        if (isincoming) {
            TextView nameheader = (TextView) findViewById(R.id.nameheaderloandetail);
            TextView companyheader = (TextView) findViewById(R.id.companyheaderloandetail);
            nameheader.setText("Issuer's rank and name");
            companyheader.setText("Issuer's company/unit");

            hpnumbertv.setText(storefnumber);

            TextView storefnumber = (TextView) findViewById(R.id.hpnumberheaderloandetail);
            storefnumber.setText("STO/Ref. Number");
        } else {
            hpnumbertv.setText(hpnumber);
        }

        nametv.setText(name);
        companytv.setText(company);
        issuedatetv.setText(date);
        summarytv.setText(summary);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LoanDetailAdapter adapter = new LoanDetailAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setmLoanlistitem(itemlist);

        inventoryViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(StoreItemViewModel.class);
        inventoryViewModel.getAllItems().observe(this, new Observer<List<StoreItem>>() {
            @Override
            public void onChanged(List<StoreItem> storeItems) {
                inventorylist = storeItems;
            }
        });

        indentViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(IndentViewModel.class);
    }

    public void updateinventory() {
        //returns issued items back to inventory, or vice versa.
        int inventorysize = inventorylist.size();

        for (LoanListItem cursor:itemlist) {
            for (int c = 0; c < inventorysize; c ++) {
                StoreItem currentitem = inventorylist.get(c);
                String itemname = currentitem.getName();
                if (itemname.compareTo(cursor.getmItemname()) == 0) {
                    int itemquantity = currentitem.getQuantity();
                    int updatedquantity = 0;
                    if (isincoming) {
                        updatedquantity = itemquantity - cursor.getmQuantity();
                    } else {
                        updatedquantity = itemquantity + cursor.getmQuantity();
                    }
                    StoreItem updateditem = new StoreItem(currentitem.getName(), currentitem.getCategory(), updatedquantity, currentitem.getOther_information());
                    updateditem.setId(currentitem.getId());
                    inventoryViewModel.update(updateditem);
                }

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_delete:
                Indent currentindent = new Indent(isincoming, name, company, date, hpnumber, storefnumber, itemlist, summary);
                currentindent.setId(id);
                indentViewModel.delete(currentindent);
                updateinventory();
                Toast.makeText(this, "Indent deleted", Toast.LENGTH_SHORT).show();
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
