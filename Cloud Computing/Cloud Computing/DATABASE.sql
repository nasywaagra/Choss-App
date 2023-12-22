CREATE TABLE users (
    email VARCHAR(500) NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(500) NOT NULL,
    PRIMARY KEY (email)
);

CREATE TABLE clothes (
    cloth_id INT NOT NULL AUTO_INCREMENT,
    ClothesName VARCHAR(500) NOT NULL,
    PRIMARY KEY (cloth_id)
);

CREATE TABLE userPersonality (
    personality_id INT NOT NULL AUTO_INCREMENT,
    email VARCHAR(500) NOT NULL,
    cloth_id INT NOT NULL,
    name_personality VARCHAR(500) NOT NULL,
    PRIMARY KEY (personality_id),
    FOREIGN KEY (email) REFERENCES user(email),
    FOREIGN KEY (cloth_id) REFERENCES clothes(cloth_id)
);

CREATE TABLE favoriteCloth (
    favorite_id INT NOT NULL AUTO_INCREMENT,
    email VARCHAR(500) NOT NULL,
    cloth_id INT NOT NULL,
    PRIMARY KEY (favorite_id),
    FOREIGN KEY (email) REFERENCES user(email),
    FOREIGN KEY (cloth_id) REFERENCES clothes(cloth_id)
);
