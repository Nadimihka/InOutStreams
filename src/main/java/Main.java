import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Main {
    public static void main(String[] args) throws IOException {

        String[] products = {"Хлеб", "Яблоки", "Молоко"};
        int[] prices = {100, 200, 300};

        System.out.println("Список возможных товаров для покупки:");
        for (int i = 0; i < products.length; i++) {
            System.out.println(i + 1 + " " + products[i] + " " + prices[i] + " руб.");
        }
        Scanner scanner = new Scanner(System.in);
        Basket result = new Basket(products, prices);
        ClientLog clientLog = new ClientLog();

        File jsonFile = new File("basket.json");
        File csvFile = new File("log.csv");

        try {
            if (jsonFile.exists()) {
                result = Basket.loadFromJsonFile(jsonFile);
            } else if (jsonFile.createNewFile())
                System.out.println("Файл был создан");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        result.printCart();

        while (true) {
            System.out.println("Выберите товар и количество или `end`");
            String input = scanner.nextLine();
            if ("end".equals(input)) {
                break;
            }
            String[] current = input.split(" ");
            int productNumber = parseInt(current[0]);
            int productQuantity = parseInt(current[1]);
            result.addToCart(productNumber - 1, productQuantity);
            clientLog.log(productNumber, productQuantity);
        }
        result.saveJson(jsonFile);
        clientLog.exportAsCSV(csvFile);
        result.printCart();
    }
}