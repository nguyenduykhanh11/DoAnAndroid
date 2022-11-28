package com.example.doanadroid.extensions

import com.google.android.material.textfield.TextInputEditText

//   extension toString()
val TextInputEditText.stringVal: String
    get() = this.text.toString()

//   extension toString().trim()
val TextInputEditText.stringWithTrim: String
    get() = this.stringVal.trim()

enum class Category {
    Complete,
    NoYet,
    ListAll,
    Default,
    Individual,
    Shopping,
    Work
}

val Category.CategoryName: String
    get() = when (this) {
        Category.Complete -> "Hoàn thành"
        Category.NoYet -> "Chưa hoàn thành"
        Category.ListAll -> "Danh sách tất cả"
        Category.Default -> "Mặc Định"
        Category.Individual -> "Cá Nhân"
        Category.Shopping -> "Mua Sắm"
        Category.Work -> "Làm Việc"
    }

val xx = Category.Individual.CategoryName

enum class Title {
    complete,
    noYet,
    delete,

}

val Title.TitleName: String
    get() = when (this) {
        Title.complete -> "Hoàn thành nhiệm vụ"
        Title.noYet -> "Huỷ hoàn thành nhiệm vụ"
        Title.delete -> "Xoá nhiệm vụ"
    }

val xxx = Title.complete.TitleName