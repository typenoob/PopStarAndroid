package com.example.popstar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.popstar.database.AppDatabase;
import com.example.popstar.database.Record;
import com.example.popstar.database.RecordDao;
import com.example.popstar.dialog.HistoryDialog;
import com.example.popstar.dialog.RankDialog;
import com.example.popstar.endpoint.UserResponse;
import com.example.popstar.endpoint.UserService;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private UserService userService;
    private Handler handler;
    private static final String TAG = "App/MainActivity/";
    private ImageView signal;
    private TextView name;
    private Button start;
    private Button resume;
    private Button scoreBoard;
    private ImageButton logout;
    private ImageButton history;
    private AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App context = (App) getApplication();
        Timer timer = new Timer();
        if (!getIntent().getBooleanExtra("offline", false))
            timer.schedule(new checkLogin(context, this), 0, 5000);
        setContentView(R.layout.activity_main);
        init();
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                name.setText(msg.getData().getString("name"));
            }
        };
        if (!context.getIsLogin()) {
            signal.setImageResource(R.drawable.ic_baseline_signal_wifi_connected_no_internet_4_24);
            scoreBoard.setEnabled(false);
            name.setText("网络未连接");
            Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), "离线状态下无法记录成绩至高分榜", BaseTransientBottomBar.LENGTH_SHORT);
            snackbar.show();
        }
        start.setOnClickListener(view -> {
            Intent intent = new Intent();
            /* 指定intent要启动的类 */
            intent.setClass(MainActivity.this, GameActivity.class);
            /* 启动一个新的Activity */
            startActivity(intent);
        });
        if (context.getStars() != null && context.getScore() != null)
            resume.setEnabled(true);
        else
            resume.setEnabled(false);
        resume.setOnClickListener(view -> {
            Intent intent = new Intent();
            /* 指定intent要启动的类 */
            intent.setClass(MainActivity.this, GameActivity.class);
            intent.putExtra("resume", true);
            /* 启动一个新的Activity */
            startActivity(intent);
        });
        scoreBoard.setOnClickListener(view -> showRank());
        history.setOnClickListener(view -> showHistory());
        logout.setOnClickListener(view -> {
            context.setIs_login(false);
            context.setToken("");
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            timer.cancel();
            this.finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        App context = (App) getApplication();
        if (context.getStars() != null && context.getScore() != null)
            resume.setEnabled(true);
        else
            resume.setEnabled(false);
    }

    public void showHistory() {
        DialogFragment historyDialog = new HistoryDialog();
        historyDialog.show(getSupportFragmentManager(), "historyDialog");
    }

    public void showRank() {
        DialogFragment historyDialog = new RankDialog();
        historyDialog.show(getSupportFragmentManager(), "rankDialog");
    }

    private void init() {
        db = AppDatabase.getInstance(getApplicationContext());
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        RecordDao recordDao = db.recordDao();
        recordDao.insertAll(new Record(100));
        List<Record> records = recordDao.getAll();
        Log.d(TAG, records.toString());
        start = findViewById(R.id.start);
        scoreBoard = findViewById(R.id.score_board);
        resume = findViewById(R.id.resume);
        history = findViewById(R.id.history);
        signal = findViewById(R.id.signal);
        name = findViewById(R.id.tv_name);
        logout = findViewById(R.id.logout);
        userService = ((App) getApplication()).getUserService();
    }

    class checkLogin extends TimerTask {
        private final App context;
        private MainActivity packageContext;
        private UserService userService;

        checkLogin(App context, MainActivity packageContext) {
            this.context = context;
            this.userService = context.getUserService();
            this.packageContext = packageContext;
        }

        public void run() {
            if (!context.getIs_login()) {
                Intent intent = new Intent();
                intent.setClass(packageContext, LoginActivity.class);
                intent.putExtra("loginExpired", true);
                packageContext.finish();
                startActivity(intent);
                this.cancel();
            }
            Call<UserResponse> call = userService.getCurrentUser("Bearer " + context.getToken());
            call.enqueue(new Callback<UserResponse>() {
                //请求成功时回调
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    //请求处理,输出结果
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            context.setIs_login(true);
                            Bundle bundle = new Bundle();
                            bundle.putString("name", response.body().getData().getName());
                            Message msg = Message.obtain();
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        } else context.setIs_login(false);
                    } else context.setIs_login(false);
                }

                //请求失败时候的回调
                @Override
                public void onFailure(Call<UserResponse> call, Throwable throwable) {
                    System.out.println("连接失败");
                    context.setIs_login(false);
                }
            });
        }
    }
}