package util;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Commit {

    public static boolean netCommit(Context context, String name, String phoneNumber, String address, String time, int servicePeopleNumber, String issue, String commitTime) {

        boolean t = false;

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("name", name)
                .add("phoneNumber", phoneNumber)
                .add("address", address)
                .add("time", time)
                .add("servicePeople", String.valueOf(servicePeopleNumber))
                .add("issue", issue)
                .add("commitTime", commitTime)
                .build();
        Request request = new Request.Builder()
                .url("http://lqwqb.ml/appdata/client/commit.php")
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String resultData = response.body().string();
            JSONObject jsonObject = new JSONObject(resultData);
            if (jsonObject.getBoolean("returnState")) {
                SharedPreferences.Editor editor = context.getSharedPreferences("returnid", Context.MODE_PRIVATE).edit();
                editor.putInt("id", jsonObject.getInt("id"));
                editor.apply();
                t = true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return t;

    }
}


