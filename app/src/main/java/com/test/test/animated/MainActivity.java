package com.test.test.animated;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Vibrator v;
    private CountDownTimer curntTimer;
    private boolean curntTimerB = false;
    private int BOOKSHELF_ROWS;
    private int BOOKSHELF_COLUMNS;
    private String linkToImage;
    private String linkToImageSize;
    private int duration_vibrate;
    private int cnt_hint_number = 0;
    private String color_name;


    private TableLayout tableLayout;
    private TextView cnt_hint_text;
    private TextView menuBtn;
    private LinearLayout linearLayout;

    private Dialog dialogMainMenu;
    private Dialog styleDialog;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);



        tableLayout = findViewById(R.id.table);
        cnt_hint_text = findViewById(R.id.cnt_hint);
        cnt_hint_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainMenuDialogShow(2);
            }
        });

        menuBtn = findViewById(R.id.menuBtn);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainMenuDialogShow(1);
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        duration_vibrate = prefs.getInt("duration_vibrate", 25);

        MobileAds.initialize(this, "ca-app-pub-8505706241717212~5395127293");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("AA6A32E3DAB63751E5ED3035550DF5D1").build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                            Log.d("Test", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.d("Test", "Code error: " + String.valueOf(errorCode));
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
               Log.d("Test", "onAdClosed");
            }
        });


        drawTable();
        curntTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                curntTimerB = false;
                tableLayout.removeAllViews();
                drawTable();
            }
        };
    }

    private void mainMenuDialogShow(int i){
        dialogMainMenu = new Dialog(MainActivity.this);
        View view1 = View.inflate( this, R.layout.menu_dialog, null);
        linearLayout = view1.findViewById(R.id.linear_layout);
        dialogMainMenu.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //linearLayout.setBackgroundColor(Color.TRANSPARENT);


        switch (i){
            case 1: //main menu

                //dialogMainMenu.setContentView(R.layout.menu_dialog);


                Button btnResume = new Button(this);
                btnResume.setText("Resume");
                //btnResume.setWidth(270);
                //btnResume.setHeight(60);
                btnResume.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.buttonshape));


                Button btnStyle = new Button(this);
                btnStyle.setText("Set Style");
                btnStyle.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.buttonshape));

                Button btnAboutUs = new Button(this);
                btnAboutUs.setText("About us");
                btnAboutUs.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.buttonshape));

                Button btnRateUs = new Button(this);
                btnRateUs.setText("Rate us");
                btnRateUs.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.buttonshape));

                btnResume.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogMainMenu.cancel();
                    }
                });

                btnAboutUs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showAboutUs();
                    }
                });

                btnStyle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showStyleDialog();
                    }
                });

                


                linearLayout.addView(
                        btnResume, new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT)
                );
                linearLayout.addView(
                        btnStyle, new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT)
                );
                linearLayout.addView(
                        btnAboutUs, new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT)
                );
                linearLayout.addView(
                        btnRateUs, new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT)
                );




                break;
            case 2:// cnt of hints
                Button btnReset = new Button(this);
                btnReset.setText("Reset");


                btnReset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cnt_hint_text.setText("0");
                        cnt_hint_number = 0;
                        dialogMainMenu.cancel();
                    }
                });

                linearLayout.addView(
                        btnReset, new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT)
                );
                btnReset.setPadding(100,75,100,75);
                btnReset.setBackgroundColor(0xFFFFFFFF);
                btnReset.setTextSize(20);
                break;


            default:
                break;
        }


        dialogMainMenu.setContentView(linearLayout);
        dialogMainMenu.show();
    }

    private void showStyleDialog(){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = prefs.edit();
        linkToImage = prefs.getString("link_to_image", "ic_launcher_foreground");
        linkToImageSize = prefs.getString("link_to_image_size", "30");
        duration_vibrate = prefs.getInt("duration_vibrate", 25);
        color_name = prefs.getString("color_name", "#000000");
        View view = View.inflate(this, R.layout.style_dialog, null);
        styleDialog = new Dialog(this);

        final ImageButton imgBtn = view.findViewById(R.id.imgbtn);
        imgBtn.setImageResource(getImageId(this, linkToImage, linkToImageSize));
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectImage();
            }
        });

        View setColorView = view.findViewById(R.id.set_color_view);
        setColorView.setBackgroundColor(Color.parseColor(color_name));

        LinearLayout setColorLayout = view.findViewById(R.id.set_color_layout);
        setColorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectColor();
            }
        });

        SeekBar seekBarSize = view.findViewById(R.id.seekBar_size);
        seekBarSize.setProgress(Integer.parseInt(linkToImageSize) - 30);
        seekBarSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int sizeImg = seekBar.getProgress();
                if (sizeImg < 30){
                    editor.putString("link_to_image_size", "30");
                    linkToImageSize = "30";
                } else if (sizeImg >= 30 && sizeImg < 60){
                    editor.putString("link_to_image_size", "60");
                    linkToImageSize = "60";
                } else if (sizeImg >= 60 && sizeImg < 90){
                    editor.putString("link_to_image_size", "90");
                    linkToImageSize = "90";
                } else if (sizeImg >= 90){
                    editor.putString("link_to_image_size", "120");
                    linkToImageSize = "120";
                }
                editor.apply();
                styleDialog.cancel();
                showStyleDialog();

            }
        });

        SeekBar seekBarVibrate = view.findViewById(R.id.seekBar_durationVibrate);
        seekBarVibrate.setProgress(duration_vibrate);
        seekBarVibrate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int duration = seekBar.getProgress();
                v.vibrate(duration);
                duration_vibrate = duration;
                editor.putInt("duration_vibrate", duration);
                editor.apply();

            }
        });


        styleDialog.setContentView(view);

        styleDialog.show();



        styleDialog.setOnDismissListener(new Dialog.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                tableLayout.removeAllViews();
                drawTable();
            }
        });

    }

    private void showAboutUs(){
        Dialog dialogAboutUs = new Dialog(MainActivity.this);
        View view = View.inflate(this, R.layout.about_us, null);
        LinearLayout linearLayout = view.findViewById(R.id.linear_layout_about_us);
        //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        dialogAboutUs.setContentView(view);
        dialogAboutUs.show();
    }

    private void showSelectColor() {
        final List<String> allColorMassive;
        allColorMassive = new ArrayList<>();
        allColorMassive.add(0, "#ffffff");
        allColorMassive.add(0, "#800080");
        allColorMassive.add(0, "#0000ff");
        allColorMassive.add(0, "#00ff00");
        allColorMassive.add(0, "#ffff00");
        allColorMassive.add(0, "#ff0000");
        allColorMassive.add(0, "#00ffff");
        allColorMassive.add(0, "#ffa500");
        allColorMassive.add(0, "#000000");




        dialogMainMenu = new Dialog(MainActivity.this);
        View view1 = View.inflate( this, R.layout.select_image, null);
        LinearLayout linearView = view1.findViewById(R.id.linear_layout_scroll);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);


        for (int i = 0; i < allColorMassive.size(); i++) {
            ImageView image = new ImageView(this);

            final String colorName = allColorMassive.get(i);
            image.setLayoutParams(layoutParams);
            image.setBackgroundColor(Color.parseColor(colorName));
            linearView.addView(image);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setNewColor(colorName);
                    drawTable();
                    styleDialog.cancel();
                    dialogMainMenu.cancel();
                    showStyleDialog();
                }
            });
        }

        dialogMainMenu.setContentView(view1);
        dialogMainMenu.show();

    }

    private void setNewColor(String nameColor){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString("color_name", nameColor);
        editor.apply();
    }

    private void showSelectImage(){
        Map<String, String[]> allImgMassive;
        allImgMassive = new LinkedHashMap<>();
        String[] a = new String[2];
        a[0] = "1"; a[1] = "1";      allImgMassive.put("ic_launcher_foreground", a);
        a[0] = "1"; a[1] = "1";      allImgMassive.put("mem_ded", a);
        a[0] = "1"; a[1] = "1";      allImgMassive.put("bellisimo", a);
        a[0] = "1"; a[1] = "1";      allImgMassive.put("meme_blin", a);
        a[0] = "1"; a[1] = "1";      allImgMassive.put("black_cat_icon", a);
        a[0] = "1"; a[1] = "1";      allImgMassive.put("sonya_swarm_cat", a);
        a[0] = "1"; a[1] = "1";      allImgMassive.put("sad_frog", a);
        a[0] = "1"; a[1] = "1";      allImgMassive.put("dog", a);
        a[0] = "1"; a[1] = "1";      allImgMassive.put("digital_resistance", a);
        a[0] = "1"; a[1] = "1";      allImgMassive.put("dog_second", a);


        dialogMainMenu = new Dialog(MainActivity.this);
        View view1 = View.inflate( this, R.layout.select_image, null);
        LinearLayout linearView = view1.findViewById(R.id.linear_layout_scroll);

        for (Map.Entry<String, String[]> entry : allImgMassive.entrySet()) {
            String key = entry.getKey();
            final String[] value = entry.getValue();
            ImageView image = new ImageView(this);
            final String name = key;
            image.setImageResource(getResources().getIdentifier(name + "_120", "drawable", getApplicationContext().getPackageName()));
            linearView.addView(image);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   setNewImage(name, value[0], value[1]);
                   drawTable();
                   styleDialog.cancel();
                   dialogMainMenu.cancel();
                   showStyleDialog();
                }
            });
        }

        dialogMainMenu.setContentView(view1);
        dialogMainMenu.show();
    }

    private void setNewImage(String nameImg, String height, String width){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString("link_to_image", nameImg);
        editor.putString("height_image", height);
        editor.putString("width_image", width);
        editor.apply();
    }




    private void drawTable(){
        tableLayout.removeAllViews();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        linkToImage = prefs.getString("link_to_image", "ic_launcher_foreground");
        linkToImageSize = prefs.getString("link_to_image_size", "30");
        Float height_image = Float.valueOf(prefs.getString("height_image", "1"));
        Float width_image = Float.valueOf(prefs.getString("width_image", "1"));
        color_name = prefs.getString("color_name", "#000000");



        int color = Color.parseColor(color_name);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels - 150;

        BOOKSHELF_ROWS = (int) (height / (convertDpToPixels(Float.parseFloat(linkToImageSize), this) * (height_image / width_image)));
        BOOKSHELF_COLUMNS = width / convertDpToPixels(Float.parseFloat(linkToImageSize), this);

        Log.d("Test", "Columns: " + String.valueOf(BOOKSHELF_COLUMNS));
        Log.d("Test", "Rows: " + String.valueOf(BOOKSHELF_ROWS));



        for (int i = 0; i < BOOKSHELF_ROWS; i++) {

            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            tableRow.setBackgroundResource(R.drawable.ic_launcher_background_30);

            for (int j = 0; j < BOOKSHELF_COLUMNS; j++) {
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(getImageId(this, linkToImage, linkToImageSize));
                imageView.setOnClickListener(this);
                imageView.setColorFilter(color);
                tableRow.addView(imageView, j);
            }

            tableLayout.addView(tableRow, i);
        }
    }

    public static int getImageId(Context context, String imageName, String imageSize) {
        return context.getResources().getIdentifier("drawable/" + imageName + "_" + imageSize, null, context.getPackageName());
    }


    @Override
    public void onClick(final View view) {
        view.setOnClickListener(null);
        //Log.d("Test", "Click");
        cnt_hint_number++;
        cnt_hint_text.setText(String.valueOf(cnt_hint_number));
        v.vibrate(duration_vibrate);
        view.animate()
                .alpha(0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.INVISIBLE);

                    }
                });

        if (!curntTimerB){
            curntTimer.start();
            curntTimerB = true;
        } else {
            curntTimer.cancel();
            curntTimer.start();
        }

    }

    public static int convertDpToPixels(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

}
