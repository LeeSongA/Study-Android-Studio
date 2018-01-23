package com.example.ms.signpad;

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

// 설명
// MainActivity: webview 있는 기본 화면, 자바스크립트와 안드로이드 연동해 줌
// SignPad: CustomSignPad 부분과 버튼 3개 있음
// CustomSignPad: 서명 작성

public class MainActivity extends AppCompatActivity {

    private final Handler handler = new Handler();
    WebView webView;
    WebSettings webSettings;
    String id;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        alertSetting(webView);

        String userAgent = webView.getSettings().getUserAgentString();
        webView.getSettings().setUserAgentString(userAgent+"SignPad");

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new AndroidBridge(webView), "SignPad");
        webView.loadUrl("file:///android_asset/index.html");            // Android에서 Javascript함수 호출
        webView.setWebViewClient(new WebViewClientClass());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(data.getStringExtra("result").equals("Save")){
                webView.loadUrl("javascript:Save(\""+id+"\");");
            }
            if(data.getStringExtra("result").equals("Clear")){
                webView.loadUrl("javascript:Clear(\""+id+"\");");
            }
        }
    }

    public void alertSetting(WebView webView) {                             // alert 창
        final Context myApp = this;
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView webView, String url, String message, final android.webkit.JsResult result) {
                new AlertDialog.Builder(myApp)
                        .setTitle(("SignPad"))
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
                    Log.e("SignPad", arg);
                    id = arg;
                    Intent intent = new Intent(
                            getApplicationContext(),
                            SignPad.class);
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
