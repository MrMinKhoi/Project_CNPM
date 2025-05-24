package com.example.todoapp.ui;

import android.view.View;

import com.example.todoapp.model.Todo;

public interface OnTodoItemLongClickListener {
    void onItemLongClick(View view, Todo todo);
}