package fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.lzh.framework.updatepluginlib.UpdateBuilder;

import fixed.MainActivity;
import liyulong.com.fixed.R;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;
import update.ClickUpdateToastCallback;

import util.getVersion;

public class AboutActivity extends Fragment {

    MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        Element versionElement = new Element();
        versionElement.setTitle("软件版本 "+getVersion.getLocalVersionName(activity));
        versionElement.setIconDrawable(R.drawable.ic_info_circle);
        versionElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateBuilder.create().setCheckCallback(new ClickUpdateToastCallback()).check();
            }
        });


        Element qqElement = new Element();
        qqElement.setTitle("QQ (刷课)");
        qqElement.setIconDrawable(R.drawable.ic_qq);
        qqElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "mqqwpa://im/chat?chat_type=wpa&uin=1870458220";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
                } catch (Exception e){
                    Toast.makeText(activity,"您还没有安装QQ哦",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Element thankPeople1 = new Element();
        thankPeople1.setTitle("@神");
        Element thankPeople2 = new Element();
        thankPeople2.setTitle("@明夕");








        View aboutPage = new AboutPage(activity)
                .isRTL(false)
                .setDescription("这是一个简单的app，没有过多复杂的东西，也没有设计感。(逃~~~)")
                .setImage(R.mipmap.ic_launcher)
                .addGroup("联系我")
                .addItem(qqElement)
                .addEmail("look.liyulong@qq.com","邮箱")
                .addWebsite("https://www.llyyll123.xyz","博客")
                .addTwitter("lylong_123","推特")
//                .addGitHub("lyl12321","github首页")
                .addInstagram("look.liyulong","Instagram")
                .addGroup("致谢")
                .addItem(thankPeople1)
                .addItem(thankPeople2)

                .addGroup("软件信息")
                .addItem(versionElement)

                .create();
        return aboutPage;
    }
}
