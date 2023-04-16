package com.kuet.kothabarta.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kuet.kothabarta.R;
import com.kuet.kothabarta.databinding.FragmentStatusBinding;

public class StatusFragment extends Fragment {
    FragmentStatusBinding binding;

    public StatusFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatusBinding.inflate(getLayoutInflater(), container, false);



        return binding.getRoot();
    }
}