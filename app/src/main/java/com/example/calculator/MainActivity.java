package com.example.calculator;

import android.annotation.SuppressLint;
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
    private Button AC;

    private boolean comma = false;
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
        AC = findViewById(R.id.acButton);
        tvSecondaryView = findViewById(R.id.tvSecondaryView);
        tvPrincipalView = findViewById(R.id.tvPrincipalView);
    }

    public void numberPressed(View v) {
        Button button = (Button) v;
        tvPrincipalView.append(button.getText().toString());

        operator = false;  // <— sblocca l’inserimento del prossimo operatore
        AC.setText(tvPrincipalView.length() > 0 ? "C" : "AC");
    }



    @SuppressLint("SetTextI18n")
    public void operatorPressed(View v) {
        Button opButton = (Button) v;
        String opTxt = opButton.getText().toString();
        String current = tvPrincipalView.getText().toString();

        // segno iniziale
        if (current.isEmpty() && opTxt.equals("-")) {
            tvPrincipalView.setText("-");
            operator = false;
            AC.setText("C");
            return;
        }

        if (operator) {
            // se l'ultimo char è già un operatore, sostituiscilo
            char last = current.charAt(current.length() - 1);
            if (last=='+'||last=='-'||last=='x'||last=='/') {
                tvPrincipalView.setText(current.substring(0, current.length()-1) + opTxt);
            }
            // rimani in stato "operator = true"
            return;
        }

        if (current.isEmpty()) {
            tvPrincipalView.setText("0" + opTxt);
            operator = true;
            comma = false;
            AC.setText("C");
            return;
        }

        char last = current.charAt(current.length()-1);
        if (last=='+'||last=='-'||last=='x'||last=='/') return;

        tvPrincipalView.append(opTxt);
        operator = true;   // ora c’è un operatore in coda
        comma = false;
        AC.setText("C");
    }



    @SuppressLint("SetTextI18n")
    public void equalPressed(View v) {
        String cur = tvPrincipalView.getText().toString();
        tvSecondaryView.setText(cur);
        try {
            Float res = compute(cur);
            tvPrincipalView.setText((res==null?"":res.toString()).replace('.', ','));
        } catch (Exception e) {
            tvPrincipalView.setText("Errore");
        }
    }





    @SuppressLint("SetTextI18n")
    public void goBackPressed(View v) {
        String current = tvPrincipalView.getText().toString();
        if (current.isEmpty()) {
            comma = false;
            operator = false;  // <— aggiungi
            AC.setText("AC");
            return;
        }

        char last = current.charAt(current.length() - 1);
        String updated = current.substring(0, current.length() - 1);
        tvPrincipalView.setText(updated);

        if (last == ',') comma = false;
        if (last == '+' || last == '-' || last == 'x' || last == '/') operator = false; // <—

        AC.setText(updated.isEmpty() ? "AC" : "C");
    }




    @SuppressLint("SetTextI18n")
    public void acPressed(View v){
        tvPrincipalView.setText("");
        tvSecondaryView.setText("");
        comma=false;
        operator=false;
        AC.setText("AC");
    }
    //doesnt work properly
    //dopo aver usato e cancellato la virgola non è possibile riutilizzarla
    public void commaPressed(View v) {
        if (!comma) {
            String current = tvPrincipalView.getText().toString();

            if (current.isEmpty()) {
                tvPrincipalView.append("0,");
                comma=true;
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
        if (input == null || input.isEmpty()) return 0f;

        // normalizza: niente spazi, × -> x, ÷ -> /
        String expr = input.replace(" ", "").replace('×','x').replace('÷','/');

        // trova il primo operatore *dopo* il primo carattere (così il '-' iniziale è segno, non operatore)
        int opIndex = -1;
        char op = 0;
        for (int i = 1; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '+' || c == '-' || c == 'x' || c == '/') {
                opIndex = i;
                op = c;
                break;
            }
        }

        // se non troviamo l'operatore, prova a fare il parse del numero (magari è solo "-5" o "12,3")
        if (opIndex == -1) {
            return Float.parseFloat(expr.replace(',', '.'));
        }

        String s1 = expr.substring(0, opIndex);
        String s2 = expr.substring(opIndex + 1);

        // se manca il secondo numero, restituisci il primo (evita crash)
        if (s2.isEmpty()) {
            return Float.parseFloat(s1.replace(',', '.'));
        }

        float n1 = Float.parseFloat(s1.replace(',', '.'));
        float n2 = Float.parseFloat(s2.replace(',', '.'));

        switch (op) {
            case '+': return n1 + n2;
            case '-': return n1 - n2;
            case 'x': return n1 * n2;
            case '/': return n2 == 0f ? Float.NaN : n1 / n2;
            default: throw new IllegalArgumentException("Operatore non valido: " + op);
        }
    }


    @SuppressLint("SetTextI18n")
    public void plusMinusPressed(View v) {
        String current = tvPrincipalView.getText().toString();
        if (current.isEmpty()) return;

        // prendo la parte prima e dopo l’ultimo operatore
        int lastOpIndex = Math.max(
                Math.max(current.lastIndexOf('+'), current.lastIndexOf('-')),
                Math.max(current.lastIndexOf('x'), current.lastIndexOf('/'))
        );

        String before, number;
        if (lastOpIndex == -1) {
            // non ci sono operatori → tutto è il numero
            before = "";
            number = current;
        } else {
            before = current.substring(0, lastOpIndex + 1);
            number = current.substring(lastOpIndex + 1);
        }

        if (number.isEmpty()) return;

        // cambia segno
        if (number.startsWith("-")) {
            number = number.substring(1);   // rimuove il meno
        } else {
            number = "-" + number;          // aggiunge il meno
        }

        tvPrincipalView.setText(before + number);
        AC.setText("C");
        String out = before + number;
        out = out.replace("+-", "-").replace("--", "+");
        tvPrincipalView.setText(out);

    }



}
