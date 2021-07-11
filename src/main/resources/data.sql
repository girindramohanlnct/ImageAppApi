DROP TABLE IF EXISTS image;

CREATE TABLE image (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  url VARCHAR(250) NOT NULL,
  details VARCHAR(250) DEFAULT NULL
);

INSERT INTO image (name, url, details) VALUES
  ('testing', 'http://clipart-library.com/images/6Tpo6G8TE.jpg', 'Billionaire Industrialist');