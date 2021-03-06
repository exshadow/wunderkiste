package com.example.dominikglueck.whatshouldaido;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    int anzahl = 1;
    private Context context;
    String variance = "";
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        Button variante = (Button) findViewById(R.id.dummy_button);
        variante.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                      Anfrage(anzahl);
                    }
                }
        );

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private void Anfrage(int optionen) {
        // int optionen;
        Button ja = (Button) findViewById(R.id.buttonja);
        Button nein = (Button) findViewById(R.id.buttonnein);
        Button vielleicht = (Button) findViewById(R.id.buttonvielleicht);
        Button reset = (Button) findViewById(R.id.buttonreset);
        String Text;
        context = getApplicationContext();
        final MySQLiteHelper helper = new MySQLiteHelper(context);
        try {
            helper.CreateDataBase();

        } catch (IOException e) {
            throw new Error("could not create db");
        }
        try {
            helper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
        int loesung = 0;
        while(loesung == 0){
            ja.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    variance+="1";
                    helper.option(variance);
                    MySQLiteHelper msh = new MySQLiteHelper(getApplicationContext());
                    SQLiteDatabase db = msh.getWritableDatabase();
                    MySQLiteDatabaseCreator.FeedEntry.newAnswer(db,"testAntwort");
                    MySQLiteDatabaseCreator.FeedEntry.newQuestion(db,"testFrage");

                    List<Result> res = msh.getEntries("fragen");
                    String s;
                    TextView tv = (TextView) findViewById(R.id.textfeld);
                    for (int i=0; i<res.size(); i++){
                        s = res.get(i).toString();

                    }
                }
            });
            nein.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    variance+="0";
                    helper.option(variance);
                }
                });
            if(helper.frage(helper.option(variance)).startsWith("Ok")) {
                vielleicht.setVisibility(View.VISIBLE);
            }else {
                vielleicht.setVisibility(View.INVISIBLE);

                vielleicht.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        variance+="2";
                        helper.option(variance);
                    }
                });
            }
            TextView Textausgabe = (TextView) findViewById(R.id.fullscreen_content);
            Text = helper.frage(helper.option(variance));
            if (Text.isEmpty()){
                loesung = 1;
                Text = helper.antwort(variance);
            }else {
            }
            Textausgabe.setText(Text);
        } // solange bis eine Lösung gefunden wurde


    }

}
