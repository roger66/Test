package com.fuli19.ui.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fuli19.R;
import com.fuli19.app.MyApp;
import com.fuli19.model.entity.News;
import com.fuli19.model.entity.NewsImg;
import com.fuli19.ui.adapter.PicPreviewAdapter;
import com.fuli19.ui.base.BaseActivity;
import com.fuli19.ui.base.BasePresenter;
import com.fuli19.ui.dialog.SendCommentDialog;
import com.fuli19.ui.presenter.PicPreviewPresenter;
import com.fuli19.utils.GlideUtils;
import com.fuli19.utils.UIUtils;
import com.fuli19.utils.WelfareHelper;
import com.fuli19.view.IPicPreviewView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.badgeview.BGABadgeImageView;

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

    @BindView(R.id.pic_preview_attention)
    TextView mAttentionBtn;

    @BindView(R.id.pic_preview_name)
    TextView mAuthorName;

    private BGABadgeImageView mCommentCount;

    @BindView(R.id.pic_preview_collection)
    ImageView mCollectionBtn;

    private News mNews;
    private SendCommentDialog mCommentDialog;

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
        mAttentionBtn.setVisibility(news.publisherId.equals("0") ||MyApp.getId().equals(news.publisherId) ?View.GONE:View.VISIBLE);
        mAttentionBtn.setSelected(news.is_follow==1);
        mAttentionBtn.setText(news.is_follow==1?"已关注":"关注");
        if (!news.commentNum.equals("0"))
        mCommentCount.showTextBadge(news.commentNum);
        mCommentDialog.setNewsId(news.id);
        mPreviewVp.setAdapter(new PicPreviewAdapter(news.thumbnailImg));
        mPreviewVp.setPageMargin(10);
        int position = getIntent().getIntExtra(NewsDetailBaseActivity.POSITION, 0);
        mPreviewVp.setCurrentItem(position);
        //设置头部信息
        mPreviewTitle.setText(((position + 1) + "/" + news.imgNum) + " " + news.thumbnailImg.get(position).imgDescription);
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
                mPreviewTitle.setText(((position + 1) + "/" + news.imgNum) + " " + newsImg.imgDescription);
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
        registerEventBus(this);
    }

    @Override
    public void initView() {
        mCommentCount = (BGABadgeImageView) findViewById(R.id.pic_preview_comment_count);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mTitleLayout
                .getLayoutParams();
        params.topMargin = UIUtils.getStatusHeight();
        mTitleLayout.setLayoutParams(params);
        mCommentDialog = new SendCommentDialog();
    }

    @OnClick({R.id.pic_preview_close, R.id.pic_preview_collection
            ,R.id.pic_preview_attention
            ,R.id.pic_preview_send_comment,R.id.pic_preview_comment_count})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pic_preview_close:
                finish();
                break;
            case R.id.pic_preview_send_comment:
                if (WelfareHelper.isLogin(this))
                    mCommentDialog.show(getSupportFragmentManager());
                break;
            case R.id.pic_preview_comment_count:
                Intent intent = new Intent(this, CommentActivity.class);
                intent.putExtra("id",mNews.id);
                startActivity(intent);
                break;
            case R.id.pic_preview_attention:
                if (WelfareHelper.isLogin(this)) {
                    if (mNews.is_follow == 1) {
                        mPresenter.cancelAttention(mNews.publisherId);
                        mNews.is_follow = 0;
                        mAttentionBtn.setText("关注");
                        mAttentionBtn.setSelected(false);
                    } else {
                        mNews.is_follow = 1;
                        mAttentionBtn.setText("已关注");
                        mPresenter.attention(mNews.publisherId);
                        mAttentionBtn.setSelected(true);
                    }
                }
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
        unregisterEventBus(this);
    }
}
