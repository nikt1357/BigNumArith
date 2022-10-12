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
                    String array[] = s.split("\\s+");
                    for(int i = 0; i < array.length; i++) {
                        if (!array[i].equals("+") || !array[i].equals("*") || !array[i].equals("^")) {
                            stack.push(array[i]);
                        } else {
                            if (array[i].equals("+")) {
                                Object num1 = stack.pop();
                                createList(num1);
                                Object num2 = stack.pop();
                                createList(num2);
                            } else if (array[i].equals("*")) {
                                Object num1 = stack.pop();
                                createList(num1);
                                Object num2 = stack.pop();
                                createList(num2);
                            } else if (array[i].equals("^")) {
                                Object num1 = stack.pop();
                                createList(num1);
                                Object num2 = stack.pop();
                                createList(num2);
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

    public static void createList(Object obj) {
        LList list = new LList();
        String s = obj.toString();
        char[] charArray = s.toCharArray();
        for (int i = charArray.length; i > 0; i--) {
//          list.append(charArray[i - 1]);
            System.out.println(charArray[i - 1]);
        }
//        return list;
    }

}
