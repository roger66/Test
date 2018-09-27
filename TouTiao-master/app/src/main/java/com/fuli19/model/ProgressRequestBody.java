package com.fuli19.model;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class ProgressRequestBody extends RequestBody {

    //实际的待包装请求体
    private final RequestBody requestBody;
    //进度回调接口
    private final UploadProgressListener progressListener;
    //包装完成的BufferedSink
    private BufferedSink bufferedSink;
    //每个RequestBody对应一个tag，存放在map中，保证计算的时候不会出现重复
    private String tag;
    //用于第二次实际写入的数据判断 显示progressListener
    private boolean mIsSecond;

    /**
     * 构造函数，赋值
     *
     */
    public ProgressRequestBody(File file, UploadProgressListener progressListener, String tag) {
        this.requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        this.progressListener = progressListener;
        this.tag = tag;
    }

    public ProgressRequestBody(RequestBody requestBody, UploadProgressListener progressListener,
                               String tag) {
        this.requestBody = requestBody;
        this.progressListener = progressListener;
        this.tag = tag;
    }

    /**
     * 重写调用实际的响应体的contentType
     *
     * @return MediaType
     */
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    /**
     * 重写调用实际的响应体的contentLength
     *
     * @return contentLength
     *
     * @throws IOException 异常
     */
    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    /**
     * 重写进行写入
     *
     * @param sink BufferedSink
     *
     * @throws IOException 异常
     */
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null || tag != null) {
            bufferedSink = Okio.buffer(sink(sink));
        }
        requestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
        mIsSecond = true;
    }

    /**
     * 写入，回调进度接口
     *
     * @param sink Sink
     *
     * @return Sink
     */
    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long writtenBytesCount = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long totalBytesCount = 0L;

            @Override
            public void write(Buffer source, final long byteCount) throws IOException {
                super.write(source, byteCount);
                //获得contentLength的值，后续不再调用
                if (totalBytesCount == 0) {
                    totalBytesCount = contentLength();
                }
                //增加当前写入的字节数
                writtenBytesCount += byteCount;
                if (mIsSecond) {
                    Observable.just(writtenBytesCount).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> progressListener.onProgress(writtenBytesCount,
                                    totalBytesCount));
                }
            }
        };
    }
    public interface UploadProgressListener {
        /**
         * 上传进度
         */
        void onProgress(long currentBytesCount, long totalBytesCount);
    }


}
