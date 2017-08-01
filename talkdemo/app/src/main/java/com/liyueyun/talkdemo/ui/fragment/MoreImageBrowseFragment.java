package com.liyueyun.talkdemo.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.liyueyun.talkdemo.R;
import com.liyueyun.talkdemo.ui.weight.photoview.PhotoView;


/**
 * Created by maa2 on 2015/12/11.
 */
public class MoreImageBrowseFragment extends Fragment {

    private static Activity activity;
    private static Context context;

    PhotoView iv_preview;


    private String imgUrl;

    ImageView videoplay;
    public static MoreImageBrowseFragment newInstance(Activity _activity,
                                                      Context _context, final String imgUrl) {
        activity = _activity;
        context = _context;
        MoreImageBrowseFragment newFragment = new MoreImageBrowseFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("imgUrl",imgUrl);
        newFragment.setArguments(bundle);
        return newFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        imgUrl =  args.getString("imgUrl");
        View view = null;
        view = inflater.inflate(R.layout.image_browse_pager_item, container,
                false);

        iv_preview = (PhotoView) view.findViewById(R.id.iv_preview);

        Glide.with(context)
                .load(imgUrl)//加载图片
                .placeholder(R.mipmap.img_default)//正在加载时的图片
                .error(R.mipmap.img_default)//加载错误是的图片
                .into(iv_preview);
        return view;
    }


}
