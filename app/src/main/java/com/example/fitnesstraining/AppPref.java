package com.example.fitnesstraining;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.FutureTask;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AppPref {
    private static SharedPreferences mPreference;
    private static final String APP_PREFERENCE = "shared_pref";
    private static final String URL_KEY = "key_pref";

    public static void initPref(Context context) {
        mPreference = context.getSharedPreferences(APP_PREFERENCE, Context.MODE_PRIVATE);
    }

    public static void saveUrl(String url) {
        mPreference.edit()
                .putString(URL_KEY, url)
                .apply();
    }

    static Observable<String> getUrl() {
        Observable<String> observableFitnessTraining;
        FutureTask<String> futureTask = new FutureTask<>(() -> mPreference.getString(URL_KEY, ""));
        observableFitnessTraining = Observable.fromFuture(futureTask)
                .doOnSubscribe(disposable -> futureTask.run());
        return observableFitnessTraining.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

//    public static String getUrl() {
//        return mPreference.getString(URL_KEY, "");
//    }
}

