package com.example.derash2;

import android.os.Bundle;
import android.view.*;

import androidx.fragment.app.Fragment;


public class AmharicFragment extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_amharic, container, false);
        return view;
    }
}