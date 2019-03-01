package util;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import context.MyApplication;
import liyulong.com.fixed.R;

public class SaveImageTask extends AsyncTask<Bitmap, Void, String> {
    private String temp = "1";

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public SaveImageTask(String a){
        temp = a;
    }

    @Override
    protected String doInBackground(Bitmap... bitmaps) {
        String result = "保存出错，请检查权限！";
        try {
            String sdcard = Environment.getExternalStorageDirectory().toString();

            File file = new File(sdcard + "/");
            if (!file.exists()) {
                file.mkdirs();
            }

            File imageFile = new File(file.getAbsolutePath(),temp+".jpg");
            FileOutputStream outStream = null;
            outStream = new FileOutputStream(imageFile);
            Bitmap image = bitmaps[0];
            image.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            result = "打钱码已经成功保存到你的SD卡根目录下！";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }


    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(MyApplication.getContext(),s,Toast.LENGTH_LONG).show();
    }
}
