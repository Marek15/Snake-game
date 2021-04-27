package main.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;


public class MenuController {

    @FXML
    TextField playerName;
    @FXML
    Label gameOverScoreLabel;
    @FXML
    Slider diffcultySlider;

    private Stage window;
    private Parent root;
    private int difficulty;

    public void setDifficulty( int difficulty ) {
        this.difficulty = difficulty;
    }

    public void quitGame() {
        System.exit( 0 );
    }

    public void validatePlayerName( javafx.event.ActionEvent event ) throws Exception {
        String name = playerName.getText();
        switchToGame( event );
    }


    public void switchToGame( javafx.event.ActionEvent event ) throws Exception {

        FXMLLoader gameScene = new FXMLLoader( getClass().getResource( "/resources/view/game.fxml" ) );

        window = ( Stage ) ( ( Node ) event.getSource() ).getScene().getWindow();

        if ( diffcultySlider != null )
            difficulty = ( int ) diffcultySlider.getValue() - 1;

        GameController gameController = new GameController( window, difficulty );

        gameScene.setController( gameController );

        root = gameScene.load();

        window.setScene( new Scene( root ) );
        window.show();

        window.getScene().setOnKeyPressed( new EventHandler<KeyEvent>() {
            @Override
            public void handle( KeyEvent keyEvent ) {
                gameController.changeDirection( keyEvent );
            }
        } );
    }

    public void switchToMainMenu( javafx.event.ActionEvent event ) throws IOException {

        FXMLLoader mainMenu = new FXMLLoader( getClass().getResource( "/resources/view/mainMenu.fxml" ) );
        root = mainMenu.load();

        window = ( Stage ) ( ( Node ) event.getSource() ).getScene().getWindow();
        window.setScene( new Scene( root ) );
        window.show();

    }
}