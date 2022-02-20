package com.ands.sravniruhackathon.data.storage

import com.ands.sravniruhackathon.domain.entities.Coeffs

class UiDataLocalStorageImpl: UiDataLocalStorage {
    override fun getDefaultCoeffsData(): List<Coeffs> {
        return listOf(
                Coeffs("БТ", "БТ", "2 754 - 4 432 Р", "базовый тариф", "Устанавливает страховая компания"),
                Coeffs("КМ", "КМ","0,6 - 1,6", "коэфф. мощности", "Чем мощнее автомобиль, тем дороже сраховой полис"),
                Coeffs("КТ", "КТ", "0,64 - 1,99", "территориальный коэфф.", "Определяется по прописке собственника автомобиля"),
                Coeffs("КБМ", "КБМ", "0,5 - 2,45", "коэфф. безаварифности", "Учитывается только самый высокий коэффициент из всех водителей"),
                Coeffs("КВС", "КВС","0,90 - 1,93", "коэфф. возраст/стаж", "Чем больше возраст и стаж у вписанного в полис водителя, тем дешевле будет полис"),
                Coeffs("КО", "КО", "1 или 1,99", "коэфф. ограничений", "Полис с ограниченным списком водителей будет стоить дешевле")
        )
    }
}