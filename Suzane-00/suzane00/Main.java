/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Usere
 */
public class Main extends Application {
    
    public static void main(String[] args) {
        loadEnvironment();
        Application.launch(args);
    }
    
    private static void loadEnvironment() {
        try {
            Class.forName("suzane00.global.Environment");
        } catch (ClassNotFoundException e) {
            System.out.println("Can't load environment class");
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainWindow mainWindow = new MainWindow();
        mainWindow.show();
    }
    
}
