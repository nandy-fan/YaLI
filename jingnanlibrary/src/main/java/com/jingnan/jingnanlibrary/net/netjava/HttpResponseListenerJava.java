package com.jingnan.jingnanlibrary.net.netjava;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.jingnan.jingnanlibrary.R;
import com.jingnan.jingnanlibrary.bean.BaseModelJava;
import com.jingnan.jingnanlibrary.bean.TestBeans;
import com.jingnan.jingnanlibrary.model.OnAgainLoginListener;
import com.yanzhenjie.nohttp.error.*;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class HttpResponseListenerJava<T> implements OnResponseListener<T> {

    private Activity mActivity;
    private Context context;
    //请求对象
    private Request<?> mRequest;
    //结果回调
    private HttpListener<T> callback;
    private Class<?> modelClass;

    private boolean canCancel = true;
    private boolean isLoading = false;

    public static OnAgainLoginListener onAgainLoginListener;

    @Override
    public void onStart(int what) {
        if (mActivity != null) {
            if (isLoading) {
                if (canCancel) {
                    //DialogUtil.startDialogLoadingText(mActivity, "加载中...");
                } else {
                    //DialogUtil.startDialogLoading(mActivity, true);
                }
            }
        }
        if (context != null) {
            if (isLoading) {
                if (canCancel) {
                    //DialogUtil.startDialogLoadingText(context, "加载中...");
                } else {
                    //DialogUtil.startDialogLoading(context, true);
                }
            }
        }
    }

    @Override
    public void onSucceed(int what, Response response) {
        if (callback != null) {
            // 这里判断一下http响应码，这个响应码问下你们的服务端你们的状态有几种，一般是200成功。
            // w3c标准http响应码：http://www.w3school.com.cn/tags/html_ref_httpmessages.asp
            String request = response.get().toString();

            //L.json("返回值：" + UnicodeUtils.decodeUnicode(request));
            //L.e("返回值：" + UnicodeUtils.decodeUnicode(request));
            Gson gson = new Gson();
            try {

               // BaseModelJava bmodel = JSON.parseObject(request, BaseModelJava.class);
               // setTimeSync(bmodel.getTimestamp());//存储当前时间和服务器时间对比时间差

                //测试

                TestBeans bmodel = gson.fromJson(request, TestBeans.class);
                callback.onSucceed(what, response, bmodel);

//                if ("0".equals(bmodel.getErrorCode())) {
//                    Object obj = JSON.parseObject(request, modelClass);
//                    callback.onSucceed(what, response, obj);
//                } else if ("20018".equals(bmodel.getErrorCode())) {
//                    Object obj = JSON.parseObject(request, modelClass);
//                    callback.onSucceed(what, response, obj);
//                } else if ("20004".equals(bmodel.getErrorCode())) {
//                    if (HttpResponseListenerJava.onAgainLoginListener != null) {
//                        HttpResponseListenerJava.onAgainLoginListener.onAgainLogin();
//                    }
//                    bmodel.setErrorMessage("");
//                    callback.onFailed(what, response, bmodel);
//                } else if ("20010".equals(bmodel.getErrorCode())) {
//                    if (HttpResponseListenerJava.onAgainLoginListener != null) {
//                        HttpResponseListenerJava.onAgainLoginListener.onAgainLogin();
//                    }
//                    bmodel.setErrorMessage("");
//                    callback.onFailed(what, response, bmodel);
//                } else if ("10010".equals(bmodel.getErrorCode())) {
//                    if (HttpResponseListenerJava.onAgainLoginListener != null) {
//                        HttpResponseListenerJava.onAgainLoginListener.onAgainLogin();
//                    }
//                    bmodel.setErrorMessage("");
//                    callback.onFailed(what, response, bmodel);
//                } else if ("5000".equals(bmodel.getErrorCode())) {
//                    if (HttpResponseListenerJava.onAgainLoginListener != null) {
//                        HttpResponseListenerJava.onAgainLoginListener.onOutLogin(bmodel);
//                    }
//                    bmodel.setErrorMessage("");
//                    callback.onFailed(what, response, bmodel);
//                } else {
//                    callback.onFailed(what, response, bmodel);
//                }

            } catch (Exception e) {
               // L.e("异常：" + e.getMessage());
                if (TextUtils.isEmpty(e.getMessage())) {
                    BaseModelJava baseModel = new BaseModelJava();
                    baseModel.setErrorCode("699");
                    baseModel.setErrorMessage("异常信息为空！");
                    callback.onFailed(what, response, baseModel);
                } else if (e.getMessage().equals("You cannot start a load for a destroyed activity")) {

                } else {
                    BaseModelJava baseModel = new BaseModelJava();
                    baseModel.setErrorCode("698");
                    baseModel.setErrorMessage("数据解析异常！");
                    callback.onFailed(what, response, baseModel);
                }

            }

        }
    }

    @Override
    public void onFailed(int what, Response response) {
        Exception exception = response.getException();
        BaseModelJava baseModel = new BaseModelJava();
        if (exception instanceof NetworkError) {// 网络不好
            baseModel.setErrorMessage("网络连接失败，请稍后重试");
            baseModel.setErrorCode("404");
        } else if (exception instanceof TimeoutError) {// 请求超时
            baseModel.setErrorMessage("请求超时，网络不好或者服务器不稳定。");
            baseModel.setErrorCode("404");
        } else if (exception instanceof UnKnownHostError) {// 找不到服务器
            baseModel.setErrorMessage("未发现指定服务器，清切换网络后重试。");
            baseModel.setErrorCode("404");
        } else if (exception instanceof URLError) {// URL是错的
            baseModel.setErrorMessage("URL错误。");
            baseModel.setErrorCode("404");
        } else if (exception instanceof NotFoundCacheError) {
            // 这个异常只会在仅仅查找缓存时没有找到缓存时返回
            // 没有缓存一般不提示用户，如果需要随你。
        } else {
            baseModel.setErrorMessage("未知错误。");
            baseModel.setErrorCode("304");
        }
    }

    @Override
    public void onFinish(int what) {
        if (mActivity != null) {
            if (isLoading) {
              //DialogUtil.stopDialogLoading(mActivity);
            }
        }
        if (context != null) {
            if (isLoading) {
               //DialogUtil.stopDialogLoading(context);
            }
        }
    }


    /**
     * @param request      请求对象.
     * @param httpCallback 回调对象.
     */
    public HttpResponseListenerJava(Request<T> request, HttpListener<T> httpCallback, Class<?> modelClass) {
        this.mRequest = request;
        this.modelClass = modelClass;
        this.callback = httpCallback;
    }

    /**
     * @param activity     context用来实例化dialog.
     * @param request      请求对象.
     * @param httpCallback 回调对象.
     * @param canCancel    是否允许用户取消请求.
     * @param isLoading    是否显示dialog.
     */
    public HttpResponseListenerJava(Activity activity, Request<T> request, HttpListener<T> httpCallback, boolean
            canCancel, boolean isLoading, Class<?> modelClass) {
        this.mActivity = activity;
        this.mRequest = request;
        this.modelClass = modelClass;
        this.callback = httpCallback;
        this.canCancel = canCancel;
        this.isLoading = isLoading;
    }

    /**
     * @param context      请求对象.
     * @param request      请求对象.
     * @param httpCallback 回调对象.
     */
    public HttpResponseListenerJava(Context context, Request<T> request, HttpListener<T> httpCallback, boolean
            canCancel, boolean isLoading, Class<?> modelClass) {
        this.mRequest = request;
        this.modelClass = modelClass;
        this.callback = httpCallback;
        this.context = context;
        this.canCancel = canCancel;
        this.isLoading = isLoading;
    }


}
