package com.example.happydog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        WebView wv_address = findViewById(R.id.wv_address);
        wv_address.getSettings().setJavaScriptEnabled(true);
        wv_address.addJavascriptInterface(new BridgeInterface(), "Android");

        wv_address.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) { //페이지 로딩이 끝났을때 javascript쪽으로 역으로 함수 콜이 가능.
                //  안드로이드 -> 자바스크립트 함수 호출! 오버라이드 자체가 콜백 느낌.
                wv_address.loadUrl("javascript:sample2_execDaumPostcode();");

            }
        });
        //최초 웹뷰 로드
        wv_address.loadUrl("https://searchaddress-c6c21.web.app");

    }
        private class BridgeInterface {
            @JavascriptInterface
            public void processDATA(String data) {
                //주소 검색 api의 결과값이 브릿지 통로를 통하여 전달받는다. (from javascript)로부터 온거
                Intent intent = new Intent();
                intent.putExtra("data",data);
                setResult(RESULT_OK,intent);
                finish();

            }

        }
}