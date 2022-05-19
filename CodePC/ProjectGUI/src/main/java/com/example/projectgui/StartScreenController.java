package com.example.projectgui;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Timer;
import java.util.TimerTask;

public class StartScreenController extends Thread{
    Thread arduino = new Thread(this::run);
    Timer timer = new Timer();
    @FXML
    private Label T1;

    public void initialize(){
        Singleton language = Singleton.getInstance();
        if (!language.getIsEnglish()) {
            T1.setText("Voer Uw pas in");
            arduino.start();
        }
    }
    public void control(boolean success) {
        SceneController controller = SceneController.getInstance();
        if (!success) {
            try {
                controller.setScene("LanguageScreen.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (success) {
            try{
                controller.setScene("PinScreenEngels.fxml");
            } catch (IOException e) {e.printStackTrace();}
        }
    }
    @Override
    public void run() {
       if (!ArduinoControls.eatCard() || ArduinoControls.getCardInfo().startsWith("ER")) {
           control(false);
       }
       control(true);
    }
}
