package controllers;

import controllers.fragments.OrderController;
import controllers.fragments.OrderItemController;
import controllers.fragments.OrdersHistoryController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Order;
import models.Restaurant;
import models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class AdminPageOrdersController extends AdminPageControllerAbstract {

    @FXML
    private Label sub_header;
    @FXML
    private Label sub_header2;
    @FXML
    private GridPane readyGrid;
    @FXML
    private GridPane inProcessGrid;

    @FXML private ScrollPane scrollPane_ready;
    @FXML private ScrollPane scrollPane_InProcess;

    private List<VBox> ordersVboxes = new ArrayList<>();
    private List<Order> orderListOfCompleted = new LinkedList<>();
    private List<Integer> selectedOrdersId = new LinkedList<>();

    public void initializeWithData(User user, boolean islightMode) {

        this.user = user;
        this.restaurant = Restaurant.retrieveRestaurant();

        scrollPane_ready.addEventFilter(ScrollEvent.ANY, event -> {
            if (event.getEventType() == ScrollEvent.SCROLL) {
                double deltaY = event.getDeltaY();
                scrollPane_ready.setVvalue(scrollPane_ready.getVvalue() - deltaY / scrollPane_ready.getHeight());
                event.consume();
            }
        });
        scrollPane_ready.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        scrollPane_InProcess.addEventFilter(ScrollEvent.ANY, event -> {
            if (event.getEventType() == ScrollEvent.SCROLL) {
                double deltaY = event.getDeltaY();
                scrollPane_InProcess.setVvalue(scrollPane_InProcess.getVvalue() - deltaY / scrollPane_InProcess.getHeight());
                event.consume();
            }
        });
        scrollPane_InProcess.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        createOrdersInProcess();
        createOrdersCompleted();

        if (!islightMode) {
            handleChangeColorMode();
        }
    }

    private void createOrdersInProcess(){
        //In process

        List<Order> orderList = Order.getAllOrdersByStatus("In process");

        int column = 0;
        int row = 1;
        for (int i = 0; i < orderList.size(); i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml_files/fragments/order-item.fxml"));
            VBox[] root = new VBox[1];
            try {
                root[0] = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            OrderItemController controller = loader.getController();
            controller.setData(orderList.get(i));

            root[0].setOnMouseClicked(event -> {
                if (!root[0].getStyleClass().contains("vbox_item_selected")) {
                    root[0].getStyleClass().add("vbox_item_selected");
                    int id = Integer.parseInt(((Label) root[0].getChildren().get(0)).getText().split(" ")[1]);
                    selectedOrdersId.add(id);

                }else {
                    root[0].getStyleClass().remove("vbox_item_selected");
                    int id = Integer.parseInt(((Label) root[0].getChildren().get(0)).getText().split(" ")[1]);
                    selectedOrdersId.remove(id);
                }

            });

            root[0].setOnTouchPressed(event -> {
                if (!root[0].getStyleClass().contains("vbox_item_selected")) {
                    root[0].getStyleClass().add("vbox_item_selected");
                    int id = Integer.parseInt(((Label) root[0].getChildren().get(0)).getText().split(" ")[1]);
                    selectedOrdersId.add(id);
                }else {
                    root[0].getStyleClass().remove("vbox_item_selected");
                    int id = Integer.parseInt(((Label) root[0].getChildren().get(0)).getText().split(" ")[1]);
                    selectedOrdersId.remove(id);
                }

            });

            ordersVboxes.add(root[0]);

            if (column == 3) {
                column = 0;
                row++;
            }

            inProcessGrid.add(root[0], column++, row);
            GridPane.setMargin(root[0], new Insets(46.5));

            if (!this.islightMode) {
                root[0].getChildren().forEach(x -> {
                    x.getStyleClass().remove("small_text_label_black");
                    x.getStyleClass().add("small_text_label_white");
                });
            }
        }
    }

    private void createOrdersCompleted(){
        //Completed

        orderListOfCompleted = Order.getAllOrdersByStatus("Completed");

        int column = 0;
        int row = 1;
        for (int i = 0; i < orderListOfCompleted.size(); i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml_files/fragments/order-item.fxml"));
            VBox[] root = new VBox[1];
            try {
                root[0] = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            OrderItemController controller = loader.getController();
            controller.setData(orderListOfCompleted.get(i));

            root[0].getStyleClass().remove("vbox_shadow_select");
            root[0].getStyleClass().add("vbox_item_green");
            root[0].getChildren().forEach(x -> {
                x.getStyleClass().remove("small_text_label_black");
                x.getStyleClass().add("order_text_green");
            });


            if (column == 3) {
                column = 0;
                row++;
            }

            readyGrid.add(root[0], column++, row);
            GridPane.setMargin(root[0], new Insets(46.5));
        }
    }

    private void openSubwindow(Stage ownerStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml_files/fragments/orders-history.fxml")));
            Parent root = fxmlLoader.load();
            OrdersHistoryController controller = fxmlLoader.getController();

            Stage subwindow = new Stage();

            subwindow.setTitle("Order Confirmation");
            subwindow.initModality(Modality.APPLICATION_MODAL);
            subwindow.initOwner(ownerStage);

            subwindow.initStyle(StageStyle.UNDECORATED);

            subwindow.setWidth(600);
            subwindow.setHeight(600);
            subwindow.setResizable(false);

            controller.initialize();
            controller.setStage(subwindow);

            subwindow.setScene(new Scene(root));
            subwindow.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleChangeColorMode() {
        if (islightMode){
            sub_header.getStyleClass().remove("text_label_black");
            sub_header.getStyleClass().add("text_label_white");

            sub_header2.getStyleClass().remove("text_label_black");
            sub_header2.getStyleClass().add("text_label_white");

            infoButton.getStyleClass().remove("active_buttonWhite");
            infoButton.getStyleClass().add("active_buttonDark");

            scrollPane_ready.getStyleClass().remove("myScrollPane_white");
            scrollPane_ready.getStyleClass().add("myScrollPane_dark");

            scrollPane_InProcess.getStyleClass().remove("myScrollPane_white");
            scrollPane_InProcess.getStyleClass().add("myScrollPane_dark");

            ordersVboxes.forEach(vBox -> vBox.getChildren().forEach(x->{
                x.getStyleClass().remove("small_text_label_black");
                x.getStyleClass().add("small_text_label_white");
            }));

        }else {
            infoButton.getStyleClass().remove("active_buttonDark");
            infoButton.getStyleClass().add("active_buttonWhite");

            sub_header.getStyleClass().remove("text_label_white");
            sub_header.getStyleClass().add("text_label_black");

            sub_header2.getStyleClass().remove("text_label_white");
            sub_header2.getStyleClass().add("text_label_black");

            scrollPane_ready.getStyleClass().remove("myScrollPane_dark");
            scrollPane_ready.getStyleClass().add("myScrollPane_white");

            scrollPane_InProcess.getStyleClass().remove("myScrollPane_dark");
            scrollPane_InProcess.getStyleClass().add("myScrollPane_white");

            ordersVboxes.forEach(vBox -> vBox.getChildren().forEach(x->{
                x.getStyleClass().remove("small_text_label_white");
                x.getStyleClass().add("small_text_label_black");
            }));

        }
        super.handleChangeColorMode();
    }

    @FXML public void handleGetHistory(){
        openSubwindow(stage);
    }

    @FXML public void handleClearAll(){
        orderListOfCompleted.forEach(x->{
            Order.updateOrderStatus(x.getId(),"Finished");
        });
        orderListOfCompleted.clear();
        refreshOrdersView();
    }

    @FXML public void handleDeclineButton(){
        selectedOrdersId.forEach(x->{
            Order.updateOrderStatus(x,"Declined");
        });
        selectedOrdersId.clear();
        refreshOrdersView();
    }

    @FXML public void handleSelectButton(){
        selectedOrdersId.forEach(x->{
            Order.updateOrderStatus(x,"Completed");
        });
        selectedOrdersId.clear();
        refreshOrdersView();
    }

    private void refreshOrdersView() {
        inProcessGrid.getChildren().clear();
        readyGrid.getChildren().clear();
        ordersVboxes.clear();

        createOrdersInProcess();
        createOrdersCompleted();
    }


    @Override
    protected boolean shouldUseKeyboard() {
        return false;
    }
}
