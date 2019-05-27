package ec.aj.com.recibealerta.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ec.aj.com.recibealerta.adapter.ContactAdapter;
import ec.aj.com.recibealerta.clases.ContactInfo;

public class ConectRest {

    Context context;
    String url;
    MensajePopUp mensaje;
    RecyclerView recList;

    public ConectRest(Context context, String url, RecyclerView recList) {
        this.context = context;
        this.url = url;
        this.mensaje = new MensajePopUp(context);
        this.recList = recList;
    }

    public void comsumirRestAlertas(final ProgressDialog progressDialog, String strUsuario) throws IOException {

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,//Request.Method.POST, //Request.Method.GET,
                url,
                null, //parameters, //null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String strRespuesta = "-1";

                        try {
                            strRespuesta = response.getJSONObject(0).getString("usuario");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (!strRespuesta.equals("-1")) {
                            llenarInfo(response);
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            mensaje.mensajeSimple("No se encuentran novedades asignadas");
                        }

                        Log.e("Rest Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        mensaje.mensajeSimple("Hubo un problema, intente nuevamente.");
                        Log.e("Rest Error", error.toString());
                    }
                }
        );

        requestQueue.add(objectRequest);
    }

    public void llenarInfo(JSONArray response) {

        List<ContactInfo> result = new ArrayList<ContactInfo>();

        for (int i = 0; i < response.length(); i++) {
            try {
                ContactInfo ci = new ContactInfo();
                ci.name = response.getJSONObject(i).getString("usuario");
                ;
                ci.date = response.getJSONObject(i).getString("fechaCreacion");
                ;
                ci.location = ci.direccion = response.getJSONObject(i).getString("localizacion");
                /**/
                String[] arrLocation = ci.location.split("\\|");
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());

                try {
                    //latitud, longitud
                    List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(arrLocation[0]), Double.parseDouble(arrLocation[1]), 1);

                    if (addresses != null) {
                        if (addresses.size() > 0) {
                            Address returnedAddress = addresses.get(0);
                            /*StringBuilder strReturnedAddress = new StringBuilder();
                            for (int j = 0; j < returnedAddress.getMaxAddressLineIndex(); j++) {
                                strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("");
                            }*/
                            ci.direccion = returnedAddress.getAddressLine(0);
                        } else {
                            String strAdrres = ("No Address!");
                        }
                    } else {
                        String strAdrres = ("No Address returned!");
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                /**/
                result.add(ci);
            } catch (JSONException e) {
                mensaje.mensajeSimple("Ha ocurrido un error.");
            }
        }
        ContactAdapter ca = new ContactAdapter(result, mensaje, context);
        recList.setAdapter(ca);
    }

    public void comsumirRest(String strLatLon, final ProgressDialog progressDialog, String strUsuario) throws IOException {

        Map<String, String> params = new HashMap();
        params.put("usuario", strUsuario); // 30 car
        params.put("localizacion", strLatLon); // 100 caracteres
        params.put("descripcion", "Ayuda"); //40 caracteres
        params.put("estado", "Activo"); // 10
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        params.put("fechaCreacion", currentDateandTime);
        params.put("fechaCierre", "");

        JSONObject parameters = new JSONObject(params);

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST, //Request.Method.GET,
                url,
                parameters, //null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String strRespuesta = "-1";

                        try {
                            strRespuesta = response.getString("valor");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        if (strRespuesta.equals("si valio"))
                            mensaje.mensajeTitulo("En un momento la atenderán",
                                    "Alerta enviada");
                        else
                            mensaje.mensajeSimple("Hubo un problema, intente nuevamente");

                        Log.e("Rest Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        mensaje.mensajeSimple("Hubo un problema, intente nuevamente.");
                        Log.e("Rest Error", error.toString());
                    }
                }
        );

        requestQueue.add(objectRequest);
    }
}
