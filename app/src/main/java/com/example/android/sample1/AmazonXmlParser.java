package com.example.android.sample1;

import android.util.Log;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AmazonXmlParser{
    private static final String LOG_TAG = AmazonXmlParser.class.getSimpleName();
    private static final String ns = null;
    public List<Entry> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            try {
                in.close();
            }
            catch(IOException e){
            }
        }
    }
    private List<Entry> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Entry> entries = new ArrayList<Entry>();
        parser.require(XmlPullParser.START_TAG, ns, "ItemSearchResponse");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("Items")) {
                entries = readItems(parser);
            } else {
                skip(parser);
            }
        }
        return entries;
    }


    private List<Entry> readItems(XmlPullParser parser) throws  XmlPullParserException,IOException{
        List<Entry> entries = new ArrayList<Entry>();
        parser.require(XmlPullParser.START_TAG, ns, "Items");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("Item")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }


    public static class Entry {
        public  String url;
        public  String title;

        private Entry(String mUrl,String mTitle) {
           url = mUrl;
            title = mTitle;
        }

        public String getUrl(){
            return url;
        }
        public String getTitle(){
            return title;
        }
    }

    private Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Item");
        String smallImage = null;
        String ItemAttribute = null;
        String name = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
                name = parser.getName();
                 if (name.equals("SmallImage")) {
                    smallImage = readsmallImage(parser);
                } else if (name.equals("ItemAttributes")) {
                    ItemAttribute = readItemAttribute(parser);
                } else {
                    skip(parser);
                }
            }


        return new Entry(smallImage,ItemAttribute);
    }

    private String readsmallImage(XmlPullParser parser) throws XmlPullParserException,IOException{
        parser.require(XmlPullParser.START_TAG, ns, "SmallImage");
        String url = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("URL")) {
                url = readLink(parser);
            }
            else
            {
                skip(parser);
            }
        }
            return url;
    }

    private String readItemAttribute(XmlPullParser parser) throws XmlPullParserException,IOException{
        parser.require(XmlPullParser.START_TAG, ns, "ItemAttributes");
        String url = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("Brand")) {
                url = readTitle(parser);
            }
            else
            {
                skip(parser);
            }
        }
        return url;
    }


    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Brand");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Brand");
        return title;
    }
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "URL");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "URL");
        return title;
    }
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
