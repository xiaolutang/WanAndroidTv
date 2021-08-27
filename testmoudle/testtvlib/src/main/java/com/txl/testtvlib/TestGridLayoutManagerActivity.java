package com.txl.testtvlib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TestGridLayoutManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_grid_layout_manager);
        RecyclerView recyclerView = findViewById(R.id.recycler_test_grid);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new TestItemDecoration());
        ItemDecorAdapter itemDecorAdapter = new ItemDecorAdapter();
        itemDecorAdapter.data.add("娃哈哈");
        itemDecorAdapter.data.add("娃哈哈");
        itemDecorAdapter.data.add("娃哈哈");
        itemDecorAdapter.data.add("娃哈哈");
        itemDecorAdapter.data.add("娃哈哈");
        itemDecorAdapter.data.add("娃哈哈");
        itemDecorAdapter.data.add("娃哈哈");
        itemDecorAdapter.data.add("娃哈哈");
        itemDecorAdapter.data.add("娃哈哈");
        itemDecorAdapter.data.add("娃哈哈");
        recyclerView.setAdapter(itemDecorAdapter);
    }

    private static class ItemDecorAdapter extends RecyclerView.Adapter<TextViewHolder> {
        ArrayList<String> data = new ArrayList<String>();

        @NonNull
        @Override
        public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_decoration_text,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {
            holder.textView.setText(""+data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private static class TextViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_text_item);
        }
    }

    private static class TestItemDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 60, 60);
        }
    }
}