package Libreria;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/*import java.sql.Statement; */

/*Aggiungere possibilità di votare i libri*/
/*Aggiungere recensione ai libri*/
/*Commentare i libri*/

public class Server {

	Connection con = null;

	boolean connectDatabase() {
		if (con == null) {
			String username = "root"; /* username database mysql */
			String password = "lezan"; /* password database mysql */
			String database = "libreria"; /* database mysql */
			String serverName = "localhost:3306"; /* sarà localhost */
			String url = "jdbc:mysql://" + serverName + "/" + database;
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				con = DriverManager.getConnection(url, username, password);
				System.out
						.println("Connessione al database avvenuta correttamente.\n");
				return true;
			} catch (Exception e) {
				System.out
						.println("Errore: impossibile connettersi al database.\n");
				System.out.println("Errore" + e.getMessage());
				return false;
			}
		} else {
			return true;
		}
	}

	public int iscrizioneUser(String nickuser, String passwd, String passwdc,
			String email) {
		/*
		 * Mettere controllo validità email. Esempio: contiene più di 8
		 * caratteri
		 */
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
			ResultSet rs = prstmt.executeQuery();
			if (rs != null) {
				System.out
						.println("Nick già utilizzato. Scegliere un altro nick.\n");
				return 5;
			}
			/* Controllo se l'email è già utilizzata */
			query = "SELECT email FROM users WHERE email=?";
			PreparedStatement prstmt2 = con.prepareStatement(query);
			prstmt.setString(1, email);
			rs = prstmt2.executeQuery();
			if (rs != null) {
				System.out
						.println("Email già utilizzata. Scegliere un'altra email.\n");
				return 6;
			}
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
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			return 7;
		}
		return 1;
	}

	public int iscrizioneLibrerie(String nome, String passwd, String passwdc,
			String email, String indirizzo, String citta, String cap,
			String partitaIva) {
		/*
		 * Mettere controllo validità email. Esempio: contiene più di 8
		 * caratteri
		 */
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
			if (rs != null) {
				System.out
						.println("Nome già utilizzato. Scegliere un altro nome.\n");
				return 5;
			}
			/* Controllo se l'email è già utilizzata */
			query = "SELECT email FROM vendors WHERE email=?";
			PreparedStatement prstmt2 = con.prepareStatement(query);
			prstmt2.setString(1, email);
			rs = prstmt2.executeQuery();
			if (rs != null) {
				System.out
						.println("Email già utilizzata. Scegliere un'altra email.\n");
				return 6;
			}
			/* Email inserita non è utilizzata */
			query = "SELECT partita_iva FROM vendors WHERE partita_iva=?";
			PreparedStatement prstmt3 = con.prepareStatement(query);
			prstmt.setString(1, partitaIva);
			rs = prstmt3.executeQuery();
			if (rs != null) {
				System.out
						.println("Partita iva già utilizzata. Scegliere un'altra partita iva.\n");
				return 7;
			}
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
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			return 8;
		}
		return 1;
	}

	public int loginUsers(String nickuser, String passwd) {
		if (!connectDatabase()) {
			return -2;
		}
		try {
			String query = "SELECT nickname FROM users WHERE nickname=? AND password=?";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nickuser);
			prstmt.setString(2, passwd);
			ResultSet rs = prstmt.executeQuery();
			if (rs != null) {
				System.out.println("Login effettuato con successo.\n");
			} else {
				System.out.println("Errore: nick o password non corretti.\n");
				return -1;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return 1;
		}
		return 0;
	}

	/* Facciamo che le librerie si loggano con l'email e la password?! */
	public int loginLibrerie(String email, String passwd) {
		if (!connectDatabase()) {
			return -2;
		}
		try {
			String query = "SELECT nome FROM vendors WHERE email=? AND password=?";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, email);
			prstmt.setString(2, passwd);
			ResultSet rs = prstmt.executeQuery();
			if (rs != null) {
				System.out.println("Login effettuato con successo.\n");
			} else {
				System.out.println("Errore: nick o password non corretti.\n");
				return -1;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return 1;
		}
		return 0;
	}

	/*
	 * public enum Nome { password, email, indirizzo, citta, cap, telefono,
	 * telefono_2, nome, cognome; }
	 * 
	 * public boolean editUser(String nomeCampo, String campo) {
	 * switch(Nome.valueOf(nomeCampo)) { case password: break; } return true; }
	 */
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
			if (rs != null) {
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

	boolean checkPasswordLibrerie(String partitaIva, String passwd) {
		if (!connectDatabase()) {
			return false;
		}
		try {
			String query = "SELECT nome FROM vendors WHERE partita_iva=? AND password=?";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, partitaIva);
			prstmt.setString(2, passwd);
			ResultSet rs = prstmt.executeQuery();
			if (rs != null) {
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
			if (rs == null) {
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

	boolean checkEmailLibrerie(String email) {
		if (!connectDatabase()) {
			return false;
		}
		try {
			String query = "SELECT nome FROM vendors WHERE email=?";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, email);
			ResultSet rs = prstmt.executeQuery();
			if (rs == null) {
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

	/*
	 * Decidere come fare le return perché così funziona a merda. Inoltre, in
	 * questo modo si può modificare solo un campo alla volta; cercare un'altro
	 * metodo per modificare più campi alla volta. Forse con due array campo[] e
	 * nomeCampo[]?!
	 */

	public int editCampiUser(String[] campi) {
		if (!connectDatabase()) {
			return -1;
		}
		// boolean
		// password,email,data,nome,cognome,indirizzoUtente,cittaUtente,capUtente,telefono,telefono2;
		// password=email=data=nome=cognome=indirizzoUtente=cittaUtente=capUtente=telefono=telefono2=false;
		boolean primo = true;
		String query = "";
		String dataComposta = "";
		try {
			for (int i = 0; i < 13; i++) {
				if (i == 0) {
					if (checkPasswordUsers(campi[13], campi[i])) {
						if (!(campi[i + 1].equals(""))
								&& checkPassword(campi[i + 1])) {
							primo = false;
							// password=true;
							query = "UPDATE users SET password=?";
						} else {
							return 1; // Errore nella nuova password
						}
					} else {
						return 2; // Errore nella vecchia password
					}
					i++; // Così salta i==1, dove c'è la nuova password e non
							// viene usato nel ciclo for
				} else if (i == 2) {
					if (!(campi[i].contains("@"))) {
						if (checkEmailUsers(campi[i])) {
							// email=true;
							if (primo) {
								query = "UPDATE users SET email=?";
								primo = false;
							} else {
								query = query + ",email=?";
							}
						} else {
							return 3; // email inserita è già usata
						}
					} else {
						return 4; // Email non valida
					}
				} else if (i == 3) {
					if (!(campi[i].equals("- -"))) {
						if (!(campi[i + 1].equals("- -"))) {
							if (!(campi[i + 2].equals("- -"))) {
								// data=true;
								if (primo) {
									primo = false;
									dataComposta = campi[i] + "/"
											+ campi[i + 1] + "/" + campi[i + 2];
									query = "UPDATE users SET data_nascita=?";
								} else {
									query = query + ",data_nascita=?";
								}
							}
						}
					}
					i = i + 2; // Salto il campo mese e giorno che ho già preso
								// quando i=3. Salto i=4 e i=5
				} else if (i == 6) {
					if (!(campi[i].equals(""))) {
						// nome = true;
						if (primo) {
							primo = false;
							query = "UPDATE users SET nome=?";
						} else {
							query = query + ",nome=?";
						}
					}
				} else if (i == 7) {
					if (!(campi[i].equals(""))) {
						// cognome = true;
						if (primo) {
							primo = false;
							query = "UPDATE users SET cognome=?";
						} else {
							query = query + ",cognome=?";
						}
					}
				} else if (i == 8) {
					if (!(campi[i].equals(""))) {
						// indirizzoUtente = true;
						if (primo) {
							primo = false;
							query = "UPDATE users SET indirizzo=?";
						} else {
							query = query + ",indirizzo=?";
						}
					}
				} else if (i == 9) {
					if (!(campi[i].equals(""))) {
						// cittaUtente = true;
						if (primo) {
							primo = false;
							query = "UPDATE users SET citta=?";
						} else {
							query = query + ",citta=?";
						}
					}
				} else if (i == 10) {
					if (!(campi[i].equals(""))) {
						// capUtente = true;
						if (primo) {
							primo = false;
							query = "UPDATE users SET cap=?";
						} else {
							query = query + ",cap=?";
						}
					}
				} else if (i == 11) {
					if (!(campi[i].equals(""))) {
						// telefono = true;
						if (primo) {
							primo = false;
							query = "UPDATE users SET telefono=?";
						} else {
							query = query + ",telefono=?";
						}
					}
				} else if (i == 12) {
					if (!(campi[i].equals(""))) {
						// telefono2 = true;
						if (primo) {
							primo = false;
							query = "UPDATE users SET telefono2=?";
						} else {
							query = query + ",telefono2=?";
						}
					}
				}
			}
			query = query + " WHERE nickname=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			for (int i = 0; i <= 13; i++) {
				if (i == 0) {
					prstmt.setString(i, campi[i]);
					i++;
				} else if (i == 3) {
					prstmt.setString(i, dataComposta);
					i = i + 2;
				} else {
					prstmt.setString(i, campi[i]);
				}
			}/*
			 * if(password=true) { prstmt.setString(1, campi[0]); }
			 * if(email=true) { prstmt.setString(1, campi[2]); } if(data=true) {
			 * prstmt.setString(1, dataComposta); } if(nome=true) {
			 * prstmt.setString(1, campi[6]); } if(cognome=true) {
			 * prstmt.setString(1, campi[7]); } if(indirizzoUtente=true) {
			 * prstmt.setString(1, campi[8]); } if(cittaUtente=true) {
			 * prstmt.setString(1, campi[9]); } if(capUtente=true) {
			 * prstmt.setString(1, campi[10]); } if(telefono=true) {
			 * prstmt.setString(1, campi[11]); } if(telefono2=true) {
			 * prstmt.setString(1, campi[12]); } prstmt.setString(1, campi[13]);
			 */
			prstmt.executeUpdate();
			prstmt.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return 5;
		}
		return 0;
	}

	public int editCampiLibreria(String[] campi) {
		if (!connectDatabase()) {
			return -1;
		}
		// boolean
		// password,email,nome,indirizzoLibreria,cittaLibreria,capLibreria,telefonoLibreria,telefono2Libreria;
		// password=email=nome=indirizzoLibreria=cittaLibreria=capLibreria=telefonoLibreria=telefono2Libreria=false;
		boolean primo = true;
		String query = "";
		try {
			for (int i = 0; i < 9; i++) {
				if (i == 0) {
					if (checkPasswordUsers(campi[9], campi[i])) {
						if (!(campi[i + 1].equals(""))
								&& checkPassword(campi[i + 1])) {
							primo = false;
							// password=true;
							query = "UPDATE vendors SET password=?";
						} else {
							return 1; // Errore nella nuova password
						}
					} else {
						return 2; // Errore nella vecchia password
					}
					i++; // Così salta i==1, dove c'è la nuova password e non
							// viene usato nel ciclo for
				} else if (i == 2) {
					if (!(campi[i].contains("@"))) {
						if (checkEmailUsers(campi[i])) {
							// email=true;
							if (primo) {
								query = "UPDATE vendors SET email=?";
								primo = false;
							} else {
								query = query + ",email=?";
							}
						} else {
							return 3; // email inserita è già usata
						}
					} else {
						return 4; // Email non valida
					}
				} else if (i == 3) {
					if (!(campi[i].equals(""))) {
						// nome = true;
						if (primo) {
							primo = false;
							query = "UPDATE vendors SET nome=?";
						} else {
							query = query + ",nome=?";
						}
					}
				} else if (i == 4) {
					if (!(campi[i].equals(""))) {
						// indirizzoLibreria = true;
						if (primo) {
							primo = false;
							query = "UPDATE vendors SET indirizzo=?";
						} else {
							query = query + ",indirizzo=?";
						}
					}
				} else if (i == 5) {
					if (!(campi[i].equals(""))) {
						// cittaLibreria = true;
						if (primo) {
							primo = false;
							query = "UPDATE vendors SET citta=?";
						} else {
							query = query + ",citta=?";
						}
					}
				} else if (i == 6) {
					if (!(campi[i].equals(""))) {
						// capLibreria = true;
						if (primo) {
							primo = false;
							query = "UPDATE vendors SET cap=?";
						} else {
							query = query + ",cap=?";
						}
					}
				} else if (i == 7) {
					if (!(campi[i].equals(""))) {
						// telefonoLibreria = true;
						if (primo) {
							primo = false;
							query = "UPDATE vendors SET telefono=?";
						} else {
							query = query + ",telefono=?";
						}
					}
				} else if (i == 8) {
					if (!(campi[i].equals(""))) {
						// telefono2Libreria = true;
						if (primo) {
							primo = false;
							query = "UPDATE vendors SET telefono2=?";
						} else {
							query = query + ",telefono2=?";
						}
					}
				}
			}
			query = query + " WHERE partita_iva=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			for (int i = 0; i <= 9; i++) {
				if (i == 0) {
					prstmt.setString(i, campi[i]);
					i++;
				} else {
					prstmt.setString(i, campi[i]);
				}
			}/*
			 * /*if(password=true) { prstmt.setString(1, campi[0]); }
			 * if(email=true) { prstmt.setString(1, campi[2]); } if(nome=true) {
			 * prstmt.setString(1, campi[3]); } if(indirizzoLibreria=true) {
			 * prstmt.setString(1, campi[4]); } if(cittaLibreria=true) {
			 * prstmt.setString(1, campi[5]); } if(capLibreria=true) {
			 * prstmt.setString(1, campi[6]); } if(telefonoLibreria=true) {
			 * prstmt.setString(1, campi[7]); } if(telefono2Libreria=true) {
			 * prstmt.setString(1, campi[8]); } prstmt.setString(1, campi[9]);
			 */
			prstmt.executeUpdate();
			prstmt.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return 5;
		}
		return 0;
	}

	/*
	 * public boolean editCampiUser(String nickuser, String passwd,String
	 * nomeCampo, String campo) { if(!connectDatabase()) { return false; } try {
	 * switch (nomeCampo) { case "password":
	 * System.out.println("Si modifica la password.\n"); if
	 * (checkPasswordUsers(nickuser, passwd)) {
	 * System.out.println("Puoi modificare la password.\n"); String query =
	 * "UPDATE users SET password=? WHERE nickname=?;"; PreparedStatement prstmt
	 * = con.prepareStatement(query); prstmt.setString(1, campo);
	 * prstmt.setString(2, nickuser); prstmt.executeUpdate(); prstmt.close(); }
	 * else { System.out.println("Password cappellata.\n"); return false; }
	 * break; case "email": if (checkEmailUsers(campo)) {
	 * System.out.println("Si modifica l'email.\n"); if
	 * (checkPasswordUsers(nickuser, passwd)) {
	 * System.out.println("Puoi modificare l'email.\n"); String query =
	 * "UPDATE users SET email=? WHERE nickname=?;"; PreparedStatement prstmt =
	 * con.prepareStatement(query); prstmt.setString(1, campo);
	 * prstmt.setString(2, nickuser); prstmt.executeUpdate(); prstmt.close(); }
	 * else { System.out.println("Password cappellata.\n"); return false; } }
	 * else { System.out.println("Email già utilizzata.\n"); } break; case
	 * "indirizzo": System.out.println("Si modifica l'indirizzo.\n"); if
	 * (checkPasswordUsers(nickuser, passwd)) {
	 * System.out.println("Puoi modificare l'email.\n"); String query =
	 * "UPDATE users SET indirizzo=? WHERE nickname=?;"; PreparedStatement
	 * prstmt = con.prepareStatement(query); prstmt.setString(1, campo);
	 * prstmt.setString(2, nickuser); prstmt.executeUpdate(); prstmt.close(); }
	 * else { System.out.println("Password cappellata.\n"); return false; }
	 * break; case "citta": System.out.println("Si modifica la città.\n"); if
	 * (checkPasswordUsers(nickuser, passwd)) {
	 * System.out.println("Puoi modificare la città.\n"); String query =
	 * "UPDATE users SET citta=? WHERE nickname=?;"; PreparedStatement prstmt =
	 * con.prepareStatement(query); prstmt.setString(1, campo);
	 * prstmt.setString(2, nickuser); prstmt.executeUpdate(); prstmt.close(); }
	 * else { System.out.println("Password cappellata.\n"); return false; }
	 * break; case "cap": System.out.println("Si modifica il cap.\n"); if
	 * (checkPasswordUsers(nickuser, passwd)) {
	 * System.out.println("Puoi modificare il cap.\n"); String query =
	 * "UPDATE users SET cap=? WHERE nickname=?;"; PreparedStatement prstmt =
	 * con.prepareStatement(query); prstmt.setString(1, campo);
	 * prstmt.setString(2, nickuser); prstmt.executeUpdate(); prstmt.close(); }
	 * else { System.out.println("Password cappellata.\n"); return false; }
	 * break; case "telefono": System.out.println("Si modifica il telefono.\n");
	 * if (checkPasswordUsers(nickuser, passwd)) {
	 * System.out.println("Puoi modificare il telefono.\n"); String query =
	 * "UPDATE users SET telefono=? WHERE nickname=?;"; PreparedStatement prstmt
	 * = con.prepareStatement(query); prstmt.setString(1, campo);
	 * prstmt.setString(2, nickuser); prstmt.executeUpdate(); prstmt.close(); }
	 * else { System.out.println("Password cappellata.\n"); return false; }
	 * break; case "telefono2": System.out.println("Si modifica telefono 2.\n");
	 * if (checkPasswordUsers(nickuser, passwd)) {
	 * System.out.println("Puoi modificare telefono 2.\n"); String query =
	 * "UPDATE users SET telefono_2=? WHERE nickname=?;"; PreparedStatement
	 * prstmt = con.prepareStatement(query); prstmt.setString(1, campo);
	 * prstmt.setString(2, nickuser); prstmt.executeUpdate(); prstmt.close(); }
	 * else { System.out.println("Password cappellata.\n"); return false; }
	 * break; case "nome": System.out.println("Si modifica il nome.\n"); if
	 * (checkPasswordUsers(nickuser, passwd)) {
	 * System.out.println("Puoi modificare il nome.\n"); String query =
	 * "UPDATE users SET nome=? WHERE nickname=?;"; PreparedStatement prstmt =
	 * con.prepareStatement(query); prstmt.setString(1, campo);
	 * prstmt.setString(2, nickuser); prstmt.executeUpdate(); prstmt.close(); }
	 * else { System.out.println("Password cappellata.\n"); return false; }
	 * break; case "cognome": System.out.println("Si modifica il cognome.\n");
	 * if (checkPasswordUsers(nickuser, passwd)) {
	 * System.out.println("Puoi modificare il cognome.\n"); String query =
	 * "UPDATE users SET cognome=? WHERE nickname=?;"; PreparedStatement prstmt
	 * = con.prepareStatement(query); prstmt.setString(1, campo);
	 * prstmt.setString(2, nickuser); prstmt.executeUpdate(); prstmt.close(); }
	 * else { System.out.println("Password cappellata.\n"); return false; }
	 * break; default: break; } } catch (SQLException e) {
	 * System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n"
	 * ); e.printStackTrace(); } return true; }
	 */

	boolean checkPassword(String password) {
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
	public boolean addLibro(String titolo, String autore, String casaEditrice,
			String ISBN, String prezzo, String lingua, String nomeLibreria,
			String nCopie, String sconto, String dataPubblicazione) {
		if (!connectDatabase()) {
			return false;
		}
		try {
			String query = "SELECT ISBN FROM libri_table WHERE ISBN=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, ISBN);
			ResultSet rs = prstmt.executeQuery();
			if (rs == null) {
				System.out
						.println("Il libro non è presente nel catalogo.\nIl libro verrà aggiunto.\n");
				query = "INSERT INTO libri_table (titolo,autore,casa_editrice,ISBN,prezzo,lingua,nome_ibreria,data_pubblicazione) VALUES (?,?,?,?,?,?,?,?);";
				prstmt = con.prepareStatement(query);
				prstmt.setString(1, titolo);
				prstmt.setString(2, autore);
				prstmt.setString(3, casaEditrice);
				prstmt.setString(4, ISBN);
				prstmt.setString(5, prezzo);
				prstmt.setString(6, lingua);
				prstmt.setString(7, nomeLibreria);
				prstmt.executeUpdate();
				System.out.println("Aggiunto.\n");
				/*
				 * Se il libro non esisteva nel catalogo, sicuramente non
				 * esisteva in libro_venditore
				 */
				System.out.println("Inserisco il libro in libro_venditore.\n");
				query = "INSERT INTO libro_venditore(ISBN,nome,sconto,copie) VALUES (?,?,?,?);";
				prstmt = con.prepareStatement(query);
				prstmt.setString(1, ISBN);
				prstmt.setString(2, nomeLibreria);
				prstmt.setString(3, sconto);
				prstmt.setString(4, nCopie);
				prstmt.executeUpdate();
				System.out.println("Inserito.\n");
			}
			/*
			 * Se non era null, significa che il libro esisteva già nel catalogo
			 * e quindi devo inserire solo in libro_venditore
			 */
			query = "SELECT ISBN,nome FROM libro_venditore WHERE ISBN=? AND nome=?;";
			prstmt = con.prepareStatement(query);
			prstmt.setString(1, ISBN);
			prstmt.setString(2, nomeLibreria);
			rs = prstmt.executeQuery();
			if (rs == null) { // Non era presente quel libro venduto da quella
								// libreria in libro_venditore
				query = "INSERT INTO libro_venditore(ISBN,nome,sconto,copie) VALUES(?,?,?,?);";
				prstmt = con.prepareStatement(query);
				prstmt.setString(1, ISBN);
				prstmt.setString(2, nomeLibreria);
				prstmt.setString(3, sconto);
				prstmt.setString(4, nCopie);
				prstmt.executeUpdate();
			} else { // E' presente quel libro di quella libreria in
						// libro_venditore
				query = "UPDATE libro_venditore SET copie=copie+? WHERE ISBN=? AND nome=?;";
				prstmt = con.prepareStatement(query);
				prstmt.setString(1, nCopie);
				prstmt.setString(2, ISBN);
				prstmt.setString(3, nomeLibreria);
				prstmt.executeUpdate();
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

	/* La funzione rimuove un libro dalla tabella libro_venditore */
	public int rimuoviLibro(String email, String isbn) {
		if (!connectDatabase()) {
			return 2;
		}
		try {
			String query = "SELECT ISBN FROM libro_venditore WHERE ISBN=? AND email=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, isbn);
			prstmt.setString(2, email);
			ResultSet rs = prstmt.executeQuery();
			if (rs == null) {
				System.out.println("Il libro cercato non è presente.\n");
				return 3;
			} else {
				query = "DELETE t1 FROM libro_venditore WHERE t1.ISBN=? AND t1.email=?;";
				prstmt = con.prepareStatement(query);
				prstmt.setString(1, isbn);
				prstmt.setString(2, email);
				prstmt.executeUpdate();
				System.out.println("Il libro con ISBN = " + isbn
						+ " è stato eliminato dalla lista.\n");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return 4;
		}
		return 1;
	}

	/*
	 * Potrebbe essere la funzione che viene richiamata se si vuole cambiare
	 * qualcosa nei libro aggiunto al catalogo. Metti che ha sbagliato qualcosa,
	 * lo corregge con questa funzione. Se invece dovesse cancellare il libro
	 * aggiunto, allora facciamo un'altra funzione che fa cancellare e non
	 * aggiornare. Purtroppo ha lo stesso problema della funzione per modificare
	 * il profilo dell'utente.
	 */

	public int modificaCatalogo(String[] campi) {
		if (!connectDatabase()) {
			return 2;
		}
		String[] risultato = new String[campi.length];
		boolean primo = true;
		String query = "";
		try {
			for (int i = 0; i <= campi.length; i++) {
				if (i == 0) {
					if (!(campi[i].equals(""))) {
						// nome = true;
						primo = false;
						query = "UPDATE libri_table SET ISBN=?";
					}
				} else if (i == 1) {
					if (!(campi[i].equals(""))) {
						// indirizzoLibreria = true;
						if (primo) {
							primo = false;
							query = "UPDATE libri_table SET titolo=?";
						} else {
							query = query + ",titolo=?";
						}
					}
				} else if (i == 2) {
					if (!(campi[i].equals(""))) {
						// cittaLibreria = true;
						if (primo) {
							primo = false;
							query = "UPDATE libri_table SET autore=?";
						} else {
							query = query + ",autore=?";
						}
					}
				} else if (i == 3) {
					if (!(campi[i].equals(""))) {
						// capLibreria = true;
						if (primo) {
							primo = false;
							query = "UPDATE libri_table SET anno=?";
						} else {
							query = query + ",cap=?";
						}
					}
				} else if (i == 4) {
					if (!(campi[i].equals(""))) {
						// telefonoLibreria = true;
						if (primo) {
							primo = false;
							query = "UPDATE libri_table SET prezzo=?";
						} else {
							query = query + ",prezzo=?";
						}
					}
				} else if (i == 5) {
					if (!(campi[i].equals(""))) {
						// telefono2Libreria = true;
						if (primo) {
							primo = false;
							query = "UPDATE libri_table SET lingua=?";
						} else {
							query = query + ",lingua=?";
						}
					}
				}
			}
			query = query + " WHERE ISBN=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			for (int i = 0; i <= campi.length; i++) {
				prstmt.setString(i, campi[i]);
			}
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return 3;
		}
		return 1;
	}

	/*
	 * public boolean modificaCatalogo(String nomeCampo,String campo,String
	 * nomeLibreria,String ISBN) { if(!connectDatabase()) { return false; }
	 * PreparedStatement prstmt = null; String query=""; try {
	 * if(nomeCampo.equals("nome_libreria")) { if (nomeCampo.equals("ISBN")) {
	 * query = "UPDATE libri_table SET ?=? WHERE nome_libreria=? AND ISBN=?;";
	 * prstmt = con.prepareStatement(query); prstmt.setString(1, nomeCampo);
	 * prstmt.setString(2, nomeLibreria); prstmt.setString(3, ISBN);
	 * prstmt.executeUpdate(); } else { query =
	 * "SELECT ISBN FROM libri_table WHERE ISBN=?;"; prstmt =
	 * con.prepareStatement(query); prstmt.setString(1, campo); ResultSet rs =
	 * prstmt.executeQuery(); if (rs == null) { query =
	 * "UPDATE libri_table SET ISBN=? WHERE nome_libreria=? AND ISBN=?;"; prstmt
	 * = con.prepareStatement(query); prstmt.setString(1, nomeCampo);
	 * prstmt.setString(2, nomeLibreria); prstmt.setString(3, ISBN);
	 * prstmt.executeUpdate(); } else { System.out.println(
	 * "L'ISBN che si sta tentando di inserire è già presente all'interno del catalogo.\n"
	 * ); System.out.println("Il libro contenente l'ISBN=" + ISBN +
	 * "verrà cancellato, perché sembrerebbe errato.\n");
	 * if(deleteLibro(nomeLibreria, ISBN)) {
	 * System.out.println("Libro cancellato correttamente.\n"); } else {
	 * System.out
	 * .println("Un errore si è presentato durante la cancellazione del libro" +
	 * " con ISBN = " + ISBN + ".\nContattare l'amministratore.\n"); } return
	 * false; } rs.close(); } } prstmt.close();
	 */
	/*
	 * switch(nomeCampo) { case "titolo": query =
	 * "UPDATE libri_table SET titolo=? WHERE nome_libreria=? AND ISBN=?;";
	 * prstmt = con.prepareStatement(query); prstmt.setString(1, nomeCampo);
	 * prstmt.setString(2, nomeLibreria); prstmt.setString(3, ISBN);
	 * prstmt.executeUpdate(); break; case "autore": query =
	 * "UPDATE libri_table SET autore=? WHERE nome_libreria=? AND ISBN=?;";
	 * prstmt = con.prepareStatement(query); prstmt.setString(1, nomeCampo);
	 * prstmt.setString(2, nomeLibreria); prstmt.setString(3, ISBN);
	 * prstmt.executeUpdate(); break; case "anno": query =
	 * "UPDATE libri_table SET anno=? WHERE nome_libreria=? AND ISBN=?;"; prstmt
	 * = con.prepareStatement(query); prstmt.setString(1, nomeCampo);
	 * prstmt.setString(2, nomeLibreria); prstmt.setString(3, ISBN);
	 * prstmt.executeUpdate(); break; case "casa_editrice": query =
	 * "UPDATE libri_table SET casa_editrice=? WHERE nome_libreria=? AND ISBN=?;"
	 * ; prstmt = con.prepareStatement(query); prstmt.setString(1, nomeCampo);
	 * prstmt.setString(2, nomeLibreria); prstmt.setString(3, ISBN);
	 * prstmt.executeUpdate(); break; case "ISBN": query =
	 * "SELECT ISBN FROM libri_table WHERE ISBN=?;"; prstmt =
	 * con.prepareStatement(query); prstmt.setString(1, campo); ResultSet rs =
	 * prstmt.executeQuery(); if (rs == null) { query =
	 * "UPDATE libri_table SET ISBN=? WHERE nome_libreria=? AND ISBN=?;"; prstmt
	 * = con.prepareStatement(query); prstmt.setString(1, nomeCampo);
	 * prstmt.setString(2, nomeLibreria); prstmt.setString(3, ISBN);
	 * prstmt.executeUpdate(); break; } else { System.out.println(
	 * "L'ISBN che si sta tentando di inserire è già presente all'interno del catalogo.\n"
	 * ); System.out.println("Il libro contenente l'ISBN=" + ISBN +
	 * "verrà cancellato, perché sembrerebbe errato.\n");
	 * if(deleteLibro(nomeLibreria, ISBN)) {
	 * System.out.println("Libro cancellato correttamente.\n"); } else {
	 * System.out
	 * .println("Un errore si è presentato durante la cancellazione del libro" +
	 * " con ISBN = " + ISBN + ".\nContattare l'amministratore.\n"); } return
	 * false; } case "genere": query =
	 * "UPDATE libri_table SET genere=? WHERE nome_libreria=? AND ISBN=?;";
	 * prstmt = con.prepareStatement(query); prstmt.setString(1, nomeCampo);
	 * prstmt.setString(2, nomeLibreria); prstmt.setString(3, ISBN);
	 * prstmt.executeUpdate(); break; case "prezzo": query =
	 * "UPDATE libri_table SET prezzo=? WHERE nome_libreria=? AND ISBN=?;";
	 * prstmt = con.prepareStatement(query); prstmt.setString(1, nomeCampo);
	 * prstmt.setString(2, nomeLibreria); prstmt.setString(3, ISBN);
	 * prstmt.executeUpdate(); break; case "voto": query =
	 * "UPDATE libri_table SET voto=? WHERE nome_libreria=? AND ISBN=?;"; prstmt
	 * = con.prepareStatement(query); prstmt.setString(1, nomeCampo);
	 * prstmt.setString(2, nomeLibreria); prstmt.setString(3, ISBN);
	 * prstmt.executeUpdate(); break; case "lingua": query =
	 * "UPDATE libri_table SET lingua=? WHERE nome_libreria=? AND ISBN=?;";
	 * prstmt = con.prepareStatement(query); prstmt.setString(1, nomeCampo);
	 * prstmt.setString(2, nomeLibreria); prstmt.setString(3, ISBN);
	 * prstmt.executeUpdate(); break; default: break; }
	 *//*
		 * } catch (SQLException e) { System.out.println(
		 * "Errore. Impossibile eseguire l'operazione richiesta.\n");
		 * e.printStackTrace(); return false; } return true; }
		 */

	public String leggiTelefono2Utente(String nickname) {
		String telefono = "";
		if (!connectDatabase()) {
			return telefono;
		}
		try {
			String query = "SELECT telefono FROM users WHERE nickname=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nickname);
			ResultSet rs = prstmt.executeQuery();
			telefono = rs.getString("telefono");
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		return telefono;
	}

	public String leggiTelefonoUtente(String nickname) {
		String telefono = "";
		if (!connectDatabase()) {
			return telefono;
		}
		try {
			String query = "SELECT telefono FROM users WHERE nickname=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nickname);
			ResultSet rs = prstmt.executeQuery();
			telefono = rs.getString("telefono");
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		return telefono;
	}

	public String leggiTelefonoLibreria(String email) {
		String telefono = "";
		if (!connectDatabase()) {
			return telefono;
		}
		try {
			String query = "SELECT telefono FROM vendors WHERE email=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, email);
			ResultSet rs = prstmt.executeQuery();
			telefono = rs.getString("telefono");
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		return telefono;
	}

	public String leggiTelefono2Libreria(String email) {
		String telefono = "";
		if (!connectDatabase()) {
			return telefono;
		}
		try {
			String query = "SELECT telefono2 FROM vendors WHERE email=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, email);
			ResultSet rs = prstmt.executeQuery();
			telefono = rs.getString("telefono2");
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		return telefono;
	}

	public String leggiCapUtente(String nickname) {
		String cap = "";
		if (!connectDatabase()) {
			return cap;
		}
		try {
			String query = "SELECT cap FROM users WHERE nickname=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nickname);
			ResultSet rs = prstmt.executeQuery();
			cap = rs.getString("cap");
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		return cap;
	}

	public String leggiCapLibreria(String email) {
		String cap = "";
		if (!connectDatabase()) {
			return cap;
		}
		try {
			String query = "SELECT cap FROM vendors WHERE email=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, email);
			ResultSet rs = prstmt.executeQuery();
			cap = rs.getString("cap");
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		return cap;
	}

	public String leggiCittaUtente(String nickname) {
		String citta = "";
		if (!connectDatabase()) {
			return citta;
		}
		try {
			String query = "SELECT citta FROM users WHERE nickname=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nickname);
			ResultSet rs = prstmt.executeQuery();
			citta = rs.getString("citta");
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		return citta;
	}

	public String leggiCittaLibreria(String email) {
		String citta = "";
		if (!connectDatabase()) {
			return citta;
		}
		try {
			String query = "SELECT citta FROM vendors WHERE email=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, email);
			ResultSet rs = prstmt.executeQuery();
			citta = rs.getString("citta");
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		return citta;
	}

	public String leggiIndirizzoUtente(String nickname) {
		String indirizzo = "";
		if (!connectDatabase()) {
			return indirizzo;
		}
		try {
			String query = "SELECT indirizzo FROM users WHERE nickname=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nickname);
			ResultSet rs = prstmt.executeQuery();
			indirizzo = rs.getString("indirizzo");
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		return indirizzo;
	}

	public String leggiCognomeUtente(String nickname) {
		String cognome = "";
		if (!connectDatabase()) {
			return cognome;
		}
		try {
			String query = "SELECT cognome FROM users WHERE nickname=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nickname);
			ResultSet rs = prstmt.executeQuery();
			cognome = rs.getString("cognome");
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		return cognome;
	}

	public String leggiNomeUtente(String nickname) {
		String nome = "";
		if (!connectDatabase()) {
			return nome;
		}
		try {
			String query = "SELECT nome FROM users WHERE nickname=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nickname);
			ResultSet rs = prstmt.executeQuery();
			nome = rs.getString("nome");
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		return nome;
	}

	public String[] leggiDataNascitaUtente(String nickname) {
		String[] risultato = new String[2];
		String data = "";
		if (!connectDatabase()) {
			return risultato;
		}
		try {
			String query = "SELECT data_nascita FROM users WHERE nickname=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nickname);
			ResultSet rs = prstmt.executeQuery();
			data = rs.getString("data_nascita");
			risultato[0] = data.substring(0, 4);
			risultato[1] = data.substring(5, 7);
			risultato[2] = data.substring(8, 10);
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		return risultato;
	}

	public String leggiEmailUtente(String nickname) {
		String email = "";
		if (!connectDatabase()) {
			return email;
		}
		try {
			String query = "SELECT email FROM users WHERE nickname=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nickname);
			ResultSet rs = prstmt.executeQuery();
			email = rs.getString("email");
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		return email;
	}

	public String leggiEmailLibreria(String partitaIva) {
		String email = "";
		if (!connectDatabase()) {
			return email;
		}
		try {
			String query = "SELECT email FROM vendors WHERE partita_iva=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, partitaIva);
			ResultSet rs = prstmt.executeQuery();
			email = rs.getString("email");
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		return email;
	}

	public String leggiPartitaIva(String email) {
		String partitaIva = "";
		if (!connectDatabase()) {
			return partitaIva;
		}
		try {
			String query = "SELECT partita_iva FROM users WHERE email=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, email);
			ResultSet rs = prstmt.executeQuery();
			email = rs.getString("partita_iva");
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		return partitaIva;
	}

	public String leggiIndirizzoLibreria(String email) {
		String indirizzo = "";
		if (!connectDatabase()) {
			return indirizzo;
		}
		try {
			String query = "SELECT indirizzo FROM vendors WHERE email=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, email);
			ResultSet rs = prstmt.executeQuery();
			email = rs.getString("indirizzo");
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		return indirizzo;
	}

	public String[] leggiLibro(String isbn) {
		String[] risultato = new String[4];
		if (!connectDatabase()) {
			return risultato;
		}
		try {
			String query = "SELECT titolo,autore,anno,prezzo,lingua  FROM libri_table WHERE ISBN=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, isbn);
			ResultSet rs = prstmt.executeQuery();
			if (rs == null) {
				System.out.println("Il libro cercato non è presente.\n");
				return risultato;
			} else {
				while (rs.next()) {
					risultato[0] = rs.getString("titolo");
					risultato[1] = rs.getString("autore");
					risultato[2] = String.valueOf(rs.getInt("anno"));
					risultato[3] = String.valueOf(rs.getDouble("prezzo"));
					risultato[4] = rs.getString("lingua");
				}
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
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
			if (rs != null) {
				numero = rs.getInt("numero");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		risultato = new String[numero][2];
		try {
			String query = "SELECT nome,sconto,copie FROM libro_venditore WHERE ISBN=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, isbn);
			ResultSet rs = prstmt.executeQuery();
			if (rs == null) {
				System.out.println("Il libro cercato non è presente.\n");
				return risultato;
			} else {
				while (rs.next()) {
					for (int i = 0; i <= numero; i++) {
						for (int j = 0; j <= 2; j++) {
							if (j == 0) {
								risultato[i][j] = rs.getString("nome");
							} else if (j == 1) {
								risultato[i][j] = Integer.toString(rs
										.getInt("sconto"));
							} else if (j == 2) {
								risultato[i][j] = Integer.toString(rs
										.getInt("copie"));
							}
						}
					}
				}
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		return risultato;
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
			if (rs == null) {
				System.out.println("Il libro cercato non è presente.\n");
			} else {
				prezzo = String.valueOf(rs.getDouble("prezzo"));
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return prezzo;
		}
		return prezzo;
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
			if (rs == null) {
				System.out.println("Il libro cercato non è presente.\n");
			} else {
				lingua = rs.getString("lingua");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return lingua;
		}
		return lingua;
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
			if (rs == null) {
				System.out.println("Il libro cercato non è presente.\n");
				return data;
			} else {
				data = rs.getString("data_pubblicazione");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return data;
		}
		return data;
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
			if (rs == null) {
				System.out.println("Il libro cercato non è presente.\n");
				return autore;
			} else {
				autore = rs.getString("autore");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
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
			if (rs == null) {
				System.out.println("Il libro cercato non è presente.\n");
				return titolo;
			} else {
				titolo = rs.getString("titolo");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
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
			if (rs == null) {
				System.out.println("Il libro cercato non è presente.\n");
				return isbn;
			} else {
				isbn = rs.getString("isbn");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
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
			if (rs == null) {
				System.out.println("Il libro cercato non è presente.\n");
				return editore;
			} else {
				editore = rs.getString("casa_editrice");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return editore;
		}
		return editore;
	}

	// Rimuove i libri inseriti dal catalogo
	public int deleteLibro(String email, String ISBN) {
		String nomeLibreria = "";
		if (!connectDatabase()) {
			return 2;
		}
		try {
			String query = "SELECT nome_libreria FROM vendors WHERE email=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, email);
			ResultSet rs = prstmt.executeQuery();
			if (rs == null) {
				System.out.println("Il libro cercato non è presente.\n");
				prstmt.close();
				rs.close();
				return 3;
			} else {
				nomeLibreria = rs.getString("nome_libreria");
			}
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return 4;
		}
		try {
			String query = "SELECT ISBN FROM libri_table WHERE ISBN=? AND nome_libreria=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, ISBN);
			prstmt.setString(2, nomeLibreria);
			ResultSet rs = prstmt.executeQuery();
			if (rs == null) {
				System.out.println("Il libro cercato non è presente.\n");
				prstmt.close();
				rs.close();
				return 3;
			} else {
				query = "DELETE t1 FROM libri_table WHERE t1.ISBN=? AND t1.nome_libreria=?;";
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
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return 4;
		}
		return 1;
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
			if (rs != null) {
				return 2;
			} else {
				String query2 = "INSERT INTO voti_libri(ISBN,username,voto) VALUES (?,?,?);";
				PreparedStatement prstmt2 = con.prepareStatement(query);
				prstmt2.setString(1, ISBN);
				prstmt2.setString(2, nickname);
				prstmt2.setInt(3, voto);
				prstmt2.executeUpdate();
				prstmt2.close();
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return -2;
		}
		try {
			String query = "SELECT * FROM libri_table WHERE ISBN=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, ISBN);
			ResultSet rs = prstmt.executeQuery();
			if (rs != null) {
				query = "UPDATE libri_table SET voto=((voto+?)/2) WHERE ISBN=?";
				prstmt = con.prepareStatement(query);
				prstmt.setString(1, ISBN);
				prstmt.executeUpdate();
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
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
			if (rs == null) {
				return voto;
			}
			voto = rs.getInt("voto");
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		return voto;
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
			if (rs == null) {
				return voto;
			}
			voto = rs.getDouble("voto");
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		return voto;
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
			if (rs == null) {
				return nome;
			}
			nome = rs.getString("emal");
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
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
		try {
			String query = "SELECT count(ISBN) AS numero FROM libri_table;";
			PreparedStatement prstmt = con.prepareStatement(query);
			ResultSet rs = prstmt.executeQuery();
			if (rs != null) {
				numero = rs.getInt("numero");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		if (numero < 4) {
			return risultato;
		}
		risultato = new String[3][2];
		ResultSet rs;
		try {
			String query = "SELECT titolo,autore,voto FROM libri_table ORDER BY voto DESC;";
			PreparedStatement prstmt = con.prepareStatement(query);
			rs = prstmt.executeQuery();
			if (rs == null) {
				risultato[0][0] = "";
				return risultato;
			}
			while (rs.next()) {
				for (int i = 0; i <= numero; i++) {
					for (int j = 0; j <= 2; j++) {
						if (j == 0) {
							risultato[i][j] = rs.getString("titolo");
						} else if (j == 1) {
							risultato[i][j] = rs.getString("autore");
						} else if (j == 2) {
							risultato[i][j] = Double
									.toString(rs.getInt("voto"));
						}
					}
				}
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		return risultato;
	}

	/* Vengono prese gli ultimi quattro libri inseriti */
	public String[][] listaNovita() {
		int numero = 0;
		String risultato[][] = null;
		if (!connectDatabase()) {
			return risultato;
		}
		try {
			String query = "SELECT count(titolo) AS numero FROM libri_table;";
			PreparedStatement prstmt = con.prepareStatement(query);
			ResultSet rs = prstmt.executeQuery();
			if (rs.next() != false) {
				numero = rs.getInt("numero");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. TRaaaaaaaaaaa Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		if (numero == 0) {
			return risultato;
		}
		if(numero>0 && numero <=4) {
			risultato = new String[numero][2];
		}
		if(numero>4) {
			risultato = new String[3][2];
		}
		ResultSet rs;
		try {
			String query = "SELECT titolo,autore,data_inserimento FROM libri_table ORDER BY data_inserimento DESC;";
			PreparedStatement prstmt = con.prepareStatement(query);
			rs = prstmt.executeQuery();
			if (rs == null) {
				risultato[0][0] = "";
				return risultato;
			}
			while (rs.next()) {
				for (int i = 0; i <= numero; i++) {
					for (int j = 0; j <= 2; j++) {
						if (j == 0) {
							risultato[i][j] = rs.getString("titolo");
						} else if (j == 1) {
							risultato[i][j] = rs.getString("autore");
						} else if (j == 2) {
							risultato[i][j] =(rs.getTimestamp("data_inserimento")).toString();
						}
					}
				}
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		return risultato;
	}

	/*
	 * Questa funzione è da controllare. Un po' articolata, specialemente la
	 * query annidata. Controllare anche se ci sono errori di chiusura del
	 * ResultSet rs una volta che si lavora con il ResultSet rs2
	 */
	public String[][] ultimeRecensioni() {
		int numero = 0;
		String risultato[][] = null;
		if (!connectDatabase()) {
			return risultato;
		}
		try {
			String query = "SELECT count(id) AS numero FROM commenti;";
			PreparedStatement prstmt = con.prepareStatement(query);
			ResultSet rs = prstmt.executeQuery();
			if (rs != null) {
				numero = rs.getInt("numero");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		if (numero < 4) {
			return risultato;
		}
		risultato = new String[3][2];
		ResultSet rs, rs2;
		try {
			String query = "SELECT titolo,autore FROM libri_table WHERE ISBN=(SELECT ISBN FROM commenti ORDER BY data DESC);";
			String query2 = "SELECT nickname FROM commenti ORDER BY data DESC;";
			PreparedStatement prstmt = con.prepareStatement(query);
			PreparedStatement prstmt2 = con.prepareStatement(query2);
			rs = prstmt.executeQuery();
			rs2 = prstmt2.executeQuery();
			if (rs == null && rs2 == null) {
				risultato[0][0] = "";
				return risultato;
			}
			while (rs.next() && rs2.next()) {
				for (int i = 0; i <= numero; i++) {
					for (int j = 0; j <= 2; j++) {
						if (j == 0) {
							risultato[i][j] = rs2.getString("nickname");
						} else if (j == 1) {
							risultato[i][j] = rs.getString("titolo");
						} else if (j == 2) {
							risultato[i][j] = rs.getString("autore");
						}
					}
				}
			}
			prstmt.close();
			rs.close();
			prstmt2.close();
			rs2.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		return risultato;
	}

	/*
	 * Controllare se funziona nella query data=CURRENT_TIMESTAMP. In
	 * alternativa provare ad utilizzare questa query: UPDATE commenti SET
	 * corpo_commento=?,data=now() WHERE id=? AND ISBN=? AND nickname=? Altre
	 * idee?
	 */
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
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		return 1;
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
			String query = "DELETE t1 FROM commenti WHERE t1.nickname=? AND t1.ISBN=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nickname);
			prstmt.setString(2, ISBN);
			prstmt.executeUpdate();
			prstmt.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return 3;
		}
		return 1;
	}

	int leggiNumeroRecensioni(String isbn) {
		int numero = 0;
		if (!connectDatabase()) {
			return numero;
		}
		try {
			String query = "SELECT count(id) AS numero FROM commenti WHERE ISBN=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, isbn);
			ResultSet rs = prstmt.executeQuery();
			if (rs != null) {
				numero = rs.getInt("numero");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return numero;
		}
		return numero;
	}

	String[][] leggiRecensioniLibro(String isbn) {
		int numero = 0;
		String risultato[][] = null;
		if (!connectDatabase()) {
			return risultato;
		}
		numero = leggiNumeroRecensioni(isbn);
		risultato = new String[numero][3];
		try {
			String query = "SELECT corpo_commento,data,nickname,id FROM commenti WHERE ISBN=? ORDER BY data DESC;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, isbn);
			ResultSet rs = prstmt.executeQuery();
			if (rs == null) {
				risultato[0][0] = "";
				return risultato;
			}
			while (rs.next()) {
				for (int i = 0; i <= numero; i++) {
					for (int j = 0; j <= 3; j++) {
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
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		return risultato;
	}

	public String[] leggiRecensione(String nickname, String isbn) {
		String[] risultato = new String[1];
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
			if (rs != null) {
				titolo = rs.getString("titolo");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		try {
			String query = "SELECT corpo_commento FROM commenti WHERE ISBN=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, isbn);
			ResultSet rs = prstmt.executeQuery();
			if (rs != null) {
				corpo = rs.getString("corpo_commento");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		risultato[0] = titolo;
		risultato[1] = corpo;
		return risultato;
	}

	public String[][] leggiRecensioniUtente(String nickname) {
		int numero = 0;
		String risultato[][] = null;
		if (!connectDatabase()) {
			return risultato;
		}
		try {
			String query = "SELECT count(id) AS numero FROM commenti WHERE nickname=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nickname);
			ResultSet rs = prstmt.executeQuery();
			if (rs != null) {
				numero = rs.getInt("numero");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		risultato = new String[numero][3];
		ResultSet rs, rs2;
		try {
			String query = "SELECT titolo FROM libri_table WHERE ISBN=(SELECT ISBN FROM commenti "
					+ "WHERE nickname=? ORDER BY data DESC);";
			String query2 = "SELECT corpo_commento,data,ISBN FROM commenti ORDER BY data DESC;";
			PreparedStatement prstmt = con.prepareStatement(query);
			PreparedStatement prstmt2 = con.prepareStatement(query2);
			prstmt.setString(1, nickname);
			rs = prstmt.executeQuery();
			rs2 = prstmt2.executeQuery();
			if (rs == null && rs2 == null) {
				risultato[0][0] = "";
				return risultato;
			}
			while (rs.next() && rs2.next()) {
				for (int i = 0; i <= numero; i++) {
					for (int j = 0; j <= 2; j++) {
						if (j == 0) {
							risultato[i][j] = rs2.getString("corpo_commento");
						} else if (j == 1) {
							risultato[i][j] = rs.getString("titolo");
						} else if (j == 2) {
							risultato[i][j] = rs2.getString("data");
						} else if (j == 3) {
							risultato[i][j] = rs2.getString("ISBN");
						}
					}
				}
			}
			prstmt.close();
			rs.close();
			prstmt2.close();
			rs2.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		return risultato;
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
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * Questa funzione realizza la scrittura delle recensioni in un file xml
	 * 'commenti_xml.xml'. Viene prelevata dalla tabella 'commenti' tutte le
	 * informazioni e poi scritte nei file. Sarà poi php che andrà a leggere
	 * questo file xml così da riportare su ogni libro la recensione realizzata
	 * dalla utente. Metto il voto alla recensione?! *************--------->
	 * CONTROLLARE SE FUNZIONA! <------***********
	 */
	public boolean scriviRecensione() {
		if (!connectDatabase()) {
			return false;
		}
		try {
			String query = "SELECT * FROM commenti ORDER BY data ASC;";
			PreparedStatement prstmt = con.prepareStatement(query);
			ResultSet rs = prstmt.executeQuery();
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder;
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			Element rootElement = document.createElement("rootComment");
			document.appendChild(rootElement);
			while (rs.next()) {
				String corpo = rs.getString("corpo_commento");
				String nickname = rs.getString("nickname");
				String ISBN = rs.getString("ISBN");
				Timestamp data = rs.getTimestamp("data");
				int id = rs.getInt("id");

				Element comment = document.createElement("recensioni");
				rootElement.appendChild(comment);

				/*
				 * Trovare un modo per far venire una struttura id questo tipo:
				 * <rootComment> <recensioni> <ISBN> <------------- ISBN vero
				 * del libro ************* <id> </id> <data> </data> <nickname>
				 * </nickname> <corpo_commento> </corpo_commento> </ISBN>
				 * </Recensioni> </rootComment>
				 * 
				 * Oppure va bene in questo modo?
				 * 
				 * <rootComment> <recensioni> <ISBN> </ISBN> <id> </id> <data>
				 * </data> <nickname> </nickname> <corpo_commento>
				 * </corpo_commento> </Recensioni> </rootComment>
				 */
				Element ISBNCommento = document.createElement("ISBN");
				ISBNCommento.appendChild(document.createTextNode(ISBN));
				rootElement.appendChild(ISBNCommento);

				Element idCommento = document.createElement("id");
				idCommento.appendChild(document.createTextNode(Integer
						.toString(id)));
				rootElement.appendChild(idCommento);

				Element dataCommento = document.createElement("data");
				dataCommento.appendChild(document.createTextNode(data
						.toString()));
				rootElement.appendChild(dataCommento);

				Element nicknameCommento = document.createElement("nickname");
				nicknameCommento.appendChild(document.createTextNode(nickname));
				rootElement.appendChild(nicknameCommento);

				Element corpoCommento = document
						.createElement("corpo_commento");
				corpoCommento.appendChild(document.createTextNode(corpo));
				rootElement.appendChild(corpoCommento);
			}
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File("commenti_xml.xml"));
			transformer.transform(source, result);
		} catch (ParserConfigurationException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return false;
		} catch (TransformerException e2) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e2.printStackTrace();
			return false;
		} catch (SQLException e3) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e3.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * Funzioni per la ricerca nel database dei libri. Abbiamo 3 funzioni: -
	 * ricerca: cerca in tutti i campi della tabella libri_tabella; -
	 * ricercaTitolo: cerca solo nel campo titolo della tabella libri_tabella; -
	 * ricercaAutore: cerca solo nel campo autore della tabella libri_tabella;
	 * Capire come sfruttare il campo anno e casa_editrice della tabella
	 * libri_tabella (forse con altre due nuove ricerche?). Sarà php a decidere
	 * quale tipo di ricerca fare, cioè quale metodo chiamare. Aggiungere
	 * ricerca per ISBN.
	 */

	/*
	 * Credo che dovremo eliminare questo ArrayList<Libro> e fare tutto con
	 * matrici di stringhe perché non capisco come lavorare su più righe di
	 * ArrayList. Cioè, l'oggetto Libro ha più righe o soltanto una?
	 */
	/*
	 * public String[][] ricerca(String campo) { int numero=0; String
	 * risultato[][]=null; if(!connectDatabase()) { return risultato; } try {
	 * String query = "SELECT count(id) AS numero FROM libri_table;";
	 * PreparedStatement prstmt = con.prepareStatement(query); ResultSet rs =
	 * prstmt.executeQuery(); if(rs != null) { numero=rs.getInt("numero"); }
	 * prstmt.close(); rs.close(); } catch (SQLException e) {
	 * System.out.println(
	 * "Errore. Impossibile eseguire l'operazione richiesta.\n");
	 * e.printStackTrace(); return risultato; } try { String query =
	 * "SELECT * FROM libri_table WHERE titolo=? OR autore=? OR anno=? " +
	 * "OR casa_editrice=? OR ISBN=? OR genere=? OR voto=? OR lingua=?;";
	 * PreparedStatement prstmt = con.prepareStatement(query);
	 * prstmt.setString(1, campo); prstmt.setString(2, campo);
	 * prstmt.setString(3, campo); prstmt.setString(4, campo);
	 * prstmt.setString(5, campo); ResultSet rs = prstmt.executeQuery(); if (rs
	 * != null) { while (rs.next()) { for(int i=0;i<numero;i++) {
	 * risultato[i][0]=rs.getString("id");
	 * risultato[i][0]=rs.getString("titolo");
	 * risultato[i][0]=rs.getString("autore");
	 * risultato[i][0]=rs.getString("anno");
	 * risultato[i][0]=rs.getString("casa_editrice");
	 * risultato[i][0]=rs.getString("ISBN");
	 * risultato[i][0]=rs.getString("prezzo");
	 * risultato[i][0]=rs.getString("lingua");
	 * risultato[i][0]=rs.getString("voto"); } } } else {
	 * System.out.println("Nessun risultato trovato.\n"); return risultato; } }
	 * catch (SQLException e) {
	 * System.out.println("Errore. Impossibile eseguire l'operazione richiesta.\n"
	 * ); e.printStackTrace(); return risultato; } return risultato; }
	 */

	int contaLibri(String query) {
		int numero = 0;
		if (!connectDatabase()) {
			return numero;
		}
		query = query.substring(6, -1); // Cioè prende la stringa a comincaire
										// da " FROM..." fino alla fine.
		query = "SELECT count(ISBN) as numero" + query;
		try {
			PreparedStatement prstmt = con.prepareStatement(query);
			ResultSet rs = prstmt.executeQuery();
			if (rs == null) {
				return numero;
			} else {
				numero = rs.getInt("numero");
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return numero;
		}
		return numero;
	}

	public String[][] ricercaAvanzata(String[] campi) {
		int numero = 0;
		String query = "";
		String[][] risultato = null;
		boolean titolo, autore, isbn, editore, anno, voto, lingua, prezzo;
		titolo = autore = isbn = editore = anno = voto = lingua = prezzo = false;
		boolean primo = true;
		if (!connectDatabase()) {
			return risultato;
		}
		try {
			for (int i = 0; i < 8; i++) {
				if (i == 0 && !((campi[i]).equals(""))) {
					titolo = true;
					primo = false;
					query = "SELECT * FROM libri_table WHERE titolo=?";
				} else if (i == 1 && !((campi[i]).equals(""))) {
					autore = true;
					primo = false;
					if (primo) {
						query = "SELECT * FROM libri_table WHERE autore=?";
					} else {
						query = query + " AND autore=?";
					}
				} else if (i == 2 && !((campi[i]).equals(""))) {
					isbn = true;
					primo = false;
					if (primo) {
						query = "SELECT * FROM libri_table WHERE isbn=?";
					} else {
						query = query + " AND isbn=?";
					}
				} else if (i == 3 && !((campi[i]).equals(""))) {
					editore = true;
					primo = false;
					if (primo) {
						query = "SELECT * FROM libri_table WHERE casa_editrice=?";
					} else {
						query = query + " AND casa_editrice=?";
					}
				} else if (i == 4 && !((campi[i]).equals(""))) {
					anno = true;
					primo = false;
					if (primo) {
						query = "SELECT * FROM libri_table WHERE anno=?";
					} else {
						query = query + " AND anno=?";
					}
				} else if (i == 5 && !((campi[i]).equals(""))) {
					voto = true;
					primo = false;
					if (primo) {
						query = "SELECT * FROM libri_table WHERE voto=?";
					} else {
						query = query + " AND voto=?";
					}
				} else if (i == 6 && !((campi[i]).equals(""))) {
					lingua = true;
					primo = false;
					if (primo) {
						query = "SELECT * FROM libri_table WHERE lingua=?";
					} else {
						query = query + " AND lingua=?";
					}
				} else if (i == 7 && !((campi[i]).equals(""))) {
					prezzo = true;
					primo = false;
					if (primo) {
						query = "SELECT * FROM libri_table WHERE prezzo=?";
					} else {
						query = query + " AND prezzo=?";
					}
				}
			}
			query = query + ";";
			if ((numero = contaLibri(query)) == 0) {
				return risultato;
			}
			risultato = new String[numero][7];
			PreparedStatement prstmt = con.prepareStatement(query);
			if (titolo = true) {
				prstmt.setString(1, campi[0]);
			}
			if (autore = true) {
				prstmt.setString(1, campi[1]);
			}
			if (isbn = true) {
				prstmt.setString(1, campi[2]);
			}
			if (editore = true) {
				prstmt.setString(1, campi[3]);
			}
			if (anno = true) {
				prstmt.setString(1, campi[4]);
			}
			if (voto = true) {
				prstmt.setString(1, campi[5]);
			}
			if (lingua = true) {
				prstmt.setString(1, campi[6]);
			}
			if (prezzo = true) {
				prstmt.setString(1, campi[7]);
			}
			ResultSet rs = prstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					for (int i = 0; i <= numero; i++) {
						risultato[i][0] = rs.getString("titolo");
						risultato[i][1] = rs.getString("autore");
						risultato[i][2] = rs.getString("anno");
						risultato[i][3] = rs.getString("casa_editrice");
						risultato[i][4] = rs.getString("ISBN");
						risultato[i][5] = rs.getString("prezzo");
						risultato[i][6] = rs.getString("lingua");
						risultato[i][7] = rs.getString("voto");
					}
				}
			} else {
				System.out.println("Nessun risultato trovato.\n");
				return risultato;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		return risultato;
	}

	public String[][] ricerca(String campo) {
		int numero = 0;
		String[][] risultato = null;
		if (!connectDatabase()) {
			return risultato;
		}
		try {
			String query = "SELECT * FROM libri_table WHERE titolo=? OR autore=? OR anno=? "
					+ "OR casa_editrice=? OR ISBN=? OR genere=? OR voto=? OR lingua=?;";
			if ((numero = contaLibri(query)) == 0) {
				return risultato;
			}
			risultato = new String[numero][7];
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, campo);
			prstmt.setString(2, campo);
			prstmt.setString(3, campo);
			prstmt.setString(4, campo);
			prstmt.setString(5, campo);
			ResultSet rs = prstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					for (int i = 0; i <= numero; i++) {
						risultato[i][0] = rs.getString("titolo");
						risultato[i][1] = rs.getString("autore");
						risultato[i][2] = rs.getString("anno");
						risultato[i][3] = rs.getString("casa_editrice");
						risultato[i][4] = rs.getString("ISBN");
						risultato[i][5] = rs.getString("prezzo");
						risultato[i][6] = rs.getString("lingua");
						risultato[i][7] = rs.getString("voto");
					}
				}
			} else {
				System.out.println("Nessun risultato trovato.\n");
				return risultato;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
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
			if ((numero = contaLibri(query)) == 0) {
				return risultato;
			}
			risultato = new String[numero][7];
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, campo);
			ResultSet rs = prstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					for (int i = 0; i <= numero; i++) {
						risultato[i][0] = rs.getString("titolo");
						risultato[i][1] = rs.getString("autore");
						risultato[i][2] = rs.getString("anno");
						risultato[i][3] = rs.getString("casa_editrice");
						risultato[i][4] = rs.getString("ISBN");
						risultato[i][5] = rs.getString("prezzo");
						risultato[i][6] = rs.getString("lingua");
						risultato[i][7] = rs.getString("voto");
					}
				}
			} else {
				System.out.println("Nessun risultato trovato.\n");
				return risultato;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
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
			if ((numero = contaLibri(query)) == 0) {
				return risultato;
			}
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, campo);
			ResultSet rs = prstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					for (int i = 0; i <= numero; i++) {
						risultato[i][0] = rs.getString("titolo");
						risultato[i][1] = rs.getString("autore");
						risultato[i][2] = rs.getString("anno");
						risultato[i][3] = rs.getString("casa_editrice");
						risultato[i][4] = rs.getString("ISBN");
						risultato[i][5] = rs.getString("prezzo");
						risultato[i][6] = rs.getString("lingua");
						risultato[i][7] = rs.getString("voto");
					}
				}
			} else {
				System.out.println("Nessun risultato trovato.\n");
				return risultato;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
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
			if ((numero = contaLibri(query)) == 0) {
				return risultato;
			}
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, campo);
			ResultSet rs = prstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					for (int i = 0; i <= numero; i++) {
						risultato[i][0] = rs.getString("titolo");
						risultato[i][1] = rs.getString("autore");
						risultato[i][2] = rs.getString("anno");
						risultato[i][3] = rs.getString("casa_editrice");
						risultato[i][4] = rs.getString("ISBN");
						risultato[i][5] = rs.getString("prezzo");
						risultato[i][6] = rs.getString("lingua");
						risultato[i][7] = rs.getString("voto");
					}
				}
			} else {
				System.out.println("Nessun risultato trovato.\n");
				return risultato;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		return risultato;
	}

	/* Conviene convertire l'anno in int? */
	public String[][] ricercaAnno(String campo) {
		int numero = 0;
		String[][] risultato = null;
		if (!connectDatabase()) {
			return risultato;
		}
		try {
			String query = "SELECT * FROM libri_table WHERE anno=?;";
			if ((numero = contaLibri(query)) == 0) {
				return risultato;
			}
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, campo);
			ResultSet rs = prstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					for (int i = 0; i <= numero; i++) {
						risultato[i][0] = rs.getString("titolo");
						risultato[i][1] = rs.getString("autore");
						risultato[i][2] = rs.getString("anno");
						risultato[i][3] = rs.getString("casa_editrice");
						risultato[i][4] = rs.getString("ISBN");
						risultato[i][5] = rs.getString("prezzo");
						risultato[i][6] = rs.getString("lingua");
						risultato[i][7] = rs.getString("voto");
					}
				}
			} else {
				System.out.println("Nessun risultato trovato.\n");
				return risultato;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
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
			if ((numero = contaLibri(query)) == 0) {
				return risultato;
			}
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, campo);
			ResultSet rs = prstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					for (int i = 0; i <= numero; i++) {
						risultato[i][0] = rs.getString("titolo");
						risultato[i][1] = rs.getString("autore");
						risultato[i][2] = rs.getString("anno");
						risultato[i][3] = rs.getString("casa_editrice");
						risultato[i][4] = rs.getString("ISBN");
						risultato[i][5] = rs.getString("prezzo");
						risultato[i][6] = rs.getString("lingua");
						risultato[i][7] = rs.getString("voto");
					}
				}
			} else {
				System.out.println("Nessun risultato trovato.\n");
				return risultato;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
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
			if ((numero = contaLibri(query)) == 0) {
				return risultato;
			}
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, campo);
			ResultSet rs = prstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					for (int i = 0; i <= numero; i++) {
						risultato[i][0] = rs.getString("titolo");
						risultato[i][1] = rs.getString("autore");
						risultato[i][2] = rs.getString("anno");
						risultato[i][3] = rs.getString("casa_editrice");
						risultato[i][4] = rs.getString("ISBN");
						risultato[i][5] = rs.getString("prezzo");
						risultato[i][6] = rs.getString("lingua");
						risultato[i][7] = rs.getString("voto");
					}
				}
			} else {
				System.out.println("Nessun risultato trovato.\n");
				return risultato;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		return risultato;
	}

	public String[][] ricercaLibriLibreria2(String nomeLibreria) {
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
			if (rs == null) {
				return risultato;
			} else {
				numero = rs.getInt("numero");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		risultato = new String[numero][7];
		try {
			String query = "SELECT ISBN,sconto,copie FROM libro_venditore WHERE nome=? ORDER BY ISBN;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nomeLibreria);
			ResultSet rs = prstmt.executeQuery();
			if (rs != null) {
				for (int i = 0; i <= numero; i++) {
					risultato[i][1] = rs.getString("ISBN");
					risultato[i][5] = rs.getString("sconto");
					risultato[i][6] = rs.getString("copie");
				}
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		try {
			String query = "SELECT titolo,autore,casa_editrice,prezzo FROM libri_table WHERE ISBN = IN("
					+ "SELECT ISBN FROM libro_venditore WHERE nome=? ORDER BY ISBN);";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nomeLibreria);
			ResultSet rs = prstmt.executeQuery();
			if (rs != null) {
				for (int i = 0; i <= numero; i++) {
					risultato[i][0] = rs.getString("titolo");
					risultato[i][2] = rs.getString("autore");
					risultato[i][3] = rs.getString("casa_editrice");
					risultato[i][4] = rs.getString("prezzo");
				}
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
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
			String query = "SELECT count(isbn) as numero FROM libro_venditore WHERE nome=?;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nomeLibreria);
			ResultSet rs = prstmt.executeQuery();
			if (rs == null) {
				return risultato;
			} else {
				numero = rs.getInt("numero");
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		risultato = new String[numero][7];
		try {
			String query = "SELECT ISBN,sconto,copie FROM libro_venditore WHERE nome=? ORDER BY ISBN;";
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nomeLibreria);
			ResultSet rs = prstmt.executeQuery();
			if (rs != null) {
				for (int i = 0; i <= numero; i++) {
					risultato[i][1] = rs.getString("ISBN");
					risultato[i][5] = rs.getString("sconto");
					risultato[i][6] = rs.getString("copie");
				}
			}
			prstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		try {
			String query = "SELECT titolo,autore,casa_editrice,prezzo FROM libri_table WHERE ISBN=? ORDER BY ISBN;";
			PreparedStatement prstmt = con.prepareStatement(query);
			for (int i = 0; i <= numero; i++) {
				for (int j = 0; j < 7; j++) {
					prstmt.setString(1, risultato[i][1]);
					ResultSet rs = prstmt.executeQuery();
					if (rs != null) {
						risultato[i][0] = rs.getString("titolo");
						risultato[i][2] = rs.getString("autore");
						risultato[i][3] = rs.getString("casa_editrice");
						risultato[i][4] = rs.getString("prezzo");
					}
					prstmt.close();
					rs.close();
				}
			}
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
		}
		return risultato;
	}

	public String[][] ricercaLibreria(String nomeLibreria) {
		int numero = 0;
		String[][] risultato = null;
		if (!connectDatabase()) {
			return risultato;
		}
		try {
			String query = "SELECT * FROM libri_table WHERE nome=?;";
			if ((numero = contaLibri(query)) == 0) {
				return risultato;
			}
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, nomeLibreria);
			ResultSet rs = prstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					for (int i = 0; i <= numero; i++) {
						risultato[i][0] = rs.getString("titolo");
						risultato[i][1] = rs.getString("autore");
						risultato[i][2] = rs.getString("anno");
						risultato[i][3] = rs.getString("casa_editrice");
						risultato[i][4] = rs.getString("ISBN");
						risultato[i][5] = rs.getString("prezzo");
						risultato[i][6] = rs.getString("lingua");
						risultato[i][7] = rs.getString("voto");
					}
				}
			} else {
				System.out.println("Nessun risultato trovato.\n");
				return risultato;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		return risultato;
	}

	/* Conviene convertire l'anno in double? */
	public String[][] ricercaVoto(String campo) {
		int numero = 0;
		String[][] risultato = null;
		if (!connectDatabase()) {
			return risultato;
		}
		try {
			String query = "SELECT * FROM libri_table WHERE voto=?;";
			if ((numero = contaLibri(query)) == 0) {
				return risultato;
			}
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, campo);
			ResultSet rs = prstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					for (int i = 0; i <= numero; i++) {
						risultato[i][0] = rs.getString("titolo");
						risultato[i][1] = rs.getString("autore");
						risultato[i][2] = rs.getString("anno");
						risultato[i][3] = rs.getString("casa_editrice");
						risultato[i][4] = rs.getString("ISBN");
						risultato[i][5] = rs.getString("prezzo");
						risultato[i][6] = rs.getString("lingua");
						risultato[i][7] = rs.getString("voto");
					}
				}
			} else {
				System.out.println("Nessun risultato trovato.\n");
				return risultato;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		return risultato;
	}

	/* Conviene convertire l'anno in double? */
	public String[][] ricercaPrezzo(String campo) {
		int numero = 0;
		String[][] risultato = null;
		if (!connectDatabase()) {
			return risultato;
		}
		try {
			String query = "SELECT * FROM libri_table WHERE prezzo=?;";
			if ((numero = contaLibri(query)) == 0) {
				return risultato;
			}
			PreparedStatement prstmt = con.prepareStatement(query);
			prstmt.setString(1, campo);
			ResultSet rs = prstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					for (int i = 0; i <= numero; i++) {
						risultato[i][0] = rs.getString("titolo");
						risultato[i][1] = rs.getString("autore");
						risultato[i][2] = rs.getString("anno");
						risultato[i][3] = rs.getString("casa_editrice");
						risultato[i][4] = rs.getString("ISBN");
						risultato[i][5] = rs.getString("prezzo");
						risultato[i][6] = rs.getString("lingua");
						risultato[i][7] = rs.getString("voto");
					}
				}
			} else {
				System.out.println("Nessun risultato trovato.\n");
				return risultato;
			}
			rs.close();
			prstmt.close();
		} catch (SQLException e) {
			System.out
					.println("Errore. Impossibile eseguire l'operazione richiesta.\n");
			e.printStackTrace();
			return risultato;
		}
		return risultato;
	}
}
