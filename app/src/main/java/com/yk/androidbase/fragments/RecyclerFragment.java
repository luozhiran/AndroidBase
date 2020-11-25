package com.yk.androidbase.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yk.androidbase.R;
import com.yk.androidbase.actions.Outcome0;
import com.yk.androidbase.actions.Outcome1;
import com.yk.androidbase.databinding.FragmentRecyclerBinding;
import com.yk.androidbase.datas.NewsData;
import com.yk.androidbase.model.RecyclerMyModel;
import com.yk.androidbase.repositorys.NewsRepository;
import com.yk.itg_util.Utils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import retrofit2.Call;

public class RecyclerFragment extends Fragment {

    private FragmentRecyclerBinding binding;
    private boolean isFirstLoading = true;
    private NewsRepository newsRepository;
    private RecyclerMyModel recyclerMyModel;

    public static RecyclerFragment instance() {
        RecyclerFragment fragment = new RecyclerFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newsRepository = new NewsRepository();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecyclerBinding.inflate(inflater);
        recyclerMyModel = ViewModelProviders.of(this).get(RecyclerMyModel.class);
        binding.setViewModel(recyclerMyModel);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoading) {
            isFirstLoading = false;
            newsRepository.loadCommentNews("comment", new Outcome0<List<NewsData>>() {
                @Override
                public void call(List<NewsData> newsData) {
                    Utils.ensureChangeOnMainThread();
                    recyclerMyModel.insertItemList(newsData);
                }
            }, new Outcome1<String>() {
                @Override
                public void error(Call call, String s) {

                }
            });
        }
    }
}
