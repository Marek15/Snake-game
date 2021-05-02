package main.dao;

import java.util.ArrayList;
import java.util.Random;

public class Barrier {

    private final ArrayList<Point> barriers = new ArrayList<>();
    private final int numberOfBarriers;

    public Barrier( int numberOfBarriers ) {
        this.numberOfBarriers = numberOfBarriers;
    }

    public void initialize( int snakeHeadXPosition ) {
        Random rn = new Random();

        for ( int i = 0; i < numberOfBarriers; i++ ) {
            int x = rn.nextInt( 15 );
            int y = rn.nextInt( 15 );

            if ( checkIfIsSame( x, y ) && checkSnakePosition( x, snakeHeadXPosition ) && checkBorderPoint( x, y ) )
                barriers.add( new Point( x, y ) );
            else i--;
        }
    }

    public ArrayList<Point> getBarriers() {
        return barriers;
    }

    public boolean checkIfIsSame( int x, int y ) {
        for ( Point barrier : barriers ) {
            if ( barrier.getX() == x && barrier.getY() == y ) return false;
        }
        return true;
    }

    public boolean checkSnakePosition( int x, int snakeHeadXPosition ) {
        return snakeHeadXPosition != x;
    }

    public boolean checkBorderPoint( int x, int y ) {
        return !( x == 14 || x == 0 || y == 0 || y == 14 );
    }


}
