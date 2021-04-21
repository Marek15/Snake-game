package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.*;
import java.util.Timer;

public class Controller implements Initializable {

    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;

    private int ROWS = 15;
    private int COLUMNS = 15;
    private int SQUARE_SIZE = 40;

    public GraphicsContext gc;
    public GraphicsContext startMenuContext;

    private Snake snake;
    private Barrier barriers;

    private int currentDirection;

    private Point food;
    private Random random = new Random();

    private enum STATE {
        STARTMENU,
        GAME,
        GAMEOVER
    };


    private STATE State = STATE.STARTMENU;

//    StartMenu startMenu;

    @FXML
    Canvas background;
    @FXML
    Canvas menu;
    @FXML
    VBox mainMenuGroup;
    @FXML
    VBox gameOverMenuGroup;

    @FXML
    Button playBtn;
    @FXML
    Button scoreBtn;
    @FXML
    Button quitBtn;
    @FXML
    Button restartBtn;
    @FXML
    Button mainMenuBtn;
    @FXML
    Button quitBtn1;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gc = background.getGraphicsContext2D();
//        startMenu = new StartMenu();
        startMenuContext = menu.getGraphicsContext2D();

        Font fnt0 = new Font("arial", 70);
        Font fnt1 = new Font("arial", 30);

        startMenuContext.setFill(Color.WHITE);
        startMenuContext.setGlobalBlendMode(BlendMode.MULTIPLY);
        startMenuContext.setGlobalAlpha(.8);
        startMenuContext.fillRect(0, 0, 600, 600);


        gameOverMenuGroup.setVisible(false);
        playBtn.setFont(fnt1);
        scoreBtn.setFont(fnt1);
        quitBtn.setFont(fnt1);
        restartBtn.setFont(fnt1);
        mainMenuBtn.setFont(fnt1);
        quitBtn1.setFont(fnt1);

        playBtn.setOnMouseClicked( mouseEvent ->  {
            State = STATE.GAME;
        });

        restartBtn.setOnMouseClicked( mouseEvent ->  {
            State = STATE.GAME;

            initializeGame();
        });
        mainMenuBtn.setOnMouseClicked( mouseEvent ->  {
            State = STATE.STARTMENU;
            initializeGame();
        });

        quitBtn.setOnMouseClicked( mouseEvent -> System.exit(0));
        quitBtn1.setOnMouseClicked( mouseEvent -> System.exit(0));


        initializeGame();



        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (State == STATE.GAME) {
                    menu.setVisible(false);
                    mainMenuGroup.setVisible(false);
                    gameOverMenuGroup.setVisible(false);
                    updateScene(gc);
                }
                else if (State == STATE.STARTMENU) {
                    menu.setVisible(true);
                    mainMenuGroup.setVisible(true);
                    gameOverMenuGroup.setVisible(false);
                }
                else {
                    menu.setVisible(true);
                    mainMenuGroup.setVisible(false);
                    gameOverMenuGroup.setVisible(true);
                }
            }
        }, 2000, 200);

        // loop  for updating scene
//        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), e -> updateScene(gc)));
//        timeline.setCycleCount(Animation.INDEFINITE);
//        timeline.play();
    }

    private void initializeGame(){
        snake = new Snake();
        currentDirection = DOWN;
        barriers = new Barrier(3, snake);

        generateFood();

        drawBackground();
        drawFood();
        drawSnake();
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

//        drawBackground();
//        drawFood();
//        drawSnake();
    }

    private void move(){

        // add to start snake body, former snake head + remove last point of snake body and in next step generate snake head on proper position by pressed key
        snake.getBody().add(0,new Point(snake.getHead().getX(), snake.getHead().getY()));
        gc.setFill(Color.web("f4f4f4"));
        gc.fillRoundRect(snake.getHead().getX() * SQUARE_SIZE, snake.getHead().getY() * SQUARE_SIZE, SQUARE_SIZE -1, SQUARE_SIZE -1, 20, 20);


        // if snake eats food we don't remove last part of snake
        if (snake.getHead().getX() == food.getX() && snake.getHead().getY() == food.getY()) {
            generateFood();

            drawFood();

            gc.setFill(Color.web("f4f4f4"));
            gc.fillRoundRect(snake.getBody().get(snake.getBody().size()-1).getX() * SQUARE_SIZE, snake.getBody().get(snake.getBody().size()-1).getY() * SQUARE_SIZE, SQUARE_SIZE -1, SQUARE_SIZE -1, 20, 20);

        }
        else {

            int xe = snake.getBody().get(snake.getBody().size() - 1).getX();
            int ye = snake.getBody().get(snake.getBody().size() - 1).getY();


            if ((xe + ye) % 2 == 0) gc.setFill(Color.web("282554"));

            else gc.setFill(Color.web("504aa5"));

            gc.fillRect(xe * SQUARE_SIZE, ye * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);


            snake.getBody().remove(snake.getBody().size() -1);

        }


        switch (currentDirection) {
            case RIGHT -> snake.getHead().setX(snake.getHead().getX() + 1);
            case LEFT -> snake.getHead().setX(snake.getHead().getX() - 1);
            case UP -> snake.getHead().setY(snake.getHead().getY() - 1);
            case DOWN -> snake.getHead().setY(snake.getHead().getY() + 1);
        }

//        gc.setFill(Color.web("AAAAAA"));
//        gc.fillRoundRect(snake.getHead().getX() * SQUARE_SIZE, snake.getHead().getY() * SQUARE_SIZE, SQUARE_SIZE  -1, SQUARE_SIZE -1,50,50);


        //snake eats some part of him
        boolean tr = true;
        int iterator = snake.getBody().size();
        for (int i = 0; i < iterator; i++) {

            if (tr && snake.getHead().getX() == snake.getBody().get(i).getX() && snake.getHead().getY() == snake.getBody().get(i).getY()) tr = false;
            if (!tr) {

                int xe = snake.getBody().get(snake.getBody().size() - 1).getX();
                int ye = snake.getBody().get(snake.getBody().size() - 1).getY();


                if ((xe + ye) % 2 == 0) gc.setFill(Color.web("282554"));

                else gc.setFill(Color.web("504aa5"));

                gc.fillRect(xe * SQUARE_SIZE, ye * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

                snake.getBody().remove(snake.getBody().size() -1);
            }

        }


        gc.setFill(Color.web("AAAAAA"));
        gc.fillRoundRect(snake.getHead().getX() * SQUARE_SIZE, snake.getHead().getY() * SQUARE_SIZE, SQUARE_SIZE  -1, SQUARE_SIZE -1,50,50);



        // if snake goes out of play field, end game
        if (snake.getHead().getY() > 14 || snake.getHead().getY() < 0 || snake.getHead().getX() < 0 || snake.getHead().getX() > 14) State = STATE.GAMEOVER;

        // end program if snake head hit barrier
        for (Point barrier: barriers.getBarriers()) {
            if (barrier.getX() == snake.getHead().getX() && barrier.getY() == snake.getHead().getY()) State = STATE.GAMEOVER;
        }

    }






    public void changeDirection(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();
//        if (State == STATE.GAME) {
        if (code == KeyCode.RIGHT) {
            if (currentDirection != LEFT) currentDirection = RIGHT;
        } else if (code == KeyCode.LEFT) {
            if (currentDirection != RIGHT) currentDirection = LEFT;
        } else if (code == KeyCode.UP) {
            if (currentDirection != DOWN) currentDirection = UP;
        } else if (code == KeyCode.DOWN) {
            if (currentDirection != UP) currentDirection = DOWN;
        }
    // }
    }

}
