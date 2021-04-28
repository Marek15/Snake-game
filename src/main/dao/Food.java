package main.dao;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Random;

public class Food extends Point {

    private final GraphicsContext graphicsContext;
    private final String[] foodImages = { "/resources/img/orange.png", "/resources/img/cherry.png", "/resources/img/watermelon.png" };
    private final int SQUARE_SIZE;


    public Food( int SQUARE_SIZE, GraphicsContext graphicsContext ) {
        super();
        this.graphicsContext = graphicsContext;
        this.SQUARE_SIZE = SQUARE_SIZE;
    }

    public void drawFood() {
        Random random = new Random();
        int num = random.nextInt( foodImages.length );
        graphicsContext.drawImage( new Image( foodImages[ num ] ), this.getX() * SQUARE_SIZE, this.getY() * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE );
    }
}
