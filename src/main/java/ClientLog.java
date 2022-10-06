import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {
    protected int productNum;
    protected int amount;
    protected List<String> logAll;

    public ClientLog() {
        this.productNum = productNum;
        this.amount = amount;
        this.logAll = new ArrayList<>();
    }

    public void log(int productNum, int amount) {
        if (logAll.isEmpty()) {
            logAll.add("productNum" + "," + "amount");
        }

        logAll.add(Integer.toString(productNum) + "," + Integer.toString(amount));
    }

    public void exportAsCSV(File txtFile) throws IOException {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(txtFile))) {
            for (String st : logAll) {
                csvWriter.writeNext(st.toString().split(","));
            }
        }
    }
}
