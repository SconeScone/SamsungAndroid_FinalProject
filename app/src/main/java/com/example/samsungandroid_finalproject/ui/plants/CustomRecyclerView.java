package com.example.samsungandroid_finalproject.ui.plants;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class CustomRecyclerView extends RecyclerView {
    private View emptyView;

    private final AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            initEmptyView();
        }
    };

    public CustomRecyclerView(@NonNull Context context) {
        super(context);
    }

    public CustomRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initEmptyView() {
        if (emptyView != null) {
            if (getAdapter() == null || getAdapter().getItemCount() == 0) {
                emptyView.setAlpha(0f);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.animate()
                        .alpha(1f)
                        .setDuration(200)
                        .setListener(null);

                CustomRecyclerView.this.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.GONE);
                CustomRecyclerView.this.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        if (getAdapter() != null) {
            getAdapter().unregisterAdapterDataObserver(observer);
        }

        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }

        super.setAdapter(adapter);
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        initEmptyView();
    }
}
