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
                    String[] AList = s.split("\\s+");
                    for (String item : AList) {
                        String strPattern = "^0+(?!$)";
                        item = item.replaceAll(strPattern, "");
                        System.out.print(item + " ");
                    }
                    for (String value : AList) {
                        if ((!value.equals("+")) && (!value.equals("*")) && (!value.equals("^"))) {
                            stack.push(value);
                        } else {
                            if (value.equals("+")) {
                                LList sum = new LList();
                                Object num1 = stack.pop();
                                Object num2 = stack.pop();
                                sum = addition(reverse(num1), reverse(num2), sum);
                                String sumString = "";
                                sum.moveToStart();
                                for (int i = 0; i < sum.length(); i++) {
                                    sumString = sumString + sum.getValue();
                                    sum.next();
                                }
                                String strPattern = "^0+(?!$)";
                                sumString = sumString.replaceAll(strPattern, "");
                                stack.push(sumString);
                            //} else if (value.equals("*")) {

                            //} else {

                            }
                        }
                    }
                    System.out.println("= " + stack.pop());
                }
                in.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("File could not be opened.");
        }
    }

    public static LList addition(LList num1, LList num2, LList sum) {
        if (num1.isEmpty() && num2.isEmpty()) {
            return sum;
        } else if (!num1.isEmpty() && num2.isEmpty()) {
            int carry = getRemainder(sum);
            num1.moveToStart();
            int carrySum = Character.getNumericValue((Character) num1.getValue()) + carry;
            num1.remove();
            sum.moveToStart();
            if (carrySum > 9) {
                sum.insert(carrySum - 10);
                sum.insert(1);
            } else {
                sum.insert(carrySum);
                sum.insert(0);
            }
            sum = addition(num1, num2, sum);
        } else if (num1.isEmpty() && !num2.isEmpty()) {
            int carry = getRemainder(sum);
            num2.moveToStart();
            int carrySum = Character.getNumericValue((Character) num2.getValue()) + carry;
            num2.remove();
            sum.moveToStart();
            if (carrySum > 9) {
                sum.insert(carrySum - 10);
                sum.moveToStart();
                sum.insert(1);
            } else {
                sum.insert(carrySum);
                sum.moveToStart();
                sum.insert(0);
            }
            sum = addition(num1, num2, sum);
        } else {
            num1.moveToStart();
            num2.moveToStart();
            int digitSum = (Character.getNumericValue((Character) num1.getValue())) + (Character.getNumericValue((Character) num2.getValue()));
            num1.remove();
            num2.remove();
            if (digitSum > 9) {
                int ones = digitSum - 10;
                int carry = getRemainder(sum);
                sum.insert(ones + carry);
                sum.moveToStart();
                sum.insert(1);
                sum = addition(num1, num2, sum);
            } else {
                int carry = getRemainder(sum);
                int carrySum = digitSum + carry;
                sum.moveToStart();
                if (carrySum > 9) {
                    sum.insert(1);
                } else {
                    sum.insert(carrySum);
                    sum.moveToStart();
                    sum.insert(0);
                }
                sum = addition(num1, num2, sum);
            }
        }
        return sum;
    }

    public static int getRemainder(LList list) {
        int carry = 0;
        list.moveToStart();
        try {
            if (!list.isAtEnd()) {
                carry = (int) list.getValue();
                list.remove();
            }
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
        return carry;
    }

//    public static LList multiplication(LList num1, LList num2, LList sum) {
//
//    }

    public static LList reverse(Object obj) {
        LList list = new LList();
        String s = obj.toString();
        char[] charArray = s.toCharArray();
        for (int i = charArray.length; i > 0; i--) {
            list.append(charArray[i - 1]);
        }
        return list;
    }

}
