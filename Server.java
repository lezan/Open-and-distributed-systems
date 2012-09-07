package Libreria;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.axis.encoding.Base64;

public class Server {

	Connection con = null;

	boolean connectDatabase() {
		if (con == null) {
			String username = "libreria"; /* username database mysql */
			String password = "libreria"; /* password database mysql */
			String database = "libreria"; /* database mysql */
			String serverName = "localhost:3306"; /* sarà localhost */
			String url = "jdbc:mysql://" + serverName + "/" + database;
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				con = DriverManager.getConnection(url, username, password);
				System.out.println("Connessione al database avvenuta correttamente.\n");
				return true;
			} catch (Exception e) {
				System.out.println("Errore: impossibile connettersi al database.\n");
				System.out.println("Errore" + e.getMessage());
				return false;
			}
		} else {
			return true;
		}
	}

	public int iscrizioneUser(String nickuser,String passwd,String passwdc, String email) {
		if (!connectDatabase()) {
			return 2;
		}
		if (!(checkPassword(passwd))) {
			return 3;
		}
		if (!(passwd.equals(passwdc))) {
			return 4;
		}
		System.out.println("Password corretta.\n");
		try {
			/* Controllo se il nick è già utilizzato */
			String query = "SELECT nickname FROM users WHERE nickname=?";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nickuser);			
			ResultSet rs = prstmt.executeQuery();;
			if (rs.next()) {
				System.out.println("Nick già utilizzato. Scegliere un altro nick.\n");
				return 5;
			}
			rs.beforeFirst();
			/* Controllo se l'email è già utilizzata */
			query = "SELECT email FROM users WHERE email=?";
			PreparedStatement prstmt2 = con.prepareStatement(query);
			prstmt2.setString(1, email);
			rs = prstmt2.executeQuery();
			if (rs.next()) {
				System.out.println("Email già utilizzata. Scegliere un'altra email.\n");
				return 6;
			}
			rs.beforeFirst();
			/* Nick ed email inseriti non sono utilizzati */
			System.out.println("Nick ed email validi.");
			query = "INSERT INTO users (nickname,password,email) VALUES (?,?,?)";
			PreparedStatement prstmt3 = con.prepareStatement(query);
			prstmt3.setString(1, nickuser);
			prstmt3.setString(2, passwd);
			prstmt3.setString(3, email);
			prstmt3.executeUpdate();
			rs.close();
			prstmt.close();
			prstmt2.close();
			prstmt3.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			return 7;
		}
		return 1;
	}

	public int iscrizioneLibrerie(String nome, String passwd, String passwdc,
			String email, String indirizzo, String citta, String cap,
			String partitaIva) {
		if (!connectDatabase()) {
			return 2;
		}
		if (!(checkPassword(passwd))) {
			return 3;
		}
		if (!(passwd.equals(passwdc))) {
			return 4;
		}
		System.out.println("Password corretta.\n");
		try {
			/* Controllo se il nome è già utilizzato */
			String query = "SELECT nome FROM vendors WHERE nome=?";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nome);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next()) {
				System.out.println("Nome già utilizzato. Scegliere un altro nome.\n");
				return 5;
			}
			rs.beforeFirst();
			/* Controllo se l'email è già utilizzata */
			query = "SELECT email FROM vendors WHERE email=?";
			PreparedStatement prstmt2 = con.prepareStatement(query);
			prstmt2.setString(1, email);
			rs = prstmt2.executeQuery();
			if (rs.next()) {
				System.out.println("Email già utilizzata. Scegliere un'altra email.\n");
				return 6;
			}
			rs.beforeFirst();
			/* Email inserita non è utilizzata */
			query = "SELECT partita_iva FROM vendors WHERE partita_iva=?";
			PreparedStatement prstmt3 = con.prepareStatement(query);
			prstmt3.setString(1, partitaIva);
			rs = prstmt3.executeQuery();
			if (rs.next()) {
				System.out.println("Partita iva già utilizzata. Scegliere un'altra partita iva.\n");
				return 7;
			}
			rs.beforeFirst();
			System.out.println("Nick ed email validi.");
			query = "INSERT INTO vendors (nome,password,email,indirizzo,citta,cap,partita_iva) VALUES (?,?,?,?,?,?,?)";
			PreparedStatement prstmt4 = con.prepareStatement(query);
			prstmt4.setString(1, nome);
			prstmt4.setString(2, passwd);
			prstmt4.setString(3, email);
			prstmt4.setString(4, indirizzo);
			prstmt4.setString(5, citta);
			prstmt4.setString(6, cap);
			prstmt4.setString(7, partitaIva);
			prstmt4.executeUpdate();
			rs.close();
			prstmt.close();
			prstmt2.close();
			prstmt3.close();
			prstmt4.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return 8;
		}
		return 1;
	}

	public int loginUsers(String nickuser, String passwd) {
		if (!connectDatabase()) {
			return -2;
		}
		System.out.println(nickuser);
		System.out.println(passwd);
		try {
			String query = "SELECT nickname FROM users WHERE nickname=? AND password=?";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nickuser);
			prstmt.setString(2, passwd);
			ResultSet rs = prstmt.executeQuery();
			System.out.println(prstmt.toString());
			if (!(rs.next())) {
				System.out.println("Errore: nick o password non corretti.\n");
				return -1;
			} else {
				System.out.println("Login effettuato con successo.\n");
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			return 1;
		}
		return 0;
	}

	public int loginLibrerie(String email, String passwd) {
		if (!connectDatabase()) {
			return -2;
		}
		System.out.println(email);
		System.out.println(passwd);
		try {
			String query = "SELECT nome FROM vendors WHERE email=? AND password=?";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, email);
			prstmt.setString(2, passwd);
			ResultSet rs = prstmt.executeQuery();
			System.out.println(prstmt.toString());
			if (rs.next()) {
				System.out.println("Login effettuato con successo.\n");
			} else {
				System.out.println("Errore: nick o password non corretti.\n");
				return -1;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			return 1;
		}
		return 0;
	}

	/* Funzione per verificare la password prima di fare modifiche */

	boolean checkPasswordUsers(String nickuser, String passwd) {
		if (!connectDatabase()) {
			return false;
		}
		try {
			String query = "SELECT nickname FROM users WHERE nickname=? AND password=?";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nickuser);
			prstmt.setString(2, passwd);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next()) {
				System.out.println("Password corretta.\n");
			} else {
				System.out.println("Password non corretta.\n");
				return false;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	boolean checkPasswordLibrerie(String email, String passwd) {
		if (!connectDatabase()) {
			return false;
		}
		try {
			String query = "SELECT nome FROM vendors WHERE email=? AND password=?";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, email);
			prstmt.setString(2, passwd);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next()) {
				System.out.println("Password corretta.\n");
			} else {
				System.out.println("Password non corretta.\n");
				return false;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	boolean checkEmailUsers(String email) {
		if (!connectDatabase()) {
			return false;
		}
		try {
			String query = "SELECT nickname FROM users WHERE email=?";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, email);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next() == false) {
				return false;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	boolean checkEmailLibrerie(String email) {
		if (!connectDatabase()) {
			return false;
		}
		try {
			String query = "SELECT nome FROM vendors WHERE email=?";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, email);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next() == false) {
				return false;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public int editUser(String[] campi) {
		if (!connectDatabase()) {
			return -1;
		}
		for(int i=0;i<14;i++) {
			System.out.println(i+"=>"+campi[i]);
		}
		/*campi[0]=password old;
		 * campi[1]= password new;
		 * campi[2]=email;
		 * campi[3]=nome_utente;
		 * campi[4]=cognome;
		 * campi[5]=indirizzo;
		 * campi[6]=citta;
		 * campi[7]=cap;
		 * campi[8]=telefono
		 * campi[9]=telefono2;
		 * campi[10]=nickuser;
		 * campi[11]=anno
		 * campi[12]=mese
		 * campi[13]=giorno 
		 */
		try{
			String query;
			PreparedStatement prstmt;
			if(!campi[0].equals("")) {
				if(checkPasswordUsers(campi[10],campi[0])) {
					query = "UPDATE users SET password=? WHERE nickname=?;";
					prstmt = con.prepareStatement(query);
					prstmt.setString(1, campi[1]);
					prstmt.setString(2, campi[10]);
					prstmt.executeUpdate();
				}
				else {
					System.out.println("password vecchia errata");
					return 2;
				}
			}
			ResultSet rs;
			query="SELECT nickname FROM users WHERE email=?;";
			prstmt = con.prepareStatement(query);
			prstmt.setString(1, campi[2]);
			rs=prstmt.executeQuery();
			if(rs.next()&&!rs.getString("nickname").equals(campi[10])) {
				return 3;
			}
			rs.beforeFirst();
			Date data=new Date(0000-00-00);
			query="UPDATE users SET email=?,nome=?,cognome=?,indirizzo=?,citta=?,cap=?,telefono=?,telefono2=?,data_nascita=? WHERE nickname=?;";
			prstmt = con.prepareStatement(query);
			prstmt.setString(1, campi[2]);
			prstmt.setString(2, campi[3]);
			prstmt.setString(3, campi[4]);
			prstmt.setString(4, campi[5]);
			prstmt.setString(5, campi[6]);
			if(campi[7].equals("")) {
				prstmt.setNull(6, java.sql.Types.VARCHAR);
			}
			else {
				prstmt.setString(6, campi[7]);
			}
			prstmt.setString(7, campi[8]);
			prstmt.setString(8, campi[9]);
			if(campi[11].equals("")||campi[11].equals("")||campi[11].equals("")) {
				prstmt.setNull(9, java.sql.Types.DATE);
			}
			else {
				prstmt.setDate(9, data.valueOf(campi[11]+"-"+campi[12]+"-"+campi[13]));
			}
			prstmt.setString(10, campi[10]);
			prstmt.executeUpdate();
			prstmt.close();
			rs.close();			
		}
		catch(SQLException e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}

	public int editLibreria(String[] campi) {
		if (!connectDatabase()) {
			return -1;
		}
		try{
			String query;
			PreparedStatement prstmt;
			/*Controllo per eventuale cambio password*/
			if(!campi[0].equals("")) {
				if(checkPasswordLibrerie(campi[10],campi[0])) {
					query = "UPDATE vendors SET password=? WHERE email=?;";
					prstmt = con.prepareStatement(query);
					prstmt.setString(1, campi[1]);
					prstmt.setString(2, campi[10]);
					prstmt.executeUpdate();
				}
				else{
					System.out.println("password vecchia errata");
					return 2;
				}
			}
			/*Controllo partita Iva*/
			ResultSet rs;
			query="SELECT email FROM vendors WHERE partita_iva=?;";
			prstmt = con.prepareStatement(query);
			prstmt.setString(1, campi[9]);
			rs=prstmt.executeQuery();
			if(rs.next()&&!rs.getString("email").equals(campi[10])) {
				prstmt.close();
				rs.close();
				return 3;
			}
			/*Controllo nome*/
			query="SELECT email FROM vendors WHERE nome=?;";
			prstmt = con.prepareStatement(query);
			prstmt.setString(1, campi[3]);
			rs=prstmt.executeQuery();
			if(rs.next()&&!rs.getString("email").equals(campi[10]))	{
				prstmt.close();
				rs.close();
				return 4;
			}
			/*Se la mail vecchia e quella nuova non coincidono, controllo l'unicità della nuova mail*/
			if(!campi[2].equals(campi[10])){
				query="SELECT nome FROM vendors WHERE email=?;";
				prstmt = con.prepareStatement(query);
				prstmt.setString(1, campi[2]);
				rs=prstmt.executeQuery();
				if(rs.next()) { /*Email già esistente*/
					prstmt.close();
					rs.close();
					return 5;
				}
				else {/*Procedo con l'aggiornamento e segnalo che è cambiata la mail*/
					query="UPDATE vendors SET email=?,nome=?,indirizzo=?,citta=?,cap=?,telefono=?,fax=?,partita_iva=? WHERE email=?;";
					prstmt = con.prepareStatement(query);
					prstmt.setString(1, campi[2]);
					prstmt.setString(2, campi[3]);
					prstmt.setString(3, campi[4]);
					prstmt.setString(4, campi[5]);
					prstmt.setString(5, campi[6]);
					prstmt.setString(6, campi[7]);
					prstmt.setString(7, campi[8]);
					prstmt.setString(8, campi[9]);
					prstmt.setString(9, campi[10]);
					prstmt.executeUpdate();
					prstmt.close();
					rs.close();
					return 6;
				}
			}
			/*La mail non è cambiata*/
			query="UPDATE vendors SET email=?,nome=?,indirizzo=?,citta=?,cap=?,telefono=?,fax=?,partita_iva=? WHERE email=?;";
			prstmt = con.prepareStatement(query);
			prstmt.setString(1, campi[2]);
			prstmt.setString(2, campi[3]);
			prstmt.setString(3, campi[4]);
			prstmt.setString(4, campi[5]);
			prstmt.setString(5, campi[6]);
			prstmt.setString(6, campi[7]);
			prstmt.setString(7, campi[8]);
			prstmt.setString(8, campi[9]);
			prstmt.setString(9, campi[10]);
			prstmt.executeUpdate();
			prstmt.close();
			rs.close();			
		}
		catch(SQLException e){
			e.printStackTrace();
			return -1;
		}
		return 0;
	}

	public boolean checkPassword(String password) {
		if (password.length() >= 8) {
			return true;
		} else {
			System.out
					.println("Errore. La password deve contenere più di 8 caratteri.\n");
			return false;
		}
	}

	/*
	 * Aggiunge un libro al catalogo se questo non è presente, e poi inserisce
	 * il libro nella tabella libro_venditore, che esplica l'associazione tra
	 * libreria e libro. In questa, infatti, sono presenti i libri venduti dalle
	 * librerie. Dobbiamo aggiungere il campo dataInserimento alla tabella
	 * libri_table.
	 */
	public int addLibro(String titolo, String isbn, String autore, String editore, 
			String anno, String lingua, String prezzo, String copie, String sconto, 
			String email) {
		if (!connectDatabase()) {
			return -1;
		}
		try {
			String query = "SELECT ISBN FROM libri_table WHERE ISBN=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, isbn);
			ResultSet rs = prstmt.executeQuery();
			String nome=leggiNomeLibreria(email);
			if (rs.next()== false) {
				System.out.println("Il libro non è presente nel catalogo.\nIl libro verrà aggiunto.\n");
				query = "INSERT INTO libri_table (titolo,autore,casa_editrice,ISBN,prezzo,lingua,nome_libreria,anno) VALUES (?,?,?,?,?,?,?,?);";
				prstmt = con.prepareStatement(query);
				prstmt.setString(1, titolo);
				prstmt.setString(2, autore);
				prstmt.setString(3, editore);
				prstmt.setString(4, isbn);
				prstmt.setString(5, prezzo);
				prstmt.setString(6, lingua);
				prstmt.setString(7, nome);
				prstmt.setString(8, anno);
				prstmt.executeUpdate();
				System.out.println("Aggiunto.\n");
				/*
				 * Se il libro non esisteva nel catalogo, sicuramente non
				 * esisteva in libro_venditore
				 */
				System.out.println("Inserisco il libro in libro_venditore.\n");
				query = "INSERT INTO libro_venditore(ISBN,nome,sconto,copie) VALUES (?,?,?,?);";
				prstmt = con.prepareStatement(query);
				prstmt.setString(1, isbn);
				prstmt.setString(2, nome);
				prstmt.setString(3, sconto);
				prstmt.setString(4, copie);
				prstmt.executeUpdate();
				System.out.println("Inserito.\n");
				rs.close();
				prstmt.close();
				return 1;
			}
			/*
			 * Se non era null, significa che il libro esisteva già nel catalogo
			 * e quindi devo inserire solo in libro_venditore
			 */
			else {
				query = "SELECT ISBN,nome FROM libro_venditore WHERE ISBN=? AND nome=?;";
				prstmt = con.prepareStatement(query);
				prstmt.setString(1, isbn);
				prstmt.setString(2, nome);
				rs = prstmt.executeQuery();
				if (rs.next()==false) { // Non era presente quel libro venduto da quella
									// libreria in libro_venditore
					query = "INSERT INTO libro_venditore(ISBN,nome,sconto,copie) VALUES(?,?,?,?);";
					prstmt = con.prepareStatement(query);
					prstmt.setString(1, isbn);
					prstmt.setString(2, nome);
					prstmt.setString(3, sconto);
					prstmt.setString(4, copie);
					prstmt.executeUpdate();
				}
				rs.close();
				prstmt.close();
			}
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return -2;
		}
		return 2;
	}

	public String[] leggiLibro(String isbn) {
		String[] risultato = new String[6];
		if (!connectDatabase()) {
			return risultato;
		}
		try {
			String query = "SELECT titolo,autore,casa_editrice,anno,prezzo,lingua FROM libri_table WHERE ISBN=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, isbn);
			ResultSet rs = prstmt.executeQuery();
			if (!rs.next()) {
				System.out.println("Il libro cercato non è presente.\n");
				return risultato;
			} else {
				rs.beforeFirst();
				while (rs.next()) {
					risultato[0] = rs.getString("titolo");
					risultato[1] = rs.getString("autore");
					risultato[2] = rs.getString("casa_editrice");
					risultato[3] = String.valueOf(rs.getInt("anno"));
					risultato[4] = String.valueOf(rs.getDouble("prezzo"));
					risultato[5] = rs.getString("lingua");
					
				}
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		return risultato;
	}

	public String[][] leggiLibroVenditore(String isbn) {
		String risultato[][] = null;
		if (!connectDatabase()) {
			return risultato;
		}
		int numero = 0;
		try {
			String query = "SELECT count(*) AS numero FROM libro_venditore WHERE ISBN=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, isbn);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next()) {
				numero = rs.getInt("numero");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		risultato = new String[numero][3];
		try {
			String query = "SELECT nome,sconto,copie FROM libro_venditore WHERE ISBN=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, isbn);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next() == false) {
				System.out.println("Il libro cercato non è presenteeeeeeee.\n");
				return risultato;
			} else {
				rs.beforeFirst();
				int i=0;
				while (rs.next()) {
					if(i<numero) {
						for (int j = 0; j < 3; j++) {
							if (j == 0) {
								risultato[i][j] = rs.getString("nome");
							} else if (j == 1) {
								risultato[i][j] = Integer.toString(rs.getInt("sconto"));
							} else if (j == 2) {
								risultato[i][j] = Integer.toString(rs.getInt("copie"));
							}
						}
					}
					i++;
				}
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return null;
		}
		return risultato;
	}
	
	public String[] leggiDatiUtente(String nickuser){
		String[] result=new String[11];
		String data="";
		if (!connectDatabase()) {
			return result;
		}
		try {
			System.out.println(nickuser);
			String query = "SELECT * FROM users WHERE nickname=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nickuser);
			ResultSet rs = prstmt.executeQuery();
			rs.next();
			result[0] = rs.getString("email");
			result[1] = rs.getString("nome");
			result[2] = rs.getString("cognome");
			result[3] = rs.getString("indirizzo");
			result[4] = rs.getString("citta");
			result[5] = rs.getString("cap");
			result[6] = rs.getString("telefono");
			result[7] = rs.getString("telefono2");
			data=rs.getString("data_nascita");
			if(data==null) {
				result[8] = "- -";
				result[9] = "- -";
				result[10] = "- -";
			}
			else { 
				result[8] = data.substring(0, 4);
				result[9] = data.substring(5, 7);
				result[10] = data.substring(8, 10);
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore. IIIIIIImpossibile leggere i dati.\n");
		}
		return result;
	}
	
	public String[] leggiDatiLibreria(String email){
		String[] result=new String[8];
		if (!connectDatabase()) {
			return result;
		}
		try {
			String query = "SELECT * FROM vendors WHERE email=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, email);
			ResultSet rs = prstmt.executeQuery();
			rs.next();
			result[0] = rs.getString("nome");
			result[1] = rs.getString("email");
			result[2] = rs.getString("indirizzo");
			result[3] = rs.getString("citta");
			result[4] = rs.getString("cap");
			result[5] = rs.getString("telefono");
			result[6] = rs.getString("fax");
			result[7] = rs.getString("partita_iva");
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile leggere i dati.\n");
		}
		return result;
	}

	public String leggiAutore(String isbn) {
		String autore = "";
		if (!connectDatabase()) {
			return autore;
		}
		try {
			String query = "SELECT autore FROM libri_table WHERE isbn=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, isbn);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next()==false) {
				System.out.println("Il libro cercato non è presente.\n");
				return autore;
			} else {
				autore = rs.getString("autore");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return autore;
		}
		return autore;
	}

	public String leggiTitolo(String isbn) {
		String titolo = "";
		if (!connectDatabase()) {
			return isbn;
		}
		try {
			String query = "SELECT titolo FROM libri_table WHERE isbn=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, isbn);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next()==false) {
				System.out.println("Il libro cercato non è presente.\n");
				return titolo;
			} else {
				titolo = rs.getString("titolo");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return titolo;
		}
		return titolo;
	}

	public String leggiISBN(String titolo) {
		String isbn = "";
		if (!connectDatabase()) {
			return isbn;
		}
		try {
			String query = "SELECT ISBN FROM libri_table WHERE titolo=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, titolo);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next()==false) {
				System.out.println("Il libro cercato non è presente.\n");
				return isbn;
			} else {
				isbn = rs.getString("isbn");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return isbn;
		}
		return isbn;
	}

	public String leggiEditore(String isbn) {
		String editore = "";
		if (!connectDatabase()) {
			return editore;
		}
		try {
			String query = "SELECT casa_editrice FROM libri_table WHERE ISBN=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, isbn);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next()==false) {
				System.out.println("Il libro cercato non è presente.\n");
				return editore;
			} else {
				editore = rs.getString("casa_editrice");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return editore;
		}
		return editore;
	}
	
	public String leggiDataPubblicazione(String isbn) {
		String data = "";
		if (!connectDatabase()) {
			return data;
		}
		try {
			String query = "SELECT data_pubblicazione FROM libri_table WHERE ISBN=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, isbn);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next()==false) {
				System.out.println("Il libro cercato non è presente.\n");
				return data;
			} else {
				data = rs.getString("data_pubblicazione");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return data;
		}
		return data;
	}
	
	public String leggiLingua(String isbn) {
		String lingua = "";
		if (!connectDatabase()) {
			return lingua;
		}
		try {
			String query = "SELECT lingua FROM libri_table WHERE ISBN=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, isbn);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next()==false) {
				System.out.println("Il libro cercato non è presente.\n");
			} else {
				lingua = rs.getString("lingua");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return lingua;
		}
		return lingua;
	}
	
	public String leggiPrezzo(String isbn) {
		String prezzo = "";
		if (!connectDatabase()) {
			return prezzo;
		}
		try {
			String query = "SELECT prezzo FROM libri_table WHERE ISBN=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, isbn);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next()==false) {
				System.out.println("Il libro cercato non è presente.\n");
			} else {
				prezzo = String.valueOf(rs.getDouble("prezzo"));
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return prezzo;
		}
		return prezzo;
	}
	
	public double leggiVoto(String isbn) {
		double voto = 0;
		if (!connectDatabase()) {
			return voto;
		}
		try {
			String query = "SELECT voto FROM libri_table WHERE ISBN=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, isbn);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next()==false) {
				System.out.println("Problema ne rs di leggiVoto");
				return voto;
			}
			System.out.println(rs.getDouble("voto"));
			voto = rs.getDouble("voto");
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		System.out.println(voto);
		return voto;
	}

	// Rimuove i libri inseriti dal catalogo
	public int deleteLibro(String email, String ISBN) {
		String nomeLibreria = "";
		if (!connectDatabase()) {
			return 2;
		}
		try {
			String query = "SELECT nome FROM vendors WHERE email=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, email);
			prstmt.toString();
			ResultSet rs = prstmt.executeQuery();
			if (!rs.next()) {
				System.out.println("La libreria cercata non esiste.\n");			
				return 3;
			} else {
				nomeLibreria = rs.getString("nome");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return 4;
		}
		try {
			String query = "SELECT ISBN FROM libro_venditore WHERE ISBN=? AND nome=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, ISBN);
			prstmt.setString(2, nomeLibreria);
			prstmt.toString();
			ResultSet rs = prstmt.executeQuery();
			if (!rs.next()) {
				System.out.println("Il libro cercato non è presente.\n");
				return 3;
			} else {
				query = "DELETE FROM libro_venditore WHERE ISBN=? AND nome=?;";
				prstmt = con.prepareStatement(query);
				prstmt.setString(1, ISBN);
				prstmt.setString(2, nomeLibreria);
				prstmt.executeUpdate();
				System.out.println("Il libro con ISBN = " + ISBN
						+ " è stato eliminato dal catalogo.\n");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return 4;
		}
		return 0;
	}
	
	public int checkOwnership(String email,String isbn) {
		if (!connectDatabase()) {
			return -1;
		}
		String nome=leggiNomeLibreria(email);
		System.out.println(email);
		System.out.println(nome);
		System.out.println(isbn);
		try{
			String query="SELECT * FROM libri_table WHERE ISBN=? AND nome_libreria=?";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, isbn);
			prstmt.setString(2, nome);
			System.out.println(prstmt.toString());
			ResultSet rs = prstmt.executeQuery();
			if(rs.next()==false) {
				return 2;
			}
			prstmt.close();
			rs.close();
		} catch(SQLException e){
			System.out.println("Errore. Qualcosa non va in checkOwnership.\n");
			e.printStackTrace();
			return -2;
		}
		return 0;
	}
	
	public int editLibroCatalogo(String campi[]) {
		if (!connectDatabase()) {
			return -1;
		}
		try{
			String query="UPDATE libri_table SET titolo=?,autore=?,casa_editrice=?,anno=?,lingua=?,prezzo=? WHERE ISBN=?;";
			PreparedStatement prstmt;
			prstmt = con.prepareStatement(query);
			prstmt.setString(1, campi[2]);
			prstmt.setString(2, campi[3]);
			prstmt.setString(3, campi[4]);
			prstmt.setString(4, campi[5]);
			prstmt.setString(5, campi[6]);
			prstmt.setDouble(6,Double.parseDouble(campi[7]));
			prstmt.setString(7, campi[1]);
			prstmt.executeUpdate();
			prstmt.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			return -1;
		}
		return 0;
	}

	public int editLibroVenditore(String[] campi) {
		if (!connectDatabase()) {
			return -1;
		}
		String nome=leggiNomeLibreria(campi[0]);
		try{
			String query="UPDATE libro_venditore SET sconto=?,copie=? WHERE ISBN=? AND nome=?;";
			PreparedStatement prstmt;
			prstmt = con.prepareStatement(query);
			prstmt.setDouble(1, Double.parseDouble(campi[1]));
			prstmt.setInt(2, Integer.parseInt(campi[2]));
			prstmt.setString(3, campi[3]);
			prstmt.setString(4, nome);
			prstmt.executeUpdate();
			prstmt.close();
		}catch(SQLException e){
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
	
	public int votaLibro(String ISBN, int voto, String nickname) {
		if (!connectDatabase()) {
			return -1;
		}
		try {
			String query = "SELECT * FROM voti_libri WHERE ISBN=? AND username=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, ISBN);
			prstmt.setString(2, nickname);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next()) {
				return 2;
			} else {
				String query2 = "INSERT INTO voti_libri(ISBN,username,voto) VALUES (?,?,?);";
				PreparedStatement prstmt2 = con.prepareStatement(query2);
				prstmt2.setString(1, ISBN);
				prstmt2.setString(2, nickname);
				prstmt2.setInt(3, voto);
				prstmt2.executeUpdate();
				prstmt2.close();
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return -2;
		}
		try {
			String query = "SELECT * FROM libri_table WHERE ISBN=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, ISBN);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next()) {
				query = "UPDATE libri_table SET voto=(SELECT AVG(voto) AS voto_media FROM voti_libri WHERE ISBN=?) WHERE ISBN=?";
				prstmt = con.prepareStatement(query);
				prstmt.setString(1, ISBN);
				prstmt.setString(2, ISBN);
				prstmt.executeUpdate();
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return -3;
		}
		return 1;
	}

	public int leggiVotoUtente(String isbn, String nickname) {
		int voto = 0;
		if (!connectDatabase()) {
			return voto;
		}
		try {
			String query = "SELECT voto FROM voti_libri WHERE ISBN=? AND username=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, isbn);
			prstmt.setString(2, nickname);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next()==false) {
				return voto;
			}
			voto = rs.getInt("voto");
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		return voto;
	}
	
	public String[] leggiScontoCopie(String ISBN) {
		String[] risultato = new String[3];
		if (!connectDatabase()) {
			return risultato;
		}
		try {
			String query = "SELECT titolo,sconto,copie FROM libro_venditore AS l JOIN (SELECT titolo,ISBN FROM libri_table WHERE ISBN=?) AS t on t.ISBN=l.ISBN;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, ISBN);
			ResultSet rs = prstmt.executeQuery();
			if (!rs.next()) {
				System.out.println("Il libro cercato non è presente.\n");
				return risultato;
			} else {
				risultato[0] = rs.getString("titolo");
				risultato[1] = rs.getString("sconto");
				risultato[2] = rs.getString("copie");
				
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		return risultato;
	}

	public String leggiNomeLibreria(String email) {
		String nome = "";
		if (!connectDatabase()) {
			return nome;
		}
		try {
			String query = "SELECT nome FROM vendors WHERE email=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, email);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next() ==false) {
				return nome;
			}
			nome = rs.getString("nome");
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		return nome;
	}

	public String[][] mostraVoti() {
		int numero = 0;
		String risultato[][] = null;
		if (!connectDatabase()) {
			return risultato;
		}
		numero=contaLibriMod();
		if (numero == 0 || numero < 0) {
			return null;
		}
		if(numero>0 && numero <=4) {
			risultato = new String[numero][3];
		}
		if(numero>4) {
			risultato = new String[4][3];
		}
		ResultSet rs;
		try {
			String query = "SELECT titolo,autore,voto FROM libri_table ORDER BY voto DESC;";
			PreparedStatement prstmt = con.prepareStatement(query);
			rs = prstmt.executeQuery();
			if (rs.next() == false) {
				return null;
			}
			rs.beforeFirst();			
			int i=0;
			while (rs.next()) {
				if(i<4) {
					for (int j = 0; j < 3; j++) {
						if (j == 0) {
							risultato[i][j] = rs.getString("titolo");
						} else if (j == 1) {
							risultato[i][j] = rs.getString("autore");
						} else if (j == 2) {
							risultato[i][j] = String.valueOf(rs.getDouble("voto"));
						}
					}
					i++;
				}
				else if(i==4) {
					break;
				}
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			return null;
		}
		return risultato;
	}
	
	public int contaLibriRicerca(String campo) {
		System.out.println("Campo conta: "+campo);
		int numero=0;
		if (!connectDatabase()) {
			return -1;
		}
		try {
			String query = "SELECT count(ISBN) AS numero FROM libri_table WHERE titolo LIKE ? OR autore LIKE ? OR anno LIKE ? "
					+ "OR casa_editrice LIKE ? OR ISBN LIKE ? OR voto LIKE ? OR lingua LIKE ?";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, "%"+campo+"%");
			prstmt.setString(2, "%"+campo+"%");
			prstmt.setString(3, "%"+campo+"%");
			prstmt.setString(4, "%"+campo+"%");
			prstmt.setString(5, "%"+campo+"%");
			prstmt.setString(6, "%"+campo+"%");
			prstmt.setString(7, "%"+campo+"%");
			ResultSet rs = prstmt.executeQuery();
			if (rs.next() != false) {
				numero = rs.getInt("numero");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return -2;
		}
		System.out.println("Numero risultati: "+numero);
		return numero;
	}
	
	int contaLibriMod() {
		int numero=0;
		if (!connectDatabase()) {
			return -1;
		}
		try {
			String query = "SELECT count(ISBN) AS numero FROM libri_table;";
			PreparedStatement prstmt = con.prepareStatement(query);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next() != false) {
				numero = rs.getInt("numero");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			return -2;
		}
		return numero;
	}

	/* Vengono prese gli ultimi quattro libri inseriti */
	public String[][] listaNovita() {
		int numero = 0;
		String[][] risultato = null;
		if (!connectDatabase()) {
			return null;
		}
		numero=contaLibriMod();
		if (numero == 0 || numero < 0) {
			return null;
		}
		if(numero>0 && numero <=4) {
			risultato = new String[numero][3];
		}
		if(numero>4) {
			risultato = new String[4][3];
		}
		ResultSet rs;
		try {
			String query = "SELECT titolo,autore,data_pubblicazione FROM libri_table ORDER BY data_pubblicazione DESC;";
			PreparedStatement prstmt = con.prepareStatement(query);
			rs = prstmt.executeQuery();
			if (rs.next()==false) {
				return null;
			}
			int i=0;
			rs.beforeFirst();
			while (rs.next()) {
				if (i<4) {
					for (int j = 0; j < 3; j++) {
						if (j == 0) {
							risultato[i][j] = rs.getString("titolo");
						} else if (j == 1) {
							risultato[i][j] = rs.getString("autore");
						} else if (j == 2) {
							risultato[i][j] = (rs.getTimestamp("data_pubblicazione")).toString();
						}
					}
					i++;
				}
				else if(i==4) {
					break;
				}
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			return null;
		}
		return risultato;
	}

	int contaRecensioni() {
		int numero = 0;
		if (!connectDatabase()) {
			return -1;
		}
		try {
			String query = "SELECT count(id) AS numero FROM commenti;";
			PreparedStatement prstmt = con.prepareStatement(query);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next()) {
				numero = rs.getInt("numero");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return -2;
		}
		return numero;
	}
	
	public String[][] ultimeRecensioni() {
		int numero = 0;
		String risultato[][] = null;
		numero=contaRecensioni();
		if (numero == 0 || numero < 0) {
			return null;
		}
		if(numero>0 && numero <=4) {
			risultato = new String[numero][3];
		}
		if(numero>4) {
			risultato = new String[4][3];
		}
		ResultSet rs;
		try {
			String query="SELECT titolo, autore, nickname FROM libri_table AS l JOIN commenti AS c ON c.ISBN = l.ISBN ORDER BY data";
			PreparedStatement prstmt = con.prepareStatement(query);
			rs = prstmt.executeQuery();
			if (rs.next() == false) {
				return null;
			}
			rs.beforeFirst();
			int i=0;
			while(rs.next()) {
				if(i<4) {
					for (int j = 0; j < 3; j++) {
						if(j == 0) {
							risultato[i][j] = rs.getString("nickname");
						}
						else if (j == 1) {
							risultato[i][j] = rs.getString("titolo");
						} else if (j == 2) {
							risultato[i][j] = rs.getString("autore");
						}
					}
					i++;
				}
				else if(i==4) {
					break;
				}
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			return null;
		}
		return risultato;
	}

	public int modificaRecensione(String nickname, String ISBN, String corpo) {
		if (!connectDatabase()) {
			return -1;
		}
		try {
			String query = "UPDATE commenti SET corpo_commento=?,data=CURRENT_TIMESTAMP WHERE ISBN=? AND nickname=?";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, corpo);
			prstmt.setString(2, ISBN);
			prstmt.setString(3, nickname);
			prstmt.executeUpdate();
			prstmt.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return -2;
		}
		return 0;
	}

	/*
	 * Questa funzione elimina una recensione all'interno della tabella
	 * 'commenti'
	 */
	public int rimuoviRecensione(String nickname, String ISBN) {
		if (!connectDatabase()) {
			return 2;
		}
		try {
			String query = "DELETE FROM commenti WHERE nickname=? AND ISBN=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nickname);
			prstmt.setString(2, ISBN);
			prstmt.executeUpdate();
			prstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			return 3;
		}
		return 1;
	}

	public int leggiNumeroRecensioni(String isbn) {
		int numero = 0;
		if (!connectDatabase()) {
			return numero;
		}
		try {
			String query = "SELECT count(id) AS numero FROM commenti WHERE ISBN=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, isbn);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next()) {
				numero = rs.getInt("numero");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return numero;
		}
		return numero;
	}

	public String[][] leggiRecensioniLibro(String isbn) {
		int numero = 0;
		String risultato[][] = null;
		if (!connectDatabase()) {
			return null;
		}
		numero = leggiNumeroRecensioni(isbn);
		risultato = new String[numero][4];
		try {
			String query = "SELECT corpo_commento,data,nickname,id FROM commenti WHERE ISBN=? ORDER BY data DESC;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, isbn);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next() == false) {
				return risultato;
			}
			int i=0;
			rs.beforeFirst();
			while (rs.next()) {
				if(i<numero) {
					for (int j = 0; j < 4; j++) {
						if (j == 0) {
							risultato[i][j] = rs.getString("corpo_commento");
						} else if (j == 1) {
							risultato[i][j] = rs.getString("data");
						} else if (j == 2) {
							risultato[i][j] = Integer.toString(rs.getInt("id"));
						} else if (j == 3) {
							risultato[i][j] = rs.getString("nickname");
						}
					}
				}
				i++;
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		for(int i=0;i<numero;i++) {
			for(int j=0;j<4;j++) {
				System.out.println("["+i+","+j+"]="+risultato[i][j]);
			}
		}
		return risultato;
	}

	public String[] leggiRecensione(String nickname, String isbn) {
		String[] risultato = new String[2];
		String titolo = "";
		String corpo = "";
		if (!connectDatabase()) {
			return risultato;
		}
		try {
			String query = "SELECT titolo FROM libri_table WHERE ISBN=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, isbn);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next()) {
				titolo = rs.getString("titolo");
			}
			else {
				return risultato;
			}
			query = "SELECT corpo_commento FROM commenti WHERE nickname=? AND ISBN=?;";
			prstmt = con.prepareStatement(query);
			prstmt.setString(1, nickname);
			prstmt.setString(2, isbn);
			rs = prstmt.executeQuery();
			if (rs.next()) {
				corpo = rs.getString("corpo_commento");
			}
			else {
				return risultato;
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		risultato[0] = titolo;
		risultato[1] = corpo;
		return risultato;
	}

	public String[][] leggiRecensioniUtente(String nickname) {
		int numero = 0;
		String result[][]=null;
		if (!connectDatabase()) {
			return result;
		}
		try {
			String query = "SELECT COUNT(id) AS numero FROM commenti WHERE nickname=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nickname);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next()) {
				numero = rs.getInt("numero");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return result;
		}
		if(numero==0) {
			return result;
		}
		else {
			result = new String[numero][4];
			ResultSet rs;
		try {
			String query = "SELECT * FROM libri_table JOIN (SELECT * FROM commenti WHERE nickname=? ORDER BY data DESC)as rec_utente on rec_utente.ISBN=libri_table.ISBN;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nickname);
			rs = prstmt.executeQuery();
			if (!rs.next()) {
				System.out.println("no rec");
				result[0][0] = "";
				return result;
			}
			rs.previous();
			int i=-1;
			while(rs.next()){
				i++;
				System.out.println(rs.getString("isbn"));
					for (int j = 0; j <= 3; j++) {
						if (j == 0) {
							result[i][j] = rs.getString("corpo_commento");
						} else if (j == 1) {
							result[i][j] = rs.getString("titolo");
						} else if (j == 2) {
							result[i][j] = rs.getString("data");
						} else if (j == 3) {
							result[i][j] = rs.getString("ISBN");
						}
					}
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			result=null;
			return result;
		}
		return result;
		}
	}

	/*
	 * Questa funzione si occupa di inserire all'interno della tabella
	 * 'commenti' le recensioni inserite su un determinato libro
	 */
	public boolean inserisciRecensione(String nickname, String ISBN,
			String corpo) {
		if (!connectDatabase()) {
			return false;
		}
		try {
			String query = "INSERT INTO commenti(corpo_commento,nickname,ISBN,data) VALUES(?,?,?,CURRENT_TIMESTAMP);";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, corpo);
			prstmt.setString(2, nickname);
			prstmt.setString(3, ISBN);
			prstmt.executeUpdate();
			prstmt.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	int contaLibri(String query) {
		int numero = 0;
		if (!connectDatabase()) {
			return numero;
		}
		query = query.substring(query.indexOf("SELECT *")+8); // Cioè prende la stringa a comincaire
										// da " FROM..." fino alla fine.
		query = "SELECT count(ISBN) as numero" + query;
		System.out.println("Query di conta"+query);
		try {
			PreparedStatement prstmt = con.prepareStatement(query);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next() == false) {
				return numero;
			} else {
				numero = rs.getInt("numero");
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			return numero;
		}
		return numero;
	}

	public String[][] ricercaAvanzata(String[] campi) {
		int numero = 0;
		String query = "";
		String[][] risultato = null;
		int titolo, autore, isbn, editore, anno, voto, lingua, prezzo;
		titolo = autore = isbn = editore = anno = voto = lingua = prezzo = -1;
		int cc=1;
		if (!connectDatabase()) {
			return risultato;
		}		
		try {
			for (int i = 0; i < campi.length; i++) {
				if (i == 0 && !((campi[i]).equals(""))) {
					titolo = cc++;
					query = "SELECT * FROM libri_table WHERE titolo=?";
				} else if (i == 1 && !((campi[i]).equals(""))) {				
					if (cc==1) {
						query = "SELECT * FROM libri_table WHERE autore=?";
					} else {
						query = query + " AND autore=?";
					}
					autore = cc++;
				} else if (i == 2 && !((campi[i]).equals(""))) {				
					if (cc==1) {
						query = "SELECT * FROM libri_table WHERE isbn=?";
					} else {
						query = query + " AND isbn=?";
					}
					isbn= cc++;
				} else if (i == 3 && !((campi[i]).equals(""))) {					
					if (cc==1) {
						query = "SELECT * FROM libri_table WHERE casa_editrice=?";
					} else {
						query = query + " AND casa_editrice=?";
					}
					editore = cc++;
				} else if (i == 4 && !((campi[i]).equals(""))) {					
					if (cc==1) {
						query = "SELECT * FROM libri_table WHERE anno=?";
					} else {
						query = query + " AND anno=?";
					}
					anno=cc++;
				} else if (i == 5 && !((campi[i]).equals(""))) {			
					if (cc==1) {
						query = "SELECT * FROM libri_table WHERE voto=?";
					} else {
						query = query + " AND voto=?";
					}
					voto=cc++;
				} else if (i == 6 && !((campi[i]).equals(""))) {					
					if (cc==1) {
						query = "SELECT * FROM libri_table WHERE lingua=?";
					} else {
						query = query + " AND lingua=?";
					}
					lingua=cc++;
				} else if (i == 7 && !((campi[i]).equals(""))) {					
					if (cc==1) {
						query = "SELECT * FROM libri_table WHERE prezzo=?";
					} else {
						query = query + " AND prezzo=?";
					}
					prezzo=cc++;
				}
			}
			System.out.println("titolo->"+titolo);
			System.out.println("contatore->"+cc);
			query = query + ";";
			PreparedStatement prstmt = con.prepareStatement(query);
			if (titolo!=-1) {
				prstmt.setString(titolo, campi[0]);
			}
			if (autore !=-1) {
				prstmt.setString(autore, campi[1]);
			}
			if (isbn !=-1) {
				prstmt.setString(isbn, campi[2]);
			}
			if (editore !=-1) {
				prstmt.setString(editore, campi[3]);
			}
			if (anno !=-1) {
				prstmt.setString(anno, campi[4]);
			}
			if (voto !=-1) {
				prstmt.setDouble(voto, (Double.valueOf(campi[5]).doubleValue()));
			}
			if (lingua !=-1) {
				prstmt.setString(lingua, campi[6]);
			}
			if (prezzo !=-1) {
				prstmt.setString(prezzo, campi[7]);
			}
			if ((numero = contaLibri(prstmt.toString())) == 0) {
				return risultato;
			}
			risultato = new String[numero][8];
			ResultSet rs = prstmt.executeQuery();
			if (!(rs.next())) {
				System.out.println("Nessun risultato trovato.\n");
				return risultato;
			}
			int i = 0;
			rs.beforeFirst();
			while (rs.next()) {
				if (i < numero) {
					risultato[i][0] = rs.getString("titolo");
					risultato[i][1] = rs.getString("autore");
					risultato[i][2] = rs.getString("anno");
					risultato[i][3] = rs.getString("casa_editrice");
					risultato[i][4] = rs.getString("ISBN");
					risultato[i][5] = rs.getString("prezzo");
					risultato[i][6] = rs.getString("lingua");
					risultato[i][7] = String.valueOf(rs.getDouble("voto"));
				}
				i++;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			return risultato;
		}
		return risultato;
	}

	public String[][] ricerca(String campo) {
		System.out.println("Campo ricerca: "+campo);
		int numero = 0;
		String[][] risultato = null;
		if (!connectDatabase()) {
			return risultato;
		}
		try {
			String query = "SELECT * FROM libri_table WHERE titolo LIKE ? OR autore LIKE ? OR anno LIKE ? "
					+ "OR casa_editrice LIKE ? OR ISBN LIKE ? OR voto LIKE ? OR lingua LIKE ?";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, "%"+campo+"%");
			prstmt.setString(2, "%"+campo+"%");
			prstmt.setString(3, "%"+campo+"%");
			prstmt.setString(4, "%"+campo+"%");
			prstmt.setString(5, "%"+campo+"%");
			prstmt.setString(6, "%"+campo+"%");
			prstmt.setString(7, "%"+campo+"%");
			ResultSet rs = prstmt.executeQuery();
			System.out.println(prstmt.toString());
			if ((numero = contaLibri(prstmt.toString())) == 0) {
				return risultato;
			}
			risultato = new String[numero][8];
			if (rs.next() == false) {
				System.out.println("Nessun risultato trovato.\n");
				return risultato;
			}
			rs.beforeFirst();
			int i=0;
			while (rs.next()) {
				if(i<numero) {
					risultato[i][0] = rs.getString("titolo");
					risultato[i][1] = rs.getString("autore");
					risultato[i][2] = rs.getString("anno");
					risultato[i][3] = rs.getString("casa_editrice");
					risultato[i][4] = rs.getString("ISBN");
					risultato[i][5] = rs.getString("prezzo");
					risultato[i][6] = rs.getString("lingua");
					risultato[i][7] = String.valueOf(rs.getString("voto"));
				}
				i++;
			}	
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		System.out.println("tutto okei\n");
		for (int i = 0; i < numero; i++) {
			for (int j = 0; j < 8; j++) {
				System.out.println("["+i+","+j+"]="+risultato[i][j]);
			}
			System.out.println("FINITO "+i);
		}
		return risultato;
	}

	public String[][] ricercaTitolo(String campo) {
		int numero = 0;
		String[][] risultato = null;
		if (!connectDatabase()) {
			return risultato;
		}
		try {
			String query = "SELECT * FROM libri_table WHERE titolo=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, campo);
			ResultSet rs = prstmt.executeQuery();
			if ((numero = contaLibri(prstmt.toString())) == 0) {
				return risultato;
			}
			risultato = new String[numero][8];
			if (!(rs.next())) {
				System.out.println("Nessun risultato trovato.\n");
				return risultato;
			}
			rs.beforeFirst();
			int i = 0;
			while (rs.next()) {
				if (i < numero) {
					risultato[i][0] = rs.getString("titolo");
					risultato[i][1] = rs.getString("autore");
					risultato[i][2] = rs.getString("anno");
					risultato[i][3] = rs.getString("casa_editrice");
					risultato[i][4] = rs.getString("ISBN");
					risultato[i][5] = rs.getString("prezzo");
					risultato[i][6] = rs.getString("lingua");
					risultato[i][7] = String.valueOf(rs.getString("voto"));
				}
				i++;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		return risultato;
	}

	public String[][] ricercaAutore(String campo) {
		int numero = 0;
		String[][] risultato = null;
		if (!connectDatabase()) {
			return risultato;
		}
		try {
			String query = "SELECT * FROM libri_table WHERE autore=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, campo);
			ResultSet rs = prstmt.executeQuery();
			if ((numero = contaLibri(prstmt.toString())) == 0) {
				return risultato;
			}
			risultato = new String[numero][8];
			if (!(rs.next())) {
				System.out.println("Nessun risultato trovato.\n");
				return risultato;
			}
			rs.beforeFirst();
			int i = 0;
			while (rs.next()) {
				if (i < numero) {
					risultato[i][0] = rs.getString("titolo");
					risultato[i][1] = rs.getString("autore");
					risultato[i][2] = rs.getString("anno");
					risultato[i][3] = rs.getString("casa_editrice");
					risultato[i][4] = rs.getString("ISBN");
					risultato[i][5] = rs.getString("prezzo");
					risultato[i][6] = rs.getString("lingua");
					risultato[i][7] = String.valueOf(rs.getString("voto"));
				}
				i++;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		return risultato;
	}

	public String[][] ricercaISBN(String campo) {
		int numero = 0;
		String[][] risultato = null;
		if (!connectDatabase()) {
			return risultato;
		}
		try {
			String query = "SELECT * FROM libri_table WHERE ISBN=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, campo);
			ResultSet rs = prstmt.executeQuery();
			if ((numero = contaLibri(prstmt.toString())) == 0) {
				return risultato;
			}
			risultato = new String[numero][8];
			if (!(rs.next())) {
				System.out.println("Nessun risultato trovato.\n");
				return risultato;
			}
			rs.beforeFirst();
			int i = 0;
			while (rs.next()) {
				if (i < numero) {
					risultato[i][0] = rs.getString("titolo");
					risultato[i][1] = rs.getString("autore");
					risultato[i][2] = rs.getString("anno");
					risultato[i][3] = rs.getString("casa_editrice");
					risultato[i][4] = rs.getString("ISBN");
					risultato[i][5] = rs.getString("prezzo");
					risultato[i][6] = rs.getString("lingua");
					risultato[i][7] = String.valueOf(rs.getString("voto"));
				}
				i++;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		return risultato;
	}

	public String[][] ricercaAnno(String campo) {
		int numero = 0;
		String[][] risultato = null;
		if (!connectDatabase()) {
			return risultato;
		}
		try {
			String query = "SELECT * FROM libri_table WHERE anno=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, campo);
			ResultSet rs = prstmt.executeQuery();
			if ((numero = contaLibri(prstmt.toString())) == 0) {
				return risultato;
			}
			risultato = new String[numero][8];
			if (!(rs.next())) {
				System.out.println("Nessun risultato trovato.\n");
				return risultato;
			}
			rs.beforeFirst();
			int i = 0;
			while (rs.next()) {
				if (i < numero) {
					risultato[i][0] = rs.getString("titolo");
					risultato[i][1] = rs.getString("autore");
					risultato[i][2] = rs.getString("anno");
					risultato[i][3] = rs.getString("casa_editrice");
					risultato[i][4] = rs.getString("ISBN");
					risultato[i][5] = rs.getString("prezzo");
					risultato[i][6] = rs.getString("lingua");
					risultato[i][7] = String.valueOf(rs.getString("voto"));
				}
				i++;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		return risultato;
	}

	public String[][] ricercaCasaEditrice(String campo) {
		int numero = 0;
		String[][] risultato = null;
		if (!connectDatabase()) {
			return risultato;
		}
		try {
			String query = "SELECT * FROM libri_table WHERE casa_editrice=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, campo);
			ResultSet rs = prstmt.executeQuery();
			if ((numero = contaLibri(prstmt.toString())) == 0) {
				return risultato;
			}
			risultato = new String[numero][8];
			if (!(rs.next())) {
				System.out.println("Nessun risultato trovato.\n");
				return risultato;
			}
			rs.beforeFirst();
			int i = 0;
			while (rs.next()) {
				if (i < numero) {
					risultato[i][0] = rs.getString("titolo");
					risultato[i][1] = rs.getString("autore");
					risultato[i][2] = rs.getString("anno");
					risultato[i][3] = rs.getString("casa_editrice");
					risultato[i][4] = rs.getString("ISBN");
					risultato[i][5] = rs.getString("prezzo");
					risultato[i][6] = rs.getString("lingua");
					risultato[i][7] = String.valueOf(rs.getString("voto"));
				}
				i++;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		return risultato;
	}

	public String[][] ricercaLingua(String campo) {
		int numero = 0;
		String[][] risultato = null;
		if (!connectDatabase()) {
			return risultato;
		}
		try {
			String query = "SELECT * FROM libri_table WHERE lingua=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, campo);
			ResultSet rs = prstmt.executeQuery();
			if ((numero = contaLibri(prstmt.toString())) == 0) {
				return risultato;
			}
			risultato = new String[numero][8];
			if (!(rs.next())) {
				System.out.println("Nessun risultato trovato.\n");
				return risultato;
			}
			rs.beforeFirst();
			int i = 0;
			while (rs.next()) {
				if (i < numero) {
					risultato[i][0] = rs.getString("titolo");
					risultato[i][1] = rs.getString("autore");
					risultato[i][2] = rs.getString("anno");
					risultato[i][3] = rs.getString("casa_editrice");
					risultato[i][4] = rs.getString("ISBN");
					risultato[i][5] = rs.getString("prezzo");
					risultato[i][6] = rs.getString("lingua");
					risultato[i][7] = String.valueOf(rs.getString("voto"));
				}
				i++;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		return risultato;
	}

	public String[][] ricercaLibriLibreria(String nomeLibreria) {
		String[][] risultato = null;
		int numero = 0;
		if (!connectDatabase()) {
			return risultato;
		}
		try {
			String query = "SELECT count(ISBN) as numero FROM libro_venditore WHERE nome=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nomeLibreria);
			ResultSet rs = prstmt.executeQuery();
			rs.next();
			if (rs.getInt("numero") == 0) {
				return risultato;
			} else {
				numero = rs.getInt("numero");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		risultato = new String[numero][9];
		try {
			String query = "SELECT ISBN,sconto,copie FROM libro_venditore WHERE nome=? ORDER BY ISBN;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nomeLibreria);
			ResultSet rs = prstmt.executeQuery();
			int i=0;
			while (rs.next()) {
				if(i<numero) {
					risultato[i][1] = rs.getString("ISBN");
					risultato[i][6] = rs.getString("sconto");
					risultato[i][7] = rs.getString("copie");
				}
				i++;
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		try {
			String query = "SELECT titolo,autore,casa_editrice,prezzo,lingua,anno FROM libri_table WHERE ISBN = ANY("
					+ "SELECT ISBN FROM libro_venditore WHERE nome=? ORDER BY ISBN);";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nomeLibreria);
			ResultSet rs = prstmt.executeQuery();
			System.out.println(prstmt.toString());
			int i=0;
			while (rs.next()) {
				if(i<numero) {
					risultato[i][0] = rs.getString("titolo");
					risultato[i][2] = rs.getString("autore");
					risultato[i][3] = rs.getString("casa_editrice");
					risultato[i][4] = rs.getString("prezzo");
					risultato[i][5] = rs.getString("lingua");
					risultato[i][8] = rs.getString("anno");
				}
				i++;
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		for(int i=0;i<numero;i++) {
			for(int j=0;j<9;j++) {
				System.out.println("["+i+","+j+"]="+risultato[i][j]);
			}
		}
		return risultato;
	}

	public String[][] ricercaVoto(String campo) {
		int numero = 0;
		String[][] risultato = null;
		if (!connectDatabase()) {
			return risultato;
		}
		try {
			String query = "SELECT * FROM libri_table WHERE voto=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, campo);
			ResultSet rs = prstmt.executeQuery();
			if ((numero = contaLibri(prstmt.toString())) == 0) {
				return risultato;
			}
			risultato = new String[numero][8];
			if (!(rs.next())) {
				System.out.println("Nessun risultato trovato.\n");
				return risultato;
			}
			rs.beforeFirst();
			int i = 0;
			while (rs.next()) {
				if (i < numero) {
					risultato[i][0] = rs.getString("titolo");
					risultato[i][1] = rs.getString("autore");
					risultato[i][2] = rs.getString("anno");
					risultato[i][3] = rs.getString("casa_editrice");
					risultato[i][4] = rs.getString("ISBN");
					risultato[i][5] = rs.getString("prezzo");
					risultato[i][6] = rs.getString("lingua");
					risultato[i][7] = String.valueOf(rs.getDouble("voto"));
				}
				i++;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		return risultato;
	}

	public String[][] ricercaPrezzo(String campo) {
		int numero = 0;
		String[][] risultato = null;
		if (!connectDatabase()) {
			return risultato;
		}
		try {
			String query = "SELECT * FROM libri_table WHERE prezzo=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, campo);
			ResultSet rs = prstmt.executeQuery();
			if ((numero = contaLibri(prstmt.toString())) == 0) {
				return risultato;
			}
			risultato = new String[numero][8];
			if (!(rs.next())) {
				System.out.println("Nessun risultato trovato.\n");
				return risultato;
			}
			rs.beforeFirst();
			int i = 0;
			while (rs.next()) {
				if (i < numero) {
					risultato[i][0] = rs.getString("titolo");
					risultato[i][1] = rs.getString("autore");
					risultato[i][2] = rs.getString("anno");
					risultato[i][3] = rs.getString("casa_editrice");
					risultato[i][4] = rs.getString("ISBN");
					risultato[i][5] = rs.getString("prezzo");
					risultato[i][6] = rs.getString("lingua");
					risultato[i][7] = String.valueOf(rs.getDouble("voto"));
				}
				i++;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		return risultato;
	}

	public int uploadAvatar(String percorso,String image,String username) {
		if (!connectDatabase()) {
			return -1;
		}
		String address="/var/www/"+percorso;
		try {
			System.out.println(address);
			Base64 decoder=new Base64();
			byte[] imgBytes=decoder.decode(image);
			File of = new File(address);
			FileOutputStream osf = new FileOutputStream(of);  
			osf.write(imgBytes);  
			osf.flush();
			osf.close();
		} catch(IOException e) {
			e.printStackTrace();
			return -2;
		}
		try {
			String query="UPDATE users SET location=? WHERE nickname=?";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1,percorso);
			prstmt.setString(2,username);
			prstmt.executeUpdate();
			prstmt.close();
		} catch(SQLException e1) {
			e1.printStackTrace();
			return -3;
		}
		return 0;
	}
	
	public String cercaAvatar(String username) {
		String risultato=null;
		if (!connectDatabase()) {
			return risultato;
		}
		try {
			String query="SELECT location FROM users WHERE nickname=?";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1,username);
			ResultSet rs=prstmt.executeQuery();
			if(rs.next()==false) {
				return risultato;
			}
			else {
				risultato=rs.getString("location");
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return risultato;
		}
		return risultato;
	}
}

