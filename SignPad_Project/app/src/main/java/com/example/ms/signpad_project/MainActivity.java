package com.example.ms.signpad_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

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

    private class AndroidBridge {                                       // 자바스크립트와 안드로이드 연동
        private WebView webView;

        public AndroidBridge(WebView webView) {
            this.webView = webView;
        }

        @JavascriptInterface
        public void callAndroid(final String arg) {
            Log.e("SignPad", arg);
            id = arg;
            Intent intent = new Intent(
                    getApplicationContext(),
                    SignPad.class);
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
