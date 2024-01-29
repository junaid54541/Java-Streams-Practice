package com.smallworld;

import com.smallworld.model.Transaction;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class TransactionDataFetcher {

    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount(List<Transaction> transactionList) {
        try {
            return transactionList.stream()
                    .collect(Collectors.toMap(Transaction::getMtn, Transaction::getAmount, (existing, replacement) -> existing))
                    .values().stream()
                    .mapToDouble(Double::doubleValue)
                    .sum();
        } catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName, List<Transaction> transactionList) {
        try {
            Set<Long> set = new HashSet<>(transactionList.size());
            return transactionList.stream().filter(transaction -> transaction.getSenderFullName().equals(senderFullName) && set.add(transaction.getMtn()))
                    .mapToDouble(Transaction::getAmount).sum();
        } catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount(List<Transaction> transactionList) {
        try {
            return transactionList.stream()
                    .collect(Collectors.toMap(Transaction::getMtn, Transaction::getAmount, (oldValue, newValue) -> oldValue))
                    .values().stream()
                    .mapToDouble(Double::doubleValue)
                    .max().orElse(0);
        } catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients(List<Transaction> transactionList) {
        try {
            return transactionList.stream()
                    .flatMap(transaction -> Stream.of(transaction.getSenderFullName(), transaction.getBeneficiaryFullName()))
                    .collect(Collectors.toSet()).size();
        } catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public Boolean hasOpenComplianceIssues(String clientFullName, List<Transaction> transactionList) {
        try {
            return transactionList.stream()
                    .anyMatch(transaction ->
                            (transaction.getBeneficiaryFullName().equals(clientFullName) ||
                                    transaction.getSenderFullName().equals(clientFullName)) &&
                                    !transaction.getIssueSolved()
                    );
        } catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, List<Transaction>> getTransactionsByBeneficiaryName(List<Transaction> transactionList) {
        try {
            return transactionList.stream()
                    .collect(Collectors.toMap(Transaction::getMtn, Function.identity(),
                            (existing, replacement) -> existing))
                    .values()
                    .stream()
                    .collect(Collectors.groupingBy(Transaction::getBeneficiaryFullName));
        } catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Integer> getUnsolvedIssueIds(List<Transaction> transactionList) {
        try {
            return transactionList.stream()
                    .filter(transaction -> !transaction.getIssueSolved())
                    .map(Transaction::getIssueId)
                    .collect(Collectors.toSet());
        } catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages(List<Transaction> transactionList) {
        try {
            return transactionList.stream()
                    .filter(transaction -> transaction.getIssueSolved() && Objects.nonNull(transaction.getIssueMessage()))
                    .map(Transaction::getIssueMessage)
                    .collect(Collectors.toList());
        } catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Returns the 3 transactions with highest amount sorted by amount descending
     */
    public List<Transaction> getTop3TransactionsByAmount(List<Transaction> transactionList) {
        try {
            return transactionList.stream()
                    .collect(Collectors.toMap(Transaction::getMtn, Function.identity(), BinaryOperator.maxBy(Comparator.comparingDouble(Transaction::getAmount))))
                    .values().stream().sorted(Comparator.comparingDouble(Transaction::getAmount).reversed()).limit(3).collect(Collectors.toList());
        } catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Returns the sender with the most total sent amount
     */
    public Optional<Object> getTopSender(List<Transaction> transactionList) {
        try {
            Map<String, Optional<Transaction>> maxTransactionBySender = transactionList.stream()
                    .collect(groupingBy(Transaction::getSenderFullName,
                            maxBy(Comparator.comparing(Transaction::getAmount))));

            return Optional.ofNullable(maxTransactionBySender.entrySet().stream()
                    .filter(entry -> entry.getValue().isPresent())
                    .max(Comparator.comparing(entry -> entry.getValue().get().getAmount()))
                    .map(Map.Entry::getKey)
                    .orElse(null));
        } catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException();
        }
    }
}
