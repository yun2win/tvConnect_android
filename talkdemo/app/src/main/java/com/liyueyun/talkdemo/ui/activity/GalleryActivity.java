package com.liyueyun.talkdemo.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.liyueyun.talkdemo.R;
import com.liyueyun.talklib.common.TvManage;
import com.liyueyun.talklib.model.TalkTvEntity;
import com.liyueyun.talkdemo.ui.adapter.ImageFragementPagerAdapter;
import com.liyueyun.talkdemo.ui.fragment.MoreImageBrowseFragment;
import com.liyueyun.talkdemo.ui.weight.PhotoViewPager;
import com.yun2win.talksdk.Back;
import com.yun2win.talksdk.TalkManage;
import com.yun2win.utils.IMStringUtil;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends FragmentActivity {

    private PhotoViewPager vp_pager;
    private ImageButton right_connect_tv;
    private Activity activity;
    private int currentPage = 0;//当前显示页面
    private TalkTvEntity current_TalkTv;//当前连接上的电视
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    ImageFragementPagerAdapter imageFragementPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gallery);
        activity = this;
        initUid();
        initDate();
    }

    private void initUid(){
        vp_pager = (PhotoViewPager) findViewById(R.id.vp_image_viewPager);
        right_connect_tv = (ImageButton) findViewById(R.id.right_connect_tv);
        right_connect_tv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                TvManage.getInstance().dialog_Select_Tv(GalleryActivity.this,new Back.Result<TalkTvEntity>(){
                    @Override
                    public void onSuccess(TalkTvEntity talkTvEntity) {
                        if(TvManage.getInstance().getCurrent_TalkTv()!=null) {
                            String message = TalkManage.getInstance().galleryMsg("image", imgUrls[currentPage], imgUrls[currentPage]);
                            TalkManage.getInstance().pushMessageTv(message,TvManage.getInstance().getCurrent_TalkTv().getTv_userId(), new Back.Callback() {
                                @Override
                                public void onSuccess() {
                                }

                                @Override
                                public void onError(int code, String error) {
                                    Toast.makeText(GalleryActivity.this,error,Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                    @Override
                    public void onError(int i, String s) {

                    }
                    @Override
                    public void OnEvent(int i, String s) {

                    }
                });
            }
        });
    }
    String imgUrls[] ={
            "http://pic.58pic.com/58pic/15/61/76/99S58PICtjg_1024.jpg",
            "http://pic11.nipic.com/20101110/3320946_144743441000_2.jpg",
            "http://pic16.nipic.com/20110828/3820950_095400303118_2.jpg",
            "http://img3.imgtn.bdimg.com/it/u=1842161916,2287034829&fm=26&gp=0.jpg",
            "http://img.taopic.com/uploads/allimg/110308/8110-11030QI30966.jpg",
            "http://img.pconline.com.cn/images/upload/upc/tx/photoblog/1501/16/c0/1935650_1935650_1421374013796.jpg"};
    private void initDate(){

        for(String url:imgUrls){
            Fragment firstFragment = MoreImageBrowseFragment.newInstance(this, this,url);
            fragmentList.add(firstFragment);
        }
        imageFragementPagerAdapter = new ImageFragementPagerAdapter(this.getSupportFragmentManager(), (ArrayList<Fragment>) fragmentList);
        vp_pager.setAdapter(imageFragementPagerAdapter);
        vp_pager.setCurrentItem(0);
        if(fragmentList.size()>3) {
            vp_pager.setOffscreenPageLimit(3);
        }
        currentPage = 0;
        vp_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {

                currentPage = position;

                //sendMessageTv(position);
                if(TvManage.getInstance().getCurrent_TalkTv()!=null) {
                    String message = TalkManage.getInstance().galleryMsg("image", imgUrls[currentPage], imgUrls[currentPage]);
                    TalkManage.getInstance().pushMessageTv(message,TvManage.getInstance().getCurrent_TalkTv().getTv_userId(), new Back.Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError(int code, String error) {
                            Toast.makeText(GalleryActivity.this,error,Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 101 && resultCode == RESULT_OK) {
            final String result = data.getStringExtra("msg");
            if(!IMStringUtil.isEmpty(result)) {
                if (result.startsWith("http://enterprise.yun2win.com")) {
                    TvManage.getInstance().ParseScanTv(GalleryActivity.this, result, new Back.Result<TalkTvEntity>() {
                        @Override
                        public void onSuccess(TalkTvEntity talkTvEntity) {
                            if(TvManage.getInstance().getCurrent_TalkTv()!=null) {
                                String message = TalkManage.getInstance().galleryMsg("image", imgUrls[currentPage], imgUrls[currentPage]);
                                TalkManage.getInstance().pushMessageTv(message,TvManage.getInstance().getCurrent_TalkTv().getTv_userId(), new Back.Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError(int code, String error) {
                                        Toast.makeText(GalleryActivity.this,error,Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(int i, String s) {

                        }

                        @Override
                        public void OnEvent(int i, String s) {

                        }
                    });
                }
            }

        }
    }

}
