package com.example.digimart;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> products;

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText(String.valueOf(product.getPrice()));
        Picasso.get().load(product.getImageUrl()).into(holder.imageView);
        holder.productQuantityTextView.setText(String.valueOf(product.getQuantity()));

        holder.addButton.setOnClickListener(view -> {
            notifyDataSetChanged();

            add(product.getName());
        });

        holder.subtractButton.setOnClickListener(view -> {
            notifyDataSetChanged();
            // Update the quantity in the database (call a method to make the API request)
            subtract(product.getName());
        });
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView priceTextView;
        ImageView imageView;
        TextView productQuantityTextView;
        Button addButton, subtractButton;

        public ProductViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.productNameTextView);
            priceTextView = itemView.findViewById(R.id.productPriceTextView);
            imageView = itemView.findViewById(R.id.productImageView);
            productQuantityTextView = itemView.findViewById(R.id.productQuantityTextView);
            addButton = itemView.findViewById(R.id.addButton);
            subtractButton = itemView.findViewById(R.id.subtractButton);
        }
    }

    private void add(String productName) {

        String name = productName;

        String[] field = new String[1];
        field[0] = "name";
        String[] data = new String[1];
        data[0] = name;


        PutData putData = new PutData("http://192.168.0.105/phpdb/add.php", "POST", field, data);

        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                // Handle the result if needed
            }
            else {
                Log.e("PutData Error", putData.getResult());
            }
            Log.e("PutData Error", putData.getResult());
        }

    }

    private void subtract(String productName) {

        String name = productName;

        String[] field = new String[1];
        field[0] = "name";
        String[] data = new String[1];
        data[0] = name;


        PutData putData = new PutData("http://192.168.0.105/phpdb/add.php", "POST", field, data);

        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
            }
        }

    }
}