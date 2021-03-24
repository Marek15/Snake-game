package sample;

import java.util.ArrayList;
import java.util.Random;

public class Barrier {

    private final ArrayList<Point> barriers = new ArrayList<>();

    public Barrier(int countOfBarriers) {
        for (int i = 0; i < countOfBarriers ; i++) {
            Random rn = new Random();
            int x = rn.nextInt(15);
            int y = rn.nextInt(15);

            if (checkIfIsSame(x,y)) barriers.add(new Point(x,y));
            else i--;
        }
    }
    
    
    public boolean checkIfIsSame(int x, int y){
        for (Point barrier: barriers) {
            if (barrier.getX() == x && barrier.getY() == y) return false;
        }
        return true;
    }

}
