package com.example.memorize;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class WordEditActivity extends AppCompatActivity {

    int word_id;
    ArrayList<HashMap<String, String>> word;
    String word_name, word_info;

    EditText editText_word_name, editText_word_info;
    Button btn_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_edit);

        word_id = getIntent().getIntExtra("id", 0);
        Database db = new Database(getApplicationContext());
        word = db.word_find(word_id);

        word_name = word.get(0).get("word_name");
        word_info = word.get(0).get("word_info");

        editText_word_name = findViewById(R.id.editText_wordEditName);
        editText_word_info = findViewById(R.id.editText_wordEditInfo);
        editText_word_name.setText(word_name);
        editText_word_info.setText(word_info);

        btn_edit = findViewById(R.id.btn_wordEdit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word_name = editText_word_name.getText().toString().trim();
                word_info = editText_word_info.getText().toString().trim();
                if(word_name.isEmpty() || word_info.isEmpty())
                {
                    Toast.makeText(WordEditActivity.this, "Boş yer bırakmayınız.",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Database db = new Database(getApplicationContext());
                    db.word_update(word_name, word_info, word_id);
                    db.close();
                    Toast.makeText(WordEditActivity.this, "Düzenleme başarılı.",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(WordEditActivity.this, GroupActivity.class));
                }
            }
        });
    }
}
