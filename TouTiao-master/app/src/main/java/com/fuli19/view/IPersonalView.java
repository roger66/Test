package com.fuli19.view;

import com.fuli19.model.entity.Dynamic;

public interface IPersonalView {

    void onGetDynamicSuccess(Dynamic dynamic);

    void onDataEmpty(String msg);

    void onError();

}
