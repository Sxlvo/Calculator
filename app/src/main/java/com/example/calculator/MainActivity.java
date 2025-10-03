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

import java.security.Principal;

public class MainActivity extends AppCompatActivity {
    private TextView tvSecondaryView;
    private TextView tvPrincipalView;
    private Button AC;

    private boolean comma = false;

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
        AC = findViewById(R.id.acButton);
        tvSecondaryView = findViewById(R.id.tvSecondaryView);
        tvPrincipalView = findViewById(R.id.tvPrincipalView);
    }

    public void numberPressed(View v) {
        Button button = (Button) v;
        tvPrincipalView.append(button.getText().toString());

        String current = tvPrincipalView.getText().toString();
        if (!current.isEmpty()) {
            AC.setText("C");
        } else {
            AC.setText("AC");
        }
    }


    public void operatorPressed(View v) {
    }

    public void equalPressed(View v) {
    }



    public void goBackPressed(View v) {
        String current = tvPrincipalView.getText().toString();
        if (current.isEmpty()) {
            AC.setText("AC");
            return; // fermati subito se non c’è niente da cancellare
        }

        tvPrincipalView.setText(current.subSequence(0, current.length() - 1));

        if (tvPrincipalView.getText().length() == 0) {
            AC.setText("AC");
        }
    }


    public void acPressed(View v){
        Button btn = (Button) v;
        String current = tvPrincipalView.getText().toString();
        tvPrincipalView.setText("");
    }

    public void commaPressed(View v) {
        if (!comma) {
            String current = tvPrincipalView.getText().toString();

            if (current.isEmpty()) {
                tvPrincipalView.append("0,");
                return;
            }

            if (!current.endsWith(",") && !current.endsWith("+") && !current.endsWith("-")
                    && !current.endsWith("x") && !current.endsWith("/") && !current.endsWith("%")
                    && !current.endsWith("=")) {
                comma=true;
                tvPrincipalView.append(",");
            }
        }
    }


    public Float compute(String input) {
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
}
