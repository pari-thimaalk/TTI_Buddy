package com.example.inventory3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.inventory3.directissue.DirectIssueFragment;
import com.example.inventory3.inventory.InventoryFragment;
import com.example.inventory3.loanledger.LoanLedgerFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static final String hello = "hello";
    int frag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView nav = findViewById(R.id.bottom_nav);
        nav.setOnNavigationItemSelectedListener(navlistener);

        if (frag == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame, new InventoryFragment()).commit();
        } else if (frag == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame, new LoanLedgerFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame, new DirectIssueFragment()).commit();
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlistener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedfrag = null;

                    switch(item.getItemId()) {
                        case R.id.inventorylist:
                            selectedfrag = new InventoryFragment();
                            frag = 0;
                            break;
                        case R.id.loanledger:
                            selectedfrag = new LoanLedgerFragment();
                            frag = 1;
                            break;
                        case R.id.directissue:
                            selectedfrag = new DirectIssueFragment();
                            frag = 2;
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame, selectedfrag).commit();
                    return true;
                }
            };
}