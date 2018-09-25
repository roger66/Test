package com.fuli19.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.fuli19.R;
import com.fuli19.constants.Constant;
import com.fuli19.model.entity.Channel;
import com.fuli19.ui.adapter.ChannelPagerAdapter;
import com.fuli19.ui.base.BaseFragment;
import com.fuli19.ui.presenter.VideoPresenter;
import com.fuli19.utils.UIUtils;
import com.fuli19.view.IVideoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import me.weyye.library.colortrackview.ColorTrackTabLayout;

/**
 * @author ChayChan
 * @description: 视频fragment
 * @date 2017/6/12  21:47
 */

public class VideoFragment extends BaseFragment<VideoPresenter> implements IVideoView {

    @BindView(R.id.tab_channel)
    ColorTrackTabLayout mTabChannel;

    @BindView(R.id.vp_content)
    ViewPager mVpContent;

    private List<Channel> mChannelList = new ArrayList<>();
    private List<NewsListFragment> mFragmentList = new ArrayList<>();

    @Override
    protected VideoPresenter createPresenter() {
        return new VideoPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_video;
    }


    private void initChannelFragments() {
        for (Channel channel : mChannelList) {
            NewsListFragment newsFragment = new NewsListFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constant.CHANNEL_CODE, channel.type);
            bundle.putString(Constant.CHANNEL_ID, channel.id);
            bundle.putBoolean(Constant.IS_VIDEO_LIST, true);//是否是视频列表页面,]true
            newsFragment.setArguments(bundle);
            mFragmentList.add(newsFragment);
        }
    }

    @Override
    public void initListener() {
        ChannelPagerAdapter channelPagerAdapter = new ChannelPagerAdapter(mFragmentList, mChannelList,getChildFragmentManager());
        channelPagerAdapter.setVideoList(true);
        mVpContent.setAdapter(channelPagerAdapter);
        mVpContent.setOffscreenPageLimit(mFragmentList.size());

        mTabChannel.setTabPaddingLeftAndRight(UIUtils.dip2Px(10), UIUtils.dip2Px(10));
        mTabChannel.setupWithViewPager(mVpContent);
        mTabChannel.post(() -> {
            //设置最小宽度，使其可以在滑动一部分距离
            ViewGroup slidingTabStrip = (ViewGroup) mTabChannel.getChildAt(0);
            slidingTabStrip.setMinimumWidth(slidingTabStrip.getMeasuredWidth());
        });
        //隐藏指示器
        mTabChannel.setSelectedTabIndicatorHeight(0);

        mVpContent.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //当页签切换的时候，如果有播放视频，则释放资源
                JCVideoPlayer.releaseAllVideos();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public String getCurrentChannelCode(){
        int currentItem = mVpContent.getCurrentItem();
        return mChannelList.get(currentItem).id;
    }

    @Override
    public void loadData() {
        mPresenter.getVideoClass();
    }

    @Override
    public void onGetClassSuccess(List<Channel> channels) {
        mChannelList.addAll(channels);
        initChannelFragments();
        initListener();
    }

    @Override
    public void onError() {

    }
}
