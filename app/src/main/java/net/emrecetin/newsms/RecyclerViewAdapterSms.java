package net.emrecetin.newsms;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapterSms extends RecyclerView.Adapter<RecyclerViewAdapterSms.MyViewHolder> {

    private Context mContext;
    private List<SmsVO> smsVOList;
    private Dialog mydialog;
    private DatabaseOperation dbo;

    public RecyclerViewAdapterSms(Context mContext, List<SmsVO> smsVOList) {
        this.mContext = mContext;
        this.smsVOList = smsVOList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_sms, null);
        final RecyclerViewAdapterSms.MyViewHolder smsViewHolder = new RecyclerViewAdapterSms.MyViewHolder(view);

        dbo = new DatabaseOperation(mContext);

        mydialog = new Dialog(mContext);
        mydialog.setContentView(R.layout.dialog_contact);
        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        smsViewHolder.item_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ImageView dialog_img = mydialog.findViewById(R.id.dialog_img);
                final TextView dialog_name_tv = mydialog.findViewById(R.id.dialog_name);
                final TextView dialog_phone_tv = mydialog.findViewById(R.id.dialog_phone);

                Button read_button = mydialog.findViewById(R.id.dialog_button);
                Button delete_button = mydialog.findViewById(R.id.spam_button);

                dialog_name_tv.setText(smsVOList.get(smsViewHolder.getAdapterPosition()).getAddress());
                dialog_phone_tv.setText("");

                read_button.setText("READ");
                delete_button.setText("DELETE");
                dialog_img.setImageResource(R.drawable.mail);

                delete_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dbo.dbSessionOpen();
                        dbo.deleteSms(smsVOList.get(smsViewHolder.getAdapterPosition()));
                        smsVOList.remove(smsViewHolder.getAdapterPosition());
                        Toast.makeText(mContext,"Sms Silindi", Toast.LENGTH_LONG).show();
                        notifyItemRemoved(smsViewHolder.getAdapterPosition());
                        mydialog.dismiss();
                        dbo.dbSessionClose();
                    }
                });

                read_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent show_sms_intent = new Intent(mContext, MessagesActivity.class);
                        show_sms_intent.putExtra("Tel",dialog_name_tv.getText());
                        show_sms_intent.putExtra("Msg",smsVOList.get(smsViewHolder.getAdapterPosition()).getMsg());
                        mContext.startActivity(show_sms_intent);
                        mydialog.dismiss();
                    }
                });

                mydialog.show();


            }
        });


        return smsViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SmsVO smsVO = smsVOList.get(position);
        Long smsTime = Long.parseLong(smsVO.getTime());
        Date date=new Date(smsTime);

        holder.sms_address.setText(smsVO.getAddress());
        holder.sms_body.setText(smsVO.getMsg());

        holder.sms_date.setText(DateFormat.getDateInstance().format(date)+" "+(((smsTime / (1000 * 60 * 60)) % 24 )+3)+":"+ (smsTime / (1000 * 60)) % 60+":"+(smsTime / 1000) % 60);
        holder.txt_type.setText(""+smsVO.getStatus());

        int status = smsVO.getType();
        if (status == 2){
            holder.sms_type.setImageResource(R.drawable.outgoing_sms);
        }
        else{
            holder.sms_type.setImageResource(R.drawable.incoming_sms);
        }
    }

    @Override
    public int getItemCount() {
        return smsVOList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView sms_address;
        private TextView sms_body;
        private TextView sms_date;
        private ImageView sms_type;
        private TextView txt_type;
        private LinearLayout item_sms;
        public MyViewHolder(View itemView) {
            super(itemView);
            item_sms = itemView.findViewById(R.id.sms_item_id);
            sms_address = (TextView) itemView.findViewById(R.id.address_sms);
            sms_body = (TextView) itemView.findViewById(R.id.body_sms);
            sms_date = (TextView) itemView.findViewById(R.id.date_sms);
            sms_type = (ImageView) itemView.findViewById(R.id.img_type_sms);
            txt_type = itemView.findViewById(R.id.txt_type);
        }
    }
}
