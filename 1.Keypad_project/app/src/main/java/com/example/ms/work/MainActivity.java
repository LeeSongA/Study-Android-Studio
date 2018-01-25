package com.example.ms.work;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

// 1차 코드리뷰

// code convention
// 클래스명의 첫 글자는 대문자
// 접근지정자는 반드시 붙일 것.

// UI
// 비밀번호 입력 시에도 editText 내용이 보임

// 리팩토링
// keypad.java onkey() 중 4번째 줄
// 랜덤 배치 반복 부분

// handler 불필요

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private WebSettings webSettings;
    private String webEdit;
    private BroadcastReceiver broadcastReceiver;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView)findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);

        String userAgent = webView.getSettings().getUserAgentString();
        webView.getSettings().setUserAgentString(userAgent+"HybridApp");

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new AndroidWebBridge(webView), "HybridApp");
        webView.loadUrl("file:///android_asset/index.html");
        webView.setWebViewClient(new WebViewClientClass());

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("test.com.action.TEST");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getStringExtra("result")!=null) {
                    webView.loadUrl("javascript:set_context('"+ webEdit +"', '"+ intent.getStringExtra("result") +"');");
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private class AndroidWebBridge {                                                // 웹뷰로 안드로이드와 자바스크립트 연동
        private WebView webView;

        public AndroidWebBridge(WebView webView) {
            this.webView = webView;
        }

        @JavascriptInterface
        public void setMessage(final String arg) {
            Log.e("HybridApp", arg);
            webEdit = arg;
            Intent intent = new Intent(
                    getApplicationContext(),
                    keypad.class);
            startActivityForResult(intent, 201);
        }
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
}


