
-- ============================================================
-- DESACTIVAR MODO SEGURO TEMPORALMENTE
-- ============================================================
SET SQL_SAFE_UPDATES = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================
-- 1. CREAR ROLES SI NO EXISTEN
-- ============================================================

-- Rol USUARIO
INSERT INTO roles (id_rol, nombre_rol, descripcion, es_activo)
SELECT 1, 'USUARIO', 'Usuario regular del sistema', TRUE
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE nombre_rol = 'USUARIO');

-- Rol ANFITRION
INSERT INTO roles (id_rol, nombre_rol, descripcion, es_activo)
SELECT 2, 'ANFITRION', 'Usuario que puede crear alojamientos', TRUE
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE nombre_rol = 'ANFITRION');

-- Rol ADMINISTRADOR
INSERT INTO roles (id_rol, nombre_rol, descripcion, es_activo)
SELECT 3, 'ADMINISTRADOR', 'Administrador del sistema', TRUE
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE nombre_rol = 'ADMINISTRADOR');

SELECT '✅ ROLES VERIFICADOS/CREADOS' as '';
SELECT * FROM roles;

-- ============================================================
-- 2. OBTENER IDS DE ROLES
-- ============================================================

SET @rol_usuario_id = (SELECT id_rol FROM roles WHERE nombre_rol = 'USUARIO' LIMIT 1);
SET @rol_anfitrion_id = (SELECT id_rol FROM roles WHERE nombre_rol = 'ANFITRION' LIMIT 1);

SELECT @rol_usuario_id as 'ID Rol Usuario', @rol_anfitrion_id as 'ID Rol Anfitrión';

-- ============================================================
-- 3. LIMPIAR DATOS DE PRUEBA EXISTENTES
-- ============================================================

-- Primero comentarios (dependen de reservas)
DELETE FROM comentarios WHERE id_usuario IN (1, 2);

-- Luego reservas (dependen de usuarios y alojamientos)
DELETE FROM reservas WHERE id_usuario IN (1, 2);

-- Luego imágenes (dependen de alojamientos)
DELETE FROM imagenes_alojamiento WHERE id_alojamiento IN (
    SELECT id_alojamiento FROM (SELECT id_alojamiento FROM alojamientos WHERE id_anfitrion = 1) AS temp
);

-- Luego alojamientos (dependen de usuarios)
DELETE FROM alojamientos WHERE id_anfitrion = 1;

-- Finalmente usuarios
DELETE FROM usuarios WHERE id_usuario IN (1, 2);

SELECT 'DATOS ANTIGUOS ELIMINADOS' as '';

-- ============================================================
-- 4. INSERTAR USUARIOS DE PRUEBA
-- ============================================================

-- Usuario Anfitrión (ID=1)
INSERT INTO usuarios (
    id_usuario, nombre, apellido, email, contrasenia_hash, 
    telefono, fecha_nacimiento, foto_perfil, 
    id_rol, estado, fecha_registro
) VALUES (
    1,
    'María',
    'García',
    'maria.anfitrion@example.com',
    '$2a$10$rYJPZvXqXx5YvXqXx5YvXeJpXqXx5YvXeJpXqXx5YvXe',
    '3001234567',
    '1985-05-15',
    'https://i.pravatar.cc/300?img=1',
    @rol_anfitrion_id,
    'ACTIVO',
    NOW()
);

-- Usuario Normal (ID=2)
INSERT INTO usuarios (
    id_usuario, nombre, apellido, email, contrasenia_hash, 
    telefono, fecha_nacimiento, foto_perfil, 
    id_rol, estado, fecha_registro
) VALUES (
    2,
    'Juan',
    'Pérez',
    'juan.usuario@example.com',
    '$2a$10$rYJPZvXqXx5YvXqXx5YvXeJpXqXx5YvXeJpXqXx5YvXe',
    '3109876543',
    '1990-03-20',
    'https://i.pravatar.cc/300?img=5',
    @rol_usuario_id,
    'ACTIVO',
    NOW()
);

SELECT 'USUARIOS CREADOS' as '';
SELECT id_usuario, nombre, apellido, email, id_rol FROM usuarios WHERE id_usuario IN (1, 2);

-- ============================================================
-- 5. INSERTAR ALOJAMIENTOS
-- ============================================================

-- Alojamiento 1: Casa Campestre en Salento
INSERT INTO alojamientos (
    titulo, descripcion, ciudad, direccion_completa, 
    precio_por_noche, capacidad_maxima, tipo, servicios, 
    estado, id_anfitrion
) VALUES (
    'Casa Campestre en Salento',
    'Hermosa casa campestre con vista panorámica al Valle del Cocora. Ideal para familias que buscan tranquilidad y contacto con la naturaleza.',
    'Salento',
    'Vereda Alto del Águila, Km 2',
    250000.00,
    6,
    'CASA',
    '["WiFi","Parqueadero","Cocina","Chimenea","Vista panorámica"]',
    'ACTIVO',
    1
);

-- Alojamiento 2: Apartamento Moderno Centro Armenia
INSERT INTO alojamientos (
    titulo, descripcion, ciudad, direccion_completa, 
    precio_por_noche, capacidad_maxima, tipo, servicios, 
    estado, id_anfitrion
) VALUES (
    'Apartamento Moderno Centro',
    'Apartamento completamente amoblado en el corazón de Armenia. Cerca de restaurantes, centros comerciales y atracciones turísticas.',
    'Armenia',
    'Carrera 14 #15-30, Centro',
    180000.00,
    4,
    'APARTAMENTO',
    '["WiFi","Aire acondicionado","TV Cable","Cocina equipada","Parqueadero"]',
    'ACTIVO',
    1
);

-- Alojamiento 3: Finca Cafetera con Piscina
INSERT INTO alojamientos (
    titulo, descripcion, ciudad, direccion_completa, 
    precio_por_noche, capacidad_maxima, tipo, servicios, 
    estado, id_anfitrion
) VALUES (
    'Finca Cafetera con Piscina',
    'Disfruta de una auténtica experiencia cafetera. Finca con cultivos de café, piscina privada y actividades de agroturismo.',
    'Circasia',
    'Vereda La Bella, Finca El Cafetal',
    400000.00,
    10,
    'FINCA',
    '["Piscina","WiFi","Parqueadero","Tour de café","Cocina","BBQ","Zona de camping"]',
    'ACTIVO',
    1
);

-- Alojamiento 4: Casa Familiar con Jardín
INSERT INTO alojamientos (
    titulo, descripcion, ciudad, direccion_completa, 
    precio_por_noche, capacidad_maxima, tipo, servicios, 
    estado, id_anfitrion
) VALUES (
    'Casa Familiar con Jardín',
    'Amplia casa familiar con jardín privado, perfecta para reuniones. Zona tranquila y segura.',
    'Calarcá',
    'Barrio La Inmaculada, Calle 10 #8-45',
    280000.00,
    8,
    'CASA',
    '["WiFi","Parqueadero","Jardín","BBQ","Cocina completa","Juegos infantiles"]',
    'ACTIVO',
    1
);

-- Alojamiento 5: Loft Urbano Moderno
INSERT INTO alojamientos (
    titulo, descripcion, ciudad, direccion_completa, 
    precio_por_noche, capacidad_maxima, tipo, servicios, 
    estado, id_anfitrion
) VALUES (
    'Loft Urbano Moderno',
    'Loft de diseño contemporáneo con todas las comodidades. Ideal para profesionales y viajeros de negocios.',
    'Armenia',
    'Avenida Bolívar #25-60, Piso 8',
    200000.00,
    2,
    'APARTAMENTO',
    '["WiFi de alta velocidad","Aire acondicionado","Smart TV","Cocina","Gym","Coworking"]',
    'ACTIVO',
    1
);

-- Alojamiento 6: Villa Campestre Premium
INSERT INTO alojamientos (
    titulo, descripcion, ciudad, direccion_completa, 
    precio_por_noche, capacidad_maxima, tipo, servicios, 
    estado, id_anfitrion
) VALUES (
    'Villa Campestre Premium',
    'Villa de lujo con piscina climatizada, cancha múltiple y amplios jardines. Perfecta para eventos.',
    'Montenegro',
    'Vereda El Caimo, Km 3',
    500000.00,
    12,
    'CASA',
    '["Piscina climatizada","Cancha deportiva","WiFi","BBQ","Cocina gourmet","Salón de eventos"]',
    'ACTIVO',
    1
);

-- Alojamiento 7: Penthouse Vista Panorámica
INSERT INTO alojamientos (
    titulo, descripcion, ciudad, direccion_completa, 
    precio_por_noche, capacidad_maxima, tipo, servicios, 
    estado, id_anfitrion
) VALUES (
    'Penthouse Vista Panorámica',
    'Exclusivo penthouse con vista 360° de la ciudad. Terraza privada con jacuzzi.',
    'Armenia',
    'Torre Empresarial, Piso 15',
    350000.00,
    4,
    'APARTAMENTO',
    '["Jacuzzi en terraza","WiFi","Aire acondicionado","Gym","Parqueadero privado","Seguridad 24/7"]',
    'ACTIVO',
    1
);

-- Alojamiento 8: Cabaña Romántica Bosque
INSERT INTO alojamientos (
    titulo, descripcion, ciudad, direccion_completa, 
    precio_por_noche, capacidad_maxima, tipo, servicios, 
    estado, id_anfitrion
) VALUES (
    'Cabaña Romántica Bosque',
    'Cabaña acogedora perfecta para parejas. Rodeada de naturaleza con chimenea y jacuzzi privado.',
    'Filandia',
    'Vereda Cruces, Km 5',
    320000.00,
    2,
    'CASA',
    '["Jacuzzi","Chimenea","WiFi","Desayuno incluido","Vista al bosque"]',
    'ACTIVO',
    1
);

SELECT '✅ ALOJAMIENTOS CREADOS' as '';
SELECT id_alojamiento, titulo, ciudad, precio_por_noche FROM alojamientos WHERE id_anfitrion = 1;

-- ============================================================
-- 6. INSERTAR IMÁGENES DE ALOJAMIENTOS
-- ============================================================

-- Imágenes Alojamiento 1
INSERT INTO imagenes_alojamiento (id_alojamiento, url_imagen, es_principal, orden) VALUES
(1, 'https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?w=800', TRUE, 1),
(1, 'https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=800', FALSE, 2);

-- Imágenes Alojamiento 2
INSERT INTO imagenes_alojamiento (id_alojamiento, url_imagen, es_principal, orden) VALUES
(2, 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=800', TRUE, 1),
(2, 'https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=800', FALSE, 2);

-- Imágenes Alojamiento 3
INSERT INTO imagenes_alojamiento (id_alojamiento, url_imagen, es_principal, orden) VALUES
(3, 'https://images.unsplash.com/photo-1564013799919-ab600027ffc6?w=800', TRUE, 1),
(3, 'https://images.unsplash.com/photo-1600566753190-17f0baa2a6c3?w=800', FALSE, 2);

-- Imágenes Alojamiento 4
INSERT INTO imagenes_alojamiento (id_alojamiento, url_imagen, es_principal, orden) VALUES
(4, 'https://images.unsplash.com/photo-1580587771525-78b9dba3b914?w=800', TRUE, 1),
(4, 'https://images.unsplash.com/photo-1600585154526-990dced4db0d?w=800', FALSE, 2);

-- Imágenes Alojamiento 5
INSERT INTO imagenes_alojamiento (id_alojamiento, url_imagen, es_principal, orden) VALUES
(5, 'https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=800', TRUE, 1),
(5, 'https://images.unsplash.com/photo-1567767292278-a4f21aa2d36e?w=800', FALSE, 2);

-- Imágenes Alojamiento 6
INSERT INTO imagenes_alojamiento (id_alojamiento, url_imagen, es_principal, orden) VALUES
(6, 'https://images.unsplash.com/photo-1600047509807-ba8f99d2cdde?w=800', TRUE, 1),
(6, 'https://images.unsplash.com/photo-1600607687644-c7171b42498f?w=800', FALSE, 2);

-- Imágenes Alojamiento 7
INSERT INTO imagenes_alojamiento (id_alojamiento, url_imagen, es_principal, orden) VALUES
(7, 'https://images.unsplash.com/photo-1600607687920-4e2a09cf159d?w=800', TRUE, 1),
(7, 'https://images.unsplash.com/photo-1512917774080-9991f1c4c750?w=800', FALSE, 2);

-- Imágenes Alojamiento 8
INSERT INTO imagenes_alojamiento (id_alojamiento, url_imagen, es_principal, orden) VALUES
(8, 'https://images.unsplash.com/photo-1449158743715-0a90ebb6d2d8?w=800', TRUE, 1),
(8, 'https://images.unsplash.com/photo-1542718610-a1d656d1884c?w=800', FALSE, 2);

SELECT '✅ IMÁGENES CREADAS' as '';
SELECT id_alojamiento, COUNT(*) as total, SUM(es_principal) as principales
FROM imagenes_alojamiento
WHERE id_alojamiento BETWEEN 1 AND 8
GROUP BY id_alojamiento;

-- ============================================================
-- 7. INSERTAR RESERVAS
-- ============================================================

-- Reserva 1 (Completada - para comentario)
INSERT INTO reservas (
    id_usuario, id_alojamiento, fecha_check_in, fecha_check_out, 
    numero_huespedes, precio_total, estado, fecha_reserva
) VALUES (
    2, 1, '2025-11-16', '2025-11-20', 4, 1000000.00, 'COMPLETADA', '2025-11-01'
);

-- Reserva 2 (Completada - para comentario)
INSERT INTO reservas (
    id_usuario, id_alojamiento, fecha_check_in, fecha_check_out, 
    numero_huespedes, precio_total, estado, fecha_reserva
) VALUES (
    2, 2, '2025-11-21', '2025-11-23', 2, 360000.00, 'COMPLETADA', '2025-11-05'
);

-- Reserva 3 (Completada - para comentario)
INSERT INTO reservas (
    id_usuario, id_alojamiento, fecha_check_in, fecha_check_out, 
    numero_huespedes, precio_total, estado, fecha_reserva
) VALUES (
    2, 3, '2025-11-24', '2025-11-26', 8, 800000.00, 'COMPLETADA', '2025-11-10'
);

-- Reserva 4 (Confirmada - futura)
INSERT INTO reservas (
    id_usuario, id_alojamiento, fecha_check_in, fecha_check_out, 
    numero_huespedes, precio_total, estado, fecha_reserva
) VALUES (
    2, 4, '2025-12-20', '2025-12-22', 6, 560000.00, 'CONFIRMADA', '2025-11-30'
);

-- Reserva 5 (Confirmada - futura)
INSERT INTO reservas (
    id_usuario, id_alojamiento, fecha_check_in, fecha_check_out, 
    numero_huespedes, precio_total, estado, fecha_reserva
) VALUES (
    2, 5, '2025-12-27', '2025-12-29', 2, 400000.00, 'CONFIRMADA', '2025-12-05'
);

SELECT '✅ RESERVAS CREADAS' as '';
SELECT id_reserva, id_alojamiento, estado, precio_total FROM reservas;

-- ============================================================
-- 8. INSERTAR COMENTARIOS
-- ============================================================

-- Comentario 1 (Reserva completada)
INSERT INTO comentarios (
    id_reserva, id_usuario, id_alojamiento, 
    calificacion, comentario
) VALUES (
    1, 2, 1, 5, 'Excelente lugar, muy tranquilo y con hermosas vistas.'
);

-- Comentario 2 (Reserva completada)
INSERT INTO comentarios (
    id_reserva, id_usuario, id_alojamiento, 
    calificacion, comentario
) VALUES (
    2, 2, 2, 4, 'Muy bien ubicado y limpio. Perfecto para estadías cortas.'
);

-- Comentario 3 (Reserva completada)
INSERT INTO comentarios (
    id_reserva, id_usuario, id_alojamiento, 
    calificacion, comentario
) VALUES (
    3, 2, 3, 5, 'Una experiencia increíble. El tour de café fue fascinante.'
);

SELECT '✅ COMENTARIOS CREADOS' as '';
SELECT id_alojamiento, COUNT(*) as total, ROUND(AVG(calificacion), 1) as promedio
FROM comentarios
GROUP BY id_alojamiento;

-- ============================================================
-- 9. VERIFICACIÓN FINAL
-- ============================================================

SELECT '========================' as '';
SELECT '   VERIFICACIÓN FINAL   ' as '';
SELECT '========================' as '';

-- Roles
SELECT '📋 ROLES:' as '';
SELECT id_rol, nombre_rol, descripcion FROM roles;

-- Usuarios
SELECT '👥 USUARIOS:' as '';
SELECT id_usuario, nombre, apellido, email, id_rol FROM usuarios WHERE id_usuario IN (1, 2);

-- Alojamientos
SELECT '🏠 ALOJAMIENTOS:' as '';
SELECT id_alojamiento, titulo, ciudad, precio_por_noche, estado 
FROM alojamientos 
WHERE id_anfitrion = 1
ORDER BY id_alojamiento;

-- Imágenes
SELECT '🖼️ IMÁGENES:' as '';
SELECT id_alojamiento, COUNT(*) as total, SUM(es_principal) as principales
FROM imagenes_alojamiento
WHERE id_alojamiento BETWEEN 1 AND 8
GROUP BY id_alojamiento;

-- Calificaciones
SELECT '⭐ CALIFICACIONES:' as '';
SELECT 
    a.titulo,
    COUNT(c.id_comentario) as comentarios,
    ROUND(AVG(c.calificacion), 1) as promedio
FROM alojamientos a
LEFT JOIN comentarios c ON a.id_alojamiento = c.id_alojamiento
WHERE a.id_anfitrion = 1
GROUP BY a.id_alojamiento, a.titulo
ORDER BY promedio DESC;

-- Reservas
SELECT '📅 RESERVAS:' as '';
SELECT COUNT(*) as total, 
       SUM(CASE WHEN estado = 'COMPLETADA' THEN 1 ELSE 0 END) as completadas
FROM reservas;

-- Resumen
SELECT '========================' as '';
SELECT 
    (SELECT COUNT(*) FROM alojamientos WHERE estado = 'ACTIVO' AND id_anfitrion = 1) as 'Alojamientos',
    (SELECT COUNT(*) FROM imagenes_alojamiento WHERE id_alojamiento BETWEEN 1 AND 8) as 'Imágenes',
    (SELECT COUNT(*) FROM comentarios) as 'Comentarios',
    (SELECT COUNT(*) FROM reservas WHERE estado = 'COMPLETADA') as 'Reservas',
    (SELECT COUNT(*) FROM usuarios WHERE id_usuario IN (1,2)) as 'Usuarios';

-- ============================================================
-- REACTIVAR MODO SEGURO Y VERIFICACIONES
-- ============================================================
SET FOREIGN_KEY_CHECKS = 1;
SET SQL_SAFE_UPDATES = 1;

SELECT '✅ SCRIPT COMPLETADO EXITOSAMENTE' as '';
