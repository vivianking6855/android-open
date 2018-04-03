package com.learn.blockmonitor.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.learn.blockmonitor.R;
import com.learn.blockmonitor.model.BlockStackModel;
import com.learn.blockmonitor.presenter.LogListPresenter;
import com.learn.blockmonitor.share.base.BaseMVPLazyFragment;
import com.learn.blockmonitor.share.util.Const;

import java.util.List;

/**
 * StackLogList Fragment
 */

public class StackLogListFragment extends BaseMVPLazyFragment<ILogDisplayer,LogListPresenter>
        implements ILogDisplayer<List<BlockStackModel>> {

    private StackAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.stack_log_list_fragment;
    }

    @Override
    protected void initViews(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.stack_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new StackItemDecoration(getContext()));
        adapter = new StackAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void loadData() {
        mPresenter.fetchData();
    }

    @Override
    protected LogListPresenter createPresenter() {
        return new LogListPresenter();
    }

    @Override
    public void onDisplay(List<BlockStackModel> data) {
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.detachReference();
    }

    private static class StackAdapter extends RecyclerView.Adapter<StackAdapter.StackViewHolder> {

        private List<BlockStackModel> stacks;

        void setData(List<BlockStackModel> stacks) {
            this.stacks = stacks;
        }

        @Override
        public StackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stack_item_layout, parent, false);
            return new StackViewHolder(view);
        }

        @Override
        public void onBindViewHolder(StackViewHolder holder, int position) {
            BlockStackModel log = stacks.get(position);
            holder.itemView.setTag(log);
            holder.textView.setText(log.getContent().substring(0, 200));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BlockStackModel logEntity = (BlockStackModel) v.getTag();
                    String detail = logEntity.getDevice().getContent() +
                            logEntity.getCpu().getContent() +
                            logEntity.getContent();
                    Intent intent = new Intent(v.getContext(), StackLogDetailActivity.class);
                    intent.putExtra(Const.KEY_DETAIL_LOG, detail);
                    v.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return stacks == null ? 0 : stacks.size();
        }

        static class StackViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            StackViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.log_item_text);
            }
        }
    }

    private static class StackItemDecoration extends RecyclerView.ItemDecoration {

        private int itemDividerHeight = 0;
        private Paint dividerPaint;

        StackItemDecoration(Context context) {
            itemDividerHeight = context.getResources().getDimensionPixelSize(R.dimen.divider_height);
            dividerPaint = new Paint();
            dividerPaint.setColor(Color.GRAY);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
            int childCount = parent.getChildCount();
            Rect rect = new Rect();
            for (int i = 1; i < childCount; i++) {
                View child = parent.getChildAt(i);
                rect.top = child.getTop() - itemDividerHeight;
                rect.right = child.getRight();
                rect.left = child.getLeft();
                rect.bottom = child.getTop();
                c.drawRect(rect, dividerPaint);
            }
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.top = itemDividerHeight;
        }
    }

}
