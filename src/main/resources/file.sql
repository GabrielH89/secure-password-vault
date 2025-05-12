create table tb_user (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(600) NOT NULL UNIQUE,
    password VARCHAR(300) NOT NULL,
    image_user varchar(255)  
);

CREATE TABLE tb_credential (
    id_password BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    system_name VARCHAR(200) NOT NULL,
    password_body VARCHAR(500) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    position int,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES tb_user(id) ON DELETE CASCADE
);