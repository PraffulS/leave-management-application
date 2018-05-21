package com.example.mit.oep_final1;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
public class login1 extends AppCompatActivity {

   private Button login1,singup1,cancel1;
    private EditText uname,passwd;
    private LoginAsync login_async;
    SharedPreferences.Editor editor ;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);

        login1 = (Button) findViewById(R.id.loginbtnok);
        cancel1 = (Button) findViewById(R.id.loginbtncancel);
        singup1 = (Button) findViewById(R.id.loginbtnsingup);
        uname = (EditText)findViewById(R.id.logintxtuser);
        passwd = (EditText)findViewById(R.id.logintxtpass);


        editor = getSharedPreferences("user",MODE_PRIVATE).edit();
        editor.putString("uid","0");
        editor.apply();
        singup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1  = new Intent(login1.this,singup1.class);
                startActivity(i1);
            }
        });

        login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(uname.getText().toString().equals("admin") && passwd.getText().toString().equals("admin"))
                {
                    Intent intent=new Intent(login1.this,admin_panel.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    login_async = new LoginAsync();
                    login_async.execute(uname.getText().toString(), passwd.getText().toString());
                }
            }
        });


    }
    class LoginAsync extends AsyncTask<String,Void,String>
    {
        int flag1=0,flag2=0;
        @Override
        protected void onPreExecute()
        {

            //load=ProgressDialog.show(context,"status","logging in",true,false);

        }
        @Override
        protected String doInBackground(String... params) {
            String uname,pwd;
            uname=params[0];
            pwd=params[1];

            String register_url="http://prafful.esy.es/login.php"; //php file
            try
            {
                URL url=new URL(register_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data= URLEncoder.encode("uname","UTF-8")+"="+URLEncoder.encode(uname,"UTF-8")+"&"+
                        URLEncoder.encode("pwd","UTF-8")+"="+URLEncoder.encode(pwd,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(IS,"iso-8859-1"));
                String response="";
                response=bufferedReader.readLine();
                if(response!=null)
                    if(response.contains("unsuccessful")==true)
                    {
                        flag1=0;
                    }
                    else
                        flag1=1;
                IS.close();
                httpURLConnection.disconnect();
                return response;

            }
            catch (Exception e)
            {
                flag2 = 1;
                return "check internet connection";
            }
        }
        @Override
        protected void onPostExecute(String ans)
        {
            // load.cancel();


            if(flag2==1)
            {
                Toast.makeText(login1.this,"check internet connection",Toast.LENGTH_LONG).show();
            }
            else
            {
                if (flag1==0)
                    Toast.makeText(login1.this,"invalid username or password",Toast.LENGTH_LONG).show();
                else
                {
                    Toast.makeText(login1.this,"login successfull",Toast.LENGTH_LONG).show();
                    editor.putString("uid",uname.getText().toString());
                    editor.apply();
                    editor.commit();
                    Intent intent=new Intent(login1.this,after_login.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }
}
