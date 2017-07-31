package com.liyueyun.talksdk.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liyueyun.talklib.Back;
import com.liyueyun.talklib.TalkManage;
import com.liyueyun.talksdk.common.SharedPreferenceUtil;
import com.liyueyun.talksdk.model.TalkTvEntity;
import com.liyueyun.talksdk.R;
import com.liyueyun.talksdk.ui.adapter.ImageFragementPagerAdapter;
import com.liyueyun.talksdk.ui.adapter.TvEntityAdapter;
import com.liyueyun.talksdk.ui.fragment.MoreImageBrowseFragment;
import com.liyueyun.talksdk.ui.weight.PhotoViewPager;
import com.liyueyun.talksdk.ui.weight.RectTotvView;
import com.yun2win.utils.IMStringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends FragmentActivity {

    private PhotoViewPager vp_pager;
    private RectTotvView rectTotvView;
    private ImageButton right_img_size;
    private boolean isGotoTvSize = false;
    private ImageButton right_connect_tv;
    private Activity activity;
    private int currentPage = 0;//当前显示页面
    private TalkTvEntity current_TalkTv;//当前连接上的电视
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    ImageFragementPagerAdapter imageFragementPagerAdapter;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what ==1){
                right_img_size.setVisibility(View.VISIBLE);
            }
        }
    };
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
        rectTotvView = (RectTotvView) findViewById(R.id.size_imgsurfaceview);
        rectTotvView.setListenSizeBack(new RectTotvView.ListenSizeBack() {
            @Override
            public void onListenBack(float x, float y, float width, float height) {
                sendSizeMessageTv(x,y,width,height);
            }
            @Override
            public RectF getImageRectF() {
                return getCurentImageRectF();
            }

        });
        right_img_size = (ImageButton) findViewById(R.id.right_img_size);
        right_img_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isGotoTvSize = !isGotoTvSize;
                if(isGotoTvSize) {
                    rectTotvView.setVisibility(View.VISIBLE);
                    right_img_size.setImageResource(R.mipmap.sizetv_chose);
                    if (currentPage < fragmentList.size()) {
                        MoreImageBrowseFragment fragment = (MoreImageBrowseFragment) fragmentList.get(currentPage);
                        fragment.setOnTouch(rectTotvView);
                    }
                }else{
                    sendSizeMessageTv(0,0,1,1);
                    rectTotvView.setVisibility(View.GONE);
                    right_img_size.setImageResource(R.mipmap.sizetv_unchose);
                    if (currentPage < fragmentList.size()) {
                        MoreImageBrowseFragment fragment = (MoreImageBrowseFragment) fragmentList.get(currentPage);
                        fragment.setOnTouch(null);
                    }
                }
            }
        });
        right_connect_tv = (ImageButton) findViewById(R.id.right_connect_tv);
        right_connect_tv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog_Select_Tv(GalleryActivity.this);
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


                if(rectTotvView!=null&&isGotoTvSize) {
                    if (currentPage < fragmentList.size()) {
                        MoreImageBrowseFragment fragment = (MoreImageBrowseFragment) fragmentList.get(currentPage);
                        fragment.setOnTouch(null);
                    }
                    if (position < fragmentList.size()) {
                        rectTotvView.setVisibility(View.VISIBLE);
                        MoreImageBrowseFragment fragment = (MoreImageBrowseFragment) fragmentList.get(position);
                        fragment.setOnTouch(rectTotvView);
                    }
                }

                currentPage = position;

                //sendMessageTv(position);
                if(current_TalkTv!=null) {
                    String message = TalkManage.getInstance().galleryMsg("image", imgUrls[currentPage], imgUrls[currentPage]);
                    TalkManage.getInstance().pushMessageTv(message,current_TalkTv.getTv_userId(), new Back.Callback() {
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
    public RectF getCurentImageRectF(){
        if (currentPage < fragmentList.size()) {
            MoreImageBrowseFragment fragment = (MoreImageBrowseFragment) fragmentList.get(currentPage);
            return fragment.getImageRectF();
        }
        return  null;
    }
    private void sendSizeMessageTv(float x,float y,float width,float height){
        if(width>0&&height>0&&current_TalkTv!=null) {
            String message =TalkManage.getInstance().pushSizeMsgToTv(x,y,width,height);
            TalkManage.getInstance().pushMessageTv(message,current_TalkTv.getTv_userId(), new Back.Callback() {
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

    public void dialog_Select_Tv(Context context){
        if(context==null) {
            Toast.makeText(context,"参数错误",Toast.LENGTH_LONG).show();
            return;
        }
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_tvlist);
        dialog.show();
        final List<TalkTvEntity> tvEntities = new ArrayList<TalkTvEntity>();
        String tvlist = SharedPreferenceUtil.getTalkTvList(context);
        if(!IMStringUtil.isEmpty(tvlist)){
            try {
                JSONArray jsonArray = new JSONArray(tvlist);
                for(int i =0;i<jsonArray.length();i++){
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    TalkTvEntity talkTvEntity = new TalkTvEntity();
                    talkTvEntity.setTv_name(jsonObject.getString("tvName"));
                    talkTvEntity.setTv_userId(jsonObject.getString("tvUserId"));
                    tvEntities.add(talkTvEntity);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        TvEntityAdapter tvEntityAdapter = new TvEntityAdapter(context,tvEntities);
        ImageView title_tvscan = (ImageView) dialog.findViewById(R.id.title_tvscan);
        title_tvscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //扫描
                startActivityForResult(new Intent(GalleryActivity.this, CaptureActivity.class), 101);
                dialog.dismiss();
            }
        });
        TextView tv_close = (TextView) dialog.findViewById(R.id.tv_close);
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        GridView gridview_tv = (GridView) dialog.findViewById(R.id.gridview_tv);
        gridview_tv.setAdapter(tvEntityAdapter);
        TextView no_tvlist = (TextView) dialog.findViewById(R.id.no_tvlist);
        if(tvEntities.size()>0){
            no_tvlist.setVisibility(View.GONE);
            gridview_tv.setVisibility(View.VISIBLE);
        }else{
            no_tvlist.setVisibility(View.VISIBLE);
            gridview_tv.setVisibility(View.GONE);
        }
        gridview_tv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                current_TalkTv = tvEntities.get(i);
                String message = TalkManage.getInstance().galleryMsg("image",imgUrls[currentPage],imgUrls[currentPage]);
                TalkManage.getInstance().pushMessageTv(message, current_TalkTv.getTv_userId(),new Back.Callback() {
                    @Override
                    public void onSuccess() {

                        handler.sendEmptyMessage(1);
                    }

                    @Override
                    public void onError(int code, String error) {
                        Toast.makeText(GalleryActivity.this,error,Toast.LENGTH_LONG).show();
                    }
                });

                dialog.dismiss();
            }
        });
        gridview_tv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //修改名字
                return true;
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 101 && resultCode == RESULT_OK) {
            final String result = data.getStringExtra("msg");
            if(!IMStringUtil.isEmpty(result)) {
                if (result.startsWith("http://enterprise.yun2win.com")) {
                    String tv1[] = result.split("tv/");
                    if (tv1.length > 1) {
                        String str1 = tv1[1];
                        String tvIds[] = str1.split("/");
                        if (!IMStringUtil.isEmpty(tvIds[0])) {
                            String deviceId = tvIds[0];//电视的UserId
                            String tv2[]= result.split("/?name=");
                            String tvName="新的电视";//电视的名字
                              if(tv2.length>1){
                                  tvName = tv2[1];
                                  try {
                                      tvName= URLDecoder.decode(tvName,"UTF-8");
                                  } catch (Exception e) {
                                  }
                              }

                           add2ConnectTv(GalleryActivity.this, deviceId, tvName);
                        }
                    }
                }
            }

        }
    }

    //添加tv并连接
    public void add2ConnectTv(Context context,String tv_userId,String tv_name){
        if(context==null||IMStringUtil.isEmpty(tv_userId)||IMStringUtil.isEmpty(tv_name)){
            Toast.makeText(context,"参数错误",Toast.LENGTH_LONG).show();
            return;
        }
        String tvlist = SharedPreferenceUtil.getTalkTvList(context);
        List<TalkTvEntity> tvEntities = new ArrayList<TalkTvEntity>();
        if(!IMStringUtil.isEmpty(tvlist)){
            try {
                JSONArray jsonArray = new JSONArray(tvlist);
                for(int i =0;i<jsonArray.length();i++){
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    TalkTvEntity talkTvEntity = new TalkTvEntity();
                    talkTvEntity.setTv_name(jsonObject.getString("tvName"));
                    talkTvEntity.setTv_userId(jsonObject.getString("tvUserId"));
                    tvEntities.add(talkTvEntity);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        boolean find = false;
        for(TalkTvEntity talktv:tvEntities){
            if(talktv.getTv_userId().equals(tv_userId)){
                find = true;
                current_TalkTv = talktv;
                String message = TalkManage.getInstance().galleryMsg("image",imgUrls[currentPage],imgUrls[currentPage]);
                TalkManage.getInstance().pushMessageTv(message,current_TalkTv.getTv_userId(), new Back.Callback() {
                    @Override
                    public void onSuccess() {
                        handler.sendEmptyMessage(1);
                    }
                    @Override
                    public void onError(int code, String error) {
                        Toast.makeText(GalleryActivity.this,error,Toast.LENGTH_LONG).show();
                    }
                });
                break;
            }
        }
        if(!find){
            TalkTvEntity talktv = new TalkTvEntity();
            talktv.setTv_name(tv_name);
            talktv.setTv_userId(tv_userId);
            tvEntities.add(talktv);

            try {
                JSONArray jsonArray;
                if(!IMStringUtil.isEmpty(tvlist)){
                    jsonArray = new JSONArray(tvlist);
                }else{
                    jsonArray = new JSONArray();
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("tvName",tv_name);
                jsonObject.put("tvUserId",tv_userId);
                jsonArray.put(jsonObject);
                SharedPreferenceUtil.setTalkTvList(context,jsonArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            current_TalkTv = talktv;
            String message = TalkManage.getInstance().galleryMsg("image",imgUrls[currentPage],imgUrls[currentPage]);
            TalkManage.getInstance().pushMessageTv(message,current_TalkTv.getTv_userId(), new Back.Callback() {
                @Override
                public void onSuccess() {
                    handler.sendEmptyMessage(1);
                }
                @Override
                public void onError(int code, String error) {
                    Toast.makeText(GalleryActivity.this,error,Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
