package com.jc.coolweather.util;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Http传输工具
 */
public class HttpUtil {
    /**
     * 发送一个异步GET请求的4个步骤：
     * 1、创建OkHttpClient对象
     * 2、通过Builder模式创建Request对象，参数必须有个url参数，可以通过Request.Builder设置更多的参数比如：header、method等
     * 3、通过request的对象去构造得到一个Call对象，Call对象有execute()和cancel()等方法。
     * 4、以异步的方式去执行请求，调用的是call.enqueue，将call加入调度队列，任务执行完成会在Callback中得到结果。
     * @param address
     * @param callback
     */
    public static void sendOkHttpRequest(String address, Callback callback){
        // 创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();
        // 创建Request对象，设置一个url地址，并设置请求方式（Builder模式）
        Request request = new Request.Builder().url(address).method("GET",null).build();
        // 创建Call对象
        Call call = client.newCall(request);
        // 异步请求，将call加入调度队列，重写回调方法
        // enqueue 入队；排队
        call.enqueue(callback);
    }
}
