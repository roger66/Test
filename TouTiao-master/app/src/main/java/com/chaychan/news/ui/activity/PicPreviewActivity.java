package com.chaychan.news.ui.activity;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaychan.news.R;
import com.chaychan.news.model.entity.News;
import com.chaychan.news.model.entity.NewsImg;
import com.chaychan.news.ui.adapter.PicPreviewAdapter;
import com.chaychan.news.ui.base.BaseActivity;
import com.chaychan.news.ui.base.BasePresenter;
import com.chaychan.news.utils.GlideUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class PicPreviewActivity extends BaseActivity {

    @BindView(R.id.pic_preview_vp)
    ViewPager mPreviewVp;

    @BindView(R.id.pic_preview_title)
    TextView mPreviewTitle;

    @BindView(R.id.pic_preview_head)
    ImageView mAuthorHead;

    @BindView(R.id.pic_preview_name)
    TextView mAuthorName;

    private News mNews;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_pic_preview;
    }

    @Subscribe (sticky = true)
    public void onEvent(News news){
        mNews = news;
        mPreviewVp.setAdapter(new PicPreviewAdapter(news.thumbnailImg));
        mPreviewVp.setPageMargin(10);
        int position = getIntent().getIntExtra(NewsDetailBaseActivity.POSITION, 0);
        mPreviewVp.setCurrentItem(position);
        //设置头部信息
        mPreviewTitle.setText(((position+1)+"/"+news.imgNum)+" "+news.thumbnailImg.get(position).imgDescription);
        mAuthorName.setText(news.publisher);
        GlideUtils.load(this,news.publisherPic,mAuthorHead);
        mPreviewVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                NewsImg newsImg = news.thumbnailImg.get(position);
                mPreviewTitle.setText(((position+1)+"/"+news.imgNum)+" "+newsImg.imgDescription);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean translucentStatusBar() {
        return true;
    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
    }

    @OnClick({R.id.pic_preview_close})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.pic_preview_close:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
