package com.example.digimart;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("get_products.php")
    Call<List<Product>> getProducts();

    // Add more API endpoints as needed, such as adding items to the cart or processing orders
}
