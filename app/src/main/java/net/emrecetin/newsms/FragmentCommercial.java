package net.emrecetin.newsms;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FragmentCommercial extends Fragment {

    View v;
    private RecyclerView recyclerView;
    private List<SmsVO> commercialSmsList;
    private DatabaseOperation dbo;

    public FragmentCommercial() {
        commercialSmsList = new ArrayList<SmsVO>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbo = new DatabaseOperation(getContext());
        dbo.dbSessionOpen();

        commercialSmsList = dbo.getSmsListStatus(3);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.white_fragment,container,false);
        recyclerView = v.findViewById(R.id.white_recyclerview);
        RecyclerViewAdapterSms recyclerViewAdapterSms = new RecyclerViewAdapterSms(getContext(), commercialSmsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapterSms);
        return v;
    }

    @Override
    public void onResume() {
        dbo.dbSessionOpen();
        super.onResume();
    }

    @Override
    public void onPause() {
        dbo.dbSessionClose();
        super.onPause();
    }
}
