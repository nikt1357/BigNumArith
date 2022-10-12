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
                    String s = in.nextLine();
                    System.out.println(s);
                }
                in.close();

            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("File could not be opened.");
        }
    }
}
