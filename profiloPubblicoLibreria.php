<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Documento senza titolo</title>
    <link rel="stylesheet" type="text/css" href="/libro.css" />
</head>

<body>
	<?php 
		session_start();
		function getUrl() {
 			return substr($_SERVER["QUERY_STRING"],strrpos($_SERVER["QUERY_STRING"],"=")+1);
		}
      $client= new SoapClient('http://localhost:8080/Libreria/services/Server?wsdl',array('features'=>SOAP_SINGLE_ELEMENT_ARRAYS));
		$nomeLibreria=getUrl();
		$nomeLibreria=str_replace('%', ' ', $nomeLibreria);
		$libriLibreria=$client->ricercaLibriLibreria(array('nomeLibreria'=>$nomeLibreria)); ?>
        <div id="contenitoreGrande" class="page_settings_profile">
        	<?php
				if(isset($_SESSION['username'])) {?>
					<!-- START HEADER -->
					<div id="dashboard" class="headerLoggato">
						<ul>
							<li>
								<a href="/cerca.php" title="cerca">Cerca il libro</a>
							</li>
							<li id="dashboard_options"><?php
								$username=$_SESSION['username'];
								echo '<strong>Ciao,'.$username.'</strong>'; ?>
								|
								<a href="/logout.php" title="Esci"><span>Esci</span></a> 
							</li>
						</ul>
					</div>
					<div id="header2" class="headerLoggato">
						<div id="logo" class="">
							<span><a href="/index.php" title="Logo | Home">Logo</a></span> 
						</div>
						<div id="menus">
						<ul id="main_menu">
								<li id="tab_A">
									<a href="/index.php"><span>Pagina iniziale</span></a>
								</li>
								<li id="tab_B">
									<a href="/recensioniUtente.php"><span>La mia recensioni</span></a>
								</li>
								<li id="tab_C">
									<a href="/profiloUser.php"><span>Profilo</span></a>
								</li>
							</ul>
						</div>
					</div>
					<!-- END HEADER --><?php	
				}
				else if(isset($_SESSION['email'])) { ?>
					<!-- START HEADER -->
					<div id="dashboard" class="headerLoggato">
						<ul>
							<li>
								<a href="/cerca.php" title="cerca">Cerca il libro</a>
							</li>
							<li id="dashboard_options"><?php
								echo '<strong>Ciao,'.$nomeLibreria.'</strong>'; ?>
								|
								<a href="/logout.php" title="Esci"><span>Esci</span></a> 
							</li>
						</ul>
					</div>
					<div id="header2" class="headerLoggato">
						<div id="logo" class="">
							<span><a href="/index.php" title="Logo | Home">Logo</a></span> 
						</div>
						<div id="menus">
						<ul id="main_menu">
								<li id="tab_A">
									<a href="/index.php"><span>Pagina iniziale</span></a>
								</li>
								<li id="tab_B">
									<a href="/libriLibreria.php"><span>I miei libri</span></a>
								</li>
								<li id="tab_C">
									<a href="/profiloLibreria.php"><span>Profilo</span></a>
								</li>
							</ul>
							</div>
					</div>
					<!-- END HEADER --> <?php	
				}
				else { ?>
					<div id="header">
						<div id="header_logo">
							<a href="/index.php">La disoccupazione ci ha dato un bel mestiere;mestiere di merda CARABINIERE.</a>
						</div>
						<div id="login">
							<div id="botton_login">
								<a href="/login.php">Login</a>
								|
								<a href="/iscrizioneUser.php">Iscriviti</a>
								|
								<a href="/iscrizioneLibreria.php">Libreria</a>
							</div>
						</div>
						<div id="scritta">
							<h3 id="slogan">Anarchia.</h3>
						</div>
					</div> <?php	
				}
			?>        
        <!-- START CONTENUTO -->
        <div id="contenitorePiccolo">
            <div id="content">
            	<div id="titolo_content">
            		<h3><?php echo $nomeLibreria; ?></h3>
                    <h6>Qua sono visualizzati tutti i libri della libreria <?php echo $nomeLibreria; ?>.</h6>
            	</div>
                <?php
					if($libriLibreria->return=='') {
						echo '<div id="error">La libreria scelta non ha nessun libro a tua disposizione.</div>';
					}
					else { ?>
            			<table> <?php
							for($i=0;$i< count($libriLibreria->return);$i++) { ?>
								<tr style="border-bottom: 3px solid rgb(200, 200, 200);">
									<td>
										<a id="cover_libro" href="#"><span>Cover</span></a>
									</td>
									<td>
										<ul>
											<li><span>Titolo: <?php echo $libriLibreria->return[$i]->array[0] ?></span></li>
											<li><span>Autore: <?php echo '<a href="/autore/'.$libriLibreria->return[$i]->array[2].'">'.$libriLibreria->return[$i]->array[2].'</a>'; ?></span></li>
											<li><span>Editore: <?php echo '<a href="/ricercaAvanzata.php?search_side_text='.$libriLibreria->return[$i]->array[3].'&search_side_select=Editore&search_side_submit=Cerca">'.$libriLibreria->return[$i]->array[3].'</a>'; ?></span></li>
											<li><span>ISBN: <?php echo '<a href="/books/'.$libriLibreria->return[$i]->array[1].'">'.$libriLibreria->return[$i]->array[1].'</a>'; ?></span></li>									
											<li><span>Prezzo: <?php echo '<a href="/ricercaAvanzata.php?search_side_text='.$libriLibreria->return[$i]->array[4].'&search_side_select=Prezzo&search_side_submit=Cerca">'.$libriLibreria->return[$i]->array[4].'</a>'; ?></span></li>
											<li><span>Lingua: <?php echo '<a href="/ricercaAvanzata.php?search_side_text='.$libriLibreria->return[$i]->array[5].'&search_side_select=Lingua&search_side_submit=Cerca">'.$libriLibreria->return[$i]->array[5].'</a>'; ?></span></li>
											<li><span>Sconto: <?php echo $libriLibreria->return[$i]->array[6] ?>%</span></li>
											<li><span>Copie: <?php echo $libriLibreria->return[$i]->array[7] ?></span></li>
										</ul>
									</td>
								</tr> <?php
							} ?>
               	 		</table> <?php
					}
				?>
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
    </div>
</body>
</html>