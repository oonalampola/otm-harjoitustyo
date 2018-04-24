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
import java.sql.SQLException;
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

        logoutButton.setOnAction(e -> {

            budService.signOut();
            primaryStage.setScene(gettingStarted);
        });
        deleteButton.setOnAction(e -> {
            try {
                budService.deleteEvents(signedInUser.getId());
            } catch (SQLException ex) {
                Logger.getLogger(BudgetingUi.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        clearBalance.setOnAction(e -> {
            try {
                budService.clearBalance();
                signedInAccount.setBalance(0);
            } catch (SQLException ex) {
                Logger.getLogger(BudgetingUi.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        newEventBut.setOnAction(e -> {

            addNewEvent(primaryStage);

        });
        mainPane.setTop(menuPane);
        pieChart = createChart();
        mainPane.setCenter(pieChart);
        mainPane.setBottom(bottomMenuPane);
        menuPane.getChildren().addAll(menuLabel, accountBalance, logoutButton, newEventBut);
        bottomMenuPane.getChildren().addAll(deleteButton, clearBalance);
        signedIn = new Scene(mainPane, 500, 300);
        //kirjautuminen
        VBox signInPane = new VBox(10);
        HBox inputPane = new HBox(10);

        signInPane.setPadding(new Insets(10));
        Label loginLabel = new Label("Username");
        TextField usernameInput = new TextField();

        signInPane.getChildren().addAll(loginLabel, usernameInput);
        Label signInMessage = new Label();

        Button signinButton = new Button("Sign in");
        Button createNewButton = new Button("Create new user");
        StackPane eventsPane = new StackPane();
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
                    pieChart = createChart();
                    mainPane.setCenter(pieChart);
                    //Tapahtumat
//                    ListView<Integer> listView = new ListView<>();
//                    listView.setPrefSize(200, 250);
//                    listView.setEditable(true);
//
//                    ObservableList<Integer> items = FXCollections.observableArrayList();
//
//                    signedInUser = budService.updateUserInfo(username);
//                    Account a = signedInUser.getAccount();
//                    eventsList = a.getEvents();
//                    System.out.println(eventsList);
//
//                    double amount;
//                    Event ev;
//                    for (int i = 0; i < eventsList.size(); i++) {
//
//                        ev = eventsList.get(i);
//                        amount = ev.getAmount();
//
//                        // items.add(amount);
//                    }
//
//                    listView.setItems(items);
//
//                    eventsPane.getChildren().add(listView);
//                    eventScroller.setContent(listView);
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

        Button addEvent = new Button("Add!");
        Label addingMessage = new Label();
        addEvent.setOnAction(e -> {

            int id = signedInUser.getId();
            boolean inOrPay = true;
            double amount = Double.parseDouble(amountTextField.getText());
            String category = categoryOptions.getValue().toString();
            System.out.println("Kategoriaksi valittu: " + category);
            int categoryCode = checkCategory(category);

            if (amount == 0) {
                addingMessage.setText("Amount can't be 0");
                addingMessage.setTextFill(Color.RED);
                amountTextField.clear();
            }

            Event event = new Event(amount, id);
            event.setCategory(categoryCode);
            try {
                if (budService.addEvent(event)) {
                    signedInUser = budService.updateUserInfo(signedInUser.getUsername());
                    accountBalance.setText("Balance: " + signedInUser.getAccountBalance());
                    primaryStage.setTitle("BudgetingApp");
                    pieChart = createChart();
                    mainPane.setCenter(pieChart);
                    primaryStage.setScene(signedIn);
                }

            } catch (SQLException ex) {
                Logger.getLogger(BudgetingUi.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        amountPane.setPadding(new Insets(10));
        amountPane.getChildren().addAll(amountLabel, amountTextField);
        buttonPane.getChildren().addAll(addEvent, addingMessage);
        newEventPane.getChildren().addAll(amountPane, categoryOptions, buttonPane);
        newEventScene = new Scene(newEventPane, 250, 200);
        primaryStage.setScene(newEventScene);

    }

    public int checkCategory(String category) {
        if (category.contains("living")) {
            return 1;
        }
        if (category.contains("food")) {
            return 2;
        }
        if (category.contains("goods")) {
            return 3;

        }
        if (category.contains("spare")) {
            return 4;
        }
        return 0;

    }

    public List getEvents(int id) {
        List<Event> ev = new ArrayList<>();
        ev = budService.getEvents();
        return ev;
    }
//    public PieChart updateChart(){
//    }

    public PieChart createChart() throws SQLException {
        PieChart empty = new PieChart();

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
                        new PieChart.Data("Living", living),
                        new PieChart.Data("Food", food),
                        new PieChart.Data("Goods", goods),
                        new PieChart.Data("Spare time", spareTime));
        // new PieChart.Data("Apples", 30));
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Consumption");

        return chart;

    }
}
