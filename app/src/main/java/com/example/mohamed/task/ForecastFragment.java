package com.example.mohamed.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mohamed.task.Interface.RetrofitClient;
import com.example.mohamed.task.Interface.WeatherService;
import com.example.mohamed.task.adapters.ForecastAdapter;

import com.example.mohamed.task.model.WeatherModel;
import com.kosalgeek.android.caching.FileCacher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForecastFragment extends Fragment {


    RecyclerView rec;
    ForecastAdapter forecastAdapter;

    ArrayList<ArrayList<WeatherModel>>data;
    FileCacher <ArrayList<ArrayList<WeatherModel>>> fileCacher;

    public ForecastFragment() {
        // Required empty public constructor

    }


    String name;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString("name");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_forecast, container, false);

       rec=v.findViewById(R.id.forecast_list);
        rec.setLayoutManager(new LinearLayoutManager(getContext()));
        fileCacher=new FileCacher<>(getActivity(),"subWeather.txt");

        data=new ArrayList<>();
        forecastAdapter=new ForecastAdapter(data,getContext());
        rec.setAdapter(forecastAdapter);



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

        return v;
    }

    private void getCache() {

        try {
            //      Toast.makeText(this,"has cache",Toast.LENGTH_LONG).show();
            data=fileCacher.readCache();
            System.out.println("size:"+data.size());

            forecastAdapter=new ForecastAdapter(data,getContext());
            rec.setAdapter(forecastAdapter);
            forecastAdapter.notifyDataSetChanged();
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

    private void getData() {

        String tempTYpe="metric";
        String apiKey="d5b8cc23a92a8c2ae540b2ec943e9645";

        final ProgressDialog progressBar = new ProgressDialog(getContext());
        progressBar.setMessage("Loading...");
        progressBar.show();

        WeatherService weatherService = RetrofitClient.newInstance().create(WeatherService.class);
        weatherService.getForecast(name,tempTYpe,apiKey).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    progressBar.dismiss();

                    try {
                        String res=response.body().string();
                        System.out.println("res:"+res);
                        JSONObject jsonObject=new JSONObject(res);
                        JSONArray list = jsonObject.getJSONArray("list");

                        int i=0,j=0;
                        ArrayList<WeatherModel>d1=new ArrayList<>();
                        String oldDate="";

                        for(;j<list.length();j++){

                            JSONObject listItem = list.getJSONObject(j);

                            String temp=listItem.getJSONObject("main").getString("temp");
                            JSONArray weather = listItem.getJSONArray("weather");
                            String img=weather.getJSONObject(0).getString("icon");
                            String desc=weather.getJSONObject(0).getString("description");


                            String date=listItem.getString("dt_txt");
                            String[] splits = date.split(" ");

                            String newDate=splits[0];
                            //System.out.println(newDate);

                            if(!oldDate.equals(newDate)&&!oldDate.equals("")){
                                data.add(i,d1);
                                d1.clear();
                                d1.add(new WeatherModel(name,temp,img,desc));


                            }else {
                                d1.add(new WeatherModel(name,temp,img,desc));

                            }
                            oldDate=newDate;


                        }

                        forecastAdapter.notifyDataSetChanged();
                        setCache(data);

                    } catch (IOException e) {
                        System.out.println("Error res convert:"+e.getMessage());
                    } catch (JSONException e) {
                        e.printStackTrace();
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

    private void setCache(ArrayList<ArrayList<WeatherModel>> cacheItem) {

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
