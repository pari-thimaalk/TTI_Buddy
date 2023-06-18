package com.example.inventory3.loanledger;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventory3.MainActivity;
import com.example.inventory3.R;
import com.example.inventory3.inventory.mvvm.StoreItem;
import com.example.inventory3.inventory.mvvm.StoreItemViewModel;
import com.example.inventory3.loanledger.mvvm.Loan;
import com.example.inventory3.loanledger.mvvm.LoanListItem;
import com.example.inventory3.loanledger.mvvm.LoanViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class NewLoanActivity extends AppCompatActivity {

    public static final String key = "update";
    Boolean isincomingloan = false;
    StoreItemViewModel storeItemViewModel;
    LoanViewModel loanViewModel;
    //stores list of items in inventory
    List<StoreItem> inventory;

    //used for autocompletetext, list of names of items in inventory
    List<String> inventorynamelist = new ArrayList<String>();
    LoanListItemAdapter adapter;
    List<LoanListItem> loanitemlist = new ArrayList<>(Arrays.asList(new LoanListItem(1)));
    RecyclerView recyclerView;

    EditText borrowernameedittext;
    EditText borrowercompanyedittext;

    EditText borrowerdateedittext;
    DatePickerDialog datepicker;

    EditText hpnumberedittext;

    EditText loansummaryedittext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newloan);

        String code = getIntent().getStringExtra(key);
        if (code.compareTo("incoming") == 0) {
            isincomingloan = true;
        }
        if (isincomingloan) {
            setTitle("Make an incoming loan");
        } else {
            setTitle("Make an outgoing loan");
        }

        EditText editText = (EditText) findViewById(R.id.storeferencenumber);
        editText.setVisibility(View.GONE);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_loan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        storeItemViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(StoreItemViewModel.class);
        storeItemViewModel.getAllItems().observe(this, new Observer<List<StoreItem>>() {
            @Override
            public void onChanged(List<StoreItem> storeItems) {
                if (storeItems != null) {
                    inventory = storeItems;
                    for (int i = 0; i < inventory.size(); i ++) {
                        inventorynamelist.add(inventory.get(i).getName());
                    }
                    ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, inventorynamelist);
                    adapter = new LoanListItemAdapter(loanitemlist, stringArrayAdapter);
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        Button additembutton = (Button) findViewById(R.id.additemloan);
        additembutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertitem();
            }
        });
        Button deletebutton = (Button) findViewById(R.id.deleteitemloan);
        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteitem();
            }
        });

        //setting the popup calender view
        borrowerdateedittext = findViewById(R.id.dateEditText);
        borrowerdateedittext.setInputType(InputType.TYPE_NULL);
        borrowerdateedittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datepicker = new DatePickerDialog(NewLoanActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                borrowerdateedittext.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datepicker.show();
            }
        });

        loanViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(LoanViewModel.class);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveloan();
                return true;
            case R.id.action_delete:
                Toast.makeText(this, "Loan cancelled", Toast.LENGTH_SHORT);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveloan() {
        borrowernameedittext = (EditText) findViewById(R.id.nameEditText);
        borrowercompanyedittext = (EditText) findViewById(R.id.companyEditText);
        borrowerdateedittext = (EditText) findViewById(R.id.dateEditText);
        loansummaryedittext = (EditText) findViewById(R.id.editTextSummary);
        hpnumberedittext = (EditText) findViewById(R.id.handphonenumberet);

        //Checks if name, company and date is filled
        if(isEmpty(borrowernameedittext) || isEmpty(borrowercompanyedittext) || isEmpty(borrowerdateedittext) || isEmpty(hpnumberedittext)) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = df.format(date);

        String borrowername = borrowernameedittext.getText().toString();
        String company = borrowercompanyedittext.getText().toString();
        String returndate = borrowerdateedittext.getText().toString();
        String handphonenumber = hpnumberedittext.getText().toString();
        String summary = loansummaryedittext.getText().toString();

        List<LoanListItem> loanlist = new ArrayList<>();

        //checks if each item field is filled, and items borrowed does not exceed quantity
        for (int i = 0; i < recyclerView.getChildCount(); i ++) {
            String name = ((AutoCompleteTextView)recyclerView.getChildAt(i).findViewById(R.id.newloanitemname)).getText().toString();
            String quantity = ((EditText)recyclerView.getChildAt(i).findViewById(R.id.newloanitemquantity)).getText().toString();
            if (quantity.isEmpty() || name.isEmpty() || (Integer.parseInt(quantity) == 0)) {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isincomingloan) {
                for (int c = 0; c < inventory.size(); c ++) {
                    if (name.compareTo(inventory.get(c).getName()) == 0 && Integer.parseInt(quantity) > inventory.get(c).getQuantity()) {
                        Toast.makeText(this, "Insufficient quantity in inventory for at least one item", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            loanlist.add(new LoanListItem(i + 1, name, Integer.parseInt(quantity)));
        }
        loanViewModel.insert(new Loan(borrowername, company, formattedDate, returndate, handphonenumber, loanlist, summary, isincomingloan));
        updateinventory(loanlist, inventory);
        Toast.makeText(this, "Loan saved", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        if (isincomingloan) {
            intent.putExtra(CalendarContract.Events.TITLE, "Return items to " + borrowername);
        } else {
            intent.putExtra(CalendarContract.Events.TITLE, "Collect items from " + borrowername);
        }
        intent.putExtra(CalendarContract.Events.DESCRIPTION, loanlist.size() + " items");
        intent.putExtra(CalendarContract.Events.ALL_DAY, true);

        String startdate = returndate + " 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date dateobj = null;
        try {
            dateobj = sdf.parse(startdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long starttime = dateobj.getTime();


        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, starttime);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Calender App not present", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void updateinventory(List<LoanListItem> loanlist, List<StoreItem> inventory) {
        int loancount = loanlist.size();
        int inventorycount = inventory.size();
        for (int i = 0; i < loancount; i ++) {
            for (int c = 0; c < inventorycount; c ++) {
                StoreItem cursor = inventory.get(c);
                if (loanlist.get(i).getmItemname().compareTo(cursor.getName()) == 0) {
                    int updatedquantity = 0;
                    if (isincomingloan) {
                        updatedquantity = cursor.getQuantity() + loanlist.get(i).getmQuantity();
                    } else {
                        updatedquantity = cursor.getQuantity() - loanlist.get(i).getmQuantity();
                    }

                    StoreItem updateditem = new StoreItem(cursor.getName(), cursor.getCategory(), updatedquantity, cursor.getOther_information());
                    updateditem.setId(cursor.getId());
                    storeItemViewModel.update(updateditem);
                    break;
                }
            }
        }

    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        }
        return true;
    }

    private void insertitem() {
        int size = loanitemlist.size() + 1;
        loanitemlist.add(new LoanListItem(size));
        adapter.notifyDataSetChanged();
    }
    private void deleteitem() {
        int count = loanitemlist.size();
        if (count == 1) {
            Toast.makeText(this, "Only one item left", Toast.LENGTH_SHORT).show();
            return;
        }
        loanitemlist.remove(count - 1);
        adapter.notifyDataSetChanged();
    }

}
