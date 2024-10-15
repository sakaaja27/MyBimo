package auth;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
//    buat nama DB
    private static final String DATABASE_NAME = "project.db";
//    versi DB
    private static final int DATABASE_VERSION = 1;
// menginisialisasi objek DBHelper dan membuat atau membuka koneksi ke database SQLite.
    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
//    inisialisasi tabel (buat table baru yg bernama users dengan isian dibawah)
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY, name TEXT,email TEXT,phone INT,password TEXT)");
    }
// Fungsi  dari kode ini adalah untuk memperbarui struktur database ketika versi
// database aplikasi ditingkatkan

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}