package com.kelvin.app_addons_0001_message;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.util.Log;
import android.os.HandlerThread;


public class MainActivity extends AppCompatActivity {

    private Button mButton;
    private static final String TAG = "MessageTest";
    private int ButtonCount = 0;
    private Thread myThread;
    private MyThread myThread2;
    private Handler mHandler;
    private int nMessageCount = 0;
    private HandlerThread myThread3;
    private Handler mHandler3;

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
    //This thread service any incoming message
    class MyThread extends Thread{
        private Looper mLooper;
        @Override
        public void run() {
            super.run();

            Looper.prepare();   //This prepare a queue
            synchronized (this) {
                mLooper = Looper.myLooper();
                notifyAll();
            }
            Looper.loop();      //This service the message
        }

        public Looper getLooper() {
            if (!isAlive()) {
                return null;
            }

            // If the thread has been started, wait until the looper has been created.
            synchronized (this) {
                while (isAlive() && mLooper == null) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
            return mLooper;
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
                Message msg = new Message();
                mHandler.sendMessage(msg);
                mHandler3.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "get Message for Thread3 "+nMessageCount);
                        nMessageCount++;
                    }
                });
            }
        });

        myThread = new Thread(new myRunnable(),"MessageTestThread");
        myThread.start();

        myThread2 = new MyThread();
        myThread2.start();

        //Handler is used to send message
        //Below link the Handler to the looper and when the
        //looper received the messsage, it will use the callback function
        //handleMessage
        mHandler = new Handler(myThread2.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Log.d(TAG, "get Message "+nMessageCount);
                nMessageCount++;
                return false;
            }
        });

        myThread3 = new HandlerThread("MessageTestThread3");
        myThread3.start();

        mHandler3 = new Handler(myThread3.getLooper());

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
