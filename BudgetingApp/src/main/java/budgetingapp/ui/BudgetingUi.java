/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetingapp.ui;

import budgetingapp.dao.UserDao;
import budgetingapp.dao.AccountDao;

import budgetingapp.database.Database;
import budgetingapp.domain.Account;
import budgetingapp.domain.BudgetingService;
import budgetingapp.domain.Event;
import budgetingapp.domain.User;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author oona
 */
public class BudgetingUi extends Application {

    private BudgetingService budService;

    private Scene gettingStarted;
    private Scene createUserScene;
    private Scene signedIn;
    private Scene newEventScene;

    private Label menuLabel = new Label();
    private Label accountBalance = new Label();
    private List<Event> eventsList;
    private User signedInUser;
    private Account signedInAccount;
    private PieChart pieChart;
    private BorderPane mainPane;
    private ScrollPane eventScroller;
    private GridPane eventsAndPiePane;
    private ListView<HBox> listView;
    ObservableList<HBox> items;
    private GuiHelper guiHelper;

    @Override
    public void init() throws Exception {

        File file = new File("db", "budappdata.db");

        Database database = new Database("jdbc:sqlite:" + file.getAbsolutePath());

        UserDao userDao = new UserDao(database);
        AccountDao accountDao = new AccountDao(database);

        budService = new BudgetingService(userDao, accountDao);
        eventsList = new ArrayList<>();
        signedInUser = null;
        signedInAccount = null;
        pieChart = null;
        mainPane = new BorderPane();
        eventScroller = new ScrollPane();
        eventsAndPiePane = new GridPane();
        listView = new ListView<>();
        items = FXCollections.observableArrayList();
        guiHelper = new GuiHelper();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //signedInScene

        HBox menuPane = new HBox(10);
        HBox bottomMenuPane = new HBox(10);

        Button logoutButton = new Button("Sign out");
        Button newEventBut = new Button("Add new event");
        
        //Toimivat, mutta eivät vielä päivitä suoraan
        Button deleteButton = new Button("Delete all events");
        Button clearBalance = new Button("Clear balance");

        //Sign out
        logoutButton.setOnAction(e -> {
            eventsAndPiePane.getChildren().removeAll(pieChart, eventScroller);
            pieChart = null;
            items.clear();
            eventsList.clear();
            budService.signOut();
            primaryStage.setScene(gettingStarted);
        });
        deleteButton.setOnAction(e -> {
            try {
                budService.deleteEvents(signedInUser.getId());
                pieChart = createChart();
                eventsAndPiePane.getChildren().remove(pieChart);
                eventsAndPiePane.add(pieChart, 0, 0);

                createEventScroller();
            } catch (SQLException ex) {
                Logger.getLogger(BudgetingUi.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        clearBalance.setOnAction(e -> {
            try {
                budService.clearBalance();
                signedInAccount.setBalance(0);
                accountBalance.setText("Balance: " + signedInAccount.getBalance());

            } catch (SQLException ex) {
                Logger.getLogger(BudgetingUi.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        newEventBut.setOnAction(e -> {

            addNewEvent(primaryStage);

        });
        mainPane.setTop(menuPane);

        mainPane.setBottom(bottomMenuPane);
        menuPane.getChildren().addAll(menuLabel, accountBalance, newEventBut, deleteButton, clearBalance, logoutButton);

        Label chooseTime = new Label("Choose time");
        Button showThisMonth = new Button("Show");
        Button showAllEvents = new Button("Show all");
        ObservableList<String> monthOptions
                = FXCollections.observableArrayList(
                        "January",
                        "February",
                        "March",
                        "April",
                        "May",
                        "June",
                        "July",
                        "August",
                        "September",
                        "November",
                        "December"
                );

        ComboBox months = new ComboBox(monthOptions);

        ObservableList<Integer> yearOptions
                = FXCollections.observableArrayList(
                        2016,
                        2017,
                        2018
                );
        ComboBox years = new ComboBox(yearOptions);
        bottomMenuPane.getChildren().addAll(chooseTime, months, years, showThisMonth, showAllEvents);

        signedIn = new Scene(mainPane, 700, 300);
        showAllEvents.setOnAction(e-> {
            eventsAndPiePane.getChildren().remove(pieChart);
            try {
                pieChart = createChart();
            } catch (SQLException ex) {
                Logger.getLogger(BudgetingUi.class.getName()).log(Level.SEVERE, null, ex);
            }
            eventsAndPiePane.getChildren().add(pieChart);
            try {
                createEventScroller();
            } catch (SQLException ex) {
                Logger.getLogger(BudgetingUi.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
        showThisMonth.setOnAction(e -> {
            eventsAndPiePane.getChildren().removeAll(pieChart, eventScroller);

            try {
                pieChart = createMonthlyChart(signedInUser.getId(), months.getValue().toString(), Integer.parseInt(years.getValue().toString()));
                createMonthlyScroller(signedInUser.getId(), months.getValue().toString(), Integer.parseInt(years.getValue().toString()));
            } catch (SQLException ex) {
                Logger.getLogger(BudgetingUi.class.getName()).log(Level.SEVERE, null, ex);
            }
            eventsAndPiePane.add(pieChart, 0, 0);
            eventsAndPiePane.add(eventScroller, 1, 0);
        });

        //kirjautuminen
        VBox signInPane = new VBox(10);
        HBox inputPane = new HBox(10);

        eventScroller.setMaxWidth(350);
        eventScroller.setMinWidth(350);

        signInPane.setPadding(new Insets(10));
        Label loginLabel = new Label("Username");
        TextField usernameInput = new TextField();

        signInPane.getChildren().addAll(loginLabel, usernameInput);
        Label signInMessage = new Label();

        Button signinButton = new Button("Sign in");
        Button createNewButton = new Button("Create new user");
        StackPane eventsPane = new StackPane();

        //Sign In
        signinButton.setOnAction(e -> {
            String username = usernameInput.getText();
            menuLabel.setText("Welcome, " + username + "!");

            try {
                if (budService.signIn(username)) {
                    signInMessage.setText("");
                    primaryStage.setScene(signedIn);

                    signedInUser = budService.getSignedIn();
                    signedInAccount = signedInUser.getAccount();

                    menuLabel.setText("Welcome, " + signedInUser.getName() + "!");
                    primaryStage.setTitle("BudgetingApp");
                    usernameInput.setText("");
                    accountBalance.setText("Balance: " + signedInUser.getAccountBalance());
                    if (pieChart == null) {
                        pieChart = createChart();
                    }
                    mainPane.setCenter(eventsAndPiePane);
                    eventsAndPiePane.add(pieChart, 0, 0);
                    eventsAndPiePane.add(eventScroller, 1, 0);

//Tapahtumat 
                    listView.setPrefSize(350, 250);
                    listView.setEditable(true);

                    signedInUser = budService.updateUserInfo(username);
                    Account a = signedInUser.getAccount();
                    eventsList = budService.getEvents(signedInUser.getId());
                    System.out.println(eventsList);

                    double amount;
                    Event ev;
                    for (int i = 0; i < eventsList.size(); i++) {
                        Event event = eventsList.get(i);
                        items.add(createEventElement(event));
                    }
                    listView.getItems().clear();
                    listView.setItems(items);

                    eventScroller.setContent(listView);

                } else {
                    signInMessage.setText("Username not found");
                    signInMessage.setTextFill(Color.RED);
                }
            } catch (SQLException ex) {
                Logger.getLogger(BudgetingUi.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        createNewButton.setOnAction(e -> {
            usernameInput.setText("");
            primaryStage.setTitle("Create new user");

            primaryStage.setScene(createUserScene);
        });
        signInPane.getChildren().addAll(signInMessage, inputPane, signinButton, createNewButton);
        gettingStarted = new Scene(signInPane, 400, 300);

        //new user
        VBox newUserPane = new VBox(10);

        HBox newUsernamePane = new HBox(10);
        HBox newNamePane = new HBox(10);

        Button createUserButton = new Button("Create user");
        Button goBackToStart = new Button("Back");
        Label creationMessage = new Label();
        Label nameLabel = new Label("Name: ");
        TextField nameInput = new TextField();
        Label newUsernameLabel = new Label("Username: ");
        TextField newUserInput = new TextField();

        newUsernamePane.setPadding(new Insets(10));
        newNamePane.setPadding(new Insets(10));

        newNamePane.getChildren().addAll(nameLabel, nameInput);
        newUsernamePane.getChildren().addAll(newUsernameLabel, newUserInput);

        createUserButton.setOnAction(e -> {
            String name = nameInput.getText();
            String username = newUserInput.getText();

            if (name.length() < 4 || username.length() < 4) {
                creationMessage.setText("Name/Username too short (min 4)");
                creationMessage.setTextFill(Color.RED);
                primaryStage.setScene(createUserScene);
                return;
            }

            try {
                if (budService.createUser(name, username)) {
                    signInMessage.setText("New user created succesfully, you can now sign in");
                    signInMessage.setTextFill(Color.GREEN);
                    primaryStage.setTitle("BudgetingApp");

                    primaryStage.setScene(gettingStarted);
                    System.out.println("Käyttäjä luotu!!!");
                } else {
                    creationMessage.setText("Username has to be unique");

                }
            } catch (SQLException ex) {
                Logger.getLogger(BudgetingUi.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        goBackToStart.setOnAction(e -> {
            primaryStage.setScene(gettingStarted);
        });
        newUserPane.getChildren().addAll(creationMessage, newNamePane, newUsernamePane, createUserButton, goBackToStart);
        createUserScene = new Scene(newUserPane, 400, 300);

        //primaryStage
        primaryStage.setTitle("BudgetingApp");
        primaryStage.setScene(gettingStarted);
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {

        launch(args);
    }

    public void addNewEvent(Stage primaryStage) {
        primaryStage.setTitle("New Event");

        VBox newEventPane = new VBox(10);
        HBox amountPane = new HBox(10);
        HBox DateOptions = new HBox(10);
        HBox buttonPane = new HBox(10);

        //Kategoriaa varten
        ObservableList<String> options
                = FXCollections.observableArrayList(
                        "Cost of living",
                        "Cost of food",
                        "Cost of goods",
                        "Cost of spare time",
                        "Income"
                );
        ComboBox categoryOptions = new ComboBox(options);

        categoryOptions.setPromptText("Choose category");
        categoryOptions.setEditable(true);
        Label amountLabel = new Label("Amount: ");
        TextField amountTextField = new TextField();

        ObservableList<Integer> yearOptions
                = FXCollections.observableArrayList(
                        2016,
                        2017,
                        2018
                );
        ComboBox years = new ComboBox(yearOptions);

        ObservableList<Integer> monthOptions
                = FXCollections.observableArrayList();

        for (int i = 1; i < 13; i++) {
            monthOptions.add(i);
        }

        ComboBox months = new ComboBox(monthOptions);

//        ObservableList<String> dayOptions
//                = FXCollections.observableArrayList();
//        for (int i = 1; i < 32; i++) {
//            dayOptions.add(i + "");
//        }
//
//        ComboBox days = new ComboBox(dayOptions);
        years.setEditable(true);
        months.setEditable(true);
//        days.setEditable(true);

        years.setPromptText("yyyy");
        months.setPromptText("mm");
//        days.setPromptText("dd");

        DateOptions.getChildren().addAll(months, years);

        Button addEvent = new Button("Add!");
        Label addingMessage = new Label();
        addEvent.setOnAction(e -> {

            int id = signedInUser.getId();
            double amount = Double.parseDouble(amountTextField.getText());
            String category = categoryOptions.getValue().toString();
            int year = Integer.parseInt(years.getValue().toString());
            int month = Integer.parseInt(months.getValue().toString());
//            int day = Integer.parseInt(days.getValue().toString());

            System.out.println("Kategoriaksi valittu: " + category);
            int categoryCode = guiHelper.checkCategory(category);

            if (amount == 0) {
                addingMessage.setText("Amount can't be 0");
                addingMessage.setTextFill(Color.RED);
                amountTextField.clear();
            }
//            Date date = new Date(year, month, day);

            Event event = new Event(amount, id);
            event.setTime(month, year);
            event.setCategory(categoryCode);

            try {
                if (budService.addEvent(event)) {
                    signedInUser = budService.updateUserInfo(signedInUser.getUsername());
                    accountBalance.setText("Balance: " + signedInUser.getAccountBalance());
                    primaryStage.setTitle("BudgetingApp");
                    createEventScroller();
                    pieChart = createChart();
                    eventsAndPiePane.getChildren().remove(pieChart);
                    eventsAndPiePane.add(pieChart, 0, 0);
                    primaryStage.setScene(signedIn);
                }

            } catch (SQLException ex) {
                Logger.getLogger(BudgetingUi.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        amountPane.setPadding(new Insets(10));
        amountPane.getChildren().addAll(amountLabel, amountTextField);
        buttonPane.getChildren().addAll(addEvent, addingMessage);
        newEventPane.getChildren().addAll(amountPane, DateOptions, categoryOptions, buttonPane);
        newEventScene = new Scene(newEventPane, 250, 200);
        primaryStage.setScene(newEventScene);

    }

//    public PieChart updateChart(){
//    }
    public void createMonthlyScroller(int id, String month, int year) throws SQLException {
        int monthNum = guiHelper.checkMonth(month);
        List<Event> list = budService.getMonthlyEvents(id, monthNum, year);
        listView.getItems().clear();

        listView.setPrefSize(350, 250);
        listView.setEditable(true);

        Account a = signedInUser.getAccount();
        System.out.println(eventsList);

        double amount;
        Event ev;
        for (int i = 0; i < list.size(); i++) {
            Event event = list.get(i);
            items.add(createEventElement(event));
        }
        listView.setItems(items);
        eventScroller.setContent(listView);
    }

    public void createEventScroller() throws SQLException {
        listView.getItems().clear();

        listView.setPrefSize(350, 250);
        listView.setEditable(true);

        signedInUser = budService.updateUserInfo(signedInUser.getUsername());
        Account a = signedInUser.getAccount();
        eventsList = budService.getEvents(signedInUser.getId());
        System.out.println(eventsList);

        double amount;
        Event ev;
        for (int i = 0; i < eventsList.size(); i++) {
            Event event = eventsList.get(i);
            items.add(createEventElement(event));
        }
        listView.setItems(items);
    }

    public PieChart createMonthlyChart(int id, String month, int year) throws SQLException {

        PieChart empty = new PieChart();

        if (signedInUser == null) {
            return empty;
        }
        int monthNum = guiHelper.checkMonth(month);

        System.out.println("id, kuukausi ja vuosi: " + id + " " + month + " " + monthNum + " " + year);
        List<Double> list = budService.getMonthlyCategoryAmounts(signedInUser.getId(), monthNum, year);
        System.out.println(list + "kuukauden tapahtumat");
        double living = list.get(0);
        double food = list.get(1);
        double goods = list.get(2);
        double spareTime = list.get(3);

        int all = list.size();

        if (all == 0) {
            return empty;
        }

        ObservableList<PieChart.Data> pieChartData
                = FXCollections.observableArrayList(
                        new PieChart.Data("Living", living),
                        new PieChart.Data("Food", food),
                        new PieChart.Data("Goods", goods),
                        new PieChart.Data("Spare time", spareTime));
        // new PieChart.Data("Apples", 30));

        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Consumption in " + month + " " + year);

        return chart;

    }

    public PieChart createChart() throws SQLException {
        PieChart empty = new PieChart();
        eventsAndPiePane.getChildren().remove(pieChart);
        if (signedInUser == null) {
            return empty;
        }
        List<Double> list = budService.getCategoryAmounts(signedInAccount);

        double living = list.get(0);
        double food = list.get(1);
        double goods = list.get(2);
        double spareTime = list.get(3);

        int all = list.size();

        if (all == 0) {
            return empty;
        }

        ObservableList<PieChart.Data> pieChartData
                = FXCollections.observableArrayList(
                        new PieChart.Data("Living, " + living + " €", living),
                        new PieChart.Data("Food, " + food + " €", food),
                        new PieChart.Data("Goods, " + goods + " €", goods),
                        new PieChart.Data("Spare time, " + spareTime + " €", spareTime));
        // new PieChart.Data("Apples", 30));
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Consumption");

        return chart;

    }

    public HBox createEventElement(Event e) {
        HBox element = new HBox();
        element.setSpacing(10);

        String category = guiHelper.getCategory(e.getCategory());

        Label amountLabel = new Label(e.getAmount() + "");
        Label timeLabel = new Label(e.getMonth() + "/" + e.getYear());
        Label categoryLabel = new Label(category);

        if (e.getCategory() != 0) {
            amountLabel.setTextFill(Color.RED);
        } else {
            amountLabel.setTextFill(Color.GREEN);

        }

        element.getChildren().add(amountLabel);
        element.getChildren().add(timeLabel);
        element.getChildren().add(categoryLabel);

        return element;
    }
}
