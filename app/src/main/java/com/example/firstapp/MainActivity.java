package com.example.firstapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText RegNo;
    Button submitBTN;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RegNo = findViewById(R.id.RegNo);
        submitBTN = findViewById(R.id.submitBTN);
        tv = findViewById(R.id.tv);

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idNumber = RegNo.getText().toString();

                if(idNumber.length() >= 2) {

                    String admissionYear = idNumber.substring(0 , 2);

                    tv.setText("Your registration Number is :- " + idNumber +
                            "\nYour year of admission is :- 20" + admissionYear);
                }
                else{
                    tv.setText("Please enter valid Registration Number");
                }
            }
        });
    }
}