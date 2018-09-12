package com.chaychan.news.media;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

public class MediaReadTask extends AsyncTask<Void, Void, MediaReadTask.ResultWrapper> {

    public interface Callback {
        /**
         * Callback the results.
         *
         * @param albumFolders album folder list.
         */
        void onScanCallback(ArrayList<AlbumFolder> albumFolders);
    }

    static class ResultWrapper {
        private ArrayList<AlbumFolder> mAlbumFolders;
    }

    private int mFunction;
    private MediaReader mMediaReader;
    private Callback mCallback;

    public MediaReadTask(Context context,int function,  Callback callback) {
        this.mMediaReader = new MediaReader(context,null,null,null,false);
        this.mFunction = function;
        this.mCallback = callback;
    }

    @Override
    protected ResultWrapper doInBackground(Void... params) {
        ArrayList<AlbumFolder> albumFolders;
        switch (mFunction) {
            case MediaReader.FUNCTION_CHOICE_IMAGE: {
                albumFolders = mMediaReader.getAllImage();
                break;
            }
            case MediaReader.FUNCTION_CHOICE_VIDEO: {
                albumFolders = mMediaReader.getAllVideo();
                break;
            }
            case MediaReader.FUNCTION_CHOICE_ALBUM: {
                albumFolders = mMediaReader.getAllMedia();
                break;
            }
            default: {
                throw new AssertionError("This should not be the case.");
            }
        }

        ResultWrapper wrapper = new ResultWrapper();
        wrapper.mAlbumFolders = albumFolders;
        return wrapper;
    }

    @Override
    protected void onPostExecute(ResultWrapper wrapper) {
        mCallback.onScanCallback(wrapper.mAlbumFolders);
    }
}