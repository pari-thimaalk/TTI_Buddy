package com.example.inventory3.directissue;

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
import com.example.inventory3.directissue.mvvm.Indent;
import com.example.inventory3.directissue.mvvm.IndentViewModel;
import com.example.inventory3.loanledger.NewLoanActivity;
import com.example.inventory3.loanledger.mvvm.Converters;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class DirectIssueFragment extends Fragment {
    IndentViewModel viewModel;
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
        View view = inflater.inflate(R.layout.layout_directissue, container, false);
        homefab = (FloatingActionButton) view.findViewById(R.id.homefab);
        outgoingloanfab = (FloatingActionButton) view.findViewById(R.id.outgoingloanfab);
        incomingloanfab = (FloatingActionButton) view.findViewById(R.id.incomingloanfab);
        outgoingloantv = (TextView) view.findViewById(R.id.outgoingloantv);
        incomingloantv = (TextView) view.findViewById(R.id.incomingloantv);
        emptyimage = view.findViewById(R.id.emptyimageview);
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
                Intent intent = new Intent(getActivity(), NewIndentActivity.class);
                intent.putExtra(NewLoanActivity.key, "outgoing");
                startActivity(intent);
            }
        });
        incomingloanfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewIndentActivity.class);
                intent.putExtra(NewLoanActivity.key, "incoming");
                startActivity(intent);
            }
        });
        
        RecyclerView recyclerView = view.findViewById(R.id.loanledger_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        final DILedgerAdapter adapter = new DILedgerAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setmContext(getContext());
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(IndentViewModel.class);
        viewModel.getAllIndents().observe(getViewLifecycleOwner(), new Observer<List<Indent>>() {
            @Override
            public void onChanged(List<Indent> indents) {
                adapter.setAllIndents(indents);
                adapter.notifyDataSetChanged();
                if (adapter.getItemCount() != 0) {
                    emptyimage.setVisibility(View.GONE);
                } else {
                    emptyimage.setVisibility(View.VISIBLE);
                }
            }
        });

        adapter.setOnItemClickListener(new DILedgerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Indent indent) {
                Intent intent = new Intent(getActivity(), IndentDetailActivity.class);
                intent.putExtra(IndentDetailActivity.extraname, indent.getName());
                intent.putExtra(IndentDetailActivity.extracompany, indent.getReceivercompany());
                intent.putExtra(IndentDetailActivity.extraissuedate, indent.getReceiveddate());
                intent.putExtra(IndentDetailActivity.extraloanitemlist, Converters.from_List(indent.getItemlist()));
                intent.putExtra(IndentDetailActivity.extrasummary, indent.getSummary());
                intent.putExtra(IndentDetailActivity.extraid, indent.getId());
                intent.putExtra(IndentDetailActivity.extraisincoming, indent.getIsincoming());
                intent.putExtra(IndentDetailActivity.extrahpnumber, indent.getHpnumber());
                intent.putExtra(IndentDetailActivity.extrastorefnumber, indent.getStorefnumber());
                startActivity(intent);
            }
        });
        return view;
    }
}
