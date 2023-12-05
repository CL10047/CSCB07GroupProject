package com.example.cscb07groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class PostQualifications extends AppCompatActivity {

    //Creating needed variable
    public Spinner a48_spinner;
    public Spinner a67_spinner;
    public Spinner a22_spinner;
    public Spinner a37_spinner;
    public Spinner a31_spinner;
    public double[] courseGPA = new double[5]; //Store the possible gpa from grades ranges
    ArrayAdapter<String> spinner_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_qualifications);
        //set all textViews
        Button calculateButton = findViewById(R.id.check_qual_button);
        Button back = findViewById(R.id.back_btn);
        TextView gpaText = findViewById(R.id.gpa_total);
        TextView a48_low = findViewById(R.id.a48_low);
        TextView gpa_low = findViewById(R.id.gpa_low);
        TextView a67_low = findViewById(R.id.a67_a22_a37_low);
        TextView all_good = findViewById(R.id.all_good);
        //initialize all the spinners
        a48_spinner = findViewById(R.id.a48_grade_spinner);
        a67_spinner = findViewById(R.id.a67_grade_spinner);
        a22_spinner = findViewById(R.id.a22_grade_spinner);
        a37_spinner = findViewById(R.id.a37_grade_spinner);
        a31_spinner = findViewById(R.id.a31_grade_spinner);

        //Back to HomePage button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostQualifications.this, StudentActivity.class);
                startActivity(intent);
            }
        });

        // get all the entries from strings.xml
        String[] spinnerItems = getResources().getStringArray(R.array.grades_array);
        //make an adapter
        spinner_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);
        // Specify the layout to use when the list of choices appears
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinners
        a48_spinner.setAdapter(spinner_adapter);
        a67_spinner.setAdapter(spinner_adapter);
        a22_spinner.setAdapter(spinner_adapter);
        a37_spinner.setAdapter(spinner_adapter);
        a31_spinner.setAdapter(spinner_adapter);

        //set the listener for the spinner
        a48_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //INDEX 0
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onSelectedGrades(parent, view, position, id,0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing here
            }
        });
        a67_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //INDEX 1
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onSelectedGrades(parent, view, position, id,1);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing here
            }
        });
        a22_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //INDEX 2
                onSelectedGrades(parent, view, position, id,2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing here
            }
        });
        a37_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //INDEX 3
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onSelectedGrades(parent, view, position, id,3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing here
            }
        });
        a31_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // INDEX 4
                onSelectedGrades(parent, view, position, id,4);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing here
            }
        });

        calculateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Calculate GPA
                double average = 0.0;
                int total = 5;
                boolean a67condition = false;
                boolean a48condition = false;
                boolean gpacondition = false;
                boolean allgoodcondition = false;
                for (double gpa : courseGPA) {
                    if (gpa == -1.0) { //Means that student did not select a grade
                        showPopup();
                        return;
                    }
                    if (gpa == -2.0) {
                        //when the class has not been attempted -> gpa gets calculate without it
                        total -= 1;
                    } else {
                        average += gpa;
                    }

                }
                if (total == 0) {
                    average = 0;
                } else {
                    average = Math.round((average / total) * 100.0) / 100.0;
                }
                gpaText.setText("GPA: " + average);
                //Check GPA >= 2.5
                //if not message will be shown
                if (average < 2.5) {
                    gpacondition = true;
                }
                gpa_low.setVisibility(gpacondition ? View.VISIBLE : View.GONE);
                //Check grade_A48 >= 73
                //if not message will be shown
                if (courseGPA[0] < 3.0) {
                    a48condition = true;
                }
                a48_low.setVisibility(a48condition ? View.VISIBLE : View.GONE);
                //Check at least 2 of a67 a22 and a37 >= 60
                //if not message will be shown
                if (((courseGPA[1] < 1.7) && (courseGPA[2] < 1.7)) || ((courseGPA[1] < 1.7) &&
                        (courseGPA[3] < 1.7)) || ((courseGPA[2] < 1.7) && (courseGPA[3] < 1.7))) {
                    a67condition = true;
                }

                a67_low.setVisibility(a67condition ? View.VISIBLE : View.GONE);
                if (!a67condition && !a48condition && !gpacondition) {
                    allgoodcondition = true;
                }
                all_good.setVisibility(allgoodcondition ? View.VISIBLE : View.GONE);
            }
        });
    }
    // Handles on selected items actions that are common to all spinners
    private void onSelectedGrades(AdapterView<?> parent, View view, int position, long id,
                                  int index){
        // looks at the selected grade and returns the corresponding gpa
        // --> Gpa according to https://www.utsc.utoronto.ca/registrar/u-t-grading-scheme
        String selectedItem = parent.getItemAtPosition(position).toString();
        int i = 0;
        double[] gpaGrades = {-1.0,4.0,3.7,3.3,3.0,2.7,2.3,2.0,1.7,1.3,1.0,0.7,0.0,-2.0};
        while (!selectedItem.equalsIgnoreCase(parent.getItemAtPosition(i).toString())){
            //will never be out of range b/c for this function to be called you need to select
            //an item in the spinner
            i+=1;
        }
        courseGPA[index] = gpaGrades[i];
    }
    private void showPopup() {
        // Create an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the title and message
        builder.setTitle("Missing Grade(s)");
        builder.setMessage("Please select a grade for ALL classes.\nIf you haven't attempted " +
                "a class yet,please select \"Class not attempted\" \n(gpa will be calculated " +
                "without the class)");
        // Set dismiss button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}