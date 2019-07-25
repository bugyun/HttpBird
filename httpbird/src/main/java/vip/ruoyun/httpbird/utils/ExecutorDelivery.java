/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vip.ruoyun.httpbird.utils;

import android.os.Handler;
import android.text.TextUtils;

import java.io.File;
import java.util.concurrent.Executor;

import vip.ruoyun.httpbird.entities.FileInfo;
import vip.ruoyun.httpbird.interfaces.FileLoadingListener;

/**
 * Delivers responses and errors.
 */
public class ExecutorDelivery {
    public static final int MESSAGE_START = 0x1;//checkurl完成开始下载
    public static final int MESSAGE_UPDATE = 0x2;
    public static final int MESSAGE_PAUSE = 0x3;
    public static final int MESSAGE_SUCCESS = 0x4;
    public static final int MESSAGE_FAILURE = 0x5;
    public static final int MESSAGE_MOVE_LOCATION = 0x6;//重定向
//    private static final int MESSAGE_STOP = 0x6;

    /**
     * Used for posting responses, typically to the main thread.
     */
    private final Executor mResponsePoster;

    /**
     * Creates a new response delivery interface.
     *
     * @param handler {@link Handler} to post responses on
     */
    public ExecutorDelivery(final Handler handler) {
        //这样使用runnable的话，是可以重用runnable,就可以不用重新创建runnable
        mResponsePoster = new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
    }

    public void postResponse(int type, FileLoadingListener listener, FileInfo info) {
        mResponsePoster.execute(new ResponseDeliveryRunnable(type, listener, info));
    }

    /**
     * A Runnable used for delivering network responses to a listener on the
     * main thread.
     */
    @SuppressWarnings("rawtypes")
    private class ResponseDeliveryRunnable implements Runnable {

        private int type;
        private FileLoadingListener listener;
        private FileInfo info;

        public ResponseDeliveryRunnable(int type, FileLoadingListener listener, FileInfo info) {
            this.type = type;
            this.listener = listener;
            this.info = info;
        }

        @Override
        public void run() {
            switch (type) {
                case MESSAGE_START:
                    break;
                case MESSAGE_UPDATE:
                    listener.onProgressUpdate(info.getUrl(), info.getFinished(), info.getLength());
                    break;
                case MESSAGE_PAUSE:
                    listener.onLoadingCancelled(info.getUrl());
                    break;
                case MESSAGE_SUCCESS:
                    if (TextUtils.isEmpty(info.getFilePath())) {
                        listener.onLoadingComplete(info.getUrl(), null, info.getResponse());
                    } else {
                        listener.onLoadingComplete(info.getUrl(), new File(info.getFilePath(), info.getFileName()), "");
                    }
                    break;
                case MESSAGE_FAILURE:
                    listener.onLoadingFailed(info.getUrl(), info.getException());
                    break;
                case MESSAGE_MOVE_LOCATION:
                    break;
            }
        }
    }
}
