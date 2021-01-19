package com.ph.carlights;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Deklarieren der Buttons für die verschiedenen Möglichkeiten
    Button button_blaulicht, button_blinker, button_alternierend, button_wandern, button_midline, button_outline, button_up, button_down;

    /** init function
     *
     */
    public void init(){
        SharedPreferences prefs = getSharedPreferences("DIGIBUS", Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firststart", true);

        SharedPreferences prefsLoad = getSharedPreferences("FooAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefsLoad.edit();
        if(firstStart == false){
            editor.putBoolean("firststart", true);
            editor.apply();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Problem das der Controller nichts zurück gibt
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //find ID'S
        button_blaulicht = findViewById(R.id.button_blaulicht);
        button_blinker = findViewById(R.id.button_blinker);
        button_alternierend = findViewById(R.id.button_alternierend);
        button_wandern = findViewById(R.id.button_wandern);
        button_midline = findViewById(R.id.button_mitte);
        button_outline = findViewById(R.id.button_outline);
        button_up = findViewById(R.id.button_up);
        button_down = findViewById(R.id.button_down);

        //register onClick Listener, wichtig für alle Button die was tun sollen
        button_blaulicht.setOnClickListener(this);
        button_blinker.setOnClickListener(this);
        button_alternierend.setOnClickListener(this);
        button_wandern.setOnClickListener(this);
        button_midline.setOnClickListener(this);
        button_outline.setOnClickListener(this);
        button_up.setOnClickListener(this);
        button_down.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent (this, activity_szene.class);
        //Schaltet den Übergang bei den verschiedenen Screens aus!
        int FLAG_ACTIVITY_NO_ANIMATION = 65536;
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        //Bundle für die Übergabe von Werten füt den nächsten Screen
        Bundle b = new Bundle();
        switch(v.getId()){
            case R.id.button_blaulicht:
                b.putString("info", "Blaulicht");
                break;
            case R.id.button_blinker:
                b.putString("info", "Blinker");
                break;
            case R.id.button_alternierend:
                b.putString("info", "Alternierend");
                break;
            case R.id.button_wandern:
                b.putString("info", "Wandern");
                break;
            case R.id.button_mitte:
                b.putString("info", "Midline");
                break;
            case R.id.button_outline:
                b.putString("info", "Outline");
                break;
            case R.id.button_up:
                b.putString("info", "ledup");
                break;
            case R.id.button_down:
                b.putString("info", "leddown");
                break;
        }
        intent.putExtras(b);
        startActivity(intent);
        overridePendingTransition(0,0);
        this.finish();
    }
}