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
                                Object num1 = stack.pop();
                                reverse(num1);
                                System.out.println();
                                Object num2 = stack.pop();
                                reverse(num2);
                            } else if (value.equals("*")) {
                                Object num1 = stack.pop();
                                reverse(num1);
                                System.out.println();
                                Object num2 = stack.pop();
                                reverse(num2);
                            } else if (value.equals("^")) {
                                Object num1 = stack.pop();
                                reverse(num1);
                                System.out.println();
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
          System.out.println(charArray[i - 1]);
        }
        return list;
    }

    public static LList addition(LList num1, LList num2) {
        LList sum = new LList();


        return sum;
    }

}
