package com.szeyankwok.foodtimer;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import static com.szeyankwok.foodtimer.NotificationChannel.CHANNEL_ID;

import static android.content.Context.MODE_PRIVATE;

public class TimerFragment extends Fragment {

    private TextView textView, nameText;
    private ImageView button_start, button_stop, button_pause, button_fav;
    private ProgressBar progressBar;

    private CountDownTimer countDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis;
    private long mStartTimeInMillis;
    private long mEndTime;

    private DataBaseHelper db;

    private NotificationManager notificationManager;

    String time, fav, foodId, timeLeft;

    Vibrator vibrator;

    View view;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Find the value in shared preferences
        view = inflater.inflate(R.layout.fragment_timer, container, false);
        context = view.getContext();

        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        textView = (TextView) view.findViewById(R.id.timeTV);
        nameText = (TextView) view.findViewById(R.id.name);

        button_fav = (ImageView) view.findViewById(R.id.fav_button);
        button_pause = (ImageView) view.findViewById(R.id.pause_button);
        button_start = (ImageView) view.findViewById(R.id.play_button);
        button_stop = (ImageView) view.findViewById(R.id.cancel_button);

        button_stop.setEnabled(false);

        foodId = this.getArguments().getString("id");

        db = new DataBaseHelper(context);

        Cursor cursor = db.getFood(foodId);

        final String foodname = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T1_COL_2));

        nameText.setText(foodname);

        time = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T1_COL_4));

        fav = cursor.getString(cursor.getColumnIndex(DataBaseHelper.T1_COL_5));

        if (fav.equals("0")){
            button_fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        } else {
            button_fav.setImageResource(R.drawable.ic_favorite);
        }

        mStartTimeInMillis = Long.parseLong(time) * 60000;

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setProgress(100);
        progressBar.setMax(100);

        mTimeLeftInMillis = mStartTimeInMillis;

        updateCountDownText();

        button_pause.setVisibility(View.INVISIBLE);

        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        button_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
            }
        });

        button_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        //add function for the favourite button
        button_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fav.equals("0")){
                    button_fav.setImageResource(R.drawable.ic_favorite);
                    db.addToFav(foodname,true);
                    fav = "1";
                } else {
                    button_fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    db.addToFav(foodname,false);
                    fav = "0";
                }
            }
        });

        setHasOptionsMenu(true);
        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.timer_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            Intent edit = new Intent(context, EditActivity.class);
            edit.putExtra("id", foodId);
            startActivity(edit);
        }

        return true;

    }

    private void startTimer(){
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis ;

        countDownTimer = new CountDownTimer(mTimeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();

            }

            @Override
            public void onFinish() {
                 mTimerRunning = false;

                 sendNotification();

                 final long[] pattern = {2000,1000};

                 vibrator.vibrate(pattern, 0);

                 Toast.makeText(context,"Vibrate",Toast.LENGTH_SHORT).show();

            }
        }.start();

        mTimerRunning = true;
        button_stop.setEnabled(true);
        button_pause.setVisibility(View.VISIBLE);
    }

    private void pauseTimer(){
        countDownTimer.cancel();
        mTimerRunning = false;
        vibrator.cancel();
    }

    private void resetTimer(){
        countDownTimer.cancel();
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        button_pause.setVisibility(View.INVISIBLE);
        vibrator.cancel();

    }

    private void updateCountDownText(){
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        if (minutes > 59) {
            int hrs = minutes / 60;
            minutes = minutes % 60;

            timeLeft = String.format(Locale.getDefault(), "%02d:%02d:%02d", hrs, minutes, seconds);

        } else {
            timeLeft = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }


        textView.setText(timeLeft);

        int percent = (int) (mStartTimeInMillis/100);

        progressBar.setProgress((int) mTimeLeftInMillis/percent);

    }

    private void updateButtons() {
        if (mTimerRunning) {
            button_pause.setVisibility(View.VISIBLE);
        } else {
            button_pause.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);


        if (mTimeLeftInMillis != Long.parseLong(time) * 60000) {

            mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
            mTimerRunning = prefs.getBoolean("timerRunning", false);

            updateCountDownText();
            updateButtons();

            if (mTimerRunning) {
                mEndTime = prefs.getLong("endTime", 0);
                mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

                if (mTimeLeftInMillis < 0) {
                    mTimeLeftInMillis = 0;
                    mTimerRunning = false;
                    updateCountDownText();
                    updateButtons();

                    sendNotification();

                    final long[] pattern = {2000, 1000};

                    vibrator.vibrate(pattern, 0);
                    Toast.makeText(context, "Vibrate", Toast.LENGTH_SHORT).show();

                } else {
                    startTimer();
                }
            }
        }
    }


    private void sendNotification(){
        android.app.NotificationChannel notificationChannel = new android.app.NotificationChannel(CHANNEL_ID, "Food Timer", NotificationManager.IMPORTANCE_HIGH);
        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);

        Notification notification = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarms)
                .setContentTitle("Food Timer")
                .setContentText("Time's up!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOnlyAlertOnce(true)
                .build();

        notificationManager.notify(1, notification);
    }


}
