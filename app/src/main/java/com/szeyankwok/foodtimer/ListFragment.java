package com.szeyankwok.foodtimer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

public class ListFragment extends Fragment {

    View view;
    Context context;
    RecyclerView recyclerView;
    SearchView searchView;
    TextView msg;
    String category;
    private DataBaseHelper db;
    private FavouriteAdapter favouriteAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        //Find the value in shared preferences
        view = inflater.inflate(R.layout.fragment_list, container, false);
        context = view.getContext();


        recyclerView = (RecyclerView) view.findViewById(R.id.foodRV);
        msg = (TextView) view.findViewById(R.id.showListMsg);
        searchView = (SearchView) view.findViewById(R.id.searchFood);

        category = this.getArguments().getString("category");

        msg.setVisibility(View.GONE);

        db = new DataBaseHelper(context);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //link the data from database and show it in the recycler view

                if (s.equals("")){
                    favouriteAdapter = new FavouriteAdapter(context, db.getFoodbyCat(category));
                    recyclerView.setAdapter(favouriteAdapter);
                } else {

                    Cursor cursor = db.searchFavFood(s);

                    int i = cursor.getCount();

                    favouriteAdapter = new FavouriteAdapter(context, db.searchFoodByCat(category,s));
                    recyclerView.setAdapter(favouriteAdapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                if (s.equals("")){
                    if (category.equals("All")){
                        favouriteAdapter = new FavouriteAdapter(context, db.getFoodList());
                    } else {
                        favouriteAdapter = new FavouriteAdapter(context, db.getFoodbyCat(category));
                    }
                    recyclerView.setAdapter(favouriteAdapter);
                } else {
                    if (category.equals("All")){
                        favouriteAdapter = new FavouriteAdapter(context, db.searchFood(s));
                    } else {
                        favouriteAdapter = new FavouriteAdapter(context, db.searchFoodByCat(category,s));
                    }
                    recyclerView.setAdapter(favouriteAdapter);
                }
                return false;
            }
        });

        Cursor cursor;
        if (category.equals("All")){
            cursor = db.getFoodList();
        } else {
            cursor = db.getFoodbyCat(category);
        }

        int i = cursor.getCount();

        if (cursor.getCount() == 0){
            msg.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            searchView.setVisibility(View.GONE);
        }
        else {
            //link the data from database and show it in the recycler view
            if (category.equals("All")){
                favouriteAdapter = new FavouriteAdapter(context, db.getFoodList());
            } else {
                favouriteAdapter = new FavouriteAdapter(context, db.getFoodbyCat(category));
            }
            recyclerView.setAdapter(favouriteAdapter);
        }

        setHasOptionsMenu(true);
        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent add = new Intent(context, AddActivity.class);
            startActivity(add);
        }

        return true;

    }
}