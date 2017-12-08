package com.kelvin.app_addons_0001_message;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    private Button mButton;
    private static final String TAG = "MessageTest";
    private int ButtonCount = 0;
    private Thread myThread;
    private MyThread myThread2;

    class myRunnable implements Runnable{
        @Override
        public void run() {
            int count = 0;
            for(;;){
                Log.d(TAG, "MyThread "+count);
                count++;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //The above way of implementing thread won't allow more than 1 function
    //Hence we use this method below
    class MyThread extends Thread{
        @Override
        public void run() {
            super.run();
            int count = 0;
            for(;;){
                Log.d(TAG, "MyThread2 "+count);
                count++;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button)findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Log.d(TAG, "Send Message "+ButtonCount);
                ButtonCount++;
            }
        });

        myThread = new Thread(new myRunnable(),"MessageTestThread");
        myThread.start();

        myThread2 = new MyThread();
        myThread2.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
