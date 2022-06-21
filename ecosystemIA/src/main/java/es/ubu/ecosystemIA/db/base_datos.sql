CREATE DATABASE ecosystemIA_DB;

GRANT ALL ON springapp.* TO ecosystemIAuser@'%' IDENTIFIED BY 'ecosystemIAuser';
GRANT ALL ON springapp.* TO ecosystemIAuser@localhost IDENTIFIED BY 'ecosystemIAuser';

USE ecosystemIA_DB;

CREATE TABLE modelosRn (
  id INTEGER PRIMARY KEY,
  nombre varchar(255),
  descripcion varchar(500),
  multilayerNetwork BLOB,
  imagenHeightInput integer,
  imagenWidthInput integer,
  imagenCanales integer
);
CREATE INDEX modeloRn_descripcion ON modelosRn(descripcion);


