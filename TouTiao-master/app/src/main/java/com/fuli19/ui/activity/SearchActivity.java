package com.fuli19.ui.activity;

import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.fuli19.R;
import com.fuli19.model.entity.SearchMatchedData;
import com.fuli19.ui.base.BaseActivity;
import com.fuli19.ui.presenter.SearchPresenter;
import com.fuli19.utils.SpannableStringUtils;
import com.fuli19.view.ISearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import flyn.Eyes;

public class SearchActivity extends BaseActivity<SearchPresenter> implements ISearchView {

    @BindView(R.id.search_edit)
    EditText mEditText;

    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter(this);
    }

    @Override
    public void initView() {
        Eyes.setStatusBarColor(this, android.R.color.black);
        mEditText.addTextChangedListener(mTextWatcher);
    }

    @Override
    public void initData() {
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_search;
    }

    @Override
    public void onGetSearchMatchedSuccess(List<List<SearchMatchedData>> matchedData) {
        for (List<SearchMatchedData> matchedDatum : matchedData) {
            for (SearchMatchedData data : matchedDatum) {
                System.out.println("---------------- "+data.content);
            }
        }
    }

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
        }
    };

    @OnClick({R.id.search_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_cancel:
                finish();
                break;
        }
    }

}
