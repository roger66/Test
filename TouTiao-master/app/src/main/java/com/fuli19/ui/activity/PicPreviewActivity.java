package com.fuli19.ui.activity;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fuli19.R;
import com.fuli19.model.entity.News;
import com.fuli19.model.entity.NewsImg;
import com.fuli19.ui.adapter.PicPreviewAdapter;
import com.fuli19.ui.base.BaseActivity;
import com.fuli19.ui.base.BasePresenter;
import com.fuli19.ui.presenter.PicPreviewPresenter;
import com.fuli19.utils.GlideUtils;
import com.fuli19.utils.UIUtils;
import com.fuli19.utils.WelfareHelper;
import com.fuli19.view.IPicPreviewView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class PicPreviewActivity extends BaseActivity<PicPreviewPresenter> implements
        IPicPreviewView {

    @BindView(R.id.pic_preview_top_layout)
    RelativeLayout mTitleLayout;

    @BindView(R.id.pic_preview_vp)
    ViewPager mPreviewVp;

    @BindView(R.id.pic_preview_title)
    TextView mPreviewTitle;

    @BindView(R.id.pic_preview_head)
    ImageView mAuthorHead;

    @BindView(R.id.pic_preview_name)
    TextView mAuthorName;

    @BindView(R.id.pic_preview_collection)
    ImageView mCollectionBtn;

    private News mNews;

    @Override
    protected PicPreviewPresenter createPresenter() {
        return new PicPreviewPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_pic_preview;
    }

    @Subscribe(sticky = true)
    public void onEvent(News news) {
        mNews = news;
        mPreviewVp.setAdapter(new PicPreviewAdapter(news.thumbnailImg));
        mPreviewVp.setPageMargin(10);
        int position = getIntent().getIntExtra(NewsDetailBaseActivity.POSITION, 0);
        mPreviewVp.setCurrentItem(position);
        //设置头部信息
        mPreviewTitle.setText(((position + 1) + "/" + news.imgNum) + " " + news.thumbnailImg.get
                (position).imgDescription);
        mAuthorName.setText(news.publisher);
        GlideUtils.load(this, news.publisherPic, mAuthorHead);
        mCollectionBtn.setSelected(news.is_collection == 1);
        mPreviewVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                NewsImg newsImg = news.thumbnailImg.get(position);
                mPreviewTitle.setText(((position + 1) + "/" + news.imgNum) + " " + newsImg
                        .imgDescription);
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

    @Override
    public void initView() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mTitleLayout
                .getLayoutParams();
        params.topMargin = UIUtils.getStatusHeight();
        mTitleLayout.setLayoutParams(params);
    }

    @OnClick({R.id.pic_preview_close, R.id.pic_preview_collection})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pic_preview_close:
                finish();
                break;
            case R.id.pic_preview_collection:
                if (WelfareHelper.isLogin(this)) {
                    if (mNews.is_collection == 1) {
                        mPresenter.cancelCollection(mNews.id);
                        mNews.is_collection = 0;
                        mCollectionBtn.setSelected(false);
                    } else {
                        mNews.is_collection = 1;
                        mPresenter.collection(mNews.id);
                        mCollectionBtn.setSelected(true);
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
