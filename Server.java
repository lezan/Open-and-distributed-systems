package Libreria;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/*import java.sql.Statement; */

public class Server {
		
	public String iscrizione(String nickuser,String passwd,String passwdc,String email) {
		Connection con = null;
		String username=""; /*username database mysql */
		String password=""; /*password database mysql */
		String database=""; /*database mysql */
		String serverName=""; /*sarà localhost */
		/* Mettere controllo validità email. Esempio: contiene più di 8 caratteri */
		if (!(checkPassword(passwd))) {
			return "Password non corretta. Inserire almeno 8 caratteri";
		}
		if (!(passwd.equals(passwdc))) {
			return "Le password inserite non coincidono";
		}
		System.out.println("Password corretta.\n");
		try {
			String url="jdbc:mysql://" + serverName + "/" + database;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(url, username, password);
			System.out.println("Connessione al database avvenuta correttamente.\n");
			try {
				/* Controllo se il nick è già utilizzato */
				String query = "SELECT nickuser FROM iscritti WHERE nickuser=?";
				PreparedStatement prstmt = con.prepareStatement(query);
				ResultSet rs = prstmt.executeQuery();
				if (rs != null) {
					System.out.println("Nick già utilizzato. Scegliere un altro nick.\n");
					return "Nick già utilizzato";
				}
				/* Controllo se l'email è già utilizzata */
				query = "SELECT email FROM iscritti WHERE email=?";
				PreparedStatement prstmt2 = con.prepareStatement(query);
				rs = prstmt2.executeQuery();
				if (rs != null) {
					System.out.println("Email già utilizzata. Scegliere un'altra email.\n");
					return "Email già utilizzata";
				}
				/* Nick ed email inseriti non sono utilizzati */
				System.out.println("Nick ed email validi.");
				query = "INSERT INTO iscritti (nickuser,password,email) VALUES (?,?,?)";
				PreparedStatement prstmt3 = con.prepareStatement(query);
				prstmt3.setString(1,nickuser);
				prstmt3.setString(2,passwd);
				prstmt3.setString(3,email);
				prstmt3.executeUpdate();
				rs.close();
				prstmt.close();
				prstmt2.close();
				prstmt3.close();
				con.close();
			} catch (SQLException e) {
				System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
				return "Errore" + e.getMessage();
			}
		} catch (Exception e) {
			System.err.println("Errore: impossibile connettersi al database.\n");
			return "Errore" + e.getMessage();
		}
		return "Registrazione avvenuta con successo";
	}
	
	public int login(String nickuser,String passwd) {
		Connection con = null;
		String username=""; /*username database mysql */
		String password=""; /*password database mysql */
		String database=""; /*database mysql */
		String serverName=""; /*localhost */
		try {
			String url="jdbc:mysql://" + serverName + "/" + database;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(url, username, password);
			System.out.println("Connessione al database avvenuta correttamente.\n");
			try {
				String query = "SELECT nick FROM iscritti WHERE nickname=? AND password=?";
				PreparedStatement prstmt = con.prepareStatement(query);
				prstmt.setString(1,nickuser);
				prstmt.setString(2,passwd);
				ResultSet rs = prstmt.executeQuery();
				if (rs != null) {
					System.out.println("Login effettuato con successo.\n");
				}
				else {
					System.out.println("Errore: nick o password non corretti.\n");
					return -1;
				}
				rs.close();
				prstmt.close();
				con.close();
			} catch (SQLException e) {
				System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
				e.printStackTrace();
				return 1;
			}
		} catch (Exception e) {
			System.err.println("Errore: impossibile connettersi al database.\n");
			System.out.println( e.getMessage() );
			e.printStackTrace();
			return 2;
		}
		return 0;
	}
	
	public boolean checkPassword(String password) {
		if (password.length() >= 8 ) {
			return true;
		}
		else {
			System.out.println("Errore. La password deve contenere più di 8 caratteri.\n");
			return false;
		}
	}
/* Funzioni per la ricerca nel database dei libri.
 * Abbiamo 3 funzioni:
 *  - ricerca: cerca in tutti i campi della tabella libri_tabella;
 *  - ricercaTItolo: cerca solo nel campo titolo della tabella libri_tabella;
 *  - ricercaAutore: cerca solo nel campo autore della tabella libri_tabella;
 *  Capire come sfruttare il campo anno e casa_editrice della tabella libri_tabella
 * (forse con altre due nuove ricerche?).
 * Sarà php a decidere quale tipo di ricerca fare, cioè quale metodo chiamare.
 * Aggiungere ricerca per ISBN.
 */
	public void ricerca(String campo) {
		Connection con = null;
		String username=""; /*username database mysql */
		String password=""; /*password database mysql */
		String database=""; /*database mysql */
		String serverName=""; /*sarà localhost */
		try {
			String url="jdbc:mysql://" + serverName + "/" + database;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(url, username, password);
			System.out.println("Connessione al database avvenuta correttamente.\n");
			try {
				String query = "SELECT * FROM libri_table WHERE titolo=? OR autore=? OR anno=? " +
						"OR casa_editrice=?;";
				ArrayList<Libro> libro = new ArrayList<Libro>();
				PreparedStatement prstmt = con.prepareStatement(query);
				prstmt.setString(1,campo);
				prstmt.setString(2,campo);
				prstmt.setString(3,campo);
				prstmt.setString(4,campo);
				ResultSet rs = prstmt.executeQuery();
				if (rs != null) {
					while(rs.next()) {
						libro.add(new Libro(rs.getString("id"),rs.getString("titolo"),rs.getString("autore"),rs.getString("anno"),rs.getString("casa_editrice"),rs.getString("ISBN")));
					}
				}
				else {
					System.out.println("Nessun risultato trovato.\n");
				}
			} catch (SQLException e) {
				System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.err.println("Errore: impossibile connettersi al database.\n");
			System.out.println( e.getMessage() );
			e.printStackTrace();
		}
	}
	
	public void ricercaTitolo(String campo) {
		Connection con = null;
		String username=""; /*username database mysql */
		String password=""; /*password database mysql */
		String database=""; /*database mysql */
		String serverName=""; /*sarà localhost */
		try {
			String url="jdbc:mysql://" + serverName + "/" + database;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(url, username, password);
			System.out.println("Connessione al database avvenuta correttamente.\n");
			try {
				String query = "SELECT * FROM libri_table WHERE titolo=?;";
				ArrayList<Libro> libro = new ArrayList<Libro>();
				PreparedStatement prstmt = con.prepareStatement(query);
				prstmt.setString(1,campo);
				ResultSet rs = prstmt.executeQuery();
				if (rs != null) {
					while(rs.next()) {
						libro.add(new Libro(rs.getString("id"),rs.getString("titolo"),rs.getString("autore"),rs.getString("anno"),rs.getString("casa_editrice"),rs.getString("ISBN")));
					}
				}
				else {
					System.out.println("Nessun risultato trovato.\n");
				}
			} catch (SQLException e) {
				System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.err.println("Errore: impossibile connettersi al database.\n");
			System.out.println( e.getMessage() );
			e.printStackTrace();
		}		
	}	
	
	public void ricercaAutore(String campo) {
		Connection con = null;
		String username=""; /*username database mysql */
		String password=""; /*password database mysql */
		String database=""; /*database mysql */
		String serverName=""; /*sarà localhost */
		try {
			String url="jdbc:mysql://" + serverName + "/" + database;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(url, username, password);
			System.out.println("Connessione al database avvenuta correttamente.\n");
			try {
				String query = "SELECT * FROM libri_table WHERE autore=?;";
				ArrayList<Libro> libro = new ArrayList<Libro>();
				PreparedStatement prstmt = con.prepareStatement(query);
				prstmt.setString(1,campo);
				ResultSet rs = prstmt.executeQuery();
				if (rs != null) {
					while(rs.next()) {
						libro.add(new Libro(rs.getString("id"),rs.getString("titolo"),rs.getString("autore"),rs.getString("anno"),rs.getString("casa_editrice"),rs.getString("ISBN")));
					}
				}
				else {
					System.out.println("Nessun risultato trovato.\n");
				}
			} catch (SQLException e) {
				System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.err.println("Errore: impossibile connettersi al database.\n");
			System.out.println( e.getMessage() );
			e.printStackTrace();
		}
	}
	
	public void ricercaISBN(String campo) {
		Connection con = null;
		String username=""; /*username database mysql */
		String password=""; /*password database mysql */
		String database=""; /*database mysql */
		String serverName=""; /*sarà localhost */
		try {
			String url="jdbc:mysql://" + serverName + "/" + database;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(url, username, password);
			System.out.println("Connessione al database avvenuta correttamente.\n");
			try {
				String query = "SELECT * FROM libri_table WHERE ISBN=?;";
				ArrayList<Libro> libro = new ArrayList<Libro>();
				PreparedStatement prstmt = con.prepareStatement(query);
				prstmt.setString(1,campo);
				ResultSet rs = prstmt.executeQuery();
				if (rs != null) {
					while(rs.next()) {
						libro.add(new Libro(rs.getString("id"),rs.getString("titolo"),rs.getString("autore"),rs.getString("anno"),rs.getString("casa_editrice"),rs.getString("ISBN")));
					}
				}
				else {
					System.out.println("Nessun risultato trovato.\n");
				}
			} catch (SQLException e) {
				System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.err.println("Errore: impossibile connettersi al database.\n");
			System.out.println( e.getMessage() );
			e.printStackTrace();
		}
	}
	
	public void ricercaAnno(String campo) {
		Connection con = null;
		String username=""; /*username database mysql */
		String password=""; /*password database mysql */
		String database=""; /*database mysql */
		String serverName=""; /*sarà localhost */
		try {
			String url="jdbc:mysql://" + serverName + "/" + database;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(url, username, password);
			System.out.println("Connessione al database avvenuta correttamente.\n");
			try {
				String query = "SELECT * FROM libri_table WHERE anno=?;";
				ArrayList<Libro> libro = new ArrayList<Libro>();
				PreparedStatement prstmt = con.prepareStatement(query);
				prstmt.setString(1,campo);
				ResultSet rs = prstmt.executeQuery();
				if (rs != null) {
					while(rs.next()) {
						libro.add(new Libro(rs.getString("id"),rs.getString("titolo"),rs.getString("autore"),rs.getString("anno"),rs.getString("casa_editrice"),rs.getString("ISBN")));
					}
				}
				else {
					System.out.println("Nessun risultato trovato.\n");
				}
			} catch (SQLException e) {
				System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.err.println("Errore: impossibile connettersi al database.\n");
			System.out.println( e.getMessage() );
			e.printStackTrace();
		}
	}
	
	public void ricercaCasaEditrice(String campo) {
		Connection con = null;
		String username=""; /*username database mysql */
		String password=""; /*password database mysql */
		String database=""; /*database mysql */
		String serverName=""; /*sarà localhost */
		try {
			String url="jdbc:mysql://" + serverName + "/" + database;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(url, username, password);
			System.out.println("Connessione al database avvenuta correttamente.\n");
			try {
				String query = "SELECT * FROM libri_table WHERE casa_editrice=?;";
				ArrayList<Libro> libro = new ArrayList<Libro>();
				PreparedStatement prstmt = con.prepareStatement(query);
				prstmt.setString(1,campo);
				ResultSet rs = prstmt.executeQuery();
				if (rs != null) {
					while(rs.next()) {
						libro.add(new Libro(rs.getString("id"),rs.getString("titolo"),rs.getString("autore"),rs.getString("anno"),rs.getString("casa_editrice"),rs.getString("ISBN")));
					}
				}
				else {
					System.out.println("Nessun risultato trovato.\n");
				}
			} catch (SQLException e) {
				System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.err.println("Errore: impossibile connettersi al database.\n");
			System.out.println( e.getMessage() );
			e.printStackTrace();
		}
	}
}
