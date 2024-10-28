package com.example.universalyoga_management;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.universalyoga_management.db.YogaClassDatabaseHelper;
import com.example.universalyoga_management.models.ClassInstance;
import adapter.InstanceAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class ManageClassInstancesActivity extends AppCompatActivity {

    private YogaClassDatabaseHelper dbHelper;
    private long selectedClassId;

    private EditText instanceDateInput, teacherInput, commentsInput;
    private Button saveInstanceButton;
    private ListView instanceRecyclerView;

    private ArrayList<ClassInstance> instanceList;
    private InstanceAdapter instanceAdapter; // Adapter for displaying instances in the ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_instances);

        dbHelper = new YogaClassDatabaseHelper(this);

        // Get selected class ID passed from the previous activity
        selectedClassId = getIntent().getLongExtra("CLASS_ID", -1);

        // Initialize views
        instanceDateInput = findViewById(R.id.instanceDateInput);
        teacherInput = findViewById(R.id.teacherInput);
        commentsInput = findViewById(R.id.commentsInput);
        saveInstanceButton = findViewById(R.id.saveInstanceButton);
        saveInstanceButton = findViewById(R.id.saveInstanceButton);
        instanceRecyclerView = findViewById(R.id.instanceRecyclerView);

        // Disable manual input for the date
        instanceDateInput.setInputType(InputType.TYPE_NULL);

        // Set DatePicker for instance date
        instanceDateInput.setOnClickListener(v -> showDatePicker());

        // Set the save button's click listener to save the class instance
        saveInstanceButton.setOnClickListener(v -> saveClassInstance());

        // Load existing instances for the selected class
        loadInstancesForClass();
    }

    // Method to show DatePickerDialog for instance date
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String formattedDate = String.format("%02d/%02d/%d", month1 + 1, dayOfMonth, year1);
            instanceDateInput.setText(formattedDate); // Set the formatted date
        }, year, month, day);

        datePickerDialog.show();
    }

    // Method to save a class instance
    private void saveClassInstance() {
        // Get values from input fields
        String instanceDate = instanceDateInput.getText().toString().trim();
        String teacher = teacherInput.getText().toString().trim();
        String comments = commentsInput.getText().toString().trim();

        // Validate required fields
        if (instanceDate.isEmpty() || teacher.isEmpty()) {
            Toast.makeText(this, "Please fill in required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert the class instance into the database
        boolean isInserted = dbHelper.insertClassInstance(selectedClassId, instanceDate, teacher, comments);

        if (isInserted) {
            Toast.makeText(this, "Class instance added successfully", Toast.LENGTH_SHORT).show();
            clearFields();
            loadInstancesForClass(); // Reload the list of instances
        } else {
            Toast.makeText(this, "Failed to add class instance", Toast.LENGTH_SHORT).show();
        }
    }

    // Load all class instances for the selected class
    private void loadInstancesForClass() {
        // Load the class instances from the database for the selected class
        instanceList = dbHelper.getClassInstances(selectedClassId); // Implement this in DB helper
        instanceAdapter = new InstanceAdapter(this, instanceList);
        instanceRecyclerView.setAdapter(instanceAdapter);
    }

    // Clear input fields after saving
    private void clearFields() {
        instanceDateInput.setText("");
        teacherInput.setText("");
        commentsInput.setText("");
    }
}
