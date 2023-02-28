package com.example.popstar;
import java.io.Serializable;
import java.lang.Math;
import java.util.logging.Handler;

public class Score implements Serializable {
    private static final long serialVersionUID = 1L;
    private int target, best, present, toadd, number;
    private static GameActivity ga;

    Score(GameActivity ga) {
        this.ga = ga;
        target = 1000;
        present = 0;
        best = 0;
        number = 1;
    }
    public void setGameActivity(GameActivity ga){
        this.ga=ga;
    }


    public void Calculate() {
        if(ga.stars.getCount()==1) toadd=0;
        else toadd = (int) (5 * Math.pow(ga.stars.getCount(), 2));
    }

    public void addScore() {
        present += toadd;
    }

    public void addScore(int toadd) {
        present += toadd;
    }

    public void Update() {
        if (target == 1000)
            target += 2000;
        else
            target += 3000;
        number += 1;
    }

    public String getTarget() {
        return Integer.toString(target);
    }

    public String getPresent() {
        return Integer.toString(present);
    }

    public String getBest() {
        return Integer.toString(best);
    }

    public String getToadd() {
        return Integer.toString(toadd);
    }

    public String getNumber() {
        return Integer.toString(number);
    }

    public String getCount() {
        return Integer.toString(ga.stars.getCount());
    }

    public boolean Reach() {
        return present >= target;
    }
}