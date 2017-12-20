package sample;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public class TestBaseUrl {
  public static void main(String[] args) throws IOException {
    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://haowanba.com")
        .addConverterFactory(new StringConverter())
        .build();
    TestUrl testUrl = retrofit.create(TestUrl.class);
    testUrl.home().enqueue(new Callback<String>() {
      public void onResponse(Call<String> call, Response<String> response) {
        if (response.isSuccessful()) {
          System.out.println(response.body());
        } else {
          System.out.println(response.errorBody());
        }
      }

      public void onFailure(Call<String> call, Throwable throwable) {
        System.err.print(throwable.getMessage());
      }
    });

    System.out.println(testUrl.baidu("https://www.baidu.com").execute().body());
  }

  interface TestUrl {
    @GET("/") Call<String> home();

    @GET()
    Call<String> baidu(@Url String otherUrl);
  }

  public static class StringConverter extends Converter.Factory {
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
        Retrofit retrofit) {
      if (type == String.class) {
        return new Converter<ResponseBody, Object>() {
          public Object convert(ResponseBody responseBody) throws IOException {
            return responseBody.string();
          }
        };
      }
      return super.responseBodyConverter(type, annotations, retrofit);
    }
  }
}
