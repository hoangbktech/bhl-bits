use citebank;
SELECT COUNT(*) FROM node AS n JOIN biblio AS b ON (n.nid = b.nid) WHERE TYPE = 'biblio'
