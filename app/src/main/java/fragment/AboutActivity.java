package fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;


import fixed.MainActivity;
import liyulong.com.fixed.R;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends Fragment {

    private MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_about,container,false);
        activity = (MainActivity) getActivity();

        Element versionElement = new Element();
        versionElement.setTitle("APP版本:1.0");
        versionElement.setIconDrawable(R.drawable.ic_info_circle);




        View aboutPage = new AboutPage(activity)
                .isRTL(false)
                .setDescription("这是一个简单的app，没有过多复杂的东西，也没有设计感。(逃~~~)")
                .setImage(R.mipmap.ic_launcher)
                .addGroup("联系我")
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
