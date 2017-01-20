package com.keni.hankkija;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;


/**
 * Created by Keni on 2016-12-28.
 */
public class SuggestionSimpleCursorAdapter extends SimpleCursorAdapter
{

    public SuggestionSimpleCursorAdapter(Context context, int layout, Cursor c,
                                         String[] from, int[] to) {
        super(context, layout, c, from, to);
    }

    public SuggestionSimpleCursorAdapter(Context context, int layout, Cursor c,
                                         String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public CharSequence convertToString(Cursor cursor) {

        int indexColumnSuggestion = cursor.getColumnIndex(SuggestionsDatabase.HANKKIJA_NIMI);

        return cursor.getString(indexColumnSuggestion);
    }


}