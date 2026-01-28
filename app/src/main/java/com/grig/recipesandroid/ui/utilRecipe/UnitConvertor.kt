package com.grig.recipesandroid.ui.utilRecipe

import com.grig.recipesandroid.data.model.dto.UnitDto

class UnitConvertor {

    /**
     * Конвертирует количество в граммы
     * @param amount количество
     * @param unit единица измерения
     * @param nameEng английское название ингредиента (для PCS)
     * @param name русское название ингредиента (для PCS)
     * @return Double? граммы или null, если не удалось конвертировать
     */
//    fun toGram(ammount: Double, unit: UnitDto, nameEng): Double? =
    fun toGram(ammount: Double, unit: UnitDto, name: String): Double? =
        when (unit.code) {
            "G" -> ammount
            "KG" -> ammount * 1000
            "MG" -> ammount / 1000
            "PCS" -> {
                println("UnitCOnvert toGram: START ${name}")
                convertPcsGram(ammount, name)
            }
//            "PCS" -> convertPcsGram(ammount, nameEng)
//            "PCS" -> null       //  яйца, штуки — калории считать нельзя
            "ML" -> ammount // условно, если жидкость ≈ вода
            else -> null
        }

    //    Средний вес популярных продуктов
    private val pcsToGramMap: Map<String, Double> = mapOf(
        "яйцо" to 50.0,          // яйцо
        "яблоко" to 150.0,       // яблоко
        "банан" to 120.0,      // банан
        "апельсин" to 130.0,       // апельсин

        // Овощи
        "помидор" to 120.0,      // средний томат
        "огурец" to 100.0,       // среднеплодный
        "картофелина" to 150.0,  // средний клубень
        "луковица" to 80.0,      // средняя репчатая
        "морковь" to 100.0,      // средняя
        "зубчик чеснока" to 5.0,
        "перец болгарский" to 200.0,
        "баклажан" to 250.0,
        "кабачок" to 300.0,      // небольшой молодой

        // Фрукты и ягоды
        "лимон" to 100.0,
        "груша" to 180.0,
        "киви" to 70.0,
        "авокадо" to 200.0,      // без косточки ~160, но обычно считают плод целиком
        "персик" to 150.0,

        // Другое
        "ломтик хлеба" to 30.0,
        "куриное филе" to 230.0, // одна половинка грудки
        "картошка" to 150.0
    )
//    private val pcsToGramMapEng: Map<String, Double> = mapOf(
//        "egg" to 50.0,          // яйцо
//        "apple" to 150.0,       // яблоко
//        "banana" to 120.0,      // банан
//        "orange" to 130.0       // апельсин
//    )

    /**
     * Конвертирует штуки в граммы, если известен средний вес
     */
    private fun convertPcsGram(amount: Double, name: String?): Double? {
        println("UnitCOnvert convertPcsGram: START ${name}")
        if (name == null) return null
        val avgGram = pcsToGramMap[name.lowercase()]
        println("UnitCOnvert convertPcsGram: START ${name}, avgGram=$avgGram")
        return avgGram?.let { it * amount }
    }
//    private fun convertPcsGram(amount: Double, nameEng: String?): Double? {
//        if (nameEng == null) return null
//        val avgGram = pcsToGramMapEng[nameEng.lowercase()]
//        return avgGram?.let { it * amount }
//    }
}