package ru.mirea.semina.retrofitapp;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// ViewHolder хранит элементы одной карточки, чтобы не искать их при прокрутке.
public class TodoViewHolder extends RecyclerView.ViewHolder {
    final ImageView image;
    final TextView title;
    final TextView state;
    final CheckBox completed;

    public TodoViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.todoImage);
        title = itemView.findViewById(R.id.todoTitle);
        state = itemView.findViewById(R.id.todoState);
        completed = itemView.findViewById(R.id.todoCompleted);
    }
}
