package com.fuli19.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fuli19.R;
import com.fuli19.model.entity.News;
import com.fuli19.model.entity.SearchMatchedData;
import com.fuli19.ui.adapter.SearchHistoryAdapter;
import com.fuli19.ui.adapter.SearchMatchedAdapter;
import com.fuli19.ui.adapter.SearchPagerAdapter;
import com.fuli19.ui.base.BaseActivity;
import com.fuli19.ui.fragment.SearchFragment;
import com.fuli19.ui.presenter.SearchPresenter;
import com.fuli19.utils.KeyboardHelper;
import com.fuli19.utils.PreUtils;
import com.fuli19.utils.UIUtils;
import com.fuli19.view.ISearchView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import flyn.Eyes;

public class SearchActivity extends BaseActivity<SearchPresenter> implements ISearchView {

    private static final String HISTORY = "history";

    @BindView(R.id.search_edit)
    EditText mEditText;

    @BindView(R.id.search_edit_delete)
    ImageView mEditDeleteBtn;

    @BindView(R.id.search_matched_rv)
    RecyclerView mMatchedRv;

    @BindView(R.id.search_history_rv)
    RecyclerView mHistoryRv;

    @BindView(R.id.search_tab)
    TabLayout mTab;

    @BindView(R.id.search_vp)
    ViewPager mVp;

    @BindView(R.id.search_history_bg)
    RelativeLayout mHistoryBg;

    private StringBuilder mStringBuilder;
    private List<String> mMatchedData = new ArrayList<>();
    private List<String> mHistoryData = new ArrayList<>();
    private SearchMatchedAdapter mMatchedAdapter;
    private SearchHistoryAdapter mHistoryAdapter;
    private SearchPagerAdapter mPagerAdapter;

    private AlertDialog mDeleteDialog;

    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter(this);
    }

    @Override
    public void initView() {
        Eyes.setStatusBarColor(this, android.R.color.black);
        mEditText.addTextChangedListener(mTextWatcher);
        mEditText.setOnEditorActionListener(mOnEditorActionListener);
        initRv();
        initDeleteDialog();
    }

    private void initDeleteDialog() {
        mDeleteDialog = new AlertDialog.Builder(this).setPositiveButton("确定", (dialogInterface,
                                                                               i) -> {
            PreUtils.putString(HISTORY, "");
            mHistoryData.clear();
            mHistoryAdapter.notifyDataSetChanged();
            mHistoryBg.setVisibility(View.GONE);
        }).setNegativeButton("取消", null).setMessage("是否清除全部搜索历史").create();
    }

    private void initRv() {
        mMatchedAdapter = new SearchMatchedAdapter();
        mMatchedAdapter.setOnItemClickListener((adapter, view, i) -> showTab(mMatchedData.get(i)));
        mMatchedRv.setLayoutManager(new LinearLayoutManager(this));
        mMatchedRv.setAdapter(mMatchedAdapter);

        mHistoryAdapter = new SearchHistoryAdapter(mHistoryData);
        mHistoryAdapter.setOnItemClickListener((adapter, view, i) -> {
            String key = mHistoryData.get(i);
            mEditText.setText(key);
            mEditText.setSelection(key.length());
            showTab(key);
        });
        mHistoryRv.setLayoutManager(new GridLayoutManager(this, 2));
        mHistoryRv.setAdapter(mHistoryAdapter);
    }

    private void showTab(String word) {
        mMatchedAdapter.setNewData(null);
        if (!mHistoryData.contains(word)) {
            mHistoryAdapter.addData(0, word);
            mStringBuilder.append(word).append(",");
            PreUtils.putString(HISTORY, mStringBuilder.toString());
        }
        mTab.setVisibility(View.VISIBLE);
        mVp.setVisibility(View.VISIBLE);
        mPagerAdapter = new SearchPagerAdapter(getSupportFragmentManager(), word);
        mVp.setAdapter(mPagerAdapter);
        mVp.setOffscreenPageLimit(4);
        mTab.setupWithViewPager(mVp);
        UIUtils.reflex(mTab);
        mMatchedRv.setVisibility(View.GONE);
        mHistoryBg.setVisibility(View.GONE);
        KeyboardHelper.getInstance().hideKeyBoard(mEditText);
    }

    @Override
    public void initData() {
        String history = PreUtils.getString(HISTORY, "");
        if (!TextUtils.isEmpty(history)) {
            mHistoryBg.setVisibility(View.VISIBLE);
            for (String word : history.split(","))
                mHistoryData.add(word);
            Collections.reverse(mHistoryData);
            mHistoryAdapter.notifyDataSetChanged();
        }
        mStringBuilder = new StringBuilder(history);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_search;
    }

    @Override
    public void onGetSearchMatchedSuccess(List<List<SearchMatchedData>> matchedData) {
        mHistoryBg.setVisibility(View.GONE);
        mMatchedAdapter.setNewData(matchedData);
        mMatchedData.clear();
        for (List<SearchMatchedData> matchedDataList : matchedData) {
            StringBuilder builder = new StringBuilder();
            for (SearchMatchedData data : matchedDataList)
                builder.append(data.content);
            mMatchedData.add(builder.toString());
        }
    }

    @Override
    public void onGetSearchResultSuccess(List<News> resultData) {

    }

    @Override
    public void onSearchDataEmpty() {

    }

    @Override
    public void onMatchedDataEmpty() {
        mMatchedRv.setVisibility(View.GONE);

    }

    //EditText按键监听
    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView
            .OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                String word = mEditText.getText().toString();
                showTab(word);
            }
            return false;
        }
    };

    //EditText输入监听
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            mPresenter.searchMatched(editable.toString());
            mMatchedRv.setVisibility(editable.length() > 0 ? View.VISIBLE : View.GONE);
            mEditDeleteBtn.setVisibility(editable.length() > 0 ? View.VISIBLE : View.GONE);
            if (editable.length() == 0) {
                if (!mHistoryData.isEmpty())
                    mHistoryBg.setVisibility(View.VISIBLE);
                mTab.setVisibility(View.GONE);
                mVp.setVisibility(View.GONE);
            }
        }
    };

    @OnClick({R.id.search_cancel, R.id.search_edit_delete, R.id.search_history_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_cancel:
                finish();
                break;
            case R.id.search_edit_delete:
                mEditText.setText("");
                break;
            case R.id.search_history_delete:
                mDeleteDialog.show();
                break;
        }
    }

}
