package com.dioses.pc05;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SearchEditActivity extends AppCompatActivity {

    private TextInputLayout tilCodeUpn, tilDni, tilNameLastname;
    private TextInputEditText tiedtCodeUpn, tiedtDni, tiedtNameLastName;
    private RadioButton radioButtonMan, radioButtonWoman;
    private CheckBox checkBoxCSharp, checkBoxCPlusPlus, checkBoxJava;
    private Button btnToggleCode, btnToggleDni;
    private Button btnSearch, btnEdit, btnUpdate, btnRemove;
    private ImageButton btnReturn;
    private ProgressBar progressBarLoading;
    private RadioGroup radioGroupGender;

    private int optionSelected = 0;//0:Por código | 1:Por Dni

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_edit);

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

        //ButtonToggle match
        btnToggleCode = findViewById(R.id.btn_toggle_code);
        btnToggleDni = findViewById(R.id.btn_toggle_dni);

        //Button match
        btnSearch = findViewById(R.id.btn_search);
        btnEdit = findViewById(R.id.btn_edit);
        btnUpdate = findViewById(R.id.btn_update);
        btnRemove = findViewById(R.id.btn_remove);

        btnReturn = findViewById(R.id.btn_return);

        //ProgressBar match
        progressBarLoading = findViewById(R.id.progress_loading);

        //RadioGroup match
        radioGroupGender = findViewById(R.id.radioGroup);

        listeners();
        setToggle(1);
    }

    private void listeners() {
        btnToggleCode.setOnClickListener(view ->
                setToggle(1));
        btnToggleDni.setOnClickListener(view ->
                setToggle(2));
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
                    tiedtDni.requestFocus();
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
                    tiedtNameLastName.requestFocus();
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

        btnSearch.setOnClickListener(view -> {
            if (optionSelected == 0) {
                searchBy(Constant.ENDPOINT_SEARCH_BY_CODE_UPN + getInputCodeUpn());
                tiedtCodeUpn.requestFocus(9);
            } else {
                searchBy(Constant.ENDPOINT_SEARCH_BY_DNI + getInputDNI());
                tiedtDni.requestFocus(8);
            }
        });

        btnEdit.setOnClickListener(view -> editFields());

        btnUpdate.setOnClickListener(view -> modify(Constant.ENDPOINT_UPDATE));

        btnRemove.setOnClickListener(view -> deleteByCode(Constant.ENDPOINT_DELETE));
    }

    private void setToggle(int i) {
        cleanView();
        switch (i) {
            case 1:
                btnToggleDni.setEnabled(true);
                btnToggleCode.setEnabled(false);
                //0:Por código | 1:Por Dni
                optionSelected = 0;
                disableAll(optionSelected);
                break;
            case 2:
                btnToggleDni.setEnabled(false);
                btnToggleCode.setEnabled(true);
                optionSelected = 1;
                disableAll(optionSelected);
                break;
            default:
        }
    }

    private void disableAll(int option) {
        if (option == 0) {
            //Por código
            tilCodeUpn.setEnabled(true);
            tiedtCodeUpn.requestFocus();
            tilDni.setEnabled(false);

        } else {
            //Por Dni
            tilCodeUpn.setEnabled(false);
            tilDni.setEnabled(true);
            tiedtDni.requestFocus();

        }
        tilNameLastname.setEnabled(false);
        radioButtonMan.setEnabled(false);
        radioButtonWoman.setEnabled(false);
        checkBoxCSharp.setEnabled(false);
        checkBoxCPlusPlus.setEnabled(false);
        checkBoxJava.setEnabled(false);
    }

    public void searchBy(String endpoint) {
        showLoading();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(endpoint, response -> {
            JSONObject jsonObject;
            for (int i = 0; i < response.length(); i++) {
                try {
                    jsonObject = response.getJSONObject(i);
                    tiedtCodeUpn.setText(jsonObject.getString("codigoupn"));
                    tiedtDni.setText(jsonObject.getString("dni"));
                    tiedtNameLastName.setText(jsonObject.getString("nombre"));
                    String dataGender = jsonObject.getString("genero");

                    radioGroupGender.check(dataGender.equals("0") ? R.id.radio_button_man : R.id.radio_button_woman);

                    renderCheckBox(jsonObject.getString("curso"));
                    btnEdit.setVisibility(View.VISIBLE);
                    btnRemove.setVisibility(View.VISIBLE);
                    hideLoading();
                } catch (JSONException e) {
                    Toast.makeText(SearchEditActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    cleanView();
                    hideLoading();
                }
            }
        }, error -> {
            Toast.makeText(SearchEditActivity.this, "ERRROR DE CONEXIÓN", Toast.LENGTH_SHORT).show();
            cleanView();
            hideLoading();
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    public void editFields() {
        tilCodeUpn.setEnabled(false);
        tilDni.setEnabled(true);
        tiedtDni.requestFocus(9);
        tilNameLastname.setEnabled(true);
        radioButtonMan.setEnabled(true);
        radioButtonWoman.setEnabled(true);
        checkBoxCSharp.setEnabled(true);
        checkBoxCPlusPlus.setEnabled(true);
        checkBoxJava.setEnabled(true);

        btnSearch.setVisibility(View.GONE);
        btnEdit.setVisibility(View.GONE);
        btnUpdate.setVisibility(View.VISIBLE);
        btnRemove.setVisibility(View.GONE);
    }

    public void modify(String endpoint) {
        if (isValidaFields()) {
            showLoading();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, endpoint,
                    response -> {
                        cleanView();
                        hideLoading();
                        Toast.makeText(this, "Operación exitosa", Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                        hideLoading();
                        Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();

                    }) {
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

    public void deleteByCode(String endpoint) {
        showLoading();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, endpoint,
                response -> {
                    cleanView();
                    Toast.makeText(this, "LA MATRÍCULA FUE ELIMINADA", Toast.LENGTH_SHORT).show();
                    hideLoading();
                },
                error -> {
                    Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
                    hideLoading();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("codigoupn", getInputCodeUpn());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void renderCheckBox(String string) {
        String[] nameSplit = string.split("-");
        checkBoxCSharp.setChecked(isCheckedSelected(nameSplit[0]));
        checkBoxCPlusPlus.setChecked(isCheckedSelected(nameSplit[1]));
        checkBoxJava.setChecked(isCheckedSelected(nameSplit[2]));
    }

    private boolean isCheckedSelected(String data) {
        return !data.equals("0");
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
        tiedtCodeUpn.requestFocus();
        tiedtDni.setText("");
        tiedtNameLastName.setText("");

        radioGroupGender.clearCheck();

        checkBoxCSharp.setChecked(false);
        checkBoxCPlusPlus.setChecked(false);
        checkBoxJava.setChecked(false);
        btnSearch.setVisibility(View.VISIBLE);
        btnEdit.setVisibility(View.GONE);
        btnUpdate.setVisibility(View.GONE);
        btnRemove.setVisibility(View.GONE);
        disableAll(optionSelected);
    }

    private int getSelectGender() {
        return radioGroupGender.getCheckedRadioButtonId() == R.id.radio_button_man ? 0 : 1;
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

    private void showLoading() {
        progressBarLoading.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBarLoading.setVisibility(View.GONE);
    }
}