package controlador;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import modelo.Usuario;

public class PantallaLoginController implements Initializable {

    @FXML
    private TextField txtIdEmpleado;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnIniciarSesion;

    private Usuario usuario;

    public PantallaLoginController() {
        this.usuario = new Usuario();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void eventIniciarSesion() throws URISyntaxException, IOException, InterruptedException {
        if (txtIdEmpleado.getText().isEmpty() || txtPassword.getText().isEmpty()) {
            Alert mensaje = new Alert(AlertType.ERROR);
            mensaje.setTitle("Error");
            mensaje.setHeaderText("Campos vacíos");
            mensaje.setContentText("Asegurate de completar ambos campos");
            mensaje.showAndWait();

            return; //Termina la función (ignora el resto del código)
        }

        //Manda llamar la API y le pasa el Id y contraseña en la url para buscar al usuario
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/RESTfulInventario/resources/usuarios/" + txtIdEmpleado.getText() + "/" + txtPassword.getText()))
                .GET().build();

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

        //Comprueba si el servidor dió respuesta
        if (getResponse.statusCode() == 200) {
            //Verifica si se obtuvo acceso a la BD
            if (!getResponse.body().equals("No se pudo establecer conexión con la BD.")) {
                Gson usuarioJson = new Gson();
                //Convierte el Usuario que regresa la api a un objeto de tipo Usuario
                this.usuario = usuarioJson.fromJson(getResponse.body(), Usuario.class);

                //Comprueba si existe el usuario
                //Si el Id es 0, significa que no existe el usuario
                if (this.usuario.getIdUsuario() > 0) {
                    mostrarPantallaPrincipal();
                    cerrarPantallaLogin();
                } else {
                    Alert mensaje = new Alert(AlertType.ERROR);
                    mensaje.setTitle("Error");
                    mensaje.setHeaderText("El usuario no existe");
                    mensaje.setContentText("Verifica que hayas introducido bien tu usuario y contraseña.");
                    mensaje.showAndWait();
                }
            } else {
                Alert mensaje = new Alert(AlertType.ERROR);
                mensaje.setTitle("Error");
                mensaje.setHeaderText("Sin conexión a la BD");
                mensaje.setContentText("Verifica que se este apuntando a la BD correcta, que la IP del servidor y el puerto sean correctos y que el usuario y contraseña de la BD también lo sean.");
                mensaje.showAndWait();
            }
        }
    }

    @FXML
    private void eventValidarId(KeyEvent event) {
        //Si se pulsan las siguientes teclas, termina la función (no es necesario validar si fue un número)
        switch (event.getCode()) {
            case KeyCode.DELETE: //Tecla Suprimir
            case KeyCode.BACK_SPACE: //Tecla borra
            case KeyCode.ENTER:
            case KeyCode.ESCAPE:
            case KeyCode.DOWN: //Flecha abajo
            case KeyCode.LEFT: //Flecha izq.
            case KeyCode.RIGHT: //Flecha der.
            case KeyCode.UP: //Flecha arriba
            case KeyCode.WINDOWS: //Tecla de inicio
                return;
        }

        //Valida que solo se ingresen números
        if (!event.getCode().isDigitKey()) {
            Alert mensaje = new Alert(AlertType.ERROR);
            mensaje.setTitle("Error");
            mensaje.setHeaderText("Carácter invalido");
            mensaje.setContentText("Sólo se aceptan números.");
            mensaje.showAndWait();
            txtIdEmpleado.setText(""); //Limpia la entrada
        }
    }

    private void mostrarPantallaPrincipal() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/PantallaPrincipal.fxml"));
        Parent root = loader.load();

        PantallaPrincipalController controladorPantallaPrincipal = loader.getController();
        controladorPantallaPrincipal.setUsuario(this.usuario);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Gestión de inventario");
        stage.setMaximized(true);
        stage.show();
        controladorPantallaPrincipal.permisosRoles();
    }

    private void cerrarPantallaLogin() {
        Stage pantalla = (Stage) btnIniciarSesion.getScene().getWindow();
        pantalla.close();
    }
}
