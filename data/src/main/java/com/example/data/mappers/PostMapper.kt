package com.example.data.mappers

import com.example.data.helpers.Constants
import com.example.data.helpers.getAuthorPostName
import com.example.data.helpers.getFormatDate
import com.example.data.helpers.getMaxSizeUrl
import com.example.domain.models.api.Attachment
import com.example.domain.models.api.Photo
import com.example.domain.models.db.PostLocal
import com.example.domain.models.api.Post
import com.example.domain.models.api.Video
import com.example.domain.models.db.AttachmentLocal
import kotlin.collections.ArrayList

fun mapPostToPostLocal(post: Post): PostLocal {
    return with(post) {
        PostLocal(
            postId,
            text,
            getFormatDate(date),
            getAuthorPostName(),
            attachments?.map { mapAttachToAttachLocal(it) }
        )
    }
}

fun mapAttachToString(attachment: Attachment): String {
    attachment.type.let {
        var result = "$it:"
        if (it == Constants.ATTACHMENTS_PHOTO_TYPE)
            result += attachment.photo.getMaxSizeUrl()
        else if (it == Constants.ATTACHMENTS_VIDEO_TYPE)
            result += attachment.video.url
        return result
    }
}

fun mapAttachToAttachLocal(attachment: Attachment): AttachmentLocal {
    return with(attachment) {
        AttachmentLocal(
            type,
            mapAttachToUrl(attachment)
        )
    }
}

fun mapAttachToUrl(attachment: Attachment): String {
    if (attachment.type == Constants.ATTACHMENTS_PHOTO_TYPE)
        return attachment.photo.getMaxSizeUrl()
    else if (attachment.type == Constants.ATTACHMENTS_VIDEO_TYPE)
        return attachment.video.url
    return ""
}

fun mapPhotoToList(photo: Photo?): List<String> {
    return if (photo == null)
        emptyList()
    else
        ArrayList<String>().apply { add(photo.getMaxSizeUrl()) }
}

fun mapVideoToList(video: Video?): List<String> {
    return if (video == null)
        emptyList()
    else
        ArrayList<String>().apply { add(video.url) }
}
