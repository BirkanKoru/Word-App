package com.example.memorize;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

public class GroupAddActivity extends AppCompatActivity {

    EditText editText_name;
    Button btn_groupAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add);

        btn_groupAdd = findViewById(R.id.btn_groupAdd);
        btn_groupAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_name = findViewById(R.id.editText_groupName);
                String group_name = editText_name.getText().toString().trim();

                Database db = new Database(getApplicationContext());
                if(group_name.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Boş yer bırakmayınız!",
                            Toast.LENGTH_LONG).show();
                }else {
                    db.group_add(editText_name.getText().toString().trim());
                    db.close();
                    Toast.makeText(getApplicationContext(), "Ekleme başarılı.",
                            Toast.LENGTH_LONG).show();
                }
                editText_name.getText().clear();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GroupAddActivity.this, GroupActivity.class));
    }
}