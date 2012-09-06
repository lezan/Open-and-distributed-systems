-- phpMyAdmin SQL Dump
-- version 3.5.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generato il: Set 06, 2012 alle 16:50
-- Versione del server: 5.1.63-0ubuntu0.11.10.1
-- Versione PHP: 5.4.0

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `libreria`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `commenti`
--

CREATE TABLE IF NOT EXISTS `commenti` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(30) NOT NULL,
  `ISBN` varchar(30) NOT NULL,
  `corpo_commento` varchar(1000) NOT NULL,
  `data` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nickISBN` (`nickname`,`ISBN`),
  KEY `ISBN` (`ISBN`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Struttura della tabella `libri_table`
--

CREATE TABLE IF NOT EXISTS `libri_table` (
  `titolo` varchar(30) NOT NULL,
  `ISBN` varchar(30) NOT NULL,
  `autore` varchar(30) NOT NULL,
  `data_pubblicazione` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `voto` double NOT NULL DEFAULT '0',
  `casa_editrice` varchar(30) DEFAULT NULL,
  `anno` int(4) DEFAULT NULL,
  `lingua` varchar(30) DEFAULT NULL,
  `prezzo` double NOT NULL DEFAULT '0',
  `nome_libreria` varchar(30) NOT NULL,
  PRIMARY KEY (`ISBN`),
  UNIQUE KEY `titolo` (`titolo`),
  KEY `nome_libreria` (`nome_libreria`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `libro_venditore`
--

CREATE TABLE IF NOT EXISTS `libro_venditore` (
  `nome` varchar(30) NOT NULL,
  `ISBN` varchar(30) NOT NULL,
  `sconto` int(2) NOT NULL,
  `copie` int(4) NOT NULL,
  PRIMARY KEY (`nome`,`ISBN`),
  KEY `ISBN` (`ISBN`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `nickname` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL,
  `email` varchar(30) NOT NULL,
  `location` varchar(50) DEFAULT 'default',
  `data_nascita` date DEFAULT NULL,
  `nome` varchar(30) DEFAULT NULL,
  `cognome` varchar(30) DEFAULT NULL,
  `indirizzo` varchar(30) DEFAULT NULL,
  `citta` varchar(30) DEFAULT NULL,
  `cap` int(8) DEFAULT NULL,
  `telefono` varchar(15) DEFAULT NULL,
  `telefono2` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`nickname`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `indirizzo` (`indirizzo`,`citta`,`cap`),
  UNIQUE KEY `telefono` (`telefono`),
  UNIQUE KEY `telefono2` (`telefono2`),
  UNIQUE KEY `location` (`location`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `vendors`
--

CREATE TABLE IF NOT EXISTS `vendors` (
  `nome` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL,
  `email` varchar(30) NOT NULL,
  `indirizzo` varchar(30) NOT NULL,
  `citta` varchar(30) NOT NULL,
  `cap` varchar(10) NOT NULL,
  `partita_iva` varchar(15) NOT NULL,
  PRIMARY KEY (`nome`),
  UNIQUE KEY `email` (`email`,`partita_iva`),
  UNIQUE KEY `indirizzo` (`indirizzo`,`citta`,`cap`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `voti_libri`
--

CREATE TABLE IF NOT EXISTS `voti_libri` (
  `ISBN` varchar(30) NOT NULL,
  `username` varchar(30) NOT NULL,
  `voto` double NOT NULL,
  PRIMARY KEY (`ISBN`,`username`),
  KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `commenti`
--
ALTER TABLE `commenti`
  ADD CONSTRAINT `commenti_ibfk_2` FOREIGN KEY (`nickname`) REFERENCES `users` (`nickname`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `commenti_ibfk_3` FOREIGN KEY (`ISBN`) REFERENCES `libri_table` (`ISBN`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Limiti per la tabella `libri_table`
--
ALTER TABLE `libri_table`
  ADD CONSTRAINT `libri_table_ibfk_1` FOREIGN KEY (`nome_libreria`) REFERENCES `vendors` (`nome`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Limiti per la tabella `libro_venditore`
--
ALTER TABLE `libro_venditore`
  ADD CONSTRAINT `libro_venditore_ibfk_1` FOREIGN KEY (`ISBN`) REFERENCES `libri_table` (`ISBN`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `libro_venditore_ibfk_2` FOREIGN KEY (`nome`) REFERENCES `vendors` (`nome`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Limiti per la tabella `voti_libri`
--
ALTER TABLE `voti_libri`
  ADD CONSTRAINT `voti_libri_ibfk_1` FOREIGN KEY (`ISBN`) REFERENCES `libri_table` (`ISBN`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `voti_libri_ibfk_2` FOREIGN KEY (`username`) REFERENCES `users` (`nickname`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
