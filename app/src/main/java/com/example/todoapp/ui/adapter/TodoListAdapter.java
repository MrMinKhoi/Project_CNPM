package com.example.todoapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.model.Todo;
import com.example.todoapp.ui.OnTodoItemLongClickListener;

import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {
    private Context context;

    private List<Todo> lsTodo;
    private TextView title;
    private TextView content;

    private OnTodoItemLongClickListener longClickListener;

    public TodoListAdapter(List<Todo> lsTodo, Context context) {
        this.lsTodo = lsTodo;
        this.context = context;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
        }

    }



    @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View todoView = inflater.inflate(R.layout.todo_item_layout, parent, false);
            ViewHolder viewHolder = new ViewHolder(todoView);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Todo todo = lsTodo.get(position);

            title.setText(todo.getTitle());
            content.setText(todo.getDescription());

            holder.itemView.setOnLongClickListener(v -> {
                if (longClickListener != null) {
                    longClickListener.onItemLongClick(v, todo);
                }
                return true;
            });

        }

        @Override
        public int getItemCount() {
            return lsTodo.size();
        }
        public void updateStatus( int position, boolean status) {
        Todo todo = lsTodo.get(position);
        todo.setCompleted(status);


    }
    public void setOnItemLongClickListener(OnTodoItemLongClickListener listener) {
        this.longClickListener = listener;
    }
    public void onUpdate(String text, String contentView) {
        title.setText(text);
        content.setText(contentView);
    }
}
