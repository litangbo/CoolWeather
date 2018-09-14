package com.jc.coolweather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jc.coolweather.gson.AQI;
import com.jc.coolweather.gson.Basic;
import com.jc.coolweather.gson.Forecast;
import com.jc.coolweather.gson.Now;
import com.jc.coolweather.gson.Suggestion;
import com.jc.coolweather.gson.Weather;
import com.jc.coolweather.util.HttpUtil;
import com.jc.coolweather.util.Utility;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    /**activity_weather.xml*/
    private ScrollView weatherLayout;
    /**title.xml*/
    private TextView titleCity;
    private TextView titleUpdateTime;
    /**now.xml*/
    private TextView degreeText;
    private TextView weatherInfoText;
    /**forecast.xml*/
    private LinearLayout forecastLayout;
    /**aqi.xml*/
    private TextView aqiText;
    private TextView pm25Text;
    /**suggestion.xml*/
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;

    /**键值存储对象*/
    private SharedPreferences prefs;
    /**键值编辑对象*/
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        // 初始化各控件
        weatherLayout = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.title_city);
        titleUpdateTime = findViewById(R.id.title_update_time);
        degreeText = findViewById(R.id.degree_text);
        weatherInfoText = findViewById(R.id.weather_info_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        aqiText = findViewById(R.id.aqi_text);
        pm25Text = findViewById(R.id.pm25_text);
        comfortText = findViewById(R.id.comfort_text);
        carWashText = findViewById(R.id.car_wash_text);
        sportText = findViewById(R.id.sport_text);
        // 初始化键值存储对象
        prefs =
                // getSharedPreferences("data",MODE_PRIVATE);
                PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
        // 预先从缓存中解析天气数据
        String weatherString = prefs.getString("weather",null);
        if(weatherString != null){
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        }else{
            // 无缓存时再去服务器查询天气数据
            Intent intent = getIntent();
            String weatherId = intent.getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);// 请求之前不展示控件
            requestWeather(weatherId);
        }
    }

    /**
     * 根据天气id请求城市天气信息
     * @param weatherId
     */
    private void requestWeather(final String weatherId){
        String weatherUrl = "http://guolin.tech/api/weather?cityid="+weatherId+"&key=bc0418b57b2d4918819d3974ac1285d9";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // "this"是指(或者说:所代表的是)当前这段代码所在的类的对象
                // "类名.this"是指"类名"的对象(一般在匿名类或内部类中使用来调用外部类的方法或属性)
                // WeatherActivity.this.runOnUiThread(new Runnable() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather != null && "ok".equals(weather.status)){
                            // 存储天气缓存
                            editor.putString("weather",responseText);
                            editor.apply();
                            // 显示天气信息
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    /**
     * 将天气实体的信息显示在各控件中
     * @param weather
     */
    private void showWeatherInfo(Weather weather){
        Basic basic = weather.basic;
        Now now = weather.now;
        List<Forecast> forecastList = weather.forecastList;
        AQI aqi = weather.aqi;
        Suggestion suggestion = weather.suggestion;
        // 基本信息
        titleCity.setText(basic.cityName);
        titleUpdateTime.setText(basic.update.updateTime.split(" ")[0]);
        // 当前天气
        degreeText.setText(now.temperature+"℃");
        weatherInfoText.setText(now.more.info);
        // 空气质量
        aqiText.setText(aqi.city.aqi);
        aqiText.setText(aqi.city.pm25);
        // 建议信息
        comfortText.setText("舒适度："+suggestion.comfort.info);
        carWashText.setText("洗车指数："+suggestion.carWash.info);
        sportText.setText("运动建议："+suggestion.sport.info);
        // 预报列表
        forecastLayout.removeAllViews();
        for(Forecast forecast : forecastList){
            View itemView = getLayoutInflater().inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText = itemView.findViewById(R.id.date_text);
            TextView infoText = itemView.findViewById(R.id.info_text);
            TextView maxText = itemView.findViewById(R.id.max_text);
            TextView minText = itemView.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(itemView);
        }
        weatherLayout.setVisibility(View.VISIBLE);
    }
}
