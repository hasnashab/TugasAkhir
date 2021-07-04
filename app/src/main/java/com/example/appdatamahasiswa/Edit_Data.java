package com.example.appdatamahasiswa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Edit_Data extends AppCompatActivity {

    DBHelper helper;
    EditText TxNIM, TxNama, TxTanggal, TxAlamat;
    Spinner SpJK;
    long id;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__data);

        helper = new DBHelper(this);

        id = getIntent().getLongExtra(DBHelper.row_id, 0);

        TxNIM = (EditText) findViewById(R.id.txNIM_Edit);
        TxNama = (EditText) findViewById(R.id.txNama_Edit);
        TxTanggal = (EditText) findViewById(R.id.txTglLahir_Edit);
        TxAlamat = (EditText) findViewById(R.id.txAlamat_Edit);
        SpJK = (Spinner) findViewById(R.id.spJK_Edit);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        TxTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        getData();
    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                TxTanggal.setText(dateFormatter.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void getData() {
        Cursor cursor = helper.oneData(id);
        if (cursor.moveToFirst()) {
            String nim = cursor.getString(cursor.getColumnIndex(DBHelper.row_nim));
            String nama = cursor.getString(cursor.getColumnIndex(DBHelper.row_nama));
            String jk = cursor.getString(cursor.getColumnIndex(DBHelper.row_jk));
            String tanggal = cursor.getString(cursor.getColumnIndex(DBHelper.row_tglLahir));
            String alamat = cursor.getString(cursor.getColumnIndex(DBHelper.row_alamat));

            TxNIM.setText(nim);
            TxNama.setText(nama);

            if (jk.equals("Perempuan")) {
                SpJK.setSelection(0);
            } else if (jk.equals("Laki=laki")) {
                SpJK.setSelection(1);
            }

            TxTanggal.setText(tanggal);
            TxAlamat.setText(alamat);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.simpan_edit:
            String nim = TxNIM.getText().toString().trim();
            String nama = TxNama.getText().toString().trim();
            String tanggal = TxTanggal.getText().toString().trim();
            String alamat = TxAlamat.getText().toString().trim();
            String jk = SpJK.getSelectedItem().toString().trim();

            ContentValues values = new ContentValues();

                values.put(DBHelper.row_nim, nim);
                values.put(DBHelper.row_nama, nama);
                values.put(DBHelper.row_tglLahir, tanggal);
                values.put(DBHelper.row_alamat, alamat);
                values.put(DBHelper.row_jk, jk);

                if (nim.equals("") || nama.equals("") || tanggal.equals("") || alamat.equals("")) {
                    Toast.makeText(Edit_Data.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    helper.insertData(values);
                    Toast.makeText(Edit_Data.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
        switch (item.getItemId()) {
            case R.id.hapus_edit:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Edit_Data.this);
                builder1.setMessage("Data ini akan dihapus");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        helper.deleteData(id);
                        Toast.makeText(Edit_Data.this, "Data sudah terhapus", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder1.setNegativeButton("Batal",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder1.create();
                alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}