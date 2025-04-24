package com.example.lab4.Controller;

import com.example.lab4.Exceptions.EmployeeNotFoundException;
import com.example.lab4.Exceptions.InvalidDepartmentException;
import com.example.lab4.Model.Employee;
import com.example.lab4.Service.EmployeeService;
import com.example.lab4.Utils.EmployeeSalaryComparator;
import com.example.lab4.Utils.EmployeePerformanceComparator;
import com.example.lab4.Utils.EmployeeValidator;
import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class EmployeeController {
    private static final Logger logger = Logger.getLogger(EmployeeController.class.getName());

    @FXML private TableView<Employee<UUID>> employeeTable;
    @FXML private TableColumn<Employee<UUID>, UUID> idColumn;
    @FXML private TableColumn<Employee<UUID>, String> nameColumn;
    @FXML private TableColumn<Employee<UUID>, String> departmentColumn;
    @FXML private TableColumn<Employee<UUID>, Double> salaryColumn;
    @FXML private TableColumn<Employee<UUID>, Double> ratingColumn;
    @FXML private TableColumn<Employee<UUID>, Integer> experienceColumn;
    @FXML private TableColumn<Employee<UUID>, Boolean> activeColumn;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortColumnCombo;
    @FXML private ComboBox<String> departmentFilterCombo;
    @FXML private CheckBox sortOrderCheckBox;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private ComboBox<String> minRatingCombo;
    @FXML private TextField minSalaryField;
    @FXML private TextField maxSalaryField;

    private SortedList<Employee<UUID>> sortedEmployees;
    private EmployeeService employeeService;
    private FilteredList<Employee<UUID>> filteredEmployees;

    @FXML
    public void initialize() {
        employeeService = new EmployeeService();

        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("performanceRating"));
        experienceColumn.setCellValueFactory(new PropertyValueFactory<>("yearsOfExperience"));
        activeColumn.setCellValueFactory(new PropertyValueFactory<>("isActive"));

        // Set up table data
        filteredEmployees = new FilteredList<>(employeeService.getAllEmployees(), p -> true);
        this.sortedEmployees = new SortedList<>(filteredEmployees);
        this.sortedEmployees.comparatorProperty().bind(employeeTable.comparatorProperty());
        employeeTable.setItems(sortedEmployees);

        // Disable update/delete buttons when no employee is selected
        updateButton.disableProperty().bind(Bindings.isNull(employeeTable.getSelectionModel().selectedItemProperty()));
        deleteButton.disableProperty().bind(Bindings.isNull(employeeTable.getSelectionModel().selectedItemProperty()));

        // Initialize sort controls
        sortColumnCombo.getItems().addAll("Salary", "Rating", "Experience");
        sortColumnCombo.setValue("Experience");
        sortOrderCheckBox.setSelected(false); // Default to descending

        // Initialize department filter
        departmentFilterCombo.getItems().add("All");
        departmentFilterCombo.getItems().addAll(employeeService.getAllDepartments());
        departmentFilterCombo.setValue("All");

        // Initialize minimum rating combo
        minRatingCombo.getItems().addAll("1.5","2", "2.5","3.0", "3.5", "4.0", "4.5");
        minRatingCombo.setPromptText("Select Rating");

        // Search and filter functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        departmentFilterCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> applyFilters());

        // Sorting functionality
        sortColumnCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> applySorting());
        sortOrderCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> applySorting());

        // Table selection listener
        employeeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> handleTableSelection());

        // Apply initial sorting
        applySorting();
    }

    private void applyFilters() {
        filteredEmployees.setPredicate(employee -> {
            boolean searchMatch = true;
            boolean salaryMatch = true;
            boolean departmentMatch = true;

            // Search filter
            String searchText = searchField.getText();
            if (searchText != null && !searchText.isEmpty()) {
                String lowerCaseFilter = searchText.toLowerCase();
                searchMatch = employee.getName().toLowerCase().contains(lowerCaseFilter) ||
                        employee.getDepartment().toLowerCase().contains(lowerCaseFilter);
            }

            // Department filter
            String selectedDepartment = departmentFilterCombo.getValue();
            if (selectedDepartment != null && !selectedDepartment.equals("All")) {
                departmentMatch = employee.getDepartment().equals(selectedDepartment);
            }


            // Salary range filter

            String minSalaryText = minSalaryField.getText();
            String maxSalaryText = maxSalaryField.getText();

            double minSalary = minSalaryText.isEmpty() ? Double.MIN_VALUE : Double.parseDouble(minSalaryText);
            double maxSalary = maxSalaryText.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxSalaryText);

            double employeeSalary = employee.getSalary();
            salaryMatch = (employeeSalary >= minSalary) && (employeeSalary <= maxSalary);



            return searchMatch && departmentMatch && salaryMatch;
        });
    }

    private void applySorting() {
        if (sortedEmployees == null) {
            return;
        }

        String selectedColumn = sortColumnCombo.getValue();
        boolean ascending = sortOrderCheckBox.isSelected();

        Comparator<Employee<UUID>> comparator;
        switch (selectedColumn) {
            case "Salary":
                comparator = new EmployeeSalaryComparator<>();
                break;
            case "Rating":
                comparator = new EmployeePerformanceComparator<>();
                break;
            default:
                comparator = Comparator.naturalOrder();
                break;
        }

        if (ascending) {
            comparator = comparator.reversed();
        }

        sortedEmployees.comparatorProperty().unbind();
        sortedEmployees.setComparator(comparator);
    }

    private Dialog<Employee<UUID>> createEmployeeDialog(Employee<UUID> employee, String title, String action) {
        Dialog<Employee<UUID>> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(null);

        ButtonType actionButtonType = new ButtonType(action, ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(actionButtonType, ButtonType.CANCEL);

        VBox content = new VBox(8);
        content.setStyle("-fx-background-color: #ffffff; -fx-padding: 16; -fx-border-color: #e2e8f0; -fx-border-width: 1.5px; -fx-border-radius: 6px; -fx-font-family: 'Roboto'; -fx-font-size: 13px;");

        Label validationLabel = new Label();
        validationLabel.setTextFill(Color.web("#ef4444"));
        validationLabel.setStyle("-fx-font-weight: 500; -fx-font-size: 14px;");

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);

        TextField nameField = new TextField(employee != null ? employee.getName() : "");
        nameField.setPromptText("Enter name");
        nameField.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cbd5e1; -fx-border-radius: 6px; -fx-font-size: 13px; -fx-focus-color: #3b82f6; -fx-text-fill: #1e293b;");

        TextField deptField = new TextField(employee != null ? employee.getDepartment() : "");
        deptField.setPromptText("Enter department");
        deptField.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cbd5e1; -fx-border-radius: 6px; -fx-font-size: 13px; -fx-focus-color: #3b82f6; -fx-text-fill: #1e293b;");

        TextField salaryField = new TextField(employee != null ? String.valueOf(employee.getSalary()) : "");
        salaryField.setPromptText("Enter salary");
        salaryField.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cbd5e1; -fx-border-radius: 6px; -fx-font-size: 13px; -fx-focus-color: #3b82f6; -fx-text-fill: #1e293b;");

        TextField ratingField = new TextField(employee != null ? String.valueOf(employee.getPerformanceRating()) : "");
        ratingField.setPromptText("Enter rating");
        ratingField.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cbd5e1; -fx-border-radius: 6px; -fx-font-size: 13px; -fx-focus-color: #3b82f6; -fx-text-fill: #1e293b;");

        TextField experienceField = new TextField(employee != null ? String.valueOf(employee.getYearsOfExperience()) : "");
        experienceField.setPromptText("Enter years of experience");
        experienceField.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cbd5e1; -fx-border-radius: 6px; -fx-font-size: 13px; -fx-focus-color: #3b82f6; -fx-text-fill: #1e293b;");

        CheckBox activeCheckBox = new CheckBox("Active");
        activeCheckBox.setSelected(employee != null ? employee.getIsActive() : true);
        activeCheckBox.setStyle("-fx-text-fill: #1e293b; -fx-font-size: 13px;");


        // Create and style labels, then add to grid
        Label nameLabel = new Label("Name:");
        nameLabel.setStyle("-fx-font-weight: 500; -fx-text-fill: #1e293b;");
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);

        Label deptLabel = new Label("Department:");
        deptLabel.setStyle("-fx-font-weight: 500; -fx-text-fill: #1e293b;");
        grid.add(deptLabel, 0, 1);
        grid.add(deptField, 1, 1);

        Label salaryLabel = new Label("Salary:");
        salaryLabel.setStyle("-fx-font-weight: 500; -fx-text-fill: #1e293b;");
        grid.add(salaryLabel, 0, 2);
        grid.add(salaryField, 1, 2);

        Label ratingLabel = new Label("Rating:");
        ratingLabel.setStyle("-fx-font-weight: 500; -fx-text-fill: #1e293b;");
        grid.add(ratingLabel, 0, 3);
        grid.add(ratingField, 1, 3);

        Label experienceLabel = new Label("Experience:");
        experienceLabel.setStyle("-fx-font-weight: 500; -fx-text-fill: #1e293b;");
        grid.add(experienceLabel, 0, 4);
        grid.add(experienceField, 1, 4);

        Label activeLabel = new Label("Active:");
        activeLabel.setStyle("-fx-font-weight: 500; -fx-text-fill: #1e293b;");
        grid.add(activeLabel, 0, 5);
        grid.add(activeCheckBox, 1, 5);

        content.getChildren().addAll(validationLabel, grid);
        dialog.getDialogPane().setContent(content);

        // Disable action button until input is valid
        Button actionButton = (Button) dialog.getDialogPane().lookupButton(actionButtonType);
        actionButton.setDisable(true);

        // Validate input dynamically
        ChangeListener<String> validationListener = (observable, oldValue, newValue) -> {
            try {
                EmployeeValidator.validateEmployeeInput(
                        nameField.getText(),
                        deptField.getText(),
                        salaryField.getText(),
                        ratingField.getText(),
                        experienceField.getText()
                );
                validationLabel.setText("");
                actionButton.setDisable(false);
            } catch (IllegalArgumentException | InvalidDepartmentException e) {
                validationLabel.setText("Error: " + e.getMessage());
                actionButton.setDisable(true);
                logger.warning("Validation failed: " + e.getMessage());
            }
        };

        nameField.textProperty().addListener(validationListener);
        deptField.textProperty().addListener(validationListener);
        salaryField.textProperty().addListener(validationListener);
        ratingField.textProperty().addListener(validationListener);
        experienceField.textProperty().addListener(validationListener);

        // Initial validation
        validationListener.changed(null, null, null);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == actionButtonType) {
                try {
                    EmployeeValidator.validateEmployeeInput(
                            nameField.getText(),
                            deptField.getText(),
                            salaryField.getText(),
                            ratingField.getText(),
                            experienceField.getText()
                    );
                    return new Employee<>(
                            employee != null ? employee.getEmployeeId() : UUID.randomUUID(),
                            nameField.getText(),
                            deptField.getText(),
                            Double.parseDouble(salaryField.getText()),
                            Double.parseDouble(ratingField.getText()),
                            Integer.parseInt(experienceField.getText()),
                            activeCheckBox.isSelected()
                    );
                } catch (IllegalArgumentException | InvalidDepartmentException e) {
                    validationLabel.setText("Error: " + e.getMessage());
                    logger.warning("Validation failed: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        return dialog;
    }

    @FXML
    private void handleAddEmployee() {
        Dialog<Employee<UUID>> dialog = createEmployeeDialog(null, "Add Employee", "Add");
        Optional<Employee<UUID>> result = dialog.showAndWait();
        result.ifPresent(employee -> {
            employeeService.addEmployee(employee);
            if (!departmentFilterCombo.getItems().contains(employee.getDepartment())) {
                departmentFilterCombo.getItems().add(employee.getDepartment());
            }
            employeeTable.refresh();
            showStatus("Employee added successfully!", Color.web("#10b981"));
        });
    }

    @FXML
    private void handleUpdateEmployee() {
        Employee<UUID> selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showStatus("Error: Please select an employee to update", Color.web("#ef4444"));
            return;
        }

        Dialog<Employee<UUID>> dialog = createEmployeeDialog(selected, "Update Employee", "Update");
        Optional<Employee<UUID>> result = dialog.showAndWait();
        result.ifPresent(employee -> {
            try {
                employeeService.updateEmployee(employee);
            } catch (EmployeeNotFoundException e) {
                showStatus("Employee with ID: " + employee.getEmployeeId() + " not found", Color.web("#ef4444"));
                logger.warning("Validation failed: " + e.getMessage());

            }
            if (!departmentFilterCombo.getItems().contains(employee.getDepartment())) {
                departmentFilterCombo.getItems().add(employee.getDepartment());
            }
            employeeTable.refresh();
            showStatus("Employee updated successfully!", Color.web("#10b981"));
        });
    }

    @FXML
    private void handleDeleteEmployee() {
        Employee<UUID> selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showStatus("Error: Please select an employee to delete", Color.web("#ef4444"));
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Employee");
        alert.setHeaderText("Are you sure you want to delete this employee?");
        alert.setContentText("Name: " + selected.getName() + "\nID: " + selected.getEmployeeId());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            employeeService.removeEmployee(selected);
            employeeTable.getSelectionModel().clearSelection();
            showStatus("Employee deleted successfully!", Color.web("#10b981"));
        }
    }

    @FXML
    private void handleFilterByRating() {
        String ratingText = minRatingCombo.getValue();
        String departmentFilter = departmentFilterCombo.getValue();
        String searchQuery = searchField.getText().trim().toLowerCase();

        try {
            // If no rating is selected or input is blank, skip rating filter
            Double rating = null;
            if (ratingText != null && !ratingText.isEmpty()) {
                EmployeeValidator.validateRating(ratingText); // Validate e.g., range, format
                rating = Double.parseDouble(ratingText);
            }

            double finalRating = (rating != null) ? rating : Double.MIN_VALUE;
            boolean applyRatingFilter = rating != null;

            filteredEmployees.setPredicate(employee -> {
                boolean matchesRating = !applyRatingFilter || employee.getPerformanceRating() >= finalRating;
                boolean matchesDepartment = departmentFilter.equals("All") || employee.getDepartment().equalsIgnoreCase(departmentFilter);
                boolean matchesSearch = searchQuery.isEmpty()
                        || employee.getName().toLowerCase().contains(searchQuery)
                        || employee.getDepartment().toLowerCase().contains(searchQuery);

                return matchesRating && matchesDepartment && matchesSearch;
            });

            if (applyRatingFilter) {
                showStatus("Filtered employees by minimum rating: " + finalRating, Color.web("#10b981"));
            } else {
                showStatus("Rating filter cleared", Color.web("#10b981"));
            }
        } catch (IllegalArgumentException e) {
            showStatus("Error: " + e.getMessage(), Color.web("#ef4444"));
            logger.warning("Validation failed: " + e.getMessage());
        }
    }


    @FXML
    private void handleFilterBySalaryRange() {
        try {
            EmployeeValidator.validateSalaryRange(minSalaryField.getText(), maxSalaryField.getText());
            double minSalary = minSalaryField.getText().isEmpty() ? 0 : Double.parseDouble(minSalaryField.getText());
            double maxSalary = maxSalaryField.getText().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxSalaryField.getText());
            filteredEmployees.setPredicate(employee -> {
                boolean matchesSalary = employee.getSalary() >= minSalary && employee.getSalary() <= maxSalary;
                boolean matchesDepartment = departmentFilterCombo.getValue().equals("All") ||
                        employee.getDepartment().equals(departmentFilterCombo.getValue());
                boolean matchesSearch = searchField.getText().isEmpty() ||
                        employee.getName().toLowerCase().contains(searchField.getText().toLowerCase()) ||
                        employee.getDepartment().toLowerCase().contains(searchField.getText().toLowerCase());
                return matchesSalary && matchesDepartment && matchesSearch;
            });
            showStatus("Filtered employees by salary range: $" + minSalary + " - $" + maxSalary, Color.web("#10b981"));
        } catch (IllegalArgumentException e) {
            showStatus("Error: " + e.getMessage(), Color.web("#ef4444"));
            logger.warning("Validation failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleShowTop10Salaries() {
        try {
            List<Employee<UUID>> top10Employees = employeeService.getTopNHighestPaidEmployees(10);
            employeeTable.setItems(FXCollections.observableArrayList(top10Employees));
            showStatus("Showing top 10 highest-paid employees", Color.web("#10b981"));
        } catch (IllegalArgumentException e) {
            showStatus("Error: " + e.getMessage(), Color.web("#ef4444"));
            logger.warning("Validation failed: " + e.getMessage());
        }
    }

    @FXML
    private void openPerformanceRaiseDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Apply Performance Raise");
        dialog.setHeaderText("Enter criteria for performance-based salary raise");

        ButtonType applyButtonType = new ButtonType("Apply", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(applyButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        grid.setStyle("-fx-font-family: 'Roboto'; -fx-font-size: 13px;");

        TextField ratingField = new TextField();
        ratingField.setPromptText("Min Rating");
        ratingField.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cbd5e1; -fx-border-radius: 6px; -fx-font-size: 13px; -fx-focus-color: #3b82f6;");
        TextField percentageField = new TextField();
        percentageField.setPromptText("Percentage Raise");
        percentageField.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cbd5e1; -fx-border-radius: 6px; -fx-font-size: 13px; -fx-focus-color: #3b82f6;");

        Label ratingLabel = new Label("Minimum Rating:");
        ratingLabel.setStyle("-fx-font-weight: 500; -fx-text-fill: #1e293b;");
        grid.add(ratingLabel, 0, 0);
        grid.add(ratingField, 1, 0);

        Label percentageLabel = new Label("Percentage Raise:");
        percentageLabel.setStyle("-fx-font-weight: 500; -fx-text-fill: #1e293b;");
        grid.add(percentageLabel, 0, 1);
        grid.add(percentageField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Button applyButton = (Button) dialog.getDialogPane().lookupButton(applyButtonType);
        applyButton.setDisable(true);

        ChangeListener<String> validationListener = (obs, oldVal, newVal) -> {
            try {
                EmployeeValidator.validatePerformanceRaise(ratingField.getText(), percentageField.getText());
                applyButton.setDisable(false);
            } catch (IllegalArgumentException e) {
                applyButton.setDisable(true);
            }
        };
        ratingField.textProperty().addListener(validationListener);
        percentageField.textProperty().addListener(validationListener);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == applyButtonType) {
                try {
                    double rating = Double.parseDouble(ratingField.getText());
                    double percentage = Double.parseDouble(percentageField.getText());
                    employeeService.applyPerformanceRaise(rating, percentage);
                    employeeTable.refresh();
                    showStatus("Applied " + percentage + "% raise for employees with rating ≥ " + rating, Color.web("#10b981"));
                } catch (IllegalArgumentException e) {
                    showStatus("Error: " + e.getMessage(), Color.web("#ef4444"));
                    logger.warning("Validation failed: " + e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    @FXML
    private void handleResetFilters() {
        searchField.clear();
        departmentFilterCombo.setValue("All");
        minRatingCombo.setValue(null);
        minSalaryField.clear();
        maxSalaryField.clear();
        filteredEmployees.setPredicate(employee -> true);
        applySorting();
        employeeTable.refresh();
        showStatus("Filters reset successfully!", Color.web("#10b981"));
    }

    private void handleTableSelection() {
        // No fields to update since Add Employee is a popup
    }

    private void showStatus(String message, Color color) {
        Label statusLabel = new Label(message);
        statusLabel.setTextFill(color);
        statusLabel.setStyle("-fx-font-weight: 500; -fx-padding: 10px; -fx-background-color: #ffffff; -fx-border-radius: 6px; -fx-font-size: 14px; -fx-border-color: #e2e8f0; -fx-border-width: 1px;");
        VBox.setMargin(statusLabel, new Insets(0, 0, 12, 0));

        VBox root = (VBox) employeeTable.getParent();
        root.getChildren().add(0, statusLabel);

        PauseTransition delay = new PauseTransition(Duration.seconds(2.5));
        delay.setOnFinished(event -> root.getChildren().remove(statusLabel));
        delay.play();
    }
}