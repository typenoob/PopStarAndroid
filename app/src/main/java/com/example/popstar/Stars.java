package com.example.popstar;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.Serializable;
import java.util.Random;

public class Stars implements Runnable, Serializable { //讲解二：子线程
    private static final long serialVersionUID = 1L;
    private int status[][];
    private int count;
    private static GameActivity ga;
    private static Handler handler;
    public static final int WIDTH = 10, HEIGHT = 10, RED = 0, GREEN = 1, BLUE = 2, YELLOW = 3, PURPLE = 4, STOP = -1,
            READY = 10, DESTROY = -11;
    Stars(GameActivity ga) {
        this.ga = ga;
        this.handler = new Handler(Looper.getMainLooper()) { //讲解三：handler操作ui
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == -1) ga.nextLevel(0);
                else {
                    ga.setBonus("Bonus:" + Integer.toString(2000 - (int) (20 * Math.pow(msg.what, 2))));
                    ga.refresh();
                }
            }
        };
        status = new int[WIDTH + 2][HEIGHT + 2];
        count = 0;
        for (int i = 0; i <= HEIGHT + 1; i++) {
            status[i][0] = STOP;
            status[i][WIDTH + 1] = STOP;
        }
        for (int j = 0; j <= WIDTH + 1; j++) {
            status[0][j] = STOP;
            status[HEIGHT + 1][j] = STOP;
        }

        for (int i = 1; i <= HEIGHT; i++)
            for (int j = 1; j <= WIDTH; j++)
                status[i][j] = randomColor();
    }


    private int randomColor() {
        return new Random().nextInt(5);
    }

    private void Merge() {
        int count = 0;
        for (int j = 1; j <= WIDTH; j++)
            if (status[HEIGHT][j] == STOP)
                count++;
            else if (count > 0)
                for (int i = 1; i <= HEIGHT; i++)
                    status[i][j - count] = status[i][j];
        for (int j = WIDTH - count + 1; j <= WIDTH; j++)
            for (int i = 1; i <= HEIGHT; i++)
                status[i][j] = STOP;
    }

    public void Checkout() {
        for (int ii = 1; ii <= HEIGHT; ii++)
            for (int jj = 1; jj <= WIDTH; jj++)
                if (status[ii][jj] != STOP) {
                    for (int i = 0; i <= 1; i++)
                        for (int j = 0; j <= 1; j++)
                            if (status[ii][jj] == status[ii + i * (j * 2 - 1)][jj + (1 - i) * (j * 2 - 1)])
                                return;

                }
        new Thread(this).start();
    }

    public int[][] getStatus() {
        return status;
    }
    public void setGameActivity(GameActivity ga){
        this.ga=ga;
    }
    public void setHandler(Handler handler){
        this.handler=handler;
    }
    public void Mark(int h, int w) {
        if (status[h][w] == STOP || status[h][w] >= READY)
            return;
        count++;
        ga.border(h, w);
        status[h][w] += READY;
        for (int i = 0; i <= 1; i++)
            for (int j = 0; j <= 1; j++)
                if (status[h][w] - READY == status[h + i * (j * 2 - 1)][w + (1 - i) * (j * 2 - 1)])
                    Mark(h + i * (j * 2 - 1), w + (1 - i) * (j * 2 - 1));
    }

    public int getStatusAt(int h, int w) {
        return status[h][w];
    }

    public void Prepare() {
        ga.sc.Calculate();
        ga.setInfo();
    }

    public void Pop(int h, int w) {
        if (count == 1) {
            status[h][w] -= READY;
            return;
        }
        if (status[h][w] == STOP || status[h][w] == DESTROY)
            return;
        status[h][w] = DESTROY;
        for (int i = 0; i <= 1; i++)
            for (int j = 0; j <= 1; j++)
                if (status[h + i * (j * 2 - 1)][w + (1 - i) * (j * 2 - 1)] >= READY)
                    Pop(h + i * (j * 2 - 1), w + (1 - i) * (j * 2 - 1));
    }

    public void Fall() {
        ga.sc.addScore();
        for (int j = 1; j <= WIDTH; j++) {
            int count = 0;
            for (int i = HEIGHT; i >= 1; i--)
                if (status[i][j] == DESTROY)
                    count++;
                else
                    status[i + count][j] = status[i][j];
            for (int i = 1; i <= count; i++)
                status[i][j] = STOP;
        }
        Merge();
        ga.refresh();
    }

    public void Release() {
        count = 0;
        for (int i = 1; i <= HEIGHT; i++)
            for (int j = 1; j <= WIDTH; j++)
                if (status[i][j] >= READY) {
                    status[i][j] -= READY;
                    ga.unBorder(i, j);
                }
    }

    public int getCount() {
        return count;
    }

    public void run() {
        StarsListener.restrict = true;
        int count = 0;
        for (int j = 1; j <= WIDTH; j++)
            for (int i = 1; i <= HEIGHT; i++)
                if (status[i][j] != STOP) {
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        System.exit(0);
                    }
                    if (count < 10)
                        count++;
                    status[i][j] = STOP;
                    handler.sendEmptyMessage(count);
                }
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            System.exit(0);
        }
        handler.sendEmptyMessage(-1);
        StarsListener.restrict = false;
    }// Destroy remain stars and entry next level
}