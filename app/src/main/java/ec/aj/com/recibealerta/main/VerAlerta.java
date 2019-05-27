package ec.aj.com.recibealerta.main;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ec.aj.com.recibealerta.R;
import ec.aj.com.recibealerta.adapter.ContactAdapter;
import ec.aj.com.recibealerta.clases.ContactInfo;
import ec.aj.com.recibealerta.service.ConectRest;

public class VerAlerta extends AppCompatActivity {

    ProgressDialog progressDialog;
    RecyclerView recList;
    SharedPreferences prefs ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_alerta);

        recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        obtenerLista();
    }

    public void obtenerLista(){
        String strUsuarrio = prefs.getString("usuario", "Usuario").toUpperCase();
        String url = "http://www.alerta.amazonebaycomprasecuador.com/api/Agente/Asignado?usuario=" + strUsuarrio;
        progressDialog = ProgressDialog.show(this, "", "Actualizando...", false);
        ConectRest conectRest = new ConectRest(this, url, recList);
        try {
            conectRest.comsumirRestAlertas(progressDialog, "POLICIA");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
