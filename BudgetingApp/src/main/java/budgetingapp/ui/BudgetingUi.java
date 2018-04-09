/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetingapp.ui;

import budgetingapp.dao.UserDao;
import budgetingapp.database.Database;
import budgetingapp.domain.BudgetingService;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Scanner;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author oona
 */
public class BudgetingUi {

//    public void init() throws Exception {
//
//        File file = new File("db", "BudAppData.db");
//        
//        Database database = new Database("jdbc:sqlite:" + file.getAbsolutePath());
// 
//        UserDao userDao = new UserDao(database);
//
//        BudgetingService budService = new BudgetingService(userDao, database);
//    }
    public static void main(String[] args) throws Exception {

        File file = new File("db", "BudAppData.db");

        Database database = new Database("jdbc:sqlite:" + file.getAbsolutePath());
        Scanner scanner = new Scanner(System.in);
        UserDao userDao = new UserDao(database);

        BudgetingService budService = new BudgetingService(userDao, scanner);
        
        budService.start();
    }
}
