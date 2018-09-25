package com.fuli19.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.fuli19.R;
import com.fuli19.model.entity.NewsImg;
import com.fuli19.utils.GlideUtils;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

public class PicPreviewAdapter extends PagerAdapter {

    private List<NewsImg> newsImg;

    public PicPreviewAdapter(List<NewsImg> newsImg) {
        this.newsImg = newsImg;
    }

    @Override
    public int getCount() {
        return newsImg==null?0:newsImg.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView photo = (PhotoView) View.inflate(container.getContext(), R.layout.item_pic_preview,null);
        GlideUtils.load(container.getContext(),newsImg.get(position).bigImgUrl,photo);
        container.addView(photo);
        return photo;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
