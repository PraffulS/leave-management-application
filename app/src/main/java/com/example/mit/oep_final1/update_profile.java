package com.example.mit.oep_final1;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class update_profile extends Fragment {


    Context ctx;
    private LoginAsync login_async;
    static final int id = 0;
    private EditText uname,pwd1,pwd2,dept_id,desg,jdate,no;
    private Button b;
    String url = "http://prafful.esy.es/fetch_names.php";
    SharedPreferences pref;
    public update_profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);
        uname = (EditText)view.findViewById(R.id.updatetxtusername);
        pwd1 = (EditText)view.findViewById(R.id.updatetxtpass1);
        pwd2 = (EditText)view.findViewById(R.id.updatetxtpass2);
        dept_id = (EditText)view.findViewById(R.id.updatetxtdept);
        desg = (EditText)view.findViewById(R.id.updatetxtdesignation);
        jdate = (EditText)view.findViewById(R.id.updatetxtjoindate);
        no = (EditText)view.findViewById(R.id.updatetxtphone);
        b = (Button)view.findViewById(R.id.updatebtnsubmit);
        pref = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pwd1.getText().toString().equals(pwd2.getText().toString())) {
                    login_async = new LoginAsync();
                    login_async.execute(uname.getText().toString(), pwd1.getText().toString(),dept_id.getText().toString(),desg.getText().toString(),jdate.getText().toString(),no.getText().toString());
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(),"password not matched",Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
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
            uname=params[0];
            pwd=params[1];
            dept_id=params[2];
            desg = params[3];
            jdate = params[4];
            no = params[5];

            uid= pref.getString("uid","");
            String register_url="http://prafful.esy.es/update.php"; //php file
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
                Toast.makeText(getActivity().getApplicationContext(),"check internet connection",Toast.LENGTH_LONG).show();
            }
            else
            {
                if (flag1==0)
                    Toast.makeText(getActivity().getApplicationContext(),"some problem occured while inseerting",Toast.LENGTH_LONG).show();
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(),"updated successfull",Toast.LENGTH_LONG).show();

                }
            }
        }
    }
}
