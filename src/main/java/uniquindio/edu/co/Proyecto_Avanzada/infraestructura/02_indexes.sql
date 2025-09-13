-- ================================================
-- SCRIPT 2: ÍNDICES PARA OPTIMIZACIÓN
-- ================================================

USE gestion_alojamientos;

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
