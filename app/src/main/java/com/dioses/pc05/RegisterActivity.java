package com.dioses.pc05;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout tilCodeUpn, tilDni, tilNameLastname;
    private TextInputEditText tiedtCodeUpn, tiedtDni, tiedtNameLastName;
    private RadioButton radioButtonMan, radioButtonWoman;
    private CheckBox checkBoxCSharp, checkBoxCPlusPlus, checkBoxJava;
    private Button btnRegister;
    private ImageButton btnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //TextInputLayout match
        tilCodeUpn = findViewById(R.id.til_code_upn);
        tilDni = findViewById(R.id.til_dni);
        tilNameLastname = findViewById(R.id.til_name_lastname);

        //TextInputEditText match
        tiedtCodeUpn = findViewById(R.id.tiedt_code_upn);
        tiedtDni = findViewById(R.id.tiedt_dni);
        tiedtNameLastName = findViewById(R.id.tiedt_name_lastname);

        //RadioButton match
        radioButtonMan = findViewById(R.id.radio_button_man);
        radioButtonWoman = findViewById(R.id.radio_button_woman);

        //Checkbox match
        checkBoxCSharp = findViewById(R.id.check_box_c_sharp);
        checkBoxCPlusPlus = findViewById(R.id.check_box_c_plus_plus);
        checkBoxJava = findViewById(R.id.check_box_java);

        //Button match
        btnRegister = findViewById(R.id.btn_register);

        //ImageButton match
        btnReturn = findViewById(R.id.btn_return);

        listeners();
    }

    private void listeners() {
        tiedtCodeUpn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 1) {
                    cleanErrorField(tilCodeUpn);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 9) {
                    tiedtDni.setFocusable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        tiedtDni.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 1) {
                    cleanErrorField(tilDni);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 8) {
                    tiedtNameLastName.setFocusable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tiedtNameLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 1) {
                    cleanErrorField(tilDni);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnReturn.setOnClickListener(view -> this.finish());

        btnRegister.setOnClickListener(
                view -> register(Constant.ENDPOINT_REGISTER));
    }

    public void register(String url) {
        if (isValidaFields()) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        cleanView();
                        Toast.makeText(this, "Operación exitosa", Toast.LENGTH_SHORT).show();
                    },
                    error -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("codigoupn", getInputCodeUpn());
                    parametros.put("dni", getInputDNI());
                    parametros.put("nombre", getInputNameLastName());
                    parametros.put("genero", String.valueOf(getSelectGender()));
                    parametros.put("curso", getCourses());
                    return parametros;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    private String getCourses() {
        char cSharp = '0';
        char cPlusPlus = '0';
        char java = '0';
        if (checkBoxCSharp.isChecked()) {
            cSharp = '1';
        }
        if (checkBoxCPlusPlus.isChecked()) {
            cPlusPlus = '1';
        }
        if (checkBoxJava.isChecked()) {
            java = '1';
        }
        return cSharp + "-" + cPlusPlus + "-" + java;
    }

    private void cleanView() {
        tiedtCodeUpn.setText("");
        tiedtCodeUpn.setFocusable(true);
        tiedtDni.setText("");
        tiedtNameLastName.setText("");

        radioButtonMan.setChecked(false);
        radioButtonWoman.setChecked(false);

        checkBoxCSharp.setChecked(false);
        checkBoxCPlusPlus.setChecked(false);
        checkBoxJava.setChecked(false);

    }

    private int getSelectGender() {
        return radioButtonMan.isChecked() ? 0 : 1;
    }

    private boolean isValidaFields() {
        boolean isCorrect = true;
        if (TextUtils.isEmpty(getInputCodeUpn().trim())) {
            setErrorField(tilCodeUpn, "Campo requerido");
            isCorrect = false;
        } else if (tiedtCodeUpn.length() < 9) {
            setErrorField(tilCodeUpn, "Ingrese correctamente su Código");
            isCorrect = false;
        } else {
            cleanErrorField(tilCodeUpn);
        }
        if (TextUtils.isEmpty(getInputDNI().trim())) {
            setErrorField(tilDni, "Campo requerido");
            isCorrect = false;
        } else if (getInputDNI().length() < 8) {
            setErrorField(tilDni, "Ingrese correctamente su DNI");
            isCorrect = false;
        } else {
            cleanErrorField(tilDni);
        }
        if (TextUtils.isEmpty(getInputNameLastName().trim())) {
            setErrorField(tilNameLastname, "Campo requerido");
            isCorrect = false;
        } else {
            cleanErrorField(tilNameLastname);
        }
        if (!radioButtonMan.isChecked() && !radioButtonWoman.isChecked()) {
            Toast.makeText(this, "Debes selecciona un género", Toast.LENGTH_SHORT).show();
            isCorrect = false;
        }
        if (!checkBoxCSharp.isChecked() && !checkBoxCPlusPlus.isChecked() && !checkBoxJava.isChecked()) {
            Toast.makeText(this, "Debes selecciona un curso", Toast.LENGTH_SHORT).show();
            isCorrect = false;
        }
        return isCorrect;
    }

    private String getInputCodeUpn() {
        return Objects.requireNonNull(tiedtCodeUpn.getText()).toString();
    }

    private String getInputDNI() {
        return Objects.requireNonNull(tiedtDni.getText()).toString();
    }

    private String getInputNameLastName() {
        return Objects.requireNonNull(tiedtNameLastName.getText()).toString();
    }


    private void setErrorField(TextInputLayout textInputLayout, String messaerror) {
        textInputLayout.setErrorEnabled(false);
        textInputLayout.setError(messaerror);
    }

    private void cleanErrorField(TextInputLayout textInputLayout) {
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(null);
    }
}