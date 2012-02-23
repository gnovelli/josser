-- phpMyAdmin SQL Dump
-- version 2.11.8.1deb1ubuntu0.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generato il: 07 Dic, 2009 at 04:35 PM
-- Versione MySQL: 5.0.67
-- Versione PHP: 5.2.6-2ubuntu4.5

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `dmoz`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `dmoz_aliases`
--

CREATE TABLE IF NOT EXISTS `dmoz_aliases` (
  `id` int(11) NOT NULL auto_increment,
  `catid` int(11) NOT NULL default '0',
  `Alias` text NOT NULL,
  `Title` text NOT NULL,
  `Target` text NOT NULL,
  `tcatid` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `catid` (`catid`),
  KEY `tcatid` (`tcatid`),
  KEY `Title` (`Title`(255))
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='List of Aliases for ODP Categories' AUTO_INCREMENT=1 ;

--
-- Dump dei dati per la tabella `dmoz_aliases`
--


-- --------------------------------------------------------

--
-- Struttura della tabella `dmoz_altlangs`
--

CREATE TABLE IF NOT EXISTS `dmoz_altlangs` (
  `id` int(11) NOT NULL auto_increment,
  `language` text NOT NULL,
  `resource` text NOT NULL,
  `catid` int(11) NOT NULL default '0',
  `rcatid` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `language` (`language`(255)),
  KEY `catid` (`catid`),
  KEY `rcatid` (`rcatid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='List of categories in other languages for ODP categories' AUTO_INCREMENT=1 ;

--
-- Dump dei dati per la tabella `dmoz_altlangs`
--


-- --------------------------------------------------------

--
-- Struttura della tabella `dmoz_categories`
--

CREATE TABLE IF NOT EXISTS `dmoz_categories` (
  `id` int(11) NOT NULL auto_increment,
  `Topic` text NOT NULL,
  `catid` int(11) NOT NULL default '0',
  `aolsearch` text NOT NULL,
  `dispname` text NOT NULL,
  `charset` text NOT NULL,
  `Title` text NOT NULL,
  `Description` text NOT NULL,
  `lastUpdate` text NOT NULL,
  `fatherid` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `catid` (`catid`),
  KEY `fatherid` (`fatherid`),
  KEY `Title` (`Title`(255))
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='List of ODP categories' AUTO_INCREMENT=1 ;

--
-- Dump dei dati per la tabella `dmoz_categories`
--


-- --------------------------------------------------------

--
-- Struttura della tabella `dmoz_editors`
--

CREATE TABLE IF NOT EXISTS `dmoz_editors` (
  `id` int(11) NOT NULL auto_increment,
  `editor` text NOT NULL,
  `catid` int(255) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `catid` (`catid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Lisf of editors for ODP categories' AUTO_INCREMENT=1 ;

--
-- Dump dei dati per la tabella `dmoz_editors`
--


-- --------------------------------------------------------

--
-- Struttura della tabella `dmoz_externalpages`
--

CREATE TABLE IF NOT EXISTS `dmoz_externalpages` (
  `id` int(11) NOT NULL auto_increment,
  `ages` text NOT NULL,
  `type` text NOT NULL,
  `link` text NOT NULL,
  `Title` text NOT NULL,
  `Description` text NOT NULL,
  `catid` int(11) NOT NULL default '0',
  `priority` int(11) NOT NULL default '0',
  `mediadate` text NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `Title` (`Title`(255)),
  KEY `catid` (`catid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='List of external pages for ODP categories.' AUTO_INCREMENT=1 ;

--
-- Dump dei dati per la tabella `dmoz_externalpages`
--


-- --------------------------------------------------------

--
-- Struttura della tabella `dmoz_letterbars`
--

CREATE TABLE IF NOT EXISTS `dmoz_letterbars` (
  `id` int(11) NOT NULL auto_increment,
  `letterbar` text NOT NULL,
  `catid` int(11) NOT NULL default '0',
  `lcatid` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `catid` (`catid`),
  KEY `lcatid` (`lcatid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='List of related letterbars for ODP categories' AUTO_INCREMENT=1 ;

--
-- Dump dei dati per la tabella `dmoz_letterbars`
--


-- --------------------------------------------------------

--
-- Struttura della tabella `dmoz_narrows`
--

CREATE TABLE IF NOT EXISTS `dmoz_narrows` (
  `id` int(11) NOT NULL auto_increment,
  `narrow` text NOT NULL,
  `priority` int(11) NOT NULL default '0',
  `catid` int(11) NOT NULL default '0',
  `ncatid` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `priority` (`priority`),
  KEY `catid` (`catid`),
  KEY `ncatid` (`ncatid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='List of related narrows for ODP categories' AUTO_INCREMENT=1 ;

--
-- Dump dei dati per la tabella `dmoz_narrows`
--


-- --------------------------------------------------------

--
-- Struttura della tabella `dmoz_newsgroups`
--

CREATE TABLE IF NOT EXISTS `dmoz_newsgroups` (
  `id` int(11) NOT NULL auto_increment,
  `type` text NOT NULL,
  `newsGroup` text NOT NULL,
  `catid` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `catid` (`catid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='List of related newsgroups for ODP categories' AUTO_INCREMENT=1 ;

--
-- Dump dei dati per la tabella `dmoz_newsgroups`
--


-- --------------------------------------------------------

--
-- Struttura della tabella `dmoz_related`
--

CREATE TABLE IF NOT EXISTS `dmoz_related` (
  `id` int(11) NOT NULL auto_increment,
  `related` text NOT NULL,
  `catid` int(11) NOT NULL default '0',
  `rcatid` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `catid` (`catid`),
  KEY `rcatid` (`rcatid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='List of related categories for ODP categories.' AUTO_INCREMENT=1 ;

--
-- Dump dei dati per la tabella `dmoz_related`
--


-- --------------------------------------------------------

--
-- Struttura della tabella `dmoz_symbolics`
--

CREATE TABLE IF NOT EXISTS `dmoz_symbolics` (
  `id` int(11) NOT NULL auto_increment,
  `resource` text NOT NULL,
  `symbolic` text NOT NULL,
  `priority` int(11) NOT NULL default '0',
  `catid` int(11) NOT NULL default '0',
  `scatid` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `priority` (`priority`),
  KEY `catid` (`catid`),
  KEY `scatid` (`scatid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='List of related symbolics for ODP categories' AUTO_INCREMENT=1 ;

--
-- Dump dei dati per la tabella `dmoz_symbolics`
--

