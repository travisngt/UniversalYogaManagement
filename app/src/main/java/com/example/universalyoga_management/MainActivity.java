package com.example.universalyoga_management;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.universalyoga_management.db.YogaClassDatabaseHelper;
import java.util.ArrayList;
import java.util.Calendar;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private YogaClassDatabaseHelper dbHelper;
    private EditText classDayInput, classTimeInput, classCapacityInput, classDurationInput, classPriceInput, classTypeInput, classDescriptionInput;
    private Button saveClassButton;
    private ListView classListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "onCreate: Activity created");

        // Initialize the database helper
        dbHelper = new YogaClassDatabaseHelper(this);
        Log.d("MainActivity", "Database helper instance created");

        // Initialize input fields and button
        classDayInput = findViewById(R.id.classDayInput);
        classTimeInput = findViewById(R.id.classTimeInput);
        classCapacityInput = findViewById(R.id.classCapacityInput);
        classDurationInput = findViewById(R.id.classDurationInput);
        classPriceInput = findViewById(R.id.classPriceInput);
        classTypeInput = findViewById(R.id.classTypeInput);
        classDescriptionInput = findViewById(R.id.classDescriptionInput);
        saveClassButton = findViewById(R.id.saveClassButton);

        Log.d("MainActivity", "UI components initialized");

        // Disable manual input for day and time
        classDayInput.setInputType(InputType.TYPE_NULL);
        classTimeInput.setInputType(InputType.TYPE_NULL);

        // Set DatePicker for Class Day
        classDayInput.setOnClickListener(v -> showDatePicker());
        Log.d("MainActivity", "Date picker listener set for classDayInput");

        // Set TimePicker for Class Time (AM/PM format)
        classTimeInput.setOnClickListener(v -> showTimePicker());
        Log.d("MainActivity", "Time picker listener set for classTimeInput");

        // Set the button's click listener to save the class
        saveClassButton.setOnClickListener(v -> validateAndConfirmInput());
        Log.d("MainActivity", "Save class button listener set");
    }

    // Method to show DatePickerDialog for class day (MM/DD/YYYY format)
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String formattedDate = String.format("%02d/%02d/%d", month1 + 1, dayOfMonth, year1);
            classDayInput.setText(formattedDate);
            Log.d("MainActivity", "Selected date: " + formattedDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    // Method to show TimePickerDialog for class time (AM/PM format)
    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            String amPm = (hourOfDay >= 12) ? "PM" : "AM";
            int displayHour = (hourOfDay == 0) ? 12 : (hourOfDay > 12 ? hourOfDay - 12 : hourOfDay);
            String formattedTime = String.format("%02d:%02d %s", displayHour, minute1, amPm);
            classTimeInput.setText(formattedTime);
            Log.d("MainActivity", "Selected time: " + formattedTime);
        }, hour, minute, false); // Use 12-hour format

        timePickerDialog.show();
    }

    // Method to validate input and then show confirmation dialog
    private void validateAndConfirmInput() {
        // Get values from input fields
        String classDay = classDayInput.getText().toString().trim();
        String classTime = classTimeInput.getText().toString().trim();
        String classCapacity = classCapacityInput.getText().toString().trim();
        String classDuration = classDurationInput.getText().toString().trim();
        String classPrice = classPriceInput.getText().toString().trim();
        String classType = classTypeInput.getText().toString().trim();
        String classDescription = classDescriptionInput.getText().toString().trim();

        boolean hasError = false;

        // Validate class day
        if (classDay.isEmpty()) {
            classDayInput.setError("Please select a class day");
            hasError = true;
        } else {
            classDayInput.setError(null);  // Clear any previous error
        }

        // Validate class time
        if (classTime.isEmpty()) {
            classTimeInput.setError("Please select a class time");
            hasError = true;
        } else {
            classTimeInput.setError(null);
        }

        // Validate class capacity
        if (classCapacity.isEmpty()) {
            classCapacityInput.setError("Please enter the class capacity");
            hasError = true;
        } else if (!isValidNumber(classCapacity)) {
            classCapacityInput.setError("Capacity must be a valid number");
            hasError = true;
        } else {
            classCapacityInput.setError(null);
        }

        // Validate class duration
        if (classDuration.isEmpty()) {
            classDurationInput.setError("Please enter the class duration");
            hasError = true;
        } else if (!isValidNumber(classDuration)) {
            classDurationInput.setError("Duration must be a valid number");
            hasError = true;
        } else {
            classDurationInput.setError(null);
        }

        // Validate class price
        if (classPrice.isEmpty()) {
            classPriceInput.setError("Please enter the class price");
            hasError = true;
        } else if (!isValidNumber(classPrice)) {
            classPriceInput.setError("Price must be a valid number");
            hasError = true;
        } else {
            classPriceInput.setError(null);
        }

        // Validate class type
        if (classType.isEmpty()) {
            classTypeInput.setError("Please enter the class type");
            hasError = true;
        } else {
            classTypeInput.setError(null);
        }

        // If there are errors, return early
        if (hasError) {
            Toast.makeText(this, "Please fix the errors in the form", Toast.LENGTH_SHORT).show();
            return;
        }

        // If validation passes, show confirmation dialog
        showConfirmationDialog(classDay, classTime, classCapacity, classDuration, classPrice, classType, classDescription);
    }

    // Method to show a confirmation dialog with entered details
    private void showConfirmationDialog(String day, String time, String capacity, String duration, String price, String type, String description) {
        String confirmationMessage = "Please confirm the following details:\n\n" +
                "Class Day: " + day + "\n" +
                "Class Time: " + time + "\n" +
                "Class Capacity: " + capacity + "\n" +
                "Class Duration: " + duration + " minutes\n" +
                "Class Price: $" + price + "\n" +
                "Class Type: " + type + "\n" +
                "Description: " + description;

        new AlertDialog.Builder(this)
                .setTitle("Confirm Class Details")
                .setMessage(confirmationMessage)
                .setPositiveButton("Confirm", (dialog, which) -> saveClassToDatabase(day, time, capacity, duration, price, type, description))
                .setNegativeButton("Edit", null)
                .show();
    }

    // Method to save class to the database
    private void saveClassToDatabase(String day, String time, String capacity, String duration, String price, String type, String description) {
        boolean isInserted = dbHelper.insertYogaClass(day, time, capacity, duration, price, type, description);
        Log.d("MainActivity", "Attempting to insert class: " + (isInserted ? "Success" : "Failure"));

        if (isInserted) {
            Toast.makeText(this, "Class added successfully", Toast.LENGTH_SHORT).show();
            clearFields(); // Clear input fields after successful insertion
        } else {
            Toast.makeText(this, "Failed to add class", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to validate numeric input
    private boolean isValidNumber(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Helper method to clear input fields
    private void clearFields() {
        classDayInput.setText("");
        classTimeInput.setText("");
        classCapacityInput.setText("");
        classDurationInput.setText("");
        classPriceInput.setText("");
        classTypeInput.setText("");
        classDescriptionInput.setText("");
    }
}
