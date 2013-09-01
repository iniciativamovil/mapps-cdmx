package appMetro.origDestino;

import java.util.ArrayList;
import java.util.Comparator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class adapterImage extends ArrayAdapter<filaDeLista>{ 
	Context context; 
    int layoutResourceId;    
    filaDeLista data[] = null;

    public adapterImage(Context context, int layoutResourceId, filaDeLista[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public adapterImage(Context context, int layoutResourceId, ArrayList<filaDeLista> data) {
    	super(context, layoutResourceId, data);
    	filaDeLista[] dataArray = new filaDeLista[data.size()];
    	for (int i =0;i<data.size();i++){
    		dataArray[i]=data.get(i);
    	}
    	this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = dataArray;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        filarHolder holder = null;
        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new filarHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.txtSubTitle = (TextView)row.findViewById(R.id.txtSubTitle);
            holder.imgRow   = (ImageView)row.findViewById(R.id.imgRow);
            row.setTag(holder);
        }else{
            holder = (filarHolder)row.getTag();
        }
        
        filaDeLista fila = data[position];
        holder.txtTitle.setText(fila.getTitulo());
        holder.txtSubTitle.setText(fila.getSubTitulo());

        if (fila.getIcono() == -1){
        	holder.imgRow.setImageResource(R.drawable.none);
        }else{
        	holder.imgRow.setImageResource(fila.getIcono());
        }
        
        if (fila.getBackground() == -999){
        	row.setBackgroundColor(Color.WHITE);
        }else{
        	row.setBackgroundResource(fila.getBackground());
        }
        
        if (fila.getColorTexto() == -999){
        	holder.txtTitle.setTextColor(Color.BLACK);
        	holder.txtSubTitle.setTextColor(Color.DKGRAY);
        }else{
        	holder.txtTitle.setTextColor(fila.getColorTexto());
        	holder.txtSubTitle.setTextColor(fila.getColorTexto());
        }
        
        if (fila.getTipo()==1){
        	holder.txtTitle.setGravity(Gravity.CENTER_VERTICAL);
        }else{
        	if (fila.getTipo()==2){
        		holder.txtTitle.setGravity(Gravity.CENTER_VERTICAL);
        		holder.txtSubTitle.setHeight(1);
        		holder.txtSubTitle.setVisibility(TextView.GONE);
        	}else{
        		if (fila.getTipo()==3){
            		//holder.txtTitle.setTextSize(18);
            		holder.txtTitle.setGravity(Gravity.CENTER_VERTICAL);
        		}else{
            		holder.txtTitle.setGravity(Gravity.BOTTOM);
            	}
        	}
        }
        return row;
    }

    static class filarHolder{
    	ImageView imgRow;
    	TextView txtTitle;
    	TextView txtSubTitle;
    }
    
	public void sort(Comparator<? super filaDeLista> comparator) {
		super.sort(comparator);
	}
}
	