package com.example.memorize;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class WordAddActivity extends AppCompatActivity {

    ArrayList<HashMap<String, String>> word_list;
    String[] word_names;

    Button btn_add;
    EditText editText_name, editText_info;
    int group_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_add);

        group_id = getIntent().getIntExtra("id", 0);
        Database db = new Database(getApplicationContext());
        word_list = db.word_list(group_id);
        word_names = new String[word_list.size()];
        for (int i=0; i<word_list.size(); i++){
            word_names[i] = word_list.get(i).get("word_name");
        }

        btn_add = findViewById(R.id.btn_wordAdd);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_name = findViewById(R.id.editText_wordName);
                editText_info = findViewById(R.id.editText_wordInfo);

                String word_name = editText_name.getText().toString().trim();
                String word_info = editText_info.getText().toString().trim();

                Database db = new Database(getApplicationContext());
                if(word_name.isEmpty() || word_info.isEmpty())
                {
                    Toast.makeText(WordAddActivity.this, "Boş yer bırakmayınız!",
                            Toast.LENGTH_SHORT).show();
                }else {
                    if(Arrays.asList(word_names).contains(word_name) == true){
                        Toast.makeText(WordAddActivity.this, "Bu kelime mevcut.", Toast.LENGTH_SHORT).show();
                    } else {
                        db.word_add(word_name.toLowerCase(), word_info.toLowerCase(), group_id);
                        db.close();
                        Toast.makeText(getApplicationContext(), "Ekleme başarılı.",
                                Toast.LENGTH_LONG).show();
                    }
                }
                editText_name.getText().clear();
                editText_info.getText().clear();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(WordAddActivity.this, WordActivity.class);
        intent.putExtra("id", group_id);
        startActivity(intent);
    }
}
