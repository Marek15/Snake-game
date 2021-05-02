package main.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import main.dao.*;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class GameController implements Initializable {


    private final String BACKGROUND_COLOR_ODD = "282554";
    private final String BACKGROUND_COLOR_EVEN = "504aa5";

    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;

    private final Stage window;

    private final Random random = new Random();
    private final Timer timer = new Timer();
    public GraphicsContext graphicsContext;

    @FXML
    Canvas background;

    @FXML
    Label gameScore;

    @FXML
    Label scoreInfoLabel;

    @FXML
    Label countdown;

    private final int ROWS = 15;
    private final int COLUMNS = 15;
    private final int SQUARE_SIZE = 40;

    private Snake snake;
    private Food food;
    private Score score;
    private Barrier barriers;
    private int difficulty;
    private Integer countdownSeconds = 4;
    private String nickname;

    public GameController( Stage window, int difficulty, String nickname ) {
        this.window = window;
        this.difficulty = difficulty;
        this.nickname = nickname;
    }


    @Override
    public void initialize( URL url, ResourceBundle resourceBundle ) {

        graphicsContext = background.getGraphicsContext2D();
        barriers = new Barrier( 3 + ( difficulty * 2 ) );

        score = new Score( gameScore, scoreInfoLabel );
        food = new Food( SQUARE_SIZE, graphicsContext );
        snake = new Snake( SQUARE_SIZE, graphicsContext );
        snake.setCurrentDirection( DOWN );

        barriers.initialize( snake.getHead().getX() );
        drawBackground();
        snake.draw();
        generateFood();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater( () -> {
                    try {

                        snake.moveBody();

                        if ( snake.isEating( food.getX(), food.getY() ) ) {
                            score.add( 50L );
                            generateFood();
                        } else {
                            fillBackgroundAfterSnake( 1 );
                        }

                        snake.setDirectionOfHead();

                        int numberOfEatenBodyParts;
                        if ( ( numberOfEatenBodyParts = snake.isCannibal() ) > 0 ) {
                            score.countdown( 60L * numberOfEatenBodyParts );
                            fillBackgroundAfterSnake( numberOfEatenBodyParts );
                        }

                        snake.moveHead();

                        if ( isCrashed() ) switchToGameOver();

                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }
                } );
            }
        };

        Timeline timeline = new Timeline();
        timeline.setCycleCount( 5 );

        KeyFrame frame = new KeyFrame( Duration.seconds( .5 ), actionEvent -> {

            if ( countdownSeconds == 4 ) countdown.getStyleClass().add( "whiteBack" );

            if ( countdownSeconds > 1 )
                countdown.setText( String.valueOf( countdownSeconds - 1 ) );
            else
                countdown.setText( "START" );

            countdownSeconds--;

            if ( countdownSeconds == -1 ) {
                timeline.stop();

                countdown.setText( "" );
                countdown.getStyleClass().remove( "whiteBack" );

                timer.schedule( timerTask, 0, 350 - ( ( difficulty ) * 50L ) );
            }
        } );

        timeline.getKeyFrames().add( frame );
        timeline.playFromStart();


    }

    private void drawBackground() {
        for ( int i = 0; i < ROWS; i++ ) {
            for ( int j = 0; j < COLUMNS; j++ ) {

                // check if it is barrier
                if ( !barriers.checkIfIsSame( i, j ) )
                    graphicsContext.drawImage( new Image( "/resources/img/woodBox.png" ), i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE );

                else if ( ( i + j ) % 2 == 0 ) {
                    graphicsContext.setFill( Color.web( BACKGROUND_COLOR_EVEN ) );
                    graphicsContext.fillRect( i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE );
                } else {
                    graphicsContext.setFill( Color.web( BACKGROUND_COLOR_ODD ) );
                    graphicsContext.fillRect( i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE );
                }
            }
        }
    }

    private void fillBackgroundAfterSnake( int numberOfBodyParts ) {

        for ( int i = 1; i <= numberOfBodyParts; i++ ) {

            int x = snake.getBody().get( snake.getBody().size() - 1 ).getX();
            int y = snake.getBody().get( snake.getBody().size() - 1 ).getY();

            if ( ( x + y ) % 2 == 0 ) graphicsContext.setFill( Color.web( BACKGROUND_COLOR_EVEN ) );

            else graphicsContext.setFill( Color.web( BACKGROUND_COLOR_ODD ) );

            graphicsContext.fillRect( x * SQUARE_SIZE, y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE );

            snake.getBody().remove( snake.getBody().size() - 1 );
        }
    }

    private void generateFood() {

        // we generate random x and y , if it is same as snake head or body or barrier we generate it again
        int foodX, foodY;
        start:
        while ( true ) {
            foodX = random.nextInt( 15 );
            foodY = random.nextInt( 15 );

            if ( snake.getHead().getX() == foodX && snake.getHead().getY() == foodY ) continue;

            for ( Point barrier : barriers.getBarriers() ) {
                if ( barrier.getX() == foodX && barrier.getY() == foodY ) continue start;
            }

            for ( Point snakeBody : snake.getBody() ) {
                if ( snakeBody.getY() == foodY && snakeBody.getX() == foodX ) continue start;
            }
            break;
        }
        food.setX( foodX );
        food.setY( foodY );
        food.drawFood();
    }

    private boolean isCrashed() {
        // if snake goes out of play field, end game
        if ( snake.getHead().getY() > 14 || snake.getHead().getY() < 0 || snake.getHead().getX() < 0 || snake.getHead().getX() > 14 )
            return true;

        // end program if snake head hit barrier
        for ( Point barrier : barriers.getBarriers() ) {
            if ( ( barrier.getX() == snake.getHead().getX() ) && ( barrier.getY() == snake.getHead().getY() ) )
                return true;
        }
        return false;
    }


    public void changeDirection( KeyEvent keyEvent ) {

        KeyCode code = keyEvent.getCode();

        if ( code == KeyCode.RIGHT ) {
            if ( snake.getCurrentDirection() != LEFT && !( snake.getHead().getX() + 1 == snake.getBody().get( 0 ).getX() ) )
                snake.setCurrentDirection( RIGHT );
        } else if ( code == KeyCode.LEFT ) {
            if ( snake.getCurrentDirection() != RIGHT && !( snake.getHead().getX() - 1 == snake.getBody().get( 0 ).getX() ) )
                snake.setCurrentDirection( LEFT );
        } else if ( code == KeyCode.UP ) {
            if ( snake.getCurrentDirection() != DOWN && !( snake.getHead().getY() - 1 == snake.getBody().get( 0 ).getY() ) )
                snake.setCurrentDirection( UP );
        } else if ( code == KeyCode.DOWN ) {
            if ( snake.getCurrentDirection() != UP && !( snake.getHead().getY() + 1 == snake.getBody().get( 0 ).getY() ) )
                snake.setCurrentDirection( DOWN );
        }
    }

    public void switchToGameOver() throws Exception {
        timer.cancel();

        FXMLLoader gameOverScene = new FXMLLoader( getClass().getResource( "/resources/view/gameOver.fxml" ) );
        Parent root = gameOverScene.load();

        MenuController menuController = gameOverScene.getController();
        menuController.gameOverScoreLabel.setText( score.getScore().toString() );
        menuController.gameOverDifficultyLabel.setText( String.valueOf( difficulty ) );
        menuController.setDifficulty( difficulty );
        menuController.setNickname( nickname );
        menuController.savePlayerScore();


        window.setScene( new Scene( root ) );
        window.show();
    }

}
