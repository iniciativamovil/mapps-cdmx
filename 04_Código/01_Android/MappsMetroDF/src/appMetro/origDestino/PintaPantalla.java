package appMetro.origDestino;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PintaPantalla extends SurfaceView implements SurfaceHolder.Callback {
	private float[][] coordenadas;
	private double factor;
	
	public PintaPantalla(Context context) {
		super(context);
		getHolder().addCallback(this);
	}

	public PintaPantalla(Context context, float[][] coordenadas,double factor) {
		super(context);
		getHolder().addCallback(this);
		this.coordenadas = coordenadas;
		this.factor = factor;
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Canvas c = holder.lockCanvas(null);
		onDraw(c);
		holder.unlockCanvasAndPost(c);
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	public void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.GRAY);
		paint.setAlpha(255);
		paint.setTextSize(8);
		paint.setStrokeWidth((float)0.5);

		//for (int i=10;i<=550*2;i+=10){
		//canvas.drawText(String.valueOf(i), i*2, 10, paint);
		//canvas.drawText(String.valueOf(i), i*2, 600, paint);
		//canvas.drawText(String.valueOf(i), i*2, 1050, paint);
		//canvas.drawLine(i*2, 0, i*2, 550*2,paint);
		//canvas.drawText(String.valueOf(i), 10, i*2, paint);
		//canvas.drawText(String.valueOf(i), 600, i*2, paint);
		//canvas.drawText(String.valueOf(i), 1050, i*2, paint);
		//canvas.drawLine(0, i*2, 550*2, i*2,paint);			
		//}
		
		//Dibujar la ruta correspondiente sobre el mapa.
		paint.setColor(Color.BLUE);
		paint.setAlpha(100);
		paint.setStrokeWidth((int)(10*factor));
		canvas.drawCircle((int)(coordenadas[0][0]*factor), (int)(coordenadas[1][0]*factor), (int)(5*factor), paint);
		//canvas.drawBitmap(bitmap, left, top, paint);
		for (int cont=1; cont< coordenadas[0].length;cont++){
			canvas.drawCircle((int)(coordenadas[0][cont]*factor), (int)(coordenadas[1][cont]*factor), (int)(5*factor), paint);
			//canvas.drawBitmap(bitmap, left, top, paint);
			canvas.drawLine((int)(coordenadas[0][cont-1]*factor), (int)(coordenadas[1][cont-1]*factor), (int)(coordenadas[0][cont]*factor), (int)(coordenadas[1][cont]*factor),paint);
		}
	}	
}