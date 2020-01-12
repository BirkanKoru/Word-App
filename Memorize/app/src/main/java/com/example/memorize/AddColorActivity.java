package com.example.memorize;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddColorActivity extends AppCompatActivity {

    int group_id;
    int m_Color;

    ImageButton btn_colorPicker;
    Button btn_showColor, btn_addColor;
    Spinner spinner;

    ArrayList<HashMap<String, String>> group_list;
    ArrayAdapter adapter;
    String group_names[];
    String group_ids[];

    ArrayList<HashMap<String, String>> color_list;
   String color_group_ids[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_color);

        spinner = findViewById(R.id.spinner_groups);
        Database db = new Database(getApplicationContext());
        group_list = db.group_list();
        color_list = db.color_list();
        color_group_ids = new String[color_list.size()];
        for(int j=0; j < color_list.size(); j++) {
            color_group_ids[j] = color_list.get(j).get("color_group_id");
        }

        if(group_list.size() == 0)
        {
            Toast.makeText(this, "Henüz bir grup eklenmemiş.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddColorActivity.this, GroupActivity.class));
        }else {
            group_names = new String[group_list.size()];
            group_ids = new String[group_list.size()];

            for(int i =0; i < group_list.size(); i++)
            {
                    group_ids[i] = group_list.get(i).get("group_id");
                    group_names[i] = group_list.get(i).get("group_name");
            }

            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, group_names);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
                {
                    group_id = Integer.parseInt(group_list.get(pos).get("group_id"));
                }
                public void onNothingSelected(AdapterView<?> parent)
                {
                    //NOTHING
                }
            });
        }

        btn_colorPicker = findViewById(R.id.btn_colorPicker);
        btn_colorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        btn_addColor = findViewById(R.id.btn_colorAdd);
        btn_addColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Arrays.asList(color_group_ids).contains(String.valueOf(group_id)) == true){
                    Toast.makeText(AddColorActivity.this, "Bu gruba ait bir renk tanımlaması mevcut.", Toast.LENGTH_SHORT).show();
                } else {
                    if(m_Color == 0xff000000){
                        Toast.makeText(AddColorActivity.this, "Siyah rengi kullanılamaz.", Toast.LENGTH_SHORT).show();
                    } else {
                        Database db = new Database(getApplicationContext());
                        db.color_add(m_Color, group_id);
                        db.close();
                        Toast.makeText(AddColorActivity.this, "Başarıyla eklendi.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddColorActivity.this, GroupActivity.class));
                    }
                }

            }
        });
    }

    public void openColorPicker()
    {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, m_Color, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog)
            {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color)
            {
                m_Color = color;
                btn_showColor = findViewById(R.id.btn_showColor);
                btn_showColor.setBackgroundColor(m_Color);
                if(m_Color == 0xff000000){
                    Toast.makeText(AddColorActivity.this, "Yazı rengi ile aynı rengi kullanmayınız.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        colorPicker.show();
    }
}
