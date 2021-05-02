package main.dao;

import javafx.application.Platform;
import javafx.scene.control.Label;
import java.util.Timer;
import java.util.TimerTask;

public class Score {
    private final int displayTimeOfScoreInfo = 1000;

    private final Label scoreLabel;
    private final Label scoreInfoLabel;
    private Long score;
    private final Timer timer;

    public Score( Label score, Label scoreInfoLabel ) {
        this.score = 0L;
        this.scoreLabel = score;
        this.scoreInfoLabel = scoreInfoLabel;
        timer = new Timer();

        displayScore( "", false );
    }

    public void countdown( Long amount ) {
        score -= amount;
        displayScore("- " + amount, true );
    }

    public void add( Long amount ) {
        score += amount;
        displayScore("+ " + amount, false);
    }

    public void displayScore(String infoText, boolean isCountdown ) {
        if ( isCountdown ) {
            scoreInfoLabel.getStyleClass().add( "scoreInfoRed" );
            scoreInfoLabel.getStyleClass().removeAll( "scoreInfoGreen" );
        }
        else {
            scoreInfoLabel.getStyleClass().add( "scoreInfoGreen" );
            scoreInfoLabel.getStyleClass().removeAll( "scoreInfoRed" );
        }

        scoreLabel.setText( score.toString() );
        scoreInfoLabel.setText( infoText );

        timer.schedule( new TimerTask() {
            @Override
            public void run() {
                Platform.runLater( () -> scoreInfoLabel.setText( "" ) );
            }
        }, displayTimeOfScoreInfo );

    }

    public Long getScore() {
        return score;
    }

}
