package com.example.lab3;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;

public class DashboardController implements Initializable {
    @FXML private Label greetingLabel;
    @FXML private Label accountDetailsLabel;
    @FXML private ListView<Transaction> transactionList;
    @FXML private TextField transactionLimitField;
    @FXML private TextField amountField;
    @FXML private Label limitErrorLabel;
    @FXML private Label errorLabel;
    @FXML private Label successLabel;

    private BankAccount account;
    private static final int DEFAULT_TRANSACTION_LIMIT = 5;
    private static final int MAX_TRANSACTION_LIMIT = 10;

    public void setAccount(BankAccount account) {
        this.account = account;
        updateDisplay();
    }

    @Override
    public void initialize(URL url, java.util.ResourceBundle rb) {
        transactionList.setCellFactory(param -> new ListCell<Transaction>() {
            @Override
            protected void updateItem(Transaction transaction, boolean empty) {
                super.updateItem(transaction, empty);
                if (empty || transaction == null) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10);
                    hbox.setPrefHeight(25);

                    Text typeText = new Text(transaction.getType());
                    typeText.setStyle("-fx-font-weight: bold;");

                    Text amountText = new Text(String.format("$%.2f", transaction.getAmount()));
                    amountText.setStyle(
                            transaction.getType().equals("Withdrawal") ?
                                    "-fx-fill: red;" : "-fx-fill: green;"
                    );
                    HBox amountBox = new HBox(amountText);
                    HBox.setHgrow(amountBox, Priority.ALWAYS);

                    Text dateText = new Text(transaction.getTimestamp().toString());

                    hbox.getChildren().addAll(typeText, amountBox, dateText);
                    setGraphic(hbox);
                }
            }
        });
    }

    private void updateDisplay() {
        if (account != null) {
            greetingLabel.setText("Hi, " + account.getAccountHolder());
            String details = String.format("Account: %s - Account Number %s - Balance: %.2f",
                    account.getAccountType(), account.getAccountNumber(), account.getBalance());
            if ("FIXED".equals(account.getAccountType())) {
                details += String.format(" - Period: %d days", account.getDuration());
            }
            accountDetailsLabel.setText(details);
            updateTransactionList(DEFAULT_TRANSACTION_LIMIT);
            clearFeedbackLabels();
        }
    }

    private void updateTransactionList(int limit) {
        limit = Math.min(limit, MAX_TRANSACTION_LIMIT);
        transactionList.setItems(FXCollections.observableArrayList(account.transactionHistory.getLastNItems(limit)));
    }

    @FXML
    private void handleDeposit(ActionEvent event) {
        try {
            clearFeedbackLabels();
            int amount = validateAmount();
            if (amount > 0) {
                account.deposit(amount);
                updateDisplay();
                successLabel.setText("Deposit of $" + amount + " successful");
                amountField.clear();
            }
        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleWithdraw(ActionEvent event) {
        try {
            clearFeedbackLabels();
            int amount = validateAmount();
            if (amount > 0) {
                try {
                    account.withdraw(amount);
                    updateDisplay();
                    successLabel.setText("Withdrawal of $" + amount + " successful");
                    amountField.clear();;
                } catch (Exception e) {
                    errorLabel.setText(e.getMessage());
                }

            }
        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/lab3/hello-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Create Account");
        stage.show();
    }

    @FXML
    private void updateTransactionLimit(ActionEvent event) {
        try {
            limitErrorLabel.setText("");
            String limitText = transactionLimitField.getText().trim();
            int limit = limitText.isEmpty() ? DEFAULT_TRANSACTION_LIMIT : Integer.parseInt(limitText);
            if (limit <= 0) {
                limitErrorLabel.setText("Limit must be positive");
                return;
            }
            updateTransactionList(limit);
            limitErrorLabel.setText("Transaction history updated");
        } catch (NumberFormatException e) {
            limitErrorLabel.setText("Please enter a valid number");
        }
    }

    private int validateAmount() throws Exception {
        String amountText = amountField.getText().trim();
        if (amountText.isEmpty()) {
            throw new Exception("Please enter an amount");
        }
        try {
            int amount = Integer.parseInt(amountText);
            if (amount <= 0) {
                throw new Exception("Amount must be positive");
            }
            return amount;
        } catch (NumberFormatException e) {
            throw new Exception("Amount must be a valid number");
        }
    }

    private void clearFeedbackLabels() {
        errorLabel.setText("");
        successLabel.setText("");
        limitErrorLabel.setText("");
    }
}