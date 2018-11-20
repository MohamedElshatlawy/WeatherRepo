package com.example.mohamed.task.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohamed.task.R;
import com.example.mohamed.task.listener.ItemClickListener;
import com.example.mohamed.task.model.WeatherModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
{

    private ArrayList<WeatherModel> mDataset;
    private Context context;
     private ItemClickListener itemClickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public  class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener
    {
        // each data item is just a string in this case
        public TextView tempView;
        public ImageView tempImage;
        public MyViewHolder(View v) {
            super(v);
            tempView = v.findViewById(R.id.mainTv);
            tempImage=v.findViewById(R.id.mainIv);
            v.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
        itemClickListener.onCLick(v,getAdapterPosition());
        }
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<WeatherModel> myDataset, Context context)
    {
        mDataset = myDataset;
        this.context=context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_list_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


            WeatherModel model=mDataset.get(position);
            holder.tempView.setText(model.getCityName());

            if(model.getCityDrawable()==null) {
                String url = "https://openweathermap.org/img/w/" + model.getCityImg() + ".png";


                Picasso.with(context).load(url).into(holder.tempImage);
                model.setCityDrawable(holder.tempImage.getDrawable());
            }else{
                holder.tempImage.setImageDrawable(model.getCityDrawable());
            }





    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}