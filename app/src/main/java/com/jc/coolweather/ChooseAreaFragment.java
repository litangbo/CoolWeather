package com.jc.coolweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jc.coolweather.db.City;
import com.jc.coolweather.db.County;
import com.jc.coolweather.db.Province;
import com.jc.coolweather.util.HttpUtil;
import com.jc.coolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseAreaFragment extends Fragment{
    /**省市县数据查询地址前缀*/
    public static final String URL = "http://guolin.tech/api/china";

    public static final int LEVEL_PROVINCE = 1;
    public static final int LEVEL_CITY = 2;
    public static final int LEVEL_COUNTY = 3;

    /**查询进度对话框*/
    private ProgressDialog progressDialog;

    /**标题文本*/
    private TextView titleText;
    /**返回按钮*/
    private Button backButton;
    /**省市县数据展示列表*/
    private ListView listView;

    /**字符串数组适配器*/
    private ArrayAdapter<String> adapter;
    /**字符串数组*/
    private List<String> dataList = new ArrayList<>();

    /**查询的省列表*/
    private List<Province> provinceList;
    /**查询的市列表*/
    private List<City> cityList;
    /**查询的县列表*/
    private List<County> countyList;

    /**选中的省*/
    private Province selectedProvince;
    /**选中的市*/
    private City selectedCity;

    /**当前选中或显示的级别*/
    private int currentLevel;

    private FragmentActivity activity;

    /**
     * 创建碎片布局，利用LayoutInflater
     * @param inflater 布局填充器
     * @param container 父容器
     * @param savedInstanceState 保存的活动实例状态
     * @return 返回创建的View
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false);
        // 初始化碎片内的控件
        titleText = view.findViewById(R.id.title_text);
        backButton = view.findViewById(R.id.back_button);
        listView = view.findViewById(R.id.list_view);
        // 创建andorid内置的简单列表项布局适配器：android.R.layout.simple_list_item_1
        // getContext() might be null
        adapter = new ArrayAdapter<>(view.getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    /**
     * 活动创建完成后，监听列表项和返回按钮的点击事件，实现不同级别的数据查询显示
     * @param savedInstanceState 保存的活动实例状态
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 获取碎片活动
        activity = getActivity();
        // 列表项点击监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(currentLevel == LEVEL_PROVINCE){
                    // 获取选中的省，并查询该省下所有的市
                    selectedProvince = provinceList.get(i);
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    // 获取选中的市，并查询该省下所有的县
                    selectedCity = cityList.get(i);
                    queryCounties();
                }else if(currentLevel == LEVEL_COUNTY){
                    /*String weatherId = countyList.get(i).getWeatherId();
                    if (getActivity() instanceof MainActivity) {
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra("weather_id", weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    } else if (getActivity() instanceof WeatherActivity) {
                        WeatherActivity activity = (WeatherActivity) getActivity();
                        activity.drawerLayout.closeDrawers();
                        activity.swipeRefresh.setRefreshing(true);
                        activity.requestWeather(weatherId);
                    }*/
                }
            }
        });
        // 返回按钮点击监听
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentLevel == LEVEL_COUNTY){
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    queryProvinces();
                }
                // 省一级，不存在返回按钮
            }
        });
        // 初始化查询所有省
        queryProvinces();
    }

    /**
     * 查询全国所有的省，优先从数据库查询，如果没有查询到，再去服务器查询
     */
    private void queryProvinces(){
        // 设置标题文本
        titleText.setText("中国");
        // 隐藏返回按钮
        backButton.setVisibility(View.GONE);
        // 优先从数据库查询
        provinceList = DataSupport.findAll(Province.class);
        if(provinceList.size() > 0){
            dataList.clear();// 必须先清除历史数据
            for(Province province : provinceList){
                dataList.add(province.getProvinceName()+","+province.getProvinceCode());
            }
            // 用适配器通知ListView数据发生改变
            adapter.notifyDataSetChanged();
            listView.setSelection(0);// 默认选中第一项
            currentLevel = LEVEL_PROVINCE;// 设置选中级别
        }else{
            // 数据库查询不到，再到服务器查询
            String address = URL+"";
            queryFromServer(address,LEVEL_PROVINCE);
        }
    }

    /**
     * 查询选中省下的所有市，优先从数据库查询，如果没有查询到，再去服务器查询
     */
    private void queryCities(){
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceId=?",selectedProvince.getId()+"").find(City.class);
        if(cityList.size() > 0){
            dataList.clear();
            for(City city : cityList){
                dataList.add(city.getCityName()+","+city.getCityCode());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else{
            String address = URL+"/"+selectedProvince.getProvinceCode();
            queryFromServer(address,LEVEL_CITY);
        }
    }

    /**
     * 查询选中市下的所有县，优先从数据库查询，如果没有查询到，再去服务器查询
     */
    private void queryCounties(){
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        // org.litepal.exceptions.DataSupportException: The parameters in conditions are incorrect.
        // countyList = DataSupport.where("cityId",selectedCity.getId()+"").find(County.class);
        countyList = DataSupport.where("cityId=?",selectedCity.getId()+"").find(County.class);
        if(countyList.size() > 0){
        dataList.clear();
        for(County county : countyList){
            dataList.add(county.getCountyName()+","+county.getWeatherId());
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
        currentLevel = LEVEL_COUNTY;
    }else{
        String address = URL+"/"+selectedProvince.getProvinceCode()+"/"+selectedCity.getCityCode();
        queryFromServer(address,LEVEL_COUNTY);
    }
}

    /**
     * 从服务器查询省市县数据
     * @param address 数据查询地址
     * @param type 数据类型
     */
    private void queryFromServer(String address, final int type){
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // showProgressDialog();
                // 注意是string()方法，而非toString()方法
                String responseText = response.body().string();
                boolean isOk = false;
                if(LEVEL_PROVINCE == type){
                    isOk = Utility.handleProvinceResponse(responseText);
                }else if(LEVEL_CITY == type){
                    isOk = Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if(LEVEL_COUNTY == type){
                    isOk = Utility.handleCountyResponse(responseText,selectedCity.getId());
                }
                if(isOk){
                    // 转回活动的主线程去修改UI
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(LEVEL_PROVINCE == type){
                                queryProvinces();
                            }else if(LEVEL_CITY == type){
                                queryCities();
                            }else if(LEVEL_COUNTY == type){
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog(){
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
