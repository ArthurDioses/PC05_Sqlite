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
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class SearchEditActivity extends AppCompatActivity {

    private TextInputLayout tilCodeUpn, tilDni, tilNameLastname;
    private TextInputEditText tiedtCodeUpn, tiedtDni, tiedtNameLastName;
    private RadioButton radioButtonMan, radioButtonWoman;
    private CheckBox checkBoxCSharp, checkBoxCPlusPlus, checkBoxJava;
    private Button btnToggleCode, btnToggleDni;
    private Button btnSearch, btnEdit, btnUpdate, btnRemove;
    private ImageButton btnReturn;

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

        btnSearch.setOnClickListener(view -> searchByCode(getInputCodeUpn()));

        btnEdit.setOnClickListener(view -> editFields());
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
            tiedtCodeUpn.setFocusable(true);
            tilDni.setEnabled(false);

        } else {
            //Por Dni
            tilCodeUpn.setEnabled(false);
            tilDni.setEnabled(true);
            tiedtDni.setFocusable(true);

        }
        tilNameLastname.setEnabled(false);
        radioButtonMan.setEnabled(false);
        radioButtonWoman.setEnabled(false);
        checkBoxCSharp.setEnabled(false);
        checkBoxCPlusPlus.setEnabled(false);
        checkBoxJava.setEnabled(false);
    }

    public void searchByCode(String codeupn) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Constant.ENDPOINT_SEARCH_BY_CODE_UPN + codeupn, response -> {
            JSONObject jsonObject;
            for (int i = 0; i < response.length(); i++) {
                try {
                    jsonObject = response.getJSONObject(i);
                    tiedtCodeUpn.setText(jsonObject.getString("codigoupn"));
                    tiedtDni.setText(jsonObject.getString("dni"));
                    tiedtNameLastName.setText(jsonObject.getString("nombre"));
                    String dataGender = jsonObject.getString("genero");
                    if (dataGender.equals("0")) {
                        radioButtonMan.setChecked(true);
                    }
                    if (dataGender.equals("1")) {
                        radioButtonWoman.setChecked(true);
                    }

                    renderCheckBox(jsonObject.getString("curso"));
                    btnEdit.setVisibility(View.VISIBLE);
                    btnRemove.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    Toast.makeText(SearchEditActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, error -> Toast.makeText(SearchEditActivity.this, "ERRROR DE CONEXIÓN", Toast.LENGTH_SHORT).show());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    public void editFields() {
        tilCodeUpn.setEnabled(true);
        tilDni.setEnabled(true);
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

    /**
     * IMPLEMENT WITH WEBSERVICE
     * public void modify(View view) {
     * if (isValidaFields()) {
     * AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "upn", null, 1);
     * SQLiteDatabase bd = admin.getWritableDatabase();
     * <p>
     * ContentValues register = new ContentValues();
     * register.put("codigoupn", getInputCodeUpn());
     * register.put("dni", getInputDNI());
     * register.put("nombre", getInputNameLastName());
     * register.put("genero", getSelectGender());//(0:Hombre, 1:Mujer)
     * register.put("curso", getCourses());//(0,0,0) c#,c++,java -> 0:no, 1:si
     * int cant = bd.update("matricula", register, "codigoupn='" + getInputCodeUpn() + "'", null);
     * bd.close();
     * if (cant == 1) {
     * Toast.makeText(this, "Se modificaron los datos", Toast.LENGTH_SHORT).show();
     * } else {
     * Toast.makeText(this, "No existe articulo con el código ingresado", Toast.LENGTH_SHORT).show();
     * }
     * }
     * }
     * <p>
     * public void deleteByCode(View view) {
     * AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "upn", null, 1);
     * SQLiteDatabase bd = admin.getWritableDatabase();
     * bd.delete("matricula", "codigoupn='" + getInputCodeUpn() + "'", null);
     * bd.close();
     * cleanView();
     * Toast.makeText(this, "Se eliminó la matrícula", Toast.LENGTH_SHORT).show();
     * <p>
     * }
     */
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
        tiedtCodeUpn.setFocusable(true);
        tiedtDni.setText("");
        tiedtNameLastName.setText("");

        radioButtonMan.setChecked(false);
        radioButtonWoman.setChecked(false);

        checkBoxCSharp.setChecked(false);
        checkBoxCPlusPlus.setChecked(false);
        checkBoxJava.setChecked(false);
        btnSearch.setVisibility(View.VISIBLE);
        btnEdit.setVisibility(View.GONE);
        btnUpdate.setVisibility(View.GONE);
        btnRemove.setVisibility(View.GONE);
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