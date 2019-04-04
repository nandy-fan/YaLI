package com.jingnan.jingnanlibrary.net.netjava;

import com.jingnan.jingnanlibrary.bean.BaseModelJava;
import com.yanzhenjie.nohttp.rest.Response;

/**
 * 接受回调结果
 */
public interface HttpListener<T> {

    void onSucceed(int what, Response<T> response, Object modelClass);

    void onFailed(int what, Response<T> response, BaseModelJava baseModel);

}
