package appMetro.origDestino;

import java.util.ArrayList;
import java.util.Comparator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class adapterFilaResultado extends ArrayAdapter<filaDeResultado>{
	Context context; 
    int layoutResourceId;    
    filaDeResultado data[] = null;
    
	public adapterFilaResultado(Context context, int layoutResourceId, filaDeResultado[] data) {
		super(context, layoutResourceId,data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
	}
	
	public adapterFilaResultado(Context context, int layoutResourceId, ArrayList<filaDeResultado> data) {
		super(context, layoutResourceId,data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        filaDeResultado[] dataArray = new filaDeResultado[data.size()];
    	for (int i =0;i<data.size();i++){
    		dataArray[i]=data.get(i);
    	}
        this.data = dataArray;
	}
	
	
    static class filaComponentes{
    	TextView txtNoResultado;
    	ImageView imgRelog;
    	TextView txtTiempos;
    	TextView txtRutaCorto;
    }
	
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        filaComponentes Componentes = null;
        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            Componentes = new filaComponentes();
            Componentes.txtNoResultado 	= (TextView)row.findViewById(R.id.txtNoResultado);
            Componentes.imgRelog 		= (ImageView)row.findViewById(R.id.imgRelog);
            Componentes.txtTiempos 		= (TextView)row.findViewById(R.id.txtTiempos);
            Componentes.txtRutaCorto   	= (TextView)row.findViewById(R.id.txtRutaCorto);
            row.setTag(Componentes);
        }else{
        	Componentes = (filaComponentes)row.getTag();
        }
        
        filaDeResultado fila = data[position];
        Componentes.txtNoResultado.setText(String.valueOf(fila.getNoRuta()));
        Componentes.txtTiempos.setText(fila.getTxtTiempos());
        
    	if(fila.isSeleccionado()){
    		row.setBackgroundResource(R.drawable.bgresultado_activo);
    		Componentes.imgRelog.setImageResource(R.drawable.reloj_activo);
    		Componentes.txtRutaCorto.setText(fila.getTxtRutaCompleta());
    		Componentes.txtNoResultado.setTextColor(Color.WHITE);
        	Componentes.txtTiempos.setTextColor(Color.WHITE);
        	Componentes.txtRutaCorto.setTextColor(Color.WHITE);
    	}else{
    		row.setBackgroundResource(R.drawable.bgresultado_inactivo);
    		Componentes.imgRelog.setImageResource(R.drawable.reloj_inactivo);
    		Componentes.txtRutaCorto.setText(fila.getTxtRuta());
    		Componentes.txtNoResultado.setTextColor(Color.DKGRAY);
        	Componentes.txtTiempos.setTextColor(Color.DKGRAY);
        	Componentes.txtRutaCorto.setTextColor(Color.DKGRAY);
    	}
    	
        return row;
    }
    
	public void sort(Comparator<? super filaDeResultado> comparator) {
		super.sort(comparator);
	}

}

