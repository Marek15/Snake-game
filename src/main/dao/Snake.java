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

    private final int SQUARE_SIZE;

    private final GraphicsContext graphicsContext;

    private Point head;
    private ArrayList<Point> body = new ArrayList<>();
    private int currentDirection;


    public Snake( int SQUARE_SIZE, GraphicsContext graphicsContext ) {

        this.graphicsContext = graphicsContext;
        this.SQUARE_SIZE = SQUARE_SIZE;


        Random random = new Random();
        int x = random.nextInt( 15 );
        int y = random.nextInt( 9 ) + 1;

        this.head = new Point( x, y );
        this.body.add( new Point( x, y - 1 ) );


    }


    public boolean isEating( double foodX, double foodY ) {
        // if snake eats food we don't remove last part of snake
        if ( this.getHead().getX() == foodX && this.getHead().getY() == foodY ) {
            drawRect( SNAKE_BODY_COLOR, this.getBody().get( this.getBody().size() - 1 ).getX() * SQUARE_SIZE, this.getBody().get( this.getBody().size() - 1 ).getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 20, 20 );
            return true;
        }
        return false;
    }

    public int isCannibal() {
        boolean tr = true;
        int iterator = this.getBody().size();
        int numberOfEatenBodyParts = 0;
        for ( int i = 0; i < iterator; i++ ) {

            if ( tr && this.getHead().getX() == this.getBody().get( i ).getX() && this.getHead().getY() == this.getBody().get( i ).getY() )
                tr = false;
            if ( !tr ) {
                numberOfEatenBodyParts++;
            }
        }

        return numberOfEatenBodyParts;
    }


    public void setDirectionOfHead() {
        switch ( currentDirection ) {
            case RIGHT -> this.getHead().setX( this.getHead().getX() + 1 );
            case LEFT -> this.getHead().setX( this.getHead().getX() - 1 );
            case UP -> this.getHead().setY( this.getHead().getY() - 1 );
            case DOWN -> this.getHead().setY( this.getHead().getY() + 1 );
        }
    }


    public void draw() {

        drawRect( SNAKE_HEAD_COLOR, this.getHead().getX() * SQUARE_SIZE, this.getHead().getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 50, 50 );

        for ( Point bodyPoint : getBody() )
            drawRect( SNAKE_BODY_COLOR, bodyPoint.getX() * SQUARE_SIZE, bodyPoint.getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 20, 20 );
    }

    public void moveHead() {

        drawRect( SNAKE_HEAD_COLOR, this.getHead().getX() * SQUARE_SIZE, this.getHead().getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 50, 50 );
    }


    public void moveBody() {

        // add to start snake body, former snake head + remove last point of snake body and in next step generate snake head on proper position by pressed key
        this.getBody().add( 0, new Point( this.getHead().getX(), this.getHead().getY() ) );

        drawRect( SNAKE_BODY_COLOR, this.getHead().getX() * SQUARE_SIZE, this.getHead().getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 20, 20 );

    }

    private void drawRect( String color, double x, double y, double width, double height, double arcWidth, double arcHeight ) {

        graphicsContext.setFill( Color.web( color ) );
        graphicsContext.fillRoundRect( x, y, width, height, arcWidth, arcHeight );

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
