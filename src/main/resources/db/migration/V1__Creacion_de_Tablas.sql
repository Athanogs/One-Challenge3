CREATE TABLE perfil (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        nombre VARCHAR(255) NOT NULL,
                        PRIMARY KEY (id)
);

CREATE TABLE usuario (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         nombre VARCHAR(255) NOT NULL,
                         correoElectronico VARCHAR(255) NOT NULL,
                         contrasena VARCHAR(255) NOT NULL,
                         PRIMARY KEY (id)
);

CREATE TABLE curso (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       nombre VARCHAR(255) NOT NULL,
                       categoria VARCHAR(255) NOT NULL,
                       PRIMARY KEY (id)
);

CREATE TABLE topico (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        titulo VARCHAR(255) NOT NULL,
                        mensaje TEXT NOT NULL,
                        fechaCreacion DATETIME NOT NULL,
                        status VARCHAR(255) NOT NULL,
                        autor BIGINT NOT NULL,
                        curso BIGINT NOT NULL,
                        PRIMARY KEY (id),
                        CONSTRAINT fk_topico_autor
                            FOREIGN KEY (autor) REFERENCES usuario(id),
                        CONSTRAINT fk_topico_curso
                            FOREIGN KEY (curso) REFERENCES curso(id)
);

CREATE TABLE respuesta (
                           id BIGINT NOT NULL AUTO_INCREMENT,
                           mensaje TEXT NOT NULL,
                           topico BIGINT NOT NULL,
                           fechaCreacion DATETIME NOT NULL,
                           autor BIGINT NOT NULL,
                           solucion BOOLEAN NOT NULL,
                           PRIMARY KEY (id),
                           CONSTRAINT fk_respuesta_topico
                               FOREIGN KEY (topico) REFERENCES topico(id),
                           CONSTRAINT fk_respuesta_autor
                               FOREIGN KEY (autor) REFERENCES usuario(id)
);

CREATE TABLE usuario_perfil (
                                usuario BIGINT NOT NULL,
                                perfil BIGINT NOT NULL,
                                PRIMARY KEY (usuario, perfil),
                                CONSTRAINT fk_usuario_perfil_usuario
                                    FOREIGN KEY (usuario) REFERENCES usuario(id),
                                CONSTRAINT fk_usuario_perfil_perfil
                                    FOREIGN KEY (perfil) REFERENCES perfil(id)
);