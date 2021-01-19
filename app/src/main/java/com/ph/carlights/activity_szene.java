package com.ph.carlights;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class activity_szene<Quote> extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TextView text_info;
    private Button button_back, button_start, button_status, button_einmal, button_halten;
    private String info, farbe, speed, farbe2, auswahl;
    private int von, bis, lauf;
    private EditText editText_von, editText_bis, editText_druchlauf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle b = getIntent().getExtras();
        info = b.getString("info");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_szene);


        button_status = findViewById(R.id.button_status);
        text_info = findViewById(R.id.text_info);
        button_back =  findViewById(R.id.button_back);
        button_start = findViewById(R.id.button_start);
        button_einmal = findViewById(R.id.button_einmal);
        button_halten = findViewById(R.id.button_halten);

        text_info.setText("Modus: " + info);
        button_back.setOnClickListener(this);
        button_start.setOnClickListener(this);
        button_einmal.setOnClickListener(this);
        button_halten.setOnClickListener(this);

        //get the spinner from the xml.
        Spinner spinner_1 = findViewById(R.id.spinner_farbe);
        Spinner spinner_2 = findViewById(R.id.spinner_farbe_2);
        Spinner spinner_speed = findViewById(R.id.spinner_speed);
        Spinner spinner_auswahl = findViewById(R.id.spinner_auswahl);

        //Editor Feld
        editText_von = findViewById(R.id.editText2_von);
        editText_bis = findViewById(R.id.editText_bis);
        editText_druchlauf = findViewById(R.id.editText_durchlauf);

        editText_druchlauf.setText("" + 1);


        String[] farben = new String[]{"rot", "blau", "gelb", "grün"};
        String[] items_speed = new String[]{"schnell", "mittel", "langsam"};
        String[] items_auswahl = new String[]{"links", "rechts", "mitte", "komplett"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, farben);
        ArrayAdapter<String> adapter_speed = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items_speed);
        ArrayAdapter<String> adapter_auswahl = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items_auswahl);

        spinner_1.setAdapter(adapter);
        spinner_1.setOnItemSelectedListener(this);

        spinner_2.setAdapter(adapter);
        spinner_2.setOnItemSelectedListener(this);

        spinner_speed.setAdapter(adapter_speed);
        spinner_speed.setOnItemSelectedListener(this);

        spinner_auswahl.setAdapter(adapter_auswahl);
        spinner_auswahl.setOnItemSelectedListener(this);

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_back:
                Intent intent = new Intent (this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0,0);
                this.finish();
                break;
            case R.id.button_start:
                new SendOperation().execute(info, "0");
                break;
            case R.id.button_einmal:
                new SendOperation().execute(info, "1");
                break;
            case R.id.button_halten:
                new SendOperation().execute(info, "2");
                break;
        }
    }



    /*
    Send Funktion zum erstellen des Links um Befehle zum Mikrocontroller zu schicjken
     */
    public void send(String modus, int extra) {
        von = Integer.valueOf(editText_von.getText().toString());
        bis = Integer.valueOf(editText_bis.getText().toString());
        lauf = Integer.valueOf(editText_druchlauf.getText().toString());
        try {
            InputStream is = null;
            URL sendURL = new URL("http://192.168.4.1/" + modus + "&" + farbe + "&" + farbe2 + "&" + speed + "&" + von + "&" + bis + "&" + extra + "&" + lauf + "&");
            URLConnection yc = sendURL.openConnection();
            is = yc.getInputStream();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        switch(parent.getId()) {
            case R.id.spinner_farbe:
                farbe = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spinner_speed:
                speed = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spinner_farbe_2:
                farbe2 = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spinner_auswahl:
                auswahl = parent.getItemAtPosition(position).toString();
                von = getVon(auswahl);
                bis = getBis(auswahl);
                editText_von.setText("" + von);
                editText_bis.setText("" + bis);
                break;
        }
    }

    /* Begrenzungen der LED Kette
    uint32_t unten = 43;
    uint32_t oben = 216;
    uint32_t mitte_unten = 100;
    uint32_t mitte_oben = 166;
     */

    private int getVon(String auswahl) {
        int von= 0;
        if (auswahl.equals("links")) {
            von = 166;
        } else if (auswahl.equals("rechts")) {
            von = 43;
        } else if (auswahl.equals("mitte")) {
            von = 100;
        } else if (auswahl.equals("komplett")) {
            von = 43;
        }
        return von;
    }

    private int getBis(String auswahl) {
        int bis= 0;
        if (auswahl.equals("links")) {
            bis = 216;
        } else if (auswahl.equals("rechts")) {
            bis = 100;
        } else if (auswahl.equals("mitte")) {
            bis = 166;
        } else if (auswahl.equals("komplett")) {
            bis = 216;
        }
        return bis;
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    private class SendOperation extends AsyncTask<String, Void, String> {
        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            send(params[0],  Integer.parseInt(params[1]));
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            button_status.setBackgroundColor(Color.GREEN);
            button_status.setText("Fertig");
        }

        @Override
        protected void onPreExecute() {
            button_status.setBackgroundColor(Color.RED);
            button_status.setText("In Arbeit");
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}

