package com.example.partycoruna.network;

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
    // Para dispositivos físicos, usa tu IP local:
    // private static final String BASE_URL = "http://192.168.1.X:8000/api/";

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
