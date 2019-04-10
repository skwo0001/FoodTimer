package com.szeyankwok.foodtimer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.CatViewHolder> {

    private Context mContext;
    private String[] catArray ;

    public CatAdapter(Context c, String[] array){
        mContext = c;
        catArray = array;
    }

    public class CatViewHolder extends RecyclerView.ViewHolder{

        TextView titleText;
        LinearLayout linearLayout;

        public CatViewHolder( View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.catText);
            linearLayout = itemView.findViewById(R.id.catLL);
        }
    }

    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.gallery_item, viewGroup, false);

        return new CatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatViewHolder catViewHolder, int i) {

        final String cat = catArray[i];

        catViewHolder.titleText.setText(cat);

        catViewHolder.linearLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("category", cat);
                ListFragment listFragment = new ListFragment();
                listFragment.setArguments(bundle);
                FragmentManager fragmentManager = ((AppCompatActivity)mContext).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, listFragment);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return catArray.length;
    }

}