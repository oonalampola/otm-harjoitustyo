/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetingapp.ui;

import budgetingapp.dao.UserDao;
import budgetingapp.dao.AccountDao;

import budgetingapp.database.Database;
import budgetingapp.domain.BudgetingService;
import budgetingapp.domain.User;
import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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

    private Label menuLabel = new Label();
    private Label accountBalance = new Label();

    @Override
    public void init() throws Exception {

        File file = new File("db", "BudAppData.db");

        Database database = new Database("jdbc:sqlite:" + file.getAbsolutePath());

        UserDao userDao = new UserDao(database);
        AccountDao accountDao = new AccountDao(database);
        Scanner scanner = new Scanner(System.in);

        budService = new BudgetingService(userDao, accountDao);
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

        signinButton.setOnAction(e -> {
            String username = usernameInput.getText();
            menuLabel.setText("Welcome, " + username + "!");

            try {
                if (budService.signIn(username)) {
                    signInMessage.setText("");
                    primaryStage.setScene(signedIn);
                    User user = budService.getSignedIn();

                    usernameInput.setText("");
                    accountBalance.setText("Balance: " + user.getAccountBalance());
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
        ScrollPane eventScroller = new ScrollPane();
        BorderPane mainPane = new BorderPane();
        signedIn = new Scene(mainPane, 400, 300);

        HBox menuPane = new HBox(10);
        Button logoutButton = new Button("Sign out");
        menuPane.getChildren().addAll(menuLabel, accountBalance, logoutButton);
        logoutButton.setOnAction(e -> {

            budService.signOut();
            primaryStage.setScene(gettingStarted);
        });

        TextField events = new TextField();
        eventScroller.setContent(events);
        mainPane.setTop(menuPane);
        
        //uusi tapahtuma EI TOTEUTETTU
//        ObservableList<String> options
//                = FXCollections.observableArrayList(
//                        "Option 1",
//                        "Option 2",
//                        "Option 3"
//                );
//        ComboBox comboBox = new ComboBox(options);

        //new user
        VBox newUserPane = new VBox(10);

        HBox newUsernamePane = new HBox(10);
        HBox newNamePane = new HBox(10);

        Button createUserButton = new Button("Create user");
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
            
            if(name.length() < 5||username.length()< 5){
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
                } else {
                    creationMessage.setText("Username has to be unique");

                }
            } catch (SQLException ex) {
                Logger.getLogger(BudgetingUi.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        newUserPane.getChildren().addAll(creationMessage, newNamePane, newUsernamePane, createUserButton);
        createUserScene = new Scene(newUserPane, 400, 300);

        //primaryStage
        primaryStage.setTitle("BudgetingApp");
        primaryStage.setScene(gettingStarted);
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {

        launch(args);
    }

}
