package main.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import java.io.IOException;


public class MenuController {

    @FXML
    Label gameOverScoreLabel;



    @FXML
    Label gameOverDifficultyLabel;

    @FXML
    Slider difficultySlider;

    private Stage window;
    private Parent root;
    private int difficulty;

    public void setDifficulty( int difficulty ) {
        this.difficulty = difficulty;

    }

    public void setValueDifficultySlider( int value ) {
        this.difficultySlider.setValue( value );
    }

    public void quitGame() {
        System.exit( 0 );
    }


    public void switchToGame( javafx.event.ActionEvent event ) throws Exception {

        FXMLLoader gameScene = new FXMLLoader( getClass().getResource( "/resources/view/game.fxml" ) );

        window = ( Stage ) ( ( Node ) event.getSource() ).getScene().getWindow();

        if ( difficultySlider != null )
            difficulty = ( int ) difficultySlider.getValue();


        GameController gameController = new GameController( window, difficulty );

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

        window = ( Stage ) ( ( Node ) event.getSource() ).getScene().getWindow();
        window.setScene( new Scene( root ) );
        window.show();

    }
}
