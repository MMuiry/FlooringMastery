package com.mthree.floorMastery.ui;
import java.util.Scanner;

public class UserIOImpl implements UserIO {
    public static Scanner sc = new Scanner(System.in);

    public void print(String prompt) {
        System.out.println(prompt);
    }

    public String readString(String prompt) {
        System.out.print(prompt);
        String usrResponse = sc.nextLine();
        return usrResponse;
    }

    public int readInt(String prompt) {
        System.out.print(prompt);
        boolean validated = false;
        int userInput = 0;
        while (!validated) {
            if (!sc.hasNextInt()) {
                print("Wrong Type: Please enter an integer: ");
                sc.nextLine();
                continue;
            }
            validated = true;
            userInput = sc.nextInt();
            sc.nextLine();
        }
        return userInput;
    }

    public int readInt(String prompt, int min, int max) {
        System.out.print(prompt);
        boolean validated = false;
        int userNum = 0;
        while (!validated) {
            if (!sc.hasNextInt()) {
                print("Wrong Type: Please enter an integer with the range " + min + "-" + max);
                sc.nextLine();
                continue;
            }
            userNum = sc.nextInt();
            sc.nextLine();
            if (userNum < min || userNum > max) {
                print("Out of Range: Please enter an integer with the range " + min + "-" + max);
                continue;
            }
            validated = true;
        }
        return userNum;
    }
}
