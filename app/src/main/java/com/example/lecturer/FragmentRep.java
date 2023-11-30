package com.example.lecturer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FragmentRep extends Fragment implements MyPagerAdapter1.TitledFragment{

    private CommonAdapter commonAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the common layout for the fragment
        View view = inflater.inflate(R.layout.fragmentrepeat, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPassList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Student> passItemList = new ArrayList<>();
        commonAdapter = new CommonAdapter(passItemList);
        recyclerView.setAdapter(commonAdapter);

        return view;
    }
    @Override
    public CharSequence getTitle() {
        return "Repeat";
    }
}
