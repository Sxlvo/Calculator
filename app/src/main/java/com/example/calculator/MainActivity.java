package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private TextView tvSecondaryView;
    private TextView tvPrincipalView;
    private boolean firstDigit = true;
    private boolean operator = false;

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

        tvSecondaryView = findViewById(R.id.tvSecondaryView);
        tvPrincipalView = findViewById(R.id.tvPrincipalView);
    }

    public void numberPressed(View v) {
    }

    public void operatorPressed(View v) {
    }

    public void equalPressed(View v) {
    }

    public Float calcola(String input) {
        String[] parts = input.split(" ");
        Float num1 = Float.parseFloat(parts[0]);
        String op = parts[1];
        Float num2 = Float.parseFloat(parts[2]);

        if (num2 == null) {
            return num1;
        }

        switch (op) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
            case "x":
                return num1 * num2;
            case "/":
            case "÷":
                return num1 / num2;
            default:
                throw new IllegalArgumentException("Operator not valid: " + op);
        }
    }


    public void goBackPressed(View v) {
    }

    public void acPresse(View v){
    }

    public void commaPressed(View v){

    }
}