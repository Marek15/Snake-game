package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;

    private int ROWS = 15;
    private int COLUMNS = 15;
    private int SQUARE_SIZE = 40;

    public GraphicsContext gc;

    private Snake snake;
    private Barrier barriers;

    private int currentDirection = DOWN;

    private Point food;
    private Random random = new Random();


    @FXML
    Canvas background;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gc = background.getGraphicsContext2D();

        snake = new Snake();
        barriers = new Barrier(3, snake);

        generateFood();

        drawBackground();
        drawFood();
        drawSnake();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateScene(gc);
            }
        }, 5000, 200);

        // loop  for updating scene
//        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), e -> updateScene(gc)));
//        timeline.setCycleCount(Animation.INDEFINITE);
//        timeline.play();
    }


    private void drawBackground() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {

                // check if it is barrier
                if (!barriers.checkIfIsSame(i, j)) gc.setFill(Color.web("fc052a"));

                else if ((i + j) % 2 == 0) gc.setFill(Color.web("282554"));

                else gc.setFill(Color.web("504aa5"));

                gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }

    }

    public void drawSnake(){
        gc.setFill(Color.web("AAAAAA"));
        gc.fillRoundRect(snake.getHead().getX() * SQUARE_SIZE, snake.getHead().getY() * SQUARE_SIZE, SQUARE_SIZE  -1, SQUARE_SIZE -1,50,50);

        gc.setFill(Color.web("f4f4f4"));
        for (Point bodyPoint: snake.getBody()) gc.fillRoundRect(bodyPoint.getX() * SQUARE_SIZE, bodyPoint.getY() * SQUARE_SIZE, SQUARE_SIZE -1, SQUARE_SIZE -1, 20, 20);
    }

    public void drawFood(){
        gc.drawImage(new Image("/img/watermelon.png"), food.getX() * SQUARE_SIZE, food.getY() * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }


    public void generateFood(){

        // we generate random x and y , if it is same as snake head or body or barrier we generate it again
        int foodX, foodY;
        start:
        while (true){
            foodX = random.nextInt(15);
            foodY = random.nextInt(15);

            if (snake.getHead().getX() == foodX && snake.getHead().getY() == foodY) continue;

            for (Point barrier: barriers.getBarriers()) {
                if (barrier.getX() == foodX && barrier.getY() == foodY) continue start;
            }

            for (Point snakeBody: snake.getBody()) {
                if (snakeBody.getY() == foodY && snakeBody.getX() == foodX) continue start;
            }
            break;
        }
        food = new Point(foodX, foodY);
    }


    private void updateScene(GraphicsContext gc) {

        move();

        drawBackground();
        drawFood();
        drawSnake();
    }

    private void move(){

        // add to start snake body, former snake head + remove last point of snake body and in next step generate snake head on proper position by pressed key
        snake.getBody().add(0,new Point(snake.getHead().getX(), snake.getHead().getY()));

        // if snake eats food we don't remove last part of snake
        if (snake.getHead().getX() == food.getX() && snake.getHead().getY() == food.getY()) generateFood();
        else snake.getBody().remove(snake.getBody().size() -1);


        switch (currentDirection) {
            case RIGHT -> snake.getHead().setX(snake.getHead().getX() + 1);
            case LEFT -> snake.getHead().setX(snake.getHead().getX() - 1);
            case UP -> snake.getHead().setY(snake.getHead().getY() - 1);
            case DOWN -> snake.getHead().setY(snake.getHead().getY() + 1);
        }

        boolean tr = true;
        int iterator = snake.getBody().size();
        for (int i = 0; i < iterator; i++) {

            if (tr && snake.getHead().getX() == snake.getBody().get(i).getX() && snake.getHead().getY() == snake.getBody().get(i).getY()) tr = false;
            if (!tr) snake.getBody().remove(snake.getBody().size() -1);

        }

        // if snake goes out of play field, end game
        if (snake.getHead().getY() > 14 || snake.getHead().getY() < 0 || snake.getHead().getX() < 0 || snake.getHead().getX() > 14) System.exit(0);

        // end program if snake head hit barrier
        for (Point barrier: barriers.getBarriers()) {
            if (barrier.getX() == snake.getHead().getX() && barrier.getY() == snake.getHead().getY()) System.exit(0);
        }

    }


    public void changeDirection(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();
        if (code == KeyCode.RIGHT) {
            if (currentDirection != LEFT) currentDirection = RIGHT;
        }
        else if (code == KeyCode.LEFT) {
            if (currentDirection != RIGHT) currentDirection = LEFT;
        }
        else if (code == KeyCode.UP) {
            if (currentDirection != DOWN) currentDirection = UP;
        }
        else if (code == KeyCode.DOWN) {
            if (currentDirection != UP) currentDirection = DOWN;
        }
    }

}
