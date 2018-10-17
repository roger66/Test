package com.fuli19.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fuli19.R;
import com.fuli19.constants.Constant;
import com.fuli19.model.entity.User;
import com.fuli19.ui.base.BaseActivity;
import com.fuli19.ui.dialog.EditProfileDialog;
import com.fuli19.ui.presenter.EditProfilePresenter;
import com.fuli19.utils.GlideUtils;
import com.fuli19.utils.SysPhotoCropper;
import com.fuli19.utils.UIUtils;
import com.fuli19.view.IEditProfileView;
import com.maning.mndialoglibrary.MProgressBarDialog;
import com.maning.mndialoglibrary.MProgressDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import flyn.Eyes;

public class EditProfileActivity extends BaseActivity<EditProfilePresenter> implements
        IEditProfileView {

    private String [] picItems = {"从相册选择","拍照","取消"};
    private String [] sexItems = {"男","女"};

    @BindView(R.id.edit_profile_head)
    CircleImageView mHeadImg;

    @BindView(R.id.edit_profile_nickName)
    TextView mNickNameTv;

    @BindView(R.id.edit_profile_introduce)
    TextView mIntroduceTv;

    @BindView(R.id.edit_profile_sex)
    TextView mSexTv;

    @BindView(R.id.edit_profile_birthday)
    TextView mBirthdayTv;

    private User mUser;
    private SysPhotoCropper mPhotoCropper;
    private EditProfileDialog mEditDialog;
    private MProgressBarDialog mUploadDialog;
    private AlertDialog mPicDialog;
    private AlertDialog mSexDialog;

    @Override
    protected EditProfilePresenter createPresenter() {
        return new EditProfilePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_edit_profile;
    }

    @Override
    public void initView() {
        Eyes.setStatusBarColor(this, R.color.color_BDBDBD);
        mUploadDialog = new MProgressBarDialog.Builder(this).setStyle(MProgressBarDialog.MProgressBarDialogStyle_Horizontal).build();
        mEditDialog = new EditProfileDialog();
        mEditDialog.setPresenter(mPresenter);
        mPicDialog = new AlertDialog.Builder(this).setItems(picItems, (dialogInterface, i) -> {
            switch (i){
                case 0:
                    mPhotoCropper.cropForGallery();
                    break;
                case 1:
                    mPhotoCropper.cropForCamera();
                    break;
            }
        }).create();
    }

    @Override
    public void initData() {
        registerEventBus(this);
        mPhotoCropper = new SysPhotoCropper(this,mPhotoCropCallBack);
    }

    @Subscribe(sticky = true)
    public void onEvent(User mUser) {
        this.mUser = mUser;
        setUserInfo(mUser);
        mSexDialog = new AlertDialog.Builder(this).setSingleChoiceItems(sexItems, mUser.sex, (dialogInterface,
                                                                                              i) -> {
            System.out.println("------------------ " + sexItems[i]);
        }).setNegativeButton("取消",null).create();
    }

    private void setUserInfo(User mUser) {
        Glide.with(this).clear(mHeadImg);
        GlideUtils.load(this, mUser.portrait, mHeadImg);
        mNickNameTv.setText(mUser.nickname);
        mIntroduceTv.setText(TextUtils.isEmpty(mUser.introduce) ? "这个人很懒，什么都没留下" : mUser.introduce);
        mSexTv.setText(mUser.sex == 0 ? "男" : "女");
        mBirthdayTv.setText(TextUtils.isEmpty(mUser.birthday)?"待完善":mUser.birthday);
    }

    @Override
    public void onEditSuccess() {
        setUserInfo(mUser);
        UIUtils.showToast("修改成功");
        EventBus.getDefault().post(Constant.EDIT_SUCCESS);
        MProgressDialog.dismissProgress();
    }

    @Override
    public void onUploadProgress(int progress) {
        mUploadDialog.showProgress(progress,"正在上传 0%");
    }

    @Override
    public void onUploadSuccess(String url) {
        mUploadDialog.dismiss();
        MProgressDialog.showProgress(this,"修改中...");
        mPresenter.editProfile("portrait",url);
    }

    @Override
    public void onError() {
        MProgressDialog.dismissProgress();
    }

    @OnClick({R.id.edit_profile_back, R.id.edit_profile_head_bg,R.id.edit_profile_sex_bg
            ,R.id.edit_profile_nickName_bg,R.id.edit_profile_introduce_bg
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_profile_back:
                finish();
                break;
            case R.id.edit_profile_head_bg:
                AndPermission.with(this).runtime().permission(Permission.CAMERA, Permission
                        .WRITE_EXTERNAL_STORAGE)
                        .onGranted(data -> mPicDialog.show())
                        .onDenied(data -> UIUtils.showToast("没有权限")).start();
                break;
            case R.id.edit_profile_sex_bg:
                mSexDialog.show();
                break;
            case R.id.edit_profile_nickName_bg:
                mEditDialog.setType(EditProfileDialog.TYPE_NICKNAME);
                mEditDialog.show(getSupportFragmentManager());
                break;
            case R.id.edit_profile_introduce_bg:
                mEditDialog.setType(EditProfileDialog.TYPE_INTRODUCE);
                mEditDialog.show(getSupportFragmentManager());
                break;
        }
    }

     private SysPhotoCropper.PhotoCropCallBack mPhotoCropCallBack = new SysPhotoCropper.PhotoCropCallBack() {
        @Override
        public void onFailed(String message) {
            UIUtils.showToast(message);
        }

        @Override
        public void onPhotoCropped(Uri uri) {
            String path = uri.getEncodedPath();
            mUser.portrait = path;
            mUploadDialog.showProgress(0,"正在上传 0%");
            mPresenter.uploadPhoto(path);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoCropper.handleOnActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterEventBus(this);
    }

}
