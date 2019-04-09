package com.szeyankwok.foodtimer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CatAdapter extends BaseAdapter {

    private Context mContext;
    private String[] catArray ;

    public CatAdapter(Context c, String[] array){
        mContext = c;
        catArray = array;
    }


    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return catArray.length;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View gridViewAndroid;

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.gallery_item,null);

            ImageView imageView = (ImageView) gridViewAndroid.findViewById(R.id.android_gridview_image);
            final TextView textView = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);

            textView.setText(catArray[position]);

            gridViewAndroid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("category", textView.getText().toString());
                    ListFragment listFragment = new ListFragment();
                    listFragment.setArguments(bundle);
                    FragmentManager fragmentManager = ((AppCompatActivity)mContext).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, listFragment);
                    fragmentTransaction.commit();
                }
            });
        } else {
            gridViewAndroid = (View) convertView;
        }
        return gridViewAndroid;
    }


}
