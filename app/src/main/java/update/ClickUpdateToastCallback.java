package update;

import android.widget.Toast;

import org.lzh.framework.updatepluginlib.base.CheckCallback;
import org.lzh.framework.updatepluginlib.model.Update;

import context.MyApplication;

public class ClickUpdateToastCallback implements CheckCallback {
    @Override
    public void onCheckStart() {
        Toast.makeText(MyApplication.getContext(),"检测更新中...",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hasUpdate(Update update) {
        Toast.makeText(MyApplication.getContext(),"有的版本可升级",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void noUpdate() {
        Toast.makeText(MyApplication.getContext(),"无更新",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCheckError(Throwable t) {
        Toast.makeText(MyApplication.getContext(),"检测更新失败",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserCancel() {

    }

    @Override
    public void onCheckIgnore(Update update) {

    }
}
