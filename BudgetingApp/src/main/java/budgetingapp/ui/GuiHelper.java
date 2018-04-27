/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetingapp.ui;

/**
 *
 * @author iskä
 */
public class GuiHelper {

    public GuiHelper() {
        
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

    public String getCategory(int category) {
        if (category == 1) {
            return "Living";
        }
        if (category == 2) {
            return "Food";
        }
        if (category == 3) {
            return "Goods";
        }
        if (category == 4) {
            return "Spare time";
        }
        return "";

    }

    public int checkMonth(String month) {
        if (month.contains("January")) {
            return 1;
        }
        if (month.contains("February")) {
            return 2;
        }
        if (month.contains("March")) {
            return 3;
        }
        if (month.contains("April")) {
            return 4;
        }
        if (month.contains("May")) {
            return 5;
        }
        if (month.contains("June")) {
            return 6;
        }
        if (month.contains("July")) {
            return 7;
        }
        if (month.contains("August")) {
            return 8;
        }
        if (month.contains("September")) {
            return 9;
        }
        if (month.contains("October")) {
            return 10;
        }
        if (month.contains("November")) {
            return 11;
        }
        return 12;
    }
    
    
}
