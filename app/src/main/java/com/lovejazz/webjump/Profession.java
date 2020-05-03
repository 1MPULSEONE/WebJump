package com.lovejazz.webjump;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SearchView;

public class Profession extends AppCompatActivity {
    RecyclerView recyclerBooks;
    String s1[],s2[];
    int images[] = {R.drawable.book1,R.drawable.book2,R.drawable.book3,R.drawable.book4,R.drawable.book5,R.drawable.book6};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_activity);
        recyclerBooks = findViewById(R.id.recyclerBooks);
        s1 = getResources().getStringArray(R.array.programming_books);
        s2 = getResources().getStringArray(R.array.author);
        RecyclerViewAdapter RecycleViewAdapter = new RecyclerViewAdapter(Profession.this,s1,s2,images);
        Log.d("Profession",RecycleViewAdapter.data2[2]);
        recyclerBooks.setAdapter(RecycleViewAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerBooks.setLayoutManager(gridLayoutManager);
    }
}
