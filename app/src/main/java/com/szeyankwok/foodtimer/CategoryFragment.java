package com.szeyankwok.foodtimer;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

public class CategoryFragment extends Fragment {

    View view;
    Context context;
    RecyclerView recyclerView;
    private DataBaseHelper db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        //Find the value in shared preferences
        view = inflater.inflate(R.layout.fragment_cat, container, false);
        context = view.getContext();

        db = new DataBaseHelper(context);

        CatAdapter catAdapter = new CatAdapter(context,arrayFromCursor(db.getCategory()));
        recyclerView = view.findViewById(R.id.foodcatRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(catAdapter);

        setHasOptionsMenu(true);
        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Add new category");
            final EditText input = new EditText(context);
            float dpi = context.getResources().getDisplayMetrics().density;
            builder.setView(input, (int)(19*dpi), (int)(5*dpi),(int)(14*dpi),(int)(5*dpi));

                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String cat = input.getText().toString().trim();
                    db.addCat(cat);
                    CategoryFragment categoryFragment = new CategoryFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, categoryFragment);
                    fragmentTransaction.commit();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.show();
        }

        return true;

    }

    public String[] arrayFromCursor(Cursor cursor) {
        int length = 1 + cursor.getCount();
        String[] array = new String[length];
        array[0] = "All";

        if (cursor.moveToFirst()){
            for (int i = 1; i< length; i++){
                array[i] = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T2_COL_1));
                cursor.moveToNext();
            }
        }

        return array;
    }
}
