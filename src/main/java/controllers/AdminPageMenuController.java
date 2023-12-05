package controllers;

import controllers.fragments.MenuItemController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.Dish;
import models.Restaurant;
import models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminPageMenuController extends AdminPageControllerAbstract{

    @FXML private Label sub_header;
    @FXML private Label sub_header2;
    @FXML private GridPane menu_gridPane;
    @FXML private ScrollPane scrollPane;
    @FXML private TextArea text_area;
    @FXML private ChoiceBox sort_choiceBox;

    private List<Dish> dishList;

    private List<VBox> products = new ArrayList<>();

    private List<Dish> selectedDish = new ArrayList<>();

    private String text;


    public void initializeWithData(User user, boolean islightMode) throws IOException {

        scrollPane.addEventFilter(ScrollEvent.ANY, event -> {
            if (event.getEventType() == ScrollEvent.SCROLL) {
                double deltaY = event.getDeltaY();
                scrollPane.setVvalue(scrollPane.getVvalue() - deltaY / scrollPane.getHeight());
                event.consume();
            }
        });

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        this.user = user;
        restaurant = Restaurant.retrieveRestaurant();

        dishList = Dish.retrieveAllDishes();

        text = "Cashier: "+user.getName()+", Tax: "+restaurant.getTax()+"\n";
        text = text+ "Product Name, Product Price: "+"\n";
        text = text + "--------------------------------------"+"\n";
        text_area.setText(text);



        int column = 0;
        int row = 1;
        for (int i = 0; i<dishList.size(); i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml_files/fragments/menu-item.fxml"));
            VBox root = loader.load();
            MenuItemController controller = loader.getController();
            controller.setData(dishList.get(i),i);

            products.add(root);

            root.setOnMouseClicked(event -> {
                if (root.getStyleClass().contains("vbox_item_selected")){
                    root.getStyleClass().remove("vbox_item_selected");
                    int id = Integer.parseInt(((Label) root.getChildren().get(3)).getText());
                    selectedDish.remove(dishList.get(id));

                    text = "Cashier: "+user.getName()+", Tax: "+restaurant.getTax()+"\n";
                    text = text+ "Product Name, Product Price: "+"\n";
                    text = text + "--------------------------------------"+"\n";
                    for (Dish dish: selectedDish){
                        text = text + dish.getName()+", "+dish.getPrice()+"\n";
                        text = text + "--------------------------------------"+"\n";
                    }
                    text_area.setText(text);
                }else {
                    root.getStyleClass().add("vbox_item_selected");
                    int id = Integer.parseInt(((Label) root.getChildren().get(3)).getText());
                    selectedDish.add(dishList.get(id));

                    text = "Cashier: "+user.getName()+", Tax: "+restaurant.getTax()+"\n";
                    text = text+ "Product Name, Product Price: "+"\n";
                    text = text + "--------------------------------------"+"\n";
                    for (Dish dish: selectedDish){
                        text = text + dish.getName()+", "+dish.getPrice()+"\n";
                        text = text + "--------------------------------------"+"\n";
                    }
                    text_area.setText(text);
                }
            });

            root.setOnTouchPressed(event -> {
                if (root.getStyleClass().contains("vbox_item_selected")){
                    root.getStyleClass().remove("vbox_item_selected");
                    int id = Integer.parseInt(((Label) root.getChildren().get(3)).getText());
                    selectedDish.remove(dishList.get(id));

                    text = "Cashier: "+user.getName()+", Tax: "+restaurant.getTax()+"\n";
                    text = text+ "Product Name, Product Price: "+"\n";
                    text = text + "--------------------------------------"+"\n";
                    for (Dish dish: selectedDish){
                        text = text + dish.getName()+", "+dish.getPrice()+"\n";
                        text = text + "--------------------------------------"+"\n";
                    }
                    text = text + "Tax: " + restaurant.getTax();
                    text_area.setText(text);
                }else {
                    root.getStyleClass().add("vbox_item_selected");
                    int id = Integer.parseInt(((Label) root.getChildren().get(3)).getText());
                    selectedDish.add(dishList.get(id));

                    text = "Cashier: "+user.getName()+", Tax: "+restaurant.getTax()+"\n";
                    text = text+ "Product Name, Product Price: "+"\n";
                    text = text + "--------------------------------------"+"\n";
                    for (Dish dish: selectedDish){
                        text = text + dish.getName()+", "+dish.getPrice()+"\n";
                        text = text + "--------------------------------------"+"\n";
                    }
                    text_area.setText(text);
                }
            });

            if (column == 3) {
                column = 0;
                row++;
            }

            menu_gridPane.add(root, column++, row);
            GridPane.setMargin(root, new Insets(42.5));
        }

        if (!islightMode) {
            handleChangeColorMode();
        }

    }

    @Override
    protected void handleChangeColorMode() {
        if (islightMode){
            sub_header.getStyleClass().remove("text_label_black");
            sub_header.getStyleClass().add("text_label_white");
            menu_gridPane.setStyle("-fx-background-color: #1E1F22");

            sub_header2.getStyleClass().remove("text_label_black");
            sub_header2.getStyleClass().add("text_label_white");

            menuButton.getStyleClass().remove("active_buttonWhite");
            menuButton.getStyleClass().add("active_buttonDark");

            products.forEach(vBox -> vBox.getChildren().forEach(x->{
                x.getStyleClass().remove("small_text_label_black");
                x.getStyleClass().add("small_text_label_white");
            }));

        }else {
            sub_header.getStyleClass().remove("text_label_white");
            sub_header.getStyleClass().add("text_label_black");
            menu_gridPane.setStyle("-fx-background-color: #ffffff");

            sub_header2.getStyleClass().remove("text_label_white");
            sub_header2.getStyleClass().add("text_label_black");

            menuButton.getStyleClass().remove("active_buttonDark");
            menuButton.getStyleClass().add("active_buttonWhite");

            products.forEach(vBox -> vBox.getChildren().forEach(x->{
                x.getStyleClass().remove("small_text_label_white");
                x.getStyleClass().add("small_text_label_black");
            }));
        }
        super.handleChangeColorMode();
    }

    @Override
    protected boolean shouldUseKeyboard() {
        return false;
    }

    @FXML
    public void handleMakeOrder(){}


}
