package ec.aj.com.recibealerta.main;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import ec.aj.com.recibealerta.R;
import ec.aj.com.recibealerta.service.ConectRest;


public class LogIn extends AppCompatActivity {

    protected EditText usu;
    EditText con;
    Button ingreso;
    SharedPreferences prefs ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        cargar();
        validarRegistro();
    }

    private void validarRegistro() {
        Boolean yourLocked = prefs.getBoolean("register", false);
        if(yourLocked){
            abrirMenu();
        }
    }

    public void cargar(){

        usu = (EditText) findViewById(R.id.input_email);
        con = (EditText) findViewById(R.id.input_password);
        ingreso = (Button) findViewById(R.id.btn_login);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        ingreso.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validar();
            }
        });

        if (!checkLocationPermission())
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
    public void validar(){
        String strUsuario = usu.getText().toString().trim().toUpperCase();

        if( (strUsuario.equals("POLICIA") || strUsuario.equals("ADMIN") || strUsuario.equals("USUARIO")) &&
                con.getText().toString().trim().equals("1234") ) {
            prefs.edit().putString("usuario", usu.getText().toString().trim()).commit();
            prefs.edit().putBoolean("register", true).commit();
            abrirMenu();
        }
        else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Usuario/Contraseña Incorrecta");
            alertDialogBuilder.show();
        }
    }
    public void abrirMenu(){
        //obtenerLista();
        Intent intent;
        intent = new Intent(this, MenuAlerta.class);
        startActivity(intent);
        finish();
    }

}
