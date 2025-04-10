package com.example.lab3;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class BankApplicationController implements Initializable {
    @FXML private Label welcomeText;
    @FXML private TextField Aname;
    @FXML private ComboBox<String> Atype;
    @FXML private TextField InitialDeposit;
    @FXML private Label errorLabel;
    @FXML private Label periodLabel;
    @FXML private TextField periodField;

    private BankAccount createdAccount;
    private static final int DEFAULT_PERIOD = 90;
    private static final int MIN_PERIOD = 90;
    private static final int MAX_PERIOD = 365;
    private static final double SAVINGS_MINIMUM_DEPOSIT = 50.00;
    private static final double FIXED_MINIMUM_DEPOSIT = 50000.00;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Atype.setItems(FXCollections.observableArrayList("SAVINGS", "CURRENT", "FIXED"));

        Atype.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if ("SAVINGS".equals(newValue)) {
                InitialDeposit.setPromptText("Minimum deposit: " + SAVINGS_MINIMUM_DEPOSIT);
            } else if ("FIXED".equals(newValue)) {
                InitialDeposit.setPromptText("Minimum deposit: " + FIXED_MINIMUM_DEPOSIT);
            } else {
                InitialDeposit.setPromptText("Enter initial deposit (no minimum)");
            }
            boolean isFixed = "FIXED".equals(newValue);
            periodLabel.setVisible(isFixed);
            periodField.setVisible(isFixed);
        });

        if (Atype.getSelectionModel().getSelectedItem() == null) {
            InitialDeposit.setPromptText("Enter initial deposit (no minimum)");
        }
    }

    @FXML
    public void handleSubmit(MouseEvent event) {
        String accountName = Aname.getText().trim();
        String accountType = Atype.getSelectionModel().getSelectedItem();
        String depositText = InitialDeposit.getText().trim();

        if (accountName.isEmpty()) {
            errorLabel.setText("Account name cannot be empty");
            return;
        }

        if (accountType == null) {
            errorLabel.setText("Please select an account type");
            return;
        }

        if (depositText.isEmpty()) {
            errorLabel.setText("Please enter an initial deposit");
            return;
        }

        int period = DEFAULT_PERIOD;
        if ("FIXED".equals(accountType)) {
            String periodText = periodField.getText().trim();
            if (!periodText.isEmpty()) {
                try {
                    period = Integer.parseInt(periodText);
                    if (period < MIN_PERIOD || period > MAX_PERIOD) {
                        errorLabel.setText("Period must be between " + MIN_PERIOD + " and " + MAX_PERIOD + " days");
                        return;
                    }
                } catch (NumberFormatException e) {
                    errorLabel.setText("Period must be a valid number");
                    return;
                }
            }
        }

        int initialDeposit;
        try {
            initialDeposit = Integer.parseInt(depositText);
            if (initialDeposit <= 0) {
                errorLabel.setText("Initial deposit must be positive");
                return;
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Initial deposit must be a valid number");
            return;
        }

        try {
            createdAccount = getCreatedAccount(accountName, accountType, initialDeposit, period);
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
            return;
        }

        try {
            switchToDashboard(event);
        } catch (Exception e) {
            errorLabel.setText("Failed to switch to dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public BankAccount getCreatedAccount(String name, String type, int initialAmount, int period) {
        return switch (type) {
            case "SAVINGS" -> new SavingsAccount(name, initialAmount, type);
            case "FIXED" -> new FixedDepositAccount(name, initialAmount, type, period);
            case "CURRENT" -> new CurrentAccount(name, initialAmount, type);
            default -> null;
        };
    }

    private void switchToDashboard(MouseEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/lab3/Dashboard.fxml"));
        Parent root = loader.load();

        DashboardController controller = loader.getController();
        controller.setAccount(createdAccount);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Account Dashboard");
        stage.show();
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}