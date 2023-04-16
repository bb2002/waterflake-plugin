package co.waterflake.libs;

import co.waterflake.types.ClientInfo;
import okhttp3.*;

import java.io.IOException;
import java.util.Objects;

public class WaterflakeHttp {
    public static Request post(String url, String body, ClientInfo authorization) {
        return new Request.Builder()
                .url(url)
                .method("POST", RequestBody.create(body, MediaType.parse("application/json; charset=utf-8")))
                .header("Authorization", "Bearer " + authorization.getClientSecret())
                .build();
    }

    public static Request get(String url, ClientInfo authorization) {
        return new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + authorization.getClientSecret())
                .build();
    }

    public static Response execute(Request request) throws IOException {
        OkHttpClient client = new OkHttpClient();
        return client.newCall(request).execute();
    }
}
