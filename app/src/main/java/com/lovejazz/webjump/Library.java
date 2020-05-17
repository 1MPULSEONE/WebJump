package com.lovejazz.webjump;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Library extends Fragment {
    DatabaseReference reference;
    RecyclerView recyclerBooks;
    RecyclerViewAdapter recyclerViewAdapter;
    String s1[], s2[];
    private static final String TAG = "Library";
    ArrayList<Book> list;
    public Library() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Creating recycle view
        View view = inflater.inflate(R.layout.fragment_library,container,false);
        recyclerBooks = view.findViewById(R.id.recyclerBooks);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerBooks.setLayoutManager(gridLayoutManager);
        list = new ArrayList<Book>();
        //Database
        reference = FirebaseDatabase.getInstance().getReference("Books");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Book book = dataSnapshot1.getValue(Book.class);
                    list.add(book);
                }
                recyclerViewAdapter = new RecyclerViewAdapter(getContext(),list);
                recyclerBooks.setAdapter(recyclerViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG,"Database cancelled");
            }
        });
        return view;
        }
    }
