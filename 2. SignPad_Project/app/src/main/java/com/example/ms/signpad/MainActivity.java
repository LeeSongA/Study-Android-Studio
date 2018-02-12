package com.example.ms.signpad;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

// 업무 내용
// html canvas 클릭 시 안드로이드에서 SignPad 창 띄우기
// SignPad 창에 서명하기(필압 효과 포함)
// Spen SDK 이용

// 설명
// MainActivity: webview 있는 기본 화면, 자바스크립트와 안드로이드 연동
// SignPad: Spen SDK 이용해서 필압 효과
// libs 폴더에 jsoup-1.8.2.jar  pen-v5.0.0_full.aar  sdk-v1.0.0.jar 있음


public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private WebSettings webSettings;
    private String id;

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
        webView.getSettings().setUserAgentString(userAgent+"SignPad");

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new AndroidBridge(webView), "SignPad");
        webView.loadUrl("file:///android_asset/index.html");            // Android에서 Javascript함수 호출
        webView.setWebViewClient(new WebViewClientClass());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null) {
            String base64_image = data.getStringExtra("data");
            if (requestCode == 201 && resultCode == RESULT_OK) {
                if (data.getStringExtra("result").equals("Save")) {
                    webView.loadUrl("javascript:Save('" + id + "', '" + base64_image + "');");
                }
                 //              if (data.getStringExtra("result").equals("Clear")) {
                 //                   webView.loadUrl("javascript:Clear('" + id + "');");
                 //               }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private class AndroidBridge {                                       // 자바스크립트와 안드로이드 연동
        private WebView webView;

        public AndroidBridge(WebView webView) {
            this.webView = webView;
        }

        @JavascriptInterface
        public void callAndroid(final String arg, final int width, final int height) {
            Log.e("SignPad", arg);
            id = arg;
            Intent intent = new Intent(
                    getApplicationContext(),
                    SignPad.class);
            intent.putExtra("width", width);
            intent.putExtra("height", height);
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
}
