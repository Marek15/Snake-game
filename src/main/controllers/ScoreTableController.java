package main.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.dao.Alert;
import main.dao.HttpRequest;
import main.dao.PlayerData;
import org.json.JSONArray;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ScoreTableController implements Initializable {


    @FXML
    TableView<PlayerData> scoreTable;

    @FXML
    Label alertLabel;

    ObservableList<PlayerData> tableData;


    Alert alert;

    @Override
    public void initialize( URL url, ResourceBundle resourceBundle ) {

        alert = new Alert( alertLabel );

        scoreTable.getItems().clear();
        scoreTable.getColumns().clear();


        tableData = FXCollections.observableArrayList();

        getData();

        //Creating columns
        TableColumn nickNameCol = new TableColumn( "Nickname" );
        nickNameCol.setCellValueFactory( new PropertyValueFactory<>( "nickname" ) );

        TableColumn diffCol = new TableColumn( "Difficulty" );
        diffCol.setCellValueFactory( new PropertyValueFactory( "difficulty" ) );

        TableColumn scoreCol = new TableColumn( "Score" );
        scoreCol.setCellValueFactory( new PropertyValueFactory( "score" ) );


        nickNameCol.setReorderable( false );
        scoreCol.setReorderable( false );
        diffCol.setReorderable( false );

        diffCol.setSortType( TableColumn.SortType.ASCENDING );

        scoreTable.setItems( tableData );
        scoreTable.getColumns().addAll( nickNameCol, diffCol, scoreCol );


    }


    public void refreshTable() {

        scoreTable.getItems().clear();

        getData();

        scoreTable.setItems( tableData );

        alert.success( "Successfully refreshed!" );

    }

    private void getData() {

        String response = HttpRequest.sendGETRequest( "https://snakeskola.herokuapp.com/api/score" );

        tableData.clear();

        if ( response.isEmpty() ) {

            alert.error( "There is problem with server connection." );


        } else if ( response.equals( "201" ) ) {

            alert.success( "Successfully loaded!" );

        } else {
            JSONArray jsonData = new JSONArray( response );

            for ( int i = 0; i < jsonData.length(); i++ ) {

                String nickname = jsonData.getJSONObject( i ).getString( "nickname" );
                long score = Long.parseLong( jsonData.getJSONObject( i ).getString( "score" ) );
                int difficulty = Integer.parseInt( jsonData.getJSONObject( i ).getString( "difficulty" ) );


                tableData.add( new PlayerData( nickname, difficulty, score ) );
            }

            alert.success( "Successfully loaded!" );
        }
    }

    public void switchToMainMenu( javafx.event.ActionEvent event ) throws IOException {

        FXMLLoader mainMenu = new FXMLLoader( getClass().getResource( "/resources/view/mainMenu.fxml" ) );
        Parent root = mainMenu.load();

        MenuController menuController = mainMenu.getController() ;
        menuController.setValueDifficultySlider( 1 );

        Stage window = ( Stage ) ( ( Node ) event.getSource() ).getScene().getWindow();
        window.setScene( new Scene( root ) );
        window.show();

    }

}
