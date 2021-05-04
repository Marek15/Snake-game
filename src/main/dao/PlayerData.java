package main.dao;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class PlayerData {

    private final SimpleStringProperty nickname;
    private final SimpleLongProperty score;
    private final SimpleIntegerProperty difficulty;


    public PlayerData( String nickname, long score, int difficulty ) {
        this.nickname = new SimpleStringProperty( nickname );
        this.score = new SimpleLongProperty( score );
        this.difficulty = new SimpleIntegerProperty( difficulty );
    }

    public String getNickname() {
        return nickname.get();
    }

    public SimpleStringProperty nicknameProperty() {
        return nickname;
    }

    public void setNickname( String nickname ) {
        this.nickname.set( nickname );
    }

    public long getScore() {
        return score.get();
    }

    public SimpleLongProperty scoreProperty() {
        return score;
    }

    public void setScore( int score ) {
        this.score.set( score );
    }

    public int getDifficulty() {
        return difficulty.get();
    }

    public void setDifficulty( int difficulty ) {
        this.difficulty.set( difficulty );
    }

    public SimpleIntegerProperty difficultyProperty() {
        return difficulty;
    }
}
