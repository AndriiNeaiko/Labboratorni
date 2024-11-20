package com.example.vanderpoloscillatorapp;

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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    private static final double TIME_STEP = 0.01; // Крок часу
    private static final double T_MAX = 100.0;    // Максимальний час

    private LineChart<Number, Number> chart;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Ван дер Пол");

        // Ось Х для часу
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Час");

        // Ось Y для значення змінної x
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("x");

        // Лінійний графік для відображення коливань
        chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Коливання х у часі");

        // Поле для вводу параметра μ
        Label muLabel = new Label("Введіть параметр μ:");
        muLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #4CAF50;");

        TextField muField = new TextField("1.0");
        muField.setPrefWidth(100);
        muField.setStyle("-fx-font-size: 14; -fx-padding: 5; -fx-background-color: #f0f0f0; -fx-border-color: #4CAF50; -fx-border-radius: 5;");

        // Кнопка для запуску симуляції
        Button startButton = new Button("Почати симуляцію");
        startButton.setStyle("-fx-font-size: 14; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10; -fx-border-radius: 5;");
        startButton.setOnAction(event -> {
            try {
                double mu = Double.parseDouble(muField.getText());
                startSimulation(mu);
            } catch (NumberFormatException e) {
                muField.setText("Невірний вхід");
            }
        });

        // Верхня панель з полем вводу і кнопкою
        HBox topControls = new HBox(10, muLabel, muField, startButton);
        topControls.setAlignment(Pos.CENTER);
        topControls.setPadding(new Insets(10));

        // Основна розмітка
        BorderPane layout = new BorderPane();
        layout.setTop(topControls);
        layout.setCenter(chart);
        BorderPane.setMargin(chart, new Insets(10));

        // Створення сцени і її відображення
        Scene scene = new Scene(layout, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startSimulation(double mu) {
        chart.getData().clear(); // Очищення графіку перед новим запуском

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Коливання x");

        double x = 1.0; // Початкове значення x
        double y = 0.0; // Початкове значення y
        double prevMinX = Double.MAX_VALUE;
        double lastMinTime = 0;
        boolean foundFirstMinimum = false;
        double period = 0;

        for (double t = 0; t < T_MAX; t += TIME_STEP) {
            // Додавання значень до серії для побудови графіку
            series.getData().add(new XYChart.Data<>(t, x));

            // Метод трапецій для обчислення нового значення x і y
            double[] newValues = trapezoidalStep(x, y, mu, TIME_STEP);
            x = newValues[0];
            y = newValues[1];

            // Виявлення мінімумів для визначення періоду
            if (x < prevMinX) {
                prevMinX = x;
                lastMinTime = t;
                foundFirstMinimum = true;
            } else if (foundFirstMinimum && x > prevMinX + 0.01) {
                period = t - lastMinTime;
                System.out.printf("Мінімум знайдено на t=%.2f, період коливань: %.2f\n", t, period);
                break;
            }
        }

        chart.getData().add(series);
    }

    // Метод трапецій для обчислення нового значення x і y
    private double[] trapezoidalStep(double x, double y, double mu, double h) {
        // Поточні похідні
        double dxdt1 = y;
        double dydt1 = mu * (1 - x * x) * y - x;

        // Попереднє обчислення нових значень
        double xTemp = x + h * dxdt1;
        double yTemp = y + h * dydt1;

        // Наступні похідні
        double dxdt2 = yTemp;
        double dydt2 = mu * (1 - xTemp * xTemp) * yTemp - xTemp;

        // Нові значення x і y за методом трапецій
        double newX = x + (h / 2) * (dxdt1 + dxdt2);
        double newY = y + (h / 2) * (dydt1 + dydt2);

        return new double[]{newX, newY};
    }
}
