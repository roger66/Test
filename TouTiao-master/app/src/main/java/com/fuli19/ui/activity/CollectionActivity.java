package com.fuli19.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaychan.uikit.powerfulrecyclerview.PowerfulRecyclerView;
import com.fuli19.R;
import com.fuli19.model.entity.News;
import com.fuli19.ui.adapter.MicroListAdapter;
import com.fuli19.ui.base.BaseActivity;
import com.fuli19.ui.presenter.CollectionPresenter;
import com.fuli19.utils.WelfareHelper;
import com.fuli19.view.ICollectionView;
import com.github.nukc.stateview.StateView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import flyn.Eyes;
import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;

public class CollectionActivity extends BaseActivity<CollectionPresenter> implements
        ICollectionView, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.collection_rv)
    PowerfulRecyclerView mCollectionRv;
    @BindView(R.id.collection_container)
    FrameLayout mContainer;

    private int page = 1;
    private List<News> mData = new ArrayList<>();
    private MicroListAdapter mAdapter;

    @Override
    protected CollectionPresenter createPresenter() {
        return new CollectionPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_collection;
    }

    @Override
    public void initView() {
        mStateView = StateView.inject(mContainer);
        Eyes.setStatusBarColor(this,R.color.color_BDBDBD);
        mAdapter = new MicroListAdapter("", mData);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(this,mCollectionRv);
        mCollectionRv.setLayoutManager(new LinearLayoutManager(this));
        mCollectionRv.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        mAdapter.setOnItemClickListener(mOnItemClickListener);
        mAdapter.setOnItemChildClickListener(mOnItemChildClickListener);
    }

    @Override
    public void initData() {
        mStateView.showLoading();
        mPresenter.getFav(page);
    }

    @Override
    public void onGetFavSuccess(List<News> newsList) {
        page += 1;
        mStateView.showContent();
        mData.addAll(newsList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDataEmpty() {
        if (mData.isEmpty())
            mStateView.showEmpty();
        else mAdapter.loadMoreEnd();
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.getFav(page);
    }

    //News点击监听
    private BaseQuickAdapter.OnItemClickListener mOnItemClickListener = (baseQuickAdapter, view,
                                                                         position) -> {
        News news = mData.get(position);
        Intent intent = null;
        if (news.type.equals("1")) {
            //视频
            intent = new Intent(this, VideoDetailActivity.class);
            if (JCVideoPlayerManager.getCurrentJcvd() != null) {
                //传递进度
                int progress = JCMediaManager.instance().mediaPlayer
                        .getCurrentPosition();
                if (progress != 0) {
                    intent.putExtra(VideoDetailActivity.PROGRESS, progress);
                }
            }
        } else if (news.type.equals("2") || news.type.equals("3"))
            //纯图片
            intent = new Intent(this, news.type_article == 1 ?
                    LongArticleDetailActivity.class : PicPreviewActivity.class);
        else
            return;

        intent.putExtra(NewsDetailBaseActivity.POSITION, position);
        intent.putExtra(NewsDetailBaseActivity.ITEM_ID, news.id);
        startActivity(intent);
        EventBus.getDefault().postSticky(news);
    };

    //News child点击事件
    private BaseQuickAdapter.OnItemChildClickListener mOnItemChildClickListener =
            (baseQuickAdapter, view, position) -> {
                switch (view.getId()) {
                    case R.id.tv_like_num:
                        if (WelfareHelper.isLogin(this)) {
                            News news = mData.get(position);
                            int likeNum = Integer.valueOf(news.thumbsUp);
                            if (news.is_thumbsUp == 0) {
                                mPresenter.like(news.id);
                                news.is_thumbsUp = 1;
                                likeNum += 1;
                            } else {
                                mPresenter.cancelLike(news.id);
                                news.is_thumbsUp = 0;
                                likeNum -= 1;
                            }
                            news.thumbsUp = String.valueOf(likeNum);
                            mAdapter.notifyItemChanged(position);
                        }
                        break;
                }
            };

    @OnClick(R.id.collection_back)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.collection_back:
                finish();
                break;
        }
    }

}
