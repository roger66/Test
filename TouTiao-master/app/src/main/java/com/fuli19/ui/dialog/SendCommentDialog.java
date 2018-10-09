package com.fuli19.ui.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.fuli19.R;
import com.fuli19.api.ApiRetrofit;
import com.fuli19.api.ApiService;
import com.fuli19.app.MyApp;
import com.fuli19.model.response.ResultResponse;
import com.fuli19.utils.UIUtils;
import com.maning.mndialoglibrary.MProgressDialog;
import com.maning.mndialoglibrary.MStatusDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.shaohui.bottomdialog.BaseBottomDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendCommentDialog extends BaseBottomDialog {

    @BindView(R.id.send_comment_edit)
    EditText mCommentEdit;

    @BindView(R.id.send_comment_confirm)
    TextView mConfirmBtn;

    private String commentId; //上级评论
    private String newsId; //内容id
    private ApiService mApiService = ApiRetrofit.getInstance().getApiService();

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_send_comment;
    }

    @Override
    public void bindView(View v) {
        ButterKnife.bind(this, v);
        mCommentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mConfirmBtn.setEnabled(editable.length() > 0);
            }
        });
        mCommentEdit.post(() -> {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.showSoftInput(mCommentEdit, 0);
        });
    }


    @OnClick(R.id.send_comment_confirm)
    public void onClick(View view) {
        MProgressDialog.showProgress(getContext(),"发送中...");
        String comment = mCommentEdit.getText().toString();
        Call<ResultResponse> commentCall = mApiService.sendComment(newsId, MyApp.getKey(),
                comment, commentId);
        commentCall.enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                ResultResponse body = response.body();
                if (body.r == 1) {
                    MProgressDialog.dismissProgress();
                    new MStatusDialog(getContext()).show("发表成功，请等待审核", getContext().getResources().getDrawable(R.drawable.dialog_ok));
                    mCommentEdit.setText("");
                    dismiss();
                } else UIUtils.showToast(body.msg);
            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {
                MProgressDialog.dismissProgress();
                UIUtils.showToast("评论失败");
            }
        });
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }
}
