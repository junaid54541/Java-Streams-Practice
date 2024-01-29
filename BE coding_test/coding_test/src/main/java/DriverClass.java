import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smallworld.TransactionDataFetcher;
import com.smallworld.model.Transaction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

public class DriverClass {
    public static void main(String[] args) throws FileNotFoundException {
        TransactionDataFetcher transactionDataFetcher = new TransactionDataFetcher();
        File directory = new File("./");

        BufferedReader br = new BufferedReader(new FileReader(directory + "\\coding_test\\transactions.json"));

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Transaction>>() {}.getType();

        List<Transaction> transactions = gson.fromJson(br, listType);
        TransactionDataFetcher dataFetcher = new TransactionDataFetcher();

        System.out.println("Total Transaction Amount: " + dataFetcher.getTotalTransactionAmount(transactions));
        System.out.println("Total Transaction Amount Sent By Arthur ShelBy: " +
                dataFetcher.getTotalTransactionAmountSentBy("Arthur ShelBy", transactions));
        System.out.println("Max Transaction Amount: " + dataFetcher.getMaxTransactionAmount(transactions));
        System.out.println("Count Unique Clients: " + dataFetcher.countUniqueClients(transactions));
        System.out.println("Open Compliance Issues for Arthur ShelBy: " +
                dataFetcher.hasOpenComplianceIssues("Arthur ShelBy", transactions));
        System.out.println("Transactions By Beneficiary Name: " +
                dataFetcher.getTransactionsByBeneficiaryName(transactions));
        System.out.println("Unsolved Issue IDs: " + dataFetcher.getUnsolvedIssueIds(transactions));
        System.out.println("Solved Issue Messages: " + dataFetcher.getAllSolvedIssueMessages(transactions));
        System.out.println("Top 3 Transactions By Amount: " + dataFetcher.getTop3TransactionsByAmount(transactions));
        System.out.println("Top Sender: " + dataFetcher.getTopSender(transactions));
    }
}
