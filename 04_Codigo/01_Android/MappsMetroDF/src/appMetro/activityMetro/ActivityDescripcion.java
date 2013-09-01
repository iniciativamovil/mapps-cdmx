package appMetro.activityMetro;

import db.Conecciones.DbAdapter;
import android.os.Bundle;
import android.content.*;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.TabHost.TabSpec;
import android.app.TabActivity;
import appMetro.origDestino.NodoInfo;
import appMetro.origDestino.R;
public class ActivityDescripcion extends TabActivity  {
	ImageView logoEstacion;
	TextView descEstacion;
	TextView nombreNodo;
	TabHost pestanasInfo;
	String estacion;
	NodoInfo nodo;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.descripcionestacion);
        logoEstacion = (ImageView) this.findViewById(R.id.imageView1);
        descEstacion = (TextView) this.findViewById(R.id.textDescripcionNodo);
        nombreNodo   = (TextView) this.findViewById(R.id.textNombreNodo);
        Intent intent = getIntent(); 
        nodo = new NodoInfo(intent.getStringExtra("estacionSel"));
        nodo = llenaInformacion(nodo);
        
        logoEstacion.setImageResource(getResources().getIdentifier(nodo.getImagen(), "drawable", "appMetro.origDestino"));
        nombreNodo.setText(nodo.getNombre());
        
        StringBuffer descripcionGeneral = new StringBuffer(nodo.getDescripcion());
        descripcionGeneral.append((char)(10)).append((char)(10));
        if(!nodo.isActivo()){
        	descripcionGeneral.append(" -- La estación no está en funcionamiento. -- ");
        	descripcionGeneral.append((char)(10)).append((char)(10));
        }
        descripcionGeneral.append("Entradas/Salidas: ").append(nodo.getEntradas());
        descripcionGeneral.append((char)(10)).append((char)(10));
        descripcionGeneral.append("Taquillas: ").append(nodo.getVentanillas());
        descripcionGeneral.append((char)(10)).append((char)(10));
        descEstacion.setText(descripcionGeneral.toString());

        pestanasInfo = getTabHost();
        pestanasInfo.addTab(addTabComercios());
        pestanasInfo.addTab(addTabReferencias());
        pestanasInfo.addTab(addTabMapa());
        pestanasInfo.setCurrentTab(0);

        pestanasInfo.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.resultadoselector);
        pestanasInfo.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.resultadoselector);
        pestanasInfo.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.resultadoselector);
		
		Drawable imagen = getResources().getDrawable(R.drawable.tabcomercioselector);
		imagen.setBounds(0,0, 50, 50);
        ((TextView) pestanasInfo.getTabWidget().getChildAt(0)).setCompoundDrawables(null, imagen, null, null);
        
        imagen = getResources().getDrawable(R.drawable.tabreferenciaselector);
		imagen.setBounds(0,0, 40, 50);
        ((TextView) pestanasInfo.getTabWidget().getChildAt(1)).setCompoundDrawables(null, imagen, null, null);
        
        imagen = getResources().getDrawable(R.drawable.tabplanobarrioselector);
		imagen.setBounds(0,0,50, 50);
		imagen.setFilterBitmap(true);
        ((TextView) pestanasInfo.getTabWidget().getChildAt(2)).setCompoundDrawables(null, imagen, null, null);
		
        try {
            XmlResourceParser parser = getResources().getXml(R.drawable.rescolorselector);
            ColorStateList colors = ColorStateList.createFromXml(getResources(), parser);
            ((TextView) pestanasInfo.getTabWidget().getChildAt(0)).setTextColor(colors);
            ((TextView) pestanasInfo.getTabWidget().getChildAt(1)).setTextColor(colors);
            ((TextView) pestanasInfo.getTabWidget().getChildAt(2)).setTextColor(colors);
        } catch (Exception e) {
            // handle exceptions
        }

        AutoCompleteTextView TV_busqueda;
        TV_busqueda = (AutoCompleteTextView) findViewById(R.id.textBusqueda);
        
        ImageButton BT_busqueda = (ImageButton) this.findViewById(R.id.botonBusqueda);
        BT_busqueda.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), ActivityBusqueda.class);
				String campo = "";
				AutoCompleteTextView TV_busqueda = (AutoCompleteTextView) findViewById(R.id.textBusqueda);
				if(TV_busqueda.getText().toString().trim().length() > 0){
					campo = TV_busqueda.getText().toString().trim();
				}
				myIntent.putExtra("busqueda", campo);
	    		startActivity(myIntent);
			}
        });
        TV_busqueda.clearFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(TV_busqueda.getWindowToken(), 0);
		
    }

	private NodoInfo llenaInformacion(NodoInfo nodo) {
    	DbAdapter baseDatos = new DbAdapter(this);
    	baseDatos.open();
    	Cursor curNodos;
        curNodos = baseDatos.fetchReg("tb_nodos",new String[] {"id_nodo","imagen","mapa","descripcion","entradas","ventanillas","interseccion","estatus"},"nombre",nodo.getNombre());
        if (curNodos.getCount()>0){
            nodo.setNodoId(curNodos.getInt(0));
            nodo.setImagen(curNodos.getString(1));
            nodo.setMapa(curNodos.getString(2));        	
            nodo.setDescripcion(curNodos.getString(3));
            
            nodo.setEntradas(curNodos.getInt(4));
            nodo.setVentanillas(curNodos.getInt(5));
            if ((curNodos.getInt(6) == 1)){
            	nodo.setInterseccion(true);	
            }else{
            	nodo.setInterseccion(false);
            }
            if ((curNodos.getInt(7) == 1)){
            	nodo.setActivo(true);	
            }else{
            	nodo.setActivo(false);
            }
        }
        curNodos.close();
        baseDatos.close();
		return nodo;
	}
	
	private TabSpec addTabComercios() {
        Intent intent = new Intent(this, ActivityListaNodoInfo.class);
		intent.setAction("tabComercios");
		intent.putExtra("id", nodo.getNodoId());
		
		TabSpec spec = pestanasInfo.newTabSpec("Tab1");
        TextView view = new TextView(this);
        view.setText("Servicios");
        view.setTextSize(10);
        view.setGravity(81);
        view.setPadding(0, 10, 0, 10);
        spec.setIndicator(view);
        spec.setContent(intent);
        return spec;
     }
	
     private TabSpec addTabReferencias() {
       Intent intent = new Intent(this, ActivityReferencias.class);
       intent.setAction("tabReferencias");
       intent.putExtra("id", nodo.getNodoId());
       intent.putExtra("nombre", nodo.getNombre());

       TabSpec spec = pestanasInfo.newTabSpec("Tab2");
       TextView view = new TextView(this);
       view.setText("Referencias");
       view.setTextSize(10);
       view.setGravity(81);
       view.setPadding(0, 10, 0, 10);
       spec.setIndicator(view);
       spec.setContent(intent);
       return spec;
     }
     
     private TabSpec addTabMapa() {
       Intent intent = new Intent(this, ActivityMapaBarrio.class);
       intent.putExtra("imagen", nodo.getMapa());
       intent.putExtra("desactivaBusqueda",true);
       TabSpec spec = pestanasInfo.newTabSpec("Tab3");
       TextView view = new TextView(this);
       view.setText("Mapa de Barrio");
       view.setTextSize(10);
       view.setGravity(81);
       view.setPadding(0, 10, 0, 10);
//       View view = View.inflate(getApplicationContext(), R.layout.tabview, getTabWidget());
//       ((ImageView) view.findViewById(R.id.imagetab)).setImageResource(R.drawable.tabplanobarrioselector);
//       ((TextView) view.findViewById(R.id.texttab)).setText("Mapa de Barrio");
       spec.setIndicator(view);
       spec.setContent(intent);
       return spec;
	}
     
	public Drawable getDrawable (int idDrawable, int width, int height){
		Bitmap B = BitmapFactory.decodeResource(getResources(),idDrawable);
		BitmapDrawable BD = new BitmapDrawable(B);
		BD.setBounds(0,0,width,height);
		return BD;
	}
}
