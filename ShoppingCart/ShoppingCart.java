package ShoppingCart;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ShoppingCart {
    public static void main(String[] args) {
        showShoppingMenu();
    }

    public static void showShoppingMenu() {
        final String MENU_PRESENTATION = """
                === Shopping Cart Menu ===
                1. Add product
                2. Remove product
                3. Show shopping cart
                4. Calculate total
                5. Apply discount
                6. Exit
                Make your option:""";
        final String TOTAL_PRICE = "\nTotal: $%.2f%n%n";
        final String THANKS_USING_SYSTEM = "Thank you for using our system!";
        final String VALID_MENU_OPTION = "Please, enter a valid menu option! \n";

        Scanner collectInput = new Scanner(System.in);
        ShoppingCartSystem cartSystem = new ShoppingCartSystem();

        while (true) {
            try {

                System.out.println(MENU_PRESENTATION);
                int operationOption = collectInput.nextInt();
                collectInput.nextLine(); // Clear buffer after reading int

                switch (operationOption) {
                    case 1 -> cartSystem.addNewProduct();
                    case 2 -> cartSystem.removeProduct();
                    case 3 -> cartSystem.displayShoppingCart();
                    case 4 -> System.out.printf(TOTAL_PRICE, cartSystem.calculateTotal());
                    case 5 -> cartSystem.applyDiscount();
                    case 6 -> {
                        System.out.println(THANKS_USING_SYSTEM);
                        collectInput.close();
                        System.exit(0);
                    }
                }
            } catch (Exception e) {
                System.out.println(VALID_MENU_OPTION);
                return;
            }
        }

    }
}

class ShoppingCartSystem {
    private static final String INSERT_PRODUCT_NAME = "Product name: ";
    private static final String INSERT_PRODUCT_PRICE = "Product price: $";
    private static final String INSERT_QUANTITY = "Quantity: ";
    private static final String SUCESSFULLY_ADDED = "Product sucessfully added!\n";
    private static final String NAME_PRODUCT_REMOVE = "Name of product to remove: ";
    private static final String SUCESSFULLY_REMOVED = "Product sucessfully removed!\n";
    private static final String FAILURE_REMOVED = "Failed to remove product...\n";
    private static final String INSERT_DISCOUNT_PERCENTAGE = "Discount percentage(ex: 20): ";
    private static final String TOTAL_DISCOUNT = "\nTotal with discount: $%.2f%n";
    private static final String ENTER_VALID_DATA = "Please, enter valid data!";
    private static final String SHOPPING_CART_LIST = ">== Shopping Cart ==<";
    private static final String CART_IS_EMPTY = "The shopping cart is empty!";
    private static final String SHOW_SHOPPING_CART = "Product: %s | Quantity: %d | Unit price %.2f | Subtotal %.2f%n";

    private List<CartItem> itemsCarts;
    Scanner collectInput = new Scanner(System.in);

    public ShoppingCartSystem() {
        this.itemsCarts = new ArrayList<>();
    }

    public void addItem(Product product, int quantity) {
        for (CartItem items : itemsCarts) {
            if (items.getProduct().getName().equals(product.getName())) {
                items.setQuantity(items.getQuantity() + quantity);
                return;
            }
        }
        itemsCarts.add(new CartItem(product, quantity));
    }

    public void removeItem(String productName) {
        itemsCarts.removeIf(items -> items.getProduct().getName().equals(productName));
    }

    public double calculateTotal() {
        return itemsCarts.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
    }

    public void addNewProduct() {

        try {
            System.out.print(INSERT_PRODUCT_NAME);
            String productName = collectInput.nextLine();

            System.out.print(INSERT_PRODUCT_PRICE);
            double productPrice = collectInput.nextDouble();
            collectInput.nextLine(); // Clear buffer

            System.out.print(INSERT_QUANTITY);
            int quantity = collectInput.nextInt();
            collectInput.nextLine(); // Clear buffer

            addItem(new Product(productName, productPrice), quantity);

            System.out.println(SUCESSFULLY_ADDED);
        } catch (Exception e) {
            System.out.println(ENTER_VALID_DATA);
            System.out.println();
            return;
        }
    }

    public void removeProduct() {
        try {
            System.out.print(NAME_PRODUCT_REMOVE);
            String productToRemove = collectInput.nextLine();
            collectInput.nextLine(); // Clear buffer

            boolean productFound = false;

            for (CartItem items : itemsCarts) {
                if (items.getProduct().getName().equals(productToRemove)) {
                    productFound = true;
                    break;
                }
            }

            if (productFound) {
                removeItem(productToRemove);
                System.out.println(SUCESSFULLY_REMOVED);
            } else {
                System.out.println(FAILURE_REMOVED);
            }

        } catch (Exception e) {
            System.out.println(ENTER_VALID_DATA);
            System.out.println();
            return;
        }
    }

    public void displayShoppingCart() {

        if (itemsCarts.isEmpty()) {
            System.out.println(CART_IS_EMPTY);
            return;
        }

        System.out.println(SHOPPING_CART_LIST);
        for (CartItem items : itemsCarts) {
            System.out.printf(SHOW_SHOPPING_CART,
                    items.getProduct().getName(),
                    items.getQuantity(),
                    items.getProduct().getPrice(),
                    items.getSubtotal());
        }
        System.out.println();
    }

    public void applyDiscount() {

        try {
            System.out.print(INSERT_DISCOUNT_PERCENTAGE);
            double discountPercentage = collectInput.nextDouble();

            double total = calculateTotal();
            double resultDiscount = total - (total * discountPercentage / 100);

            System.out.printf(TOTAL_DISCOUNT, resultDiscount);
            System.out.println();

        } catch (Exception e) {
            System.out.println(ENTER_VALID_DATA);
            System.out.println();
            return;
        }

    }

}

class Product {
    String name;
    double price;

    public Product(String itemName, double itemPrice) {
        this.name = itemName;
        this.price = itemPrice;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

class CartItem {
    Product product;
    int quantity;

    public CartItem(Product product, int itemQuantity) {
        this.product = product;
        this.quantity = itemQuantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return getProduct().getPrice() * quantity;
    }

}
