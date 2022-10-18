import java.io.*;
import java.util.*;

public class BigNumArithmetic {

    public static void main(String[] args) {
        try {
            /* Error checking statement:
             If the number of arguments is not exactly 1, throw FileNotFoundException */
            if (args.length != 1) {
                throw new FileNotFoundException("Incorrect number of arguments.");
            } else {
                /* Set up FileInputStream and Scanner to read from the file passed */
                FileInputStream file = new FileInputStream(args[0]);
                Scanner in = new Scanner(file);

                /* Reads through the input file line by line as a String */
                /* Call .trim() on the String to get rid of whitespace on the edges, then split the String
                   into an Array by whitespace */
                while (in.hasNext()) {
                    AStack stack = new AStack();
                    String s = (in.nextLine()).trim();
                    String[] AList = s.split("\\s+");

                    /* Iterates through the Array, removing leading zeros off every value */
                    for (String item : AList) {
                        String strPattern = "^0+(?!$)";
                        item = item.replaceAll(strPattern, "");
                        System.out.print(item + " ");
                    }

                    /* boolean to keep track of if operations performed are valid (i.e. 2 numbers for every operator) */
                    boolean valid = true;

                    /* Iterates through the Array,
                       if the value is a number, push it on the stack
                       if it's not then perform the corresponding operation */
                    for (String value : AList) {
                        if ((!value.equals("+")) && (!value.equals("*")) && (!value.equals("^"))) {
                            /* Number found, push to stack */
                            stack.push(value);
                        } else {
                            /* Check if there are at least two items on the stack,
                               if there are, perform the corresponding operation
                               if there's not, set the valid variable equal to -1*/
                            if (stack.length() >= 2) {
                                if (value.equals("+")) {
                                    /* Addition symbol found, pop two values off the stack and call
                                       the addition method on them and then push the result back on the stack */
                                    LList sum = new LList();
                                    Object num1 = stack.pop();
                                    Object num2 = stack.pop();
                                    sum = addition(reverse(num1), reverse(num2), sum);
                                    stack.push(listToString(sum));
                                } else if (value.equals("*")) {
                                    /* Multiplication symbol found, pop two values off the stack and call
                                       the multiplication method on them and then push the result back on the stack */
                                    LList product = new LList();
                                    AStack mult = new AStack(100);
                                    Object num1 = stack.pop();
                                    Object num2 = stack.pop();
                                    product = multiplication(reverse(num1), reverse(num2), mult, 0, product);
                                    stack.push(listToString(product));
                                } else {
                                    /* Exponentiation symbol found, pop two values off the stack and call
                                       the exponential method on them and then push the result back on the stack */
                                    Object num1 = stack.pop();
                                    Object num2 = stack.pop();
                                    String result = exponential(num2.toString(), num1.toString());
                                    stack.push(result);
                                }
                            } else {
                                valid = false;
                            }
                        }
                    }

                    /* Nested if statements to figure out what to print, if the valid variable has been set
                       to false then just print the equals sign and clear the stack */
                    if (valid) {
                        /* First check if the line is blank (length of the array is greater than 1,
                           if it is, first print just an equals sign,
                           if it's not then do nothing */
                        if (AList.length > 1) {
                            System.out.print("= ");
                            /* Then check the stack,
                               if there is only one value then pop it and print it,
                               if not then print a new line and clear the stack */
                            if (stack.length() == 1) {
                                System.out.println(stack.pop());
                            } else {
                                System.out.println();
                                stack.clear();
                            }
                        }
                    } else {
                        System.out.println("= ");
                        stack.clear();
                    }
                }
                /* Close the Scanner */
                in.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("File could not be opened.");
        }
    }

    /**
     * Recursive method that computes the sum of two BigNums
     * @param num1 LList containing a reversed BigNum
     * @param num2 LList containing a reversed BigNum
     * @param sum An empty LList used to store the resulting sum
     * @return A LList containing a BigNum
     */
    public static LList addition(LList num1, LList num2, LList sum) {

        if (num1.isEmpty() && num2.isEmpty()) {
            /* Base case: Both num1 and num2 LLists are empty, return the sum LList */
            return sum;
        } else if (!num1.isEmpty() && num2.isEmpty()) {
            /* Recursive case 1: num1 is not empty while num2 is */

            /* Check the remainder, add to the current digit and then remove it from the LList */
            int carry = getRemainder(sum);
            num1.moveToStart();
            int carrySum = Character.getNumericValue((Character) num1.getValue()) + carry;
            num1.remove();

            /* Figure out what has to carry */
            sum.moveToStart();
            if (carrySum > 9) {
                /* If carrySum is greater than 9 then we need to carry a 1,
                   first we insert the ones place into sum and then the 1 in front of it */
                sum.insert(carrySum - 10);
                sum.moveToStart();
                sum.insert(1);
            } else {
                /* If carrySum is less than 9 then we need to carry a 0,
                   first we insert the carrySum into sum and then a 0 in front of it */
                sum.insert(carrySum);
                sum.moveToStart();
                sum.insert(0);
            }

            /* Recursive call */
            sum = addition(num1, num2, sum);

        } else if (num1.isEmpty() && !num2.isEmpty()) {
            /* Recursive case 2: num1 is empty while num2 is not */

            /* Check the remainder, add to the current digit and then remove it from the LList */
            int carry = getRemainder(sum);
            num2.moveToStart();
            int carrySum = Character.getNumericValue((Character) num2.getValue()) + carry;
            num2.remove();

            /* Figure out what has to carry */
            sum.moveToStart();
            if (carrySum > 9) {
                /* If carrySum is greater than 9 then we need to carry a 1,
                   first we insert the ones place into sum and then the 1 in front of it */
                sum.insert(carrySum - 10);
                sum.moveToStart();
                sum.insert(1);
            } else {
                /* If carrySum is less than 9 then we need to carry a 0,
                   first we insert the carrySum into sum and then a 0 in front of it */
                sum.insert(carrySum);
                sum.moveToStart();
                sum.insert(0);
            }

            /* Recursive call */
            sum = addition(num1, num2, sum);

        } else {
            /* Recursive case 3: num1 and num2 are both not empty */

            /* Get single digits from both LLists, add them together, and then remove them from their LLists */
            num1.moveToStart();
            num2.moveToStart();
            int digitSum = (Character.getNumericValue((Character) num1.getValue()))
                    + (Character.getNumericValue((Character) num2.getValue()));
            num1.remove();
            num2.remove();

            if (digitSum > 9) {
                /* If digitSum is greater than 9 then we need to carry the tenths place, first we insert the
                   ones place plus any remainder, and then the tenths place in front of it */
                int ones = digitSum - 10;
                sum.insert(ones + getRemainder(sum));
                sum.moveToStart();
                sum.insert(1);

                /* Recursive call */
                sum = addition(num1, num2, sum);

            } else {
                /* If digitSum is less than 9, we need to add any remainders and check again if it is greater than 9 */
                int carrySum = digitSum + getRemainder(sum);

                sum.moveToStart();
                if (carrySum > 9) {
                    /* If carrySum is greater than 9 then we need to carry a 1,
                       first we insert the ones place into sum and then the 1 in front of it */
                    sum.insert(carrySum - 10);
                    sum.insert(1);
                } else {
                    /* If carrySum is less than 9 then we need to carry a 0,
                       first we insert the carrySum into sum and then a 0 in front of it */
                    sum.insert(carrySum);
                    sum.moveToStart();
                    sum.insert(0);
                }

                /* Recursive call */
                sum = addition(num1, num2, sum);
            }
        }

        /* Returns the sum LList */
        return sum;
    }

    /**
     * Recursive method that computes the product of two BigNums
     * @param num1 LList containing a reversed BigNum
     * @param num2 LList containing a reversed BigNum
     * @param stack AStack used for storing products of a single digit times a BigNum
     * @param count int used to keep track of how many digits we've multiplied by
     *              (how many zeros to add onto the end of the product)
     * @param product An empty LList used to store the resulting product
     * @return A LList containing a BigNum
     */
    public static LList multiplication(LList num1, LList num2, AStack stack, int count, LList product) {

        LList top;
        LList bottom;
        LList loop = new LList();

        /* Figures out which number is shorter and uses that as the bottom number */
        if (num1.length() >= num2.length()) {
            top = num1;
            bottom = num2;
        } else {
            top = num2;
            bottom = num1;
        }

        if (bottom.isEmpty()) {
            /* Base case: bottom is empty, add together the values in the stack */

            /* Pop off the tow two values of the stack and call the addition function on them
               until the stack is empty */
            while (stack.length() > 1) {
                Object first = stack.pop();
                Object second = stack.pop();
                loop = addition(reverse(first), reverse(second), loop);
                stack.push(listToString(loop));
                loop.clear();
            }

            /* Pop off the final value in the stack and insert it into the product LList */
            product.moveToStart();
            product.insert(stack.pop());
            return product;

        } else {
            /* Recursive case: bottom is not empty */

            top.moveToStart();
            bottom.moveToStart();

            /* Multiply the first value of bottom by every value of top */
            while (!top.isAtEnd()) {
                int b = Character.getNumericValue((Character) bottom.getValue());
                int t = Character.getNumericValue((Character) top.getValue());
                int digitMult = b * t;

                /* Figure out what has to carry, get any remainder and add it to digitMult */
                int carry = getRemainder(loop);
                int carrySum = digitMult + carry;
                loop.moveToStart();

                if (digitMult > 9) {
                    /* If digitMult is greater than 9 then we need to check carrySum before
                       inserting the tenths place */
                    if (carrySum > 9) {
                        /* If carrySum is greater than 9 then we need to carry the ones place, insert it
                           into loop */
                        loop.insert(carrySum % 10);
                    } else {
                        /* If carrySum is less than 9 then just insert it into loop */
                        loop.insert(carrySum);
                    }

                    /* Insert the tenths place into loop */
                    loop.moveToStart();
                    loop.insert(carrySum / 10);
                } else {
                    /* If digitMult is less than 9 then we need to check carrySum before inserting anything */
                    if (carrySum > 9) {
                        /* If carrySum is greater than 9 then we need to insert the ones place and then carry the
                           tenths place */
                        loop.insert(carrySum % 10);
                        loop.moveToStart();
                        loop.insert(carrySum / 10);
                    } else {
                        /* If carrySum is less than 9 then we need to insert the carrySum and then carry a 0 */
                        loop.insert(carrySum);
                        loop.moveToStart();
                        loop.insert(0);
                    }
                }

                /* Move to the next digit in top */
                top.next();
            }

            /* Convert the LList loop into a String and then add on the appropriate amount of zeros before
               pushing onto the stack */
            String sumString = listToString(loop);
            for (int i = count; i > 0; i--) {
                sumString = sumString + "0";
            }
            stack.push(sumString);

            /* Remove the first value of bottom, increase the count, and make the recursive call */
            bottom.moveToStart();
            bottom.remove();
            count++;
            product = multiplication(top, bottom, stack, count, product);
        }

        /* Returns the product LList */
        return product;
    }

    public static String exponential(String num1, String num2) {

        LList base = reverse(num1);
        int n = Integer.parseInt(num2);

        if (n == 0) {
            return "1";
        } else if ((n % 2) == 0) {
            AStack stack = new AStack(100);
            LList product = new LList();
            product = multiplication(base, reverse(num1), stack, 0, product);
            int i = n/2;

            return exponential(listToString(product), String.valueOf(i));
        } else {
            AStack stack = new AStack(100);
            LList product = new LList();
            product = multiplication(base, reverse(num1), new AStack(100), 0, product);
            int i = (n - 1) / 2;

            String x = exponential(listToString(product), String.valueOf(i));
            product.clear();
            return listToString(multiplication(base, reverse(x), stack, 0, product));
        }
    }

    public static String listToString(LList list) {
        String s = "";
        list.moveToStart();
        for (int i = 0; i < list.length(); i++) {
            s = s + list.getValue();
            list.next();
        }
        String strPattern = "^0+(?!$)";
        s = s.replaceAll(strPattern, "");
        return s;
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
