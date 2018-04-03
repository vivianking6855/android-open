package com.learn.blockmonitor.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.learn.blockmonitor.BlockMonitor;
import com.learn.blockmonitor.R;
import com.learn.blockmonitor.model.BlockStackModel;
import com.learn.blockmonitor.model.LogModel;
import com.learn.blockmonitor.share.util.Const;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * StackLogList Fragment
 */

public class StackLogListFragment extends Fragment {

    private static final String SEPARATOR = "\n";
    private StackAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.stack_log_list_fragment, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.stack_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new StackItemDecoration(getContext()));
        adapter = new StackAdapter();
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        new LoadLogTask(this).execute(BlockMonitor.getInstance().getLogPath());
    }

    private static class LoadLogTask extends AsyncTask<String, Void, List<BlockStackModel>> {
        WeakReference<StackLogListFragment> fragmentWeakReference;

        LoadLogTask(StackLogListFragment fr) {
            fragmentWeakReference = new WeakReference<>(fr);
        }

        @Override
        protected List<BlockStackModel> doInBackground(String... strings) {
            if (strings[0] == null || strings[0].isEmpty()) {
                return null;
            }
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(strings[0])));
                String line;
                StringBuilder sb = new StringBuilder();
                List<BlockStackModel> mergedEntityList = new ArrayList<>();
                LogModel deviceInfo = null;
                LogModel cpuLog = null;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line).append(SEPARATOR);
                    //parse device info
                    if (line.startsWith("[app total memory]")) {
                        LogModel entity = LogModel.obtain();
                        entity.setContent(sb.toString());
                        entity.setType(LogModel.TYPE_DEVICE);
                        deviceInfo = entity;
                        sb.delete(0, sb.length());
                    } else if (line.startsWith("[drop frame count]")) {
                        //parse cpu and block stacktrace log
                        LogModel entity = LogModel.obtain();
                        entity.setContent(sb.toString());
                        entity.setType(LogModel.TYPE_CPU);
                        cpuLog = entity;
                        sb.delete(0, sb.length());
                    } else if (line.startsWith("================block end")) {
                        BlockStackModel entity = BlockStackModel.obtain();
                        entity.setCpu(cpuLog);
                        entity.setDevice(deviceInfo);
                        entity.setContent(sb.toString());
                        mergedEntityList.add(entity);
                        sb.delete(0, sb.length());
                    }
                }
                Collections.reverse(mergedEntityList);
                return mergedEntityList;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<BlockStackModel> result) {
            StackLogListFragment fr = fragmentWeakReference.get();
            if (fr != null) {
                fr.adapter.setData(result);
                fr.adapter.notifyDataSetChanged();
            }
        }
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
