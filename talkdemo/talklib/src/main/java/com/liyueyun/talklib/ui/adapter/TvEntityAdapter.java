package com.liyueyun.talklib.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liyueyun.talklib.R;
import com.liyueyun.talklib.model.TalkTvEntity;

import java.util.List;

public class TvEntityAdapter extends BaseAdapter {

    private List<TalkTvEntity> tvModels;
    private Context context;
    public TvEntityAdapter(Context context,List<TalkTvEntity> tvModels){
        this.context = context;
        this.tvModels =tvModels;
    }

    public void updateListView() {
     try {
         notifyDataSetChanged();
     }catch (Exception e){}
    }

    @Override
    public int getCount() {
        return tvModels == null ? 0 :tvModels.size();
    }

    @Override
    public Object getItem(int position) {
        return tvModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(position > tvModels.size() - 1){
            return view;
        }
       final TalkTvEntity talkTvEntity = tvModels.get(position);
        HoldView holdView;
        if (null == view) {
            holdView = new HoldView();
            view = LayoutInflater.from(context).inflate(R.layout.tv_item, null);
            holdView.tv_title = (TextView) view
                    .findViewById(R.id.divice_name1);
            view.setTag(holdView);
        } else {
            holdView = (HoldView) view.getTag();
        }
        holdView.tv_title.setText(talkTvEntity.getTv_name());
        return view;
    }

    class HoldView {
        public TextView tv_title;
    }

}
