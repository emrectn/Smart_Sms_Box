package net.emrecetin.newsms;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RecyclerViewAdapterContact extends RecyclerView.Adapter<RecyclerViewAdapterContact.ContactViewHolder> {

    private List<ContactVO> contactVOList;
    private Context mContext;
    private Dialog mydialog;
    private DatabaseOperation dbo;

    public RecyclerViewAdapterContact(List<ContactVO> contactVOList, Context mContext) {
        this.contactVOList = contactVOList;
        this.mContext = mContext;
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout item_contact;
        private TextView tv_name;
        private TextView tv_phone;
        private ImageView img;

        public ContactViewHolder(View itemView) {
            super(itemView);
            item_contact = (LinearLayout) itemView.findViewById(R.id.contact_item_id);
            tv_name = (TextView) itemView.findViewById(R.id.name_contact);
            tv_phone = (TextView) itemView.findViewById(R.id.phone_contact);
            img = (ImageView) itemView.findViewById(R.id.img_contact);
        }
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_contact, null);
        final ContactViewHolder contactViewHolder = new ContactViewHolder(view);

        dbo = new DatabaseOperation(mContext);

        //Dialog initalize
        mydialog = new Dialog(mContext);
        mydialog.setContentView(R.layout.dialog_contact);
        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        contactViewHolder.item_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(mContext, "TestClick" + contactVOList.get(contactViewHolder.getAdapterPosition()).getContactName(),Toast.LENGTH_LONG).show();
                final TextView dialog_name_tv = mydialog.findViewById(R.id.dialog_name);
                final TextView dialog_phone_tv = mydialog.findViewById(R.id.dialog_phone);

                Button dialog_button = mydialog.findViewById(R.id.dialog_button);
                Button spam_button = mydialog.findViewById(R.id.spam_button);

                dialog_name_tv.setText(contactVOList.get(contactViewHolder.getAdapterPosition()).getContactName());
                dialog_phone_tv.setText(contactVOList.get(contactViewHolder.getAdapterPosition()).getContactNumber());
                mydialog.show();

                dialog_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View view) {

                        Intent send_sms_intent = new Intent(mContext, SendSmsActivity.class);
                        send_sms_intent.putExtra("Tel",dialog_phone_tv.getText());
                        mContext.startActivity(send_sms_intent);
                    }
                });

                spam_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dbo.dbSessionOpen();
                        if(dbo.isBlocked(dialog_phone_tv.getText().toString())){
                            Toast.makeText(mContext, "'"+dialog_name_tv.getText().toString()+"'"+" Önceden Blocklanmış", Toast.LENGTH_LONG).show();
                        }
                        else {
                            dbo.addBlackListMember(dialog_name_tv.getText().toString(), dialog_phone_tv.getText().toString());
                            Toast.makeText(mContext, "'"+dialog_name_tv.getText().toString()+"'"+" Blocklandı.", Toast.LENGTH_LONG).show();
                        }
                        mydialog.dismiss();
                        dbo.dbSessionClose();
                    }
                });


            }
        });
        return contactViewHolder;

    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        ContactVO contactVO = contactVOList.get(position);
        holder.tv_name.setText(contactVO.getContactName());
        holder.tv_phone.setText(contactVO.getContactNumber());
        holder.img.setImageResource(contactVO.getContactImage());

    }

    @Override
    public int getItemCount() {
        return contactVOList.size();
    }

}
