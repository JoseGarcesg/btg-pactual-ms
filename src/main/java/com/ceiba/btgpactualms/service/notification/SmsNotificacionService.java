package com.ceiba.btgpactualms.service.notification;

import org.springframework.stereotype.Service;

@Service("SMS")
public class SmsNotificacionService implements NotificacionService{
    @Override
    public void enviar(String mensaje, String destino) {
        System.out.println("📱 Enviando SMS a " + destino + ": " + mensaje);
    }
}
