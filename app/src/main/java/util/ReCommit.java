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

public class ReCommit {

    public static boolean netReCommit(Context context,int id){

        boolean t = false;

        if (id == 0){
            return false;
        }

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("id", id+"")
                .build();
        Request request = new Request.Builder()
                .url("http://lqwqb.ml/appdata/client/recommit.php")
                .post(requestBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String returnData = response.body().string();
            JSONObject jsonObject = new JSONObject(returnData);
            if (jsonObject.getBoolean("returnState") && jsonObject.getBoolean("solve")){
                SharedPreferences.Editor editor = context.getSharedPreferences("returnid", Context.MODE_PRIVATE).edit();
                editor.putInt("id", 0);
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
