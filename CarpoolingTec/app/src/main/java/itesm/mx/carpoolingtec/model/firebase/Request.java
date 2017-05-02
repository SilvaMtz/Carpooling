package itesm.mx.carpoolingtec.model.firebase;

import java.util.HashMap;
import java.util.Map;

public class Request {

    private User solicitante;
    private boolean aceptado;
    private boolean leida;

    public Request() {
    }

    public Request(User solicitante, boolean aceptado, boolean leida) {
        this.solicitante = solicitante;
        this.aceptado = aceptado;
        this.leida = leida;
    }

    public User getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(User solicitante) {
        this.solicitante = solicitante;
    }

    public boolean isAceptado() {
        return aceptado;
    }

    public void setAceptado(boolean aceptado) {
        this.aceptado = aceptado;
    }

    public boolean isLeida() {
        return leida;
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("aceptado", aceptado);
        map.put("leida", leida);

        Map<String, Object> userMap = solicitante.toMap();
        map.put("solicitante", userMap);
        return map;
    }
}
