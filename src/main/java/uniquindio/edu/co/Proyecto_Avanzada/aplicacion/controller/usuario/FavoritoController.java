package uniquindio.edu.co.Proyecto_Avanzada.aplicacion.controller.usuario;// Ruta: aplicacion/controller/usuario/FavoritoController.java

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Alojamiento.AlojamientoSummaryDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.service.FavoritoService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuario/favoritos")
@RequiredArgsConstructor
@Tag(name = "Favoritos", description = "Gestión de alojamientos favoritos del usuario")
@SecurityRequirement(name = "Bearer Authentication")
public class FavoritoController {

    private final FavoritoService favoritoService;

    @PostMapping("/{alojamientoId}")
    @Operation(summary = "Añadir un alojamiento a favoritos")
    public ResponseEntity<Map<String, String>> agregarFavorito(@PathVariable Long alojamientoId) {
        try {
            Long usuarioId = 1L; // Fijo para probar
            favoritoService.agregarFavorito(usuarioId, alojamientoId);
            return ResponseEntity.ok(Map.of("message", "Alojamiento añadido a favoritos."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Ver mi lista de alojamientos favoritos")
    public ResponseEntity<?> listarFavoritos() {
        try {
            Long usuarioId = 1L; // Fijo para probar
            List<AlojamientoSummaryDTO> favoritos = favoritoService.listarFavoritosPorUsuario(usuarioId);
            return ResponseEntity.ok(favoritos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}