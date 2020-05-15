package ru.nsu.merkuriev.waterbalance.domain.model

class NotificationData(
    val notificationType: NotificationType,
    val hour: Int,
    val minute: Int
)