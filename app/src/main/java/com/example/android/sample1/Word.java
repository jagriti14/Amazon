package com.example.android.sample1;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Dell on 23-07-2017.
 */

public class Word {
    private String mImageId;
    private String mName;

    public Word(String ImageId, String name) {
        mImageId = ImageId;
        mName = name;
    }

    public String getmImageId() {
        return mImageId;
    }

    public String getmName() {
        return mName;
    }

}
