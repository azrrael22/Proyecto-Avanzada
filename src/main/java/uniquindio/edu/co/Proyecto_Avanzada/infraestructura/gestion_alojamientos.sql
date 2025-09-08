-- ================================================
-- SCRIPT SQL - SISTEMA DE GESTIÓN DE ALOJAMIENTOS
-- ================================================

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

-- ================================================
-- ÍNDICES PARA OPTIMIZACIÓN
-- ================================================
CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_usuarios_rol ON usuarios(id_rol);
CREATE INDEX idx_usuarios_estado ON usuarios(estado);

CREATE INDEX idx_alojamientos_ciudad ON alojamientos(ciudad);
CREATE INDEX idx_alojamientos_estado ON alojamientos(estado);
CREATE INDEX idx_alojamientos_tipo ON alojamientos(tipo);
CREATE INDEX idx_alojamientos_precio ON alojamientos(precio_por_noche);

CREATE INDEX idx_reservas_fechas ON reservas(fecha_check_in, fecha_check_out);
CREATE INDEX idx_reservas_estado ON reservas(estado);
CREATE INDEX idx_reservas_fecha_reserva ON reservas(fecha_reserva);

CREATE INDEX idx_comentarios_alojamiento ON comentarios(id_alojamiento);
CREATE INDEX idx_comentarios_calificacion ON comentarios(calificacion);
CREATE INDEX idx_comentarios_fecha ON comentarios(fecha_comentario);

CREATE INDEX idx_alojamientos_anfitrion_estado ON alojamientos(id_anfitrion, estado);
CREATE INDEX idx_reservas_usuario_estado ON reservas(id_usuario, estado);
CREATE INDEX idx_reservas_alojamiento_estado ON reservas(id_alojamiento, estado);
CREATE INDEX idx_imagenes_alojamiento_orden ON imagenes_alojamiento(id_alojamiento, orden);

-- ================================================
-- TRIGGERS PARA VALIDACIONES DE NEGOCIO
-- ================================================

-- Validar edad mínima (13 años)
DELIMITER //
CREATE TRIGGER tr_validar_fecha_nacimiento_insert
    BEFORE INSERT ON usuarios
    FOR EACH ROW
BEGIN
    IF NEW.fecha_nacimiento IS NOT NULL AND NEW.fecha_nacimiento > DATE_SUB(CURDATE(), INTERVAL 13 YEAR) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El usuario debe tener al menos 13 años de edad';
    END IF;
END//

CREATE TRIGGER tr_validar_fecha_nacimiento_update
    BEFORE UPDATE ON usuarios
    FOR EACH ROW
BEGIN
    IF NEW.fecha_nacimiento IS NOT NULL AND NEW.fecha_nacimiento > DATE_SUB(CURDATE(), INTERVAL 13 YEAR) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El usuario debe tener al menos 13 años de edad';
    END IF;
END//
DELIMITER ;

-- Validar datos básicos de usuarios
DELIMITER //
CREATE TRIGGER tr_validar_datos_usuario_insert
    BEFORE INSERT ON usuarios
    FOR EACH ROW
BEGIN
    IF NEW.email NOT REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Formato de email inválido';
    END IF;

    IF NEW.telefono IS NOT NULL AND NEW.telefono NOT REGEXP '^[0-9+\\-\\s()]{10,20}$' THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Formato de teléfono inválido';
    END IF;
END//

CREATE TRIGGER tr_validar_datos_usuario_update
    BEFORE UPDATE ON usuarios
    FOR EACH ROW
BEGIN
    IF NEW.email NOT REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Formato de email inválido';
    END IF;

    IF NEW.telefono IS NOT NULL AND NEW.telefono NOT REGEXP '^[0-9+\\-\\s()]{10,20}$' THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Formato de teléfono inválido';
    END IF;
END//
DELIMITER ;

-- Validar códigos de recuperación
DELIMITER //
CREATE TRIGGER tr_codigos_recuperacion_insert
    BEFORE INSERT ON codigos_recuperacion
    FOR EACH ROW
BEGIN
    SET NEW.fecha_expiracion = DATE_ADD(NEW.fecha_creacion, INTERVAL 15 MINUTE);

    IF NEW.codigo NOT REGEXP '^[0-9]{6}$' THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El código debe tener exactamente 6 dígitos';
    END IF;
END//
DELIMITER ;

-- Validar alojamientos
DELIMITER //
CREATE TRIGGER tr_validar_alojamiento_insert
    BEFORE INSERT ON alojamientos
    FOR EACH ROW
BEGIN
    IF NEW.precio_por_noche <= 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El precio por noche debe ser mayor a 0';
    END IF;

    IF NEW.capacidad_maxima <= 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'La capacidad máxima debe ser mayor a 0';
    END IF;
END//

CREATE TRIGGER tr_validar_alojamiento_update
    BEFORE UPDATE ON alojamientos
    FOR EACH ROW
BEGIN
    IF NEW.precio_por_noche <= 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El precio por noche debe ser mayor a 0';
    END IF;

    IF NEW.capacidad_maxima <= 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'La capacidad máxima debe ser mayor a 0';
    END IF;
END//
DELIMITER ;

-- Validar reservas
DELIMITER //
CREATE TRIGGER tr_validar_reserva_insert
    BEFORE INSERT ON reservas
    FOR EACH ROW
BEGIN
    IF NEW.fecha_check_in < CURDATE() THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'La fecha de check-in no puede ser anterior a hoy';
    END IF;

    IF NEW.fecha_check_out <= NEW.fecha_check_in THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'La fecha de check-out debe ser posterior al check-in';
    END IF;

    IF NEW.numero_huespedes <= 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El número de huéspedes debe ser mayor a 0';
    END IF;

    IF NEW.precio_total <= 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El precio total debe ser mayor a 0';
    END IF;
END//

CREATE TRIGGER tr_validar_reserva_update
    BEFORE UPDATE ON reservas
    FOR EACH ROW
BEGIN
    IF NEW.fecha_check_out <= NEW.fecha_check_in THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'La fecha de check-out debe ser posterior al check-in';
    END IF;

    IF NEW.numero_huespedes <= 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El número de huéspedes debe ser mayor a 0';
    END IF;
END//
DELIMITER ;

-- Validar cancelación con 48 horas de anticipación
DELIMITER //
CREATE TRIGGER tr_validar_cancelacion
    BEFORE UPDATE ON reservas
    FOR EACH ROW
BEGIN
    IF NEW.estado = 'CANCELADA' AND OLD.estado != 'CANCELADA' THEN
        IF TIMESTAMPDIFF(HOUR, NOW(), OLD.fecha_check_in) < 48 THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'No se puede cancelar con menos de 48 horas de anticipación';
        END IF;
        SET NEW.fecha_cancelacion = NOW();
    END IF;
END//
DELIMITER ;

-- Actualizar estado de reservas completadas
DELIMITER //
CREATE TRIGGER tr_actualizar_reservas_completadas
    BEFORE UPDATE ON reservas
    FOR EACH ROW
BEGIN
    IF NEW.fecha_check_out < CURDATE() AND OLD.estado = 'CONFIRMADA' THEN
        SET NEW.estado = 'COMPLETADA';
    END IF;
END//
DELIMITER ;

-- Validar comentarios
DELIMITER //
CREATE TRIGGER tr_validar_comentario_insert
    BEFORE INSERT ON comentarios
    FOR EACH ROW
BEGIN
    IF NEW.calificacion < 1 OR NEW.calificacion > 5 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'La calificación debe estar entre 1 y 5 estrellas';
    END IF;

    IF NEW.comentario IS NOT NULL AND CHAR_LENGTH(NEW.comentario) > 500 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El comentario no puede exceder 500 caracteres';
    END IF;
END//
DELIMITER ;

-- Validar respuestas
DELIMITER //
CREATE TRIGGER tr_validar_respuesta_insert
    BEFORE INSERT ON respuestas_comentarios
    FOR EACH ROW
BEGIN
    IF CHAR_LENGTH(NEW.respuesta) > 300 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'La respuesta no puede exceder 300 caracteres';
    END IF;
END//
DELIMITER ;

-- Validar imágenes (sin conflicto de tabla)
DELIMITER //
CREATE TRIGGER tr_validar_imagen_insert
    BEFORE INSERT ON imagenes_alojamiento
    FOR EACH ROW
BEGIN
    IF NEW.orden <= 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El orden debe ser mayor a 0';
    END IF;
END//
DELIMITER ;

-- ================================================
-- INSERTAR ROLES Y PERMISOS
-- ================================================

INSERT INTO roles (nombre_rol, descripcion) VALUES
                                                ('USUARIO', 'Huésped que puede buscar y reservar alojamientos'),
                                                ('ANFITRION', 'Propietario que puede crear y gestionar alojamientos'),
                                                ('ADMINISTRADOR', 'Administrador del sistema con acceso completo');

INSERT INTO permisos (nombre_permiso, descripcion, modulo, accion) VALUES
-- Módulo AUTENTICACION
('LOGIN', 'Iniciar sesión en el sistema', 'AUTENTICACION', 'ACCEDER'),
('LOGOUT', 'Cerrar sesión', 'AUTENTICACION', 'ACCEDER'),
('RECUPERAR_CONTRASEÑA', 'Solicitar recuperación de contraseña', 'AUTENTICACION', 'RECUPERAR'),
('CAMBIAR_CONTRASEÑA', 'Cambiar contraseña propia', 'AUTENTICACION', 'ACTUALIZAR'),

-- Módulo PERFIL
('VER_PROPIO_PERFIL', 'Ver información del perfil propio', 'PERFIL', 'LEER'),
('EDITAR_PROPIO_PERFIL', 'Editar información del perfil propio', 'PERFIL', 'ACTUALIZAR'),
('SOLICITAR_CAMBIO_A_ANFITRION', 'Convertirse en anfitrión automáticamente', 'PERFIL', 'ACTUALIZAR'),

-- Módulo ALOJAMIENTOS
('VER_VISTA_PREVIA_ALOJAMIENTOS', 'Ver lista limitada de alojamientos (visitantes)', 'ALOJAMIENTOS', 'LEER'),
('VER_DETALLE_ALOJAMIENTO', 'Ver detalles completos de alojamientos', 'ALOJAMIENTOS', 'LEER'),
('BUSCAR_ALOJAMIENTOS', 'Buscar alojamientos con filtros', 'ALOJAMIENTOS', 'LEER'),
('CREAR_ALOJAMIENTO', 'Crear nuevos alojamientos', 'ALOJAMIENTOS', 'CREAR'),
('EDITAR_PROPIO_ALOJAMIENTO', 'Editar alojamientos propios', 'ALOJAMIENTOS', 'ACTUALIZAR'),
('ELIMINAR_PROPIO_ALOJAMIENTO', 'Eliminar alojamientos propios (sin reservas futuras)', 'ALOJAMIENTOS', 'ELIMINAR'),
('ACTIVAR_INACTIVAR_PROPIO_ALOJAMIENTO', 'Activar/Inactivar alojamientos propios', 'ALOJAMIENTOS', 'ACTUALIZAR'),

-- Módulo RESERVAS
('CREAR_RESERVA', 'Realizar nuevas reservas', 'RESERVAS', 'CREAR'),
('VER_PROPIAS_RESERVAS', 'Ver historial de reservas propias', 'RESERVAS', 'LEER'),
('CANCELAR_PROPIA_RESERVA', 'Cancelar reservas propias', 'RESERVAS', 'ACTUALIZAR'),
('VER_RESERVAS_MIS_ALOJAMIENTOS', 'Ver reservas de mis alojamientos', 'RESERVAS', 'LEER'),

-- Módulo COMENTARIOS
('CREAR_COMENTARIO', 'Crear comentarios en reservas completadas', 'COMENTARIOS', 'CREAR'),
('VER_COMENTARIOS', 'Ver comentarios de alojamientos', 'COMENTARIOS', 'LEER'),
('RESPONDER_COMENTARIO', 'Responder a comentarios de mis alojamientos', 'COMENTARIOS', 'CREAR'),

-- Módulo USUARIOS (Solo Administradores)
('VER_TODOS_USUARIOS', 'Ver lista de todos los usuarios', 'USUARIOS', 'LEER'),
('ACTIVAR_DESACTIVAR_USUARIO', 'Activar/Desactivar cuentas de usuario', 'USUARIOS', 'ACTUALIZAR'),
('CAMBIAR_ROL_USUARIO', 'Cambiar rol de usuarios', 'USUARIOS', 'ACTUALIZAR'),
('VER_HISTORIAL_ACTIVIDAD_USUARIO', 'Ver historial de actividad de usuarios', 'USUARIOS', 'LEER'),
('VER_ESTADISTICAS_USUARIO', 'Ver estadísticas de usuarios', 'USUARIOS', 'LEER'),

-- Módulo METRICAS
('VER_METRICAS_MIS_ALOJAMIENTOS', 'Ver métricas de mis alojamientos', 'METRICAS', 'LEER'),
('VER_AUDITORIA_COMPLETA', 'Ver auditoría completa del sistema', 'AUDITORIA', 'LEER');

-- ================================================
-- ASIGNAR PERMISOS A ROLES
-- ================================================

-- PERMISOS PARA ROL USUARIO (ID: 1)
INSERT INTO roles_permisos (id_rol, id_permiso)
SELECT 1, id_permiso FROM permisos WHERE nombre_permiso IN (
                                                            'LOGIN', 'LOGOUT', 'RECUPERAR_CONTRASEÑA', 'CAMBIAR_CONTRASEÑA',
                                                            'VER_PROPIO_PERFIL', 'EDITAR_PROPIO_PERFIL', 'SOLICITAR_CAMBIO_A_ANFITRION',
                                                            'VER_DETALLE_ALOJAMIENTO', 'BUSCAR_ALOJAMIENTOS',
                                                            'CREAR_RESERVA', 'VER_PROPIAS_RESERVAS', 'CANCELAR_PROPIA_RESERVA',
                                                            'CREAR_COMENTARIO', 'VER_COMENTARIOS'
    );

-- PERMISOS PARA ROL ANFITRION (ID: 2)
INSERT INTO roles_permisos (id_rol, id_permiso)
SELECT 2, id_permiso FROM permisos WHERE nombre_permiso IN (
                                                            'LOGIN', 'LOGOUT', 'RECUPERAR_CONTRASEÑA', 'CAMBIAR_CONTRASEÑA',
                                                            'VER_PROPIO_PERFIL', 'EDITAR_PROPIO_PERFIL', 'SOLICITAR_CAMBIO_A_ANFITRION',
                                                            'VER_DETALLE_ALOJAMIENTO', 'BUSCAR_ALOJAMIENTOS',
                                                            'CREAR_RESERVA', 'VER_PROPIAS_RESERVAS', 'CANCELAR_PROPIA_RESERVA',
                                                            'CREAR_COMENTARIO', 'VER_COMENTARIOS',
                                                            'CREAR_ALOJAMIENTO', 'EDITAR_PROPIO_ALOJAMIENTO', 'ELIMINAR_PROPIO_ALOJAMIENTO',
                                                            'ACTIVAR_INACTIVAR_PROPIO_ALOJAMIENTO', 'VER_RESERVAS_MIS_ALOJAMIENTOS',
                                                            'RESPONDER_COMENTARIO', 'VER_METRICAS_MIS_ALOJAMIENTOS'
    );

-- PERMISOS PARA ROL ADMINISTRADOR (ID: 3)
INSERT INTO roles_permisos (id_rol, id_permiso)
SELECT 3, id_permiso FROM permisos;


-- ================================================
-- VISTAS ÚTILES
-- ================================================

CREATE VIEW v_permisos_usuario AS
SELECT
    u.id_usuario,
    u.nombre as nombre_usuario,
    u.email,
    r.nombre_rol,
    p.nombre_permiso,
    p.modulo,
    p.accion,
    p.descripcion as descripcion_permiso
FROM usuarios u
         JOIN roles r ON u.id_rol = r.id_rol
         JOIN roles_permisos rp ON r.id_rol = rp.id_rol
         JOIN permisos p ON rp.id_permiso = p.id_permiso
WHERE u.estado = 'ACTIVO' AND r.es_activo = TRUE AND p.es_activo = TRUE;

CREATE VIEW v_alojamientos_calificacion AS
SELECT
    a.*,
    COALESCE(AVG(c.calificacion), 0) as calificacion_promedio,
    COUNT(c.id_comentario) as total_comentarios,
    u.nombre as nombre_anfitrion,
    r.nombre_rol as rol_anfitrion
FROM alojamientos a
         LEFT JOIN comentarios c ON a.id_alojamiento = c.id_alojamiento
         JOIN usuarios u ON a.id_anfitrion = u.id_usuario
         JOIN roles r ON u.id_rol = r.id_rol
WHERE a.estado = 'ACTIVO'
GROUP BY a.id_alojamiento;