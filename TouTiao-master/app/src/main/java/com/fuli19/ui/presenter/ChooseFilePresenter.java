package com.fuli19.ui.presenter;

import com.fuli19.media.AlbumFile;
import com.fuli19.media.AlbumFolder;
import com.fuli19.media.MediaReadTask;
import com.fuli19.media.MediaReader;
import com.fuli19.ui.base.BasePresenter;
import com.fuli19.utils.UIUtils;
import com.fuli19.view.IChooseView;

import java.util.ArrayList;
import java.util.List;

public class ChooseFilePresenter extends BasePresenter<IChooseView> {

    public ChooseFilePresenter(IChooseView view) {
        super(view);
    }

    public void getAllVideo(){
        new MediaReadTask(UIUtils.getContext(), MediaReader.FUNCTION_CHOICE_VIDEO, albumFolders -> {
            List<AlbumFile> files = new ArrayList<>();
            for (AlbumFolder folder : albumFolders)
                files.addAll(folder.getAlbumFiles());
            mView.onGetAlbumFileSuccess(files);
        }).execute();
    }

    public void getAllImage(){
        new MediaReadTask(UIUtils.getContext(), MediaReader.FUNCTION_CHOICE_IMAGE, albumFolders -> {
            List<AlbumFile> files = new ArrayList<>();
            for (AlbumFolder folder : albumFolders)
                files.addAll(folder.getAlbumFiles());
            mView.onGetAlbumFileSuccess(files);
        }).execute();
    }

}
