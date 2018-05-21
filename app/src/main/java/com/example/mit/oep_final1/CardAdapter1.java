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
public class CardAdapter1 extends RecyclerView.Adapter<CardAdapter1.ViewHolder> {

    private ImageLoader imageLoader;
    private Context context;


    //List to store all superheroes
    List<requests> superHeroes;

    //Constructor of this class
    public CardAdapter1(List<requests> superHeroes, Context context) {
        super();
        //Getting all superheroes
        this.superHeroes = superHeroes;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_list1, parent, false);
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



        }
    }
}