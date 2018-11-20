package com.example.mohamed.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mohamed.task.Interface.RetrofitClient;
import com.example.mohamed.task.Interface.WeatherService;
import com.example.mohamed.task.adapters.MyAdapter;
import com.example.mohamed.task.listener.ItemClickListener;
import com.example.mohamed.task.model.WeatherModel;
import com.kosalgeek.android.caching.FileCacher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlankFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public BlankFragment() {
        // Required empty public constructor
    }


    ArrayList<WeatherModel>data;

    MyAdapter myAdapter;
    RecyclerView rec;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v=inflater.inflate(R.layout.fragment_blank, container, false);
       rec = v.findViewById(R.id.mainList);
        data=new ArrayList<>();

        fileCacher=new FileCacher(getContext(),"weather.txt");

        rec.setLayoutManager(new LinearLayoutManager(getContext()));
        myAdapter=new MyAdapter(data,getContext());
        rec.setAdapter(myAdapter);

        if(!checkInternetConnection()){
            //getCache();
            if(fileCacher.hasCache()){
                System.out.println("No internet and has cache");
                getCache();
            }else{

                System.out.println("No internet No cache");
            }

        }else{
            try {
                fileCacher.clearCache();
                getData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        myAdapter.setClickListener(new ItemClickListener() {
            @Override
            public void onCLick(View v, int position) {

                // Toast.makeText(getContext(),,Toast.LENGTH_LONG).show();

                String name=data.get(position).getCityName();
                Bundle bundle=new Bundle();
                bundle.putString("name",name);

                ForecastFragment forecastFragment=new ForecastFragment();
                forecastFragment.setArguments(bundle);


                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame,forecastFragment);
                        transaction.addToBackStack(null);
                       transaction.commit();


            }
        });



        return v;
    }

    private void getCache() {

        try {
            //      Toast.makeText(this,"has cache",Toast.LENGTH_LONG).show();
            data=fileCacher.readCache();
            System.out.println("size:"+data.size());
            myAdapter=new MyAdapter(data,getContext());
            rec.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    boolean checkInternetConnection(){
        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }



    FileCacher<ArrayList<WeatherModel>> fileCacher;
    private void getData() {

        String citiesIds="524901,703448,2643743,707860,519188,1283378";
        String tempTYpe="metric";
        String apiKey="d5b8cc23a92a8c2ae540b2ec943e9645";

        final ProgressDialog progressBar = new ProgressDialog(getContext());
        progressBar.setMessage("Loading...");
        progressBar.show();

        WeatherService weatherService = RetrofitClient.newInstance().create(WeatherService.class);
        weatherService.getTemp(citiesIds,tempTYpe,apiKey).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    progressBar.dismiss();


                    try {
                        String res = response.body().string();
                        JSONObject jsonObject=new JSONObject(res);


                        JSONArray list=jsonObject.getJSONArray("list");

                        for(int i=0;i<list.length();i++){
                            JSONObject listItem = list.getJSONObject(i);
                            String name=listItem.getString("name");
                            String temp=listItem.getJSONObject("main").getString("temp");

                            JSONArray weather = listItem.getJSONArray("weather");
                            String img=weather.getJSONObject(0).getString("icon");
                            String desc=weather.getJSONObject(0).getString("description");

                            data.add(new WeatherModel(name,temp,img,desc));


                        }
                        myAdapter.notifyDataSetChanged();
                        //fileCacher.writeCache(data);
                        setCache(data);
                    } catch (JSONException e) {
                        System.out.println("Json Error:"+e.getMessage());
                    } catch (IOException e) {
                        System.out.println("Res Error:"+e.getMessage());
                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                progressBar.dismiss();
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setCache(ArrayList<WeatherModel> cacheItem) {

        try {
            System.out.println(cacheItem);
            fileCacher.clearCache();
            fileCacher.writeCache(cacheItem);
            /*if(stringCacher.hasCache()){
                Toast.makeText(this,"has cache inside setcache",Toast.LENGTH_LONG).show();
                stringCacher.clearCache();
                stringCacher= new FileCacher<>(MainActivity.this, "repCache.txt");
            }*/
            //stringCacher.writeCache(cacheItem);

            if(fileCacher.hasCache()){
                System.out.println("Inside SetCache DoneCache");

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
