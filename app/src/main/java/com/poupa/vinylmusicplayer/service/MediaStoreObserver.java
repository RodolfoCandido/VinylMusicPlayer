package com.poupa.vinylmusicplayer.service;

import android.database.ContentObserver;
import android.os.Handler;

public class MediaStoreObserver extends ContentObserver implements Runnable {
    // milliseconds to delay before calling refresh to aggregate events
    private static final long REFRESH_DELAY = 50;
    private Handler mHandler;
    private MusicService mMusicService;

    public MediaStoreObserver(MusicService musicService, Handler handler) {
        super(handler);
        mHandler = handler;
        mMusicService = musicService;
    }

    @Override
    public void onChange(boolean selfChange) {
        // if a change is detected, remove any scheduled callback
        // then post a new one. This is intended to prevent closely
        // spaced events from generating multiple refresh calls
        mHandler.removeCallbacks(this);
        mHandler.postDelayed(this, REFRESH_DELAY);
    }

    // TODO Implement onChange(boolean selfChange, Uri uri) for a finer handling

    @Override
    public void run() {
        // actually call refresh when the delayed callback fires
        // do not send a sticky broadcast here
        mMusicService.handleAndSendChangeInternal(MusicService.MEDIA_STORE_CHANGED);
    }
}