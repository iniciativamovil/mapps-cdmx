package appMetro.origDestino;

public class filaDeLista {
	private int icono;
	private int ID;
    private String titulo;
    private String subTitulo;
    private int tipo;
    private int background;
    private int colorTexto;

	public filaDeLista() {
		super();
	}
    
	public filaDeLista(int ID, int icono,String titulo, String subTitulo,int tipo) {
		super();
		this.ID = ID;
		this.icono = icono;
		this.titulo = titulo;
		this.subTitulo = subTitulo;
		this.tipo = tipo;
		this.background = -999;
		this.colorTexto = -999;
	}
	
	public filaDeLista(int ID, int icono, String titulo, String subTitulo,int tipo, int background,int colorTexto) {
		super();
		this.ID = ID;
		this.icono = icono;
		this.titulo = titulo;
		this.subTitulo = subTitulo;
		this.tipo = tipo;
		this.background = background;
		this.colorTexto = colorTexto;
	}

	public int compareTo(filaDeLista object2) {
		return this.titulo.compareTo(object2.titulo);
	}
	
	public String getTitulo(){
		return this.titulo;
	}
	
	public String getSubTitulo(){
		return this.subTitulo;
	}
	
	public int getIcono(){
		return this.icono;
	}
	
	public int getTipo(){
		return this.tipo;
	}
	
	public int getBackground() {
		return this.background;
	}

	public int getColorTexto() {
		return this.colorTexto;
	}

	public int getID() {
		return this.ID;
	}

	public filaDeLista clone(){
		return new filaDeLista(this.ID, this.icono,this.titulo,this.subTitulo,this.tipo, this.background, this.colorTexto);
	}

}