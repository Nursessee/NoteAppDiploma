package com.example.clientlist;

import android.os.Handler;
import android.os.Looper;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecuter {
    private static final Object LOCK = new Object();
    private static AppExecuter instanceExecuter;
    private final Executor discIO;
    private final Executor mainIO;
    private final Executor networkIO;

    public AppExecuter(Executor discIO, Executor mainIO, Executor networkIO) {
        this.discIO = discIO;
        this.mainIO = mainIO;
        this.networkIO = networkIO;
    }

    public static AppExecuter getInstance()
    {
        if (instanceExecuter == null)
        {
            synchronized (LOCK)
            {
                instanceExecuter= new AppExecuter(Executors.newSingleThreadExecutor(),
                        new MainThreadHandler(), Executors.newFixedThreadPool(3));
            }
        }
        return instanceExecuter;
    }

    private static class MainThreadHandler implements Executor
    {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NotNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

    public Executor getDiscIO() {
        return discIO;
    }

    public Executor getMainIO() {
        return mainIO;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }
}
