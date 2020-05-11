CREATE SCHEMA soteria ;

USE soteria;

CREATE TABLE soteria.user (
  id INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(45) NOT NULL,
  password VARCHAR(45) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX iduser_UNIQUE (id ASC),
  UNIQUE INDEX username_UNIQUE (username ASC));

CREATE TABLE soteria.password (
  id INT NOT NULL AUTO_INCREMENT,
  domain VARCHAR(45) NOT NULL,
  username VARCHAR(45) NOT NULL,
  password VARCHAR(45) NOT NULL,
  fkUser INT NOT NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX id_UNIQUE (id ASC),
  INDEX fkUser_idx (fkUser ASC),
  CONSTRAINT fkUser
    FOREIGN KEY (fkUser)
    REFERENCES soteria.user (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);