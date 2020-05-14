package ru.nsu.merkuriev.waterbalance.domain.model

enum class ActiveType(
    val title: String,
    val waterSum: Double
) {
    NO("", 0.0),
    LAZY("Ленивый", 0.3),
    MIDDLE("Средняя активность", 0.9),
    ACTIVE("Активный", 1.5);

    override fun toString(): String {
        return title
    }
}