-- ================================================
-- SCRIPT 5: VISTAS DEL SISTEMA
-- ================================================

USE gestion_alojamientos;

-- ================================================
-- VISTA: PERMISOS POR USUARIO
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

-- ================================================
-- VISTA: ALOJAMIENTOS CON CALIFICACIÓN
-- ================================================
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

-- ================================================
-- VISTA: ESTADÍSTICAS DE RESERVAS POR ALOJAMIENTO
-- ================================================
CREATE VIEW v_estadisticas_reservas AS
SELECT
    a.id_alojamiento,
    a.titulo,
    a.ciudad,
    u.nombre as nombre_anfitrion,
    COUNT(r.id_reserva) as total_reservas,
    COUNT(CASE WHEN r.estado = 'CONFIRMADA' THEN 1 END) as reservas_confirmadas,
    COUNT(CASE WHEN r.estado = 'COMPLETADA' THEN 1 END) as reservas_completadas,
    COUNT(CASE WHEN r.estado = 'CANCELADA' THEN 1 END) as reservas_canceladas,
    COALESCE(SUM(CASE WHEN r.estado IN ('CONFIRMADA', 'COMPLETADA') THEN r.precio_total END), 0) as ingresos_totales
FROM alojamientos a
         LEFT JOIN reservas r ON a.id_alojamiento = r.id_alojamiento
         JOIN usuarios u ON a.id_anfitrion = u.id_usuario
WHERE a.estado = 'ACTIVO'
GROUP BY a.id_alojamiento;

-- ================================================
-- VISTA: RESUMEN DE USUARIOS
-- ================================================
CREATE VIEW v_resumen_usuarios AS
SELECT
    u.id_usuario,
    u.nombre,
    u.email,
    u.telefono,
    r.nombre_rol,
    u.estado,
    u.fecha_registro,
    COUNT(DISTINCT res.id_reserva) as total_reservas_realizadas,
    COUNT(DISTINCT al.id_alojamiento) as total_alojamientos_creados,
    COUNT(DISTINCT c.id_comentario) as total_comentarios_escritos
FROM usuarios u
         JOIN roles r ON u.id_rol = r.id_rol
         LEFT JOIN reservas res ON u.id_usuario = res.id_usuario
         LEFT JOIN alojamientos al ON u.id_usuario = al.id_anfitrion
         LEFT JOIN comentarios c ON u.id_usuario = c.id_usuario
GROUP BY u.id_usuario;

-- ================================================
-- VISTA: DISPONIBILIDAD DE ALOJAMIENTOS
-- ================================================
CREATE VIEW v_disponibilidad_alojamientos AS
SELECT
    a.id_alojamiento,
    a.titulo,
    a.ciudad,
    a.precio_por_noche,
    a.capacidad_maxima,
    a.tipo,
    COUNT(r.id_reserva) as reservas_futuras,
    CASE
        WHEN COUNT(r.id_reserva) = 0 THEN 'DISPONIBLE'
        ELSE 'CON_RESERVAS'
        END as estado_disponibilidad
FROM alojamientos a
         LEFT JOIN reservas r ON a.id_alojamiento = r.id_alojamiento
    AND r.estado IN ('PENDIENTE', 'CONFIRMADA')
    AND r.fecha_check_in >= CURDATE()
WHERE a.estado = 'ACTIVO'
GROUP BY a.id_alojamiento;