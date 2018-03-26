package com.wenxi.learn.blockmonitor.ui;

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

import com.wenxi.learn.blockmonitor.BlockMonitor;
import com.wenxi.learn.blockmonitor.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Venjee_Shen on 2018/3/26.
 *
 */

public class StackLogListFragment extends Fragment {

    private static final String SEPARATOR = "\n";
    private StackAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.stack_log_list_fragment,container,false);
        RecyclerView recyclerView = v.findViewById(R.id.stack_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new StackAdapter();
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        new LoadLogTask(this).execute(BlockMonitor.getInstance().getLogPath());
    }

    private static class LoadLogTask extends AsyncTask<String,Void,List<String>> {
        WeakReference<StackLogListFragment> fragmentWeakReference;

        LoadLogTask(StackLogListFragment fr){
            fragmentWeakReference = new WeakReference<>(fr);
        }

        @Override
        protected List<String> doInBackground(String... strings) {
            if(strings[0]==null || strings[0].isEmpty()){
                return null;
            }
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(strings[0])));
                String line;
                StringBuilder sb = new StringBuilder();
                List<String> results = new ArrayList<>();
                while ((line = bufferedReader.readLine())!=null){
                    if(line.startsWith("[start]")){
                        sb.append(line);
                        while ((line = bufferedReader.readLine())!=null){
                            sb.append(line).append(SEPARATOR);
                            if(line.startsWith("[stack]")){
                                results.add(sb.toString());
                                sb.delete(0,sb.length());
                                break;
                            }
                        }
                    }else {
                        if(line.startsWith("=")){
                            if(line.contains("block begin")){
                                sb.append(line);
                                while ((line = bufferedReader.readLine())!=null){
                                    sb.append(line).append(SEPARATOR);
                                    if(line.startsWith("=") && line.contains("block end")){
                                        results.add(sb.toString());
                                        sb.delete(0,sb.length());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                return results;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(bufferedReader != null) {
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
        protected void onPostExecute(List<String> result) {
            StackLogListFragment fr = fragmentWeakReference.get();
            if(fr!=null){
                fr.adapter.setData(result);
                fr.adapter.notifyDataSetChanged();
            }
        }
    }

    private static class StackAdapter extends RecyclerView.Adapter<StackAdapter.StackViewHolder>{

        private List<String> stacks;

        void setData(List<String> stacks){
            this.stacks = stacks;
        }

        @Override
        public StackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stack_item_layout,parent,false);
            return new StackViewHolder(view);
        }

        @Override
        public void onBindViewHolder(StackViewHolder holder, int position) {
            String log = stacks.get(position);
            holder.textView.setText(log.substring(0,100));
        }

        @Override
        public int getItemCount() {
            return stacks == null?0:stacks.size();
        }

        static class StackViewHolder extends RecyclerView.ViewHolder{
            TextView textView;
            StackViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.log_item_text);
            }
        }
    }

}
