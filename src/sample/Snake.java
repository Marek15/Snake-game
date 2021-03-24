package sample;

import java.util.ArrayList;
import java.util.Random;

public class Snake {

    private Point head;
    private ArrayList<Point> body = new ArrayList<>();

    public Snake() {
        Random rn = new Random();

        int x = rn.nextInt(15);
        int y = rn.nextInt(10);

        this.head = new Point(x, y);
        this.body.add(new Point(x, y+1));
    }

    public Point getHead() {
        return head;
    }

    public void setHead(Point head) {
        this.head = head;
    }

    public ArrayList<Point> getBody() {
        return body;
    }

    public void setBody(ArrayList<Point> body) {
        this.body = body;
    }
}
