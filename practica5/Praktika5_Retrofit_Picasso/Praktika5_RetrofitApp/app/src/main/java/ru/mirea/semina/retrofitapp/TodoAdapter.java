package ru.mirea.semina.retrofitapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Адаптер превращает Java-объекты Todo в карточки RecyclerView.
public class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder> {

    // Через интерфейс сообщаем Activity, что пользователь нажал CheckBox.
    public interface OnTodoCheckedChangeListener {
        void onTodoCheckedChanged(Todo todo, boolean newValue);
    }

    private final List<Todo> todos = new ArrayList<>();
    private final Set<Integer> updatingIds = new HashSet<>();
    private final OnTodoCheckedChangeListener listener;
    private boolean cropImages = true;


    public TodoAdapter(OnTodoCheckedChangeListener listener) {
        this.listener = listener;
        setHasStableIds(true); // У задач есть стабильные ID с сервера.
    }

    // Получаем новый список после GET-запроса.
    public void setItems(List<Todo> newItems) {
        todos.clear();
        updatingIds.clear();
        if (newItems != null) todos.addAll(newItems);
        notifyDataSetChanged(); // Для учебного примера достаточно.
    }

    // Переключаем обрезку фотографий и обновляем видимые карточки.
    public void setCropImages(boolean cropImages) {
        this.cropImages = cropImages;
        notifyDataSetChanged();
    }

    // Пока PATCH выполняется, повторное нажатие на CheckBox запрещено.
    public void setUpdating(int todoId, boolean updating) {
        if (updating) updatingIds.add(todoId);
        else updatingIds.remove(todoId);
        notifyTodoChanged(todoId);
    }

    // Обновляем только карточку задачи с нужным ID.
    public void notifyTodoChanged(int todoId) {
        for (int i = 0; i < todos.size(); i++) {
            if (todos.get(i).getId() == todoId) {
                notifyItemChanged(i);
                return;
            }
        }
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Создаём одну карточку из item_todo.xml.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Todo todo = todos.get(position);
        holder.title.setText(todo.getId() + ". " + todo.getTitle());
        holder.state.setText(todo.isCompleted() ? "Выполнено" : "Не выполнено");

        // Сначала снимаем старый обработчик: RecyclerView повторно использует карточки.
        holder.completed.setOnCheckedChangeListener(null);
        holder.completed.setChecked(todo.isCompleted());
        holder.completed.setEnabled(!updatingIds.contains(todo.getId()));
        holder.completed.setContentDescription("Изменить состояние задачи № " + todo.getId());

        // Обрабатываем только пользовательское изменение, не обновление карточки.
        holder.completed.setOnCheckedChangeListener((buttonView, checked) -> {
            if (listener != null && !updatingIds.contains(todo.getId())) {
                listener.onTodoCheckedChanged(todo, checked);
            }
        });

        // В JSONPlaceholder у Todo нет фотографии, поэтому используем
        // отдельный демонстрационный сервис случайных фотографий.
        // Размер 320x220 специально не квадратный: так заметен режим обрезки.
        String imageUrl = "https://picsum.photos/seed/todo" + todo.getId() + "/320/220";
        int sizePx = (int) (88 * holder.itemView.getResources().getDisplayMetrics().density + 0.5f);
        holder.image.setScaleType(cropImages
                ? ImageView.ScaleType.CENTER_CROP : ImageView.ScaleType.CENTER_INSIDE);

        RequestCreator request = Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_photo_placeholder)
                .error(R.drawable.ic_photo_placeholder)
                .resize(sizePx, sizePx);

        if (cropImages) request.centerCrop();
        else request.centerInside();
        request.into(holder.image);
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    @Override
    public long getItemId(int position) {
        return todos.get(position).getId();
    }
}
