package main.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.dao.Barrier;
import main.dao.Point;
import main.dao.Score;
import main.dao.Snake;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class GameController implements Initializable {

    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;

    private final String SNAKE_HEAD_COLOR = "ff6666";
    private final String SNAKE_BODY_COLOR = "66aa66";
    private final Stage window;
    private final Timer timer = new Timer();
    public GraphicsContext gc;
    @FXML
    Canvas background;
    @FXML
    Label gameScore;

    private final int ROWS = 15;
    private final int COLUMNS = 15;
    private final int SQUARE_SIZE = 40;

    private Snake snake;
    private Barrier barriers;
    private int currentDirection;
    private Point food;
    private Parent root;
    private Score score;

    private final Random random = new Random();
    private int difficulty = 1;

    public GameController( Stage window, int difficulty ) {
        this.window = window;
        this.difficulty = difficulty;
    }


    @Override
    public void initialize( URL url, ResourceBundle resourceBundle ) {

        gc = background.getGraphicsContext2D();

        snake = new Snake();
        currentDirection = DOWN;
        barriers = new Barrier( 3 + ( difficulty * 2 ), snake );
        score = new Score( gameScore );

        generateFood();

        drawBackground();
        drawFood();
        drawSnake();


        timer.schedule( new TimerTask() {
            @Override
            public void run() {
                Platform.runLater( () -> {
                    try {
                        move();
                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }
                } );
            }
        }, 2000, 300 - ( ( difficulty ) * 50 ) );


    }

    private void drawBackground() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {

                // check if it is barrier
                if ( !barriers.checkIfIsSame( i, j ) )
                    gc.drawImage( new Image( "/resources/img/woodBox.png" ), i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE );

                else if ( ( i + j ) % 2 == 0 ) {
                    gc.setFill( Color.web( "282554" ) );
                    gc.fillRect( i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE );
                } else {
                    gc.setFill( Color.web( "504aa5" ) );
                    gc.fillRect( i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE );
                }
            }
        }
    }

    public void drawSnake() {
        gc.setFill( Color.web( SNAKE_HEAD_COLOR ) );
        gc.fillRoundRect( snake.getHead().getX() * SQUARE_SIZE, snake.getHead().getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 50, 50 );

        gc.setFill( Color.web( SNAKE_BODY_COLOR ) );
        for ( Point bodyPoint : snake.getBody() )
            gc.fillRoundRect( bodyPoint.getX() * SQUARE_SIZE, bodyPoint.getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 20, 20 );
    }

    public void drawFood() {
        int rn = random.nextInt( 3 );
        String source = switch ( rn ) {
            case 1 -> "/resources/img/orange.png";
            case 2 -> "/resources/img/cherry.png";
            default -> "/resources/img/watermelon.png";
        };

        gc.drawImage( new Image( source ), food.getX() * SQUARE_SIZE, food.getY() * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE );
    }

    public void generateFood() {

        // we generate random x and y , if it is same as snake head or body or barrier we generate it again
        int foodX, foodY;
        start:
        while (true) {
            foodX = random.nextInt(15);
            foodY = random.nextInt(15);

            if (snake.getHead().getX() == foodX && snake.getHead().getY() == foodY) continue;

            for (Point barrier : barriers.getBarriers()) {
                if (barrier.getX() == foodX && barrier.getY() == foodY) continue start;
            }

            for (Point snakeBody : snake.getBody()) {
                if (snakeBody.getY() == foodY && snakeBody.getX() == foodX) continue start;
            }
            break;
        }
        food = new Point(foodX, foodY);
    }

    private void move() throws Exception {

        // add to start snake body, former snake head + remove last point of snake body and in next step generate snake head on proper position by pressed key
        snake.getBody().add( 0, new Point( snake.getHead().getX(), snake.getHead().getY() ) );
        gc.setFill( Color.web( SNAKE_BODY_COLOR ) );
        gc.fillRoundRect( snake.getHead().getX() * SQUARE_SIZE, snake.getHead().getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 20, 20 );


        // if snake eats food we don't remove last part of snake
        if ( snake.getHead().getX() == food.getX() && snake.getHead().getY() == food.getY() ) {
            generateFood();
            drawFood();

            gc.setFill( Color.web( SNAKE_BODY_COLOR ) );
            gc.fillRoundRect( snake.getBody().get( snake.getBody().size() - 1 ).getX() * SQUARE_SIZE, snake.getBody().get( snake.getBody().size() - 1 ).getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 20, 20 );

            score.add( 50L );
        } else {

            fillGroundByColor();

            snake.getBody().remove(snake.getBody().size() - 1);
        }


        switch (currentDirection) {
            case RIGHT -> snake.getHead().setX( snake.getHead().getX() + 1 );
            case LEFT -> snake.getHead().setX( snake.getHead().getX() - 1 );
            case UP -> snake.getHead().setY( snake.getHead().getY() - 1 );
            case DOWN -> snake.getHead().setY( snake.getHead().getY() + 1 );
        }


        //snake eats some part of him
        boolean tr = true;
        int iterator = snake.getBody().size();
        int loop;
        for ( loop = 0; loop < iterator; loop++ ) {

            if ( tr && snake.getHead().getX() == snake.getBody().get( loop ).getX() && snake.getHead().getY() == snake.getBody().get( loop ).getY() )
                tr = false;
            if ( !tr ) {

                fillGroundByColor();

                snake.getBody().remove( snake.getBody().size() - 1 );
                score.countdown( 60L );
            }
        }


        gc.setFill( Color.web( SNAKE_HEAD_COLOR ) );
        gc.fillRoundRect( snake.getHead().getX() * SQUARE_SIZE, snake.getHead().getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 50, 50 );


        // if snake goes out of play field, end game
        if ( snake.getHead().getY() > 14 || snake.getHead().getY() < 0 || snake.getHead().getX() < 0 || snake.getHead().getX() > 14 )
            switchToGameOver();

        // end program if snake head hit barrier
        for ( Point barrier : barriers.getBarriers() ) {
            if ( ( barrier.getX() == snake.getHead().getX() ) && ( barrier.getY() == snake.getHead().getY() ) )
                switchToGameOver();
        }

    }

    public void fillGroundByColor() {
        int x = snake.getBody().get(snake.getBody().size() - 1).getX();
        int y = snake.getBody().get(snake.getBody().size() - 1).getY();

        if ((x + y) % 2 == 0) gc.setFill(Color.web("282554"));

        else gc.setFill(Color.web("504aa5"));

        gc.fillRect(x * SQUARE_SIZE, y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }

    public void changeDirection(KeyEvent keyEvent) {

        KeyCode code = keyEvent.getCode();


        if ( code == KeyCode.RIGHT ) {
            if ( currentDirection != LEFT && !( snake.getHead().getX() + 1 == snake.getBody().get( 0 ).getX() ) )
                currentDirection = RIGHT;
        } else if ( code == KeyCode.LEFT ) {
            if ( currentDirection != RIGHT && !( snake.getHead().getX() - 1 == snake.getBody().get( 0 ).getX() ) )
                currentDirection = LEFT;
        } else if ( code == KeyCode.UP ) {
            if ( currentDirection != DOWN && !( snake.getHead().getY() - 1 == snake.getBody().get( 0 ).getY() ) )
                currentDirection = UP;
        } else if ( code == KeyCode.DOWN ) {
            if ( currentDirection != UP && !( snake.getHead().getY() + 1 == snake.getBody().get( 0 ).getY() ) )
                currentDirection = DOWN;
        }
    }

    public void switchToGameOver() throws Exception {
        timer.cancel();

        FXMLLoader gameOverScene = new FXMLLoader( getClass().getResource( "/resources/view/gameOver.fxml" ) );
        root = gameOverScene.load();

        MenuController menuController = gameOverScene.getController();
        menuController.gameOverScoreLabel.setText( score.getScore().toString() );
        menuController.setDifficulty( difficulty );

        window.setScene( new Scene( root ) );
        window.show();
    }

}
