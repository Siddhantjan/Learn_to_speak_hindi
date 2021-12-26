package com.personal.learn_to_speakhindi;

public class Word {

    private String mDefaultTranslation;
    private String mHindiTranslation;
    private int mImageResourceId = NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED = -1;

    private int mAudioResourceID;

    public Word(String mDefaultTranslation, String mHindiTranslation, int mAudioResourceID) {
        this.mDefaultTranslation = mDefaultTranslation;
        this.mHindiTranslation = mHindiTranslation;
        this.mAudioResourceID = mAudioResourceID;
    }

    public Word(String mDefaultTranslation, String mHindiTranslation, int mImageResourceId, int mAudioResourceID) {
        this.mDefaultTranslation = mDefaultTranslation;
        this.mHindiTranslation = mHindiTranslation;
        this.mImageResourceId = mImageResourceId;
        this.mAudioResourceID = mAudioResourceID;
    }


    public String getHindiTranslation() {
        return mHindiTranslation;
    }

    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    public int getAudioResourceID() {
        return mAudioResourceID;
    }

    public int getImageResourceId() {
        return mImageResourceId;
    }

    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }
}
