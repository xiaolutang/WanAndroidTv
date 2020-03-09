package com.txl.commonlibrary.utils.exector;/*
 * Copyright (C) 2017 The Android Open Source Project
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

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Global executor pools for the whole application.
 * <p>
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */
public class AppExecutors {

    private static final int THREAD_COUNT = 5;

    private final Executor diskIO;

    private final Executor networkIO;

    private final MainThreadExecutor mainThread;

    @VisibleForTesting
    AppExecutors(Executor diskIO, Executor networkIO, MainThreadExecutor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    private AppExecutors() {
        this(new DiskIOThreadExecutor(), Executors.newFixedThreadPool(THREAD_COUNT),
                new MainThreadExecutor());
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor networkIO() {
        return networkIO;
    }

    public MainThreadExecutor mainThread() {
        return mainThread;
    }

    public static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }

        public void executeDelay(@NonNull Runnable command, long ms){
            mainThreadHandler.postDelayed( command,ms );
        }
    }

    public static AppExecutors getInstance(){
        return AppExecutorsHolder.appExecutors;
    }

    private static class AppExecutorsHolder {
        static AppExecutors appExecutors = new AppExecutors();
    }

    public static void execDiskIo(Runnable runnable){
        getInstance().diskIO.execute(runnable);
    }

    public static void execNetIo(Runnable runnable){
        getInstance().networkIO.execute(runnable);
    }
    public static void execMainThread(Runnable runnable){
        getInstance().mainThread.execute(runnable);
    }
}