package com.example.todoapp.ui.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.apiService.RetrofitClient;
import com.example.todoapp.apiService.TodoApi;
import com.example.todoapp.model.Todo;
import com.example.todoapp.ui.adapter.TodoListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
        TodoApi todoApi = RetrofitClient.getInstance().create(TodoApi.class);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(view -> {
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
                todoApi.createTodo(todo2).enqueue(new Callback<Todo>() {
                    @Override
                    public void onResponse(Call<Todo> call, Response<Todo> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            lsTodo.add(response.body());
                            adapter.notifyItemInserted(lsTodo.size() - 1);
                        }
                    }

                    @Override
                    public void onFailure(Call<Todo> call, Throwable t) {
                        Toast.makeText(WorkHandleActivity.this, "Loi khi tao moi", Toast.LENGTH_SHORT).show();
                    }
                });

            });

            builder.setNegativeButton("Huy", (dialog, which) -> dialog.cancel());
            builder.show();
        });
        RecyclerView lsView = findViewById(R.id.recyclerTodos);

        lsView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TodoListAdapter(lsTodo, WorkHandleActivity.this);
        lsView.setAdapter(adapter);
        todoApi.getTodos().enqueue(new Callback<List<Todo>>() {
            @Override
            public void onResponse(Call<List<Todo>> call, Response<List<Todo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    lsTodo.clear();
                    lsTodo.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(WorkHandleActivity.this, "Khong the tai danh sach", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Todo>> call, Throwable t) {
                Toast.makeText(WorkHandleActivity.this, "Loi mang: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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
                        todoApi.createTodo(todo2).enqueue(new Callback<Todo>() {
                            @Override
                            public void onResponse(Call<Todo> call, Response<Todo> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    lsTodo.add(response.body());
                                    adapter.notifyItemInserted(lsTodo.size() - 1);

                                }

                                Toast.makeText(WorkHandleActivity.this, "Tao moi todo", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Todo> call, Throwable t) {
                                Log.e("API_ERROR", t.getMessage(), t);

                                Toast.makeText(WorkHandleActivity.this, "Loi khi tao moi", Toast.LENGTH_SHORT).show();
                            }
                        });

                    });

                    builder.setNegativeButton("Huy", (dialog, which) -> dialog.cancel());
                    builder.show();

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
                        Todo todo2 = new Todo(editTitle.getText().toString(), editContent.getText().toString(), true);
                        todoApi.updateTodo(todo.getId(),todo2).enqueue(new Callback<Todo>() {
                            @Override
                            public void onResponse(Call<Todo> call, Response<Todo> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    Todo updatedTodo = response.body();
                                    if (updatedTodo != null) {
                                        int index = lsTodo.indexOf(todo);
                                        if (index != -1) {
                                            lsTodo.set(index, updatedTodo);
                                            adapter.notifyItemChanged(index);
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<Todo> call, Throwable t) {
                                Log.e("API_ERROR", t.getMessage(), t);

                                Toast.makeText(WorkHandleActivity.this, "Loi khi cap nhat", Toast.LENGTH_SHORT).show();
                            }
                        });

                    });

                    builder.setNegativeButton("Huy", (dialog, which) -> dialog.cancel());
                    builder.show();

                    Toast.makeText(WorkHandleActivity.this, "Sua: " + todo.getTitle(), Toast.LENGTH_SHORT).show();
                    return true;

                } else if (itemId == R.id.menu_delete) {
                    Todo todo2 = new Todo(todo.getTitle(), todo.getDescription(), false);
                    todoApi.updateTodo(todo.getId() , todo2).enqueue(new Callback<Todo>() {
                        @Override
                        public void onResponse(Call<Todo> call, Response<Todo> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                int index = lsTodo.indexOf(todo);
                                if (index != -1) {
                                    lsTodo.remove(index);
                                    adapter.notifyItemRemoved(index);
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<Todo> call, Throwable t) {
                            Log.e("API_ERROR", t.getMessage(), t);

                            Toast.makeText(WorkHandleActivity.this, "Loi khi xoa", Toast.LENGTH_SHORT).show();
                        }
                    });

                    Toast.makeText(WorkHandleActivity.this, "Xoa: " + todo.getTitle(), Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            });
            popup.show();
        });

    }


}