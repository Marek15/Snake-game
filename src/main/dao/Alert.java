package main.dao;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.Timer;
import java.util.TimerTask;

public class Alert {

    private final String SUCCESS_CSS_CLASS = "alertGreen";
    private final String ERROR_CSS_CLASS = "alertRed";
    private final String WARN_CSS_CLASS = "alertWarn";

    private final int displayTime = 3000;

    private final Label label;
    private final Timer timer;

    public Alert( Label label ) {
        this.label = label;
        timer = new Timer();
    }

    public void success( String message ) {
        label.getStyleClass().remove( WARN_CSS_CLASS );
        label.getStyleClass().remove( ERROR_CSS_CLASS );

        label.getStyleClass().add( SUCCESS_CSS_CLASS );
        setMessage( message, true );
    }

    public void error( String message ) {
        label.getStyleClass().remove( WARN_CSS_CLASS );
        label.getStyleClass().remove( SUCCESS_CSS_CLASS );

        label.getStyleClass().add( ERROR_CSS_CLASS );
        setMessage( message, false );
    }

    public void warn( String message ) {
        label.getStyleClass().remove( SUCCESS_CSS_CLASS );
        label.getStyleClass().remove( ERROR_CSS_CLASS );

        label.getStyleClass().add( WARN_CSS_CLASS );
        setMessage( message, false );
    }

    private void setMessage( String msg, Boolean temporary ) {
        label.setText( msg );

        if ( temporary ) {
            timer.schedule( new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater( () -> label.setText( "" ) );
                }
            }, displayTime );
        }
    }

}
