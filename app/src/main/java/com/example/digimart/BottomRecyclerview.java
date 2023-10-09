package com.example.digimart;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BottomRecyclerview extends RecyclerView.Adapter<BottomRecyclerview.ImageViewHolder> {
    private List<Integer> imageList;
    private static final float maxElevation = 16.0f;
    RecyclerView recyclerView;
    private static final float translationZ = 16.0f;
    private int selectedItem = 0; // Initially, no item is selected


    public BottomRecyclerview(List<Integer> imageList, RecyclerView recyclerView) {
        this.imageList = imageList;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imgrecycler, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        int imageResId = imageList.get(position);
        holder.imageView.setImageResource(imageResId);
        float scale = position == selectedItem ? 1.0f : 0.5f;
        holder.imageView.setScaleX(scale);
        holder.imageView.setScaleY(scale);

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void setSelectedItem(int position) {
        selectedItem = position;
        notifyDataSetChanged(); // Trigger item updates

    }

    public interface OnOkButtonClickListener
    {
        void onOkButtonClick(int imageResId);
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }


}
