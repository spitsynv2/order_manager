package controllers.fragments;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Order;
import models.User;

import javafx.scene.image.ImageView;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrdersHistoryController {

    private Stage stage;

    @FXML private TableView ordersTable;
    @FXML private ScrollPane scrollPane;
    @FXML private ImageView chequeImage;
    @FXML private ScrollPane chequeScrollPane;
    @FXML private HBox hBox;

    @FXML private Label pathLabel;

    private boolean exitButton = true;


    private List<Order> ordersList = new ArrayList<>();

    public void initialize(){
        ordersList = Order.getAllOrders();
        fillTable();
        chequeScrollPane.setManaged(false);
        chequeScrollPane.setVisible(false);
        pathLabel.setManaged(false);
        pathLabel.setVisible(false);

        scrollPane.addEventFilter(ScrollEvent.ANY, event -> {
            if (event.getEventType() == ScrollEvent.SCROLL) {
                double deltaY = event.getDeltaY();
                scrollPane.setVvalue(scrollPane.getVvalue() - deltaY / scrollPane.getHeight());
                event.consume();
            }
        });
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void fillTable(){
        TableColumn<User, Integer> id_column = new TableColumn<>("Id");
        TableColumn<User, String> date_column = new TableColumn<>("Date");
        TableColumn<User, String> status_column = new TableColumn<>("Status");

        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        date_column.setCellValueFactory(new PropertyValueFactory<>("date"));
        status_column.setCellValueFactory(new PropertyValueFactory<>("status"));

        id_column.setPrefWidth(100);
        date_column.setPrefWidth(234);
        status_column.setPrefWidth(234);

        ordersTable.setMouseTransparent(true);

        ordersTable.getColumns().addAll(id_column, date_column, status_column);

        ordersTable.setItems(FXCollections.observableArrayList(ordersList));
        handleGoDownButton();
    }

    @FXML
    private void handleGoUpButton() {
        int selectedIndex = ordersTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex > 0) {
            ordersTable.getSelectionModel().select(selectedIndex - 1);
            scrollToSelection();
        }
    }
    @FXML
    private void handleGoDownButton() {
        int selectedIndex = ordersTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex < ordersList.size() - 1) {
            ordersTable.getSelectionModel().select(selectedIndex + 1);
            scrollToSelection();
        }
    }

    private void scrollToSelection() {
        int selectedIndex = ordersTable.getSelectionModel().getSelectedIndex();
        ordersTable.scrollTo(selectedIndex);
    }

    @FXML
    private void handleGetCheque(){
        exitButton = false;
        hBox.setManaged(false);
        hBox.setVisible(false);
        scrollPane.setVisible(false);
        chequeScrollPane.setManaged(true);
        chequeScrollPane.setVisible(true);
        pathLabel.setManaged(true);
        pathLabel.setVisible(true);
        Order selectedOrder = (Order) ordersTable.getSelectionModel().getSelectedItem();
        int id = selectedOrder.getId();
        int size = Order.getUniqueDishesByOrderId(id).size();

        if (size <= 8){
            chequeImage.setFitHeight(500);
            chequeImage.setFitWidth(200);
        }else {
            chequeImage.setFitHeight(0);
            chequeImage.setFitWidth(500);
        }

        String filePath = System.getProperty("user.home") + File.separator + "order_document_number_" + id +".pdf";
        pathLabel.setText(filePath);
        try (PDDocument orderExample = Loader.loadPDF(new File(filePath))) {
            PDFRenderer renderer = new PDFRenderer(orderExample);

            final float dpi = 250;
            BufferedImage bufferedImage = renderer.renderImageWithDPI(0, dpi);

            File path = new File(System.getProperty("user.home") + File.separator + "example_image.png");
            ImageIO.write(bufferedImage, "png", path);
            Image image = new Image(path.toURI().toString());
            chequeImage.setImage(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleExitButton(){
        if (exitButton){
            stage.close();
        }else {
            chequeScrollPane.setManaged(false);
            chequeScrollPane.setVisible(false);
            pathLabel.setManaged(false);
            pathLabel.setVisible(false);
            hBox.setManaged(true);
            hBox.setVisible(true);
            scrollPane.setVisible(true);
            exitButton = true;
        }
    }
}
