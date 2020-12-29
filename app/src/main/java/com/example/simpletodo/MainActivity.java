package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> listItems;
    Button addButton;
    EditText editTextItem;
    RecyclerView recyclerViewItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = findViewById(R.id.addButton);
        editTextItem = findViewById(R.id.addTextItem);
        recyclerViewItems = findViewById(R.id.recyclerViewItems);

        loadItems();
        /*
        listItems = new ArrayList<>();
        listItems.add("Setup Android");
        listItems.add("Setup SimpleToDo App");
        listItems.add("Design SimpleToDo App");
        listItems.add("Render items for SimpleToDo App");
        listItems.add("Implement Add and Remove for SimpleToDo App");
        listItems.add("Submit SimpleToDo App");
         */

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // Delete the item from the model
                listItems.remove(position);
                // Notify the adapter
                itemsAdapter.notifyItemRemoved(position);

                Toast.makeText(getApplicationContext(), "Item was removed!", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single click at position " + position);
                // Create a new edit activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);

                // Pass data being edited
                i.putExtra(KEY_ITEM_TEXT, listItems.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);

                // Display the activity
                startActivityForResult(i, EDIT_TEXT_CODE);

            }
        };

        itemsAdapter = new ItemsAdapter(listItems, onLongClickListener, onClickListener);
        recyclerViewItems.setAdapter(itemsAdapter);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = editTextItem.getText().toString();
                // Add item to the model
                listItems.add(todoItem);

                // Notify adapter that an item is inserted
                itemsAdapter.notifyItemInserted(listItems.size() - 1);
                editTextItem.setText("");

                Toast.makeText(getApplicationContext(), "Item was added!", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    // Handle the result of the edit activity
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            // Retreive the updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);

            // Extract the original position of the edited item from the position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            // Update model at the right position with the new item text
            listItems.set(position, itemText);

            // Notify adapter that a change has occurred
            itemsAdapter.notifyItemChanged(position);

            // Persist the changes
            Toast.makeText(getApplicationContext(), "Item has been updated!", Toast.LENGTH_SHORT).show();
            saveItems();
        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    // This function will load items by reading every line of the datafile
    private void loadItems() {
        try {
            listItems = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            listItems = new ArrayList<>();
        }
    }

    // This function saves items by writing them to the datafile
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listItems);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}