package com.zuzuche.demo.fastdev;

import android.arch.lifecycle.MediatorLiveData;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zuzuche.demo.fastdev.component.network.CoolAPi;
import com.zzc.baselib.arch.ConsumerLiveData;
import com.zzc.baselib.util.L;
import com.zzc.easycomponents.component.Components;
import com.zzc.easycomponents.util.Utils;
import com.zzc.network.support.GlobalRequestAdapter;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L.d("======="+Utils.resource().toString());
        L.d("=======" + Utils.app().packageName() + " " + Utils.app().version() + " " + Utils.device().device());
        Components.network().builder(CoolAPi.class)
                .baseUrl("https://suggest.taobao.com/")
                .requestAdapter(new GlobalRequestAdapter() {

                    @Override
                    public void addHeader(Request.Builder builder) {

                    }

                    @Override
                    public void addQueryParams(HttpUrl.Builder httpUrlBuilder) {

                    }
                }).build();

        Components.network().api(CoolAPi.class).testSearchNetwork("城").subscribe();


        data.postValue("222");
        data1.postValue("333");

        data.observe(this, data -> {
            L.d("======data " + data);
        });
        data1.observe(this, data -> {
            L.d("======data1 " + data);
        });

//
//        ViewModel viewModel = ViewModelProviders.of(this).get(TestViewModel.class);
    }

    MediatorLiveData<String> data = new MediatorLiveData<>();
    ConsumerLiveData<String> data1 = new ConsumerLiveData<>();

    public void test1(View view) {
        startActivity(new Intent(this, Test1Activity.class));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
