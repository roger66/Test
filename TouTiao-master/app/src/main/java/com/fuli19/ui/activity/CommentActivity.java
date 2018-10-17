package com.fuli19.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chaychan.uikit.powerfulrecyclerview.PowerfulRecyclerView;
import com.fuli19.R;
import com.fuli19.model.entity.CommentData;
import com.fuli19.ui.adapter.CommentAdapter;
import com.fuli19.ui.base.BaseActivity;
import com.fuli19.ui.dialog.SendCommentDialog;
import com.fuli19.ui.presenter.CommentPresenter;
import com.fuli19.view.ICommentView;
import com.github.nukc.stateview.StateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CommentActivity extends BaseActivity<CommentPresenter> implements ICommentView {

    @BindView(R.id.news_detail_collection)
    ImageView mCollectionBtn;

    @BindView(R.id.comment_fl_content)
    FrameLayout mContentLayout;

    @BindView(R.id.comment_rv)
    PowerfulRecyclerView mRv;

    private int page = 1;
    private List<CommentData> mCommentData = new ArrayList<>();
    private CommentAdapter mCommentAdapter;
    private SendCommentDialog mSendCommentDialog;

    @Override
    protected CommentPresenter createPresenter() {
        return new CommentPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_comment;
    }

    @Override
    public void initView() {
        mSendCommentDialog = new SendCommentDialog();
        findViewById(R.id.news_detail_comment_count).setVisibility(View.GONE);
        mCollectionBtn.setVisibility(View.GONE);

        mCommentAdapter = new CommentAdapter();
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(mCommentAdapter);
    }

    @Override
    public void initData() {
        String id = getIntent().getStringExtra("id");
        mSendCommentDialog.setNewsId(id);
        mStateView = StateView.inject(mContentLayout);
        mStateView.showLoading();
        mPresenter.getComment(id,page);
    }

    @Override
    public void onGetCommentSuccess(List<CommentData> data) {
        mStateView.showContent();
        mCommentData.addAll(data);
        mCommentAdapter.setNewData(mCommentData);
    }

    @Override
    public void onError() {
        mStateView.showRetry();
    }

    @Override
    public void onDataEmpty() {
            mStateView.showEmpty();
    }

    @OnClick(R.id.detail_write_comment)
    public void onClick(View view){
        mSendCommentDialog.show(getSupportFragmentManager());
    }

}
