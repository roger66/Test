package com.fuli19.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;

import com.fuli19.constants.Constant;

import java.io.File;

public class SysPhotoCropper {

    /**
     * 默认大小 PX单位
     */
    private int mDefaultSize = 100;
    private PhotoCropCallBack mPhotoCropCallBack;
    private Activity mActivity;

    /**
     * 请求相机
     */
    private static final int REQUEST_TYPE_CAMERA = 311;
    /**
     * 请求图库
     */
    private static final int REQUEST_TYPE_GALLERY = 312;
    /**
     * 剪裁
     */
    private static final int REQUEST_TYPE_CROP = 313;

    /**
     * 临时照片文件名称
     */
    private static final String TMP_FILE = "default_photo_crop_tmp.jpg";
    private Uri mDefaultOutPutUri;

    private SysPhotoCropper() {
    }


    public SysPhotoCropper(Activity activity, PhotoCropCallBack cropCallBack) {
        this.mActivity = activity;
        this.mPhotoCropCallBack = cropCallBack;
        init();
    }

    private void init() {
        File tmpFile = new File(Constant.BASE_DIR, TMP_FILE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        mDefaultOutPutUri = Uri.fromFile(tmpFile);
    }

    /**
     * 设置剪裁大小
     *
     * @param cropSize
     */
    public void setCropSize(int cropSize) {
        this.mDefaultSize = cropSize;
    }

    public void setOutPutUri(Uri uri) {
        this.mDefaultOutPutUri = uri;
    }


    /**
     * 相机拍照剪裁
     */
    public void cropForCamera() {
        if (!Environment.getExternalStorageState().equalsIgnoreCase(
                Environment.MEDIA_MOUNTED)) {
            mPhotoCropCallBack.onFailed("sdcard not found");
            return;
        }
        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mDefaultOutPutUri);
        mActivity.startActivityForResult(cameraIntent, REQUEST_TYPE_CAMERA);
    }

    /**
     * 图库选择剪裁
     */
    public void cropForGallery() {
        try {
            Intent intent = getPhotoPickIntent();
            mActivity.startActivityForResult(intent, REQUEST_TYPE_GALLERY);
        } catch (ActivityNotFoundException e) {
            mPhotoCropCallBack.onFailed("Gallery not found");
        }
    }

    private Intent getPhotoPickIntent() {
        return new Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    private Intent getCropImageIntent(Uri photoUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", mDefaultSize);
        intent.putExtra("outputY", mDefaultSize);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mDefaultOutPutUri);
        return intent;
    }

    /**
     * 进行剪裁
     *
     * @param uri 输入
     */
    private void doCropPhoto(Uri uri) {
        try {
            if (uri == null)
                uri = mDefaultOutPutUri;
            final Intent intent = getCropImageIntent(uri);
            mActivity.startActivityForResult(intent, REQUEST_TYPE_CROP);
        } catch (Exception e) {
            mPhotoCropCallBack.onFailed("cannot crop image");
        }
    }

    /**
     * 处理OnActivtyResult
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_TYPE_CAMERA:
                doCropPhoto(null);
                break;
            case REQUEST_TYPE_GALLERY:
                Uri imageUri = data.getData();
                doCropPhoto(imageUri);
                break;
            case REQUEST_TYPE_CROP:
                mPhotoCropCallBack.onPhotoCropped(mDefaultOutPutUri);
                break;
        }
    }

    public interface PhotoCropCallBack {
        void onFailed(String message);

        void onPhotoCropped(Uri uri);
    }

}
