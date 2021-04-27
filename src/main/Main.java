package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    Stage window;

    public static void main( String[] args ) {
        launch( args );
    }

    @Override
    public void start( Stage primaryStage ) throws Exception {
        window = primaryStage;
        FXMLLoader mainMenuScene = new FXMLLoader( getClass().getResource( "../resources/view/mainMenu.fxml" ) );
        Parent root = mainMenuScene.load();

        window.setTitle( "Snake" );
        window.setScene( new Scene( root, 600, 600 ) );
        window.show();
    }
}
