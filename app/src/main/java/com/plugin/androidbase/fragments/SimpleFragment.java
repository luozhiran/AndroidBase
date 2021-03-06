package com.plugin.androidbase.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.itg.lib_log.L;
import com.plugin.androidbase.actions.Outcome0;
import com.plugin.androidbase.actions.Outcome1;
import com.plugin.androidbase.databinding.FragmentSimpleBinding;
import com.plugin.androidbase.datas.NewsData;
import com.plugin.androidbase.model.ListViewMyModel;
import com.plugin.androidbase.repositorys.NewsRepository;
import com.plugin.itg_util.Utils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import retrofit2.Call;


public class SimpleFragment extends Fragment {

    private FragmentSimpleBinding binding;
    private ListViewMyModel simpleViewModel;
    private int position;
    private boolean isFirstLoading = true;
    private NewsRepository newsRepository;

    public static SimpleFragment newInstance(int position) {
        SimpleFragment fragment = new SimpleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        position = bundle.getInt("position");
        newsRepository = new NewsRepository();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoading && isVisible()) {
            isFirstLoading = false;
            switch (position) {
                case 0:
                    newsRepository.loadCommentNews("comment", new Outcome0<List<NewsData>>() {
                        @Override
                        public void call(List<NewsData> newsData) {
                            Utils.ensureChangeOnMainThread();
                            simpleViewModel.insertItemList(newsData);
                        }
                    }, new Outcome1<String>() {
                        @Override
                        public void error(Call call, String s) {

                        }
                    });
                    break;
                case 1:
                    newsRepository.loadCommentNews("hot", new Outcome0<List<NewsData>>() {
                        @Override
                        public void call(List<NewsData> newsData) {
                            Utils.ensureChangeOnMainThread();
                            simpleViewModel.insertItemList(newsData);
                        }
                    }, new Outcome1<String>() {
                        @Override
                        public void error(Call call, String s) {


                        }
                    });
                    break;
            }

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSimpleBinding.inflate(LayoutInflater.from(getContext()));
        simpleViewModel = ViewModelProviders.of(this).get(ListViewMyModel.class);
        binding.setViewModel(simpleViewModel);
        binding.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.e("ddddddddddddddddd");
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
