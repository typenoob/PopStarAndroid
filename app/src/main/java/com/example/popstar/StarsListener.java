package com.example.popstar;

import android.util.Log;
import android.view.View;

public class StarsListener implements View.OnClickListener {
    public static boolean restrict = false;
    private final int h;
    private final int w;
    private final GameActivity ga;

    StarsListener(int h, int w, GameActivity ga) {
        this.h = h;
        this.w = w;
        this.ga = ga;
    }

    @Override
    public void onClick(View view) {
        if (restrict)
            return;
        if (ga.stars.getStatusAt(h, w) >= Stars.READY) {
            ga.stars.Pop(h, w);
            ga.stars.Fall();
            ga.stars.Checkout();
        } else {
            ga.stars.Release();
            ga.stars.Mark(h, w);
            ga.stars.Prepare();
        }
        // stars.printTrace();
    }
}
