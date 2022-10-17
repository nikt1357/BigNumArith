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
                            } else if (value.equals("*")) {
                                LList product = new LList();
                                AStack mult = new AStack(100);
                                Object num1 = stack.pop();
                                Object num2 = stack.pop();
                                product = multiplication(reverse(num1), reverse(num2), mult, 0, product);
                                String productString = "";
                                product.moveToStart();
                                for (int i = 0; i < product.length(); i++) {
                                    productString = productString + product.getValue();
                                    product.next();
                                }
                                String strPattern = "^0+(?!$)";
                                productString = productString.replaceAll(strPattern, "");
                                stack.push(productString);
                            } else {
                                LList result = new LList();
                                Object num1 = stack.pop();
                                Object num2 = stack.pop();
                                result = exponential(num2.toString(), num1.toString(), result);
                                String resultString = "";
                                result.moveToStart();
                                for (int i = 0; i < result.length(); i++) {
                                    resultString = resultString + result.getValue();
                                    result.next();
                                }
                                String strPattern = "^0+(?!$)";
                                resultString = resultString.replaceAll(strPattern, "");
                                stack.push(resultString);
                            }
                        }
                    }
                    if (AList.length > 1) {
                        System.out.println("= " + stack.pop());
                    }
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
                sum.moveToStart();
                sum.insert(1);
            } else {
                sum.insert(carrySum);
                sum.moveToStart();
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
                    sum.insert(carrySum - 10);
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

    public static LList multiplication(LList num1, LList num2, AStack stack, int count, LList product) {
        LList top;
        LList bottom;
        LList loop = new LList();

        if (num1.length() >= num2.length()) {
            top = num1;
            bottom = num2;
        } else {
            top = num2;
            bottom = num1;
        }

        if (bottom.isEmpty()) {
            while (stack.length() > 1) {
                Object first = stack.pop();
                Object second = stack.pop();
                loop = addition(reverse(first), reverse(second), loop);
                String sumString = "";
                loop.moveToStart();
                for (int i = 0; i < loop.length(); i++) {
                    sumString = sumString + loop.getValue();
                    loop.next();
                }
                String strPattern = "^0+(?!$)";
                sumString = sumString.replaceAll(strPattern, "");
                stack.push(sumString);
                loop.clear();
            }
            product.moveToStart();
            product.insert(stack.pop());
            return product;
        } else {
            top.moveToStart();
            bottom.moveToStart();
            while (!top.isAtEnd()) {
                int b = Character.getNumericValue((Character) bottom.getValue());
                int t = Character.getNumericValue((Character) top.getValue());
                int digitMult = b * t;
                if (digitMult > 9) {
                    int carry = getRemainder(loop);
                    int carrySum = digitMult + carry;
                    int ones = carrySum % 10;
                    int tens = carrySum / 10;
                    loop.moveToStart();
                    if (carrySum > 9) {
                        loop.insert(ones);
                    } else {
                        loop.insert(carrySum);
                    }
                    loop.moveToStart();
                    loop.insert(tens);
                    top.next();
                } else {
                    int carry = getRemainder(loop);
                    int carrySum = digitMult + carry;
                    loop.moveToStart();
                    if (carrySum > 9) {
                        loop.insert(carrySum % 10);
                        loop.moveToStart();
                        loop.insert(carrySum / 10);
                    } else {
                        loop.insert(carrySum);
                        loop.moveToStart();
                        loop.insert(0);
                    }
                    top.next();
                }
            }

            String sumString = "";
            loop.moveToStart();
            for (int i = 0; i < loop.length(); i++) {
                sumString = sumString + loop.getValue();
                loop.next();
            }
            for (int i = count; i > 0; i--) {
                sumString = sumString + "0";
            }
            String strPattern = "^0+(?!$)";
            sumString = sumString.replaceAll(strPattern, "");
            stack.push(sumString);
            bottom.moveToStart();
            bottom.remove();
            count++;
            product = multiplication(top, bottom, stack, count, product);
        }
        return product;
    }

    public static LList exponential(String num1, String num2, LList result) {

        LList base = reverse(num1);
        int n = Integer.parseInt(num2);

        System.out.println("Base = " + base);
        System.out.println("n = " + n);

        if (n == 0) {
            result.moveToStart();
            result.insert(1);
            return result;
        } else if ((n % 2) == 0) {
            AStack stack = new AStack(100);
            LList product = new LList();
            product = multiplication(base, reverse(num1), stack, 0, product);
            int i = n/2;

            String productString = "";
            product.moveToStart();
            for (int j = 0; j < product.length(); j++) {
                productString = productString + product.getValue();
                product.next();
            }

            String exponent = "";
            reverse(i).moveToStart();
            for (int j = 0; j < reverse(i).length(); j++) {
                exponent = exponent + reverse(i).getValue();
                reverse(i).next();
            }

            return exponential(productString, exponent, result);
        } else {
            AStack stack = new AStack(100);
            LList product = new LList();
            product = multiplication(base, reverse(num1), new AStack(100), 0, product);
            int i = (n - 1) /2;

            String productString = "";
            product.moveToStart();
            for (int j = 0; j < product.length(); j++) {
                productString = productString + product.getValue();
                product.next();
            }

            String exponent = "";
            reverse(i).moveToStart();
            for (int j = 0; j < reverse(i).length(); j++) {
                exponent = exponent + reverse(i).getValue();
                reverse(i).next();
            }

            LList x = exponential(productString, exponent, result);
            product = new LList();
            return multiplication(reverse(num1), x, stack, 0, product);
        }
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
