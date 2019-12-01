package com.md.gamepractical;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class Utils {

    private static Set<String> stopWords = new HashSet<String>();
    private static BufferedReader reader = null;
    private static Context context;
    private static final String STOPWORDS_FILE = "stopwords.txt";
    public static final String SOURCE = "Source";
    public static final String DEMO = "demo";
    public static final String EXPLORE = "explore";
    public static final String DEMO_SIMPLE = "Atoms(Simple)";
    public static final String DEMO_COMPLEX = "Leaves(Complex)";
    public static final String DEMO_SIMPLE_IMAGE = "atoms.png";
    public static final String DEMO_LARGE_IMAGE  = "leaves.png";
    public static final String DRAG_DROP  = "DragDrop";


    public Utils(Context context) {
        this.context = context;
        savestopWords();
    }

    public static boolean getStopWords(String word) {
        return stopWords.contains(word);
    }

    public static void savestopWords() {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(STOPWORDS_FILE)));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                stopWords.add(mLine.trim());
            }
        } catch (Exception e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
