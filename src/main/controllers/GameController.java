package main.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.dao.Barrier;
import main.dao.Score;
import main.dao.Snake;

import java.net.URL;
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

    private final Timer timer = new Timer();
    public GraphicsContext graphicsContext;
    @FXML
    Canvas background;
    @FXML
    Label gameScore;
    @FXML
    Label countdown;

    private final int ROWS = 15;
    private final int COLUMNS = 15;
    private final int SQUARE_SIZE = 40;

    private Snake snake;
    private int difficulty = 1;
    private Integer seconds = 4;

    public GameController( Stage window, int difficulty ) {
        this.window = window;
        this.difficulty = difficulty;
    }


    @Override
    public void initialize( URL url, ResourceBundle resourceBundle ) {

        graphicsContext = background.getGraphicsContext2D();
        Barrier barriers = new Barrier( 3 + ( difficulty * 2 ) );
        Score score = new Score( gameScore );
        snake = new Snake( barriers, score, SQUARE_SIZE, graphicsContext );
        snake.setCurrentDirection( DOWN );

        drawBackground();
        snake.drawSnake();
        snake.generateFood();
        snake.getFood().drawFood();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater( () -> {
                    try {

                        snake.move();

                        if ( !snake.isEating() ) {
                            fillGroundByColor();
                            snake.getBody().remove( snake.getBody().size() - 1 );
                        }

                        snake.setDirectionOfHead();

                        boolean tr = true;
                        int iterator = snake.getBody().size();
                        int loop;
                        for ( loop = 0; loop < iterator; loop++ ) {

                            if ( tr && snake.getHead().getX() == snake.getBody().get( loop ).getX() && snake.getHead().getY() == snake.getBody().get( loop ).getY() )
                                tr = false;
                            if ( !tr ) {

                                fillGroundByColor();

                                snake.getBody().remove( snake.getBody().size() - 1 );
                                snake.getScore().countdown( 60L );
                            }
                        }

                        snake.moveHead();

                        if ( snake.isCrashed() ) switchToGameOver();

                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }
                } );
            }
        };

        Timeline timeline = new Timeline();
        timeline.setCycleCount( 5 );
        KeyFrame frame = new KeyFrame( Duration.seconds( .75 ), actionEvent -> {

            if ( seconds == 4 ) countdown.getStyleClass().add( "whiteBack" );

            if ( seconds > 1 )
                countdown.setText( String.valueOf( seconds - 1 ) );
            else
                countdown.setText( "START" );

            seconds--;

            if ( seconds == -1 ) {
                timeline.stop();

                countdown.setText( "" );
                countdown.getStyleClass().remove( "whiteBack" );

                timer.schedule( timerTask, 0, 300 - ( ( difficulty ) * 50 ) );
            }
        } );
        timeline.getKeyFrames().add( frame );
        timeline.playFromStart();


    }

    private void drawBackground() {
        for ( int i = 0; i < ROWS; i++ ) {
            for ( int j = 0; j < COLUMNS; j++ ) {

                // check if it is barrier
                if ( !snake.getBarriers().checkIfIsSame( i, j ) )
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

    public void fillGroundByColor() {
        int x = snake.getBody().get( snake.getBody().size() - 1 ).getX();
        int y = snake.getBody().get( snake.getBody().size() - 1 ).getY();

        if ( ( x + y ) % 2 == 0 ) graphicsContext.setFill( Color.web( BACKGROUND_COLOR_EVEN ) );

        else graphicsContext.setFill( Color.web( BACKGROUND_COLOR_ODD ) );

        graphicsContext.fillRect( x * SQUARE_SIZE, y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE );
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
        menuController.gameOverScoreLabel.setText( snake.getScore().getScore().toString() );
        menuController.setDifficulty( difficulty );

        window.setScene( new Scene( root ) );
        window.show();
    }

}
