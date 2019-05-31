/*
 * Copyright (c) 2016. Truiton (http://www.truiton.com/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Mohit Gupt (https://github.com/mohitgupt)
 *
 */

package gatogoku.epsonapagar;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.StrictMode;

import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.WindowManager;

public class MyService extends Service {
    CountDownTimer countdown ;
    Notification notification;
    private static final String LOG_TAG = "MyService";
    PowerManager.WakeLock wl;
    MainActivity myMain;
    Long minutos = Long.valueOf(0),segundos =  Long.valueOf(0), horas =  Long.valueOf(0);;
boolean destruido = false;
boolean firstTime;


    //public  MyService(MainActivity main) {
     //   super.onCreate();myMain=main;

    PendingIntent pendingIntent;
    Intent previousIntent;
    PendingIntent ppreviousIntent;
    String IP ="";
    @Override

    public int onStartCommand(Intent intent, int flags, int startId) {
        IP = intent.getStringExtra("IP");
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {


            if (intent.getExtras().get("minutosN").equals("")){//myMain.ed1.getText().toString().equals("")){
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                WebRequest wr = new WebRequest();
                wr.get("http://" + IP +"/cgi-bin/directsend?KEY=3B&_=1546577251572");
            }
            else {


                Log.i(LOG_TAG, "Received Start Foreground Intent ");
                Intent notificationIntent = new Intent(this, MainActivity.class);
                notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                pendingIntent = PendingIntent.getActivity(this, 0,
                        notificationIntent, 0);

                previousIntent = new Intent(this, MyService.class);
                previousIntent.setAction(Constants.ACTION.PREV_ACTION);
                ppreviousIntent = PendingIntent.getService(this, 0,
                        previousIntent, 0);

                Intent playIntent = new Intent(this, MyService.class);
                playIntent.setAction(Constants.ACTION.PLAY_ACTION);
                PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                        playIntent, 0);

                Intent nextIntent = new Intent(this, MyService.class);
                nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
                PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                        nextIntent, 0);
              //  BuildNofity(pendingIntent, ppreviousIntent,"");
             //   Integer val = (Integer) intent.getExtras().get("minutosN");
                    Integer cantidadMinutos = intent.getIntExtra("minutosN",0);//remove .toString().

                 countdown  = new CountDownTimer(1000 * 1 * cantidadMinutos, 1000) {
                        public void onTick(long millisUntilFinished) {
                            Long sec = millisUntilFinished / 1000;
                            String min = "", seg = "", hor = "";
                            minutos = millisUntilFinished / 60000;
                            segundos = (millisUntilFinished / 1000) % 60;

                            if (minutos < 10) min = "0" + minutos;
                            else min = minutos.toString();
                            if (segundos < 10) seg = "0" + segundos;
                            else seg = segundos.toString();
                            // if( horas < 10 ) hor = "00" + horas;else if( horas < 10 ) hor = "0" + horas;else hor = horas.toString();
                          //  myMain.tv.setText(min + ":" + seg);//  hor+ ":" +min + ":" + seg  );

                         BuildNofity(pendingIntent, ppreviousIntent,min + ":" + seg);

                        }

                        public void onFinish() {
                          // unlockScreen();
                           // myMain.tv.setText("APAGANDO PROYECTOR");
                            for (int i = 0; i < 2; ++i) {
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                StrictMode.setThreadPolicy(policy);
                                WebRequest wr = new WebRequest();
                                wr.get("http://" + IP +"/cgi-bin/directsend?KEY=3B&_=1546577251572");
                                stopForeground(true);
                                stopSelf();
                            }
                           // myMain.tv.setText("APAGADO");

                           // wl.release(PowerManager.SCREEN_DIM_WAKE_LOCK);

                        }
                    }.start();


                }


            //codigo propio
        }
        else if (intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            try{ countdown.cancel();}catch (Exception e){}

            onDestroy();

        }
        return START_NOT_STICKY;
    }

    private void BuildNofity(PendingIntent pendingIntent, PendingIntent ppreviousIntent, String hora) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.dr);
        Intent notificationIntent = new Intent(this, MyService.class);
        PendingIntent pendingIntent2=PendingIntent.getActivity(this,0,notificationIntent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"chanel")
                .setSmallIcon(R.drawable.xd)
                // .setContentIntent(pendingIntent2)
                .setLargeIcon( Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentTitle("Temporizador de apagado proyector EPSON")
                .setContentText(hora)
                .setColor(Color.parseColor("#25baa2"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                //.setVibrate(new long[]{ 0 })
                .setContentIntent(pendingIntent);
               // .addAction(android.R.drawable.ic_media_previous, "Previous", ppreviousIntent);

        Notification notification=builder.build();

        if(Build.VERSION.SDK_INT>=26) {
            NotificationChannel channel = new NotificationChannel("chanel", "chan", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("desc");
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            //channel.setVibrationPattern(new long[]{ 0 });
            channel.enableVibration(false);
        }
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);
    }


    private void unlockScreen() {

        myMain.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        // myMain.requestWindowFeature(Window.FEATURE_NO_TITLE);
        PowerManager pm = (PowerManager)  myMain.getSystemService(myMain.POWER_SERVICE);
        // wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "My Tag");
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "My Tag");
        wl.acquire();
    }

    @Override
    public void onDestroy() {
         Handler handler = new Handler();
        handler.removeCallbacksAndMessages(null);
destruido = true;
        Log.i(LOG_TAG, "In onDestroy");
        stopForeground(true);
        stopSelf();
        super.onDestroy();
        stopService(new Intent(this, MyService.class));
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }
}
