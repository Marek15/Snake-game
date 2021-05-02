package main.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.dao.HttpRequest;
import main.dao.PlayerData;
import org.json.JSONArray;


import java.io.IOException;

public class ScoreTableController {


    @FXML
    TableView<PlayerData> scoreTable;


    private Stage window;
    private Parent root;

    ObservableList<PlayerData> tableData;

    public void initialize(){
        scoreTable.getItems().clear();
        scoreTable.getColumns().clear();


        tableData = FXCollections.observableArrayList();

        getData();

        //Creating columns
        TableColumn fileNameCol = new TableColumn("Nickname");
        fileNameCol.setCellValueFactory(new PropertyValueFactory<>("nickname"));

        TableColumn pathCol = new TableColumn("Score");
        pathCol.setCellValueFactory(new PropertyValueFactory("score"));



        scoreTable.setItems(tableData);
//        scoreTable.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE);
        scoreTable.getColumns().addAll(fileNameCol, pathCol);
    }

    public void refreshTable( ActionEvent actionEvent ) {
        System.out.println( "Refreshed table" );
        scoreTable.getItems().clear();

        getData();

        scoreTable.setItems( tableData );

    }

    private void getData(){
        String response = HttpRequest.sendGETRequest( "https://snakeskola.herokuapp.com/api/score");

        tableData.clear();

        JSONArray jsonData = new JSONArray(response);

        for ( int i = 0; i < jsonData.length(); i++ ) {

            String nickname = jsonData.getJSONObject( i ).getString( "nickname" );
            Long score = Long.parseLong( jsonData.getJSONObject( i ).getString( "score" ) ) ;

            tableData.add( new PlayerData( nickname, score ) );
        }
    }

    public void switchToMainMenu( javafx.event.ActionEvent event ) throws IOException {

        FXMLLoader mainMenu = new FXMLLoader( getClass().getResource( "/resources/view/mainMenu.fxml" ) );
        root = mainMenu.load();

        MenuController menuController = mainMenu.getController() ;
        menuController.setValueDifficultySlider( 1 );

        window = ( Stage ) ( ( Node ) event.getSource() ).getScene().getWindow();
        window.setScene( new Scene( root ) );
        window.show();

    }
}
