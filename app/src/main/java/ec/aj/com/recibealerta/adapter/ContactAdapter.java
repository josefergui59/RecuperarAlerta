package ec.aj.com.recibealerta.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ec.aj.com.recibealerta.R;
import ec.aj.com.recibealerta.clases.ContactInfo;
import ec.aj.com.recibealerta.main.MapsActivity;
import ec.aj.com.recibealerta.service.MensajePopUp;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<ContactInfo> contactList;
    MensajePopUp mensaje;
    Context context;

    public ContactAdapter(List<ContactInfo> contactList, MensajePopUp mensaje, Context context) {
        this.contactList = contactList;
        this.mensaje= mensaje;
        this.context = context;
    }


    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        final ContactInfo ci = contactList.get(i);
        contactViewHolder.vName.setText(ci.direccion);
        contactViewHolder.vFecha.setText(ci.date);
        contactViewHolder.vTitle.setText(ci.name);
        contactViewHolder.vMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirMapa(ci);
            }
        });
        contactViewHolder.vCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirMapa(ci);
            }
        });
    }
    public void abrirMapa(final ContactInfo ci){
        //mensaje.mensajeSimple(strLocation);
        String[] arrLocation = ci.location.split("\\|");
        Intent intent = new Intent(context.getApplicationContext(), MapsActivity.class);
        intent.putExtra("dbLatitud",arrLocation[0]);
        intent.putExtra("dbLongitud",arrLocation[1]);
        intent.putExtra("strRecinto",ci.name);
        intent.putExtra("strDireccion",ci.direccion);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
    }
    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        protected TextView vName;
        protected TextView vFecha;
        //protected TextView vEmail;
        protected TextView vTitle;
        protected ImageView vMap;
        protected CardView vCard;

        public ContactViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.txtName);
            vFecha = (TextView)  v.findViewById(R.id.txtFecha);
            //vEmail = (TextView)  v.findViewById(R.id.txtEmail);
            vTitle = (TextView) v.findViewById(R.id.title);
            vMap = (ImageView) v.findViewById(R.id.ivMap);
            vCard = (CardView) v.findViewById(R.id.card_view);
        }
    }
}