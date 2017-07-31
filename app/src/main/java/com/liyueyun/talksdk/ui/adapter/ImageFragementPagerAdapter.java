package com.liyueyun.talksdk.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class ImageFragementPagerAdapter extends FragmentStatePagerAdapter {

	private List<Fragment> list;
	public ImageFragementPagerAdapter(FragmentManager fm, List<Fragment> _list) {
		super(fm);
		this.list = _list;
		notifyDataSetChanged();
	}

	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list !=null ?list.size():0;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}
	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return PagerAdapter.POSITION_NONE;
	}
}
