package com.jingnan.jingnanlibrary.base

import android.support.v7.app.AppCompatActivity
import com.jingnan.jingnanlibrary.bean.BaseModelJava
import com.jingnan.jingnanlibrary.net.netjava.CallServerJava
import com.jingnan.jingnanlibrary.net.netjava.HttpListener
import com.jingnan.jingnanlibrary.net.netjava.HttpResponseListenerJava
import com.yanzhenjie.nohttp.rest.OnResponseListener
import com.yanzhenjie.nohttp.rest.Request
import com.yanzhenjie.nohttp.rest.Response


open class BaseActivity : AppCompatActivity(),HttpListener<String>{


    private val cancelObject = Any()


    fun <T> request(what: Int, request: Request<T>, canCancel:Boolean, isLoading:Boolean,modelClass:Class<*>,callback:HttpListener<T> ) {
        // 这里设置一个sign给这个请求。
        request.cancelSign = cancelObject

        CallServerJava.getInstance().add(what, request, callback, modelClass)
    }



    override fun onSucceed(what: Int, response: Response<String>?, modelClass: Any?) {
        onSucceedV2(what, modelClass)
    }

    override fun onFailed(what: Int, response: Response<String>?, baseModel: BaseModelJava?) {
        onFailedV2 (what,baseModel)}


    override fun onDestroy() {
        // 在组件销毁的时候调用队列的按照sign取消的方法即可取消。
        val mQueue = CallServerJava.getInstance().getmRequestQueue()
        mQueue.cancelBySign(cancelObject)
        // 因为回调函数持有了activity，所以退出activity时请停止队列。
        mQueue.stop()
        super.onDestroy()
    }


    /***
     *
     * @param what
     * @param modelClass
     */


    open fun onFailedV2(what: Int, modelClass: Any?) {

    }


    open fun onSucceedV2(what: Int, modelClass: Any?) {

    }

    fun onSucceedV2(what: Int, result: String, modelClass: Any) {

    }
}