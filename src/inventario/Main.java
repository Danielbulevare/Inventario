package inventario;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/vista/PantallaLogin.fxml"));
        Scene scene = new Scene(root);
        
        
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setTitle("Iniciar sesión");
    }

    public static void main(String[] args) {
        launch(args);
    }    
}
