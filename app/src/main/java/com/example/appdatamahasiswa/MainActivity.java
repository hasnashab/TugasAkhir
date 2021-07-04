package com.example.appdatamahasiswa;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;
    DBHelper helper;
    LayoutInflater inflater;
    View dialogView;
    TextView view_NIM, view_Nama, view_Tanggal, view_JK, view_Alamat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Add_Data.class));
            }
        });
                helper = new DBHelper(this);
                listView = (ListView)findViewById(R.id.ListData);
                listView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setListView(){
        Cursor cursor = helper.allData();
        CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(this,cursor,1);
        listView.setAdapter(customCursorAdapter);
    }

    public void onItemClick(AdapterView<?> parent, View view, int i, long x) {
        TextView getId = (TextView)view.findViewById(R.id.listID);
        final long id = Long.parseLong(getId.getText().toString());
        final Cursor cur = helper.oneData(id);
        cur.moveToFirst();

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Pilih Opsi");

        String[] options = {"Lihat Data", "Edit Data", "Hapus Data"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        final AlertDialog.Builder viewdata = new AlertDialog.Builder(MainActivity.this);
                        inflater = getLayoutInflater();
                        dialogView = inflater.inflate(R.layout.view_data,null);
                        viewdata.setView(dialogView);
                        viewdata.setTitle("Lihat Data");

                        view_NIM = (TextView)dialogView.findViewById(R.id.viewNIM);
                        view_Nama = (TextView)dialogView.findViewById(R.id.viewNama);
                        view_Tanggal = (TextView)dialogView.findViewById(R.id.viewTanggal);
                        view_JK = (TextView)dialogView.findViewById(R.id.view_JK);
                        view_Alamat = (TextView)dialogView.findViewById(R.id.viewAlamat);

                        view_NIM.setText("NIM: "+ cur.getString(cur.getColumnIndex(DBHelper.row_nim)));
                        view_Nama.setText("Nama: " + cur.getString(cur.getColumnIndex(DBHelper.row_nama)));
                        view_Tanggal.setText("Tanggal Lahir: " + cur.getString(cur.getColumnIndex(DBHelper.row_tglLahir)));
                        view_JK.setText("Gender: " + cur.getString(cur.getColumnIndex(DBHelper.row_jk)));
                        view_Alamat.setText("Alamat: " + cur.getString(cur.getColumnIndex(DBHelper.row_alamat)));

                        viewdata.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        viewdata.show();
                }
                switch (which){
                    case 1:
                        Intent iddata = new Intent(MainActivity.this,Edit_Data.class);
                        iddata.putExtra(DBHelper.row_id, id);
                        startActivity(iddata);
                }
                switch (which){
                    case 2:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                        builder1.setMessage("Data ini akan dihapus.");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                helper.deleteData(id);
                                Toast.makeText(MainActivity.this, "Data terhapus!", Toast.LENGTH_SHORT).show();
                                setListView();
                            }
                        });
                        builder1.setNegativeButton("batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder1.create();
                        alertDialog.show();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListView();
    }
}