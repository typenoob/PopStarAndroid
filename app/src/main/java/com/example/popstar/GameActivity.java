package com.example.popstar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.popstar.endpoint.ScoreResponse;
import com.example.popstar.endpoint.ScoreService;
import com.example.popstar.io.FileIO;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameActivity extends AppCompatActivity {
    private ImageButton[][] starbt;
    private Button pause;
    private Drawable[] color = new Drawable[5];
    private Drawable[] color_ = new Drawable[5];
    protected Stars stars;
    protected Score sc;
    private TextView best, stage, target, score, info, bonus;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        init();
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                System.out.println("123"+msg.what);
                best.setText(String.valueOf(msg.what));
            }
        };
        App context = (App) getApplication();
        ScoreService scoreService = context.getScoreService();
        Call<ScoreResponse> call = scoreService.getBestScore("Bearer " + context.getToken());
        call.enqueue(new Callback<ScoreResponse>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<ScoreResponse> call, Response<ScoreResponse> response) {
                //请求处理,输出结果
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        handler.sendEmptyMessage(Integer.parseInt(response.body().getData().get(0).getScore()));
                    }
                }
            }

            //请求失败时候的回调
            @Override
            public void onFailure(Call<ScoreResponse> call, Throwable throwable) {
                System.out.println("连接失败");
            }
        });
        pause.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
            builder.setTitle("暂停");
            builder.setPositiveButton("继续", (dialog, id) -> {
            });
            builder.setNegativeButton("保存并退出", (dialog, id) -> {
                save();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void save() {
        stars.Release();
        App context = (App) getApplication();
        context.setStars(stars);
        context.setScore(sc);
        FileIO.writeObject(getApplicationContext(), "stars.obj", stars);
        FileIO.writeObject(getApplicationContext(), "score.obj", sc);
        this.finish();
    }

    private void record() {
        App context = (App) getApplication();
        context.setStars(null);
        context.setScore(null);
        ScoreService scoreService = context.getScoreService();
        Call<ScoreResponse> call = scoreService.commitScore(new ScoreService.ScoreRequest(sc.getPresent()), "Bearer " + context.getToken());
        call.enqueue(new Callback<ScoreResponse>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<ScoreResponse> call, Response<ScoreResponse> response) {
                //请求处理,输出结果
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {

                        Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), "游玩记录已上传", BaseTransientBottomBar.LENGTH_SHORT);
                        snackbar.show();
                    }
                }
            }

            //请求失败时候的回调
            @Override
            public void onFailure(Call<ScoreResponse> call, Throwable throwable) {
                System.out.println("连接失败");
            }
        });
        this.finish();
    }

    private void init() {
        sc = new Score(this);
        Intent intent = getIntent();
        Boolean resume = intent.getBooleanExtra("resume", false);
        if (resume) {
            App context = (App) getApplication();
            stars = context.getStars();
            stars.setGameActivity(GameActivity.this);
            stars.setHandler(new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == -1) nextLevel(Integer.parseInt(getBonus()));
                    else {
                        setBonus("Bonus:" + Integer.toString(2000 - (int) (20 * Math.pow(msg.what, 2))));
                        refresh();
                    }
                }
            });
            sc = context.getScore();
            stars.setGameActivity(GameActivity.this);
        } else {
            stars = new Stars(this);
            sc = new Score(this);
        }
        best = (TextView) findViewById(R.id.best);
        stage = (TextView) findViewById(R.id.stage);
        target = (TextView) findViewById(R.id.target);
        score = (TextView) findViewById(R.id.score);
        info = (TextView) findViewById(R.id.info);
        bonus = (TextView) findViewById(R.id.bonus);
        pause = (Button) findViewById(R.id.pause);
        for (int i = 0; i < 5; i++) {
            color[i] = pattern(i);
            color_[i] = pattern_(i);
        }
        starbt = new ImageButton[Stars.HEIGHT + 1][Stars.WIDTH + 1];
        for (int i = 1; i <= Stars.HEIGHT; i++)
            for (int j = 1; j <= Stars.WIDTH; j++) {
                int buttonId = getResources().getIdentifier("ImageButton" + (i - 1) + (j - 1), "id", getPackageName());
                starbt[i][j] = findViewById(buttonId);
                starbt[i][j].setOnClickListener(new StarsListener(i, j, this));
            }


        refresh();
    }

    private Drawable pattern(int color) {
        Drawable star = null;
        switch (color) {
            case Stars.RED:
                star = getResources().getDrawable(R.drawable.star_red);
                break;
            case Stars.GREEN:
                star = getResources().getDrawable(R.drawable.star_green);
                break;
            case Stars.BLUE:
                star = getResources().getDrawable(R.drawable.star_blue);
                break;
            case Stars.YELLOW:
                star = getResources().getDrawable(R.drawable.star_yellow);
                break;
            case Stars.PURPLE:
                star = getResources().getDrawable(R.drawable.star_purple);
                break;
        }
        return star;
    }

    private Drawable pattern_(int color) {
        Drawable star = null;
        switch (color) {
            case Stars.RED:
                star = getResources().getDrawable(R.drawable.star_red_);
                break;
            case Stars.GREEN:
                star = getResources().getDrawable(R.drawable.star_green_);
                break;
            case Stars.BLUE:
                star = getResources().getDrawable(R.drawable.star_blue_);
                break;
            case Stars.YELLOW:
                star = getResources().getDrawable(R.drawable.star_yellow_);
                break;
            case Stars.PURPLE:
                star = getResources().getDrawable(R.drawable.star_purple_);
                break;
        }
        star.setBounds(0, 0, 20, 20);
        return star;
    }

    public void border(int h, int w) {
        starbt[h][w].setImageDrawable(color_[stars.getStatusAt(h, w)]);
    }

    public void unBorder(int h, int w) {
        starbt[h][w].setImageDrawable(color[stars.getStatusAt(h, w)]);
    }

    public void setInfo() {
        info.setText(sc.getCount() + "  BLOCKS  " + sc.getToadd() + "  POINTS");
    }

    public void setBonus(String bonus) {
        this.bonus.setText(bonus);
    }

    public String getBonus() {
        return this.bonus.getText().toString().substring(6, this.bonus.getText().length());
    }

    public void nextLevel(int bonus) {
        if (bonus > 0)
            sc.addScore(bonus);
        this.bonus.setText(null);
        refresh();
        if (sc.Reach()) {
            sc.Update();
            AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
            builder.setMessage("新的目标:" + sc.getTarget())
                    .setTitle("恭喜进入下一关！");
            builder.setPositiveButton("确认", (dialog, id) -> {
            });
            builder.setNegativeButton("保存并退出", (dialog, id) -> {
                save();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            // JOptionPane.showMessageDialog(null, "新的目标:" + sc.getTarget(), "恭喜进入下一关！", JOptionPane.WARNING_MESSAGE);
            stars = (new Stars(this));
            refresh();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
            builder.setMessage("目标未达成，大侠请下次再来！")
                    .setTitle("游戏结束");
            builder.setPositiveButton("确认", (dialog, id) -> {
                record();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


    public void refresh() {
        score.setText(sc.getPresent());
        target.setText(sc.getTarget());
        stage.setText(sc.getNumber());
        for (int i = 1; i <= Stars.HEIGHT; i++)
            for (int j = 1; j <= Stars.WIDTH; j++) {
                switch (stars.getStatusAt(i, j)) {
                    case Stars.STOP:
                        starbt[i][j].setVisibility(View.INVISIBLE);
                        break;
                    default:
                        starbt[i][j].setVisibility(View.VISIBLE);
                        starbt[i][j].setImageDrawable(color[stars.getStatusAt(i, j)]);
                }
            }
    }
}
