package com.example.memorize;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupActivity extends AppCompatActivity {

    SwipeMenuListView listView;
    ArrayAdapter adapter;
    ArrayList<HashMap<String, String>> group_list;
    ArrayList<HashMap<String, String>> color_list;
    String group_names[];
    int group_ids[];

    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        floatingActionButton = findViewById(R.id.btn_groupAdd);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GroupActivity.this, GroupAddActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Database db = new Database(getApplicationContext());
        group_list = db.group_list();
        color_list = db.color_list();
        if(group_list.size() == 0)
        {
            Toast.makeText(this, "Henüz bir grup eklenmemiş.", Toast.LENGTH_SHORT).show();
        }else {
            group_names = new String[group_list.size()];
            group_ids = new int[group_list.size()];

            for(int i =0; i < group_list.size(); i++)
            {
                group_names[i] = group_list.get(i).get("group_name");
                group_ids[i] = Integer.parseInt(group_list.get(i).get("group_id"));
            }

            listView = findViewById(R.id.listView_groups);
            adapter = new ArrayAdapter(this, R.layout.listview_group, R.id.textView_group_name, group_names)
            {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View row = super.getView(position, convertView, parent);
                    for(int i=0; i < color_list.size(); i++){
                        if(getItem(position).equals(group_names[i])){
                            row.setBackgroundColor(Integer.parseInt(color_list.get(i).get("color_hex")));
                            break;
                        }
                    }
                    return row;
                }
            };
            listView.setAdapter(adapter);

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
                    SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
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
            listView.setMenuCreator(creator);

            listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                    switch (index) {
                        case 0:
                            Intent intent = new Intent(GroupActivity.this, GroupEditActivity.class);
                            intent.putExtra("id", Integer.parseInt(group_list.get(position).get("group_id")));
                            startActivity(intent);
                            break;
                        case 1:
                            Database db = new Database(getApplicationContext());
                            db.group_delete(Integer.parseInt(group_list.get(position).get("group_id")));
                            Toast.makeText(GroupActivity.this, "Silme başarılı!",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(getIntent());
                            break;
                    }
                    // false : close the menu; true : not close the menu
                    return false;
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(GroupActivity.this, WordActivity.class);
                    intent.putExtra("id", group_ids[position]);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_group, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.action_bar_btn_settings:
                startActivity(new Intent(GroupActivity.this, AddColorActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
