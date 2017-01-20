package com.keni.hankkija;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keni on 2017-01-16.
 */

public class LueFirebase  extends AsyncTask<String, String, String> {
    private Context context;
    private Hankkijat hankkijat = Hankkijat.getInstance();
    private Hankkija hankkija;
    private SuggestionsDatabase database;
    private DatabaseReference mDatabase;
    private ArrayList<Hankkija> hankkijatLista;

    public interface LoadingTaskFinishedListener {
        void onTaskFinished(); // If you want to pass something back to the listener add a param to this method
    }

    private LoadingTaskFinishedListener finishedListener = null;

    public LueFirebase(Context mContext, LoadingTaskFinishedListener finishedListener){
        context = mContext;
        hankkijat.addContext(context);
        database = hankkijat.getDatabase();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        hankkijatLista = new ArrayList<>();
        this.finishedListener = finishedListener;

    }
    @Override
    protected String doInBackground(String... strings) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("hankkijat");


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<Hankkija> hankkijatLista = new ArrayList<>();
                SuggestionsDatabase database = hankkijat.getDatabase();
                hankkijatLista.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Hankkija hankkija = postSnapshot.getValue(Hankkija.class);
                    hankkijatLista.add(hankkija);
                    database.insertSuggestion(hankkija.getHankkijaNimi(), hankkija.getOikeaNimi(), hankkija.getId()+"");
                    System.out.println(postSnapshot.getValue());
                }
                //System.out.println(hankkijatLista);
                hankkijat.setHankkijatLista(hankkijatLista);

                finishedListener.onTaskFinished();
                System.out.println("VALMIS");
                //new LueParsitutKellotukset(context).execute();





            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }


        });

        return null;
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        //Toast.makeText(context, "Database ladattu", Toast.LENGTH_SHORT).show();





    }
}
