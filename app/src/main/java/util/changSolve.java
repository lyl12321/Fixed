package util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class changSolve {

    public static boolean changSolve(String id,String solveNum){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("mm","1870458220")
                .add("id",id)
                .add("solveNum",solveNum)
                .build();
        Request request = new Request.Builder()
                .url("http://172.96.252.160/appdata/client/user_cancel_finished.php")
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String returnData = response.body().string();
            JSONObject jsonObject = new JSONObject(returnData);
            if (!jsonObject.getBoolean("returnState")){
                return false;
            }
            if (jsonObject.getBoolean("solve")){
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            return false;
        } catch (JSONException e) {
            return false;
        }

    }


}
