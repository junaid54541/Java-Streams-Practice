package com.smallworld;


import com.smallworld.model.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionDataFetcherTest {

    private final TransactionDataFetcher transactionDataFetcher;
    private final ArrayList<Transaction> initializedList = new ArrayList<>();

    public TransactionDataFetcherTest(){
        transactionDataFetcher = new TransactionDataFetcher();
        initializedList.add(new Transaction(12345L, 150.2, "ali", 20, "junaid", 27, 3, false, "issue3"));
        initializedList.add(new Transaction(12345L, 150.2, "ali", 20, "junaid", 27, 2, false, "issue2"));
        initializedList.add(new Transaction(12346L, 250.4, "leo", 21, "dan", 29, 2, true, "issue2"));
        initializedList.add(new Transaction(22347L, 620.6, "shams", 20, "sami", 27, 10, true, "issue10"));
        initializedList.add(new Transaction(32347L, 75.6, "saleem", 26, "ravi", 27, 15, true, "issue15"));
        initializedList.add(new Transaction(42347L, 210.6, "ravi", 20, "sami", 27, 11, false, "issue11"));
        initializedList.add(new Transaction(42347L, 210.6, "ravi", 20, "sami", 27, null, true, null));
    }

    @Test
    public void whenGivenTransactionListReturnTotalTransactionAmount(){
        double totalTransactionAmount = transactionDataFetcher.getTotalTransactionAmount(initializedList);
        assertEquals(totalTransactionAmount, 1307.4);
    }

    @Test
    public void whenGivenTransactionListAndSenderNameReturnTotalTransactionAmountSentBy(){
        double totalTransactionAmount = transactionDataFetcher.getTotalTransactionAmountSentBy("ali", initializedList);
        assertEquals(totalTransactionAmount, 150.2);
    }

    @Test
    public void whenGivenTransactionListReturnMaxTransactionAmount(){
        double totalTransactionAmount = transactionDataFetcher.getMaxTransactionAmount(initializedList);
        assertEquals(totalTransactionAmount, 620.6);
    }

    @Test
    public void whenGivenTransactionListReturnTopSender(){
        String topSender = transactionDataFetcher.getTopSender(initializedList).get().toString();
        assertEquals("shams", topSender);
    }

    @Test
    public void whenGivenTransactionListReturnCountUniqueClients(){
        long actual = transactionDataFetcher.countUniqueClients(initializedList);
        assertEquals(8, actual);
    }

    @Test
    public void whenGivenTransactionListReturnAllSolvedIssueMessages(){
        List<String> expected = Arrays.asList("issue2", "issue10", "issue15");
        List<String> actual = transactionDataFetcher.getAllSolvedIssueMessages(initializedList);
        assertEquals(expected, actual);
    }

    @Test
    public void whenGivenTransactionListReturnTop3TransactionsByAmount(){
        List<Transaction> expected = new ArrayList<>();
        expected.add(new Transaction(22347L, 620.6, "shams", 20, "sami", 27, 10, true, "issue10"));
        expected.add(new Transaction(12346L, 250.4, "leo", 21, "dan", 29, 2, true, "issue2"));
        expected.add(new Transaction(42347L, 210.6, "ravi", 20, "sami", 27, 11, false, "issue11"));

        List<Transaction> actual = transactionDataFetcher.getTop3TransactionsByAmount(initializedList);
        Assertions.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void whenGivenTransactionListReturnTransactionsByBeneficiaryName(){
        Map<String, List<Transaction>> expected = new HashMap<>();
        List<Transaction> list = new ArrayList<>();
        list.add(new Transaction(32347L, 75.6, "saleem", 26, "ravi", 27, 15, true, "issue15"));
        list.add(new Transaction(32349L, 75.6, "areeb", 26, "ravi", 27, 15, true, "issue15"));
        expected.put("ravi", list);

        Map<String, List<Transaction>> actual = transactionDataFetcher.getTransactionsByBeneficiaryName(list);
        assertEquals(expected, actual);
    }

    @Test
    public void whenGivenTransactionListReturnUnsolvedIssueIds(){
        Set<Integer> expected = new HashSet<>();
        expected.add(2);
        expected.add(3);
        expected.add(11);

        Set<Integer> actual = transactionDataFetcher.getUnsolvedIssueIds(initializedList);
        assertEquals(expected, actual);
    }

    @Test
    public void whenGivenTransactionListAndSenderNameOrBeneficiaryThenReturnHasOpenComplianceIssuesPresent(){
        Boolean actual = transactionDataFetcher.hasOpenComplianceIssues("sami", initializedList);
        assertEquals(true, actual);
    }

    @Test
    public void whenGivenTransactionListAndSenderNameOrBeneficiaryThenReturnHasOpenComplianceIssuesNotPresent(){
        Boolean actual = transactionDataFetcher.hasOpenComplianceIssues("sameer", initializedList);
        assertEquals(false, actual);
    }

    @Test
    public void whenGivenEmptyListThenReturnUnSupportedOperationExceptionMessage() {
        List<Transaction> transactionList = new ArrayList<>();
        try {
            transactionDataFetcher.getTotalTransactionAmount(transactionList);
        } catch (UnsupportedOperationException e) {
            assertEquals("Operation Not Supported", e.getMessage());
        }
    }
}
