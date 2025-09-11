
package uniquindio.edu.co.Proyecto_Avanzada.aplicacion.controller.admin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Administración", description = "Operaciones exclusivas para administradores del sistema")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminController {


    @GetMapping("/dashboard")
    @Operation(summary = "Dashboard de administración",
            description = "Panel principal con estadísticas generales del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboard obtenido exitosamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario no es administrador")
    })
    public ResponseEntity<Map<String, Object>> dashboard() {
        Map<String, Object> estadisticasGenerales = new HashMap<>();
        estadisticasGenerales.put("totalUsuarios", 1250);
        estadisticasGenerales.put("usuariosActivos", 1100);
        estadisticasGenerales.put("totalAnfitriones", 300);
        estadisticasGenerales.put("totalAlojamientos", 850);
        estadisticasGenerales.put("alojamientosActivos", 750);
        estadisticasGenerales.put("reservasEsteMes", 420);
        estadisticasGenerales.put("ingresosTotales", 125000000);

        Map<String, Object> actividadReciente = new HashMap<>();
        actividadReciente.put("nuevosUsuariosHoy", 15);
        actividadReciente.put("reservasHoy", 32);
        actividadReciente.put("comentariosHoy", 18);
        actividadReciente.put("reportesPendientes", 5);

        Map<String, Object> crecimientoMensual = new HashMap<>();
        crecimientoMensual.put("usuarios", 8.5);
        crecimientoMensual.put("alojamientos", 12.3);
        crecimientoMensual.put("reservas", 15.7);
        crecimientoMensual.put("ingresos", 18.2);

        Map<String, Object> response = new HashMap<>();
        response.put("estadisticasGenerales", estadisticasGenerales);
        response.put("actividadReciente", actividadReciente);
        response.put("crecimientoMensual", crecimientoMensual);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuarios")
    @Operation(summary = "Gestionar usuarios",
            description = "HU-AD001: Lista, filtro y búsqueda de usuarios del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario no es administrador")
    })
    public ResponseEntity<Map<String, Object>> gestionarUsuarios(
            @Parameter(description = "Filtro por rol: USUARIO, ANFITRION, ADMINISTRADOR")
            @RequestParam(required = false) String rol,

            @Parameter(description = "Filtro por estado: ACTIVO, INACTIVO")
            @RequestParam(required = false) String estado,

            @Parameter(description = "Búsqueda por nombre, email o ID")
            @RequestParam(required = false) String busqueda,

            @Parameter(description = "Página (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Tamaño de página", example = "20")
            @RequestParam(defaultValue = "20") int size
    ) {
        Map<String, Object> usuario1 = new HashMap<>();
        usuario1.put("id", 1);
        usuario1.put("nombre", "Juan Pérez");
        usuario1.put("email", "juan.perez@email.com");
        usuario1.put("telefono", "+57300123456");
        usuario1.put("rol", "USUARIO");
        usuario1.put("estado", "ACTIVO");
        usuario1.put("fechaRegistro", "2023-01-15");
        usuario1.put("ultimoAcceso", "2024-01-20T10:30:00");
        usuario1.put("totalReservas", 8);
        usuario1.put("calificacionPromedio", 4.2);

        Map<String, Object> usuario2 = new HashMap<>();
        usuario2.put("id", 2);
        usuario2.put("nombre", "María García");
        usuario2.put("email", "maria.garcia@email.com");
        usuario2.put("telefono", "+57310987654");
        usuario2.put("rol", "ANFITRION");
        usuario2.put("estado", "ACTIVO");
        usuario2.put("fechaRegistro", "2023-02-10");
        usuario2.put("ultimoAcceso", "2024-01-19T15:45:00");
        usuario2.put("totalAlojamientos", 3);
        usuario2.put("totalReservasRecibidas", 25);
        usuario2.put("calificacionPromedio", 4.5);

        Map<String, Object> usuario3 = new HashMap<>();
        usuario3.put("id", 3);
        usuario3.put("nombre", "Carlos Suspendido");
        usuario3.put("email", "carlos@email.com");
        usuario3.put("telefono", "+57320555666");
        usuario3.put("rol", "USUARIO");
        usuario3.put("estado", "INACTIVO");
        usuario3.put("fechaRegistro", "2023-03-05");
        usuario3.put("ultimoAcceso", "2024-01-01T08:00:00");
        usuario3.put("motivoSuspension", "Comportamiento inapropiado");
        usuario3.put("fechaSuspension", "2024-01-05");

        Map<String, Object> filtros = new HashMap<>();
        filtros.put("rol", rol);
        filtros.put("estado", estado);
        filtros.put("busqueda", busqueda);

        Map<String, Object> resumen = new HashMap<>();
        resumen.put("totalUsuarios", 1250);
        resumen.put("activos", 1100);
        resumen.put("inactivos", 150);
        resumen.put("usuarios", 950);
        resumen.put("anfitriones", 280);
        resumen.put("administradores", 20);

        Map<String, Object> paginacion = new HashMap<>();
        paginacion.put("page", page);
        paginacion.put("size", size);
        paginacion.put("totalElementos", 1250);
        paginacion.put("totalPaginas", 63);

        Map<String, Object> response = new HashMap<>();
        response.put("usuarios", List.of(usuario1, usuario2, usuario3));
        response.put("filtros", filtros);
        response.put("resumen", resumen);
        response.put("paginacion", paginacion);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuarios/{id}")
    @Operation(summary = "Ver detalles de usuario específico",
            description = "Información completa y historial de actividad de un usuario")
    public ResponseEntity<Map<String, Object>> verDetalleUsuario(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id
    ) {
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("id", id);
        usuario.put("nombre", "Juan Pérez");
        usuario.put("email", "juan.perez@email.com");
        usuario.put("telefono", "+57300123456");
        usuario.put("fechaNacimiento", "1990-05-15");
        usuario.put("rol", "USUARIO");
        usuario.put("estado", "ACTIVO");
        usuario.put("fechaRegistro", "2023-01-15T10:30:00");
        usuario.put("ultimoAcceso", "2024-01-20T10:30:00");
        usuario.put("ipUltimoAcceso", "192.168.1.100");

        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("totalReservas", 8);
        estadisticas.put("reservasCompletadas", 6);
        estadisticas.put("reservasCanceladas", 2);
        estadisticas.put("comentariosRealizados", 5);
        estadisticas.put("calificacionPromedio", 4.2);
        estadisticas.put("totalGastado", 1250000);

        Map<String, Object> actividad1 = new HashMap<>();
        actividad1.put("fecha", "2024-01-20T10:30:00");
        actividad1.put("accion", "LOGIN");
        actividad1.put("descripcion", "Inicio de sesión exitoso");
        actividad1.put("ip", "192.168.1.100");

        Map<String, Object> actividad2 = new HashMap<>();
        actividad2.put("fecha", "2024-01-19T15:45:00");
        actividad2.put("accion", "RESERVA_CREADA");
        actividad2.put("descripcion", "Reserva creada para Casa Campestre");
        actividad2.put("detalles", Map.of("reservaId", 15, "alojamientoId", 3));

        Map<String, Object> actividad3 = new HashMap<>();
        actividad3.put("fecha", "2024-01-18T09:20:00");
        actividad3.put("accion", "COMENTARIO_CREADO");
        actividad3.put("descripcion", "Comentario y calificación dejados");
        actividad3.put("detalles", Map.of("comentarioId", 25, "calificacion", 5));

        Map<String, Object> response = new HashMap<>();
        response.put("usuario", usuario);
        response.put("estadisticas", estadisticas);
        response.put("historialActividad", List.of(actividad1, actividad2, actividad3));

        return ResponseEntity.ok(response);
    }

    @PutMapping("/usuarios/{id}/estado")
    @Operation(summary = "Activar/Desactivar usuario",
            description = "HU-AD001: Cambiar estado de cuenta de usuario con justificación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "400", description = "Justificación requerida para desactivación"),
            @ApiResponse(responseCode = "409", description = "No se puede desactivar a otro administrador")
    })
    public ResponseEntity<Map<String, Object>> cambiarEstadoUsuario(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id,

            @Parameter(description = "Nuevo estado: ACTIVO, INACTIVO", required = true)
            @RequestParam String estado,

            @Parameter(description = "Justificación del cambio (obligatoria para desactivación)", required = true)
            @RequestParam String justificacion
    ) {
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("id", id);
        usuario.put("nombre", "Juan Pérez");
        usuario.put("email", "juan.perez@email.com");
        usuario.put("estadoAnterior", "ACTIVO");
        usuario.put("nuevoEstado", estado);

        Map<String, Object> auditoria = new HashMap<>();
        auditoria.put("administrador", "Admin Carlos");
        auditoria.put("fecha", java.time.LocalDateTime.now().toString());
        auditoria.put("accion", "CAMBIO_ESTADO");
        auditoria.put("justificacion", justificacion);
        auditoria.put("valorAnterior", "ACTIVO");
        auditoria.put("valorNuevo", estado);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Estado de usuario actualizado exitosamente");
        response.put("usuario", usuario);
        response.put("auditoria", auditoria);

        if ("INACTIVO".equals(estado)) {
            response.put("efectos", List.of(
                    "El usuario no podrá iniciar sesión",
                    "Sus reservas activas se mantienen",
                    "No podrá crear nuevas reservas",
                    "Se envía notificación por email"
            ));
        } else {
            response.put("efectos", List.of(
                    "El usuario puede iniciar sesión nuevamente",
                    "Puede crear nuevas reservas",
                    "Se envía notificación de reactivación"
            ));
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/usuarios/{id}/rol")
    @Operation(summary = "Cambiar rol de usuario",
            description = "HU-AD001: Cambiar rol entre USUARIO y ANFITRION con aprobación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "400", description = "Rol inválido o cambio no permitido"),
            @ApiResponse(responseCode = "409", description = "No se puede cambiar rol de administrador")
    })
    public ResponseEntity<Map<String, Object>> cambiarRolUsuario(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id,

            @Parameter(description = "Nuevo rol: USUARIO, ANFITRION", required = true)
            @RequestParam String nuevoRol,

            @Parameter(description = "Motivo del cambio de rol", required = true)
            @RequestParam String motivo
    ) {
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("id", id);
        usuario.put("nombre", "Juan Pérez");
        usuario.put("email", "juan.perez@email.com");
        usuario.put("rolAnterior", "USUARIO");
        usuario.put("nuevoRol", nuevoRol);

        Map<String, Object> auditoria = new HashMap<>();
        auditoria.put("administrador", "Admin Carlos");
        auditoria.put("fecha", java.time.LocalDateTime.now().toString());
        auditoria.put("accion", "CAMBIO_ROL");
        auditoria.put("justificacion", motivo);
        auditoria.put("valorAnterior", "USUARIO");
        auditoria.put("valorNuevo", nuevoRol);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Rol de usuario actualizado exitosamente");
        response.put("usuario", usuario);
        response.put("auditoria", auditoria);

        if ("ANFITRION".equals(nuevoRol)) {
            response.put("nuevasCapacidades", List.of(
                    "Puede crear y gestionar alojamientos",
                    "Puede recibir reservas",
                    "Acceso a métricas de anfitrión",
                    "Puede responder comentarios"
            ));
        } else {
            response.put("restricciones", List.of(
                    "Ya no puede crear alojamientos",
                    "Sus alojamientos actuales se mantienen",
                    "Mantiene acceso a reservas existentes"
            ));
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/auditoria")
    @Operation(summary = "Ver auditoría del sistema",
            description = "Historial completo de acciones administrativas realizadas")
    public ResponseEntity<Map<String, Object>> verAuditoria(
            @Parameter(description = "Filtro por administrador")
            @RequestParam(required = false) Long administradorId,

            @Parameter(description = "Filtro por acción: ACTIVAR, DESACTIVAR, CAMBIO_ROL")
            @RequestParam(required = false) String accion,

            @Parameter(description = "Fecha desde (YYYY-MM-DD)")
            @RequestParam(required = false) String fechaDesde,

            @Parameter(description = "Fecha hasta (YYYY-MM-DD)")
            @RequestParam(required = false) String fechaHasta,

            @Parameter(description = "Página (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Tamaño de página", example = "20")
            @RequestParam(defaultValue = "20") int size
    ) {
        Map<String, Object> registro1 = new HashMap<>();
        registro1.put("id", 1);
        registro1.put("fecha", "2024-01-20T14:30:00");
        registro1.put("administrador", "Admin Carlos");
        registro1.put("accion", "DESACTIVAR");
        registro1.put("usuarioAfectado", "Juan Problemático");
        registro1.put("valorAnterior", "ACTIVO");
        registro1.put("valorNuevo", "INACTIVO");
        registro1.put("justificacion", "Múltiples reportes de comportamiento inadecuado");

        Map<String, Object> registro2 = new HashMap<>();
        registro2.put("id", 2);
        registro2.put("fecha", "2024-01-19T10:15:00");
        registro2.put("administrador", "Admin María");
        registro2.put("accion", "CAMBIO_ROL");
        registro2.put("usuarioAfectado", "Pedro Nuevo Anfitrión");
        registro2.put("valorAnterior", "USUARIO");
        registro2.put("valorNuevo", "ANFITRION");
        registro2.put("justificacion", "Solicitud aprobada tras verificación de documentación");

        Map<String, Object> registro3 = new HashMap<>();
        registro3.put("id", 3);
        registro3.put("fecha", "2024-01-18T16:45:00");
        registro3.put("administrador", "Admin Carlos");
        registro3.put("accion", "ACTIVAR");
        registro3.put("usuarioAfectado", "Ana Rehabilitada");
        registro3.put("valorAnterior", "INACTIVO");
        registro3.put("valorNuevo", "ACTIVO");
        registro3.put("justificacion", "Apelación exitosa, comportamiento corregido");

        Map<String, Object> filtros = new HashMap<>();
        filtros.put("administradorId", administradorId);
        filtros.put("accion", accion);
        filtros.put("fechaDesde", fechaDesde);
        filtros.put("fechaHasta", fechaHasta);

        Map<String, Object> resumen = new HashMap<>();
        resumen.put("totalRegistros", 156);
        resumen.put("activaciones", 45);
        resumen.put("desactivaciones", 28);
        resumen.put("cambiosRol", 83);

        Map<String, Object> paginacion = new HashMap<>();
        paginacion.put("page", page);
        paginacion.put("size", size);
        paginacion.put("totalElementos", 156);
        paginacion.put("totalPaginas", 8);

        Map<String, Object> response = new HashMap<>();
        response.put("registros", List.of(registro1, registro2, registro3));
        response.put("filtros", filtros);
        response.put("resumen", resumen);
        response.put("paginacion", paginacion);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/estadisticas")
    @Operation(summary = "Estadísticas del sistema",
            description = "Estadísticas detalladas de usuarios, alojamientos, reservas y rendimiento")
    public ResponseEntity<Map<String, Object>> verEstadisticas(
            @Parameter(description = "Período: dia, semana, mes, año", example = "mes")
            @RequestParam(defaultValue = "mes") String periodo
    ) {
        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("totalRegistrados", 1250);
        usuarios.put("activosUltimoMes", 850);
        usuarios.put("nuevosEstePerrodo", 125);
        usuarios.put("churnRate", 2.3);
        usuarios.put("distribucionRoles", Map.of(
                "USUARIO", 950,
                "ANFITRION", 280,
                "ADMINISTRADOR", 20
        ));

        Map<String, Object> alojamientos = new HashMap<>();
        alojamientos.put("totalCreados", 850);
        alojamientos.put("activos", 750);
        alojamientos.put("nuevosEstePerrodo", 85);
        alojamientos.put("ocupacionPromedio", 68.5);
        alojamientos.put("distribucionTipos", Map.of(
                "CASA", 420,
                "APARTAMENTO", 380,
                "FINCA", 50
        ));

        Map<String, Object> reservas = new HashMap<>();
        reservas.put("totalRealizadas", 3450);
        reservas.put("completadasExitosamente", 2890);
        reservas.put("canceladas", 560);
        reservas.put("tasaCompletacion", 83.8);
        reservas.put("ingresosTotales", 425000000);
        reservas.put("ingresoPromedioPorReserva", 125000);

        Map<String, Object> rendimiento = new HashMap<>();
        rendimiento.put("tiempoPromedioRespuesta", "125ms");
        rendimiento.put("disponibilidadSistema", 99.8);
        rendimiento.put("satisfaccionUsuarios", 4.3);
        rendimiento.put("reportesProblemas", 12);

        Map<String, Object> response = new HashMap<>();
        response.put("periodo", periodo);
        response.put("fechaGeneracion", java.time.LocalDateTime.now().toString());
        response.put("usuarios", usuarios);
        response.put("alojamientos", alojamientos);
        response.put("reservas", reservas);
        response.put("rendimiento", rendimiento);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/reportes")
    @Operation(summary = "Generar reportes del sistema",
            description = "Reportes exportables en diferentes formatos")
    public ResponseEntity<Map<String, Object>> generarReportes(
            @Parameter(description = "Tipo: usuarios, alojamientos, reservas, ingresos")
            @RequestParam String tipo,

            @Parameter(description = "Formato: PDF, EXCEL, CSV", example = "PDF")
            @RequestParam(defaultValue = "PDF") String formato,

            @Parameter(description = "Fecha desde (YYYY-MM-DD)")
            @RequestParam(required = false) String fechaDesde,

            @Parameter(description = "Fecha hasta (YYYY-MM-DD)")
            @RequestParam(required = false) String fechaHasta
    ) {
        Map<String, Object> reporte = new HashMap<>();
        reporte.put("id", "RPT-" + System.currentTimeMillis());
        reporte.put("tipo", tipo);
        reporte.put("formato", formato);
        reporte.put("fechaGeneracion", java.time.LocalDateTime.now().toString());
        reporte.put("parametros", Map.of(
                "fechaDesde", fechaDesde,
                "fechaHasta", fechaHasta
        ));
        reporte.put("estado", "GENERANDO");
        reporte.put("urlDescarga", "/api/admin/reportes/download/RPT-" + System.currentTimeMillis());
        reporte.put("tiempoEstimado", "2-5 minutos");

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Reporte generándose exitosamente");
        response.put("reporte", reporte);
        response.put("instrucciones", List.of(
                "El reporte se está generando en segundo plano",
                "Recibirás notificación cuando esté listo",
                "Podrás descargarlo desde el enlace proporcionado",
                "El reporte estará disponible por 24 horas"
        ));

        return ResponseEntity.ok(response);
    }
}