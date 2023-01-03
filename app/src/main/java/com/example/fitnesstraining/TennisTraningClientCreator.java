package com.example.fitnesstraining;

import android.content.Intent;
import android.net.Uri;
import android.webkit.ValueCallback;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.FragmentActivity;

public class TennisTraningClientCreator {
    private String url;
    private FitnesTrainingWebClient.Callback callback;

    public TennisTraningClientCreator(String url, FitnesTrainingWebClient.Callback callback){
        this.url = url;
        this.callback = callback;
    }

    public FitnesTrainingWebClient createWebClient(){
        return  new FitnesTrainingWebClient(url, callback);
    }

    public TennisTrainingChromeClient createChromeClient(FragmentActivity context, ValueCallback<Uri[]> valueCallback, ActivityResultLauncher<Intent> register){
        return new TennisTrainingChromeClient(context, valueCallback, register);
    }


}