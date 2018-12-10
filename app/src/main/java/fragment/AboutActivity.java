package fragment;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import org.lzh.framework.updatepluginlib.UpdateBuilder;

import context.MyApplication;
import fixed.BaseActivity;
import liyulong.com.fixed.R;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;
import util.getVersion;

public class AboutActivity extends BaseActivity {

    Context activity = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        Element versionElement = new Element();
        versionElement.setTitle("软件版本:"+getVersion.getLocalVersionName(activity));
        versionElement.setIconDrawable(R.drawable.ic_info_circle);
        versionElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateBuilder.create().check();
            }
        });


        Element qqElement = new Element();
        qqElement.setTitle("QQ");
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

        setContentView(aboutPage);


    }
}
