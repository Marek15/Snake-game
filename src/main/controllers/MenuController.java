package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.dao.Alert;
import main.dao.HttpRequest;

import java.io.IOException;


public class MenuController {

    @FXML
    Label gameOverScoreLabel;

    @FXML
    Label gameOverDifficultyLabel;

    @FXML
    Slider difficultySlider;

    @FXML
    TextField nicknameField;

    @FXML
    Label gameOverAlertLabel;

    @FXML
    Label mainMenuAlertLabel;

    private Stage window;
    private Parent root;
    private int difficulty;
    private String nickname;

    public void setDifficulty( int difficulty ) {
        this.difficulty = difficulty;

    }

    public void setNickname( String nickname ) {
        this.nickname = nickname;
    }

    public void setNicknameFieldText( String nickname ) {
        nicknameField.setText( nickname );
    }

    public void setValueDifficultySlider( int value ) {
        this.difficultySlider.setValue( value );
    }

    public void quitGame() {
        System.exit( 0 );
    }

    public void validateInput( javafx.event.ActionEvent event ) throws Exception {

        Alert alert = new Alert( mainMenuAlertLabel );

        nickname = nicknameField.getText().trim();

        if ( nickname.length() > 10 ) {
            alert.error( "Nick must have a maximum of 10 letters." );
        } else
            switchToGame( event );

    }

    public void savePlayerScore() {

        Alert alert = new Alert( gameOverAlertLabel );

        if ( !nickname.isEmpty() ) {
            String postData = "nickname=" + nickname + "&score=" + gameOverScoreLabel.getText().trim() + "&difficulty=" + difficulty;
            System.out.println( "POST data => " + postData );
            int responseCode = HttpRequest.sendPOSTRequest( "https://snakeskola.herokuapp.com/api/score", postData );
            System.out.println( responseCode );
            if ( responseCode == 201 ) {
                alert.success( "Your score was saved!" );
            } else {
                alert.error( "Your score wasn't saved, because there is problem with server connection." );
            }
        } else {
            alert.warn( "Your score wasn't saved, because you didn't enter nickname." );
        }
    }

    public void switchToGame( ActionEvent event ) throws Exception {

        FXMLLoader gameScene = new FXMLLoader( getClass().getResource( "/resources/view/game.fxml" ) );

        window = ( Stage ) ( ( Node ) event.getSource() ).getScene().getWindow();

        if ( difficultySlider != null )
            difficulty = ( int ) difficultySlider.getValue();


        GameController gameController = new GameController( window, difficulty, nickname );

        gameScene.setController( gameController );

        root = gameScene.load();

        window.setScene( new Scene( root ) );
        window.show();
        window.getScene().setOnKeyPressed( gameController::changeDirection );
    }

    public void switchToMainMenu( javafx.event.ActionEvent event ) throws IOException {

        FXMLLoader mainMenu = new FXMLLoader( getClass().getResource( "/resources/view/mainMenu.fxml" ) );
        root = mainMenu.load();

        MenuController menuController = mainMenu.getController() ;
        menuController.setValueDifficultySlider( difficulty );
        menuController.setNicknameFieldText( nickname );


        window = ( Stage ) ( ( Node ) event.getSource() ).getScene().getWindow();
        window.setScene( new Scene( root ) );
        window.show();

    }

    public void switchToScoreTable( javafx.event.ActionEvent event ) throws IOException {

        FXMLLoader scoreTableView = new FXMLLoader( getClass().getResource( "/resources/view/scoreTable.fxml" ) );
        root = scoreTableView.load();


        window = ( Stage ) ( ( Node ) event.getSource() ).getScene().getWindow();
        window.setScene( new Scene( root ) );
        window.show();

    }
}
