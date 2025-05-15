-- Inserindo usuários
INSERT INTO tb_user (id, username, email, password, image_user) VALUES 
(1, 'admin', 'admin@example.com', 'ga@783R', 'admin.png'),
(2, 'joao', 'joao@example.com', 'j019PH1*', 'joao.png'),
(3, 'maria', 'maria@example.com', 'ma42K&1', 'maria.png');

-- Inserindo credenciais
INSERT INTO tb_credential (id_password, user_id, system_name, password_body, position) VALUES
(1, 1, 'Github', 'senhaGithub123', 1),
(2, 1, 'LinkedIn', 'senhaLinkedIn123', 2),
(3, 2, 'Outlook', 'senhaOutlook456', 1),
(4, 3, 'Facebook', 'senhaFacebook789', 1);
