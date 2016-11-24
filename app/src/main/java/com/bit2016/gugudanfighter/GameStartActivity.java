package com.bit2016.gugudanfighter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameStartActivity extends AppCompatActivity {

    private static final int TIME_LIMIT = 30;
    private static final String L_TAG = "GameStartActivity";
    private Timer timer = new Timer();
    private TextView tvLastTime;

    private int resultCount=0;
    private int totalCount=0;

    //buttons
    private int[] button_ids = {
            R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7, R.id.button_8, R.id.button_9
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);

        tvLastTime = (TextView) findViewById(R.id.textView26);

        timer.schedule(new GameTimerTask(), 1000, 1000);

        gameRe();
    }

    public void gameRe(){

        final Gugudan[] gugudan = new Gugudan[10];

        for (int i = 0; i < 9; i++) {
            gugudan[i] = new Gugudan();
        }

        Random random = new Random();

        HashSet<Gugudan> hashset = new HashSet<Gugudan>();

        while (true) {
            Log.d(L_TAG, " hash size: " + hashset.size());
            if(hashset.size() >= 9 ){
                break;
            }
            gugudan[hashset.size()].setInA(random.nextInt(9)+1) ;
            gugudan[hashset.size()].setInB(random.nextInt(9) + 1);

            Log.d(L_TAG, " gugudan " + gugudan[hashset.size()].toString());
            if(hashset.contains(gugudan[hashset.size()]) == false) {
                hashset.add(gugudan[hashset.size()]);
            }
        }

        //문제다
        final int queNo = random.nextInt(9);

        //버튼에 숫자 입력
        for (int i = 0; i < 9; i++) {
            Button button = (Button) findViewById(button_ids[i]);
            button.setText(String.valueOf(gugudan[i].getInResult()));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button button = (Button) view;
                    if (Integer.parseInt(button.getText().toString()) == gugudan[queNo].getInResult()) {
                        resultCount++;
                        totalCount++;
                    } else {
                        totalCount++;
                    }
                    gameRe();
                }
            });
        }

        //답과총횟수입력
        {
            gameScore();
        }

        //문제 입력
        {
            TextView textView = (TextView) findViewById(R.id.textView20);
            Log.d(L_TAG,"quesNo"+ queNo);
            textView.setText(String.valueOf(gugudan[queNo].getInA()));
        }
        {
            TextView textView = (TextView) findViewById(R.id.textView16);
            textView.setText(String.valueOf(gugudan[queNo].getInB()));
        }
        //-------------------------------------------
    }

    public void gameScore(){
        //답과총횟수입력
        {
            TextView textView = (TextView) findViewById(R.id.textView24);
            textView.setText(String.valueOf(totalCount));

            TextView textView2 = (TextView) findViewById(R.id.textView22);
            textView2.setText(String.valueOf(resultCount));
        }

    }


    private class GameTimerTask extends TimerTask {

        private int seconds;

        @Override
        public void run() {
            seconds++;

            if (seconds >= TIME_LIMIT) {
                //타이머 중지
                timer.cancel();

                //Toast.makeText(getApplicationContext(), "타이머 중지", Toast.LENGTH_LONG).show();
                Log.i("-------------------->", "타이머 정지");


                //결과 activity 호출 startactivity
                finish();
            }
            //UI변경

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateLastTime(seconds);
                }
            });
        }
    }

    private void updateLastTime(int seconds) {
        tvLastTime.setText("" + (TIME_LIMIT - seconds));
    }

    public class Gugudan {
        public int inA;
        public int inB;
        public int inResult;

        public int getInA() {
            return inA;
        }

        public void setInA(int inA) {
            this.inA = inA;
        }

        public int getInB() {
            return inB;
        }

        public void setInB(int inB) {
            this.inB = inB;
            setInResult();
        }

        public int getInResult() {
            return inResult;
        }

        public void setInResult() {
            this.inResult = this.inA * this.inB;
        }

        @Override
        public int hashCode() {
            final int prime = 31;

            return prime * inResult;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Gugudan other = (Gugudan) obj;
            if (inResult != other.inResult)
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Gugudan{" +
                    "inA=" + inA +
                    ", inB=" + inB +
                    ", inResult=" + inResult +
                    '}';
        }
    }

}
