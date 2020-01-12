package com.example.memorize;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordActivity extends AppCompatActivity {

    SwipeMenuListView listView;
    SimpleAdapter adapter;
    ArrayList<HashMap<String, String>> word_list;
    int group_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        listView = findViewById(R.id.listView_words);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "change" item
                SwipeMenuItem changeItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                changeItem.setBackground(new ColorDrawable(Color.rgb(0xee, 0x76, 0x00)));
                // set item width
                changeItem.setWidth(170);
                //
                changeItem.setIcon(R.drawable.ic_change);
                // add to menu
                menu.addMenuItem(changeItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0x8B, 0x3E, 0x2F)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Intent intent = new Intent(WordActivity.this, WordEditActivity.class);
                        intent.putExtra("id", Integer.parseInt(word_list.get(position).get("word_id")));
                        startActivity(intent);
                        break;
                    case 1:
                        Database db = new Database(getApplicationContext());
                        db.word_delete(Integer.parseInt(word_list.get(position).get("word_id")));
                        Toast.makeText(WordActivity.this, "Silme başarılı!",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(WordActivity.this, GroupActivity.class));

                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();

        group_id = getIntent().getIntExtra("id", 0);
        Database db = new Database(getApplicationContext());
        word_list = db.word_list(group_id);

        if(word_list.size() == 0)
        {
            Toast.makeText(this, "Henüz bir word eklenmemiş.", Toast.LENGTH_SHORT).show();
        }else {
            String[] listView_word_line_names = new String[]{"word", "info"};
            int[] listView_word_line_ids = new int[]{R.id.textView_word_name, R.id.textView_word_info};

            final List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
            for(int i =0; i < word_list.size(); i++)
            {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("word", word_list.get(i).get("word_name"));
                map.put("info", word_list.get(i).get("word_info"));
                fillMaps.add(map);
            }

            adapter = new SimpleAdapter(this, fillMaps, R.layout.listview_word,
                    listView_word_line_names, listView_word_line_ids);

            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(WordActivity.this, GroupActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_word, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_bar_btn_add:
                Intent intent = new Intent(WordActivity.this, WordAddActivity.class);
                intent.putExtra("id", group_id);
                startActivity(intent);
                return true;

            case R.id.action_bar_btn_quizz:
                Intent intent2 = new Intent(WordActivity.this, QuizActivity.class);
                intent2.putExtra("id", group_id);
                startActivity(intent2);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
