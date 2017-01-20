package com.keni.hankkija;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keni on 2017-01-04.
 */

public class StatFragment extends Fragment implements SearchView.OnQueryTextListener, SearchView.OnSuggestionListener{
    private TextView etsiTeksti, valitseTeksti, teksti11, teksti21, teksti31, nimi;
    private Spinner valitseAjanJakso;
    private SearchView etsiHankkija;
    private Hankkijat hankkijat= Hankkijat.getInstance();
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "hankkijaPref";
    private SuggestionsDatabase database;
    private DatabaseReference mDatabase, mFirebaseRef;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        database = hankkijat.getDatabase();
        View view = inflater.inflate(R.layout.stat_layout, container, false);
        etsiTeksti = (TextView) view.findViewById(R.id.text1);
        valitseTeksti = (TextView) view.findViewById(R.id.text2);
        etsiHankkija = (SearchView)  view.findViewById(R.id.etsiHankkija);
        valitseAjanJakso = (Spinner)  view.findViewById(R.id.ajanJaksoSpinner);
        teksti11 = (TextView) view.findViewById(R.id.teksti11);
        teksti21 = (TextView) view.findViewById(R.id.teksti21);
        teksti31 = (TextView) view.findViewById(R.id.teksti31);
        nimi = (TextView) view.findViewById(R.id.nimi);
        etsiHankkija.setOnQueryTextListener(this);
        etsiHankkija.setOnSuggestionListener(this);

        int idText = etsiHankkija.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        int idMag = etsiHankkija.getContext().getResources().getIdentifier("android:id/search_mag_icon", null, null);
        int idClose = etsiHankkija.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);//search_close_btn tai search_close_icon
        TextView textView = (TextView) etsiHankkija.findViewById(idText);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryText));
        textView.setHintTextColor(ContextCompat.getColor(getContext(), R.color.primaryText));
        ImageView searchClose = (ImageView) etsiHankkija.findViewById(idClose);
        searchClose.setColorFilter(R.color.primaryText);




       valitseAjanJakso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           public void onItemSelected(AdapterView<?> parent, View view,
                                      int position, long id) {
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });
        valitseAjanJakso.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.primaryText), PorterDuff.Mode.SRC_ATOP);
        List<String> vaihtoehdot= new ArrayList<String>();
        vaihtoehdot.add("Koko historia");
        vaihtoehdot.add("Vuosi");
        vaihtoehdot.add("Kuukausi");
        vaihtoehdot.add("Vikko");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, vaihtoehdot);
        valitseAjanJakso.setAdapter(dataAdapter);

        String hankkijaId = sharedpreferences.getString("id", null);

        update(hankkijaId);
        //nimi.setText(hankkijat.getHankkijaLista().get(Integer.parseInt(hankkijaId)).getHankkijaNimi());
        //teksti11.setText(hankkijat.getHankkijaLista().get(Integer.parseInt(hankkijaId)).getKellotukset().size()+"");
        //teksti21.setText(hankkijat.getKeskiarvo(Integer.parseInt(hankkijaId))+"");
        //teksti31.setText(hankkijat.getYleisinTulos(Integer.parseInt(hankkijaId))+"");
        //System.out.println("testi: "+hankkijat.getHankkijaLista().get(Integer.parseInt(hankkijaId)).getKellotukset());




        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        Cursor cursor = database.getSuggestions(s);
        if (cursor.getCount() != 0) {
            String[] columns = new String[]{SuggestionsDatabase.HANKKIJA_NIMI};
            int[] columnTextId = new int[]{android.R.id.text1};

            SuggestionSimpleCursorAdapter simple = new SuggestionSimpleCursorAdapter(getContext(),
                    android.R.layout.simple_list_item_1, cursor,
                    columns, columnTextId
                    , 0);

            etsiHankkija.setSuggestionsAdapter(simple);
            return true;
        } else {

            return false;
        }
    }

    @Override
    public boolean onSuggestionSelect(int i) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int i) {

        SQLiteCursor cursor = (SQLiteCursor) etsiHankkija.getSuggestionsAdapter().getItem(i);
        int indexColumnSuggestion = cursor.getColumnIndex(SuggestionsDatabase.HANKKIJA_NIMI);
        int nimiId = cursor.getColumnIndex(SuggestionsDatabase.HANKKIJA_ID);
        update(cursor.getString(nimiId));
        etsiHankkija.setQuery("", false);




        return true;
    }


    public void update(String nimiId) {
        nimi.setText(hankkijat.getHankkijaLista().get(Integer.parseInt(nimiId)).getHankkijaNimi());
        teksti11.setText(hankkijat.getHankkijaLista().get(Integer.parseInt(nimiId)).getKellotustenMaara()+"");
        teksti21.setText(hankkijat.getKeskiarvo(Integer.parseInt(nimiId))+"");
        teksti31.setText(hankkijat.getYleisinTulos(Integer.parseInt(nimiId))+"");

    }
}
