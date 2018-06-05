package net.emrecetin.newsms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BlackListActivity extends AppCompatActivity {

    private RecyclerView blacklistrcyc;
    private List<String> blacklist;
    private DatabaseOperation dbo;
    private Button btn_block;
    private EditText edt_blocknumber;
    private RecyclerViewBlackList recyclerViewBlackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);

        dbo = new DatabaseOperation(this);
        dbo.dbSessionOpen();
        blacklist = new ArrayList<String>();
        blacklist = dbo.getBlackList();
        btn_block = findViewById(R.id.btn_add_blacklist);
        edt_blocknumber = findViewById(R.id.edt_blacklist);

        if (blacklist.size() == 0 ){
            blacklist.add("0 - Black List Boş - 00000");
        }
        blacklistrcyc = (RecyclerView) findViewById(R.id.blacklist_recyclerview);
        recyclerViewBlackList = new RecyclerViewBlackList(this, blacklist);
        blacklistrcyc.setLayoutManager(new LinearLayoutManager(this));
        blacklistrcyc.setAdapter(recyclerViewBlackList);

        btn_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = edt_blocknumber.getText().toString();
                if (phone.equals("") || dbo.isBlocked(phone)){
                    Toast.makeText(getBaseContext(),"Geçerli Bir Numara Girin",Toast.LENGTH_LONG).show();
                }
                else{

                    String name = dbo.getContactName(phone);
                    dbo.addBlackListMember(name,phone);
                    Toast.makeText(getBaseContext(),"BlackList'e Eklendi",Toast.LENGTH_LONG).show();
                    edt_blocknumber.setText("");
                    blacklist.add(0,"1-"+name+"-"+phone);

                    recyclerViewBlackList.notifyItemInserted(0);
                    recyclerViewBlackList.notifyDataSetChanged();
                }

            }
        });
    }

    @Override
    protected void onPause() {
        dbo.dbSessionClose();
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        dbo.dbSessionOpen();
        super.onPostResume();
    }
}
