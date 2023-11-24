package com.example.my;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class selectactivity extends AppCompatActivity {
Button btn,btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectactivity);
        btn1=findViewById(R.id.button1);
        btn=findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(selectactivity.this, "进入监控", Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(selectactivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void myclick(View v){
        Toast.makeText(selectactivity.this, "进入拍照", Toast.LENGTH_SHORT).show();
        Intent intent =new Intent(selectactivity.this,cameractivity.class);
        startActivity(intent);
    }
}