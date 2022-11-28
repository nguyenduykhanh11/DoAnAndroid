package com.example.doanadroid.model.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "USER_TABLE_NAME")
data class UserEntity (
    @PrimaryKey(autoGenerate = true) val id : Int?,
    @ColumnInfo(name="name")val name :String?,
    @ColumnInfo(name = "phone")val phone :String?,
    @ColumnInfo(name = "address")val address :String?,
    @ColumnInfo(name = "brithday")val brithday :String?,
    @ColumnInfo(name = "email")val email :String?,
    @ColumnInfo(name = "password")val password :String?
    ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
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
        parcel.writeString(phone)
        parcel.writeString(address)
        parcel.writeString(brithday)
        parcel.writeString(email)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserEntity> {
        override fun createFromParcel(parcel: Parcel): UserEntity {
            return UserEntity(parcel)
        }

        override fun newArray(size: Int): Array<UserEntity?> {
            return arrayOfNulls(size)
        }
    }
}