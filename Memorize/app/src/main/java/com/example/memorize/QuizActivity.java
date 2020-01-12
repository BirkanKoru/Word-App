package com.example.memorize;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    ListView listView;
    TextView textV_word_name, textV_quest_num, textV_score;

    ArrayList<HashMap<String, String>> word_list;
    int group_id;
    String wordInfo;

    private int score=0;
    private int leftQuest = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        listView = findViewById(R.id.listView_answers);
        textV_word_name = findViewById(R.id.textView_quiz_word_name);
        textV_quest_num = findViewById(R.id.textView_quiz_quest_num);
        textV_score = findViewById(R.id.textView_quiz_score);
        textV_quest_num.setText("Left: "+leftQuest);
        textV_score.setText("Score: "+score);

        group_id = getIntent().getIntExtra("id", 0);
        Database db = new Database(getApplicationContext());
        word_list = db.word_list(group_id);

        if(word_list.size() < 15)
        {
            Toast.makeText(this, "En az 15 kelime girilmelidir.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(QuizActivity.this, GroupActivity.class));
        }else{
            chooseWord();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String clicked = parent.getItemAtPosition(position).toString();
                if(clicked.equals(wordInfo))
                {
                    Toast.makeText(QuizActivity.this, "Doğru :)", Toast.LENGTH_SHORT).show();
                    score+= 10;
                }else{
                    Toast.makeText(QuizActivity.this, "Yanlış!", Toast.LENGTH_SHORT).show();
                }

                leftQuest--;
                textV_score.setText("Score:"+score);
                textV_quest_num.setText("Left:"+leftQuest);

                if(leftQuest > 0)
                {
                    chooseWord();
                }else {
                    //GAME OVER
                    textV_quest_num.setTextColor(getResources().getColor(R.color.colorRed));
                    textV_quest_num.setText("Game finished.");
                    listView.setEnabled(false);
                    listView.setOnItemClickListener(null);
                }
            }
        });
    }


    private void chooseWord()
    {
        Random rnd = new Random();
        int rndNum = rnd.nextInt(word_list.size());
        String wordName = word_list.get(rndNum).get("word_name");
        wordInfo = word_list.get(rndNum).get("word_info");

        word_list.remove(rndNum);
        Collections.shuffle(word_list);
        List<String> wordInfos = new ArrayList<>();
        wordInfos.add(word_list.get(0).get("word_info"));
        wordInfos.add(word_list.get(1).get("word_info"));
        wordInfos.add(word_list.get(2).get("word_info"));
        wordInfos.add(word_list.get(3).get("word_info"));
        wordInfos.add(wordInfo);
        Collections.shuffle(wordInfos);

        textV_word_name.setText(wordName);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                new ArrayList<String>(wordInfos)
        );
        listView.setAdapter(adapter);
    }
}
