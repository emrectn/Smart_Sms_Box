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

public class FragmentOTP extends Fragment {

    View v;
    private RecyclerView recyclerView;
    private List<SmsVO> otpSmsList;
    private DatabaseOperation dbo;

    public FragmentOTP() {
        otpSmsList = new ArrayList<SmsVO>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbo = new DatabaseOperation(getContext());
        dbo.dbSessionOpen();

        otpSmsList = dbo.getSmsListStatus(4);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.white_fragment,container,false);
        recyclerView = v.findViewById(R.id.white_recyclerview);
        RecyclerViewAdapterSms recyclerViewAdapterSms = new RecyclerViewAdapterSms(getContext(), otpSmsList);
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
