package com.example.partycoruna.network;

import android.content.Context;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/* ApiClient
 - Provee una instancia singleton de Retrofit configurada con la `BASE_URL` y Gson.
 - Uso rápido:
     ApiService api = ApiClient.getClient().create(ApiService.class);
 */
public class ApiClient {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://10.0.2.2:8000/api/";
    private static Context context;

    public static void init(Context ctx) {
        context = ctx.getApplicationContext();
        retrofit = null; // Forzar reconstrucción con el nuevo contexto/interceptor
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            
            okhttp3.OkHttpClient.Builder clientBuilder = new okhttp3.OkHttpClient.Builder();
            
            if (context != null) {
                clientBuilder.addInterceptor(new AuthInterceptor(context));
            }

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(clientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
