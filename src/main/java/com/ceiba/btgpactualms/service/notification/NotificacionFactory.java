package com.ceiba.btgpactualms.service.notification;

import com.ceiba.btgpactualms.model.CanalNotificacion;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotificacionFactory {

    private final EmailNotificacionService emailService;
    private final SmsNotificacionService smsService;

    public NotificacionFactory(
            EmailNotificacionService emailService,
            SmsNotificacionService smsService
    ) {
        this.emailService = emailService;
        this.smsService = smsService;
    }

    public NotificacionService obtener(CanalNotificacion tipo) {

        return switch (tipo) {
            case EMAIL -> emailService;
            case SMS -> smsService;
        };
    }
}
