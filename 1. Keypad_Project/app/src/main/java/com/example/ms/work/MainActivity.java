package com.example.ms.work;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

// 1차 코드리뷰

// code convention
// 클래스명의 첫 글자는 대문자
// 접근지정자는 반드시 붙일 것.

// UI
// 비밀번호 입력 시에도 editText 내용이 보임

// 리팩토링
// Keypad.java onkey() 중 4번째 줄
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

        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(layoutParams);

        webView = new WebView(this);
        LinearLayout.LayoutParams webviewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        webView.setLayoutParams(webviewParams);
        linearLayout.addView(webView);

        setContentView(linearLayout);

        webView.getSettings().setJavaScriptEnabled(true);

        String userAgent = webView.getSettings().getUserAgentString();
        webView.getSettings().setUserAgentString(userAgent+"HybridApp");

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        WebView.setWebContentsDebuggingEnabled(true);
        webView.addJavascriptInterface(new AndroidWebBridge(webView), "HybridApp");
        webView.loadUrl("file:///android_asset/index.html");
        webView.setWebViewClient(new WebViewClientClass());

        IntentFilter intentFilter = new IntentFilter();                                 // 브로드캐스트의 액션을 등록하기 위한 인텐트 필터
        intentFilter.addAction("test.com.action.TEST");

        broadcastReceiver = new BroadcastReceiver() {                                // 동적 리시버 구현
            @Override
            public void onReceive(Context context, Intent intent) {

                if(intent != null) {
                    int result = intent.getIntExtra("result", -1);

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("javascript:");
                    if(result == 60 || result == 100)
                        stringBuilder.append(result == 60 ? "delete" : "clear");
                    else
                        stringBuilder.append("set");
                    stringBuilder.append("_context('");
                    stringBuilder.append(webEdit);
                    if(result != 60) {
                        stringBuilder.append("', '");
                        stringBuilder.append(result);
                    }
                    stringBuilder.append("');");

                    webView.loadUrl(stringBuilder.toString());

//                    if(result == 60)
//                        webView.loadUrl("javascript:   delete   _context('"+ webEdit +"');");
//                    else if(result == 100)
//                        webView.loadUrl("javascript:   clear    _context('"+ webEdit +"');");
//                    else
//                        webView.loadUrl("javascript:   set      _context('"+ webEdit +"', '"+ intent.getIntExtra("result", -1) +"');");

                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);                           // 리시버 등록
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
                    Keypad.class);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
}



