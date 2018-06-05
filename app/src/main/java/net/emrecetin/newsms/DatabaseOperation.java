package net.emrecetin.newsms;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseOperation  {
    SQLiteDatabase db;
    Database mydb;

    public DatabaseOperation(Context context) {
        mydb = new Database(context);
    }

    // Veri tabanına bağlantı aç
    public void dbSessionOpen() {
        // Eğer veritabanı yoksa yaratır varsa sadece erişim sağlıcak yol
        db = mydb.getWritableDatabase();
    }

    public void dbSessionClose() {
        mydb.close();
    }

    public void addContact(ContactVO contact){
        ContentValues val = new ContentValues();
        val.put("name", contact.getContactName());
        val.put("telephone", contact.getContactNumber());
        db.insert("t_contacts", null, val);
        Log.i("NEWCONTACT", contact.getContactName());
    }

    public void addSms(SmsVO sms){
        ContentValues val = new ContentValues();
        val.put("body", sms.getMsg());
        val.put("address", sms.getAddress());
        val.put("latitude", sms.getLatitude());
        val.put("longitude", sms.getLongitude());
        val.put("status", sms.getStatus());
        val.put("time", sms.getTime());
        val.put("type", sms.getType());
        db.insert("t_messages", null, val);
        Log.i("NEWCONTACT", sms.getAddress());
        Log.i("NEWCONTACT - LOCATİON", sms.getLatitude()+ "  " + sms.getLongitude());
    }

    public void addBlackListMember(String name, String telephone){
        ContentValues val = new ContentValues();
        val.put("name", name);
        val.put("telephone", telephone);
        db.insert("t_blacklist", null, val);
        Log.i("NEWBLACKLIST", telephone);
    }

    public List<ContactVO> getContactList() {
        List<ContactVO> contactVOList = new ArrayList<ContactVO>();

        String coloum [] = {"id", "name", "telephone"};
        Cursor c = db.query("t_contacts",coloum, null,null,null,null, null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            int id = c.getInt(0);
            String name = c.getString(1);
            String telephone = c.getString(2);
            ContactVO contact = new ContactVO(id,name,telephone);
            contact.setContactImage(R.drawable.male);
            contactVOList.add(contact);
            c.moveToNext();
        }
        return contactVOList;
    }

    public List<SmsVO> getSmsList() {
        List<SmsVO> smsList = new ArrayList<SmsVO>();
        String coloum [] = {"id", "body", "address","latitude","longitude","status","type","time"};
        Cursor c = db.query("t_messages", coloum, null,null,null,null,"time"+" DESC");
        c.moveToFirst();

        while (!c.isAfterLast()){
            SmsVO sms = new SmsVO();
            sms.setId(c.getString(0));
            sms.setMsg(c.getString(1));
            sms.setAddress(c.getString(2));
            sms.setLatitude(c.getDouble(3));
            sms.setLongitude(c.getDouble(4));
            sms.setStatus(c.getInt(5));
            sms.setType(c.getInt(6));
            sms.setTime(c.getString(7));

            sms.setAddress(getContactName(sms.getAddress()));
            smsList.add(sms);
            c.moveToNext();
        }
        return smsList;
    }

    public List<SmsVO> getSmsListStatus(int Status) {
        List<SmsVO> smsList = new ArrayList<SmsVO>();
        String coloum [] = {"id", "body", "address","latitude","longitude","status","type","time"};
        Cursor c = db.query("t_messages", coloum, "status="+Status,null,null,null,"time"+" DESC");
        c.moveToFirst();

        while (!c.isAfterLast()){
            SmsVO sms = new SmsVO();
            sms.setId(c.getString(0));
            sms.setMsg(c.getString(1));
            sms.setAddress(c.getString(2));
            sms.setLatitude(c.getDouble(3));
            sms.setLongitude(c.getDouble(4));
            sms.setStatus(c.getInt(5));
            sms.setType(c.getInt(6));
            sms.setTime(c.getString(7));

            sms.setAddress(getContactName(sms.getAddress()));
            smsList.add(sms);
            c.moveToNext();
        }
        return smsList;
    }

    public List<String> getBlackList(){
        List<String> blackList = new ArrayList<String>();
        String coloum [] = {"id", "name", "telephone"};
        Cursor c = db.query("t_blacklist", coloum, null,null,null,null,null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            String listeleman = c.getInt(0) + "-" + c.getString(1) + "-" + c.getString(2);
            blackList.add(listeleman);
            c.moveToNext();
        }
        return blackList;
    }

    public void deleteContact(ContactVO contact){
        int id = contact.getId();
        db.delete("t_contacts", "id="+id,null);
    }

    public void deleteSms(SmsVO sms){
        String id = sms.getId();
        db.delete("t_messages", "id="+id,null);
    }

    public void deleteBlackList(String tel){
        db.delete("t_blacklist", "telephone LIKE '"+tel+"'",null);
    }

    public String getContactName(String number) {
        number = number.replaceAll("\\+9","");
        Cursor c = db.rawQuery("SELECT *FROM t_contacts WHERE telephone LIKE '" + number + "'",null );
        if (c.moveToFirst()){
            return c.getString(1);
        }
        return number;
    }

    public void getSms(int id){
        Cursor c = db.rawQuery("SELECT * FROM t_messages WHERE id='" + id + "'" ,null);
        if (c.moveToFirst()){
            Log.i("SORGU", "id : " + c.getInt(0));
            Log.i("SORGU", "body : " + c.getString(1));
            Log.i("SORGU", "address : " + c.getString(2));
        }
    }

    public boolean isContact(String number){
        number=number.replaceAll("\\+9","");
        Cursor c = db.rawQuery("SELECT *FROM t_contacts WHERE telephone LIKE '" + number + "'",null );
        if (c.moveToFirst()){
            return true;
        }
        return false;
    }

    public boolean isBlocked(String number){
        number = number.replaceAll("\\+9","");
        Cursor c = db.rawQuery("SELECT *FROM t_blacklist WHERE telephone LIKE '" + number + "'",null );
        if (c.moveToFirst()){
            return true;
        }
        return false;
    }
}

