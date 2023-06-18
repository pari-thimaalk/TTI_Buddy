package com.example.inventory3.directissue;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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

import com.example.inventory3.R;
import com.example.inventory3.directissue.mvvm.Indent;
import com.example.inventory3.directissue.mvvm.IndentViewModel;
import com.example.inventory3.inventory.mvvm.StoreItem;
import com.example.inventory3.inventory.mvvm.StoreItemViewModel;
import com.example.inventory3.loanledger.LoanListItemAdapter;
import com.example.inventory3.loanledger.mvvm.LoanListItem;
import com.example.inventory3.loanledger.mvvm.LoanViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewIndentActivity extends AppCompatActivity {

    Boolean isincomingindent = false;

    public static final String key = "update";
    StoreItemViewModel storeItemViewModel;
    IndentViewModel indentViewModel;

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
    EditText borrowerhpnumberedittext;
    EditText stonumberedittext;

    EditText loansummaryedittext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newloan);

        String code = getIntent().getStringExtra(key);
        if (code.compareTo("incoming") == 0) {
            isincomingindent = true;
        }
        if (isincomingindent) {
            setTitle("Make an indent");
            EditText editText = (EditText) findViewById(R.id.handphonenumberet);
            editText.setVisibility(View.GONE);
        } else {
            setTitle("Make a direct issue");

        }



        borrowerdateedittext = findViewById(R.id.dateEditText);
        borrowerdateedittext.setVisibility(View.GONE);
        borrowerhpnumberedittext = findViewById(R.id.handphonenumberet);
        stonumberedittext = findViewById(R.id.storeferencenumber);


        if(isincomingindent) {
            borrowerhpnumberedittext.setVisibility(View.GONE);
        } else {
            stonumberedittext.setVisibility(View.GONE);
        }

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

        indentViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(IndentViewModel.class);

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
                saveindent();
                return true;
            case R.id.action_delete:
                Toast.makeText(this, "Indent cancelled", Toast.LENGTH_SHORT);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveindent() {
        borrowernameedittext = (EditText) findViewById(R.id.nameEditText);
        borrowercompanyedittext = (EditText) findViewById(R.id.companyEditText);
        loansummaryedittext = (EditText) findViewById(R.id.editTextSummary);

        //Checks if name, company and date is filled
        if(isEmpty(borrowernameedittext) || isEmpty(borrowercompanyedittext)) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = df.format(date);

        String borrowername = borrowernameedittext.getText().toString();
        String company = borrowercompanyedittext.getText().toString();
        String summary = loansummaryedittext.getText().toString();

        List<LoanListItem> indentlist = new ArrayList<>();

        //checks if each item field is filled, and items borrowed does not exceed quantity. then adds items to indentlist
        for (int i = 0; i < recyclerView.getChildCount(); i ++) {
            String name = ((AutoCompleteTextView)recyclerView.getChildAt(i).findViewById(R.id.newloanitemname)).getText().toString();
            String quantity = ((EditText)recyclerView.getChildAt(i).findViewById(R.id.newloanitemquantity)).getText().toString();
            if (quantity.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isincomingindent) {
                for (int c = 0; c < inventory.size(); c ++) {
                    if (name.compareTo(inventory.get(c).getName()) == 0 && Integer.parseInt(quantity) > inventory.get(c).getQuantity()) {
                        Toast.makeText(this, "Insufficient quantity in inventory for at least one item", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            indentlist.add(new LoanListItem(i + 1, name, Integer.parseInt(quantity)));
        }
        String stonumber = "wtv";
        String hpnumber = "wtv";
        if (isincomingindent) {
            if (isEmpty(stonumberedittext)) {
                Toast.makeText(this, "Please enter the STO number", Toast.LENGTH_SHORT).show();
                return;
            }
            stonumber = stonumberedittext.getText().toString();
        } else {
            if (isEmpty(borrowerhpnumberedittext)) {
                Toast.makeText(this, "Please enter the handphone number", Toast.LENGTH_SHORT).show();
                return;
            }
            hpnumber = borrowerhpnumberedittext.getText().toString();
        }
        indentViewModel.insert(new Indent(isincomingindent, borrowername, company, formattedDate, hpnumber, stonumber, indentlist, summary));
        updateInventory(indentlist, inventory);
        Toast.makeText(this, "Indent saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void updateInventory(List<LoanListItem> indentlist, List<StoreItem> inventory) {
        int indentcount = indentlist.size();
        int inventorycount = inventory.size();

        for (int i = 0; i < indentcount; i ++) {
            for (int c = 0; c < inventorycount; c ++) {
                StoreItem cursor = inventory.get(c);
                if (indentlist.get(i).getmItemname().compareTo(cursor.getName()) == 0) {
                    int updatedquantity = 0;
                    if (isincomingindent) {
                        updatedquantity = cursor.getQuantity() + indentlist.get(i).getmQuantity();
                    } else {
                        updatedquantity = cursor.getQuantity() - indentlist.get(i).getmQuantity();
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
