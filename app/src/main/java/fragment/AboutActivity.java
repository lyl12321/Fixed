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


import fixed.MainActivity;
import liyulong.com.fixed.R;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;
import util.getVersion;

public class AboutActivity extends Fragment {

    private MainActivity activity;
    private int clickCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_about,container,false);
        activity = (MainActivity) getActivity();

        clickCount = 0;

        Element versionElement = new Element();
        versionElement.setTitle("软件版本:"+getVersion.getLocalVersionName(activity));
        versionElement.setIconDrawable(R.drawable.ic_info_circle);
        versionElement.setOnClickListener(new View.OnClickListener() {
            String string = "";
            @Override
            public void onClick(View v) {
                clickCount += 1;
                switch (clickCount){
                    case 1:
                        string = "这里没有东西，不要点了";
                        break;
                    case 2:
                        string = "没有东西，真的不要点了";
                        break;
                    case 3:
                        string = "你咋还点呢";
                        break;
                    case 4:
                        string = "woc你是真的无聊呀";
                        break;
                    case 5:
                        string = "我受不了你了";
                        break;
                    case 6:
                        string = "再点不理你了";
                        break;
                    case 7:
                        string = "真不理你了，别点了";
                        break;
                    case 8:
                        string = "真没想到你会这么坚持";
                        break;
                    case 9:
                        string = "牛批";
                        break;
                    default:
                        string = "牛批";
                        break;
                }
                if (!string.isEmpty()) {
                    Toast.makeText(activity, string, Toast.LENGTH_SHORT).show();
                }
                if (clickCount == 50){
                    Toast.makeText(activity,"兄贵你是真滴无聊真滴牛批",Toast.LENGTH_SHORT).show();
                    clickCount = 0;
                }
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




        View aboutPage = new AboutPage(activity)
                .isRTL(false)
                .setDescription("这是一个简单的app，没有过多复杂的东西，也没有设计感。(逃~~~)")
                .setImage(R.mipmap.ic_launcher)
                .addGroup("联系我")
                .addItem(qqElement)
                .addEmail("look.liyulong@qq.com","邮箱")
                .addWebsite("https://www.llyyll123.xyz","博客")
                .addTwitter("medyo80","推特")
                .addGitHub("lyl12321","github首页")
                .addInstagram("look.liyulong","Instagram")
                .addGroup("软件信息")
                .addItem(versionElement)

                .create();


        return aboutPage;
    }

//    @Override
//    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        return MoveAnimation.create(MoveAnimation.LEFT,enter,500);
//
//    }
}
