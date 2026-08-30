package com.oms.service;

import com.oms.dto.OrderCreatedEvent;
import com.oms.enums.NotificationStatus;
import com.oms.enums.NotificationType;
import com.oms.model.Notification;
import com.oms.repository.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements  NotificationService{

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void saveNotification(OrderCreatedEvent event) {

        Notification notification = new Notification();

        notification.setOrderId(event.getOrderId());

        notification.setCustomerName(event.getCustomerName());

        notification.setEmail(event.getCustomerEmail());

        notification.setPhoneNumber(event.getCustomerPhone());

        notification.setTitle("Order Confirmation");

        notification.setMessage(
                "Dear " + event.getCustomerName()
                        + ", your order has been placed successfully."
        );

        notification.setNotificationType(NotificationType.EMAIL);

        notification.setStatus(NotificationStatus.PENDING);

        notificationRepository.save(notification);

        System.out.println("Notification saved successfully.");
    }


}
