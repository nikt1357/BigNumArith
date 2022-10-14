import java.io.*;
import java.util.*;

public class BigNumArithmetic {

    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                throw new FileNotFoundException("Incorrect number of arguments.");
            } else {
                /* Set up FileInputStream and Scanner to read from the file passed */
                FileInputStream file = new FileInputStream(args[0]);
                Scanner in = new Scanner(file);

                while (in.hasNext()) {
                    AStack stack = new AStack();
                    String s = in.nextLine();
                    String[] array = s.split("\\s+");
                    for (String value : array) {
                        if ((!value.equals("+")) && (!value.equals("*")) && (!value.equals("^"))) {
                            stack.push(value);
                        } else {
                            if (value.equals("+")) {
                                LList sum = new LList();
                                Object num1 = stack.pop();
                                Object num2 = stack.pop();
                                sum = addition(reverse(num1), reverse(num2), sum);
                                stack.push(sum);
                            } else if (value.equals("*")) {
                                Object num1 = stack.pop();
                                reverse(num1);
                                Object num2 = stack.pop();
                                reverse(num2);
                            } else if (value.equals("^")) {
                                Object num1 = stack.pop();
                                reverse(num1);
                                Object num2 = stack.pop();
                                reverse(num2);
                            }
                        }
                    }
                }
                in.close();

            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("File could not be opened.");
        }
    }

    public static LList reverse(Object obj) {
        LList list = new LList();
        String strPattern = "^0+(?!$)";
        String s = obj.toString();
        s = s.replaceAll(strPattern, "");
        char[] charArray = s.toCharArray();
        for (int i = charArray.length; i > 0; i--) {
          list.append(charArray[i - 1]);
        }
        return list;
    }

    public static LList addition(LList num1, LList num2, LList sum) {
        if (num1.isEmpty() && num2.isEmpty()) {
            return sum;
        } else if (!num1.isEmpty() && num2.isEmpty()) {
            int carry = 0;
            sum.moveToStart();
            try {
                if (!sum.isAtEnd()) {
                    carry = (int) sum.getValue();
                }
            } catch (NoSuchElementException e) {
                System.out.println(e.getMessage());
            }
            num1.moveToStart();
            sum.insert(num1.getValue());
            return sum;
        } else if (num1.isEmpty() && !num2.isEmpty()) {
            int carry = 0;
            sum.moveToStart();
            try {
                if (!sum.isAtEnd()) {
                    carry = (int) sum.getValue();
                }
            } catch (NoSuchElementException e) {
                System.out.println(e.getMessage());
            }
            num2.moveToStart();
            sum.insert(num2.getValue());
            return sum;
        } else {
            num1.moveToStart();
            num2.moveToStart();
            int digitSum = (Character.getNumericValue((Character) num1.getValue())) + (Character.getNumericValue((Character) num2.getValue()));
            num1.remove();
            num2.remove();
            if (digitSum > 9) {
                int ones = digitSum - 10;
                int carry = 0;
                sum.moveToStart();
                try {
                    if (!sum.isAtEnd()) {
                        carry = (int) sum.getValue();
                    }
                } catch (NoSuchElementException e) {
                    System.out.println(e.getMessage());
                }
                sum.insert(ones + carry);
                sum.moveToStart();
                sum.insert(1);
                sum = addition(num1, num2, sum);
            } else {
                int carry = 0;
                sum.moveToStart();
                try {
                    if (!sum.isAtEnd()) {
                        carry = (int) sum.getValue();
                    }
                } catch (NoSuchElementException e) {
                    System.out.println(e.getMessage());
                }
                int carrySum = digitSum + carry;
                if (carrySum > 9) {
                    sum.moveToStart();
                    sum.insert(1);
                } else {
                    sum.insert(digitSum + carry);
                }
                sum = addition(num1, num2, sum);
            }
        }
        return sum;
    }

}
