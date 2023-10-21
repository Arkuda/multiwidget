package com.kiryantsev.multiwidget.core.calendar.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "calendar_event_entity")
data class CalendarEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val startDate: Long,
    val endDate: Long,
    val color: Int,
    val recurDate: String?,
    val recurRule: String?,
) {


    fun startDateLocal() =
        Instant.fromEpochMilliseconds(startDate).toLocalDateTime(TimeZone.currentSystemDefault())

    fun endDateLocal() =
        Instant.fromEpochMilliseconds(endDate).toLocalDateTime(TimeZone.currentSystemDefault())

    fun isMultiDayEvent(): Boolean = endDate - startDate > 86400 * 1000

    fun fixToNormalItem(): CalendarEventEntity {

        var resultItem = this

        resultItem = resultItem.fixRecur()

        if (resultItem.endDate == 0L) {
            // this periodic allday event, need fix
            val startDate = resultItem.startDateLocal()
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

            val newDate = LocalDate(now.date.year, startDate.month, startDate.dayOfMonth)
            val newStartDate = LocalDateTime(
                newDate,
                LocalTime(0, 0, 0)
            ).toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
            val newEndDate =
                LocalDateTime(
                    newDate,
                    LocalTime(23, 59, 59)
                ).toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

            resultItem.copy(
                startDate = newStartDate,
                endDate = newEndDate
            )
        }

        return resultItem
    }

    private fun fixRecur(): CalendarEventEntity {
        //recurRule=FREQ=DAILY;UNTIL=20230324T040000Z;INTERVAL=4;WKST=MO
        //recurRule=FREQ=YEARLY;WKST=MO
        //recurRule=FREQ=MONTHLY;WKST=MO;BYMONTHDAY=23

        if (recurRule == null) {
            return this
        }

        val rules = recurRule.split(";")
        val freq = rules.findValue("FREQ")
        val interval = rules.findValue("INTERVAL")
        val wkst = rules.findValue("WKST") //weekstart
        val byMonthDay = rules.findValue("BYMONTHDAY")
        val until = rules.findValue("UNTIL")

        if(freq == "DAILY"){
            // как то досчитываем интервалы
            return this //TODO fix
        }else if(freq == "WEEKLY"){
            return this //TODO fix
        }else if(freq == "MONTHLY"){
            //берем день месяца, вкарячиваем
            return this //TODO fix
        }else if(freq == "YEARLY"){
            val startDate = startDateLocal()
            val endDate = endDateLocal()
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

            val newStartDate = LocalDateTime(
                LocalDate(now.date.year, startDate.month, startDate.dayOfMonth),
                startDate.time,
            ).toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
            val newEndDate =
                LocalDateTime(
                    LocalDate(now.date.year, endDate.month, endDate.dayOfMonth),
                    endDate .time
                ).toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

            return copy(
                startDate =  newStartDate,
                endDate = newEndDate,
            )
        }else {
            return this
        }
    }
}

private fun List<String>.findValue(key: String): String? {
    return find { it.startsWith(key, true) }?.split("=")?.last()
}
