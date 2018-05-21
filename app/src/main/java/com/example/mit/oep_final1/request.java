package com.example.mit.oep_final1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import okhttp3.*;

/*import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;*/

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.lang.*;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * A simple {@link Fragment} subclass.
 */
public class request extends Fragment {


    private SendRequest sendrequest;
    Context ctx;
    private Spinner s1, s2;
    int year=2007, month=04, day=23;
    String date;
    static final int id = 0;
    private EditText leave_type, leave_reason, total_days;
    public static Button submit, from, to;
    private ArrayList<String> users;
    private JSONArray result;
    ArrayList<String> listItems=new ArrayList<>();
    ArrayAdapter<String> adapter;
    String url = "http://prafful.esy.es/fetch_names.php";
    SharedPreferences pref;
    DatePickerDialog d;
    public request() {
        //this.ctx = ctx;
        // Required empty public constructor
    }
    /*public request(Context  ctx) {
        this.ctx = ctx;
        // Required empty public constructor
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        leave_type = (EditText) view.findViewById(R.id.leavetype);
        leave_reason = (EditText) view.findViewById(R.id.leavereason);
        total_days = (EditText) view.findViewById(R.id.totaldays);
        from = (Button) view.findViewById(R.id.from);
        okHttpHandler ok = new okHttpHandler();
        ok.execute();
        to = (Button) view.findViewById(R.id.to);
        submit = (Button) view.findViewById(R.id.button3);
        s1 = (Spinner) view.findViewById(R.id.spinner);
        users = new ArrayList<String>();
        url = "http://prafful.esy.es/fetch_names.php";
        pref = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);


        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment newFragment = new SelectDateFragment();

                newFragment.show(getActivity().getFragmentManager(),"datee");
            }
        });

       to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DialogFragment newFragment = new SelectDateFragment1();

                newFragment.show(getActivity().getFragmentManager(),"datee");
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendrequest = new SendRequest();
                sendrequest.execute(leave_type.getText().toString(),leave_reason.getText().toString(),from.getText().toString(),to.getText().toString(),total_days.getText().toString(),s1.getSelectedItem().toString());
            }
        });
        return view;
    }



    public class okHttpHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();

       @Override
        protected String doInBackground(String[] params) {
            Request.Builder builder = new Request.Builder();
            builder.url("http://prafful.esy.es/fetch_names.php");
            Request request = builder.build();
            try
            {
                Log.e("reached here1","reached here1");
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch(Exception e)
            {
                Log.e("reached here1","reached here1");
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String o) {
            try {
                JSONObject obj = new JSONObject(o.toString());
                JSONArray jsonArray = obj.getJSONArray("result");
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject x = jsonArray.getJSONObject(i);
                    String n = x.getString("name");
                    listItems.add(n);

                }
                Log.e("reached here1","reached here1");
                adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,listItems);
                s1.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }



   public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{


       @Override
       public Dialog onCreateDialog(Bundle savedInstanceState) {

           return new DatePickerDialog(getActivity(),this,2017,9,23);
       }

       @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
           Toast.makeText(getActivity().getApplicationContext(), "hello there", Toast.LENGTH_SHORT).show();
           from.setText(String.valueOf(i2)+"/"+String.valueOf(i1)+"/"+String.valueOf(i));

        }
    }

    public static class SelectDateFragment1 extends DialogFragment implements DatePickerDialog.OnDateSetListener{


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            return new DatePickerDialog(getActivity(),this,2017,9,23);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            Toast.makeText(getActivity().getApplicationContext(), "hello there", Toast.LENGTH_SHORT).show();
            to.setText(String.valueOf(i2)+"/"+String.valueOf(i1)+"/"+String.valueOf(i));

        }
    }
    class SendRequest extends AsyncTask<String,Void,String>
    {
        int flag1=0,flag2=0;
        @Override
        protected void onPreExecute()
        {

            //load=ProgressDialog.show(context,"status","logging in",true,false);

        }
        @Override
        protected String doInBackground(String... params) {
            String leavetype,leavereason,from,to,totaldays,loadtransfer,uid;
            leavetype=params[0];
            leavereason=params[1];
            from=params[2];
            to=params[3];
            totaldays = params[4];
            loadtransfer = params[5];
           uid= pref.getString("uid","");

            //Toast.makeText(getActivity().getApplicationContext(),"->"+uid.toString(),Toast.LENGTH_LONG).show();
            String register_url="http://prafful.esy.es/sendrequest.php"; //php file
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
                        URLEncoder.encode("leavetype","UTF-8")+"="+URLEncoder.encode(leavetype,"UTF-8")+"&"+
                        URLEncoder.encode("leavereason","UTF-8")+"="+URLEncoder.encode(leavereason,"UTF-8")+"&"+
                        URLEncoder.encode("from","UTF-8")+"="+URLEncoder.encode(from,"UTF-8")+"&"+
                        URLEncoder.encode("to","UTF-8")+"="+URLEncoder.encode(to,"UTF-8")+"&"+
                        URLEncoder.encode("totaldays","UTF-8")+"="+URLEncoder.encode(totaldays,"UTF-8")+"&"+
                        URLEncoder.encode("loadtransfer","UTF-8")+"="+URLEncoder.encode(loadtransfer,"UTF-8");
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
                    Toast.makeText(getActivity().getApplicationContext(),"request sent successfully",Toast.LENGTH_LONG).show();
                    //Intent intent=new Intent(singup1.this,login1.class);
                   // startActivity(intent);
                }
            }
        }
    }

}
