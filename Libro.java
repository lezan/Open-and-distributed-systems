package Libreria;
public class Libro {
    
    private String id;    
    private String titolo;
    private String autore;    
    private String anno;
    private String casaEditrice;    
    private String ISBN;

    public Libro(String id,String titolo,String autore,String anno,String casaEditrice,String ISBN) {
        this.id = id;
        this.titolo = titolo;
        this.autore = autore;
        this.anno = anno;
        this.casaEditrice = casaEditrice;
        this.ISBN = ISBN;
    }
        
    public void setID(String id) {
    	this.id = id;
    }
    public void setTitolo(String titolo){
    	this.titolo = titolo;
    }
    public void setAutore(String autore){
    	this.autore = autore;
    }
    public void setAnno(String anno){
    	this.anno = anno;
    }
    public void setCasaEditrice(String casaEditrice){
    	this.casaEditrice = casaEditrice;
    }
    public void setISBN(String ISBN){
    	this.ISBN = ISBN;
    }    
    public String getID() {
    	return id;
    }
    public String getTitolo(){
    	return titolo;
    }
    public String getAutore(){
    	return autore;
    }
    public String getAnno(){
    	return anno;
    }
    public String getCasaEditrice(){
    	return casaEditrice;
    }
    public String getISBN(){
    	return ISBN;
    }
}