package com.fuli19.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fuli19.R;

import java.util.List;

public class SearchHistoryAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SearchHistoryAdapter(List<String> data) {
        super(R.layout.item_search_history,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, String text) {
        holder.setText(R.id.item_search_history_text, text);
    }
}
