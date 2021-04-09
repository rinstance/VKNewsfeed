//package com.example.domain.models.db
//
//import androidx.room.Embedded
//import androidx.room.Relation
//
//data class PostWithAttachments(
//    @Embedded val post: PostLocal,
//    @Relation(parentColumn = "id", entityColumn = "postId")
//    val attachments: List<AttachmentLocal>
//)