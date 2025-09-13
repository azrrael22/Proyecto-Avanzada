-- ================================================
-- SCRIPT 4: ROLES Y PERMISOS
-- ================================================

USE gestion_alojamientos;

-- ================================================
-- INSERTAR ROLES BÁSICOS
-- ================================================

INSERT INTO roles (nombre_rol, descripcion) VALUES
                                                ('USUARIO', 'Huésped que puede buscar y reservar alojamientos'),
                                                ('ANFITRION', 'Propietario que puede crear y gestionar alojamientos'),
                                                ('ADMINISTRADOR', 'Administrador del sistema con acceso completo');

-- ================================================
-- INSERTAR PERMISOS DEL SISTEMA
-- ================================================

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

-- PERMISOS PARA ROL ADMINISTRADOR (ID: 3) - Todos los permisos
INSERT INTO roles_permisos (id_rol, id_permiso)
SELECT 3, id_permiso FROM permisos;