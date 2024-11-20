package com.example.vanderpolapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    private TextField paramBField;
    private LineChart<Number, Number> chartX;
    private Scene mainScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Рівняння Ейлера");

        // Лейбл для параметра b
        Label paramBLabel = new Label("Введіть параметр b:");
        paramBLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #333;");

        // Поле для введення параметра b
        paramBField = new TextField("0.01");
        paramBField.setPrefWidth(100);
        paramBField.setStyle("-fx-font-size: 14; -fx-padding: 5; -fx-background-color: #f4f4f4; -fx-border-color: #ddd;");

        // Кнопка для початку обчислень
        Button calculateButton = new Button("Розпочати обчислення");
        calculateButton.setStyle("-fx-font-size: 14; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10;");
        calculateButton.setOnAction(e -> calculate());

        // Ось для графіка
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Час");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("x");

        // Лінійний графік для змінної x
        chartX = new LineChart<>(xAxis, yAxis);
        chartX.setTitle("Графік змінної x(t)");
        chartX.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ddd;");

        // Розміщення елементів інтерфейсу
        VBox mainControls = new VBox(15, paramBLabel, paramBField, calculateButton);
        mainControls.setAlignment(Pos.CENTER);
        mainControls.setPadding(new Insets(20));

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(chartX);
        mainLayout.setBottom(mainControls);

        mainScene = new Scene(mainLayout, 900, 600);

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private void calculate() {
        double b;
        try {
            b = Double.parseDouble(paramBField.getText());
        } catch (NumberFormatException e) {
            paramBField.setText("Неправильний ввід");
            return;
        }

        double h = 0.005;  // Крок часу
        double x = 1.0, v = 0.0;  // Початкові значення
        int steps = 1000;  // Кількість кроків інтегрування

        XYChart.Series<Number, Number> seriesX = new XYChart.Series<>();
        seriesX.setName("x(t) при b = " + b);

        // Ітераційний процес із чисельного методу Ейлера
        for (int i = 0; i < steps; i++) {
            double t = i * h;
            double newX = x + h * v;
            double newV = v + h * (b * (1 - x * x) * v - x);

            seriesX.getData().add(new XYChart.Data<>(t, x));

            x = newX;
            v = newV;
        }

        chartX.getData().clear();
        chartX.getData().add(seriesX);
    }
}

