package com.example.ms.work;

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


public class MainActivity extends AppCompatActivity {

    private final Handler handler = new Handler();
    WebView webView;

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

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new AndroidWebBridge(webView), "HybridApp");
        webView.loadUrl("file:///android_asset/index.html");
        webView.setWebViewClient(new WebViewClientClass());
    }

    public void alertSetting(WebView webView) {
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
    private class AndroidWebBridge {
        private WebView webView;

        public AndroidWebBridge(WebView webView) {
            this.webView = webView;
        }

        @JavascriptInterface
        public void setMessage(final String arg) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.e("HybridApp", arg);
                        Intent intent = new Intent(
                                getApplicationContext(),
                                keypad.class);
                        startActivity(intent);
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



