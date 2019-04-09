package com.szeyankwok.foodtimer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class EditActivity extends AppCompatActivity {

    NumberPicker hrNP,minNP, secNP;
    EditText foodNameET;
    Spinner categorySpinner;
    String foodId;

    private DataBaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add);
        db = new DataBaseHelper(this);

        Intent intent = getIntent();

        foodNameET = (EditText) findViewById(R.id.addName);
        categorySpinner = (Spinner) findViewById(R.id.spinner);
        hrNP = (NumberPicker) findViewById(R.id.hrNP);
        minNP = (NumberPicker) findViewById(R.id.minNP);
        secNP = (NumberPicker) findViewById(R.id.secNP);

        hrNP.setMaxValue(23);
        secNP.setMaxValue(59);
        minNP.setMaxValue(59);

        foodId = intent.getStringExtra("id");

        Cursor cat = db.getCategory();

        String[] catArray = arrayFromCursor(cat);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_item,catArray);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        categorySpinner.setAdapter(adapter);

        Cursor detail = db.getFood(foodId);

        String foodName = detail.getString(detail.getColumnIndex(DataBaseHelper.T1_COL_2));
        String foodTime = detail.getString(detail.getColumnIndex(DataBaseHelper.T1_COL_4));
        String foodCat = detail.getString(detail.getColumnIndex(DataBaseHelper.T1_COL_3));

        foodNameET.setText(foodName);

        long time  = Long.parseLong(foodTime) * 60000;

        int hrs = 0;
        int minutes = (int) (time / 1000) / 60;
        int seconds = (int) (time / 1000) % 60;

        if (minutes > 59) {
            hrs = minutes / 60;
            minutes = minutes % 60;
        }

        hrNP.setValue(hrs);
        minNP.setValue(minutes);
        secNP.setValue(seconds);

        categorySpinner.setSelection(adapter.getPosition(foodCat));

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
        builder.setTitle("Confirm to Save");

        final TextView message = new TextView(this);
        builder.setMessage("Are you sure to save the changes?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                db.editInfo(foodId,name,category,time);
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
