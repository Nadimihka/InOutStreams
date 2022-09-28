import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Basket implements Serializable{
    protected String[] products;
    protected int[] prices;
    protected int[] cart;

    public Basket(String[] products, int[] prices) {
        this.products = products;
        this.prices = prices;
        this.cart = new int[products.length];
    }

    public void setCart(int[] cart) {
        this.cart = cart;
    }

    public void addToCart(int productNum, int amount) {
        cart[productNum] += amount;
    }
    void saveBin(File file) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(this);
        out.close();
    }

    static Basket loadFromBinFile(File file) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        Basket basket = (Basket) in.readObject();
        in.close();
        return basket;
    }

    public void printCart() {
        int sumProducts = 0;
        System.out.println("Ваша корзина:");
        for (int i = 0; i < products.length; i++) {
            if (cart[i] != 0) {
                System.out.println(products[i] + " " +
                        cart[i] + " шт " +
                        prices[i] + " руб/шт " +
                        prices[i] * cart[i] + " руб в сумме");
                sumProducts += prices[i] * cart[i];
            }
        }
        System.out.println("Общая стоимость корзины " + sumProducts);
    }

    public void saveTxt(File textFile) throws IOException {
        try (PrintWriter out = new PrintWriter(textFile)) {
            for (String e : products)
                out.print(e + " ");
            out.println();
            for (int e : prices)
                out.print(e + " ");
            out.println();
            for (int e : cart)
                out.print(e + " ");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static Basket loadFromTxtFile(File textFile) {
        List<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader
                (new FileReader(textFile))) {
            while (reader.ready()) {
                list.add(reader.readLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String[] pr = (list.get(1).split(" "));
        int[] prices = new int[pr.length];
        for (int i = 0; i < pr.length; i++) {
            prices[i] = Integer.parseInt(pr[i]);
        }
        String[] c = (list.get(2).split(" "));
        int[] cartArr = new int[c.length];
        for (int i = 0; i < c.length; i++) {
            cartArr[i] = Integer.parseInt(c[i]);
        }

        Basket cart = new Basket(list.get(0).split(" "), prices);
        cart.setCart(cartArr);
        return cart;
    }
}