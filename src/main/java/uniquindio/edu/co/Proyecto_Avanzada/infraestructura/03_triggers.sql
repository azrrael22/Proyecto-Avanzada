
-- ================================================
-- SCRIPT 3: TRIGGERS PARA VALIDACIONES DE NEGOCIO
-- ================================================

USE gestion_alojamientos;

-- ================================================
-- TRIGGERS PARA VALIDACIÓN DE USUARIOS
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

-- ================================================
-- TRIGGERS PARA CÓDIGOS DE RECUPERACIÓN
-- ================================================

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

-- ================================================
-- TRIGGERS PARA ALOJAMIENTOS
-- ================================================

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

-- ================================================
-- TRIGGERS PARA RESERVAS
-- ================================================

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

-- ================================================
-- TRIGGERS PARA COMENTARIOS
-- ================================================

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

-- ================================================
-- TRIGGERS PARA RESPUESTAS DE COMENTARIOS
-- ================================================

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

-- ================================================
-- TRIGGERS PARA IMÁGENES
-- ================================================

-- Validar imágenes
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