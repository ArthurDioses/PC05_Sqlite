package com.dioses.pc05;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

public class SearchEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_edit);
    }

    public void searchByCode(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "upn", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();
        /*
        Cursor fila = bd.rawQuery("select codigoupn,dni,nombre from matricula where codigoupn='" + getInputCodeUpn()+"'", null);
        if (fila.moveToFirst()) {
            tv_data.setText(fila.getString(0)+fila.getString(1)+fila.getString(2));
        } else {

            Toast.makeText(this, "No existe articulo para ese código", Toast.LENGTH_SHORT).show();
        }
         */
    }
}