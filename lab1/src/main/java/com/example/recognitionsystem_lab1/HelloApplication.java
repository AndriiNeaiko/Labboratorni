package com.example.recognitionsystem_lab1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    AirplaneRecognitionSystem system = new AirplaneRecognitionSystem();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Система розпізнавання літаків");

        initializeData();

        // Головний контейнер
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Верхній заголовок
        Label title = new Label("Система розпізнавання літаків");
        title.setFont(new Font("Arial", 20));
        title.setTextFill(Color.DARKBLUE);
        root.setTop(title);
        BorderPane.setAlignment(title, Pos.CENTER);

        // Ліва панель з формою
        VBox formPane = new VBox(15);
        formPane.setPadding(new Insets(20));
        formPane.setStyle("-fx-background-color: #E8F0FE; -fx-border-color: #A2A9B8; -fx-border-radius: 10; -fx-background-radius: 10;");

        Label weightLabel = new Label("Вага літака (кг):");
        TextField weightInput = new TextField();

        Label speedLabel = new Label("Швидкість літака (км/год):");
        TextField speedInput = new TextField();

        Label wingspanLabel = new Label("Ширина крил (м):");
        TextField wingspanInput = new TextField();

        Label categoryLabel = new Label("Категорія літака:");
        TextField categoryInput = new TextField();

        Button classifyButton = new Button("Класифікувати");
        classifyButton.setFont(new Font("Arial", 14));
        classifyButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        Button addButton = new Button("Додати літак");
        addButton.setFont(new Font("Arial", 14));
        addButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");

        Label resultLabel = new Label();
        resultLabel.setWrapText(true);
        resultLabel.setStyle("-fx-text-fill: #333; -fx-padding: 10;");

        formPane.getChildren().addAll(
                weightLabel, weightInput,
                speedLabel, speedInput,
                wingspanLabel, wingspanInput,
                categoryLabel, categoryInput,
                classifyButton, addButton, resultLabel
        );

        root.setLeft(formPane);

        // Нижня панель з кнопками
        HBox bottomPane = new HBox(15);
        bottomPane.setPadding(new Insets(10));
        bottomPane.setAlignment(Pos.CENTER);

        Button showListButton = new Button("Показати список літаків");
        showListButton.setFont(new Font("Arial", 14));
        showListButton.setStyle("-fx-background-color: #FFC107; -fx-text-fill: black; -fx-font-weight: bold;");

        bottomPane.getChildren().add(showListButton);
        root.setBottom(bottomPane);

        // Події для кнопок
        classifyButton.setOnAction(e -> {
            try {
                double weight = Double.parseDouble(weightInput.getText());
                double speed = Double.parseDouble(speedInput.getText());
                double wingspan = Double.parseDouble(wingspanInput.getText());

                Airplane unknownAirplane = new Airplane(weight, speed, wingspan, "Unknown");

                String category = system.classifyAirplane(unknownAirplane);
                StringBuilder distanceInfo = new StringBuilder("Літак належить до класу: " + category + "\n");

                for (Airplane airplane : system.airplanes) {
                    double distance = unknownAirplane.distanceTo(airplane);
                    distanceInfo.append("Відстань до ").append(airplane.category)
                            .append(": ").append(String.format("%.2f", distance)).append(" од.\n");
                }

                resultLabel.setText(distanceInfo.toString());
            } catch (NumberFormatException ex) {
                resultLabel.setText("Помилка: введіть числові значення!");
            }
        });

        addButton.setOnAction(e -> {
            try {
                double weight = Double.parseDouble(weightInput.getText());
                double speed = Double.parseDouble(speedInput.getText());
                double wingspan = Double.parseDouble(wingspanInput.getText());
                String category = categoryInput.getText();

                Airplane newAirplane = new Airplane(weight, speed, wingspan, category);

                system.addAirplane(newAirplane);
                resultLabel.setText("Новий літак доданий у систему: " + category);
            } catch (NumberFormatException ex) {
                resultLabel.setText("Помилка: введіть числові значення!");
            }
        });

        showListButton.setOnAction(e -> {
            Stage listStage = new Stage();
            listStage.setTitle("Список літаків");

            TableView<Airplane> airplaneTable = new TableView<>();

            TableColumn<Airplane, Double> weightColumn = new TableColumn<>("Вага (кг)");
            weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));

            TableColumn<Airplane, Double> speedColumn = new TableColumn<>("Швидкість (км/год)");
            speedColumn.setCellValueFactory(new PropertyValueFactory<>("speed"));

            TableColumn<Airplane, Double> wingspanColumn = new TableColumn<>("Ширина крил (м)");
            wingspanColumn.setCellValueFactory(new PropertyValueFactory<>("wingspan"));

            TableColumn<Airplane, String> categoryColumn = new TableColumn<>("Категорія");
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

            airplaneTable.getColumns().addAll(weightColumn, speedColumn, wingspanColumn, categoryColumn);
            airplaneTable.getItems().addAll(system.airplanes);

            VBox vbox = new VBox(airplaneTable);
            Scene scene = new Scene(vbox, 400, 300);

            listStage.setScene(scene);
            listStage.show();
        });

        // Встановлення сцени
        Scene scene = new Scene(root, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeData() {
        system.addAirplane(new Airplane(15000, 600, 38, "Цивільний літак"));
        system.addAirplane(new Airplane(10000, 2000, 30, "Швидкісний літак"));
        system.addAirplane(new Airplane(5000, 3200, 25, "Винищувач-бомбардувальник"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}


