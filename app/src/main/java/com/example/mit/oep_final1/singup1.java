package com.example.mit.oep_final1;

import android.content.Intent;
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

public class singup1 extends AppCompatActivity {

    private EditText uid,uname,pwd1,pwd2,dept_id,desg,jdate,no;
    private Button b;
    private LoginAsync login_async;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup1);
        uname = (EditText)findViewById(R.id.regtxtusername);
        pwd1 = (EditText)findViewById(R.id.regtxtpass1);
        pwd2 = (EditText)findViewById(R.id.regtxtpass2);
        uid = (EditText)findViewById(R.id.regtxtempid);
        dept_id = (EditText)findViewById(R.id.regtxtdept);
        desg = (EditText)findViewById(R.id.regtxtdesignation);
        jdate = (EditText)findViewById(R.id.regtxtjoindate);
        no = (EditText)findViewById(R.id.reftxtphone);
        b = (Button)findViewById(R.id.regbtnsubmit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pwd1.getText().toString().equals(pwd2.getText().toString())) {
                    login_async = new LoginAsync();
                    login_async.execute(uid.getText().toString(),uname.getText().toString(), pwd1.getText().toString(),dept_id.getText().toString(),desg.getText().toString(),jdate.getText().toString(),no.getText().toString());
                }
                else
                {
                    Toast.makeText(singup1.this,"password not matched",Toast.LENGTH_LONG).show();
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
            String uid,uname,pwd,dept_id,desg,jdate,no;
            uid=params[0];
            uname=params[1];
            pwd=params[2];
            dept_id=params[3];
            desg = params[4];
            jdate = params[5];
            no = params[6];

            String register_url="http://prafful.esy.es/register.php"; //php file
            try
            {
                URL url=new URL(register_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data= URLEncoder.encode("uid","UTF-8")+"="+URLEncoder.encode(uid,"UTF-8")+"&"+
                        URLEncoder.encode("uname","UTF-8")+"="+URLEncoder.encode(uname,"UTF-8")+"&"+
                        URLEncoder.encode("pwd","UTF-8")+"="+URLEncoder.encode(pwd,"UTF-8")+"&"+
                        URLEncoder.encode("dept_id","UTF-8")+"="+URLEncoder.encode(dept_id,"UTF-8")+"&"+
                        URLEncoder.encode("desg","UTF-8")+"="+URLEncoder.encode(desg,"UTF-8")+"&"+
                        URLEncoder.encode("jdate","UTF-8")+"="+URLEncoder.encode(jdate,"UTF-8")+"&"+
                        URLEncoder.encode("no","UTF-8")+"="+URLEncoder.encode(no,"UTF-8");
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
                Toast.makeText(singup1.this,"check internet connection",Toast.LENGTH_LONG).show();
            }
            else
            {
                if (flag1==0)
                    Toast.makeText(singup1.this,"some problem occured while inseerting",Toast.LENGTH_LONG).show();
                else
                {
                    Toast.makeText(singup1.this,"register successfull",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(singup1.this,login1.class);
                    startActivity(intent);
                }
            }
        }
    }
}
