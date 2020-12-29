package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText editTextItem;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editTextItem = findViewById(R.id.editTextItem);
        saveButton = findViewById(R.id.saveButton);

        getSupportActionBar().setTitle("Edit item");

        editTextItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        // When the user is done editing, they click the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent which will contain the results
                Intent intent = new Intent();

                // Pass the data (results of editing)
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, editTextItem.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

                // Set the result of the intent
                setResult(RESULT_OK, intent);

                // Finish activity, close the screen and go back
                finish();
            }
        });
    }
}