package net.emrecetin.newsms;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    private static final String DB_NAME = "storage";
    private static final int DB_VERSION = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_contacts = " create table t_contacts (id integer primary key autoincrement, " +
                "name text not null, " +
                "telephone text not null," +
                "status integer)";

        String sql_message = "create table t_messages (id integer primary key autoincrement," +
                "body text, " +
                "address text, " +
                "latitude double, " +
                "longitude double, " +
                "status integer not null, " +
                "time text, " +
                "type integer)";

        String sql_blacklist = "create table t_blacklist (id integer primary key autoincrement," +
                "name text, " +
                "telephone text)";

        db.execSQL(sql_contacts);
        db.execSQL(sql_message);
        db.execSQL(sql_blacklist);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old_version, int new_version) {
        db.execSQL("drop table if exists t_contacts");
        db.execSQL("drop table if exists t_messages");
        db.execSQL("drop table if exists t_blacklist");
    }
}