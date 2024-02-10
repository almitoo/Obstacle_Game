package com.example.save_jerry;


import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.Random;

public class MainActivity extends AppCompatActivity {



    public static final int NUM_OF_LANES = 3;
    public static final int NUM_OF_TOM_ROWS = 6;
    public static final int NUM_OF_HEART = 3;
    public static final int DELAY = 1500;
    // private AppCompatImageView main_IMG_background;
    private ShapeableImageView[] main_IMG_HEART; //
    private ShapeableImageView[][] main_IMG_MATRIX_TOM;
    private ShapeableImageView[] main_IMG_JERRY;
    private FloatingActionButton main_BTN_left; //
    private FloatingActionButton main_BTN_right; //
    // private AlertDialog.Builder builder;
    private int numOfCrashes = 0;
    private CountDownTimer gameTimer;
    private Vibrator vibrator;
    public Random rand;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        findViews();
        setKeyboard();
        startTime();
        RestAll();





    }

    private void findViews() {

        main_IMG_HEART = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)};

        main_IMG_MATRIX_TOM = new ShapeableImageView[NUM_OF_TOM_ROWS][NUM_OF_LANES];
        main_IMG_MATRIX_TOM[0][0] = findViewById(R.id.main_IMG_tom1);
        main_IMG_MATRIX_TOM[0][1] = findViewById(R.id.main_IMG_tom8);
        main_IMG_MATRIX_TOM[0][2] = findViewById(R.id.main_IMG_tom15);

        main_IMG_MATRIX_TOM[1][0] = findViewById(R.id.main_IMG_tom2);
        main_IMG_MATRIX_TOM[1][1] = findViewById(R.id.main_IMG_tom9);
        main_IMG_MATRIX_TOM[1][2] = findViewById(R.id.main_IMG_tom16);

        main_IMG_MATRIX_TOM[2][0] = findViewById(R.id.main_IMG_tom3);
        main_IMG_MATRIX_TOM[2][1] = findViewById(R.id.main_IMG_tom10);
        main_IMG_MATRIX_TOM[2][2] = findViewById(R.id.main_IMG_tom17);

        main_IMG_MATRIX_TOM[3][0] = findViewById(R.id.main_IMG_tom4);
        main_IMG_MATRIX_TOM[3][1] = findViewById(R.id.main_IMG_tom11);
        main_IMG_MATRIX_TOM[3][2] = findViewById(R.id.main_IMG_tom18);

        main_IMG_MATRIX_TOM[4][0] = findViewById(R.id.main_IMG_tom5);
        main_IMG_MATRIX_TOM[4][1] = findViewById(R.id.main_IMG_tom12);
        main_IMG_MATRIX_TOM[4][2] = findViewById(R.id.main_IMG_tom19);

        main_IMG_MATRIX_TOM[5][0] = findViewById(R.id.main_IMG_tom6);
        main_IMG_MATRIX_TOM[5][1] = findViewById(R.id.main_IMG_tom13);
        main_IMG_MATRIX_TOM[5][2] = findViewById(R.id.main_IMG_tom20);


        main_IMG_JERRY = new ShapeableImageView[NUM_OF_LANES];
        main_IMG_JERRY[0] = findViewById(R.id.main_IMG_jerry7);
        main_IMG_JERRY[1] = findViewById(R.id.main_IMG_jerry14);
        main_IMG_JERRY[2] = findViewById(R.id.main_IMG_jerry21);

        main_BTN_left = findViewById(R.id.main_FAB_left);
        main_BTN_right = findViewById(R.id.main_FAB_right);

        rand = new Random(NUM_OF_LANES + 1);



    }
    private void RestAll(){
        numOfCrashes = 0;
        for(int i = 0; i < NUM_OF_HEART; i++)
            main_IMG_HEART[i].setVisibility(View.VISIBLE);

        for(int i = 0; i < NUM_OF_TOM_ROWS;i++)
            for(int j = 0;j < NUM_OF_LANES;j++)
                main_IMG_MATRIX_TOM[i][j].setVisibility(View.INVISIBLE);


        for(int i = 0;i < NUM_OF_LANES; i++)
            main_IMG_JERRY[i].setVisibility(View.INVISIBLE);

        main_IMG_JERRY[1].setVisibility(View.VISIBLE);



    }

    private void setKeyboard(){
        main_BTN_left.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
                moveLeft(v);
                return true;
            }
            return false;
        });

        main_BTN_right.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                moveRight(v);
                return true;
            }
            return false;
        });
    }
    public void moveRight(View view) {
        if (main_IMG_JERRY[0].getVisibility() == VISIBLE){
            main_IMG_JERRY[0].setVisibility(INVISIBLE);
            main_IMG_JERRY[1].setVisibility(VISIBLE);
        }

        else if (main_IMG_JERRY[1].getVisibility() == VISIBLE) {
            main_IMG_JERRY[1].setVisibility(INVISIBLE);
            main_IMG_JERRY[2].setVisibility(VISIBLE);
        }
    }

    public void moveLeft(View view) {
        if (main_IMG_JERRY[2].getVisibility() == VISIBLE){
            main_IMG_JERRY[2].setVisibility(INVISIBLE);
            main_IMG_JERRY[1].setVisibility(VISIBLE);
        }

        else if (main_IMG_JERRY[1].getVisibility() == VISIBLE) {
            main_IMG_JERRY[1].setVisibility(INVISIBLE);
            main_IMG_JERRY[0].setVisibility(VISIBLE);
        }
    }

    private void startTime() {
        if (gameTimer == null) {
            gameTimer = new CountDownTimer(999999999, DELAY) {
                @Override
                public void onTick(long millisUntilFinished) {
                    removeBottomLine();
                    randomNewTom();
                    TomMoveDown();
                    CheckCollision();

                }

                @Override
                public void onFinish() {
                    gameTimer.cancel();
                }
            }.start();
        }
    }

    private void  removeBottomLine(){

        {
            for(int i = 0; i < NUM_OF_LANES; i++)
            {
                main_IMG_MATRIX_TOM[NUM_OF_TOM_ROWS - 1][i].setVisibility(INVISIBLE);
            }
        }
    }
    public void randomNewTom(){
        int randLane = rand.nextInt(NUM_OF_LANES+1);
        if (randLane != NUM_OF_LANES)
            main_IMG_MATRIX_TOM[0][randLane].setVisibility(VISIBLE);
    }
    private void TomMoveDown() {
        for(int i = NUM_OF_TOM_ROWS-1; i > 0;i--) {
            for(int j = NUM_OF_LANES - 1; j >= 0 ;j--){
                if(main_IMG_MATRIX_TOM[i-1][j].getVisibility() == VISIBLE){
                    main_IMG_MATRIX_TOM[i-1][j].setVisibility(INVISIBLE);
                    main_IMG_MATRIX_TOM[i][j].setVisibility(VISIBLE);
                }
            }
        }
    }
    private void CheckCollision(){
        for(int i = 0;i < NUM_OF_LANES; i++){
            if(main_IMG_JERRY[i].getVisibility() == main_IMG_MATRIX_TOM[NUM_OF_TOM_ROWS-1][i].getVisibility()
                    && main_IMG_JERRY[i].getVisibility() == VISIBLE){
                UpdateLifeAndCollision();
            }
        }

    }
    private void UpdateLifeAndCollision() {
        if(numOfCrashes < NUM_OF_HEART){
            main_IMG_HEART[numOfCrashes].setVisibility(INVISIBLE);
            numOfCrashes++;
            Toast.makeText(getApplicationContext(), NUM_OF_HEART - numOfCrashes +" LIFE LEFT", Toast.LENGTH_SHORT).show();
            makeVibrate();
        }

        else{
            Toast.makeText(getApplicationContext(), "GAME OVER!", Toast.LENGTH_SHORT).show();
            gameOver();
        }
    }
    private void makeVibrate() {
        final VibrationEffect vibrationEffect1;
            vibrationEffect1 = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE);
            vibrator.cancel();
            vibrator.vibrate(vibrationEffect1);
        }
    private void gameOver(){
        gameTimer.cancel();
        newGame();
    }
   private void newGame(){
       Toast.makeText(getApplicationContext(), " START NEW GAME!", Toast.LENGTH_SHORT).show();
        RestAll();
        gameTimer.start();

    }

}
