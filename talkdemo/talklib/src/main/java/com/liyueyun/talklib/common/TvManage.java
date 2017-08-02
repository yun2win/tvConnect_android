package com.liyueyun.talklib.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liyueyun.talklib.R;
import com.liyueyun.talklib.model.TalkTvEntity;
import com.liyueyun.talklib.ui.activity.CaptureActivity;
import com.liyueyun.talklib.ui.adapter.TvEntityAdapter;
import com.yun2win.talksdk.Back;
import com.yun2win.utils.IMStringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejie on 2017/8/1.
 */

public class TvManage {

    public TalkTvEntity current_TalkTv;//当前连接上的电视
    private static TvManage tvManage = null;
    private Dialog tvListDialog=null;

    public static TvManage getInstance(){
        if(tvManage == null){
            tvManage = new TvManage();
        }
        return tvManage;
    }


    public TalkTvEntity getCurrent_TalkTv() {
        return current_TalkTv;
    }

    public void setCurrent_TalkTv(TalkTvEntity current_TalkTv) {
        this.current_TalkTv = current_TalkTv;
    }

    /**
     * 显示电视列表
     * @param context 应用上下文
     * @param result  回调返回用户选择的电视或者错误码
     */
    public void dialog_Select_Tv(final Activity context, final Back.Result<TalkTvEntity> result){
        if(context==null) {
            Toast.makeText(context,"参数错误",Toast.LENGTH_LONG).show();
            return;
        }
        if(tvListDialog==null) {
            tvListDialog = new Dialog(context);
        }
        tvListDialog.setContentView(R.layout.dialog_tvlist);
        tvListDialog.show();
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
        ImageView title_tvscan = (ImageView) tvListDialog.findViewById(R.id.title_tvscan);
        title_tvscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //扫描
                context.startActivityForResult(new Intent(context, CaptureActivity.class), 101);
                tvListDialog.dismiss();
                //result.OnEvent(500,"调用用户的扫描界面");
            }
        });
        TextView tv_close = (TextView) tvListDialog.findViewById(R.id.tv_close);
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvListDialog.dismiss();
            }
        });
        GridView gridview_tv = (GridView) tvListDialog.findViewById(R.id.gridview_tv);
        gridview_tv.setAdapter(tvEntityAdapter);
        TextView no_tvlist = (TextView) tvListDialog.findViewById(R.id.no_tvlist);
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
                result.onSuccess(current_TalkTv);
                tvListDialog.dismiss();
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

    /**
     *
     * @param context 应用上下文
     * @param result 扫描的内容
     * @param backResult 解析扫描结果返回的电视或错误码
     */
    public void ParseScanTv(Context context,String result,final Back.Result<TalkTvEntity> backResult){
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

                add2ConnectTv(context, deviceId, tvName,backResult);
            }
        }
    }
    //添加tv并连接
    private  void add2ConnectTv(Context context,String tv_userId,String tv_name,final Back.Result<TalkTvEntity> backResult){
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
                backResult.onSuccess(current_TalkTv);
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
            backResult.onSuccess(current_TalkTv);
        }
    }
}
