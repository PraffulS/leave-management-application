package com.example.mit.oep_final1;

/**
 * Created by Administrator on 9/28/2017.
 */
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.w3c.dom.Text;

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
import java.util.List;
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private ImageLoader imageLoader;
    private Context context;

    //List to store all superheroes
    List<requests> superHeroes;

    //Constructor of this class
    public CardAdapter(List<requests> superHeroes, Context context) {
        super();
        //Getting all superheroes
        this.superHeroes = superHeroes;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Getting the particular item from the list
        requests superHero = superHeroes.get(position);

        //Loading image from url
        //imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        //imageLoader.get(superHero.getImageUrl(), ImageLoader.getImageListener(holder.imageView, R.drawable.image, android.R.drawable.ic_dialog_alert));

        //Showing data on the views
        //holder.imageView.setImageUrl(superHero.getImageUrl(), imageLoader);
        holder.textViewName.setText(superHero.getName());
        holder.leave_id.setText(superHero.get_leave_id());
        holder.leave_type.setText(superHero.get_leave_type());
        holder.leave_reason.setText(superHero.get_leave_reason());
        holder.from_date.setText(superHero.get_from_date());
        holder.to_date.setText(superHero.get_to_date());
        holder.total_days.setText(superHero.get_no_of_days());
        holder.load_transfer.setText(superHero.get_load_transfer());
        //holder.

    }

    @Override
    public int getItemCount() {
        return superHeroes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //Views
        public NetworkImageView imageView;
        public TextView textViewName;
        public TextView textViewPublisher, leave_id, leave_type, leave_reason, from_date, to_date, total_days, load_transfer;
        public Button accept;
        public Button reject;

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            //imageView = (NetworkImageView) itemView.findViewById(R.id.imageViewHero);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            leave_id = (TextView) itemView.findViewById(R.id.textLeave_Id);
            leave_type = (TextView) itemView.findViewById(R.id.textLeave_Type);
            leave_reason = (TextView) itemView.findViewById(R.id.textLeave_Reason);
            from_date= (TextView) itemView.findViewById(R.id.textFrom_Date);
            to_date = (TextView) itemView.findViewById(R.id.textTo_Date);
            total_days = (TextView) itemView.findViewById(R.id.textTotal_Days);
            load_transfer = (TextView) itemView.findViewById(R.id.textLoad_Transfer);


            accept = (Button) itemView.findViewById(R.id.accept);
            accept.setText("accept");
            //accept.setBackgroundColor();
            reject = (Button) itemView.findViewById(R.id.reject);
            reject.setText("reject");
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    bgprocess bg1 = new bgprocess();
                    bg1.execute(leave_id.getText().toString(),"1");
                }
            });
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bgprocess bg1 = new bgprocess();
                    bg1.execute(leave_id.getText().toString(),"0");
                }
            });
        }
    }
    class bgprocess extends AsyncTask<String,Void,String>{
        int flag1=0,flag2=0;
        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String leave_id = strings[0];
            String register_url = "";
            if(strings[1].equals("1")) {
                register_url = "http://prafful.esy.es/accept_request.php"; //php file
            }
            else
            {
                register_url = "http://prafful.esy.es/reject_request.php";
            }
            try
            {
                URL url=new URL(register_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data= URLEncoder.encode("leave_id","UTF-8")+"="+URLEncoder.encode(leave_id,"UTF-8");
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
        protected void onPostExecute(String s) {
            if(flag2==1)
            {
                Toast.makeText(context,"check internet connection",Toast.LENGTH_LONG).show();
            }
            else
            {
                if (flag1==0)
                    Toast.makeText(context,"some problem occured",Toast.LENGTH_LONG).show();
                else
                {
                    Toast.makeText(context,"request accepted/rejected successfully",Toast.LENGTH_LONG).show();

                    Intent i = new Intent(context, admin_panel.class);
                  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                    //((Activity)context).finish();
                    //Intent intent=new Intent(getIntent());
                    // startActivity(intent);

                }
            }
        }
    }
}