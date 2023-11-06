package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.auth.SignInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class NewNoteActivity extends AppCompatActivity {

    TextInputEditText noteTitleTextField,noteDescriptionTextField;
    Button saveNoteBtn;

    TextView page_title,deleteTextViewBtn;
    boolean editmode=false;

    CollectionReference collectionReference;
    FirebaseUser currentUser;



    String title,description,docId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        noteTitleTextField=findViewById(R.id.noteTitleTextField);
        noteDescriptionTextField=findViewById(R.id.noteDescriptionTextField);
        saveNoteBtn =findViewById(R.id.saveNoteBtn);
        page_title=findViewById(R.id.page_title);
        deleteTextViewBtn=findViewById(R.id.deleteTextView);

        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        collectionReference=FirebaseFirestore.getInstance().collection("notes").document(currentUser.getUid()).collection("my_notes");


        //receive data from noteadapter if you wants to edit note
        title=getIntent().getStringExtra("title");
        description=getIntent().getStringExtra("description");
        docId=getIntent().getStringExtra("docId");

        if(docId!=null&&!docId.isEmpty()){
            editmode=true;
        }

        noteTitleTextField.setText(title);
        noteDescriptionTextField.setText(description);
        if(editmode){
        page_title.setText("Edit your note");
        deleteTextViewBtn.setVisibility(View.VISIBLE);
        }


        saveNoteBtn.setOnClickListener(view -> saveNote());
        deleteTextViewBtn.setOnClickListener(view -> deleteNoteFromFireStore());
    }

    private void deleteNoteFromFireStore() {
        DocumentReference documentReference;
            //update note
        documentReference=collectionReference.document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(NewNoteActivity.this, "delete", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewNoteActivity.this,NoteDetailsActivity.class));
                }
                else{
                    Toast.makeText(NewNoteActivity.this,"failed",Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void saveNote() {
        String noteTitle= Objects.requireNonNull(noteTitleTextField.getText()).toString();
        String noteDescription= Objects.requireNonNull(noteDescriptionTextField.getText()).toString();

        if(TextUtils.isEmpty(noteTitle)){
            noteTitleTextField.setError("enter");
            noteTitleTextField.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(noteDescription)){
            noteDescriptionTextField.setError("enter");
            noteDescriptionTextField.requestFocus();
            return;

        }

        Note note=new Note();
        note.setNotetitle(noteTitle);
        note.setNotedescription(noteDescription);

        saveNoteToFirebase(note);


    }

    private void saveNoteToFirebase(Note note) {

        DocumentReference documentReference;
        if(editmode){
            //update note
            documentReference=collectionReference.document(docId);
        }
        else{
            //create new note
        documentReference=collectionReference.document();
        }
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(NewNoteActivity.this, "uploaded", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewNoteActivity.this,NoteDetailsActivity.class));

                }
                else{
                    Toast.makeText(NewNoteActivity.this,"failed",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}