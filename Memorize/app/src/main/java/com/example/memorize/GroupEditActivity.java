package com.example.memorize;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupEditActivity extends AppCompatActivity {

    int group_id;
    ArrayList<HashMap<String, String>> group;
    String group_name;

    EditText editText_group_name;
    Button btn_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_edit);
        group_id = getIntent().getIntExtra("id", 0);

        Database db = new Database(getApplicationContext());
        group = db.group_find(group_id);

        group_name = group.get(0).get("group_name");
        editText_group_name = findViewById(R.id.editText_groupEditName);
        editText_group_name.setText(group_name);

        btn_edit = findViewById(R.id.btn_groupEdit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group_name = editText_group_name.getText().toString().trim();
                if(group_name.isEmpty())
                {
                    Toast.makeText(GroupEditActivity.this, "Boş yer bırakmayınız!",
                            Toast.LENGTH_SHORT).show();
                }else {
                    Database db = new Database(getApplicationContext());
                    db.group_update(group_name, group_id);
                    db.close();
                    Toast.makeText(GroupEditActivity.this, "Düzenleme başarılı.",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(GroupEditActivity.this, GroupActivity.class));
                }
            }
        });
    }
}
