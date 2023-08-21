package com.example.asian;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class FragmentTabTwo extends Fragment {
    static List<ItemObject> listItemObject = getListItems();
    static ItemAdapter itemAdapter = new ItemAdapter();
    static RecyclerView rcvContainer;

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab, container, false);

        rcvContainer = view.findViewById(R.id.rcvContainer);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvContainer.setLayoutManager(linearLayoutManager);


        itemAdapter.setData(listItemObject);
        rcvContainer.setAdapter(itemAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        rcvContainer.addItemDecoration(itemDecoration);

        Button btnAdd;
        btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(view1 -> {
            try {
                listItemObject = updateListItems("Item " + (listItemObject.size() + 1));
                itemAdapter.setData(listItemObject);
                rcvContainer.setAdapter(itemAdapter);
            } catch (Exception e) {
                btnAdd.setError(e.toString());
            }
        });
        return view;
    }

    private static List<ItemObject> getListItems() {
        List<ItemObject> list = new ArrayList<>();
        for (int i = 0; i <= ALPHABET.length() - 1; i++) {
            list.add(new ItemObject("Item 2" + ALPHABET.charAt(i)));
        }

        return list;
    }

    private List<ItemObject> updateListItems(String s) {
        List<ItemObject> itemObjects = listItemObject;
        itemObjects.add(new ItemObject(s));
        return itemObjects;
    }

    @SuppressLint("NotifyDataSetChanged")
    public static void removeItem(int position) {
        listItemObject.remove(position);
        itemAdapter.notifyDataSetChanged();
    }
}