package main.dao;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class PlayerData {

    private  SimpleStringProperty nickname;
    private SimpleLongProperty score;


    public PlayerData( String nickname, long score ) {
        this.nickname = new SimpleStringProperty(nickname);
        this.score = new SimpleLongProperty(score);
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


}
