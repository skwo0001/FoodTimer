package com.szeyankwok.foodtimer;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;


public class AddActivity extends AppCompatActivity {

    NumberPicker hrNP,minNP, secNP;
    EditText foodNameET;
    Spinner categorySpinner;

    private DataBaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add);
        db = new DataBaseHelper(this);

        foodNameET = (EditText) findViewById(R.id.addName);
        categorySpinner = (Spinner) findViewById(R.id.spinner);
        hrNP = (NumberPicker) findViewById(R.id.hrNP);
        minNP = (NumberPicker) findViewById(R.id.minNP);
        secNP = (NumberPicker) findViewById(R.id.secNP);

        hrNP.setMaxValue(23);
        secNP.setMaxValue(59);
        minNP.setMaxValue(59);


        Cursor cat = db.getCategory();

        String[] catArray = arrayFromCursor(cat);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_item,catArray);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        categorySpinner.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save){

            String name = foodNameET.getText().toString().trim();
            String category = categorySpinner.getSelectedItem().toString().trim();
            int hr = hrNP.getValue();
            int min = minNP.getValue();
            int sec = secNP.getValue();

            String time  = Long.toString (hr * 60 + min + sec/60);



            if (name.equals("") || category.equals("") || (hr == 0 && min == 0 && sec == 0))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Invalid Input");

                final TextView message = new TextView(this);
                builder.setMessage("Please input validate data!");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            } else {

                askForSave(name,category,time);
            }
        }

        return true;
    }

    private void askForSave(final String name, final String category, final String time){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm to Add");

        final TextView message = new TextView(this);
        builder.setMessage("Are you sure to add this new food?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                db.addFood(name,category,time,false);
                goBack();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    public void goBack(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public String[] arrayFromCursor(Cursor cursor) {
        int length = cursor.getCount();
        String[] array = new String[length];

        if (cursor.moveToFirst()){
            for (int i = 0; i< length; i++){
                array[i] = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T1_COL_3));
                cursor.moveToNext();
            }
        }

        return array;
    }



}

