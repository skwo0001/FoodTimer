package com.szeyankwok.foodtimer;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FavouriteFragment extends Fragment{

    View view;
    Context context;
    RecyclerView recyclerView;
    SearchView searchView;
    TextView msg;
    private DataBaseHelper db;
    private FavouriteAdapter favouriteAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        //Find the value in shared preferences
        view = inflater.inflate(R.layout.fragment_fav, container, false);
        context = view.getContext();

        recyclerView = (RecyclerView) view.findViewById(R.id.foodfavRV);
        msg = (TextView) view.findViewById(R.id.showFavMsg);
        searchView = (SearchView) view.findViewById(R.id.searchFav);

        SwipeController swipeController = new SwipeController();
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        msg.setVisibility(View.GONE);

        db = new DataBaseHelper(context);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //link the data from database and show it in the recycler view

                if (s.equals("")){
                    favouriteAdapter = new FavouriteAdapter(context, db.getFoodbyFav());
                    recyclerView.setAdapter(favouriteAdapter);
                } else {

                    Cursor cursor = db.searchFavFood(s);

                    int i = cursor.getCount();

                    favouriteAdapter = new FavouriteAdapter(context, db.searchFavFood(s));
                    recyclerView.setAdapter(favouriteAdapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                if (s.equals("")){
                    favouriteAdapter = new FavouriteAdapter(context, db.getFoodbyFav());
                    recyclerView.setAdapter(favouriteAdapter);
                } else {

                    Cursor cursor = db.searchFavFood(s);

                    int i = cursor.getCount();

                    favouriteAdapter = new FavouriteAdapter(context, db.searchFavFood(s));
                    recyclerView.setAdapter(favouriteAdapter);
                }
                return false;
            }
        });



        Cursor cursor = db.getFoodbyFav();

        int i = cursor.getCount();

        if (cursor.getCount() == 0){
            msg.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            searchView.setVisibility(View.GONE);
        }
        else {
            //link the data from database and show it in the recycler view
            favouriteAdapter = new FavouriteAdapter(context, db.getFoodbyFav());
            recyclerView.setAdapter(favouriteAdapter);
        }
        return view;

    }
}
