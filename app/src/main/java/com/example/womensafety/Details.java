package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Details extends AppCompatActivity {

    String prevStarted = "yes";
    EditText nameText;
    EditText cont1;
    EditText cont2;
    Button save;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        nameText = findViewById(R.id.editText1);
        cont1 = findViewById(R.id.editText2);
        cont2 = findViewById(R.id.editText3);
        save = findViewById(R.id.button);

            checkExistence();


       }

       private void checkExistence() {

        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        if (sharedpreferences.getBoolean(prevStarted, false))
        {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
     }



    public void addRecord(View view) {
        DbHelper db = new DbHelper(this);


        String t1 = nameText.getText().toString();
        String t2 = cont1.getText().toString();
        String t3 = cont2.getText().toString();

        if (validation(t1, t2, t3)) {

            String result = db.addRecord(t1, t2, t3);
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();

            SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(prevStarted, Boolean.TRUE);
            editor.apply();
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            finish();

        }
        else
            {

            Toast.makeText(this, "Recheck Details", Toast.LENGTH_LONG).show();
            }



    }

    public boolean validation(String name, String Phno_1, String Phno_2) {

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(Phno_1) && !TextUtils.isEmpty(Phno_2))
            return Patterns.PHONE.matcher(Phno_1).matches() && Patterns.PHONE.matcher(Phno_2).matches()
                    && Phno_1.length()==10 && Phno_2.length()==10;

        return false;
    }
}
