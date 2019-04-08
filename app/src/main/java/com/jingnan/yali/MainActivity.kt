package com.jingnan.yali

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jingnan.jingnanlibrary.base.BaseActivity
import com.jingnan.jingnanlibrary.bean.TestBeans
import com.jingnan.jingnanlibrary.net.netjava.CallServerJava
import com.jingnan.yali.bean.TestBean
import com.yanzhenjie.nohttp.NoHttp
import com.yanzhenjie.nohttp.RequestMethod
import com.yanzhenjie.nohttp.rest.Response
import com.yanzhenjie.nohttp.rest.StringRequest
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initListener()
    }

    private fun initListener() {
        mMainButton.setOnClickListener {
            val mRequestMap = hashMapOf<String,Object>()
            val postUrl = "http://v.juhe.cn/toutiao/index"
            val request = StringRequest(postUrl, RequestMethod.POST)
            request.add("key","29e6b7c4d8953edbdbbb2b1e45b55ba2")
                    .add("type","top")
            request(0,request,false,false, TestBean::class.java,this)
        }
    }

    override fun onSucceedV2(what: Int, modelClass: Any?) {
        super.onSucceedV2(what, modelClass)
        val test = modelClass as TestBeans
        text.text = test.result.data[0].title

    }

}
