package com.example.appdatamahasiswa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
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

public class Add_Data extends AppCompatActivity {

    DBHelper helper;
    EditText TxNIM, TxNama, TxTanggal, TxAlamat;
    Spinner SpJK;
    long id;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__data);

        helper = new DBHelper(this);

        id = getIntent().getLongExtra(DBHelper.row_id, 0);

        TxNIM = (EditText) findViewById(R.id.txNIM_add);
        TxNama = (EditText) findViewById(R.id.txNama_add);
        TxTanggal = (EditText) findViewById(R.id.txTanggalLahir_add);
        TxAlamat = (EditText) findViewById(R.id.txAlamat_add);
        SpJK = (Spinner) findViewById(R.id.SpJK_add);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        TxTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_add:
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
                    Toast.makeText(Add_Data.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    helper.insertData(values);
                    Toast.makeText(Add_Data.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);
    }
}