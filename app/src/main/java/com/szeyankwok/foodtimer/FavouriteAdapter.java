package com.szeyankwok.foodtimer;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.SuggestionViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public FavouriteAdapter(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }

    public class SuggestionViewHolder extends RecyclerView.ViewHolder{

        TextView titleText;
        ImageView favImg;
        LinearLayout linearLayout;

        public SuggestionViewHolder( View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.favFood);
            favImg = itemView.findViewById(R.id.fav_btn);
            linearLayout = itemView.findViewById(R.id.LinearFav);
        }
    }

    @NonNull
    @Override
    public SuggestionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.listitem, viewGroup, false);

        return new SuggestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionViewHolder suggestionViewHolder, int i) {

        final String foodid = mCursor.getString(mCursor.getColumnIndex(DataBaseHelper.T1_COL_1));
        final String name = mCursor.getString(mCursor.getColumnIndex(DataBaseHelper.T1_COL_2));
        final String fav = mCursor.getString(mCursor.getColumnIndex(DataBaseHelper.T1_COL_5));

        if (fav.equals("1")){
            suggestionViewHolder.favImg.setImageResource(R.drawable.ic_favorite);
        } else {
            suggestionViewHolder.favImg.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
        suggestionViewHolder.titleText.setText(name);

        suggestionViewHolder.linearLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id", foodid);
                TimerFragment timerFragment = new TimerFragment();
                timerFragment.setArguments(bundle);
                FragmentManager fragmentManager = ((AppCompatActivity)mContext).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, timerFragment);
                fragmentTransaction.commit();
            }
        });

        if (!mCursor.moveToNext()) {
            return;
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

}
