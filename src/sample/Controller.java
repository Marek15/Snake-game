package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private int ROWS = 15, COLUMNS = 15, SQUARE_SIZE = 40;

    private GraphicsContext gc;
    @FXML
    Canvas background;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gc = background.getGraphicsContext2D();
        drawBackground(gc);

    }





    private void drawBackground(GraphicsContext gc){
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (i == 10 && j == 10 || i == 4 && j == 2 || i == 8 && j == 5){
                    gc.setFill(Color.web("fc052a"));
                }
                else if ((i+j) % 2 == 0){
                    gc.setFill(Color.web("282554"));
                }
                else {
                    gc.setFill(Color.web("504aa5"));
                }
                gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }
}
