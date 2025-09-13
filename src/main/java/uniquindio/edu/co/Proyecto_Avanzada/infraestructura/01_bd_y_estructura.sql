
-- ======================================================
-- SCRIPT 1: CREACIÓN DE LA BASE DE DATOS Y SU ESTRUCTURA
-- ======================================================

-- Crear base de datos
CREATE DATABASE IF NOT EXISTS gestion_alojamientos
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE gestion_alojamientos;

-- ================================================
-- TABLA: ROLES
-- ================================================
CREATE TABLE roles (
                       id_rol INT AUTO_INCREMENT PRIMARY KEY,
                       nombre_rol VARCHAR(50) NOT NULL UNIQUE,
                       descripcion TEXT,
                       es_activo BOOLEAN DEFAULT TRUE,
                       fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ================================================
-- TABLA: PERMISOS
-- ================================================
CREATE TABLE permisos (
                          id_permiso INT AUTO_INCREMENT PRIMARY KEY,
                          nombre_permiso VARCHAR(100) NOT NULL UNIQUE,
                          descripcion TEXT,
                          modulo VARCHAR(50) NOT NULL,
                          accion VARCHAR(50) NOT NULL,
                          es_activo BOOLEAN DEFAULT TRUE,
                          fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ================================================
-- TABLA: ROLES_PERMISOS
-- ================================================
CREATE TABLE roles_permisos (
                                id_rol_permiso INT AUTO_INCREMENT PRIMARY KEY,
                                id_rol INT NOT NULL,
                                id_permiso INT NOT NULL,
                                fecha_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                FOREIGN KEY (id_rol) REFERENCES roles(id_rol) ON DELETE CASCADE,
                                FOREIGN KEY (id_permiso) REFERENCES permisos(id_permiso) ON DELETE CASCADE,
                                UNIQUE KEY uk_rol_permiso (id_rol, id_permiso)
);

-- ================================================
-- TABLA: USUARIOS
-- ================================================
CREATE TABLE usuarios (
                          id_usuario INT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          email VARCHAR(150) NOT NULL UNIQUE,
                          contrasenia_hash VARCHAR(255) NOT NULL,
                          telefono VARCHAR(20),
                          fecha_nacimiento DATE,
                          foto_perfil VARCHAR(500),
                          id_rol INT NOT NULL,
                          estado ENUM('ACTIVO', 'INACTIVO') DEFAULT 'ACTIVO',
                          fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          fecha_ultima_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                          FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
);

-- ================================================
-- TABLA: CODIGOS_RECUPERACION
-- ================================================
CREATE TABLE codigos_recuperacion (
                                      id_codigo INT AUTO_INCREMENT PRIMARY KEY,
                                      id_usuario INT NOT NULL,
                                      codigo VARCHAR(6) NOT NULL,
                                      fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      fecha_expiracion TIMESTAMP NOT NULL,
                                      usado BOOLEAN DEFAULT FALSE,

                                      FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

-- ================================================
-- TABLA: ALOJAMIENTOS
-- ================================================
CREATE TABLE alojamientos (
                              id_alojamiento INT AUTO_INCREMENT PRIMARY KEY,
                              id_anfitrion INT NOT NULL,
                              titulo VARCHAR(200) NOT NULL,
                              descripcion TEXT NOT NULL,
                              ciudad VARCHAR(100) NOT NULL,
                              direccion_completa VARCHAR(500) NOT NULL,
                              precio_por_noche DECIMAL(10,2) NOT NULL,
                              capacidad_maxima INT NOT NULL,
                              tipo ENUM('CASA', 'APARTAMENTO', 'FINCA') NOT NULL,
                              servicios JSON,
                              estado ENUM('ACTIVO', 'INACTIVO', 'ELIMINADO') DEFAULT 'ACTIVO',
                              fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                              FOREIGN KEY (id_anfitrion) REFERENCES usuarios(id_usuario)
);

-- ================================================
-- TABLA: IMAGENES_ALOJAMIENTO
-- ================================================
CREATE TABLE imagenes_alojamiento (
                                      id_imagen INT AUTO_INCREMENT PRIMARY KEY,
                                      id_alojamiento INT NOT NULL,
                                      url_imagen VARCHAR(500) NOT NULL,
                                      es_principal BOOLEAN DEFAULT FALSE,
                                      orden INT DEFAULT 1,
                                      fecha_subida TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                      FOREIGN KEY (id_alojamiento) REFERENCES alojamientos(id_alojamiento) ON DELETE CASCADE
);

-- ================================================
-- TABLA: RESERVAS
-- ================================================
CREATE TABLE reservas (
                          id_reserva INT AUTO_INCREMENT PRIMARY KEY,
                          id_usuario INT NOT NULL,
                          id_alojamiento INT NOT NULL,
                          fecha_check_in DATE NOT NULL,
                          fecha_check_out DATE NOT NULL,
                          numero_huespedes INT NOT NULL,
                          precio_total DECIMAL(10,2) NOT NULL,
                          estado ENUM('PENDIENTE', 'CONFIRMADA', 'CANCELADA', 'COMPLETADA') DEFAULT 'PENDIENTE',
                          fecha_reserva TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          fecha_cancelacion TIMESTAMP NULL,
                          motivo_cancelacion TEXT NULL,

                          FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
                          FOREIGN KEY (id_alojamiento) REFERENCES alojamientos(id_alojamiento)
);

-- ================================================
-- TABLA: COMENTARIOS
-- ================================================
CREATE TABLE comentarios (
                             id_comentario INT AUTO_INCREMENT PRIMARY KEY,
                             id_reserva INT NOT NULL UNIQUE,
                             id_usuario INT NOT NULL,
                             id_alojamiento INT NOT NULL,
                             calificacion INT NOT NULL,
                             comentario VARCHAR(500),
                             fecha_comentario TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                             FOREIGN KEY (id_reserva) REFERENCES reservas(id_reserva) ON DELETE CASCADE,
                             FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
                             FOREIGN KEY (id_alojamiento) REFERENCES alojamientos(id_alojamiento)
);

-- ================================================
-- TABLA: RESPUESTAS_COMENTARIOS
-- ================================================
CREATE TABLE respuestas_comentarios (
                                        id_respuesta INT AUTO_INCREMENT PRIMARY KEY,
                                        id_comentario INT NOT NULL UNIQUE,
                                        id_anfitrion INT NOT NULL,
                                        respuesta VARCHAR(300) NOT NULL,
                                        fecha_respuesta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                        FOREIGN KEY (id_comentario) REFERENCES comentarios(id_comentario) ON DELETE CASCADE,
                                        FOREIGN KEY (id_anfitrion) REFERENCES usuarios(id_usuario)
);

-- ================================================
-- TABLA: AUDITORIA_USUARIOS
-- ================================================
CREATE TABLE auditoria_usuarios (
                                    id_auditoria INT AUTO_INCREMENT PRIMARY KEY,
                                    id_usuario INT NOT NULL,
                                    id_administrador INT NOT NULL,
                                    accion ENUM('ACTIVAR', 'DESACTIVAR', 'CAMBIO_ROL') NOT NULL,
                                    valor_anterior VARCHAR(50),
                                    valor_nuevo VARCHAR(50),
                                    justificacion TEXT NOT NULL,
                                    fecha_accion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
                                    FOREIGN KEY (id_administrador) REFERENCES usuarios(id_usuario)
);