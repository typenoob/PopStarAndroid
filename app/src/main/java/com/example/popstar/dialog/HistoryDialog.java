package com.example.popstar.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.popstar.R;
import com.example.popstar.database.AppDatabase;
import com.example.popstar.database.Record;
import com.example.popstar.database.RecordDao;

import java.util.List;

public class HistoryDialog extends DialogFragment {
    private TableLayout tableLayout;
    private TextView dialogTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.table, container, false);
        tableLayout = view.findViewById(R.id.tableLayout);
        dialogTitle=view.findViewById(R.id.dialogTitle);
        tableLayout.setStretchAllColumns(true);
        dialogTitle.setText("历史记录");
        TableRow rowHead = new TableRow(getContext());
        TextView timeHead = new TextView(getContext());
        TextView scoreHead = new TextView(getContext());
        timeHead.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        timeHead.setText("日期");
        rowHead.addView(timeHead);
        scoreHead.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        scoreHead.setText("分数");
        rowHead.addView(scoreHead);
        tableLayout.addView(rowHead);
        RecordDao recordDao = AppDatabase.getInstance(getContext()).recordDao();
        List<Record> recordList = recordDao.getAll();
        for (Record record : recordList) {
            TableRow row = new TableRow(getContext());
            TextView time = new TextView(getContext());
            TextView score = new TextView(getContext());
            time.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            time.setText(record.getFormattedTime());
            row.addView(time);
            score.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            score.setText(String.valueOf(record.getScore()));
            row.addView(score);
            tableLayout.addView(row);
        }
        return view;
    }
}