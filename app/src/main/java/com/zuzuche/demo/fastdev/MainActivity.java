package com.zuzuche.demo.fastdev;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zuzuche.demo.fastdev.network.CoolAPi;
import com.zzc.easycomponents.EasyComponents;
import com.zzc.easycomponents.Easys;
import com.zzc.network.support.GlobalRequestAdapter;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Easys.network()
                .builder(CoolAPi.class)
                .baseUrl("https://suggest.taobao.com/")
                .setGlobalRequestAdapter(new GlobalRequestAdapter(){

                    @Override
                    public void addHeader(Request.Builder builder) {

                    }

                    @Override
                    public void addQueryParams(HttpUrl.Builder httpUrlBuilder) {

                    }

                    @Override
                    public void addPostParams(FormBody.Builder builder) {

                    }
                }).build();

        Easys.network().api(CoolAPi.class).testSearchNetwork("市").subscribe();
    }
}
