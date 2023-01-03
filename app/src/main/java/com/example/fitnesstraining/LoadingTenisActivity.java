package com.example.fitnesstraining;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class LoadingTenisActivity extends AppCompatActivity {

    private ValueCallback<Uri[]> valueCallback = null;
    private ActivityResultLauncher<Intent> register = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() != AppCompatActivity.RESULT_CANCELED) {
                    valueCallback.
                    onReceiveValue(new Uri[]{Uri.parse(result.getData() != null ? result.getData().getDataString() : null)});
                } else {
                    valueCallback.onReceiveValue(null);
                }

                valueCallback = null;
            });

    private static final String FIREBASE_URL_TAG = "url_reddirect";
    WebView webViewFitnessTraining;
    ProgressBar progressBarWorkoutNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_tenis);
        webViewFitnessTraining = findViewById(R.id.webView);
        progressBarWorkoutNote = findViewById(R.id.progressBarWorkoutNote);
        progressBarWorkoutNote.setMax(100);

        //Получаем доступ к локальному хранилищу
        AppPref.initPref(this);

        //Запускаем логику получения и проверки ссылки
        AppPref.getUrl().subscribe(this::checkFirebaseUrl);
        if (savedInstanceState == null) {
//            initWebViewSettings();
        }
    }

    private void checkFirebaseUrl(String savedUrl) {
        //Если ссылка пустая, то обращаемя к FirebaseRemote
        if (savedUrl.isEmpty()) {
            //Настраиваем конфиг FirebaseRemote
            FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(60)
                    .build();
            remoteConfig.setConfigSettingsAsync(configSettings);
            //Ставим слушатель на получение ссылки из FirebaseRemote
            remoteConfig.fetchAndActivate().addOnSuccessListener(aBoolean -> {
                //Здесь мы получили ссылку по уникальному тегу
                //Тег должен быть одинаковым в проекте и в Firebase
                String webUrl = remoteConfig.getString(FIREBASE_URL_TAG);
                //Проверяем если значение не пустое, то есть пришла реальная ссылка
                if (!webUrl.isEmpty()) {
                    //Сохраняем ссылку в локальное хранилище
                    AppPref.saveUrl(webUrl);
                    //Запускаем WebView
                    setWebViewFitnessTraining(webUrl);
                }
            });
        } else {
            //Срабатывает если ссылка уже сохранена локально,
            //чтобы не делать повторные запросы в FirebaseRemote

            //Запускаем WebView
            setWebViewFitnessTraining(savedUrl);
        }
    }

    private void setWebViewFitnessTraining(String loadingUrl) {
        //Делаем проверку
        // 1) Вставлена ли сим-карта
        // 2) Реальное ли устройство (сравнивает бренд телефона, если "google" - false
        // 3) Проверяет соединение с интернетом
        if (isSIMInserted() && isRealDevice() && isNetworkAvailable(getApplication())) {
            //Настраиваем WebView
            webViewFitnessTraining = findViewById(R.id.webView);
            initWebViewSettings(loadingUrl);
            webViewFitnessTraining.loadUrl(loadingUrl);
        } else {
            reddirectFitnessTraining();
        }
    }

    public void reddirectFitnessTraining() {
        progressBarWorkoutNote.setVisibility(View.GONE);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void initWebViewSettings(String url) {
        CookieManager.getInstance().setAcceptThirdPartyCookies(webViewFitnessTraining, true);

        TennisTraningClientCreator creator = new TennisTraningClientCreator(url, this::reddirectFitnessTraining);
        webViewFitnessTraining.setWebViewClient(creator.createWebClient());

        webViewFitnessTraining.setWebChromeClient(creator.createChromeClient(this, valueCallback, register));

        WebSettings mWebSettings = webViewFitnessTraining.getSettings();
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webViewFitnessTraining.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        webViewFitnessTraining.restoreState(savedInstanceState);
    }

    private boolean isSIMInserted() {
        return TelephonyManager.SIM_STATE_ABSENT != ((TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE)).getSimState();
    }

    private boolean isRealDevice() {
        return !Build.BRAND.equalsIgnoreCase("google");
    }

    private Boolean isNetworkAvailable(Application application) {
        ConnectivityManager connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network nw = connectivityManager.getActiveNetwork();
            if (nw == null) return false;
            NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
            return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
        } else {
            NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
            return nwInfo != null && nwInfo.isConnected();
        }
    }

    @Override
    public void onBackPressed() {
        if (webViewFitnessTraining.canGoBack()) {
            webViewFitnessTraining.goBack();
        } else {
            super.onBackPressed();
        }
    }
}