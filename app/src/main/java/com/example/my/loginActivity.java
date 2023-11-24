package com.example.my;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
public class loginActivity extends AppCompatActivity {
    public Button btn;
    public Button btn1;
    public EditText mim;
    private EditText user;
    public TextView tx;
    boolean isCancel=false;
    int time=8;
    Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginmain);
        mim=(EditText)findViewById(R.id.mm1);
        tx=findViewById(R.id.tx2);
        EditText user= (EditText) findViewById(R.id.yh);
        btn = (Button) findViewById(R.id.button);
        btn1=(Button)findViewById(R.id.button2);
        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }});
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String psw = mim.getText().toString();
                String zhh = user.getText().toString();
                if (psw.equals("123") && zhh.equals("00")) {
                    Toast.makeText(loginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(loginActivity.this,selectactivity.class);
                    startActivity(intent);
                } else if (psw.equals("") && zhh.equals("")) {
                    Toast.makeText(loginActivity.this, "密码和账号不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(loginActivity.this, "登录失败，账号或密码错误", Toast.LENGTH_SHORT).show();
                    mim.setText("");
                    user.setText("");}}});}
}
