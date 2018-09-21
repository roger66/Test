package com.chaychan.news.ui.activity;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaychan.news.R;
import com.chaychan.news.model.entity.NewsDetail;
import com.chaychan.news.utils.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;
import flyn.Eyes;

/**
 * @author ChayChan
 * @description: 非视频新闻详情
 * @date 2017/6/24  19:3
 */

public class NewsDetailActivity extends NewsDetailBaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;

    @BindView(R.id.ll_user)
    LinearLayout mLlUser;

    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;

    @BindView(R.id.tv_author)
    TextView mTvAuthor;


    @Override
    protected int getViewContentViewId() {
        return R.layout.activity_news_detail;
    }


    @Override
    public void initView() {
        super.initView();
        Eyes.setStatusBarColor(this, UIUtils.getColor(R.color.color_BDBDBD));//设置状态栏的颜色为灰色
    }

    @Override
    public void initListener() {
        super.initListener();

        int llInfoBottom = mHeaderView.mLlInfo.getBottom();
//        LinearLayoutManager layoutManager = (LinearLayoutManager) mRvComment.getLayoutManager();
//        mRvComment.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                int position = layoutManager.findFirstVisibleItemPosition();
//                View firstVisiableChildView = layoutManager.findViewByPosition(position);
//                int itemHeight = firstVisiableChildView.getHeight();
//                int scrollHeight = (position) * itemHeight - firstVisiableChildView.getTop();
//
//                KLog.i("scrollHeight: " + scrollHeight);
//                KLog.i("llInfoBottom: " + llInfoBottom);
//
//                mLlUser.setVisibility(scrollHeight > llInfoBottom ? View.VISIBLE : View.GONE);//如果滚动超过用户信息一栏，显示标题栏中的用户头像和昵称
//            }
//        });
    }

    @Override
    public void onGetNewsDetailSuccess(NewsDetail newsDetail) {
        mHeaderView.setDetail(newsDetail, () -> {
            //加载完成后，显示内容布局
            mStateView.showContent();
        });

    }

    @Override
    public void onBackPressed() {
        postVideoEvent(false);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        postVideoEvent(false);
    }
}
