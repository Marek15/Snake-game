package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private int ROWS = 15;
    private int COLUMNS = 15;
    private int SQUARE_SIZE = 40;

    public GraphicsContext gc;


    private Snake snake;
    private Barrier barriers;



    @FXML
    Canvas background;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gc = background.getGraphicsContext2D();


        snake = new Snake();

        barriers = new Barrier(3, snake);


        drawBackground();



    }


    private void drawBackground(){

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if(snake.getHead().getX() == i && snake.getHead().getY() == j){
                    gc.setFill(Color.web("aaaaaa"));
                }
                else if (snake.getBody().get(0).getX() == i && snake.getBody().get(0).getY() == j){
                    gc.setFill(Color.web("f4f4f4"));
                }
                else if (!barriers.checkIfIsSame(i,j)){
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
