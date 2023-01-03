package com.example.fitnesstraining;

import android.net.Uri;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Objects;

public class FitnesTrainingWebClient extends WebViewClient {
    private String uri;
    public interface Callback{
        void start();
    }
    private Callback callback;

    public FitnesTrainingWebClient(String uri, Callback callback){
        this.uri = uri;
        this.callback = callback;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        CookieManager.getInstance().setAcceptThirdPartyCookies(view, true);
        String hostUrl = Uri.parse(url.toString()).getHost();
        String hostLoadingUrl = Uri.parse(uri.toString()).getHost();

        String queryUrl = Uri.parse(url.toString()).getQuery();
        String queryLoadingUrl = Uri.parse(uri.toString()).getQuery();
        if (Objects.equals(hostLoadingUrl, "") || Objects.equals(hostUrl, ""))
            return;
        if (Objects.equals(hostLoadingUrl, hostUrl) &&
                Objects.equals(queryUrl, queryLoadingUrl) )
            if (callback!= null)
                callback.start();
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
        CookieManager.getInstance().setAcceptThirdPartyCookies(view, true);
    }
}
