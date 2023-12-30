package controllers;

import controllers.fragments.MenuItemController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Dish;
import models.Order;
import models.Restaurant;
import models.User;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class AdminPageMenuController extends AdminPageControllerAbstract {

    @FXML
    private Label sub_header;
    @FXML
    private Label sub_header2;
    @FXML
    private GridPane menu_gridPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextArea text_area;
    @FXML
    private ChoiceBox sort_choiceBox;

    private List<Dish> dishList = new LinkedList<>();
    List<Dish> dishListByPopularity = new ArrayList<>();

    private List<VBox> products = new ArrayList<>();

    private List<Dish> selectedDish = new ArrayList<>();

    private List<String> types = new ArrayList<>();

    private String text;

    private Map<Dish, Integer> selectedDishCountMap = new LinkedHashMap<>();

    public void initializeWithData(User user, boolean islightMode) throws IOException {

        this.user = user;
        restaurant = Restaurant.retrieveRestaurant();

        scrollPane.addEventFilter(ScrollEvent.ANY, event -> {
            if (event.getEventType() == ScrollEvent.SCROLL) {
                double deltaY = event.getDeltaY();
                scrollPane.setVvalue(scrollPane.getVvalue() - deltaY / scrollPane.getHeight());
                event.consume();
            }
        });
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        dishList = Dish.retrieveAllDishes();
        dishListByPopularity = sortDishesByPopularity(dishList, Dish.retrieveDishesByPopularity());

        text = "Cashier: " + user.getName() + ", Tax: " + restaurant.getTax() + "\n";
        text = text + "Product Name, Product Price:"+"\n";
        text = text + "--------------------------------------" + "\n";
        text_area.setText(text);

        types.add("All");
        types.add("Popularity");
        types.addAll(Dish.getUniqueTypes(dishList));

        sort_choiceBox.getItems().addAll(FXCollections.observableArrayList(types));

        sort_choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            List<Dish> filteredDishes;
            if ("All".equals(newValue)) {
                filteredDishes = dishList;
            } else if ("Popularity".equals(newValue)) {
                filteredDishes = dishListByPopularity;
            } else {
                filteredDishes = dishList.stream().filter(dish -> dish.getType().equals(newValue)).collect(Collectors.toList());
            }

            products.clear();
            menu_gridPane.getChildren().clear();

            int column = 0;
            int row = 1;
            for (int i = 0; i < filteredDishes.size(); i++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml_files/fragments/menu-item.fxml"));
                VBox[] root = new VBox[1];
                try {
                    root[0] = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                MenuItemController controller = loader.getController();
                controller.setData(filteredDishes.get(i), i);

                products.add(root[0]);

                if (selectedDish.contains(filteredDishes.get(i))) {
                    root[0].getStyleClass().add("vbox_item_selected");
                }

                root[0].setOnMouseClicked(event -> {
                    if (!root[0].getStyleClass().contains("vbox_item_selected")) {
                        root[0].getStyleClass().add("vbox_item_selected");
                    }

                    int id = Integer.parseInt(((Label) root[0].getChildren().get(3)).getText());
                    Dish selected = filteredDishes.get(id);
                    selectedDish.add(selected);

                    selectedDishCountMap.put(selected, selectedDishCountMap.getOrDefault(selected, 0) + 1);

                    updateTextArea();
                });

                root[0].setOnTouchPressed(event -> {
                    if (!root[0].getStyleClass().contains("vbox_item_selected")) {
                        root[0].getStyleClass().add("vbox_item_selected");
                    }

                    int id = Integer.parseInt(((Label) root[0].getChildren().get(3)).getText());
                    Dish selected = filteredDishes.get(id);
                    selectedDish.add(selected);

                    selectedDishCountMap.put(selected, selectedDishCountMap.getOrDefault(selected, 0) + 1);

                    updateTextArea();
                });

                if (column == 3) {
                    column = 0;
                    row++;
                }

                menu_gridPane.add(root[0], column++, row);
                GridPane.setMargin(root[0], new Insets(42.5));

                if (!this.islightMode) {
                    root[0].getChildren().forEach(x -> {
                        x.getStyleClass().remove("small_text_label_black");
                        x.getStyleClass().add("small_text_label_white");
                    });
                }
            }
        });

        sort_choiceBox.getSelectionModel().select(0);

        if (!islightMode) {
            handleChangeColorMode();
        }
    }

    @Override
    protected void handleChangeColorMode() {
        if (islightMode){
            sub_header.getStyleClass().remove("text_label_black");
            sub_header.getStyleClass().add("text_label_white");

            scrollPane.getStyleClass().remove("myScrollPane_white");
            scrollPane.getStyleClass().add("myScrollPane_dark");

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

            scrollPane.getStyleClass().remove("myScrollPane_dark");
            scrollPane.getStyleClass().add("myScrollPane_white");

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
    public void handleMakeOrder(){
        openSubwindow(stage);
    }

    private void openSubwindow(Stage ownerStage) {
        Stage subwindow = new Stage();
        subwindow.setTitle("Subwindow");

        // Set the modality to APPLICATION_MODAL to block input to the owner stage
        subwindow.initModality(Modality.APPLICATION_MODAL);

        // Set the owner stage
        subwindow.initOwner(ownerStage);

        Button closeButton = new Button("Close Subwindow");
        closeButton.setOnAction(e -> subwindow.close());

        StackPane layout = new StackPane();
        layout.getChildren().add(closeButton);

        subwindow.setScene(new Scene(layout, 200, 150));
        subwindow.showAndWait(); // ShowAndWait makes the subwindow modal
    }

    public void registerOrder(String payment, String additionalInfo){
        Restaurant.retrieveToPrint(restaurant);
        String[] toPrint = restaurant.getToPrint().split(",");

        List<String> orderDetails = new ArrayList<>();
        orderDetails.add(restaurant.getName());
        orderDetails.add("Cheque: ");
        orderDetails.add("--------------------");

        orderDetails.add("Dishes: ");
        selectedDishCountMap.entrySet().forEach(x->{
            orderDetails.add(x.getKey().getName()+" x "+x.getValue());
            orderDetails.add("Price: "+x.getKey().getPrice());
        });
        orderDetails.add("--------------------");


        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double sum = selectedDishCountMap.entrySet().stream().mapToDouble(x->x.getKey().getPrice()*x.getValue()).sum();
        orderDetails.add("Total: " + decimalFormat.format(sum));
        orderDetails.add("--------------------");
        orderDetails.add("Tax: "+restaurant.getTax());
        orderDetails.add("--------------------");
        double sumTax = sum+((sum*restaurant.getTax())/100);
        orderDetails.add("To pay: " + decimalFormat.format(sumTax));
        orderDetails.add("--------------------");

        for (String s : toPrint) {
            switch (s) {
                case "Address":
                    orderDetails.add("Address:");
                    List<String> tempAddress = Arrays.stream(restaurant.getAddress().split("\n")).toList();
                    for (String part: tempAddress){
                        orderDetails.add(part);
                    }
                    break;
                case "Phone":
                    orderDetails.add("Phone number:");
                    orderDetails.add(restaurant.getPhone());
                    break;
                case "Email":
                    orderDetails.add("Email:");
                    orderDetails.add(restaurant.getEmail());
                    break;
                case "Additional info":
                    List<String> tempInfo = Arrays.stream(restaurant.getInfo().split("\n")).toList();
                    for (String part: tempInfo){
                        orderDetails.add(part);
                    }
                    break;
            }

        }

        orderDetails.add("--------------------");
        orderDetails.add("Cashier: "+user.getName());
        orderDetails.add("--------------------");

        Order order = new Order(Order.getNextOrderId(),"Card","In process");
        List<Dish> dishes = new ArrayList<>();
        dishes.addAll(selectedDish);
        order.setDishes(dishes);
        order.makeOrder();

        Order.getAllOrders().forEach(x->{
            x.setDishes(x.getDishesByOrderId());
            System.out.println(x);
        });

        createPdf(orderDetails, Integer.parseInt(restaurant.getPaperSize().split("mm")[0]));
        System.out.println(restaurant.getPaperSize().split("mm")[0]);
    }

    public  List<String> splitStrings(List<String> orderList, int maxSymbolsPerLine) {
        List<String> newList = new ArrayList<>();

        for (String string : orderList) {
            if (string.length() > maxSymbolsPerLine) {
                for (int i = 0; i < string.length(); i += maxSymbolsPerLine) {
                    int endIndex = Math.min(i + maxSymbolsPerLine, string.length());
                    String part = string.substring(i, endIndex);
                    newList.add(part);
                }
            } else {
                newList.add(string);
            }
        }

        return newList;
    }

    private void createPdf(List<String> orderDetails, int paperSize) {
        float lineHeight = 14;
        float paperWidthPoints = paperSize * 72f / 25.4f;  // Convert mm to points
        float AVERAGE_CHAR_WIDTH = 0.55f;
        float margin = 20;
        int fontSize = 10;

        float maxWidth = paperWidthPoints - 2 * margin;
        int maxSymbolsPerLine = (int) (maxWidth / (AVERAGE_CHAR_WIDTH * fontSize));

        orderDetails = splitStrings(orderDetails, maxSymbolsPerLine);

        float paperHeightPoints = orderDetails.size() * lineHeight + 1 * margin;

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(new PDRectangle(paperWidthPoints, paperHeightPoints));
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                PDType0Font font = PDType0Font.load(document, AdminPageMenuController.class.getResourceAsStream("/styles/fonts/DejaVuSans.ttf"));
                contentStream.setFont(font, fontSize);

                float yStart = paperHeightPoints - margin;
                float yPosition = yStart;

                for (String line : orderDetails) {

                    float textWidth = font.getStringWidth(line) / 1000 * fontSize;
                    float xPosition = (paperWidthPoints - textWidth) / 2;

                    while (line.length() > maxSymbolsPerLine) {
                        String part = line.substring(0, maxSymbolsPerLine);
                        line = line.substring(maxSymbolsPerLine);

                        contentStream.beginText();
                        contentStream.newLineAtOffset(xPosition, yPosition);
                        contentStream.showText(part);
                        contentStream.endText();

                        yPosition -= lineHeight;
                    }

                    contentStream.beginText();
                    contentStream.newLineAtOffset(xPosition, yPosition);
                    contentStream.showText(line);
                    contentStream.endText();
                    yPosition -= lineHeight;
                }
            }

            document.save("dynamic_order_document.pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void handleClearLast() {
        if (!selectedDish.isEmpty()) {
            Dish removedDish = selectedDish.remove(selectedDish.size() - 1);

            int count = selectedDishCountMap.get(removedDish);
            if (count > 1) {
                selectedDishCountMap.put(removedDish, count - 1);
            } else {
                selectedDishCountMap.remove(removedDish);
            }

            products.stream()
                    .filter(product -> ((Label) product.getChildren().get(0)).getText().equals(removedDish.getName()))
                    .findFirst()
                    .ifPresent(product -> {
                        if (!selectedDishCountMap.containsKey(removedDish)) {
                            product.getStyleClass().remove("vbox_item_selected");
                        }
                    });

            updateTextArea();
        }
    }

    private void updateTextArea() {
        text = "Cashier: "+user.getName()+", Tax: "+restaurant.getTax()+"\n";
        text = text+ "Product Name, Product Price: "+"\n";
        text = text + "--------------------------------------"+"\n";
        for (Dish dish: selectedDish){
            text = text + dish.getName()+", "+dish.getPrice()+"\n";
            text = text + "--------------------------------------"+"\n";
        }
        text_area.setText(text);
    }

    private List<Dish> sortDishesByPopularity(List<Dish> dishList, List<Dish> dishListByPopularity) {
        List<Integer> dishIdByPopularity = dishListByPopularity.stream()
                .map(Dish::getId)
                .collect(Collectors.toList());

        Comparator<Dish> popularityComparator = Comparator.comparingInt(dish -> dishIdByPopularity.indexOf(dish.getId()));
        return dishList.stream()
                .sorted(popularityComparator)
                .collect(Collectors.toList());
    }

}
