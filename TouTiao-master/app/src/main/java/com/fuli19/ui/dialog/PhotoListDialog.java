package com.fuli19.ui.dialog;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fuli19.R;
import com.fuli19.media.AlbumFile;
import com.fuli19.media.AlbumFolder;
import com.fuli19.media.MediaReadTask;
import com.fuli19.media.MediaReader;
import com.fuli19.ui.adapter.PhotoListAdapter;
import com.fuli19.ui.view.GridItemDecoration;
import com.fuli19.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.shaohui.bottomdialog.BaseBottomDialog;

public class PhotoListDialog extends BaseBottomDialog {

    @BindView(R.id.dialog_photo_list_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.dialog_photo_list_finish)
    TextView mFinishBtn;

    private PhotoListAdapter mPhotoAdapter;
    private OnPhotoSelectedListener onPhotoSelectedListener;
    private List<AlbumFile> mSelectedData = new ArrayList<>();

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_photo_list;
    }

    @Override
    public void bindView(View v) {
        ButterKnife.bind(this,v);
        mSelectedData.clear();
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        mRecyclerView.addItemDecoration(new GridItemDecoration(getContext().getResources().getDimensionPixelSize(R.dimen.dp_2),4));
        mPhotoAdapter = new PhotoListAdapter();
        mRecyclerView.setAdapter(mPhotoAdapter);
        mPhotoAdapter.setOnItemClickListener((adapter, view, i) -> {
            AlbumFile file = (AlbumFile) adapter.getData().get(i);
            file.setChecked(!file.isChecked());
            mPhotoAdapter.notifyItemChanged(i);
            if (file.isChecked()){
                mSelectedData.add(file);
            }else {
                if (mSelectedData.contains(file))
                    mSelectedData.remove(file);
            }
            mFinishBtn.setEnabled(!mSelectedData.isEmpty());
        });
        getAllImage();
    }

    public void getAllImage(){
        new MediaReadTask(UIUtils.getContext(), MediaReader.FUNCTION_CHOICE_IMAGE, albumFolders -> {
            List<AlbumFile> files = new ArrayList<>();
            for (AlbumFolder folder : albumFolders)
                files.addAll(folder.getAlbumFiles());
            mPhotoAdapter.setNewData(files);
        }).execute();
    }

    @OnClick({R.id.dialog_photo_list_finish,R.id.dialog_photo_list_close})
    public void onClick(View view){
        if (view.getId() == R.id.dialog_photo_list_finish){
            if (onPhotoSelectedListener!=null)
                onPhotoSelectedListener.onPhotoSelected(mSelectedData);
        }
        dismiss();
    }


    @Override
    public int getHeight() {
        return getContext().getResources().getDisplayMetrics().heightPixels;
    }

    public void setOnPhotoSelectedListener(OnPhotoSelectedListener onPhotoSelectedListener) {
        this.onPhotoSelectedListener = onPhotoSelectedListener;
    }

    public interface OnPhotoSelectedListener {
        void onPhotoSelected(List<AlbumFile> files);
    }

}
