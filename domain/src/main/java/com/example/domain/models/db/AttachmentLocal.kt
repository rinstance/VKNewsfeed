//package com.example.domain.models.db
//
//import androidx.room.Entity
//import androidx.room.ForeignKey
//import androidx.room.PrimaryKey
//
//@Entity(
//    foreignKeys = [
//        ForeignKey(
//            entity = PostLocal::class,
//            parentColumns = ["id"],
//            childColumns = ["postId"],
//            onDelete = ForeignKey.CASCADE
//        )]
//)
//data class AttachmentLocal(
//    val type: String,
//    val url: String,
//    val postId: Int
//) {
//    @PrimaryKey(autoGenerate = true)
//    var id: Int = 0
//}