package com.fuli19.ui.activity;

import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuli19.R;
import com.fuli19.model.entity.NewsDetail;
import com.fuli19.utils.GlideUtils;
import com.fuli19.utils.UIUtils;

import butterknife.BindView;
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

    @Override
    public void initView() {
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
        mStateView.showContent();
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

}
