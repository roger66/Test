package com.fuli19.ui.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.fuli19.R;
import com.fuli19.model.entity.User;
import com.fuli19.ui.presenter.EditProfilePresenter;
import com.fuli19.utils.UIUtils;
import com.maning.mndialoglibrary.MProgressDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.shaohui.bottomdialog.BaseBottomDialog;

public class EditProfileDialog extends BaseBottomDialog {

    public static final int TYPE_NICKNAME = 0;
    public static final int TYPE_INTRODUCE = 1;

    @BindView(R.id.dialog_edit_profile_et)
    EditText mProfileEdit;

    private int type;
    private User mUser;
    private EditProfilePresenter mPresenter;

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_edit_profile;
    }

    @Override
    public void bindView(View v) {
        ButterKnife.bind(this, v);

        mProfileEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mProfileEdit.post(() -> {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.showSoftInput(mProfileEdit, 0);
        });
    }


    @OnClick(R.id.dialog_edit_profile_confirm)
    public void onClick(View view) {
        String content = mProfileEdit.getText().toString();
        if (TextUtils.isEmpty(content)) {
            UIUtils.showToast("内容不能为空");
            return;
        }
        MProgressDialog.showProgress(getContext(), "修改中...");
        String name = type == TYPE_INTRODUCE ? "introduce" : "nickname";
        mPresenter.editProfile(name, content);
        if (type==TYPE_INTRODUCE)
            mUser.introduce = content;
        else mUser.nickname = content;
        dismiss();
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setPresenter(EditProfilePresenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    public void setUser(User mUser) {
        this.mUser = mUser;
    }
}
