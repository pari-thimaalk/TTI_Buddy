package com.example.inventory3.loanledger;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventory3.R;
import com.example.inventory3.directissue.NewIndentActivity;
import com.example.inventory3.loanledger.mvvm.Converters;
import com.example.inventory3.loanledger.mvvm.Loan;
import com.example.inventory3.loanledger.mvvm.LoanViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class LoanLedgerFragment extends Fragment {
    LoanViewModel loanViewModel;
    FloatingActionButton homefab;
    FloatingActionButton outgoingloanfab;
    FloatingActionButton incomingloanfab;
    TextView outgoingloantv;
    TextView incomingloantv;
    private boolean clicked = false;
    ImageView emptyimage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_loanledger, container, false);
        emptyimage = view.findViewById(R.id.emptyimageview);
        homefab = (FloatingActionButton) view.findViewById(R.id.homefab);
        outgoingloanfab = (FloatingActionButton) view.findViewById(R.id.outgoingloanfab);
        incomingloanfab = (FloatingActionButton) view.findViewById(R.id.incomingloanfab);
        outgoingloantv = (TextView) view.findViewById(R.id.outgoingloantv);
        incomingloantv = (TextView) view.findViewById(R.id.incomingloantv);
        homefab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!clicked) {
                    outgoingloanfab.setVisibility(View.VISIBLE);
                    incomingloanfab.setVisibility(View.VISIBLE);
                    outgoingloantv.setVisibility(View.VISIBLE);
                    incomingloantv.setVisibility(View.VISIBLE);
                }
                else {
                    outgoingloanfab.setVisibility(View.INVISIBLE);
                    incomingloanfab.setVisibility(View.INVISIBLE);
                    outgoingloantv.setVisibility(View.INVISIBLE);
                    incomingloantv.setVisibility(View.INVISIBLE);
                }
                clicked = !clicked;
            }
        });
        outgoingloanfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewLoanActivity.class);
                intent.putExtra(NewIndentActivity.key, "outgoing");
                startActivity(intent);
            }
        });
        incomingloanfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewLoanActivity.class);
                intent.putExtra(NewIndentActivity.key, "incoming");
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.loanledger_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        final LoanLedgerAdapter adapter = new LoanLedgerAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setmContext(getContext());
        loanViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(LoanViewModel.class);
        loanViewModel.getAllLoans().observe(getViewLifecycleOwner(), new Observer<List<Loan>>() {
            @Override
            public void onChanged(List<Loan> loans) {
                adapter.setLoans(loans);
                adapter.notifyDataSetChanged();
                if (adapter.getItemCount() != 0) {
                    emptyimage.setVisibility(View.GONE);
                } else {
                    emptyimage.setVisibility(View.VISIBLE);
                }
            }
        });

        adapter.setOnItemClickListener(new LoanLedgerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Loan loan) {
                Intent intent = new Intent(getActivity(), LoanDetailActivity.class);
                intent.putExtra(LoanDetailActivity.extraname, loan.getBorrowername());
                intent.putExtra(LoanDetailActivity.extracompany, loan.getBorrowercompany());
                intent.putExtra(LoanDetailActivity.extraissuedate, loan.getIssuedate());
                intent.putExtra(LoanDetailActivity.extrareturndate, loan.getReturndate());
                intent.putExtra(LoanDetailActivity.extrasummary, loan.getSummary());
                intent.putExtra(LoanDetailActivity.extraid, loan.getId());
                intent.putExtra(LoanDetailActivity.extraloanitemlist, Converters.from_List(loan.getItemlist()));
                intent.putExtra(LoanDetailActivity.extraisincoming, loan.getIsincoming());
                intent.putExtra(LoanDetailActivity.extramobilenumber, loan.getHpnumber());
                startActivity(intent);
            }
        });



        return view;
    }
}
