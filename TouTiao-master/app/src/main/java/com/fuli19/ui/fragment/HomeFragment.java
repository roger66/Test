package com.fuli19.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fuli19.R;
import com.fuli19.constants.Constant;
import com.fuli19.listener.OnChannelListener;
import com.fuli19.model.entity.Channel;
import com.fuli19.ui.adapter.ChannelPagerAdapter;
import com.fuli19.ui.base.BaseFragment;
import com.fuli19.ui.presenter.HomePresenter;
import com.fuli19.utils.PreUtils;
import com.fuli19.utils.UIUtils;
import com.fuli19.view.IHomeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import me.weyye.library.colortrackview.ColorTrackTabLayout;

/**
 * @author ChayChan
 * @description: 首页fragment
 * @date 2017/6/12  21:47
 */

public class HomeFragment extends BaseFragment<HomePresenter> implements OnChannelListener, IHomeView {

    @BindView(R.id.tab_channel)
    ColorTrackTabLayout mTabChannel;

    @BindView(R.id.iv_operation)
    ImageView ivAddChannel;

    @BindView(R.id.vp_content)
    ViewPager mVpContent;

    private List<Channel> mSelectedChannels = new ArrayList<>();
    private List<Channel> mUnSelectedChannels = new ArrayList<>();
    private List<NewsListFragment> mChannelFragments = new ArrayList<>();
    private Gson mGson = new Gson();
    private ChannelPagerAdapter mChannelPagerAdapter;
    private String[] mChannelCodes;

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initData() {
        mPresenter.getTagList();
//        initChannelData();
//        initChannelFragments();
    }

    /**
     * 初始化已选频道和未选频道的数据
     */
    private void initChannelData() {
        String selectedChannelJson = PreUtils.getString(Constant.SELECTED_CHANNEL_JSON, "");
        String unselectChannel = PreUtils.getString(Constant.UNSELECTED_CHANNEL_JSON, "");

        if (TextUtils.isEmpty(selectedChannelJson) || TextUtils.isEmpty(unselectChannel)) {
            //本地没有title
            String[] channels = getResources().getStringArray(R.array.channel);
            String[] channelCodes = getResources().getStringArray(R.array.channel_code);
            //默认添加了全部频道
            for (int i = 0; i < channelCodes.length; i++) {
                String title = channels[i];
                String code = channelCodes[i];
                mSelectedChannels.add(new Channel(title, code));
            }

            selectedChannelJson = mGson.toJson(mSelectedChannels);//将集合转换成json字符串
            KLog.i("selectedChannelJson:" + selectedChannelJson);
//            PreUtils.putString(Constant.SELECTED_CHANNEL_JSON, selectedChannelJson);//保存到sp
        } else {
            //之前添加过
            List<Channel> selectedChannel = mGson.fromJson(selectedChannelJson, new TypeToken<List<Channel>>() {
            }.getType());
            List<Channel> unselectedChannel = mGson.fromJson(unselectChannel, new TypeToken<List<Channel>>() {
            }.getType());
            mSelectedChannels.addAll(selectedChannel);
            mUnSelectedChannels.addAll(unselectedChannel);
        }
    }

    /**
     * 初始化已选频道的fragment的集合
     */
    private void initChannelFragments() {
        KLog.e("initChannelFragments");
        mChannelCodes = getResources().getStringArray(R.array.channel_code);
        for (Channel channel : mSelectedChannels) {
            NewsListFragment newsFragment = new NewsListFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constant.CHANNEL_CODE, channel.id);
            bundle.putBoolean(Constant.IS_VIDEO_LIST, false);//是否是视频列表页
            newsFragment.setArguments(bundle);
            mChannelFragments.add(newsFragment);//添加到集合中
        }
    }

    @Override
    public void initListener() {
        mChannelPagerAdapter = new ChannelPagerAdapter(mChannelFragments, mSelectedChannels,getChildFragmentManager());
        mVpContent.setAdapter(mChannelPagerAdapter);
        mVpContent.setOffscreenPageLimit(mSelectedChannels.size());

        mTabChannel.setTabPaddingLeftAndRight(UIUtils.dip2Px(10), UIUtils.dip2Px(10));
        mTabChannel.setupWithViewPager(mVpContent);
        mTabChannel.post(() -> {
            //设置最小宽度，使其可以在滑动一部分距离
            ViewGroup slidingTabStrip = (ViewGroup) mTabChannel.getChildAt(0);
            slidingTabStrip.setMinimumWidth(slidingTabStrip.getMeasuredWidth() + ivAddChannel.getMeasuredWidth());
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

    @Override
    public void loadData() {

    }

    public String getCurrentChannelCode(){
        int currentItem = mVpContent.getCurrentItem();
        return mSelectedChannels.get(currentItem).id;
    }

    @OnClick({R.id.tv_search, R.id.iv_operation})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search:

                break;
            case R.id.iv_operation:
                ChannelDialogFragment dialogFragment = ChannelDialogFragment.newInstance(mSelectedChannels, mUnSelectedChannels);
                dialogFragment.setOnChannelListener(this);
                dialogFragment.show(getChildFragmentManager(), "CHANNEL");
                dialogFragment.setOnDismissListener(dialog -> {
                    mChannelPagerAdapter.notifyDataSetChanged();
                    mVpContent.setOffscreenPageLimit(mSelectedChannels.size());
                    mTabChannel.setCurrentItem(mTabChannel.getSelectedTabPosition());
                    ViewGroup slidingTabStrip = (ViewGroup) mTabChannel.getChildAt(0);
                    //注意：因为最开始设置了最小宽度，所以重新测量宽度的时候一定要先将最小宽度设置为0
                    slidingTabStrip.setMinimumWidth(0);
                    slidingTabStrip.measure(0, 0);
                    slidingTabStrip.setMinimumWidth(slidingTabStrip.getMeasuredWidth() + ivAddChannel.getMeasuredWidth());

                    //保存选中和未选中的channel
                    PreUtils.putString(Constant.SELECTED_CHANNEL_JSON, mGson.toJson(mSelectedChannels));
                    PreUtils.putString(Constant.UNSELECTED_CHANNEL_JSON, mGson.toJson(mUnSelectedChannels));
                });
                break;
        }
    }

    @Override
    public void onItemMove(int starPos, int endPos) {
        listMove(mSelectedChannels, starPos, endPos);
        listMove(mChannelFragments, starPos, endPos);
    }


    @Override
    public void onMoveToMyChannel(int starPos, int endPos) {
        //移动到我的频道
        Channel channel = mUnSelectedChannels.remove(starPos);
        mSelectedChannels.add(endPos, channel);

        NewsListFragment newsFragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.CHANNEL_CODE, channel.id);
        bundle.putBoolean(Constant.IS_VIDEO_LIST, channel.id.equals(mChannelCodes[1]));
        newsFragment.setArguments(bundle);
        mChannelFragments.add(newsFragment);
    }

    @Override
    public void onMoveToOtherChannel(int starPos, int endPos) {
        //移动到推荐频道
        mUnSelectedChannels.add(endPos, mSelectedChannels.remove(starPos));
        mChannelFragments.remove(starPos);
    }

    private void listMove(List datas, int starPos, int endPos) {
        Object o = datas.get(starPos);
        //先删除之前的位置
        datas.remove(starPos);
        //添加到现在的位置
        datas.add(endPos, o);
    }

    @Override
    public void onGetTagSuccess(List<Channel> tags) {
        for (Channel tag : tags)
            mSelectedChannels.add(new Channel(tag.type, tag.id));
        initChannelFragments();
        initListener();
    }

    @Override
    public void onError() {

    }
}
