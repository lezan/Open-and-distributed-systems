<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Documento senza titolo</title>
    <link rel="stylesheet" type="text/css" href="profiloUser.css" />
</head>

<body>
	<?php
		/*
		In sostanza la pagina è fatta.
		Il problema maggiore è impostare la data a seconda del valore che c'è impostato. Bisogna spostare l'attributo "selected" 
		in base alla data di nascita, il tutto dinamicamente.
		*/
		session_start();
		if(!isset($_SESSION['username'])) {
			header('Location:login.php');
		}
		else if(isset($_POST['modifica']))
		{
			$client= new SoapClient('http://localhost:8080/Libreria/services/Server?wsdl');
			$parametri=array($_POST['password_old'],$_POST['password_new'],$_POST['email'],$_POST['nome_utente'],$_POST['cognome_utente'],$_POST['indirizzo_utente'],$_POST['citta_utente'],$_POST['cap_utente'],$_POST['telefono_utente'],$_POST['telefono2_utente'],$_SESSION['username'],$_POST['birth_year'],$_POST['birth_month'],$_POST['birth_day']);
			$result=$client->editUser($parametri);
				if($result->return==0) {
					header('Location:profiloUser.php?update=ok');
				}
				else if($result->return==1) {
					echo 'Password nuova non corretta';
					echo '<form name="back" action="profiloUser.php" method="POST">';
					echo '<input type="submit" name="back" value="Torna alla modifica"/>';
					echo '</form>';				
				}
				else if($result->return==2) {
					echo 'Password vecchia errata';
					echo '<form name="back" action="profiloUser.php" method="POST">';
					echo '<input type="submit" name="back" value="Torna alla modifica"/>';
					echo '</form>';
				}
				else if($result->return==3) {
					echo 'Email già utilizzata';
					echo '<form name="back" action="profiloUser.php" method="POST">';
					echo '<input type="submit" name="back" value="Torna alla modifica"/>';
					echo '</form>';	
				}
				else if($result->return==4) {
					echo 'Email non valida';
					echo '<form name="back" action="profiloUser.php" method="POST">';
					echo '<input type="submit" name="back" value="Torna alla modifica"/>';
					echo '</form>';	
				}
		}
		else { ?>
                <div id="contenitoreGrande" class="page_settings_profile">
                    <!-- START HEADER -->
                    <div id="dashboard">
                        <ul>
                            <li>
                                <a href="cerca.php" title="cerca">Cerca il libro</a>
                            </li>
                            <li id="dashboard_options">
                                <strong>Ciao, <?php echo $_SESSION['username'];?></strong>
                                |
                                <a href="logout.php" title="Esci"><span>Esci</span></a> 
                            </li>
                        </ul>
                    </div>
                    <div id="header2">
                        <div id="logo" class="">
                            <span><a href="index.php" title="Logo | Home">Logo</a></span> 
                        </div>
                        <div id="menus">   
                            <ul id="main_menu">
                                <li id="tab_A">
                                    <a href="index.php"><span>Pagina iniziale</span></a>
                                </li>
                                <li id="tab_B">
                                    <a href="recensioniUtente.php"><span>Le mie recensioni</span></a>
                                </li>
                                <li id="tab_C">
                                    <a href="profiloUser.php"><span>Profilo</span></a>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <!-- END HEADER -->
                    <!-- START CONTENUTO -->
			<?php
				if(isset($_GET['update'])){
					echo 'Dati aggiornati correttamente!<br/><br/>';
					unset($_GET['update']);
				}
				$client= new SoapClient('http://localhost:8080/Libreria/services/Server?wsdl');
				$parametri=array("nickuser"=>$_SESSION['username']);
				$dati=$client->leggiDatiUtente($parametri);
			?>
                    <div id="contenitorePiccolo">
                        <div id="content">
                            <div id="page_head">
                                <span>Modifica il tuo profilo</span>
                            </div>
                            <form id="change_profile" class="standard_form" action="profiloUser.php" method="post">
                                <h4>
                                    <label for="buddy_icon">
                                        <span>Il tuo avatar</span>
                                    </label>
                                </h4>
                                <h6 class="instruction">
                                    <span>Scegli un immagine per il tuo avatar</span>
                                </h6>
                                <div id="buddy_icon_wrap" class="input_wrap"> 
                                		<?php
	                                		$location=$client->cercaAvatar(array('username'=>$_SESSION['username']));
	                                		if($location->return=='default') {
	                                    	echo '<img src="icon_sample.gif" />';
	                                    }
	                                    else if($location->return!=null) {
	                                    	echo '<a href="'.$location->return.'"><img width="48px" height="48px" src="'.$location->return.'" /></a>';
	                                    } 
	                                 ?>
                                    <span class="options">
                                        <a href="modificaAvatar.php"><span>Modifica</span></a>
                                    </span>
                                </div>
                                <h4>
                                    <label for="password">
                                        <span>Password</span>
                                    </label>
                                </h4>
                                <div id="password_div" class="input_wrap">
                                    <h6>Inserisci la vecchia password</h6>
                                    <?php
                                        echo '<input type="password_old" id="password" class="text_input" name="password_old" size="30" min="8" maxlength="255" />';
                                    ?>
                                    <h6>Inserisci la nuova password</h6>
                                    <?php
                                        echo '<input type="password_new" id="password" class="text_input" name="password_new" size="30" maxlength="255" />';
                                    ?>
                                </div>
                                <h4>
                                    <label for="email_name">
                                        <span>Email</span>
                                    </label>
                                </h4>
                                <h6 class="instruction">
                                    <span>Cambia la tua email: </span>
                                </h6>
                                <div id="email_name_div" class="input_wrap">
                                    <?php
                                        echo '<input type="text" id="email" class="text_input" value="'.$dati->return['0'].'" name="email" size="30" maxlength="255" />';
                                    ?>
                                </div>
                                <?php
                                    $optionsAnno = array (
                                        1946 => 1946,1947 => 1947,1948 => 1948,1949=> 1949,
                                        1950 => 1950,1951 => 1951,1952 => 1952,1953 => 1953,1954 => 1954,1955 => 1955,1956 => 1956,1957 => 1957,1958 => 1958,1959 => 1959,
                                        1960 => 1960,1961 => 1961,1962 => 1962,1963 => 1963,1964 => 1964,1965 => 1965,1966 => 1966,1967 => 1967,1968 => 1968,1969 => 1969,
                                        1970 => 1970,1971 => 1971,1972 => 1972,1973 => 1973,1974 => 1974,1975 => 1975,1976 => 1976,1977 => 1977,1978 => 1978,1979 => 1979,
                                        1980 => 1980,1981 => 1981,1982 => 1982,1983 => 1983,1984 => 1984,1985 => 1985,1986 => 1986,1987 => 1987,1988 => 1988,1989 => 1989,
                                        1990 => 1990,1991 => 1991,1992 => 1992,1993 => 1993,1994 => 1994,1995 => 1995
                                    );
                                    $optionsMese = array (
                                        'Gen' => '01','Feb' => '02','Mar' => '03','Apr' => '04','Mag' => '05','Giu' => '06','Lug' => '07','Ago' => '08','Set' => '09','Ott' => '10','Nov' => '11','Dic' => '12'
                                    );
                                    $optionsGiorno = array (
                                        01 => '01',02 => '02',03 => '03',04 => '04',05 => '05',06 => '06',07 => '07',08 => '08',09 => '09',10 => '10',11 => '11',12 => '12',13 => '13',14 => '14',15 => '15',16 => '16',17 => '17',18 => '18',19 => '19',20 => '20',21 => '21',22 => '22',23 => '23',24 => '24',25 => '25',26 => '26',27 => '27',28 => '28',29 => '29',30 => '30',31 => '31'
                                    );                              
                                    $option_selected_anno = $dati->return['8'];
                                    $option_selected_mese = $dati->return['9']; 
                                    $option_selected_giorno = $dati->return['10']; 
                                ?>
                                <h4>
                                    <label>
                                        <span>Data di nascita</span>
                                    </label>
                                </h4>
                                <div id="birth" class="input_wrap ">
                                    <select id="birth_year" name="birth_year">
                                        <?php
                                            if($option_selected_anno!='- -') {
                                                foreach($optionsAnno as $index => $value)  {
                                                    print '<option value="'.$index.'"';
                                                    if($index==$option_selected_anno) {
                                                        print ' selected="selected"';
                                                    }
                                                    print '>'.$value.'</option>';
                                                }
                                            }
                                            else { ?>
                                                <option value="" selected="selected">- -</option>
                                                <option value="1995">1995</option>
                                                <option value="1994">1994</option>
                                                <option value="1993">1993</option>
                                                <option value="1992">1992</option>
                                                <option value="1991">1991</option>
                                                <option value="1990">1990</option>
                                                <option value="1989">1989</option>
                                                <option value="1988">1988</option>
                                                <option value="1987">1987</option>
                                                <option value="1986">1986</option>
                                                <option value="1985">1985</option>
                                                <option value="1984">1984</option>
                                                <option value="1983">1983</option>
                                                <option value="1982">1982</option>
                                                <option value="1981">1981</option>
                                                <option value="1980">1980</option>
                                                <option value="1979">1979</option>
                                                <option value="1978">1978</option>
                                                <option value="1977">1977</option>
                                                <option value="1976">1976</option>
                                                <option value="1975">1975</option>
                                                <option value="1974">1974</option>
                                                <option value="1973">1973</option>
                                                <option value="1972">1972</option>
                                                <option value="1971">1971</option>
                                                <option value="1970">1970</option>
                                                <option value="1969">1969</option>
                                                <option value="1968">1968</option>
                                                <option value="1967">1967</option>
                                                <option value="1966">1966</option>
                                                <option value="1965">1965</option>
                                                <option value="1964">1964</option>
                                                <option value="1963">1963</option>
                                                <option value="1962">1962</option>
                                                <option value="1961">1961</option>
                                                <option value="1960">1960</option>
                                                <option value="1959">1959</option>
                                                <option value="1958">1958</option>
                                                <option value="1957">1957</option>
                                                <option value="1956">1956</option>
                                                <option value="1955">1955</option>
                                                <option value="1954">1954</option>
                                                <option value="1953">1953</option>
                                                <option value="1952">1952</option>
                                                <option value="1951">1951</option>
                                                <option value="1950">1950</option>
                                                <option value="1949">1949</option>
                                                <option value="1948">1948</option>
                                                <option value="1947">1947</option>
                                                <option value="1946">1946</option> <?php
                                            }
                                        ?>
                                    </select> 
                                    <select id="birth_month" name="birth_month">
                                        <?php
                                            if($option_selected_mese!='- -') {
                                                foreach($optionsMese as $index => $value)  {
                                                    print '<option value="'.$value.'"';
                                                    if($value==$option_selected_mese) {
                                                        print ' selected="selected"';
                                                    }
                                                    print '>'.$index.'</option>';
                                                }
                                            }
                                            else { ?>
                                                <option value="" selected="selected">- -</option>
                                                <option value="01">Gen</option>
                                                <option value="02">Feb</option>
                                                <option value="03">Mar</option>
                                                <option value="04">Apr</option>
                                                <option value="05">Mag</option>
                                                <option value="06">Giu</option>
                                                <option value="07">Lug</option>
                                                <option value="08">Ago</option>
                                                <option value="09">Set</option>
                                                <option value="10">Ott</option>
                                                <option value="11">Nov</option>
                                                <option value="12">Dic</option> <?php
                                            }
                                        ?>
                                    </select> 
                                    <select id="birth_day" name="birth_day">
                                        <?php
                                            if($option_selected_giorno!='- -') {
                                                foreach($optionsGiorno as $index => $value)  {
                                                    print '<option value="'.$value.'"';
                                                    if($value==$option_selected_giorno) {
                                                        print ' selected="selected"';
                                                    }
                                                    print '>'.$index.'</option>';
                                                }
                                            }
                                            else { ?>
                                                <option value="" selected="selected">- -</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
                                                <option value="10">10</option>
                                                <option value="11">11</option>
                                                <option value="12">12</option>
                                                <option value="13">13</option>
                                                <option value="14">14</option>
                                                <option value="15">15</option>
                                                <option value="16">16</option>
                                                <option value="17">17</option>
                                                <option value="18">18</option>
                                                <option value="19">19</option>
                                                <option value="20">20</option>
                                                <option value="21">21</option>
                                                <option value="22">22</option>
                                                <option value="23">23</option>
                                                <option value="24">24</option>
                                                <option value="25">25</option>
                                                <option value="26">26</option>
                                                <option value="27">27</option>
                                                <option value="28">28</option>
                                                <option value="29">29</option>
                                                <option value="30">30</option>
                                                <option value="31">31</option> <?php
                                            }
                                        ?>
                                    </select>
                                </div>
                                <h4>
                                    <label for="nome_utente">
                                        <?php
                                           // $nomeUtente=$client->leggiNomeUtente($_SESSION['username']);
                                        ?>
                                        <span>Nome: </span>
                                    </label>
                                </h4>
                                <h6 class="instruction">
                                    <span>Inserisci il tuo nome reale</span>
                                </h6>
                                <div id="nome_utente_div" class="input_wrap">
                                    <?php
                                        echo '<input type="text" id="nome_utente" class="text_input" name="nome_utente" value="'.$dati->return['1'].'" size="30" maxlength="255" />';
                                    ?>
                                </div>
                                <h4>
                                    <label for="cognome_utente">
                                        <?php
                                         //   $cognomeUtente=$client->leggiCognomeUtente($_SESSION['username']);
                                        ?>
                                        <span>Cognome</span>
                                    </label>
                                </h4>
                                <h6 class="instruction">
                                    <span>Inserisci il tuo cognome reale</span>
                                </h6>
                                <div id="cognome_utente_div" class="input_wrap">
                                    <?php
                                        echo '<input type="text" id="cognome_utente" class="text_input" name="cognome_utente" value="'.$dati->return['2'].'" size="30" maxlength="255" />';
                                    ?>
                                </div>
                                <h2>Indirizzo</h2>
                                <div id="indirizzo">                    	
                                    <h4>
                                        <label for="indirizzo_utente">
                                            <?php
                                           //     $indirizzo=$client->leggiIndirizzoUtente($_SESSION['username']);
                                            ?>
                                            <span>Via/Pzza: </span>
                                        </label>
                                    </h4>
                                    <h6 class="instruction">
                                        <span>Inserisci il tuo indirizzo</span>
                                    </h6>
                                    <div id="indirizzo_utente_div" class="input_wrap">
                                        <?php
                                            echo '<input type="text" id="indirizzo_utente" class="text_input" name="indirizzo_utente" value="'.$dati->return['3'].'" size="30" maxlength="150" />';
                                        ?>
                                    </div>
                                    <h4>
                                        <label for="citta_utente">
                                            <?php
                                           //     $citta=$client->leggiCittaUtente($_SESSION['username']);
                                            ?>
                                            <span>Citta: </span>
                                        </label>
                                    </h4>
                                    <h6 class="instruction">
                                        <span>Inserisci la tua citta</span>
                                    </h6>
                                    <div id="citta_utente_div" class="input_wrap">
                                        <?php
                                            echo '<input type="text" id="citta_utente" class="text_input" name="citta_utente" value="'.$dati->return['4'].'" size="30" maxlength="100" />';
                                        ?>
                                    </div>
                                    <h4>
                                        <label for="cap_utente">
                                            <?php
                                             //   $cap=$client->leggiCapUtente($_SESSION['username']);
                                            ?>
                                            <span>Cap: </span>
                                        </label>
                                    </h4>
                                    <h6 class="instruction">
                                        <span>Inserisci il codice di avviamento postale</span>
                                    </h6>
                                    <div id="cap_utente_div" class="input_wrap">
                                        <?php
                                            echo '<input type="text" id="cap_utente" class="text_input" name="cap_utente" value="'.$dati->return['5'].'" size="30" maxlength="5" />';
                                        ?>
                                    </div>
                                </div>
                                <h2>Telefoni</h2>
                                <div id="telefono">
                                    <h4>
                                        <label for="telefono_utente">
                                            <?php
                                           //     $telefono=$client->leggiTelefonoUtente($_SESSION['username']);
                                            ?>
                                            <span>Telefono: </span>
                                        </label>
                                    </h4>
                                    <h6 class="instruction">
                                        <span>Inserisci un numero di telefono valido</span>
                                    </h6>
                                    <div id="telefono_utente_div" class="input_wrap">
                                        <?php
                                            echo '<input type="text" id="telefono_utente" class="text_input" name="telefono_utente" value="'.$dati->return['6'].'" size="30" maxlength="10" />';
                                        ?>
                                    </div>
                                    <h4>
                                        <label for="telefono2_utente">
                                            <?php
                                          //      $telefono2=$client->leggiTelefono2Utente($_SESSION['username']);
                                            ?>
                                            <span>Telefono(2): </span>
                                        </label>
                                    </h4>
                                    <h6 class="instruction">
                                        <span>Inserisci un numero di telefono valido</span>
                                    </h6>
                                    <div id="telefono_utente_div" class="input_wrap">
                                        <?php
                                            echo '<input type="text" id="telefono2_utente" class="text_input" name="telefono2_utente" value="'.$dati->return['7'].'" size="30" maxlength="10" />';
                                        ?>
                                    </div>
                                </div>
				<input type="hidden" name="modifica" value="modifica"/>
				<input type="hidden" name="updated" value="updated"/>
				<input type="submit" name="submit" value="Modifica i tuoi dati"/>
                            </form>
                        </div>
                    </div>
                    <!-- END CONTENUTO -->       
                    <!-- START FOOTER -->
                    <div id="footer">
                        <ul id="links_footer">
                            <li class="item_footer">
                                <a href=""> Il nostro progetto</a>
                            </li>
                            <li class="item_footer">
                                <a href=""> Chi siamo?</a>
                            </li>
                            <li class="item_footer">
                                <a href="" class="last"> Contattaci</a>
                            </li>
                        </ul>
                    </div>
                    <!-- END FOOTER -->
                </div><?php
			}
	?>
</body>
</html>
