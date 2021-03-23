package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public ArrayList<Point> barrier = new ArrayList<>();

    private int ROWS = 15;
    private int COLUMNS = 15;
    private int SQUARE_SIZE = 40;

    private GraphicsContext gc;


    private Snake snake;



    @FXML
    Canvas background;

    @FXML
    private void handleKeyPressed(KeyEvent keyEvent){
        System.out.println("je");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gc = background.getGraphicsContext2D();

        snake = new Snake();
        drawBackground(gc);

    }


    private void drawBackground(GraphicsContext gc){

        setBarriers(gc);

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (snake.getTail().getX() == i && snake.getTail().getY() == j){
                    gc.setFill(Color.web("ffffff"));
                }
                else if(snake.getHead().getX() == i && snake.getHead().getY() == j){
                    gc.setFill(Color.web("aaaaaa"));
                }
                else if (snake.getBody().get(0).getX() == i && snake.getBody().get(0).getY() == j){
                    gc.setFill(Color.web("f4f4f4"));
                }
                else if (barrier.contains(new Point(i,j))){
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

    private void setBarriers(GraphicsContext gc){
        for (int i = 0; i < 3 ; i++) {
            Random rn = new Random();
            int x = rn.nextInt(15);
            int y = rn.nextInt(15);

            if (!barrier.contains(new Point(x,y))) barrier.add(new Point(x,y));
            else i--;
        }
    }

}
