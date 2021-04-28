package main.dao;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class Snake {
    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    private final String SNAKE_HEAD_COLOR = "ff6666";
    private final String SNAKE_BODY_COLOR = "66aa66";
    private final Barrier barriers;

    private Point head;
    private ArrayList<Point> body = new ArrayList<>();
    private final Food food;
    private final Score score;
    private final int SQUARE_SIZE;
    private final Random random = new Random();
    private int currentDirection;
    private final GraphicsContext graphicsContext;
    public Snake( Barrier barriers, Score score, int SQUARE_SIZE, GraphicsContext graphicsContext ) {

        this.barriers = barriers;

        this.food = new Food( SQUARE_SIZE, graphicsContext );
        this.score = score;
        this.graphicsContext = graphicsContext;
        this.SQUARE_SIZE = SQUARE_SIZE;


        int x = random.nextInt( 15 );
        int y = random.nextInt( 9 ) + 1;

        this.head = new Point( x, y );
        this.body.add( new Point( x, y - 1 ) );

        this.barriers.Initialze( this.getHead().getX() );
    }

    public Barrier getBarriers() {
        return barriers;
    }

    public Food getFood() {
        return food;
    }

    public Score getScore() {
        return score;
    }

    public void move() {

        // add to start snake body, former snake head + remove last point of snake body and in next step generate snake head on proper position by pressed key
        this.getBody().add( 0, new Point( this.getHead().getX(), this.getHead().getY() ) );
        graphicsContext.setFill( Color.web( SNAKE_BODY_COLOR ) );
        graphicsContext.fillRoundRect( this.getHead().getX() * SQUARE_SIZE, this.getHead().getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 20, 20 );

    }

    public boolean isEating() {
        // if snake eats food we don't remove last part of snake
        if ( this.getHead().getX() == food.getX() && this.getHead().getY() == food.getY() ) {
            generateFood();
            food.drawFood();

            graphicsContext.setFill( Color.web( SNAKE_BODY_COLOR ) );
            graphicsContext.fillRoundRect( this.getBody().get( this.getBody().size() - 1 ).getX() * SQUARE_SIZE, this.getBody().get( this.getBody().size() - 1 ).getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 20, 20 );

            score.add( 50L );
            return true;

        } else return false;
    }


    public void setDirectionOfHead() {
        switch ( currentDirection ) {
            case RIGHT -> this.getHead().setX( this.getHead().getX() + 1 );
            case LEFT -> this.getHead().setX( this.getHead().getX() - 1 );
            case UP -> this.getHead().setY( this.getHead().getY() - 1 );
            case DOWN -> this.getHead().setY( this.getHead().getY() + 1 );
        }


    }

    public void moveHead() {

        graphicsContext.setFill( Color.web( SNAKE_HEAD_COLOR ) );
        graphicsContext.fillRoundRect( this.getHead().getX() * SQUARE_SIZE, this.getHead().getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 50, 50 );
    }

    public void drawSnake() {
        graphicsContext.setFill( Color.web( SNAKE_HEAD_COLOR ) );
        graphicsContext.fillRoundRect( this.getHead().getX() * SQUARE_SIZE, this.getHead().getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 50, 50 );

        graphicsContext.setFill( Color.web( SNAKE_BODY_COLOR ) );
        for ( Point bodyPoint : this.getBody() )
            graphicsContext.fillRoundRect( bodyPoint.getX() * SQUARE_SIZE, bodyPoint.getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 20, 20 );
    }

    public void generateFood() {

        // we generate random x and y , if it is same as snake head or body or barrier we generate it again
        int foodX, foodY;
        start:
        while ( true ) {
            foodX = random.nextInt( 15 );
            foodY = random.nextInt( 15 );

            if ( this.getHead().getX() == foodX && this.getHead().getY() == foodY ) continue;

            for ( Point barrier : barriers.getBarriers() ) {
                if ( barrier.getX() == foodX && barrier.getY() == foodY ) continue start;
            }

            for ( Point snakeBody : this.getBody() ) {
                if ( snakeBody.getY() == foodY && snakeBody.getX() == foodX ) continue start;
            }
            break;
        }
        food.setX( foodX );
        food.setY( foodY );
    }


    public boolean isCrashed() {
        // if snake goes out of play field, end game
        if ( this.getHead().getY() > 14 || this.getHead().getY() < 0 || this.getHead().getX() < 0 || this.getHead().getX() > 14 )
            return true;

        // end program if snake head hit barrier
        for ( Point barrier : barriers.getBarriers() ) {
            if ( ( barrier.getX() == this.getHead().getX() ) && ( barrier.getY() == this.getHead().getY() ) )
                return true;
        }
        return false;
    }

    public Point getHead() {
        return head;
    }


    public ArrayList<Point> getBody() {
        return body;
    }


    public int getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection( int currentDirection ) {
        this.currentDirection = currentDirection;
    }


}
