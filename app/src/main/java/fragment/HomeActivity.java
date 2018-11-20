package fragment;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.IOException;

import javax.security.auth.callback.Callback;

import context.MyApplication;
import fixed.MainActivity;
import liyulong.com.fixed.R;
import okhttp3.Call;

import okhttp3.Response;
import util.HttpUtil;

public class HomeActivity extends Fragment {
    View view;
    MainActivity mainActivityH;
    ImageView bingPicImg;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_home, container, false);
        mainActivityH = (MainActivity) getActivity();
        bingPicImg = view.findViewById(R.id.bing_pic_img);


        return view;
    }




//    @Override
//    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        return MoveAnimation.create(MoveAnimation.DOWN, enter, 500);
//    }







}