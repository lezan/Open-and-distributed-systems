package Libreria;
public class Utente {
    
    private String id;
    private String nickuser;
    private String password;
    private String email;
    private String indirizzo;    
    private String citta;
    private String cap;
    private String telefono;    
    private String telefono2;    
    private String nome;
    private String cognome;
    

    public Utente(String id,String nickuser,String password,String email,String nome,String cognome) {
        this.id = id;
        this.nickuser = nickuser;
        this.password = password;
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
    }
        
    public void setID(String id) {
    	this.id = id;
    }
    
    public void setNickuser(String nickuser){
    	this.nickuser = nickuser;
    }
    
    public void setPassword(String password){
    	this.password = password;
    }
    
    public void setEmail(String email){
    	this.email = email;
    }
    
    public void setIndirizzo(String indirizzo){
    	this.indirizzo = indirizzo;
    }
    
    public void setCitta(String citta){
    	this.citta = citta;
    }
    
    public void setCap(String cap) {
    	this.cap = cap;
    }
    
    public void setTelefono(String telefono) {
    	this.telefono = telefono;
    }
    
    public void setTelefono2(String telefono2) {
    	this.telefono2 = telefono2;
    }
    
    public void setNome(String nome) {
    	this.nome = nome;
    }
    
    public void setCognome(String cognome) {
    	this.cognome = cognome;
    }
    
    public String getID() {
    	return id;
    }
    
    public String getNickuser(){
    	return nickuser;
    }
    
    public String getPassword(){
    	return password;
    }
    
    public String getEmail(){
    	return email;
    }
    
    public String setIndirizzo(){
    	return indirizzo;
    }
    
    public String getCitta(){
    	return citta;
    }
    
    public String getCap() {
    	return cap;
    }
    
    public String getTelefono() {
    	return telefono;
    }
    
    public String getTelefono2() {
    	return telefono2;
    }
    
    public String getNome() {
    	return nome;
    }
    
    public String getCognome() {
    	return cognome;
    }
}
