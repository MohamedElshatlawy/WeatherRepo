package com.example.mohamed.task.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mohamed.task.R;
import com.example.mohamed.task.model.WeatherModel;

import java.util.ArrayList;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.MyViewHolder> {

    Context context;
    ArrayList<ArrayList<WeatherModel>>data;
    public ForecastAdapter(ArrayList<ArrayList<WeatherModel>>data,Context context) {
        this.data=data;
        this.context=context;
    }

    @NonNull
    @Override
    public ForecastAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.forecast_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;

    }


    @Override
    public void onBindViewHolder( ForecastAdapter.MyViewHolder myViewHolder, int i) {

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        myViewHolder.recyclerView.setLayoutManager(layoutManager);

        ForeCastItemAdapter forecastAdapter=new ForeCastItemAdapter(context,data.get(i));
       // System.out.println("Data:"+data.get(i).get(j).getCityTemp());
        myViewHolder.recyclerView.setAdapter(forecastAdapter);
        forecastAdapter.notifyDataSetChanged();
        //j++;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        public MyViewHolder( View itemView) {
            super(itemView);
            recyclerView=itemView.findViewById(R.id.forecast_item);
        }
    }

}
