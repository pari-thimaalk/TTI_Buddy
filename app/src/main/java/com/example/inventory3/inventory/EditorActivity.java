package com.example.inventory3.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProvider;

import com.example.inventory3.R;
import com.example.inventory3.inventory.mvvm.StoreItem;
import com.example.inventory3.inventory.mvvm.StoreItemViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    StoreItemViewModel storeItemViewModel;

    //Tags for passing in storeitem from mainactivity
    public static final String extraname = "EXTRA_ITEM_NAME";
    public static final String extracategory = "EXTRA_ITEM_CATEGORY";
    public static final String extraquantity = "EXTRA_ITEM_QUANTITY";
    public static final String extrasummary = "EXTRA_ITEM_SUMMARY";
    public static final String extraid = "EXTRA_ITEM_ID";


    /** EditText fields to enter the item's details */
    private EditText mNameEditText;
    private Spinner mCategorySpinner;
    private String mCategory;
    private EditText mQuantityEditText;
    private EditText mSummaryEditText;

    //only used when a preexisting item is being updated/deleted
    private boolean isupdate = false;
    private int _id;
    private String dname;
    private String dcategory;
    private int dquantity;
    private String dsummary;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        mNameEditText = (EditText) findViewById(R.id.nameEditText);
        mCategorySpinner = (Spinner) findViewById(R.id.categoryEditText);
        mQuantityEditText = (EditText) findViewById(R.id.quantityEditText);
        mSummaryEditText = (EditText) findViewById(R.id.summaryEditText);

        //TODO: move this array to resources and extract from there
        List<String> list = new ArrayList<>(Arrays.asList("accoms", "clothing, no.1", "clothing, no.3", "clothing, no.4", "laundry", "stationery", "field equipment", "toner"));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        mCategorySpinner.setAdapter(adapter);
        mCategorySpinner.setOnItemSelectedListener(this);

        storeItemViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(StoreItemViewModel.class);

        Intent intent = getIntent();
        //If activity was opened through onclicklistener, change title to "edit" and insert
        //item's data, instead of passing in empty fields. Also saves the item's details in
        //a global variable in case it has to be deleted.
        if (intent.hasExtra(extraname)) {
            setTitle("Edit Item");
            isupdate = true;
            dname = intent.getStringExtra(extraname);
            dcategory = intent.getStringExtra(extracategory);
            dquantity = intent.getIntExtra(extraquantity, -1);
            dsummary = intent.getStringExtra(extrasummary);
            _id = intent.getIntExtra(extraid, -1);

            mNameEditText.setText(dname);
            int sldkfn = 0;
            for (int i = 0; i < list.size(); i ++) {
                if (dcategory.compareTo(list.get(i)) == 0) {
                    sldkfn = i;
                    break;
                }
            }
            mCategorySpinner.setSelection(sldkfn);
            mQuantityEditText.setText(String.valueOf(dquantity));
            if (dsummary != null) {
                mSummaryEditText.setText(dsummary);
            }

        }
        else {
            setTitle("Add an item");
        }
    }

    private void insertitem() {
        if (isEmpty(mQuantityEditText) || isEmpty(mNameEditText)) {
            Toast.makeText(this, "Please ensure there is a name and quantity", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = mNameEditText.getText().toString();

        int quantity = Integer.parseInt(mQuantityEditText.getText().toString());

        if (quantity < 0) {
            Toast.makeText(this, "Please enter a valid quantity", Toast.LENGTH_SHORT).show();
            return;
        }
        String summary = mSummaryEditText.getText().toString();

        storeItemViewModel.insert(new StoreItem(name, mCategory, quantity, summary));
        Toast.makeText(this, "Item saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void updateitem() {
        if (isEmpty(mQuantityEditText) || isEmpty(mNameEditText)) {
            Toast.makeText(this, "Please ensure there is a name and quantity", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = mNameEditText.getText().toString();
        int quantity = Integer.parseInt(mQuantityEditText.getText().toString());
        if (quantity < 0) {
            Toast.makeText(this, "Please enter a valid quantity", Toast.LENGTH_SHORT).show();
            return;
        }
        String summary = mSummaryEditText.getText().toString();

        StoreItem updateditem = new StoreItem(name, mCategory, quantity, summary);
        updateditem.setId(_id);
        storeItemViewModel.update(updateditem);

        Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void deleteitem() {
        StoreItem deleteditem = new StoreItem(dname, dcategory, dquantity, dsummary);
        deleteditem.setId(_id);
        storeItemViewModel.delete(deleteditem);
        Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
        finish();
    }

    //checks if edittext fields are empty
    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_save:
                if (isupdate == true) { updateitem(); }
                else { insertitem(); }
                return true;
            case R.id.action_delete:
                if (isupdate == true) { deleteitem(); }
                else { NavUtils.navigateUpFromSameTask(this); }
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mCategory = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
