package com.mthree.floorMastery.ui;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class UserIOImpl implements UserIO {
    public static Scanner sc = new Scanner(System.in);

    //This will get specific data types

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
        int usrResponse = 0;
        while (!validated) {
            if (!sc.hasNextInt()) {
                print("Wrong Type: Please enter an integer: ");
                sc.nextLine();
                continue;
            }
            validated = true;
            usrResponse = sc.nextInt();
            sc.nextLine();
        }
        return usrResponse;
    }

    public int readInt(String prompt, int min, int max) {
        System.out.print(prompt);
        boolean validated = false;
        int usrResponse = 0;
        while (!validated) {
            if (!sc.hasNextInt()) {
                print("Wrong Type: Please enter an integer with the range " + min + "-" + max);
                sc.nextLine();
                continue;
            }
            usrResponse = sc.nextInt();
            sc.nextLine();
            if (usrResponse < min || usrResponse > max) {
                print("Out of Range: Please enter an integer with the range " + min + "-" + max);
                continue;
            }
            validated = true;
        }
        return usrResponse;
    }

    @Override
    public LocalDate readDate(String prompt, String format) {
        LocalDate usrResponse = null;
        boolean validInput = false;
        while (!validInput) {
            try {
                print(prompt + " - " + format + ": ");
                usrResponse = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern(format));
                validInput = true;
            } catch (Exception e) {
                print("This is in an invalid format");
            }

        }
        return usrResponse;
    }


    @Override
    public BigDecimal readBigDecimal(String prompt, double minimumDouble, boolean allowBlank) {
        BigDecimal minBD = BigDecimal.valueOf(minimumDouble);
        while (true) {
            print(prompt + (allowBlank ? " (leave blank to keep current)" : "") + ": ");
            String input = sc.nextLine();

            if (allowBlank && input.isBlank()) {
                return null;
            }

            try {
                BigDecimal usrResponse = new BigDecimal(input);
                if (usrResponse.compareTo(minBD) < 0) {
                    print("This is an invalid number below the minimum of " + minimumDouble);
                    continue;
                }
                return usrResponse.setScale(2, RoundingMode.HALF_UP);
            } catch (NumberFormatException e) {
                print("Wrong Type: Please enter a positive number (rounded up at 2 decimal spaces)");
            }
        }
    }

    @Override
    public boolean readBoolean(String prompt) {
        String usrResponse;
        boolean usrResponseBoolean = true;
        boolean validInput = false;
        while (!validInput) {
            print(prompt);
            usrResponse = sc.nextLine();
            if (usrResponse.equalsIgnoreCase("no")) {
                usrResponseBoolean = false;
            } else if (usrResponse.equalsIgnoreCase("yes")){
                usrResponseBoolean = true;
            } else {
                print("Invalid input: Please enter 'yes' or 'no'");
                continue;
            }
            validInput = true;
        }

        return usrResponseBoolean;
    }

}
