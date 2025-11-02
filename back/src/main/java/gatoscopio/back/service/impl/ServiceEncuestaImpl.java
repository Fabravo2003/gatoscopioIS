package gatoscopio.back.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gatoscopio.back.dto.EncuestaDetalleResponse;
import gatoscopio.back.dto.RespuestaUpsertRequest;
import gatoscopio.back.model.Encuesta;
import gatoscopio.back.model.Pregunta;
import gatoscopio.back.model.Respuesta;
import gatoscopio.back.model.ValorValidoPregunta;
import gatoscopio.back.repository.EncuestaRepository;
import gatoscopio.back.repository.PreguntaRepository;
import gatoscopio.back.repository.RespuestaRepository;
import gatoscopio.back.repository.ValorValidoPreguntaRepository;
import gatoscopio.back.service.ServiceEncuesta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import gatoscopio.back.repository.UsuarioRepository;

@Service
public class ServiceEncuestaImpl implements ServiceEncuesta {

    @Autowired private EncuestaRepository encuestaRepository;
    @Autowired private RespuestaRepository respuestaRepository;
    @Autowired private PreguntaRepository preguntaRepository;
    @Autowired private ValorValidoPreguntaRepository valorValidoRepository;
    @PersistenceContext private EntityManager em;
    @Autowired private UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public EncuestaDetalleResponse getDetalle(Integer encuestaId) {
        Encuesta e = encuestaRepository.findById(encuestaId)
                .orElseThrow(() -> new java.util.NoSuchElementException("encuesta no existe"));
        var resp = new EncuestaDetalleResponse();
        resp.setId(e.getId());
        resp.setEstado(e.getEstado());
        resp.setPacienteCodigo(e.getPacienteCodigo());
        resp.setCreatedAt(e.getCreatedAt());
        var respuestas = new ArrayList<EncuestaDetalleResponse.ItemRespuesta>();
        for (Respuesta r : respuestaRepository.findByEncuestaId(encuestaId)) {
            EncuestaDetalleResponse.ItemRespuesta ir = new EncuestaDetalleResponse.ItemRespuesta(r.getPreguntaId(), r.getValor());
            preguntaRepository.findById(r.getPreguntaId()).ifPresent(p -> {
                ir.setNombreVariable(p.getNombreVariable());
                ir.setEnunciado(p.getEnunciado());
                ir.setTipoDato(p.getTipoDato());
                var vals = valorValidoRepository.findByPreguntaId(p.getId()).stream().map(ValorValidoPregunta::getValor).toList();
                ir.setValoresValidos(vals);
            });
            respuestas.add(ir);
        }
        resp.setRespuestas(respuestas);
        // ultimo editor (si existe)
        var rows = em.createNativeQuery(
                "select u.id, u.nombre, u.correo, ue.fecha_hora_modificacion " +
                "from usuarios_encuestas ue join usuarios u on u.id = ue.usuario_id " +
                "where ue.encuesta_id = :eid order by ue.fecha_hora_modificacion desc limit 1")
                .setParameter("eid", encuestaId)
                .getResultList();
        if (!rows.isEmpty()) {
            Object[] r = (Object[]) rows.get(0);
            var ed = new EncuestaDetalleResponse.UltimoEditor();
            if (r[0] != null) ed.setId(((Number) r[0]).intValue());
            ed.setNombre((String) r[1]);
            ed.setCorreo((String) r[2]);
            if (r[3] instanceof Timestamp ts) {
                ed.setFechaHoraModificacion(ts.toLocalDateTime());
            } else if (r[3] instanceof java.time.LocalDateTime ldt) {
                ed.setFechaHoraModificacion(ldt);
            }
            resp.setUltimoEditor(ed);
        }
        return resp;
    }

    @Override
    @Transactional
    public int upsertRespuestas(Integer encuestaId, List<RespuestaUpsertRequest> items, Integer userId) {
        if (items == null || items.isEmpty()) throw new IllegalArgumentException("lista de respuestas requerida");
        Encuesta e = encuestaRepository.findById(encuestaId)
                .orElseThrow(() -> new java.util.NoSuchElementException("encuesta no existe"));
        if (e.getEstado() != null && "Completo".equalsIgnoreCase(e.getEstado())) {
            throw new IllegalStateException("encuesta completa no se puede editar");
        }

        int updated = 0;
        if (userId != null && !usuarioRepository.existsById(userId)) {
            throw new java.util.NoSuchElementException("usuario no existe");
        }
        for (RespuestaUpsertRequest it : items) {
            Integer pid = it.getPreguntaId();
            String valor = it.getValor();
            if (pid == null) throw new IllegalArgumentException("preguntaId requerido");
            Pregunta p = preguntaRepository.findById(pid)
                    .orElseThrow(() -> new java.util.NoSuchElementException("pregunta no existe: " + pid));

            // Validar valor con banco de válidos (si existen)
            List<ValorValidoPregunta> validos = valorValidoRepository.findByPreguntaId(pid);
            if (!validos.isEmpty()) {
                boolean ok = validos.stream().map(ValorValidoPregunta::getValor).anyMatch(v -> v.equalsIgnoreCase(valor));
                if (!ok) throw new IllegalArgumentException("valor inválido para la pregunta: " + pid);
            }
            // Validación simple por tipo_dato
            if (p.getTipoDato() != null) {
                String t = p.getTipoDato().toLowerCase();
                switch (t) {
                    case "numero":
                        try { new BigDecimal(valor); } catch (Exception ex) { throw new IllegalArgumentException("valor no numérico"); }
                        break;
                    case "fecha":
                        try { LocalDate.parse(valor); } catch (Exception ex) { throw new IllegalArgumentException("fecha inválida (ISO yyyy-MM-dd)"); }
                        break;
                    case "bool":
                    case "boolean":
                        Set<String> allowed = Set.of("true","false","si","no","0","1");
                        if (!allowed.contains(valor.toLowerCase())) throw new IllegalArgumentException("boolean inválido");
                        break;
                    default: // texto u otros
                        break;
                }
            }

            Respuesta r = respuestaRepository.findByEncuestaIdAndPreguntaId(encuestaId, pid).orElse(null);
            if (r == null) {
                r = new Respuesta();
                r.setEncuestaId(encuestaId);
                r.setPreguntaId(pid);
            }
            r.setValor(valor);
            respuestaRepository.save(r);
            updated++;
        }
        // Auditoría opcional
        if (userId != null) touchAudit(encuestaId, userId);
        return updated;
    }

    @Override
    @Transactional
    public void updateEstado(Integer encuestaId, String estado, Integer userId) {
        if (estado == null || estado.isBlank()) throw new IllegalArgumentException("estado requerido");
        if (userId != null && !usuarioRepository.existsById(userId)) {
            throw new java.util.NoSuchElementException("usuario no existe");
        }
        Encuesta e = encuestaRepository.findById(encuestaId)
                .orElseThrow(() -> new java.util.NoSuchElementException("encuesta no existe"));
        String nuevo = canonicalizeEstado(estado);
        if (nuevo == null) {
            throw new IllegalArgumentException("estado inválido (permitidos: Incompleto, Completo)");
        }
        // Transiciones permitidas: Incompleto <-> Completo (también null -> Incompleto)
        String actual = e.getEstado();
        if (actual != null) {
            String act = canonicalizeEstado(actual);
            if (act == null) act = actual; // por si hay datos antiguos
            boolean permitido = (act.equals("Incompleto") && nuevo.equals("Completo"))
                    || (act.equals("Completo") && nuevo.equals("Incompleto"))
                    || act.equals(nuevo); // idempotente
            if (!permitido) {
                throw new IllegalStateException("transición de estado no permitida: " + act + " -> " + nuevo);
            }
        } else {
            // si no hay estado previo, sólo permitimos establecer a Incompleto
            if (!"Incompleto".equals(nuevo)) {
                throw new IllegalStateException("transición de estado no permitida: null -> " + nuevo);
            }
        }
        e.setEstado(nuevo);
        encuestaRepository.save(e);
        if (userId != null) touchAudit(encuestaId, userId);
    }

    private void touchAudit(Integer encuestaId, Integer userId) {
        em.createNativeQuery("insert into usuarios_encuestas(usuario_id, encuesta_id, fecha_hora_modificacion) values (:uid,:eid, now()) on conflict (usuario_id, encuesta_id) do update set fecha_hora_modificacion = excluded.fecha_hora_modificacion")
                .setParameter("uid", userId)
                .setParameter("eid", encuestaId)
                .executeUpdate();
    }

    @Override
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<Encuesta> list(String estado, String pacienteCodigo, org.springframework.data.domain.Pageable pageable) {
        return encuestaRepository.findAllFiltered(estado, pacienteCodigo, pageable);
    }

    private String canonicalizeEstado(String estado) {
        if (estado == null) return null;
        String s = estado.trim();
        if (s.equalsIgnoreCase("Incompleto")) return "Incompleto";
        if (s.equalsIgnoreCase("Completo")) return "Completo";
        return null;
    }
}
