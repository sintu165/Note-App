package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class NoteDetailsActivity extends AppCompatActivity {

    FloatingActionButton addNewNoteBtn;
    RecyclerView noteDetailsRecyclerView;

    NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        addNewNoteBtn=findViewById(R.id.newNoteBtn);
        noteDetailsRecyclerView=findViewById(R.id.noteDetailsRecyclerview);
        addNewNoteBtn.setOnClickListener(view -> {
            startActivity(new Intent(NoteDetailsActivity.this, NewNoteActivity.class));
        });

        setUpRecyclerView();


    }

    private void setUpRecyclerView() {

        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        CollectionReference collectionReference=FirebaseFirestore.getInstance().collection("notes").document(currentUser.getUid()).collection("my_notes");
        Query query=collectionReference.orderBy( "notetitle", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>().setQuery(query, Note.class).build();
        noteDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(options,this);
      //  noteAdapter.notifyDataSetChanged();
        noteDetailsRecyclerView.setAdapter(noteAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }


}