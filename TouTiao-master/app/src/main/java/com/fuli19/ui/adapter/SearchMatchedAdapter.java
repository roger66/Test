package com.fuli19.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fuli19.R;
import com.fuli19.model.entity.SearchMatchedData;
import com.fuli19.utils.SpannableStringUtils;

import java.util.List;

public class SearchMatchedAdapter extends BaseQuickAdapter<List<SearchMatchedData>,BaseViewHolder>{

    public SearchMatchedAdapter() {
        super(R.layout.item_search_matched);
    }

    @Override
    protected void convert(BaseViewHolder holder, List<SearchMatchedData> searchMatchedData) {
        SpannableStringUtils.Builder builder = SpannableStringUtils.getBuilder("");
        for (SearchMatchedData data : searchMatchedData) {
            builder.append(data.content);
            if (data.type==1)
                builder.setForegroundColor(mContext.getResources().getColor(R.color.colorAccent));
        }
        holder.setText(R.id.item_search_matched_text,builder.create());
    }
}
