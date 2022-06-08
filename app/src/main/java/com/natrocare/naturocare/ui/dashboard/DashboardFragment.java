package com.natrocare.naturocare.ui.dashboard;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.type.DateTime;
import com.natrocare.naturocare.CurrentWeatherApi;
import com.natrocare.naturocare.R;
import com.natrocare.naturocare.model.common.WeatherItem;
import com.natrocare.naturocare.model.currentweather.CurrentWeatherResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DashboardFragment extends Fragment implements LocationListener {

    private DashboardViewModel mViewModel;
    public double longitude=22.4,latitude=88.5;
    private final String apiKey = "594e5e5431f6e37f7c4f7be9a08f8e16";

    TextView windValue,temperature,feelsLikeValue,humidityValue,locationName,sunrise,sunset,motorValue;
    ImageView windArrow;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dashboard_fragment, container, false);
        windValue = root.findViewById(R.id.WindValue);
        windArrow = root.findViewById(R.id.WindArrow);
        temperature = root.findViewById(R.id.Temperature);
        feelsLikeValue = root.findViewById(R.id.FeelsLikeValue);
        humidityValue = root.findViewById(R.id.HumidityValue);
        locationName = root.findViewById(R.id.LocationName);
        sunrise = root.findViewById(R.id.DayNightValue);
        sunset = root.findViewById(R.id.DayNightSunsetTime);
        motorValue = root.findViewById(R.id.MotorValue);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CurrentWeatherApi currentWeatherApi = retrofit.create(CurrentWeatherApi.class);

        Call<CurrentWeatherResponse> currentWeather = currentWeatherApi.getCurrentWeather(latitude,longitude,apiKey,"metric");

        currentWeather.enqueue(new Callback<CurrentWeatherResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {
                if(response.isSuccessful()){
                    Log.d("ResponseBody", ""+response.body().getMain().getTemp());
                    if(!TextUtils.isEmpty(response.body().getName())){
                        locationName.setText(response.body().getName());
                    }
                    List<WeatherItem> weatherItems = response.body().getWeather();
                    for (WeatherItem weatherItem : weatherItems){
                        temperature.setText(String.format(Locale.getDefault(), "%.0f°",response.body().getMain().getTemp()+30)+" | "+weatherItem.getMain());
                    }
                    windValue.setText(""+response.body().getWind().getSpeed());
                    windArrow.setRotation(Float.parseFloat(""+(105+response.body().getWind().getDeg())%360));
                    feelsLikeValue.setText(String.format(Locale.getDefault(), "%.0f°",response.body().getMain().getFeelsLike()+30));
                    humidityValue.setText(String.format(Locale.getDefault(), "%d%%",response.body().getMain().getHumidity()));
                    Date date1 = new Date(response.body().getSys().getSunrise());
                    Date date2 = new Date(response.body().getSys().getSunset());
                    sunrise.setText(String.format("%02d:%02d",date1.getHours(),date1.getMinutes()));
                    sunset.setText(String.format("%02d:%02d",date2.getHours(),date2.getMinutes()));
                }else{
                    Log.d("ResponseBody","Response Failed");
                }
            }

            @Override
            public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return root;
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
    }
}