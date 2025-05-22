package com.example.todoapp;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkHandleActivity extends AppCompatActivity {

    List<Todo> lsTodo = new ArrayList<>();
    TodoListAdapter adapter;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workhandle_layout);

        RecyclerView lsView = findViewById(R.id.recyclerTodos);
        TodoApi api = RetrofitClient.getInstance().create(TodoApi.class);
        lsView.setLayoutManager(new LinearLayoutManager(this));
        lsTodo.add(new Todo("thien", "gih chi 1", true)) ;
        lsTodo.add(new Todo("title", "gih chi 2", true)) ;
        lsTodo.add(new Todo("header", "gih chi 3", true)) ;
        lsTodo.add(new Todo("first", "gih chi 4", true)) ;
        lsTodo.add(new Todo("wqw", "gih chi 5", true)) ;
        adapter = new TodoListAdapter(lsTodo, WorkHandleActivity.this);

        lsView.setAdapter(adapter);
        adapter.setOnItemLongClickListener((view, todo) -> {
            PopupMenu popup = new PopupMenu(WorkHandleActivity.this, view);
            popup.getMenuInflater().inflate(R.menu.todo_item_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();

                if (itemId == R.id.menu_add) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(WorkHandleActivity.this);
                    builder.setTitle("Them todo");

                    LinearLayout layout = new LinearLayout(WorkHandleActivity.this);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setPadding(16, 16, 16, 16);

                    final EditText editTitle = new EditText(WorkHandleActivity.this);
                    editTitle.setHint("Nhap title");

                    final EditText editContent = new EditText(WorkHandleActivity.this);
                    editContent.setHint("Nhap content");

                    layout.addView(editTitle);
                    layout.addView(editContent);

                    builder.setView(layout);

                    builder.setPositiveButton("OK", (dialog, which) -> {
                        Todo todo2 = new Todo(editTitle.getText().toString(), editContent.getText().toString(), true);
                        lsTodo.add(todo2);
                        adapter.notifyItemInserted(lsTodo.size() - 1);
                    });

                    builder.setNegativeButton("Huy", (dialog, which) -> dialog.cancel());
                    builder.show();

                    Toast.makeText(WorkHandleActivity.this, "Tao moi todo", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (itemId == R.id.menu_edit) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(WorkHandleActivity.this);
                    builder.setTitle("Sua todo");

                    LinearLayout layout = new LinearLayout(WorkHandleActivity.this);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setPadding(16, 16, 16, 16);

                    final EditText editTitle = new EditText(WorkHandleActivity.this);
                    editTitle.setHint("Nhap title");
                    editTitle.setText(todo.getTitle());

                    final EditText editContent = new EditText(WorkHandleActivity.this);
                    editContent.setHint("Nhap content");
                    editContent.setText(todo.getDescription());

                    layout.addView(editTitle);
                    layout.addView(editContent);

                    builder.setView(layout);

                    builder.setPositiveButton("Luu", (dialog, which) -> {
                        todo.setTitle(editTitle.getText().toString());
                        todo.setDescription(editContent.getText().toString());
                        adapter.notifyDataSetChanged();
                        lsView.setAdapter(adapter);
                    });

                    builder.setNegativeButton("Huy", (dialog, which) -> dialog.cancel());
                    builder.show();

                    Toast.makeText(WorkHandleActivity.this, "Sua: " + todo.getTitle(), Toast.LENGTH_SHORT).show();
                    return true;

                } else if (itemId == R.id.menu_delete) {
                    todo.setCompleted(false); // hoac xoa khoi list
                    adapter = new TodoListAdapter(lsTodo, WorkHandleActivity.this);
                    lsView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(WorkHandleActivity.this, "Xoa: " + todo.getTitle(), Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            });
            popup.show();
        });

    }


}