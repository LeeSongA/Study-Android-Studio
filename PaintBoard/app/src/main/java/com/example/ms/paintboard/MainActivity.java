package com.example.ms.paintboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


// 업무 내용
// html canvas 클릭 시 안드로이드에서 Screen 창 띄우기
// Screen 창에 서명하기(필압 처리)

// 각 역할
// MainActivity: webview 있는 기본 화면, 자바스크립트와 안드로이드 연동해 줌
// Screen: PaintBoard 부분과 버튼 3개 있음
// PaintBoard: 마우스로 그림 그리는 효과 처리

// 문제점
// html canvas 클릭 시 안드로이드에서 Screen 창이 보여지지 않음
// PaintBoard 를 Screen에 연결?하는 것을 잘못한 거 같음

public class MainActivity extends AppCompatActivity {

    private final Handler handler = new Handler();
    WebView webView;
    WebSettings webSettings;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        alertSetting(webView);

        String userAgent = webView.getSettings().getUserAgentString();
        webView.getSettings().setUserAgentString(userAgent+"SignScreen");

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new AndroidBridge(webView), "SignScreen");
        webView.loadUrl("file:///android_asset/index.html");        // Android에서 Javascript함수 호출
        webView.setWebViewClient(new WebViewClientClass());
    }

    public void alertSetting(WebView webView) {                         // alert 창
        final Context myApp = this;
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView webView, String url, String message, final android.webkit.JsResult result) {
                new AlertDialog.Builder(myApp)
                        .setTitle(("SignScreen"))
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            }
        });
    }

    private class AndroidBridge {                                       // 자바스크립트와 안드로이드 연동
        private WebView webView;

        public AndroidBridge(WebView webView) {
            this.webView = webView;
        }

        @JavascriptInterface
        public void callAndroid(final String arg) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.e("SignScreen", arg);
                    Intent intent = new Intent(
                            getApplicationContext(),
                            Screen.class);
                    startActivityForResult(intent, 201);
                }
            });
        }
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}



