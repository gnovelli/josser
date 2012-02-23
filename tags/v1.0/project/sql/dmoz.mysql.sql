-- Table structure for table `dmoz_aliases`
DROP TABLE IF EXISTS `dmoz_aliases`;
CREATE TABLE IF NOT EXISTS `dmoz_aliases` (
  `id` int(11) NOT NULL auto_increment,
  `catid` int(11) NOT NULL default '0',
  `Alias` varchar(128) NOT NULL default '',
  `Title` varchar(255) NOT NULL default '',
  `Target` varchar(255) NOT NULL default '',
  `tcatid` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `Title` (`Title`),
  KEY `catid` (`catid`),
  KEY `tcatid` (`tcatid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='List of Aliases for ODP Categories' AUTO_INCREMENT=1 ;

-- Table structure for table `dmoz_altlangs`
DROP TABLE IF EXISTS `dmoz_altlangs`;
CREATE TABLE IF NOT EXISTS `dmoz_altlangs` (
  `id` int(11) NOT NULL auto_increment,
  `language` varchar(255) NOT NULL default '',
  `resource` varchar(255) NOT NULL default '',
  `catid` int(11) NOT NULL default '0',
  `rcatid` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `language` (`language`),
  KEY `catid` (`catid`),
  KEY `rcatid` (`rcatid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='List of categories in other languages for ODP categories' AUTO_INCREMENT=1 ;

-- Table structure for table `dmoz_categories`
DROP TABLE IF EXISTS `dmoz_categories`;
CREATE TABLE IF NOT EXISTS `dmoz_categories` (
  `id` int(11) NOT NULL auto_increment,
  `Topic` varchar(255) NOT NULL default '',
  `catid` int(11) NOT NULL default '0',
  `aolsearch` varchar(255) NOT NULL default '',
  `dispname` varchar(255) NOT NULL default '',
  `charset` varchar(255) NOT NULL default '',
  `Title` varchar(255) NOT NULL default '',
  `Description` text NOT NULL,
  `lastUpdate` varchar(255) NOT NULL default '',
  `fatherid` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `catid` (`catid`),
  KEY `Title` (`Title`),
  KEY `fatherid` (`fatherid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='List of ODP categories' AUTO_INCREMENT=1 ;

-- Table structure for table `dmoz_editors`
DROP TABLE IF EXISTS `dmoz_editors`;
CREATE TABLE IF NOT EXISTS `dmoz_editors` (
  `id` int(11) NOT NULL auto_increment,
  `editor` varchar(255) NOT NULL default '',
  `catid` int(255) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `catid` (`catid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Lisf of editors for ODP categories' AUTO_INCREMENT=1 ;

-- Table structure for table `dmoz_externalpages`
DROP TABLE IF EXISTS `dmoz_externalpages`;
CREATE TABLE IF NOT EXISTS `dmoz_externalpages` (
  `id` int(11) NOT NULL auto_increment,
  `ages` varchar(128) NOT NULL default '',
  `type` varchar(128) NOT NULL default '',
  `link` varchar(255) NOT NULL default '',
  `Title` varchar(255) NOT NULL default '',
  `Description` text NOT NULL,
  `catid` int(11) NOT NULL default '0',
  `priority` int(11) NOT NULL default '0',
  `mediadate` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`id`),
  KEY `Title` (`Title`),
  KEY `catid` (`catid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='List of external pages for ODP categories.' AUTO_INCREMENT=1 ;

-- Table structure for table `dmoz_letterbars`
DROP TABLE IF EXISTS `dmoz_letterbars`;
CREATE TABLE IF NOT EXISTS `dmoz_letterbars` (
  `id` int(11) NOT NULL auto_increment,
  `letterbar` varchar(255) NOT NULL default '',
  `catid` int(11) NOT NULL default '0',
  `lcatid` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `catid` (`catid`),
  KEY `lcatid` (`lcatid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='List of related letterbars for ODP categories' AUTO_INCREMENT=1 ;


-- Table structure for table `dmoz_narrows`
DROP TABLE IF EXISTS `dmoz_narrows`;
CREATE TABLE IF NOT EXISTS `dmoz_narrows` (
  `id` int(11) NOT NULL auto_increment,
  `narrow` varchar(255) NOT NULL default '',
  `priority` int(11) NOT NULL default '0',
  `catid` int(11) NOT NULL default '0',
  `ncatid` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `priority` (`priority`),
  KEY `catid` (`catid`),
  KEY `ncatid` (`ncatid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='List of related narrows for ODP categories' AUTO_INCREMENT=1 ;

-- Table structure for table `dmoz_newsgroups`
DROP TABLE IF EXISTS `dmoz_newsgroups`;
CREATE TABLE IF NOT EXISTS `dmoz_newsgroups` (
  `id` int(11) NOT NULL auto_increment,
  `type` varchar(128) NOT NULL default '',
  `newsGroup` varchar(255) NOT NULL default '',
  `catid` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `catid` (`catid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='List of related newsgroups for ODP categories' AUTO_INCREMENT=1 ;

-- Table structure for table `dmoz_related`
DROP TABLE IF EXISTS `dmoz_related`;
CREATE TABLE IF NOT EXISTS `dmoz_related` (
  `id` int(11) NOT NULL auto_increment,
  `related` varchar(254) NOT NULL default '',
  `catid` int(11) NOT NULL default '0',
  `rcatid` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `catid` (`catid`),
  KEY `rcatid` (`rcatid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='List of related categories for ODP categories.' AUTO_INCREMENT=1 ;

-- Table structure for table `dmoz_symbolics`
DROP TABLE IF EXISTS `dmoz_symbolics`;
CREATE TABLE IF NOT EXISTS `dmoz_symbolics` (
  `id` int(11) NOT NULL auto_increment,
  `resource` varchar(255) NOT NULL default '',
  `symbolic` varchar(255) NOT NULL default '',
  `priority` int(11) NOT NULL default '0',
  `catid` int(11) NOT NULL default '0',
  `scatid` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `priority` (`priority`),
  KEY `catid` (`catid`),
  KEY `scatid` (`scatid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='List of related symbolics for ODP categories' AUTO_INCREMENT=1 ;