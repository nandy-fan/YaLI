package com.jingnan.jingnanlibrary.net.netjava;

import android.app.Activity;
import android.content.Context;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.download.DownloadQueue;
import com.yanzhenjie.nohttp.download.DownloadRequest;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;

import java.util.ArrayList;
import java.util.List;

/**
 * 队列的单例模式封装
 */

public class CallServerJava {

    public static CallServerJava instance;
    private RequestQueue mRequestQueue;
    private DownloadQueue mDownloadQueue;
    List<RequestQueue> listRequestQueues = new ArrayList<>();

    //双重校验锁式
    public static CallServerJava getInstance(){
        if(instance == null)
            synchronized (CallServerJava.class){
                if(instance == null)
                    instance = new CallServerJava();
            }
            return instance;
        }

    private CallServerJava() {
        int MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2 + 1;// 设置线程池的最大线程数
        mRequestQueue = NoHttp.newRequestQueue(MAX_POOL_SIZE/2);
        mDownloadQueue = NoHttp.newDownloadQueue(MAX_POOL_SIZE/2);
    }
        //获得创建的队列
    public RequestQueue getmRequestQueue() {
        return mRequestQueue;
    }

    /**
     * 添加一个请求到请求队列。
     *
     * @param what      用来标志请求, 当多个请求使用同一个Listener时, 在回调方法中会返回这个what。
     * @param request   请求对象。
     * @param listener  结果回调对象。
     */
    public <T> void add(int what, Request<T> request, OnResponseListener listener) {
        mRequestQueue.add(what, request, listener);
        listRequestQueues.add(mRequestQueue);
    }

    public <T> void add(Activity activity, int what, Request<T> request, HttpListener<T> callback,
                            boolean canCancel, boolean isLoading, Class<?> modelclass) {
        mRequestQueue.add(what, request, new HttpResponseListenerJava<>(activity, request, callback, canCancel, isLoading, modelclass));
        listRequestQueues.add(mRequestQueue);
    }

    public <T> void add(Context context, int what, Request<T> request, HttpListener<T> callback,
                            boolean canCancel, boolean isLoading, Class<?> modelclass) {
        mRequestQueue.add(what, request, new HttpResponseListenerJava<>(context, request, callback, canCancel, isLoading, modelclass));
        listRequestQueues.add(mRequestQueue);
    }

    public <T> void add(int what, Request<T> request, HttpListener<T> callback,
                            Class<?> modelclass) {
        mRequestQueue.add(what, request, new HttpResponseListenerJava<>(request, callback, modelclass));
        listRequestQueues.add(mRequestQueue);
    }

    public <T> void add(int what, Request<T> request, HttpListener<T> callback, boolean canCancel, boolean isLoading,
                            Class<?> modelclass) {
        mRequestQueue.add(what, request, new HttpResponseListenerJava<>(request, callback, modelclass));
        listRequestQueues.add(mRequestQueue);
    }


//    public void download(int what, DownloadRequest request, HttpDownloadListener listener) {
//        mDownloadQueue.add(what, request, listener);
//    }


    public void stopNetALl(){
        for(int i=0;i<listRequestQueues.size();i++){
            listRequestQueues.get(i).cancelAll();
            listRequestQueues.get(i).stop();
        }
    }

}
