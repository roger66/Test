package com.fuli19.ui.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.Date;

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

    private void showDateDialog() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = "";
            if (TextUtils.isEmpty(mUser.birthday))
                format = dateFormat.format(new Date());
            else format = mUser.birthday;
            String[] split = format.split("-");
            int year = Integer.parseInt(split[0]);
            int month = Integer.parseInt(split[1]);
            int day = Integer.parseInt(split[2]);

            new DatePickerDialog(this, (datePicker, y, m, d) -> {
                String date = y+"-"+(m+1)+"-"+d;
                MProgressDialog.showProgress(this,"修改中...");
                mPresenter.editProfile("birthday",date);
                mUser.birthday = date;
            }, year, month - 1, day).show();
        }catch (Exception e){e.printStackTrace();}

    }


    @Subscribe(sticky = true)
    public void onEvent(User mUser) {
        this.mUser = mUser;
        mEditDialog.setUser(mUser);
        setUserInfo(mUser);
        mSexDialog = new AlertDialog.Builder(this).setSingleChoiceItems(sexItems, mUser.sex, (dialogInterface,
                                                                                              i) -> {
            MProgressDialog.showProgress(EditProfileActivity.this,"修改中...");
            mPresenter.editProfile("sex",i+"");
            mUser.sex = i;
            mSexDialog.dismiss();
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
        UIUtils.showToast("修改失败");
        MProgressDialog.dismissProgress();
    }

    @OnClick({R.id.edit_profile_back, R.id.edit_profile_head_bg,R.id.edit_profile_sex_bg
            ,R.id.edit_profile_nickName_bg,R.id.edit_profile_introduce_bg,R.id.edit_profile_birthday_bg
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
            case R.id.edit_profile_birthday_bg:
                showDateDialog();
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
