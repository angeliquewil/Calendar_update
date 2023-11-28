package com.example.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;




public class SecondActivity extends AppCompatActivity {



    private static final String PREFS_NAME = "NotePrefs";
    private static final String KEY_NOTE_COUNT = "NoteCount";
    private LinearLayout notesContainer;
    private List<Note> noteList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondactivity);

        notesContainer = findViewById(R.id.notesContainer);
        Button saveButton = findViewById(R.id.saveButton);

        noteList = new ArrayList<>();

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                saveNote();
            }
        });

        loadNotesFromPreferences();
        displayNotes();
    }

    private void displayNotes() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String day = extras.getString("date");
            //The key argument here must match that used in the other activity

            for (Note note : noteList) {

                if(note.getDate().equals(day)){
                    createNoteView(note);

                }


            }

        }
    }

    //lets create a array of preferences for each date
    private void loadNotesFromPreferences() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String day = extras.getString("date");

            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            int noteCount = sharedPreferences.getInt(KEY_NOTE_COUNT, 0);

            for (int i = 0; i < noteCount; i++) {

                String title = sharedPreferences.getString("note_title_" + i, "");
                String content = sharedPreferences.getString("note_content_" + i, "");
                String Date = sharedPreferences.getString("note_date_" + i, "");


                Note note = new Note();
                note.setTitle(title);
                note.setContent(content);
                note.setDate(Date);

                noteList.add(note);

            }
        }
    }

    private void saveNote() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String day = extras.getString("date");
            //The key argument here must match that used in the other activity

            EditText titleEditText = findViewById(R.id.titleEditText);
            EditText contentEditText = findViewById(R.id.contentEditText);
            TextView DateEditText = findViewById(R.id.displayDate);

            DateEditText.setText(day);


            String title = titleEditText.getText().toString();
            String content = contentEditText.getText().toString();

            if (!content.isEmpty() && !title.isEmpty()) {

                Note note = new Note();

                note.setTitle(title);
                note.setContent(content);
                note.setDate(day);

                noteList.add(note);
                saveNoteToPreferences();
                createNoteView(note);
                clearInputFields();

                startActivity(new Intent(SecondActivity.this, MainActivity.class));
            }
        }

    }

    private void clearInputFields() {

        EditText titleEditText = findViewById(R.id.titleEditText);
        EditText contentEditText = findViewById(R.id.contentEditText);
        TextView DateEditText = findViewById(R.id.displayDate);


        titleEditText.getText().clear();
        contentEditText.getText().clear();

    }

    private void createNoteView(final Note note) {

        View noteView = getLayoutInflater().inflate(R.layout.note_item, null);
        TextView titleTextView = noteView.findViewById(R.id.titleTextView);
        TextView contentTextView = noteView.findViewById(R.id.contentTextView);
        TextView DateEditText = findViewById(R.id.displayDate);


        titleTextView.setText(note.getTitle());
        contentTextView.setText(note.getContent());
        DateEditText.setText(note.getDate());
        notesContainer.addView(noteView);
        noteView.setClickable(true);
        noteView.setLongClickable(true);
        noteView.setLongClickable(true);
        noteView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                deleteNoteAndRefresh(note);
                return true;

            }
        });

    }

    private void deleteNoteAndRefresh(Note note) {

        noteList.remove(note);
        saveNoteToPreferences();
        refreshNotesView();
    }

    private void refreshNotesView() {

        notesContainer.removeAllViews();
        displayNotes();
    }


    private void saveNoteToPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_NOTE_COUNT, noteList.size());
        for(int i = 0; i < noteList.size(); i++){

            Note note = noteList.get(i);

            editor.putString("note_title_" + i,note.getTitle());
            editor.putString("note_content_" + i, note.getContent());
            editor.putString("note_date_" + i, note.getDate());
        }

        editor.apply();
    }

    public class Note {
        private String title;
        private String Content;

        private String Date;

        public Note(){


        }
        public String getDate(){
            return Date;
        }
        public void setDate(String Date){

            this.Date = Date;
        }
        public String getTitle(){

            return title;

        }

        public void setTitle(String title){

            this.title = title;
        }

        public String getContent(){

            return Content;
        }
        public void setContent(String content){

            this.Content = content;
        }
        public Note(String title, String content, String Date){

            this.title = title;
            this.Content = content;
            this.Date = Date;


        }


    }

    public void goBack (View view){
        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);
    }


}