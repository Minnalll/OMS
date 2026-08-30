package com.oms.service;

import com.oms.dto.OrderCreatedEvent;

public interface NotificationService {

    void saveNotification(OrderCreatedEvent event);
}
