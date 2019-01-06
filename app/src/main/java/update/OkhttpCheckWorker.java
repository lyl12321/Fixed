package update;

import org.lzh.framework.updatepluginlib.base.CheckWorker;
import org.lzh.framework.updatepluginlib.impl.DefaultCheckWorker;
import org.lzh.framework.updatepluginlib.model.CheckEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 定制一个简单的使用okHttp做更新接口检查的网络任务。
 */
public class OkhttpCheckWorker extends CheckWorker {

    private static OkHttpClient sOkClient;
    @Override
    protected String check(CheckEntity entity) throws Exception {
        // 自定网络任务。在此通过同步请求的方式调用即可
        Request.Builder builder = new Request.Builder().url(entity.getUrl());
        if ("GET".equalsIgnoreCase(entity.getMethod())) {
            builder.method("GET",null);
        } else {
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            Map<String, String> params = entity.getParams();
            if (params == null) {
                params = new HashMap<>();
            }
            Set<String> keys = params.keySet();
            for (String key : keys) {
                bodyBuilder.add(key,params.get(key));
            }
            builder.method("POST",bodyBuilder.build());
        }
        Request request = builder.build();
        Call call = sOkClient.newCall(request);
        return call.execute().body().string();
    }

    static {
        sOkClient = new OkHttpClient();
    }
}