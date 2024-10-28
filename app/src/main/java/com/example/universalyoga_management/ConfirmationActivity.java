package com.example.universalyoga_management;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation_activity);

        // Get the passed data from Intent
        Intent intent = getIntent();
        String classDay = intent.getStringExtra("classDay");
        String classTime = intent.getStringExtra("classTime");
        String classCapacity = intent.getStringExtra("classCapacity");
        String classDuration = intent.getStringExtra("classDuration");
        String classPrice = intent.getStringExtra("classPrice");
        String classType = intent.getStringExtra("classType");
        String classDescription = intent.getStringExtra("classDescription");

        // Set the data in the confirmation TextViews
        ((TextView) findViewById(R.id.classDayConfirmation)).setText("Day: " + classDay);
        ((TextView) findViewById(R.id.classTimeConfirmation)).setText("Time: " + classTime);
        ((TextView) findViewById(R.id.classCapacityConfirmation)).setText("Capacity: " + classCapacity);
        ((TextView) findViewById(R.id.classDurationConfirmation)).setText("Duration: " + classDuration);
        ((TextView) findViewById(R.id.classPriceConfirmation)).setText("Price: " + classPrice);
        ((TextView) findViewById(R.id.classTypeConfirmation)).setText("Type: " + classType);
        ((TextView) findViewById(R.id.classDescriptionConfirmation)).setText("Description: " + classDescription);

        // Confirm button
        Button confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(v -> {
            // Go back to MainActivity and pass the result back for saving
            Intent resultIntent = new Intent();
            resultIntent.putExtra("confirm", true);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        // Edit button
        Button editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(v -> {
            // Allow user to go back to MainActivity to edit details
            Intent resultIntent = new Intent();
            resultIntent.putExtra("confirm", false);
            setResult(RESULT_CANCELED, resultIntent);
            finish();
        });
    }
}
