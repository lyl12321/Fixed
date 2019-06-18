package welcome;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

import fixed.MainActivity;
import liyulong.com.fixed.R;
import util.SharedPreferencesUtil;

public class IntroActivity extends AppIntro {

    String[] string = new String[]{
//            Manifest.permission.SEND_SMS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
//            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showStatusBar(false);
        showSkipButton(false);


        SliderPage sliderPage1 = new SliderPage();
        sliderPage1.setTitle("你好");
        sliderPage1.setDescription("轻按右下方按钮开始");
        sliderPage1.setImageDrawable(R.drawable.touxiang);
        sliderPage1.setBgColor(getResources().getColor(R.color.introcolor));
        addSlide(AppIntroFragment.newInstance(sliderPage1));



        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle("权限");
        sliderPage2.setDescription("我需要一些权限才能正常工作，在您进行下一步前，我会向您申请一些必要的权限！");
        sliderPage2.setImageDrawable(R.drawable.ic_service);
        sliderPage2.setBgColor(getResources().getColor(R.color.materialcolor1));
        addSlide(AppIntroFragment.newInstance(sliderPage2));

        SliderPage sliderPage3 = new SliderPage();
        sliderPage3.setTitle("使用");
        sliderPage3.setDescription("您只需在主页面填入个人信息，点击提交按钮，即可，如果有更多需求，可以在关于页面单击我的qq联系我！");
        sliderPage3.setImageDrawable(R.drawable.ic_per);
        sliderPage3.setBgColor(getResources().getColor(R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage3));


        setFadeAnimation();
        askForPermissions(string,2);
//        setSwipeLock(false);
    }

    @Override
    public void onDonePressed() {
        super.onDonePressed();
        SharedPreferencesUtil.putBoolean(this,SharedPreferencesUtil.FIRST_OPEN,false);

        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
