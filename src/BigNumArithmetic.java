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
                            stack.push(s);
                        } else {
                            if (array[i].equals("+")) {
                                Object num1 = stack.pop();
                                Object num2 = stack.pop();
                            } else if (array[i].equals("*")) {

                            } else if (array[i].equals("^")) {

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

}
