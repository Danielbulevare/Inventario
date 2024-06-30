package controlador;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import constantes.AccionSeleccionadaModuloProductos;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import modelo.ContenedorObjetosTransaccion;
import modelo.Producto;
import modelo.TransaccionInventario;
import modelo.Usuario;

public class PantallaPrincipalController implements Initializable {

    private Usuario usuario;
    private ObservableList<String> opcionesEstatus;
    private ObservableList<String> opcionesMovimiento;
    private AccionSeleccionadaModuloProductos accionModuloProducto;

    //Componentes de la lista de módulos
    @FXML
    private VBox pnlListaModulos;

    //Componentes, datos del usuario
    @FXML
    private Label lblNombreUsuario;

    @FXML
    private Label lblRolUsuario;

    //Botones de los módulos
    @FXML
    private Button btnProductos;

    @FXML
    private Button btnInventario;

    @FXML
    private Button btnEntradaSalida;

    @FXML
    Button btnHistorico;

    //Contenedor de los módulos
    @FXML
    private TabPane tabModulos;

    //Formularios de los múdulos
    @FXML
    private Tab tabProductos;

    @FXML
    private Tab tabInventario;

    @FXML
    private Tab tabEntradaSalida;

    @FXML
    private Tab tabHistorico;

    //Componentes del módulo de Productos
    @FXML
    private ImageView imgNuevoProducto;

    @FXML
    private ImageView imgBuscarProducto;

    @FXML
    private ImageView imgEditarProducto;

    @FXML
    private TextField txtIdProductoModProducto;

    @FXML
    private TextField txtProductoModProducto;

    @FXML
    private ComboBox cmbEstatusModProducto;

    @FXML
    private Button btnAccionModProductos;

    @FXML
    private Circle shpEstatusProducto;

    //Componentes del módulo de Inventario
    @FXML
    private ComboBox cmbFiltrarEstatusProducto;

    @FXML
    private TableView<Producto> tblProductosInventario;

    @FXML
    private TableColumn colIdProductoModInventario;

    @FXML
    private TableColumn colProductoModInventario;

    @FXML
    private TableColumn colCantidadModInventario;

    @FXML
    private TableColumn colEstatusModInventario;

    private ObservableList<Producto> listaDeProductos;

    //Componentes del módulo Entrada y Salida
    @FXML
    private TextField txtIdProductoModEntradaSalida;

    @FXML
    private TextField txtCantidad;

    @FXML
    private ComboBox cmbMovimiento;

    @FXML
    private Button btnGuardarMovimiento;

    @FXML
    private TextField txtNombreProductoModEntradaSalida;

    @FXML
    private TextField txtExistencia;

    //Componentes del modulo Histórico
    @FXML
    private ComboBox cmbFiltroMovimiento;

    @FXML
    private TableView<TransaccionInventario> tblHistorico;

    @FXML
    private TableColumn colIdProductoModHistorico;

    @FXML
    private TableColumn colProductoModHistorico;

    @FXML
    private TableColumn colMovimientoModHistorico;

    @FXML
    private TableColumn colCantidadModHistorico;

    @FXML
    private TableColumn colFechaModHistorico;

    @FXML
    private TableColumn colRealizadoPorModHistorico;

    private ObservableList<TransaccionInventario> listaDeTransaccionesInventario;

    public PantallaPrincipalController() {
        this.usuario = new Usuario();
        opcionesEstatus = FXCollections.observableArrayList(
                "ACTIVO",
                "INACTIVO"
        );
        opcionesMovimiento = FXCollections.observableArrayList(
                "ENTRADA",
                "SALIDA"
        );

        //Por defecto al iniciar la app, no hay ninguna opción seleccionada
        accionModuloProducto = AccionSeleccionadaModuloProductos.NINGUNA;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configura las opciones de los filtros del ComboBox
        cmbFiltrarEstatusProducto.setItems(opcionesEstatus);
        cmbFiltroMovimiento.setItems(opcionesMovimiento);

        //Oculta el botón de acción del módulo de Productos
        btnAccionModProductos.setVisible(false);

        //Simular el click del primer botón para marcar que seleciona el módulo de Productos, y se apliquen los estilos
        btnProductos.fire();
    }

    @FXML
    private void eventTabProductos(ActionEvent event) {
        marcarModuloSeleccionado(event);

        resetearModuloPoductos();
        limpiarCamposModuloEntradaSalida();
        //Muestra el módulo (pestaña) Alta de productos
        tabModulos.getSelectionModel().select(tabProductos);

        permisosRoles();
    }

    @FXML
    private void eventTabInventario(ActionEvent event) throws URISyntaxException, IOException, InterruptedException {
        marcarModuloSeleccionado(event);

        resetearModuloPoductos();
        limpiarCamposModuloEntradaSalida();

        //Muestra el módulo (pestaña) de inventarios
        tabModulos.getSelectionModel().select(tabInventario);
        cmbFiltrarEstatusProducto.getSelectionModel().selectFirst();//Selecciona por defecto la primera opción (ACTIVO)
        mostrarInventario();
    }

    @FXML
    private void eventTabEntradaSalida(ActionEvent event) {
        marcarModuloSeleccionado(event);

        resetearModuloPoductos();
        limpiarCamposModuloEntradaSalida();

        //Muestra el módulo (pestaña) Entrada y Salidas
        tabModulos.getSelectionModel().select(tabEntradaSalida);

        txtIdProductoModEntradaSalida.requestFocus();//Asigna el foco a la entrada del Id
    }

    @FXML
    private void eventTabHistorico(ActionEvent event) throws URISyntaxException, IOException, InterruptedException {
        marcarModuloSeleccionado(event);

        resetearModuloPoductos();
        limpiarCamposModuloEntradaSalida();
        //Muestra el módulo (pestaña) del histórico de movimientos
        tabModulos.getSelectionModel().select(tabHistorico);
        cmbFiltroMovimiento.getSelectionModel().selectFirst(); //Selecciona ENTRADA por defecto
        mostrarHistorico();
    }

    @FXML
    private void eventNuevoProducto() {
        limpiarCamposModuloProductos();
        shpEstatusProducto.setFill(Color.BLACK); //Relleno
        shpEstatusProducto.setStroke(Color.BLACK); //Contorno

        //Activa las entradas
        txtProductoModProducto.setDisable(false);
        cmbEstatusModProducto.setDisable(false);

        //Desactiva las entradas
        txtIdProductoModProducto.setDisable(true);

        cmbEstatusModProducto.getItems().clear();//Borra todos los elemtos del item
        cmbEstatusModProducto.getItems().add("ACTIVO"); //Agrega esta opción por defecto
        cmbEstatusModProducto.getSelectionModel().selectFirst(); //Selecciona la única opción

        accionModuloProducto = AccionSeleccionadaModuloProductos.NUEVO;

        btnAccionModProductos.setText("Guardar producto");
        btnAccionModProductos.setVisible(true);

        txtProductoModProducto.requestFocus(); //Asigna el foco
    }

    @FXML
    private void eventBuscarProducto() {
        limpiarCamposModuloProductos();
        //Desactiva las entradas
        txtProductoModProducto.setDisable(true);
        cmbEstatusModProducto.setDisable(true);

        //Activa las entradas
        txtIdProductoModProducto.setDisable(false);

        accionModuloProducto = AccionSeleccionadaModuloProductos.BUSCAR;

        cmbEstatusModProducto.getItems().clear();//Borra todos los elemtos del item

        txtIdProductoModProducto.requestFocus(); //Asigna el foco

        btnAccionModProductos.setText("Buscar producto");
        btnAccionModProductos.setVisible(true);

    }

    @FXML
    private void eventEditarProducto() {
        //Activa las entradas
        txtProductoModProducto.setDisable(false);
        cmbEstatusModProducto.setDisable(false);

        //Desactiva las entradas
        txtIdProductoModProducto.setDisable(true);

        accionModuloProducto = AccionSeleccionadaModuloProductos.EDITAR;

        cmbEstatusModProducto.getItems().clear();//Borra todos los elementos
        cmbEstatusModProducto.getItems().add("ACTIVO");
        cmbEstatusModProducto.getItems().add("INACTIVO");

        txtProductoModProducto.requestFocus(); //Asigna el foco

        btnAccionModProductos.setText("Modificar producto");
        btnAccionModProductos.setVisible(true);
    }

    @FXML
    private void eventAccionModuloProductos() throws URISyntaxException, IOException, InterruptedException {
        switch (accionModuloProducto) {
            case AccionSeleccionadaModuloProductos.NUEVO:
                insertarNuevoProducto();
                limpiarCamposModuloProductos();
                break;
            case AccionSeleccionadaModuloProductos.BUSCAR:
                buscarProductoModuloProductos();
                break;
            case AccionSeleccionadaModuloProductos.EDITAR:
                modificarProducto();
                limpiarCamposModuloProductos();
                cmbEstatusModProducto.getItems().clear();

                shpEstatusProducto.setFill(Color.BLACK); //Relleno
                shpEstatusProducto.setStroke(Color.BLACK); //Contorno

                //Desaparece el botón para obligar al usuario a selecionar una acción
                btnAccionModProductos.setVisible(false);
                accionModuloProducto = AccionSeleccionadaModuloProductos.NINGUNA;
                break;
        }
    }

    @FXML
    private void eventFiltroEstatus() throws URISyntaxException, IOException, InterruptedException {
        mostrarInventario();
    }

    @FXML
    private void eventFiltroMovimiento() throws URISyntaxException, IOException, InterruptedException {
        mostrarHistorico();
    }

    @FXML
    private void eventBuscarDetalleProducto(KeyEvent event) throws URISyntaxException, IOException, InterruptedException {
        //Limpia los campos si no hay ningún Id, para que o se quede los datos de otro producto
        if (txtIdProductoModEntradaSalida.getText().isEmpty()) {
            limpiarCamposModuloEntradaSalida();
        }

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
            Alert mensaje = new Alert(Alert.AlertType.ERROR);
            mensaje.setTitle("Error");
            mensaje.setHeaderText("Carácter invalido");
            mensaje.setContentText("Sólo se aceptan números.");
            mensaje.showAndWait();
            limpiarCamposModuloEntradaSalida();
            return;
        }

        buscarDetalleProducto();
    }

    @FXML
    public void eventValidarCantidadEntradaSalida(KeyEvent event) {
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
            Alert mensaje = new Alert(Alert.AlertType.ERROR);
            mensaje.setTitle("Error");
            mensaje.setHeaderText("Cantidad invalida");
            mensaje.setContentText("Sólo se pueden introducir númerosenteros positivos.");
            mensaje.showAndWait();
            txtCantidad.setText("");
        }
    }

    private void buscarProductoModuloProductos() throws URISyntaxException, IOException, InterruptedException {
        if (txtIdProductoModProducto.getText().isEmpty()) {
            Alert mensaje = new Alert(Alert.AlertType.ERROR);
            mensaje.setTitle("Error");
            mensaje.setHeaderText("Campo vacío");
            mensaje.setContentText("Debes ingresar el Id del producto.");
            mensaje.showAndWait();

            return; //Termina la función (ignora el resto del código)
        }

        //Manda llamar la API y le pasa el Id en la url para buscar al producto
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/RESTfulInventario/resources/productos/" + txtIdProductoModProducto.getText()))
                .GET().build();

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

        //Comprueba si el servidor dió respuesta
        if (getResponse.statusCode() == 200) {
            //Verifica si se obtuvo acceso a la BD
            if (!getResponse.body().equals("No se pudo establecer conexión con la BD.")) {
                Gson productoJson = new Gson();
                //Convierte el Producto que regresa la api a un objeto de tipo Producto
                Producto producto = productoJson.fromJson(getResponse.body(), Producto.class);

                //Comprueba si existe el usuario
                //Si el Id es 0, significa que no existe el usuario
                if (producto.getId_producto() > 0) {
                    txtIdProductoModProducto.setDisable(true); //Desactiva la entrada del Id
                    cmbEstatusModProducto.setDisable(false); //Activa la entrada del estatus

                    //Muestra la info. del producto en las entradas
                    txtIdProductoModProducto.setText(String.valueOf(producto.getId_producto()));
                    txtProductoModProducto.setText(producto.getProducto());
                    cmbEstatusModProducto.getItems().add(producto.getEstatus());
                    cmbEstatusModProducto.getSelectionModel().selectFirst();

                    if (producto.getEstatus().equals("ACTIVO")) {
                        shpEstatusProducto.setFill(Color.GREEN); //Relleno
                        shpEstatusProducto.setStroke(Color.GREEN); //Contorno
                    } else {
                        shpEstatusProducto.setFill(Color.RED);
                        shpEstatusProducto.setStroke(Color.RED);
                    }

                } else {
                    Alert mensaje = new Alert(Alert.AlertType.ERROR);
                    mensaje.setTitle("Error");
                    mensaje.setHeaderText("El producto no existe");
                    mensaje.setContentText("Verifica que hayas introducido el Id correcto.");
                    mensaje.showAndWait();
                }
            } else {
                Alert mensaje = new Alert(Alert.AlertType.ERROR);
                mensaje.setTitle("Error");
                mensaje.setHeaderText("Sin conexión a la BD");
                mensaje.setContentText("Verifica que se este apuntando a la BD correcta, que la IP del servidor y el puerto sean correctos y que el usuario y contraseña de la BD también lo sean.");
                mensaje.showAndWait();
            }
        }
    }

    @FXML
    private void eventGuardarMovimiento() throws URISyntaxException, IOException, InterruptedException {
        if (txtIdProductoModEntradaSalida.getText().isEmpty() || txtCantidad.getText().isEmpty()
                || cmbMovimiento.getSelectionModel().getSelectedItem() == null) {
            Alert mensaje = new Alert(Alert.AlertType.ERROR);
            mensaje.setTitle("Error");
            mensaje.setHeaderText("Campos vacíos");
            mensaje.setContentText("Debes completar todos los campos.");
            mensaje.showAndWait();
            return;
        }

        Producto producto = new Producto();
        //Sólo necesito el Id del producto a registrar
        producto.setId_producto(Integer.parseInt(txtIdProductoModEntradaSalida.getText()));

        TransaccionInventario transaccionInventario = new TransaccionInventario();
        //Sólo necesito la cantidad y el movimiento
        int cantidad = Integer.parseInt(txtCantidad.getText());
        int existencia = Integer.parseInt(txtExistencia.getText());

        if (cmbMovimiento.getSelectionModel().getSelectedItem().toString().equals("SALIDA")) {

            if (cantidad > existencia) {
                Alert mensaje = new Alert(Alert.AlertType.ERROR);
                mensaje.setTitle("Error");
                mensaje.setHeaderText("Existencia insuficiente");
                mensaje.setContentText("No se puede generar la salida.");
                mensaje.showAndWait();
                return;
            }

            //Convierte la cantidad en negativa
            cantidad = -cantidad;
        }

        transaccionInventario.setCantidad(cantidad);
        transaccionInventario.setMovimiento(cmbMovimiento.getSelectionModel().getSelectedItem().toString());

        //Crea este objeto contenedor de objetos para convertirlos a json y enviarlos al servidor
        ContenedorObjetosTransaccion contenedorObj = new ContenedorObjetosTransaccion(usuario, producto, transaccionInventario);

        //Convertir a JSON
        Gson gson = new Gson();
        String objetosJson = gson.toJson(contenedorObj);

        //Manda llamar la API y le pasa los tres objetos en formato Json
        HttpRequest putRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/RESTfulInventario/resources/entrada-salida"))
                .headers("Content-Type", "application/json")
                .PUT(BodyPublishers.ofString(objetosJson)).build();

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse<String> putResponse = httpClient.send(putRequest, HttpResponse.BodyHandlers.ofString());

        if (putResponse.statusCode() == 200) {

            String respuesta = putResponse.body();
            Alert mensaje = new Alert(Alert.AlertType.NONE);

            switch (respuesta) {
                case "Creado":
                    mensaje.setAlertType(Alert.AlertType.INFORMATION);
                    mensaje.setTitle("Éxito al guardar");
                    mensaje.setHeaderText("Transacción creada");
                    mensaje.setContentText("La transacción se creó.");
                    limpiarCamposModuloEntradaSalida();
                    txtIdProductoModEntradaSalida.requestFocus();//Asigna el foco
                    break;
                case "No creado":
                    mensaje.setAlertType(Alert.AlertType.ERROR);
                    mensaje.setTitle("Fallo al guardar");
                    mensaje.setHeaderText("La transacción no fue creada");
                    mensaje.setContentText("No se pudo insertar la transacción al inventario.");
                    break;
                case "No se pudo establecer conexión con la BD":
                    mensaje.setAlertType(Alert.AlertType.INFORMATION);
                    mensaje.setTitle("Error");
                    mensaje.setHeaderText("Sin conexión a la BD");
                    mensaje.setContentText("Verifica que se este apuntando a la BD correcta, que la IP del servidor y el puerto sean correctos y que el usuario y contraseña de la BD también lo sean.");
                    break;
            }

            mensaje.showAndWait();
        }
    }

    public void modificarProducto() throws URISyntaxException, IOException, InterruptedException {

        if (txtProductoModProducto.getText().isEmpty() || txtIdProductoModProducto.getText().isEmpty()
                || cmbEstatusModProducto.getSelectionModel().getSelectedItem() == null) {
            Alert mensaje = new Alert(Alert.AlertType.ERROR);
            mensaje.setTitle("Error");
            mensaje.setHeaderText("Campos vacíos");
            mensaje.setContentText("Dejaste campos en blanco.");
            mensaje.showAndWait();
            return;
        }

        Producto producto = new Producto();

        producto.setId_producto(Integer.parseInt(txtIdProductoModProducto.getText()));
        producto.setProducto(txtProductoModProducto.getText().toUpperCase()); //Asigna el nombre del producto en mayúsculas
        producto.setEstatus(cmbEstatusModProducto.getSelectionModel().getSelectedItem().toString());

        Gson json = new Gson();
        String jsonProducto = json.toJson(producto); //Convierte el objeto Producto a un Json

        //Manda llamar la API y le pasa el objeto de productos paara modificarlo
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/RESTfulInventario/resources/productos/"))
                .headers("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(jsonProducto)).build();

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

        if (postResponse.statusCode() == 200) {

            String respuesta = postResponse.body();
            Alert mensaje = new Alert(Alert.AlertType.NONE);

            switch (respuesta) {
                case "Modificado":
                    mensaje.setAlertType(Alert.AlertType.INFORMATION);
                    mensaje.setTitle("Éxito al modificar");
                    mensaje.setHeaderText("Registro modificado");
                    mensaje.setContentText("Haz modificado un producto del inventario.");
                    break;
                case "No modificado":
                    mensaje.setAlertType(Alert.AlertType.ERROR);
                    mensaje.setTitle("Fallo al modificar");
                    mensaje.setHeaderText("El registro no fue modificado");
                    mensaje.setContentText("No se pudo modificar el producto.");
                    break;
                case "No se pudo establecer conexión con la BD":
                    mensaje.setAlertType(Alert.AlertType.INFORMATION);
                    mensaje.setTitle("Error");
                    mensaje.setHeaderText("Sin conexión a la BD");
                    mensaje.setContentText("Verifica que se este apuntando a la BD correcta, que la IP del servidor y el puerto sean correctos y que el usuario y contraseña de la BD también lo sean.");
                    break;
            }

            mensaje.showAndWait();
        }
    }

    private void insertarNuevoProducto() throws URISyntaxException, IOException, InterruptedException {
        if (txtProductoModProducto.getText().isEmpty()) {
            Alert mensaje = new Alert(Alert.AlertType.ERROR);
            mensaje.setTitle("Error");
            mensaje.setHeaderText("Campo vacío");
            mensaje.setContentText("Dejaste el campo Producto en blanco.");
            mensaje.showAndWait();
            return;
        }

        Producto producto = new Producto();
        //Asigna el nombre del producto en mayúsculas
        producto.setProducto(txtProductoModProducto.getText().toUpperCase());

        Gson json = new Gson();
        String jsonProducto = json.toJson(producto); //Convierte el objeto Producto a un Json

        //Manda llamar la API y le pasa el Id y contraseña en la url para buscar al usuario
        HttpRequest putRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/RESTfulInventario/resources/productos/"))
                .headers("Content-Type", "application/json")
                .PUT(BodyPublishers.ofString(jsonProducto)).build();

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse<String> putResponse = httpClient.send(putRequest, HttpResponse.BodyHandlers.ofString());

        if (putResponse.statusCode() == 200) {

            String respuesta = putResponse.body();
            Alert mensaje = new Alert(Alert.AlertType.NONE);

            switch (respuesta) {
                case "Creado":
                    mensaje.setAlertType(Alert.AlertType.INFORMATION);
                    mensaje.setTitle("Éxito al guardar");
                    mensaje.setHeaderText("Registro creado");
                    mensaje.setContentText("Haz insertado un nuevo producto al inventario.");
                    break;
                case "No creado":
                    mensaje.setAlertType(Alert.AlertType.ERROR);
                    mensaje.setTitle("Fallo al guardar");
                    mensaje.setHeaderText("El registro no fue creado");
                    mensaje.setContentText("No se pudo insertado el nuevo producto al inventario.");
                    break;
                case "No se pudo establecer conexión con la BD":
                    mensaje.setAlertType(Alert.AlertType.INFORMATION);
                    mensaje.setTitle("Error");
                    mensaje.setHeaderText("Sin conexión a la BD");
                    mensaje.setContentText("Verifica que se este apuntando a la BD correcta, que la IP del servidor y el puerto sean correctos y que el usuario y contraseña de la BD también lo sean.");
                    break;
            }

            mensaje.showAndWait();
        }

    }

    private void limpiarCamposModuloProductos() {
        txtIdProductoModProducto.setText("");
        txtProductoModProducto.setText("");
    }

    private void limpiarCamposModuloEntradaSalida() {
        txtIdProductoModEntradaSalida.setText("");
        txtCantidad.setText("");
        cmbMovimiento.getSelectionModel().select(-1);
        txtNombreProductoModEntradaSalida.setText("");
        txtExistencia.setText("");
    }

    private void resetearModuloPoductos() {
        limpiarCamposModuloProductos();
        cmbEstatusModProducto.getItems().clear();

        accionModuloProducto = AccionSeleccionadaModuloProductos.NINGUNA;

        btnAccionModProductos.setVisible(false);

        shpEstatusProducto.setFill(Color.BLACK);
        shpEstatusProducto.setStroke(Color.BLACK);

        //Desactiva las entradas
        txtIdProductoModProducto.setDisable(true);
        txtProductoModProducto.setDisable(true);
        cmbEstatusModProducto.setDisable(true);
    }

    private void mostrarInventario() throws URISyntaxException, IOException, InterruptedException {
        //Manda llamar la API y le pasa el estatus a filtrar en la url
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/RESTfulInventario/resources/inventario/" + cmbFiltrarEstatusProducto.getSelectionModel().getSelectedItem().toString()))
                .GET().build();

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

        //Comprueba si el servidor dió respuesta
        if (getResponse.statusCode() == 200) {
            //Verifica si se obtuvo acceso a la BD
            if (!getResponse.body().equals("No se pudo establecer conexión con la BD.")) {
                Gson productosJson = new Gson();

                //Convierte la lista de objetos Producto que estan en formato Json a Objetos de tipo Producto
                //y los agrega al array
                ArrayList<Producto> listaProducto = productosJson.fromJson(getResponse.body(), new TypeToken<ArrayList<Producto>>() {
                }.getType());

                //Comprueba si hay datos
                if (listaProducto.size() > 0) {
                    listaDeProductos = FXCollections.observableArrayList();
                    listaDeProductos.addAll(listaProducto); //Pasa todos los elementos del ArrayList al observableList

                    //Llena las columnas de la tabbla
                    colIdProductoModInventario.setCellValueFactory(new PropertyValueFactory("id_producto"));
                    colProductoModInventario.setCellValueFactory(new PropertyValueFactory("producto"));
                    colCantidadModInventario.setCellValueFactory(new PropertyValueFactory("cantidad"));
                    colEstatusModInventario.setCellValueFactory(new PropertyValueFactory("estatus"));

                    tblProductosInventario.setItems(listaDeProductos); //Muestra los registros
                } else {
                    try {
                        //Borra los registros de la tabla
                        listaDeProductos.clear();
                    } catch (Exception ex) {

                    }
                    tblProductosInventario.setItems(listaDeProductos);

                    Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                    mensaje.setTitle("Sin datos");
                    mensaje.setHeaderText("No se encontraron registros");
                    mensaje.setContentText("No hay datos con el estatus seleccionado.");
                    mensaje.showAndWait();
                }
            } else {
                Alert mensaje = new Alert(Alert.AlertType.ERROR);
                mensaje.setTitle("Error");
                mensaje.setHeaderText("Sin conexión a la BD");
                mensaje.setContentText("Verifica que se este apuntando a la BD correcta, que la IP del servidor y el puerto sean correctos y que el usuario y contraseña de la BD también lo sean.");
                mensaje.showAndWait();
            }
        }
    }

    private void mostrarHistorico() throws URISyntaxException, IOException, InterruptedException {
        //Manda llamar la API y le pasa el movimiento a filtrar en la url
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/RESTfulInventario/resources/historico/" + cmbFiltroMovimiento.getSelectionModel().getSelectedItem().toString()))
                .GET().build();

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

        //Comprueba si el servidor dió respuesta
        if (getResponse.statusCode() == 200) {
            //Verifica si se obtuvo acceso a la BD
            if (!getResponse.body().equals("No se pudo establecer conexión con la BD.")) {
                Gson transaccionesJson = new Gson();

                //Convierte la lista de objetos Transacciones que estan en formato Json a Objetos de tipo Transacciones
                //y los agrega al array
                ArrayList<TransaccionInventario> listaTransacciones = transaccionesJson.fromJson(getResponse.body(), new TypeToken<ArrayList<TransaccionInventario>>() {
                }.getType());

                //Comprueba si hay datos
                if (listaTransacciones.size() > 0) {
                    listaDeTransaccionesInventario = FXCollections.observableArrayList();
                    listaDeTransaccionesInventario.addAll(listaTransacciones); //Pasa todos los elementos del ArrayList al observableList

                    //Llena las columnas de la tabla
                    colIdProductoModHistorico.setCellValueFactory(new PropertyValueFactory("idProducto"));
                    colProductoModHistorico.setCellValueFactory(new PropertyValueFactory("producto"));
                    colMovimientoModHistorico.setCellValueFactory(new PropertyValueFactory("movimiento"));
                    colCantidadModHistorico.setCellValueFactory(new PropertyValueFactory("cantidad"));
                    colFechaModHistorico.setCellValueFactory(new PropertyValueFactory("fechaHoraTransaccion"));
                    colRealizadoPorModHistorico.setCellValueFactory(new PropertyValueFactory("realizadoPorUsuario"));

                    tblHistorico.setItems(listaDeTransaccionesInventario); //Muestra los registros
                } else {
                    try {
                        //Borra los registros de la tabla
                        listaDeTransaccionesInventario.clear();
                    } catch (Exception e) {

                    }
                    tblHistorico.setItems(listaDeTransaccionesInventario);

                    Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                    mensaje.setTitle("Sin datos");
                    mensaje.setHeaderText("No se encontraron registros");
                    mensaje.setContentText("No hay datos con el movimiento seleccionado.");
                    mensaje.showAndWait();
                }
            } else {
                Alert mensaje = new Alert(Alert.AlertType.ERROR);
                mensaje.setTitle("Error");
                mensaje.setHeaderText("Sin conexión a la BD");
                mensaje.setContentText("Verifica que se este apuntando a la BD correcta, que la IP del servidor y el puerto sean correctos y que el usuario y contraseña de la BD también lo sean.");
                mensaje.showAndWait();
            }
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        //this.Usuario.setNombre(Usuario.getNombre());
        this.usuario = usuario;

        lblNombreUsuario.setText(this.usuario.getNombre());
        lblRolUsuario.setText(this.usuario.getRol());
    }

    private void buscarDetalleProducto() throws URISyntaxException, IOException, InterruptedException {
        int idProducto = Integer.parseInt(txtIdProductoModEntradaSalida.getText());

//Manda llamar la API y le pasa el Id en la url para buscar al producto
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/RESTfulInventario/resources/entrada-salida/" + idProducto))
                .GET().build();

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

        //Comprueba si el servidor dió respuesta
        if (getResponse.statusCode() == 200) {
            //Verifica si se obtuvo acceso a la BD
            if (!getResponse.body().equals("No se pudo establecer conexión con la BD.")) {
                Gson productoJson = new Gson();
                //Convierte el Producto que regresa la api a un objeto de tipo Producto
                Producto producto = productoJson.fromJson(getResponse.body(), Producto.class);

                //Comprueba si existe el usuario
                //Si el Id es 0, significa que no existe el producto o esta inactivo
                if (producto.getId_producto() > 0) {
                    //Muestra la info. del detalle del producto
                    txtNombreProductoModEntradaSalida.setText(String.valueOf(producto.getProducto()));
                    txtExistencia.setText(String.valueOf(producto.getCantidad()));
                } else {
                    limpiarCamposModuloEntradaSalida();
                    Alert mensaje = new Alert(Alert.AlertType.ERROR);
                    mensaje.setTitle("Error");
                    mensaje.setHeaderText("El producto no existe o tiene estatus de Inactivo");
                    mensaje.setContentText("Introducido u producto Activo.");
                    mensaje.showAndWait();
                }
            } else {
                Alert mensaje = new Alert(Alert.AlertType.ERROR);
                mensaje.setTitle("Error");
                mensaje.setHeaderText("Sin conexión a la BD");
                mensaje.setContentText("Verifica que se este apuntando a la BD correcta, que la IP del servidor y el puerto sean correctos y que el usuario y contraseña de la BD también lo sean.");
                mensaje.showAndWait();
            }
        }
    }

    private void marcarModuloSeleccionado(ActionEvent event) {
        //Recorre todos los botones de la lista de módulo para saber cual fue pulsado para cambiarle el color
        for (Node node : pnlListaModulos.getChildren()) {
            Button btn = (Button) node;

            if (event.getSource() == btn) {
                //Elimina el fondo gris para que tone el azul
                btn.getStyleClass().removeAll("moduloDesceleccionado");
            } else {
                //Asigna el fondo gris
                btn.getStyleClass().add("moduloDesceleccionado");
            }
        }
    }

    public void permisosRoles() {
        cmbMovimiento.getItems().clear(); //Elimina todos los items

        switch (this.usuario.getRol()) {
            case "ADMINISTRADOR":
                cmbMovimiento.getItems().add("ENTRADA");
                break;
            case "ALMACENISTA":
                cmbMovimiento.getItems().add("SALIDA");
                pnlListaModulos.getChildren().remove(btnProductos); //Elimina el botón que navega al módulo Productos
                pnlListaModulos.getChildren().remove(btnHistorico); //Elimina el botón que navega al módulo Histórico
                btnInventario.fire();
                break;
        }
    }
}
