package net.emrecetin.newsms;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class RecyclerViewBlackList extends RecyclerView.Adapter<RecyclerViewBlackList.MyViewHolder> {

    private Context mContext;
    private List<String> blackList;
    private DatabaseOperation dbo;

    public RecyclerViewBlackList(Context mContext, List<String> blackList) {
        this.mContext = mContext;
        this.blackList = blackList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_blacklist, null);
        final MyViewHolder blackListHolder = new MyViewHolder(view);

        dbo = new DatabaseOperation(mContext);

        blackListHolder.item_blacklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbo.dbSessionOpen();
                dbo.deleteBlackList(blackList.get(blackListHolder.getAdapterPosition()).split("-")[2]);
                blackList.remove(blackListHolder.getAdapterPosition());
                Toast.makeText(mContext,"Element Silindi", Toast.LENGTH_LONG).show();
                notifyItemRemoved(blackListHolder.getAdapterPosition());
            }
        });


        return blackListHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String eleman = blackList.get(position);
        System.out.println(eleman);
        String [] parts = eleman.split("-");

        holder.block_name.setText(parts[1]);
        holder.block_phone.setText(parts[2]);
    }

    @Override
    public int getItemCount() {
        return blackList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView block_name;
        private TextView block_phone;
        private LinearLayout item_blacklist;

        public MyViewHolder(View itemView) {
            super(itemView);
            item_blacklist = itemView.findViewById(R.id.blacklist_item_id);
            block_name = (TextView) itemView.findViewById(R.id.block_name);
            block_phone = (TextView) itemView.findViewById(R.id.block_phone);
        }
    }
}
