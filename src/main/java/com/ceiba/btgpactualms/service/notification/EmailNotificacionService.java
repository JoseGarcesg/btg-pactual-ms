package com.ceiba.btgpactualms.service.notification;

import org.springframework.stereotype.Service;

@Service("EMAIL")
public class EmailNotificacionService implements NotificacionService{
    @Override
    public void enviar(String mensaje, String destino) {
        System.out.println("📧 Enviando EMAIL a " + destino + ": " + mensaje);
    }
}
