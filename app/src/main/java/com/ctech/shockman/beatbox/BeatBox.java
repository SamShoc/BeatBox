package com.ctech.shockman.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {

    public static final String TAG = "BeatBox";
    public static final String SOUNDS_FOLDER = "sample_sounds";
    public static final int MAX_SOUNDS = 5;

    private AssetManager mAssets;
    private List<Sound> mSoundList = new ArrayList<>();
    private SoundPool mSoundPool;


    public BeatBox(Context context) {
        mAssets = context.getAssets();
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        loadSounds();
    }

    public void play(Sound sound) {
        Integer soundId = sound.getSoundId();
        if (soundId == null) {
            return;
        }
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    private void loadSounds() {
        String[] soundNames;
        try {
            soundNames = mAssets.list(SOUNDS_FOLDER);
            Log.i(TAG, "Found " + soundNames.length + " sounds.");
        } catch (IOException ioe) {
            Log.e(TAG, "Could not list assets in " + SOUNDS_FOLDER, ioe);
            return;
        }

        for (String filename : soundNames) {
            try {
                String assetPath = SOUNDS_FOLDER + "/" + filename;
                Sound mySound = new Sound(assetPath);
                load(mySound);
                mSoundList.add(mySound);
            } catch (IOException ioe) {
                Log.e(TAG, "Could not load sound from file: " + filename, ioe);
            }
        }
    }

    private void load (Sound sound) throws IOException {
        AssetFileDescriptor afd = mAssets.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd, 1);
        sound.setSoundId(soundId);
    }

    public List<Sound> getSoundList () {
        return mSoundList;
    }

    public void release() {
        mSoundPool.release();;
    }
}

