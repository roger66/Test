package com.fuli19.ui.activity;

import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuli19.R;
import com.fuli19.model.entity.NewsDetail;
import com.fuli19.utils.GlideUtils;
import com.fuli19.utils.UIUtils;
import com.fuli19.utils.WelfareHelper;

import butterknife.BindView;
import butterknife.OnClick;
import flyn.Eyes;

/**
 * @author ChayChan
 * @description: 非视频新闻详情
 * @date 2017/6/24  19:3
 */

public class LongArticleDetailActivity extends NewsDetailBaseActivity{

    @BindView(R.id.long_article_title)
    TextView mTitle;

    @BindView(R.id.long_article_head)
    ImageView mIvAvatar;

    @BindView(R.id.long_article_name)
    TextView mTvAuthor;

    @BindView(R.id.long_article_content)
    WebView mContent;

    @BindView(R.id.news_detail_collection)
    ImageView mCollectionBtn;

    private NewsDetail mNewsDetail;

    @Override
    public void initView() {
        super.initView();
        Eyes.setStatusBarColor(this, UIUtils.getColor(R.color.color_BDBDBD));//设置状态栏的颜色为灰色
    }

    @Override
    public void initData() {
        super.initData();
        mStateView.showLoading();
        mPresenter.getLongArticleDetail(mItemId);
    }

    @Override
    protected int getViewContentViewId() {
        return R.layout.activity_long_article_detail;
    }

    @Override
    public void onGetNewsDetailSuccess(NewsDetail newsDetail) {
        mNewsDetail = newsDetail;
        mStateView.showContent();
        if (!newsDetail.commentNum.equals("0"))
        mCommentCount.showTextBadge(newsDetail.commentNum);
        mTitle.setText(newsDetail.title);
        mTvAuthor.setText(newsDetail.publisher);
        GlideUtils.load(this,newsDetail.publisherPic,mIvAvatar);
        mContent.loadData(newsDetail.text,"text/html; charset=UTF-8", null);
    }


    @Override
    public void onDataEmpty() {

    }

    @Override
    public void onError() {
        mStateView.showRetry();
    }


    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    @OnClick({R.id.news_detail_collection,R.id.news_detail_comment_count})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.news_detail_collection:
                if (WelfareHelper.isLogin(this)) {
                    if (mNewsDetail.is_collection == 1) {
                        mNewsDetail.is_collection = 0;
                        mCollectionBtn.setSelected(false);
                        mPresenter.cancelCollection(mNewsDetail.id);
                    } else {
                        mNewsDetail.is_collection = 1;
                        mCollectionBtn.setSelected(true);
                        mPresenter.collection(mNewsDetail.id);
                    }
                }
                break;
            case R.id.news_detail_comment_count:
                Intent intent = new Intent(this, CommentActivity.class);
                intent.putExtra("id",mNewsDetail.id);
                startActivity(intent);
                break;
        }
    }

}
