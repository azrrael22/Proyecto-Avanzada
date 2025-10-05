CREATE TABLE favoritos (
                           id_usuario INT NOT NULL,
                           id_alojamiento INT NOT NULL,
                           PRIMARY KEY (id_usuario, id_alojamiento),
                           FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
                           FOREIGN KEY (id_alojamiento) REFERENCES alojamientos(id_alojamiento) ON DELETE CASCADE
);