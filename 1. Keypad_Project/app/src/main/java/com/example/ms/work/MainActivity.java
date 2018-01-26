package com.example.ms.work;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
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
// Keypad.java onkey() 중 4번째 줄
// 랜덤 배치 반복 부분

// handler 불필요?


public class MainActivity extends AppCompatActivity {

    WebView webView;
    WebSettings webSettings;
    private String webEdit;
    private BroadcastReceiver broadcastReceiver;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView)findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        alertSetting(webView);

        String userAgent = webView.getSettings().getUserAgentString();
        webView.getSettings().setUserAgentString(userAgent+"HybridApp");

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new AndroidWebBridge(webView), "HybridApp");
        webView.loadUrl("file:///android_asset/index.html");
        webView.setWebViewClient(new WebViewClientClass());

        IntentFilter intentFilter = new IntentFilter();                                 // 브로드캐스트의 액션을 등록하기 위한 인텐트 필터
        intentFilter.addAction("test.com.action.TEST");

        broadcastReceiver = new BroadcastReceiver() {                                // 동적 리시버 구현
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getStringExtra("result")!=null) {
                    webView.loadUrl("javascript:set_context('"+ webEdit +"', '"+ intent.getIntExtra("result", -1) +"');");
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);                           // 리시버 등록
    }

    public void alertSetting(WebView webView) {                                       // alert 창 메소드
        final Context myApp = this;
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView webView1, String url, String message, final android.webkit.JsResult result) {
                new AlertDialog.Builder(myApp)
                        .setTitle(("Keypad"))
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        result.confirm();
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            };
        });
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



