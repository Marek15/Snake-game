package sample;

import java.util.ArrayList;

public class Snake {

    private Point head;
    private Point tail;
    private ArrayList<Point> body = new ArrayList<>();

    public Snake() {
        this.head = new Point(10, 5);
        this.tail = new Point(10, 3);
        this.body.add(new Point(10, 4));
    }

    public Point getHead() {
        return head;
    }

    public void setHead(Point head) {
        this.head = head;
    }

    public Point getTail() {
        return tail;
    }

    public void setTail(Point tail) {
        this.tail = tail;
    }

    public ArrayList<Point> getBody() {
        return body;
    }

    public void setBody(ArrayList<Point> body) {
        this.body = body;
    }
}
