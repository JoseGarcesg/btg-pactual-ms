package com.ceiba.btgpactualms.service.notification;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotificacionFactory {

    private final Map<String, NotificacionService> notificacionService;

    public NotificacionFactory(Map<String, NotificacionService> notificacionService) {
        this.notificacionService = notificacionService;
    }

    public NotificacionService obtener(String tipo) {
        return notificacionService.get(tipo);
    }
}
