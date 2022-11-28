package com.example.doanadroid.model.entity

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todolist.utils.Constants
import kotlin.math.log

fun List<CalendarEntity>.logByName(tag: String = "TAG", screen: String) {
    Log.d(tag, screen + " ${this.map { it.name }}")
}

@Entity(tableName = Constants.CALENDAR_TABLE_NAME)
data class CalendarEntity(
    @PrimaryKey(autoGenerate = true) val id : Int?,
    @ColumnInfo(name="name")val name :String?,
    @ColumnInfo(name = "day")val day :String?,
    @ColumnInfo(name = "time")val time :String?,
    @ColumnInfo(name = "categoty")val category :String?,
    @ColumnInfo(name = "complete")val complete :String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(day)
        parcel.writeString(time)
        parcel.writeString(category)
        parcel.writeString(complete)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CalendarEntity> {
        override fun createFromParcel(parcel: Parcel): CalendarEntity {
            return CalendarEntity(parcel)
        }

        override fun newArray(size: Int): Array<CalendarEntity?> {
            return arrayOfNulls(size)
        }
    }
}
