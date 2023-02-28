package com.example.popstar.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.popstar.App;
import com.example.popstar.R;
import com.example.popstar.database.Record;
import com.example.popstar.endpoint.Score;
import com.example.popstar.endpoint.ScoreResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankDialog extends DialogFragment {
    private TableLayout tableLayout;
    private TextView dialogTitle;
    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.table, container, false);
        tableLayout = view.findViewById(R.id.tableLayout);
        dialogTitle = view.findViewById(R.id.dialogTitle);
        tableLayout.setStretchAllColumns(true);
        dialogTitle.setText("高分榜");
        TableRow rowHead = new TableRow(getContext());
        TextView userHead = new TextView(getContext());
        TextView timeHead = new TextView(getContext());
        TextView scoreHead = new TextView(getContext());
        userHead.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        userHead.setText("用户名");
        rowHead.addView(userHead);
        timeHead.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        timeHead.setText("日期");
        rowHead.addView(timeHead);
        scoreHead.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        scoreHead.setText("分数");
        rowHead.addView(scoreHead);
        tableLayout.addView(rowHead);
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                List<Score> recordList = (List<Score>) msg.obj;
                for (Score record : recordList) {
                    TableRow row = new TableRow(getContext());
                    TextView user = new TextView(getContext());
                    TextView time = new TextView(getContext());
                    TextView score = new TextView(getContext());
                    user.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    user.setText(record.getName());
                    row.addView(user);
                    time.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    time.setText(record.getFormattedTime());
                    row.addView(time);
                    score.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    score.setText(record.getScore());
                    row.addView(score);
                    tableLayout.addView(row);
                }
            }
        };
        App context = (App) getActivity().getApplication();
        Call<ScoreResponse> call = context.getScoreService().getScores();
        call.enqueue(new Callback<ScoreResponse>() {
            @Override
            public void onResponse(Call<ScoreResponse> call, Response<ScoreResponse> response) {
                handler.sendMessage(Message.obtain(handler, 0, response.body().getData()));
            }

            @Override
            public void onFailure(Call<ScoreResponse> call, Throwable throwable) {
                System.out.println("连接错误");
            }
        });
        return view;
    }
}