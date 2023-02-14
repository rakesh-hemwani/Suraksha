package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    DbHelper db;
    EditText e1,e2,e3;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        e1=findViewById(R.id.e1);
        e2=findViewById(R.id.e2);
        e3=findViewById(R.id.e3);

        save=findViewById(R.id.save);
        db=new DbHelper(this);
        Cursor cursor=new DbHelper(getApplicationContext()).readalldata();
        while(cursor.moveToNext())
        {
            e1.setText(cursor.getString(1));
            e2.setText(cursor.getString(2));
            e3.setText(cursor.getString(3));
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t1 = e1.getText().toString();
                String t2 = e2.getText().toString();
                String t3 = e3.getText().toString();
                if (validation(t1, t2, t3)) {

                    Boolean result = db.upgrade(t1, t2, t3);
                    if(result)
                    {
                        Toast.makeText(EditActivity.this, "Upgraded Successfully", Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    Toast.makeText(EditActivity.this, "Recheck Details", Toast.LENGTH_LONG).show();
                }

                Intent i = new Intent(EditActivity.this,HomeActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    public boolean validation(String name, String Phno_1, String Phno_2) {

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(Phno_1) && !TextUtils.isEmpty(Phno_2))
            return Patterns.PHONE.matcher(Phno_1).matches() && Patterns.PHONE.matcher(Phno_2).matches()
                    && Phno_1.length()==10 && Phno_2.length()==10;

        return false;
    }
}