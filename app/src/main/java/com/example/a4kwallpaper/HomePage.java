package com.example.a4kwallpaper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomePage extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface {
    private EditText searchEdt;
    private ImageView searchIV;
    private RecyclerView categoryRV,wallpaperRV;
    private ProgressBar loadingPB;
    private ArrayList<String> wallpaperArrayList;
    private ArrayList<CategoryRVModal> categoryRVModalArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private WallpaperRVAdapter wallpaperRVAdapter;
    //yBLSLv5bMcLZuCg9MQywQd6QzNT5TeRH9Ed33Ij5BJNVurkTkUDpLaGe

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        searchEdt = findViewById(R.id.idEdtSearch);
        searchIV = findViewById(R.id.idIVSearch);
        categoryRV = findViewById(R.id.idRVCategory);
        wallpaperRV = findViewById(R.id.idRVWallpapers);
        loadingPB = findViewById(R.id.idPBLoading);
        wallpaperArrayList = new ArrayList<>();
        categoryRVModalArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomePage.this,RecyclerView.HORIZONTAL,false);
        categoryRVAdapter = new CategoryRVAdapter(categoryRVModalArrayList,HomePage.this,this::onCategoryClick);
        categoryRV.setAdapter(categoryRVAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(HomePage.this,2);
        wallpaperRV.setLayoutManager(gridLayoutManager);
        wallpaperRVAdapter = new WallpaperRVAdapter(wallpaperArrayList,this);
        wallpaperRV.setAdapter(wallpaperRVAdapter);

        getCategories();
        getWallpapers();

        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchStr  = searchEdt.getText().toString();
                if(searchStr.isEmpty()){
                    Toast.makeText(HomePage.this,"Please enter your search query",Toast.LENGTH_SHORT).show();
                }else{
                    getWallpapersByCategory(searchStr);
                }
            }
        });
    }

    private void getWallpapersByCategory(String category){
        wallpaperArrayList.clear();
        loadingPB.setVisibility(View.VISIBLE);
        String url = "https://api.pexels.com/v1/search?query="+category+"&per_page=30&page=1";
        RequestQueue requestQueue = Volley.newRequestQueue(HomePage.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray photoArray = null;
                try {
                    photoArray = response.getJSONArray("photos");
                    for(int i=0;i<photoArray.length();i++){
                        JSONObject photoObj = photoArray.getJSONObject(i);
                        String imgurl = photoObj.getJSONObject("src").getString("portrait");
                        wallpaperArrayList.add(imgurl);
                    }
                    wallpaperRVAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomePage.this, "Fail to load wallpapers...", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();
                headers.put("Authorization","yBLSLv5bMcLZuCg9MQywQd6QzNT5TeRH9Ed33Ij5BJNVurkTkUDpLaGe");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    private void getWallpapers() {
        wallpaperArrayList.clear();
        loadingPB.setVisibility(View.VISIBLE);
        String url = "https://api.pexels.com/v1/curated?per_page=30&page=1";
        RequestQueue requestQueue = Volley.newRequestQueue(HomePage.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingPB.setVisibility(View.GONE);
                try {
                    JSONArray photoArray = response.getJSONArray("photos");
                    for(int i=0; i<photoArray.length();i++){
                        JSONObject photoObj = photoArray.getJSONObject(i);
                        String imgUrl = photoObj.getJSONObject("src").getString("portrait");
                        wallpaperArrayList.add(imgUrl);
                    }
                    wallpaperRVAdapter.notifyDataSetChanged();
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomePage.this, "Fail to Load wallpapers...", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();
                headers.put("Authorization","yBLSLv5bMcLZuCg9MQywQd6QzNT5TeRH9Ed33Ij5BJNVurkTkUDpLaGe");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void getCategories(){
        categoryRVModalArrayList.add(new CategoryRVModal("Technology","https://images.unsplash.com/photo-1488590528505-98d2b5aba04b?auto=format&fit=crop&q=80&w=500&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRVModalArrayList.add(new CategoryRVModal("Programming","https://images.unsplash.com/photo-1542831371-29b0f74f9713?auto=format&fit=crop&q=80&w=500&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRVModalArrayList.add(new CategoryRVModal("Nature","https://images.pexels.com/photos/2387873/pexels-photo-2387873.jpeg?auto-compress&cs=tinysrgb&dpr=1&w=588"));
        categoryRVModalArrayList.add(new CategoryRVModal("Travel","https://images.pexels.com/photos/672358/pexels-photo-672358.jpeg?auto-compress&cs=tinysrgb&dpr=1&w=500"));
        categoryRVModalArrayList.add(new CategoryRVModal("Architecture","https://images.pexels.com/photos/256150/pexels-photo-256150.jpeg?auto-compress&cs=tinysrgh&dpr=1&w=500"));
        categoryRVModalArrayList.add(new CategoryRVModal("Arts","https://images.pexels.com/photos/1194420/pexels-photo-1194420.jpeg?auto-compress&cs=tinysrgb&dpr-1&w-500"));
        categoryRVModalArrayList.add(new CategoryRVModal("Music","https://images.pexels.com/photos/4348093/pexels-photo-4348093.jpeg?auto-compress&cs=tinysrgb&dpr=1&w=500"));
        categoryRVModalArrayList.add(new CategoryRVModal("Abstract","https://images.pexels.com/photos/2110951/pexels-photo-2110951.jpeg?auto-compress&cs=tinysrgb&dpr=1&w=500"));
        categoryRVModalArrayList.add(new CategoryRVModal("Cars","https://images.pexels.com/photos/3802510/pexels-photo-3802510.jpeg?auto-compress&cs=tinysrgb&dpr=1&w=500"));
        categoryRVModalArrayList.add(new CategoryRVModal("Flowers","https://images.pexels.com/photos/1086178/pexels-photo-1086178.jpeg?auto-compress&cs=tinysrgb&dpr=1&w=500"));
        categoryRVAdapter.notifyDataSetChanged();
    }
    @Override
    public void onCategoryClick(int position) {
        String category = categoryRVModalArrayList.get(position).getCategory();
        getWallpapersByCategory(category);
    }
}