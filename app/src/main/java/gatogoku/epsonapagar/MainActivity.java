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

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    EditText ed1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView startButton = (ImageView) findViewById(R.id.button);
        ImageView stopButton = (ImageView) findViewById(R.id.button2);
        ed1 =(EditText)  findViewById(R.id.editText);

        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {



        switch (v.getId()) {
            case R.id.button:

                Intent startIntent = new Intent(MainActivity.this, MyService.class);
                String ips= PreferenceManager.getDefaultSharedPreferences(this).getString("IP", "");
                startIntent.putExtra("IP", ips);
                startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);

                if (ed1.getText().toString().equals("")){
                    startIntent.putExtra("minutosN", "");
                    startService(startIntent);
                }
                else {

                    if (isNumeric(String.valueOf(ed1.getText().toString()))) {
                        MyService fs = new MyService();

                        startIntent.putExtra("minutosN", Integer.parseInt(ed1.getText().toString()));
                       startService(startIntent);

                       // fs.onStartCommand(startIntent,0,0);

                    } else {
                        // 1. Instantiate an AlertDialog.Builder with its constructor
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

// 2. Chain together various setter methods to set the dialog characteristics
                        builder.setMessage("El tiempo debe estar dado en minutos y debe ser numerico")
                                .setTitle("Solo valores numericos");

// 3. Get the AlertDialog from create()
                        AlertDialog dialog = builder.create();

                        dialog.show();
                    }


                }

//startIntent.putExtra("minutosN",100);





                break;
            case R.id.button2:
                MyService fs = new MyService();
                Intent stopIntent = new Intent(MainActivity.this, MyService.class);
                stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                startService(stopIntent);

                break;
            default:
                break;
        }

    }


    public static boolean isNumeric(String cadena) {

        boolean resultado;

        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }

        return resultado;
    }

    public void configuracion(View v){
        Intent i = new Intent(this,SettingsActivity.class);
        startActivity(i);
    }




}
