import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Main {

    public static Boolean TxtOrJson(String fileName) {
        return fileName.contains(".txt");
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {

        String[] products = {"Хлеб", "Яблоки", "Молоко"};
        int[] prices = {100, 200, 300};

        System.out.println("Список возможных товаров для покупки:");
        for (int i = 0; i < products.length; i++) {
            System.out.println(i + 1 + " " + products[i] + " " + prices[i] + " руб.");
        }
        Scanner scanner = new Scanner(System.in);
        Basket result = new Basket(products, prices);
        ClientLog clientLog = new ClientLog();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("shop.xml"));

        XPath xPath = XPathFactory.newInstance().newXPath();
        String loadFileName = xPath
                .compile("/config/load/fileName")
                .evaluate(doc);
        Boolean loadEnabledFile = Boolean.parseBoolean(xPath
                .compile("/config/load/enabled")
                .evaluate(doc));

        String logFileName = xPath
                .compile("/config/log/fileName")
                .evaluate(doc);
        Boolean saveLogFile = Boolean.parseBoolean(xPath
                .compile("/config/log/enabled")
                .evaluate(doc));

        File loadFile = new File(loadFileName);
        File csvFile = new File(logFileName);

        try {
            if ((loadEnabledFile) && (loadFile.exists())) {
                if (TxtOrJson(loadFileName)) {
                    result = Basket.loadFromTxtFile(loadFile);
                } else {
                    result = Basket.loadFromJsonFile(loadFile);
                }
            }
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
        if (saveLogFile) {
            clientLog.exportAsCSV(csvFile);
        }

        String saveFileName = xPath
                .compile("/config/save/fileName")
                .evaluate(doc);
        Boolean saveEnabledFile = Boolean.parseBoolean(xPath
                .compile("/config/save/enabled")
                .evaluate(doc));

        File saveFile = new File(saveFileName);

        try {
            if (saveEnabledFile) {
                if (!saveFile.exists()) {
                    saveFile.createNewFile();
                    System.out.println("Файл был создан");
                }
                if (TxtOrJson(saveFileName)) {
                    result.saveTxt(saveFile);
                } else {
                    result.saveJson(saveFile);
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        result.printCart();
    }
}