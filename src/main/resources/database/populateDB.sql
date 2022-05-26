INSERT INTO  roles(name) VALUES
('ROLE_USER'),
('ROLE_ADMIN') ;

-- 12345
INSERT INTO  users VALUES
(1, 'user', 'Default newUser','Default newUser','user@mail.ru',null ,'$2a$04$sweeYfA1IOmERGq0mXFjXeY7ksEK.7h0AHgj0yM84XSOICobdw9nC', '2020-12-10 00:38:25', '2020-12-10 00:38:31','ACTIVE'),
(2, 'romanungefuk', 'Roman','Ungefuk','roman.ungefuk@gmail.com',null,'$2a$04$yrw4nT/LZzRJeiptGHGuKO4rhhb.LTzakr0MayNLrgW.T55iYEdNq', '2020-12-10 00:38:25', '2020-12-10 00:38:31','ACTIVE'),
(3, 'testuser', 'Test','User','testuser@icloud.com',null,'$2y$12$P5aOczmzIpLg.fkwB2TEvu/gskl1ZhL3FXwsps8fOGmRBhmJx8Pva', '2020-12-10 00:38:25', '2020-12-10 00:38:31','ACTIVE');

INSERT INTO  user_roles VALUES
(1,1),(2,1),(2,2),(3,1),(3,2);