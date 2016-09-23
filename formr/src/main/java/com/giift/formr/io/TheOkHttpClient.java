package com.giift.formr.io;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/**
 * Created by vieony on 18/8/2015.
 * Singleton class for OkHttpClient
 */
public class TheOkHttpClient {

  public static final MediaType MEDIA_TYPE_MARKDOWN
      = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
  private static OkHttpClient instance_ = null;

  private TheOkHttpClient() {
  }

  @NonNull
  public static synchronized OkHttpClient GetInstance() {
    if (instance_ == null) {
      instance_ = new OkHttpClient.Builder()
          .cookieJar(CookieJar.NO_COOKIES)
          .connectTimeout(30, TimeUnit.SECONDS)
          .readTimeout(30, TimeUnit.SECONDS)
          .build();
    }
    return instance_;
  }

  @NonNull
  public static synchronized OkHttpClient SetAuthenticator(@NonNull Authenticator auth) {
    instance_ = GetInstance().newBuilder().authenticator(auth).build();
    return instance_;
  }

  @NonNull
  public static synchronized OkHttpClient AddInterceptor(@NonNull Interceptor interceptor) {
    instance_ = GetInstance().newBuilder().addInterceptor(interceptor).build();
    return instance_;
  }

  public static void SetInstance(@NonNull OkHttpClient okHttpClient){
    instance_ = okHttpClient;
  }
}
