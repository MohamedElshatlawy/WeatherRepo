package com.example.mohamed.task.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohamed.task.R;
import com.example.mohamed.task.model.WeatherModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ForeCastItemAdapter  extends RecyclerView.Adapter<ForeCastItemAdapter.MyViewHolder> {

    Context context;
    ArrayList<WeatherModel>data;

    public ForeCastItemAdapter(Context context, ArrayList<WeatherModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ForeCastItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(context).inflate(R.layout.forecast_list_item_item,viewGroup,false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ForeCastItemAdapter.MyViewHolder myViewHolder, int i) {

        WeatherModel model=data.get(i);
        myViewHolder.tvTmp.setText(model.getCityTemp());
        myViewHolder.tvDesc.setText(model.getCityDesc());

        if(model.getCityDrawable()==null) {
            String url = "https://openweathermap.org/img/w/" + model.getCityImg() + ".png";
            Picasso.with(context).load(url).into(myViewHolder.im);
            model.setCityDrawable(myViewHolder.im.getDrawable());
        }else{
            myViewHolder.im.setImageDrawable(model.getCityDrawable());
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView im;
        TextView tvTmp,tvDesc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            im=itemView.findViewById(R.id.forecast_item_item_img);
            tvTmp=itemView.findViewById(R.id.forecast_item_item_txtTmp);
            tvDesc=itemView.findViewById(R.id.forecast_item_item_txtDesc);
        }
    }
}