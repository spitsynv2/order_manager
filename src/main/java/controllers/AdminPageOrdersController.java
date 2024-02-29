package controllers;

import controllers.fragments.OrderItemController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.Order;
import models.Restaurant;
import models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
    private List<Order> orderList = new LinkedList<>();

    private Label[] labels;

    public void initializeWithData(User user, boolean islightMode) {

        this.user = user;
        this.restaurant = Restaurant.retrieveRestaurant();

        labels = new Label[]{};

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

        orderList = Order.getAllOrdersInProcess();

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
                }else {
                    root[0].getStyleClass().remove("vbox_item_selected");
                }

            });

            root[0].setOnTouchPressed(event -> {
                if (!root[0].getStyleClass().contains("vbox_item_selected")) {
                    root[0].getStyleClass().add("vbox_item_selected");
                }else {
                    root[0].getStyleClass().remove("vbox_item_selected");
                }

            });

            ordersVboxes.add(root[0]);

            if (column == 3) {
                column = 0;
                row++;
            }

            inProcessGrid.add(root[0], column++, row);
            GridPane.setMargin(root[0], new Insets(42.5));

            if (!this.islightMode) {
                root[0].getChildren().forEach(x -> {
                    x.getStyleClass().remove("small_text_label_black");
                    x.getStyleClass().add("small_text_label_white");
                });
            }
        }



        if (!islightMode) {
            handleChangeColorMode();
        }
    }

    @Override
    protected void handleChangeColorMode() {
        if (islightMode){
            for (Label label : labels) {
                label.getStyleClass().remove("text_label_black_medium");
                label.getStyleClass().add("text_label_white_medium");
            }

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
            for (Label label : labels) {
                label.getStyleClass().remove("text_label_white_medium");
                label.getStyleClass().add("text_label_black_medium");
            }
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

    }

    @FXML public void handleClearAll(){

    }

    @FXML public void handleDeclineButton(){

    }

    @FXML public void handleSelectButton(){

    }


    @Override
    protected boolean shouldUseKeyboard() {
        return false;
    }
}
