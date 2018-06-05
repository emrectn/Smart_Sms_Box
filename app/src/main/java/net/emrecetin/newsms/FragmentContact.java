package net.emrecetin.newsms;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FragmentContact extends Fragment {

    View v;
    private RecyclerView contactrcyclerview;
    private List<ContactVO> listContact;
    private DatabaseOperation dbo;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public FragmentContact() {
        listContact = new ArrayList<ContactVO>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbo = new DatabaseOperation(getActivity());
        dbo.dbSessionOpen();

        sharedPref = getContext().getSharedPreferences("sensor", Context.MODE_PRIVATE);
        editor = sharedPref.edit();


        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 100);
        }

        while (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){}

        if (sharedPref.getString("ContactList", "-").equals("OK")){
            listContact = dbo.getContactList();
        }else {

            getAllContacts();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.contact_fragment,container,false);
        setRecyclerAdapter();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Contact");
        return v;
    }

    public void setRecyclerAdapter(){
        contactrcyclerview = (RecyclerView) v.findViewById(R.id.contact_recyclerview);
        RecyclerViewAdapterContact recyclerViewAdapterContact = new RecyclerViewAdapterContact(listContact, getContext());
        contactrcyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        contactrcyclerview.setAdapter(recyclerViewAdapterContact);
    }

    private void getAllContacts() {

        Toast.makeText(getContext(), "Rehberiniz Yüklendi...",Toast.LENGTH_LONG).show();
        ContactVO contactVO;

        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                String hasPhoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (Integer.parseInt(hasPhoneNumber) > 0) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    contactVO = new ContactVO(R.drawable.male,name,hasPhoneNumber);
                    contactVO.setContactName(name);

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    if (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactVO.setContactNumber(phoneNumber);
                    }

                    phoneCursor.close();

                    Cursor emailCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCursor.moveToNext()) {
                        String emailId = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    }
                    listContact.add(contactVO);
                    dbo.addContact(contactVO);
                }
            }

            // Contact List Durumu kaydedildi.
            editor.putString("ContactList","OK");
            editor.commit();


        }
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
