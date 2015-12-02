package com.example.choa.examplequickreturn;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarLayout mAppBarLayout;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private RecyclerView recyclerView;
    private MyAdapter mAdapter;

    private QuickReturnBehavior mQuickReturnBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
        lp.setBehavior(new QuickReturnBehavior(this, null));

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setProgressViewEndTarget(true, 336); // set progressView's location
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyAdapter(initData());
        recyclerView.setAdapter(mAdapter);
    }

    private List<String> initData() {
        List<String> list = new ArrayList<>();

        for(int i=0; i<200; i++) {
            list.add("Hello - " + i);
        }

        return list;
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ItemHolder> {
        private List<String> mItems = new ArrayList<>();

        public MyAdapter(List<String> items) {
            mItems = items;
        }

        public void addAll(List<String> list) {
            mItems.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

            return new ItemHolder(v);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            holder.bind(mItems.get(position));
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        class ItemHolder extends RecyclerView.ViewHolder {
            private TextView textView;

            public ItemHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.text_item);
            }

            public void bind(String msg) {
                textView.setText(msg);
            }
        }
    }
}
