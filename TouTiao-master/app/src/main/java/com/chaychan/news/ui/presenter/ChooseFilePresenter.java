package com.chaychan.news.ui.presenter;

import android.content.Context;

import com.chaychan.news.media.AlbumFile;
import com.chaychan.news.media.AlbumFolder;
import com.chaychan.news.media.MediaReadTask;
import com.chaychan.news.media.MediaReader;
import com.chaychan.news.ui.base.BasePresenter;
import com.chaychan.news.utils.UIUtils;
import com.chaychan.news.view.IChooseView;

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

}
