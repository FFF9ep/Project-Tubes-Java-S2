package org.example.com.main.UI;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class UIManager{
    private static Scene previousLayout;
    private static int width = 880;
    private static int height = 720;
    public static void setPreviousLayout(Scene scene) {
        previousLayout = scene;
    }
    public static Scene getPreviousLayout() {
        return previousLayout;
    }

    public static void showError(Text actionTarget, String message) {
        actionTarget.setFill(Color.FIREBRICK);
        actionTarget.setText(message);

        // Create a Timeline to clear the message after 5 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(7), ae -> actionTarget.setText("")));
        timeline.setCycleCount(1); // Run only once
        timeline.play();
    }
    public static void showSuccess(Text actionTarget, String message) {
        actionTarget.setFill(Color.GREEN);
        actionTarget.setText(message);

        // Create a Timeline to clear the message after 5 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(7), ae -> actionTarget.setText("")));
        timeline.setCycleCount(1); // Run only once
        timeline.play();
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }
}