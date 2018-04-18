package com.learn.blockmonitor.model;

import android.os.AsyncTask;
import android.util.Log;

import com.learn.blockmonitor.data.api.LogMan;
import com.learn.blockmonitor.data.util.Const;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Venjee_Shen on 2018/4/3.
 */


public class FileLogProvider implements ILogProvider<List<BlockStackModel>, String> {

    private static final String SEPARATOR = "\n";

    @Override
    public void fetchLogAsync(IFetchLogListener<List<BlockStackModel>, String> listener) {
        try {
            new LoadLogTask(listener).execute(LogMan.getInstance().getLogPath().getPath());
        } catch (Exception e) {
            Log.w(Const.BLOCK_TAG, "fetchLogAsync ex", e);
        }
    }

    private static class LoadLogTask extends AsyncTask<String, Void, List<BlockStackModel>> {
        WeakReference<IFetchLogListener<List<BlockStackModel>, String>> listenerWeakReference;

        LoadLogTask(IFetchLogListener<List<BlockStackModel>, String> fr) {
            listenerWeakReference = new WeakReference<>(fr);
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
            } catch (IOException e) {
                e.printStackTrace();
                IFetchLogListener<List<BlockStackModel>, String> fr = listenerWeakReference.get();
                if (fr != null) {
                    fr.onError(e.toString());
                }
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
            IFetchLogListener<List<BlockStackModel>, String> fr = listenerWeakReference.get();
            if (fr != null) {
                if (result == null) {
                    fr.onError("fetch log error");
                } else {
                    fr.onSuccess(result);
                }
            }
        }
    }
}
