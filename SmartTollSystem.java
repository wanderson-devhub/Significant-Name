import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class SmartTollSystem {
    private final List<VehicleFeature> userVehicleInstance = new ArrayList<>();
    private final TollPriceFee tollPriceCalculator = new TollPriceFee();
    private final Scanner readingUserInput = new Scanner(System.in);

    /* All most important messages */
    private final String MENU_MESSAGE = """

            ===Welcome to Automated Toll System===
                Make your choice:
                ---------
                1. Register new vehicle
                2. Generate daily report
                3. See total revenue of the day
                4. Close
                ---------""";
    private final String VEHICLE_TYPE_MESSAGE = """
            Vehicle Type:
                1. Car
                2. Motorcycle
                3. Truck
                Make your choice:""";
    private final String CLOSING_PROGRAM = "(x) Closing the program...";
    private final String CHOOSE_VALID_MENU = "Please, choose a valid menu option";
    private final String ENTER_VEHICLE_PLATE = "? Enter the vehicle plate: ";
    private final String INVALID_VEHICLE_TYPE = "Invalid choice of vehicle type!\n";
    private final String VEHICLE_REGISTERED = "(V) Vehicle sucessfully registered!\n";
    private final String REPORT_DAY_PASSAGES = "\nReport of the day's passages";
    private final String VEHICLE_LICENSE_PLATE = "\n- Plate: ";
    private final String SHOW_VEHICLE_TYPE = "\n- Type: ";
    private final String CURRENT_TIME_MESSAGE = "\n- Registered date: ";
    private final String TOLL_FEE_MESSAGE = "\n- Toll Fee: $";
    private final String TOTAL_REVENUE_DAILY = "\n>> Total revenue of the day: $";

    public static void main(String[] args) {
        SmartTollSystem tollSystem = new SmartTollSystem();
        tollSystem.showMenuToUser();
    }

    public void showMenuToUser() {
        while (true) {
            try {
                System.out.println(MENU_MESSAGE);
                int userMenuChoice = readingUserInput.nextInt();
                processMenuChoice(userMenuChoice);
            } catch (Exception e) {
                System.out.println(CHOOSE_VALID_MENU);
                readingUserInput.nextLine();
            }
        }
    }
    
    private void processMenuChoice(int choice) {
        switch (choice) {
            case 1 -> registerNewVehicle();
            case 2 -> showDailyReport();
            case 3 -> showDailyRevenue();
            case 4 -> finalizeProgram();
            default -> throw new IllegalArgumentException(CHOOSE_VALID_MENU);
        }
    }

    private void finalizeProgram() {
        System.out.println(CLOSING_PROGRAM);
        readingUserInput.close();
        System.exit(0);
    }

    private void registerVehicleInSystem(String vehiclePlate, VehicleType choiceType,
            LocalDateTime currentDate) {
        double tollFee = tollPriceCalculator.returnTollFee(choiceType);
        VehicleFeature registerVehicle = new VehicleFeature(vehiclePlate, choiceType, currentDate, tollFee);
        userVehicleInstance.add(registerVehicle);
    }

    private void registerNewVehicle() {
        readingUserInput.nextLine();

        System.out.println(ENTER_VEHICLE_PLATE);
        String vehiclePlate = readingUserInput.nextLine();

        System.out.println("\n" + VEHICLE_TYPE_MESSAGE);
        int vehicleTypeChoice = readingUserInput.nextInt();

        VehicleType typeVehicleChosen = null;

        switch (vehicleTypeChoice) {
            case 1 -> typeVehicleChosen = VehicleType.CAR;
            case 2 -> typeVehicleChosen = VehicleType.MOTORCYCLE;
            case 3 -> typeVehicleChosen = VehicleType.TRUCK;
            default -> {
                System.out.println(INVALID_VEHICLE_TYPE);
                return;
            }
        }

        registerVehicleInSystem(vehiclePlate, typeVehicleChosen, LocalDateTime.now());
        System.out.println(VEHICLE_REGISTERED);
    }

    private List<VehicleFeature> generateDailyReport(LocalDate dateRegistration) {
        return userVehicleInstance.stream()
                .filter(vehiclePassage -> vehiclePassage.getCurrentDatePassage().toLocalDate().equals(dateRegistration))
                .collect(Collectors.toList());
    }

    private void showDailyReport() {
        List<VehicleFeature> dailyReportGenerated = generateDailyReport(LocalDate.now());
        System.out.println(REPORT_DAY_PASSAGES);
        dailyReportGenerated.forEach(this::printVehicleInfo);
    }

    private void printVehicleInfo(VehicleFeature vehiclePassage) {
        System.out.printf("%s%s%s%s%s%s%s%.2f%n",
                VEHICLE_LICENSE_PLATE, vehiclePassage.getLicensePlate(),
                SHOW_VEHICLE_TYPE, vehiclePassage.getVehicleType(),
                CURRENT_TIME_MESSAGE, vehiclePassage.getCurrentDatePassage(),
                TOLL_FEE_MESSAGE, vehiclePassage.getTollFee());
    }

    private double getDailyRevenue(LocalDate dateRegistration) {
        return userVehicleInstance.stream()
                .filter(vehiclePassage -> vehiclePassage.getCurrentDatePassage().toLocalDate().equals(dateRegistration))
                .mapToDouble(VehicleFeature::getTollFee)
                .sum();
    }

    private void showDailyRevenue() {
        double totalRevenueOfTheDay = getDailyRevenue(LocalDate.now());
        System.out.printf("%s%.2f%n", TOTAL_REVENUE_DAILY, totalRevenueOfTheDay);
    }
}

class VehicleFeature {
    private String licensePlate;
    private VehicleType vehicleType;
    private LocalDateTime currentDatePassage;
    private double tollFee;

    public VehicleFeature(String licensePlate, VehicleType vehicleType, LocalDateTime currentDatePassage,
            double tollFee) {
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.currentDatePassage = currentDatePassage;
        this.tollFee = tollFee;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public LocalDateTime getCurrentDatePassage() {
        return currentDatePassage;
    }

    public double getTollFee() {
        return tollFee;
    }
}

class TollPriceFee {
    private static final String INVALID_VEHICLE_TYPE = "Invalid choice of vehicle type!\n";

    public double returnTollFee(VehicleType choiceVehicleType) {
        return switch (choiceVehicleType) {
            case CAR -> 10.0;
            case MOTORCYCLE -> 5.0;
            case TRUCK -> 20.0;
            default -> throw new IllegalArgumentException(INVALID_VEHICLE_TYPE);
        };
    }
}

enum VehicleType {
    CAR,
    MOTORCYCLE,
    TRUCK
}