package appMetro.activityMetro;

import db.Conecciones.DbAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import appMetro.origDestino.R;

public class ActivityReferencias extends Activity {
	TextView tv_referencias;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.referencias);
        Intent intent = getIntent();
        StringBuilder datosReferencias = new StringBuilder();        
        DbAdapter baseDatos = new DbAdapter(this);
        
        tv_referencias = (TextView) this.findViewById(R.id.textreferencias);
        
        long idNodo = intent.getIntExtra("id", (int) 0);

        baseDatos.open();
        Cursor curReferencias;
        curReferencias = baseDatos.fetchReg("tb_referencias",new String[] {"des_corta","des_larga"},"id_nodo",idNodo);
        if (curReferencias.getCount()>0){
        	datosReferencias.append((char)(10));
        	do{
                datosReferencias.append(curReferencias.getString(0)).append((char)(10)).append((char)(10));
            } while (curReferencias.moveToNext());
        	datosReferencias.append((char)(10)).append((char)(10));
        }else{
        	datosReferencias.append((char)(10));
//            datosReferencias.append("Aun no hay referencias de esta estación.");
            datosReferencias.append("Estamos completando esta información para ti.");
            datosReferencias.append((char)(10)).append((char)(10));
            datosReferencias.append("En estos momentos no hay referencias ligadas a esta estación, pero estamos trabajando para recopilar toda la información y poderte dar un mejor servicio.");
            datosReferencias.append((char)(10)).append((char)(10));
            datosReferencias.append("Muchas gracias por tu paciencia.");
            datosReferencias.append((char)(10));
        }

        curReferencias.close();
        baseDatos.close();
        tv_referencias.setText(datosReferencias.toString());
	}
}
