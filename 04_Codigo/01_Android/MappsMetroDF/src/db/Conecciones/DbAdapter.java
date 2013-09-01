package db.Conecciones;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import appMetro.origDestino.R;

public class DbAdapter {
    public static final String KEY_TITLE = "title";
    public static final String KEY_BODY = "body";
    public static final String KEY_ROWID = "_id";
    private static final String TAG = "NotasDbAdapter";
    private static final String DATABASE_NAME = "railsdb.db";
    private static final int DATABASE_VERSION = 2;

    private static final String[] TABLES = new String[] 
    		{"tb_sistemas","tb_clases","tb_propiedades","tb_nodos","tb_referencias","tb_grupos","tb_adyacencias","tb_asignaciones"};
    
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Context mCtx;
    
    private class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
        	for (int i = 0; i < TABLES.length;i++){
            	db.execSQL("DROP TABLE IF EXISTS " + TABLES[i]);
            }
        	creaTablas(db);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            onCreate(db);
        }
    }

    public DbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getReadableDatabase();
        return this;
    }
    
    public DbAdapter openWr() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
        mDbHelper.close();
    }

    public Cursor fetchAllTable(String Tabla, String[] Columnas) {
        return mDb.query(Tabla, Columnas, null, null, null, null, null);        
    }
    
    public Cursor fetchAllTable(boolean Distinct,String Tabla, String[] Columnas) {
        return mDb.query(Distinct,Tabla, Columnas, null, null, null, null, null, null);        
    }
    
    public Cursor fetchAllTable(String Tabla, String[] Columnas, String NCampo ,String VCampo) {
        return mDb.query(true, Tabla, Columnas, NCampo + "='" + VCampo + "'", null, null, null, null,null);        
    }
    public Cursor fetchAllTable(String Tabla, String[] Columnas, String NCampo ,Long VCampo) {
        return mDb.query(true,Tabla, Columnas, NCampo + "=" + VCampo.toString() + "", null, null, null, null,null);        
    }

    public void reiniciaTablas(){
    	mDbHelper.onUpgrade(mDb, 2, 3);
    }
    
    public Cursor fetchReg(String Tabla, String[] Columnas, String nColumna, String vColumna) throws SQLException {
        Cursor mCursor =
                mDb.query(true, Tabla, Columnas, nColumna + "='" + vColumna+"'", null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchReg(String Tabla, String[] Columnas, String nColumna, Long vColumna) throws SQLException {
        Cursor mCursor =
                mDb.query(true, Tabla, Columnas, nColumna + "=" + vColumna+"", null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor fetchReg(String Tabla, String[] Columnas, String[] nColumna, Long[] vColumna) throws SQLException {
    	Cursor mCursor;
    	if(nColumna.length != vColumna.length){
    		mCursor = null;
    	}else{
        	StringBuffer Seleccion = new StringBuffer();
        	Seleccion.append(nColumna[0]).append(vColumna[0]).append(" ");
        	int numCol = 1;
        	while (numCol < nColumna.length){
        		Seleccion.append(" and ").append(nColumna[numCol]).append(vColumna[numCol]);
        		numCol++;
        	}
    		mCursor =
                mDb.query(true, Tabla, Columnas, Seleccion.toString() , null,
                        null, null, null, null);
	        if (mCursor != null) {
	            mCursor.moveToFirst();
	        }
    	}
        return mCursor;
    }
    
    public Cursor fetchRegLong(String Tabla, String[] Columnas, String nColumna, String vColumna) throws SQLException {
        Cursor mCursor =
                mDb.query(true, Tabla, Columnas, nColumna + "=" + vColumna, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public boolean updateNote(String DATABASE_TABLE, long rowId, String title, String body) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_BODY, body);
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public void creaTablas(SQLiteDatabase db){
        try { 
//    		int insertCount = cargaArchivo(mCtx, R.raw.creatablas1,db); 
//    		Toast.makeText(mCtx, "Actualizacion de Base - Lineas Ejecutadas= " + insertCount, Toast.LENGTH_LONG).show();
    		cargaArchivo(mCtx, R.raw.creatablas1,db); 
    	} catch (IOException e) { 
    		Toast.makeText(mCtx, e.toString(), Toast.LENGTH_LONG).show(); 
    		e.printStackTrace(); 
    	}
        llenaTablas(db);    	
    }

    public int cargaArchivo(Context context, int fileId,SQLiteDatabase db) throws IOException { 
    	int result = 0; 
    	InputStream insertsStream = context.getResources().openRawResource(fileId);
    	BufferedReader archivo = new BufferedReader(new InputStreamReader(insertsStream,"iso-8859-1"));
    	while (archivo.ready()) { 
    		String instruccion = archivo.readLine();
    		db.execSQL(URLDecoder.decode(instruccion,"UTF-8"));
    		result++; 
    	}
    	archivo.close();
    	return result; 
    } 
    
    public void llenaTablas(SQLiteDatabase db){
    	try { 
//    		int insertCount = cargaArchivo(mCtx, R.raw.insertadatos,db); 
//    		Toast.makeText(mCtx, "Actualizacion de Base - Lineas Ejecutadas= " + insertCount, Toast.LENGTH_LONG).show();
    		cargaArchivo(mCtx, R.raw.insertadatos,db);
    	} catch (IOException e) { 
    		Toast.makeText(mCtx, e.toString(), Toast.LENGTH_LONG).show(); 
    		e.printStackTrace(); 
    	}
    }

	public Cursor fetchReg(String Tabla, String[] Columnas, String Condicion)  throws SQLException {
        Cursor mCursor =
                mDb.query(true, Tabla, Columnas, Condicion, null,
                        null, null, Columnas[0], null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
	}
}
