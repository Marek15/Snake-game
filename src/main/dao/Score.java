package main.dao;

import javafx.scene.control.Label;

public class Score {


    private final Label label;
    private Long score;

    public Score( Label label ) {
        score = 0L;
        this.label = label;
    }

    public void countdown( Long amount ) {
        score -= amount;
        displayScore();
    }

    public void add( Long amount ) {
        score += amount;
        displayScore();
    }

    public void displayScore() {
        label.setText( score.toString() );
    }

    public Long getScore() {
        return score;
    }

}
