package ru.nsu.merkuriev.waterbalance.domain.model

enum class ActiveType(
    val title: String,
    val waterSum: Float
) {
    NO("", 0f),
    LAZY("Ленивый", 300f),
    MIDDLE("Средняя активность", 600f),
    ACTIVE("Активный", 900f);

    override fun toString(): String {
        return title
    }
}