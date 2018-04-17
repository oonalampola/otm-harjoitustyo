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

    @Override
    public void init() throws Exception {

        File file = new File("db", "budappdata.db");

        Database database = new Database("jdbc:sqlite:" + file.getAbsolutePath());

        UserDao userDao = new UserDao(database);
        AccountDao accountDao = new AccountDao(database);

        budService = new BudgetingService(userDao, accountDao);
        eventsList = new ArrayList<>();

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

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
        ScrollPane eventScroller = new ScrollPane();
        signinButton.setOnAction(e -> {
            String username = usernameInput.getText();
            menuLabel.setText("Welcome, " + username + "!");

            try {
                if (budService.signIn(username)) {
                    signInMessage.setText("");
                    primaryStage.setScene(signedIn);
                    User user = budService.getSignedIn();
                    menuLabel.setText("Welcome, " + user.getName() + "!");

                    usernameInput.setText("");
                    accountBalance.setText("Balance: " + user.getAccountBalance());

                    //Tapahtumat
                    ListView<Integer> listView = new ListView<>();
                    listView.setPrefSize(200, 250);
                    listView.setEditable(true);

                    ObservableList<Integer> items = FXCollections.observableArrayList();
                    User u = budService.getSignedIn();
                    Account a = u.getAccount();
                    eventsList = a.getEvents();
                    System.out.println(eventsList);

                    int amount;
                    Event ev;
                    for (int i = 0; i < eventsList.size(); i++) {

                        ev = eventsList.get(i);
                        amount = ev.getAmount();

                        items.add(amount);
                    }

                    listView.setItems(items);

                    eventsPane.getChildren().add(listView);
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
            primaryStage.setScene(createUserScene);
        });
        signInPane.getChildren().addAll(signInMessage, inputPane, signinButton, createNewButton);
        gettingStarted = new Scene(signInPane, 400, 300);

        //signedInScene
        BorderPane mainPane = new BorderPane();

        HBox menuPane = new HBox(10);
        Button logoutButton = new Button("Sign out");

        Button newEventBut = new Button("Add new event");

        mainPane.setTop(menuPane);
        mainPane.setCenter(eventScroller);
        menuPane.getChildren().addAll(menuLabel, accountBalance, logoutButton, newEventBut);
        signedIn = new Scene(mainPane, 400, 300);
        logoutButton.setOnAction(e -> {

            budService.signOut();
            primaryStage.setScene(gettingStarted);
        });

        newEventBut.setOnAction(e -> {

            addNewEvent(primaryStage);

        });

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

        Label amountLabel = new Label("Amount: ");
        TextField amountTextField = new TextField();

        User user = budService.getSignedIn();

        Button addEvent = new Button("Add!");
        Label addingMessage = new Label();
        addEvent.setOnAction(e -> {

            int id = user.getId();
            boolean inOrPay = true;
            int amount = Integer.parseInt(amountTextField.getText());

            if (amount == 0) {
                addingMessage.setText("Amount cant be 0");
                addingMessage.setTextFill(Color.RED);
                amountTextField.clear();
            }
            if (amount < 0) {
                inOrPay = false;
            }

            Event event = new Event(amount, inOrPay, id);

            try {
                if(budService.addEvent(event)){
                    primaryStage.setScene(signedIn);
                }

            } catch (SQLException ex) {
                Logger.getLogger(BudgetingUi.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        amountPane.setPadding(new Insets(10));
        amountPane.getChildren().addAll(amountLabel, amountTextField);
        buttonPane.getChildren().addAll(addEvent, addingMessage);
        newEventPane.getChildren().addAll(amountPane, buttonPane);
        newEventScene = new Scene(newEventPane, 250, 200);
        primaryStage.setScene(newEventScene);

        //Kategoriaa varten
//        ObservableList<String> options
//                = FXCollections.observableArrayList(
//                        "Option 1",
//                        "Option 2",
//                        "Option 3"
//                );
//        ComboBox comboBox = new ComboBox(options);
    }

}
