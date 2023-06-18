package com.example.inventory3.loanledger;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventory3.R;
import com.example.inventory3.inventory.mvvm.StoreItem;
import com.example.inventory3.inventory.mvvm.StoreItemViewModel;
import com.example.inventory3.loanledger.mvvm.Converters;
import com.example.inventory3.loanledger.mvvm.Loan;
import com.example.inventory3.loanledger.mvvm.LoanListItem;
import com.example.inventory3.loanledger.mvvm.LoanViewModel;

import java.util.List;

public class LoanDetailActivity extends AppCompatActivity {
    public static final String extraname = "EXTRA_LOAN_NAME";
    public static final String extracompany = "EXTRA_LOAN_COMPANY";
    public static final String extraissuedate = "EXTRA_LOAN_ISSUEDATE";
    public static final String extrareturndate = "EXTRA_LOAN_RETURNDATE";
    public static final String extrasummary = "EXTRA_LOAN_SUMMARY";
    public static final String extraid = "EXTRA_LOAN_ID";
    public static final String extraloanitemlist = "EXTRA_LOAN_ITEM_LIST";
    public static final String extraisincoming = "EXTRA_ISINCOMING";
    public static final String extramobilenumber = "EXTRA_MOBILENUMBER";

    private TextView mNameTv;
    private TextView mCategoryTv;
    private TextView mIssueDateTv;
    private TextView mReturnDateTv;
    private TextView mNumber;
    private TextView mSummaryTv;
    private RecyclerView recyclerView;


    //only used when loan is being shredded
    Button shredloanbutton;
    StoreItemViewModel storeItemViewModel;
    LoanViewModel loanViewModel;
    List<StoreItem> inventoryItemList;

    int _id;
    String borrowername;
    String borrowercompany;
    String issuedate;
    String returndate;
    String mobilenumber;
    String summary;
    List<LoanListItem> loanitemlist;
    Boolean isincoming;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isincoming) {
            getMenuInflater().inflate(R.menu.menu_loandetail, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sendsms:
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address",mobilenumber);

                String textmessage = "Hello " + borrowername + ", you have borrowed " + loanitemlist.size() + " items due on " + returndate
                        + ". Please return them promptly, thank you. This is just a reminder";

                //int loansize = loanitemlist.size();
                //for (int i = 0; i < loansize; i ++) {
                //    textmessage.concat(loanitemlist.get(i).getmItemname() + " " + loanitemlist.get(i).getmQuantity() + "\n");
                //}

                smsIntent.putExtra("sms_body", textmessage);
                smsIntent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);

                if (smsIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(smsIntent);
                } else {
                    Toast.makeText(this, "SMS App not present", Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_detail);



        Intent intent = getIntent();
        _id = intent.getIntExtra(extraid, -1);
        borrowername = intent.getStringExtra(extraname);
        borrowercompany = intent.getStringExtra(extracompany);
        issuedate = intent.getStringExtra(extraissuedate);
        returndate = intent.getStringExtra(extrareturndate);
        mobilenumber = intent.getStringExtra(extramobilenumber);
        summary = intent.getStringExtra(extrasummary);
        loanitemlist = Converters.from_String(intent.getStringExtra(extraloanitemlist));
        isincoming = intent.getBooleanExtra(extraisincoming, false);

        if (isincoming) {
            TextView nameheader = (TextView) findViewById(R.id.nameheaderloandetail);
            TextView companyheader = (TextView) findViewById(R.id.companyheaderloandetail);
            nameheader.setText("Lender's rank and name");
            companyheader.setText("Lender's company/unit");
        }



        mNameTv = (TextView) findViewById(R.id.DisplayBorrowerNametv);
        mCategoryTv = (TextView) findViewById(R.id.DisplayBorrowerCompanytv);
        mNumber = (TextView) findViewById(R.id.DisplayHPNumbertv);
        mIssueDateTv = (TextView) findViewById(R.id.DisplayIssueDatetv);
        mReturnDateTv = (TextView) findViewById(R.id.DisplayReturnDatetv);
        mSummaryTv = (TextView) findViewById(R.id.DisplaySummaryText);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mNameTv.setText(borrowername);
        mCategoryTv.setText(borrowercompany);
        mIssueDateTv.setText(issuedate);
        mReturnDateTv.setText(returndate);
        mSummaryTv.setText(summary);
        mNumber.setText(mobilenumber);

        LoanDetailAdapter adapter = new LoanDetailAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setmLoanlistitem(loanitemlist);

        storeItemViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(StoreItemViewModel.class);
        storeItemViewModel.getAllItems().observe(this, new Observer<List<StoreItem>>() {
                    @Override
                    public void onChanged(List<StoreItem> storeItems) {
                        inventoryItemList = storeItems;
                    }
                });
                loanViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(LoanViewModel.class);
        shredloanbutton = findViewById(R.id.button);
        shredloanbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //removes current loan from list
                Loan thisloan = new Loan(borrowername, borrowercompany, issuedate, returndate, mobilenumber, loanitemlist, summary, isincoming);
                thisloan.setId(_id);
                loanViewModel.delete(thisloan);

                //returns loaned items back to inventory, or vice versa
                int loancount = loanitemlist.size();
                int inventorysize = inventoryItemList.size();

                for (int i = 0; i < loancount; i ++) {
                    LoanListItem cursor = loanitemlist.get(i);
                    for (int c = 0; c < inventorysize; c ++) {
                        StoreItem currentitem = inventoryItemList.get(c);
                        if (cursor.getmItemname().compareTo(currentitem.getName()) == 0) {
                            int updatedquantity = 0;
                            if (isincoming) {
                                updatedquantity = currentitem.getQuantity() - cursor.getmQuantity();
                            }
                            else {
                                updatedquantity = currentitem.getQuantity() + cursor.getmQuantity();
                            }

                            StoreItem updateditem = new StoreItem(currentitem.getName(), currentitem.getCategory(), updatedquantity, currentitem.getOther_information());
                            updateditem.setId(currentitem.getId());
                            storeItemViewModel.update(updateditem);
                        }
                    }
                }
                Toast.makeText(getApplicationContext(), "Loan shredded", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

    }
}
