package com.example.myapplication;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by hero on 2017/5/20.
 */

public class MyConverterFactory extends Converter.Factory {

    private final Gson gson;

    public static MyConverterFactory create() {
        return create(new Gson());
    }

    public static MyConverterFactory create(Gson gson) {
        if(gson == null) {
            throw new NullPointerException("gson == null");
        } else {
            return new MyConverterFactory(gson);
        }
    }

    private MyConverterFactory(Gson gson) {
        this.gson = gson;
    }

    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = this.gson.getAdapter(TypeToken.get(type));
        return new MyConverter(this.gson, adapter);
    }

    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = this.gson.getAdapter(TypeToken.get(type));
        return new MyConverter(this.gson, adapter);
    }

    public static class MyConverter<T> implements Converter<ResponseBody, T> {

        private final Gson gson;
        private final TypeAdapter<T> adapter;
        private final TypeAdapter<JSONObject> jsonAdapter;
        MyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
            jsonAdapter = gson.getAdapter(JSONObject.class);
        }

        public T convert(ResponseBody value) throws IOException {


            Object var3 = null;
            try {
                String str = value.string();
                Log.i("MyConverter", "value:"+ str);

                JSONObject obj = new JSONObject(str);
                if (obj != null && obj.optBoolean("status")) {
                    JSONArray array = obj.optJSONArray("tngou");
                    StringReader reader = new StringReader(array.toString());
                    JsonReader jsonReader = this.gson.newJsonReader(reader);
                    var3 = this.adapter.read(jsonReader);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                value.close();
            }

            return (T) var3;
        }
    }
}
