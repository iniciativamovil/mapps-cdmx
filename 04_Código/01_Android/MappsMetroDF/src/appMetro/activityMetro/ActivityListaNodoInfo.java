package appMetro.activityMetro;

import java.util.ArrayList;
import db.Conecciones.DbAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import appMetro.origDestino.R;
import appMetro.origDestino.adapterImage;
import appMetro.origDestino.filaDeLista;

public class ActivityListaNodoInfo extends Activity  {
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listageneral);
        ListView LV_lista = (ListView) this.findViewById(R.id.listViewNodos);
        Intent intent = getIntent();
        String accion = intent.getAction();
        if (accion.compareTo("tabComercios")==0){
        	long id = intent.getIntExtra("id", (int) 0);
            ArrayList<filaDeLista> datosLista;
            datosLista = new ArrayList<filaDeLista>();
            adapterImage arrayAdapterLin;
            
            DbAdapter baseDatos = new DbAdapter(this);
        	baseDatos.open();
            Cursor curAsignacion;
            Cursor curPropiedad;
            Cursor curClase;
            curAsignacion = baseDatos.fetchReg("tb_asignaciones",new String[] {"id_propiedad","id_grupo","estatus"},"id_nodo",id);
            
            if (curAsignacion.getCount()>0){
                do{
                	if (curAsignacion.getInt(2)==1){
	                	long idPropiedad = curAsignacion.getLong(0);
	                	curPropiedad = baseDatos.fetchReg("tb_propiedades",new String[] {"id_clase","nombre","descripcion","imagen","estatus"},"id_propiedad",idPropiedad);
	                	curClase = baseDatos.fetchReg("tb_clases",new String[] {"nombre ","imagen","descripcion"},"id_clase",curPropiedad.getLong(0));
	                	int resID;
	                	String nombre;
	                	String descripcion;
//	                	if (curPropiedad.getString(3).compareTo("")==0){
	                	if (curPropiedad.getInt(4) == 0){
	                		resID = getResources().getIdentifier(curClase.getString(1), "drawable", "appMetro.origDestino");
	                		nombre = curClase.getString(0);
	                		descripcion = curClase.getString(2);
	                	}else{
	                		resID = getResources().getIdentifier(curPropiedad.getString(3), "drawable", "appMetro.origDestino");
	                		nombre = curPropiedad.getString(1);
	                		descripcion = curPropiedad.getString(2);
	                	}
	                	datosLista.add(new filaDeLista((int)curAsignacion.getLong(0),resID, nombre ,descripcion, 1));
	                	curPropiedad.close();
	                	curClase.close();
                	}
                } while (curAsignacion.moveToNext());            	
            }
            curAsignacion.close();
            baseDatos.close();

            if (datosLista.isEmpty()){
            	datosLista.add(new filaDeLista(0,-1, " - Recolectando la Info - ","Estamos trabajando para recopilar toda la información y poderte dar un mejor servicio. Muchas gracias por tu paciencia.", 1));
            }
            arrayAdapterLin = new adapterImage(this,R.layout.list_item_image_gcs,datosLista);
        	LV_lista.setAdapter(arrayAdapterLin);
        }
	}
}
