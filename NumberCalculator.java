import java.util.Scanner;

public class NumberCalculator {

    /* All the variables that will be used in the program */
    private static Scanner readingInputValues = new Scanner(System.in);
    private static boolean isRunningProgram;
    private static int operationChoice;
    private static float firstNumber;
    private static float secondNumber;

    /* All the messages that will be displayed to the user */
    private static final String MENU_MESSAGE = """
            Welcome to the Number Calculator!
            Make your choice:
            ---------
            1 - Sum
            2 - Subtract
            3 - Multiply
            4 - To divide
            5 - Close the program
            ---------""";
    private static final String INVALID_CHOICE = "Invalid choice! Please enter a number between 1 and 5.";
    private static final String INFO_EXIT_STRING = "Goodbye! Closing the program...";
    private static final String ENTER_FIRST_NUMBER = "Enter the first number: ";
    private static final String ENTER_SECOND_NUMBER = "Enter the second number: ";
    private static final String CALCULATION_RESULT_MESSAGE = "The result of the calculation is: ";
    private static final String INSERT_POSITIVE_NUMBER = "Insert a number greater than 0!";

    /* All the methods that will be used in the program */
    public static void main(String[] args) {

        isRunningProgram = true;
        while (isRunningProgram) {

            try {
                System.out.println(MENU_MESSAGE);
                operationChoice = readingInputValues.nextInt();

                if (operationChoice == 5) {
                    System.out.println(INFO_EXIT_STRING);
                    finalizeProgram();
                } else {
                    executeSelectedOperation(operationChoice);
                }

            } catch (Exception e) {
                System.out.println(INVALID_CHOICE);
                readingInputValues.nextLine();
            }

        }
    }

    public static void executeSelectedOperation(int choosingCalculationType) {
        float result = 0;

        System.out.println(ENTER_FIRST_NUMBER);
        firstNumber = readingInputValues.nextInt();
        System.out.println(ENTER_SECOND_NUMBER);
        secondNumber = readingInputValues.nextInt();

        while (firstNumber <= 0 || secondNumber <= 0) {
            System.out.println(INSERT_POSITIVE_NUMBER);
            System.out.println();
            System.out.println(ENTER_FIRST_NUMBER);
            firstNumber = readingInputValues.nextInt();
            System.out.println(ENTER_SECOND_NUMBER);
            secondNumber = readingInputValues.nextInt();
        }

        switch (choosingCalculationType) {
            case 1 -> result = firstNumber + secondNumber;
            case 2 -> result = firstNumber - secondNumber;
            case 3 -> result = firstNumber * secondNumber;
            case 4 -> result = firstNumber / secondNumber;
        }

        System.out.println(CALCULATION_RESULT_MESSAGE + result);
        System.out.println();
    }

    public static void finalizeProgram() {
        readingInputValues.close();
        isRunningProgram = false;
        System.exit(0);
    }
}
