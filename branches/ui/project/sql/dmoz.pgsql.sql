-- Database: dmoz

-- DROP DATABASE dmoz;

-- CREATE DATABASE dmoz
--   WITH OWNER = dmoz
--        ENCODING = 'UNICODE'
--        TABLESPACE = pg_default;
-- GRANT TEMPORARY ON DATABASE dmoz TO public;
-- GRANT ALL ON DATABASE dmoz TO dmoz WITH GRANT OPTION;


-- DROP TABLE dmoz_aliases;
CREATE TABLE dmoz_aliases (
  id SERIAL,
  catid int NOT NULL default '0',
  Alias varchar NOT NULL default '',
  Title varchar NOT NULL default '',
  Target varchar NOT NULL default '',
  tcatid int NOT NULL default '0'
);
ALTER TABLE dmoz_aliases ADD PRIMARY KEY (id);
-- Index: dmoz_aliases_title

-- DROP INDEX dmoz_aliases_title;

CREATE INDEX dmoz_aliases_title
  ON dmoz_aliases
  USING btree
  (title);

-- Index: dmoz_aliases_catid

-- DROP INDEX dmoz_aliases_catid;

CREATE INDEX dmoz_aliases_catid
  ON dmoz_aliases
  USING btree
  (catid);

-- Index: dmoz_aliases_tcatid

-- DROP INDEX dmoz_aliases_tcatid;

CREATE INDEX dmoz_aliases_tcatid
  ON dmoz_aliases
  USING btree
  (tcatid);

CREATE TABLE dmoz_altlangs (
  id SERIAL,
  language varchar NOT NULL default '',
  resource varchar NOT NULL default '',
  catid int NOT NULL default '0',
  rcatid int NOT NULL default '0'
);

-- Constraint: dmoz_altlangs_pkey

-- ALTER TABLE dmoz_altlangs DROP CONSTRAINT dmoz_altlangs_pkey

ALTER TABLE dmoz_altlangs
  ADD CONSTRAINT dmoz_altlangs_pkey PRIMARY KEY(id);

-- Index: dmoz_altlangs_language

-- DROP INDEX dmoz_altlangs_language;

CREATE INDEX dmoz_altlangs_language
  ON dmoz_altlangs
  USING btree
  (language);

-- Index: dmoz_altlangs_catid

-- DROP INDEX dmoz_altlangs_catid;

CREATE INDEX dmoz_altlangs_catid
  ON dmoz_altlangs
  USING btree
  (catid);

-- Index: dmoz_altlangs_rcatid

-- DROP INDEX dmoz_altlangs_rcatid;

CREATE INDEX dmoz_altlangs_rcatid
  ON dmoz_altlangs
  USING btree
  (catid);

CREATE TABLE dmoz_categories (
  id SERIAL,
  Topic varchar NOT NULL default '',
  catid int NOT NULL default '0',
  aolsearch varchar NOT NULL default '',
  dispname varchar NOT NULL default '',
  charset varchar NOT NULL default '',
  Title varchar NOT NULL default '',
  Description text NOT NULL,
  lastUpdate varchar NOT NULL default '',
  fatherid int NOT NULL default '0'
);
ALTER TABLE dmoz_categories ADD PRIMARY KEY (id);

-- Index: dmoz_categories_title

-- DROP INDEX dmoz_categories_title;

CREATE INDEX dmoz_categories_title
  ON dmoz_categories
  USING btree
  (title);

-- Index: dmoz_categories_catid

-- DROP INDEX dmoz_categories_catid;

CREATE INDEX dmoz_categories_catid
  ON dmoz_categories
  USING btree
  (catid);


-- Index: dmoz_categories_fatherid

-- DROP INDEX dmoz_categories_fatherid;

CREATE INDEX dmoz_categories_fatherid
  ON dmoz_categories
  USING btree
  (fatherid);

CREATE TABLE dmoz_editors (
  id SERIAL,
  editor varchar NOT NULL default '',
  catid int NOT NULL default '0'
);
ALTER TABLE dmoz_editors ADD PRIMARY KEY (id);

-- Index: dmoz_editors_catid

-- DROP INDEX dmoz_editors_catid;

CREATE INDEX dmoz_editors_catid
  ON dmoz_editors
  USING btree
  (catid);

CREATE TABLE dmoz_externalpages (
  id SERIAL,
  ages varchar NOT NULL default '',
  type varchar NOT NULL default '',
  link varchar NOT NULL default '',
  Title varchar NOT NULL default '',
  Description text NOT NULL,
  catid int NOT NULL default '0',
  priority int NOT NULL default '0',
  mediadate varchar NOT NULL default ''
);
ALTER TABLE dmoz_externalpages ADD PRIMARY KEY (id);

-- Index: dmoz_externalpages_title

-- DROP INDEX dmoz_externalpages_title;

CREATE INDEX dmoz_externalpages_title
  ON dmoz_externalpages
  USING btree
  (title);

-- Index: dmoz_externalpages_catid

-- DROP INDEX dmoz_externalpages_catid;

CREATE INDEX dmoz_externalpages_catid
  ON dmoz_externalpages
  USING btree
  (catid);

CREATE TABLE dmoz_letterbars (
  id SERIAL,
  letterbar varchar NOT NULL default '',
  catid int NOT NULL default '0',
  lcatid int NOT NULL default '0'
);
ALTER TABLE dmoz_letterbars ADD PRIMARY KEY (id);

-- Index: dmoz_letterbars_catid

-- DROP INDEX dmoz_letterbars_catid;

CREATE INDEX dmoz_letterbars_catid
  ON dmoz_letterbars
  USING btree
  (catid);

-- Index: dmoz_letterbars_lcatid

-- DROP INDEX dmoz_letterbars_lcatid;

CREATE INDEX dmoz_letterbars_lcatid
  ON dmoz_letterbars
  USING btree
  (lcatid);

CREATE TABLE dmoz_narrows (
  id SERIAL,
  narrow varchar NOT NULL default '',
  priority int NOT NULL default '0',
  catid int NOT NULL default '0',
  ncatid int NOT NULL default '0'
);
ALTER TABLE dmoz_narrows ADD PRIMARY KEY (id);

-- Index: dmoz_narrows_priority

-- DROP INDEX dmoz_narrows_priority;

CREATE INDEX dmoz_narrows_priority
  ON dmoz_narrows
  USING btree
  (priority);

-- Index: dmoz_narrows_catid

-- DROP INDEX dmoz_narrows_catid;

CREATE INDEX dmoz_narrows_catid
  ON dmoz_narrows
  USING btree
  (catid);

-- Index: dmoz_narrows_ncatid

-- DROP INDEX dmoz_narrows_ncatid;

CREATE INDEX dmoz_narrows_ncatid
  ON dmoz_narrows
  USING btree
  (ncatid);

CREATE TABLE dmoz_newsgroups (
  id SERIAL,
  type varchar NOT NULL default '',
  newsGroup varchar NOT NULL default '',
  catid int NOT NULL default '0'
);

ALTER TABLE dmoz_newsgroups ADD PRIMARY KEY (id);

-- Index: dmoz_newsgroups_catid

-- DROP INDEX dmoz_newsgroups_catid;

CREATE INDEX dmoz_newsgroups_catid
  ON dmoz_newsgroups
  USING btree
  (catid);

CREATE TABLE dmoz_related (
  id SERIAL,
  related varchar NOT NULL default '',
  catid int NOT NULL default '0',
  rcatid int NOT NULL default '0'
);
ALTER TABLE dmoz_related ADD PRIMARY KEY (id);

-- Index: dmoz_related_catid

-- DROP INDEX dmoz_related_catid;

CREATE INDEX dmoz_related_catid
  ON dmoz_related
  USING btree
  (catid);

-- Index: dmoz_related_rcatid

-- DROP INDEX dmoz_related_rcatid;

CREATE INDEX dmoz_related_rcatid
  ON dmoz_related
  USING btree
  (rcatid);

CREATE TABLE dmoz_symbolics (
  id SERIAL,
  resource varchar NOT NULL default '',
  symbolic varchar NOT NULL default '',
  priority int NOT NULL default '0',
  catid int NOT NULL default '0',
  scatid int NOT NULL default '0'
);
ALTER TABLE dmoz_symbolics ADD PRIMARY KEY (id);

-- Index: dmoz_symbolics_priority

-- DROP INDEX dmoz_symbolics_priority;

CREATE INDEX dmoz_symbolics_priority
  ON dmoz_symbolics
  USING btree
  (priority);

-- Index: dmoz_symbolics_catid

-- DROP INDEX dmoz_symbolics_catid;

CREATE INDEX dmoz_symbolics_catid
  ON dmoz_symbolics
  USING btree
  (catid);

-- Index: dmoz_symbolics_scatid

-- DROP INDEX dmoz_symbolics_scatid;

CREATE INDEX dmoz_symbolics_scatid
  ON dmoz_symbolics
  USING btree
  (scatid);  

