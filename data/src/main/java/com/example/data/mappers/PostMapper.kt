package com.example.data.mappers

import com.example.data.helpers.getAuthorPostName
import com.example.data.helpers.getFormatDate
import com.example.domain.models.api.*
import com.example.domain.models.db.cache.PostAuthorLocal
import com.example.domain.models.db.PostLocal
import com.example.domain.models.db.cache.PostCache

fun mapPostToPostLocal(post: Post, userId: Int): PostLocal {
    return with(post) {
        PostLocal(
            postId,
            userId,
            sourceId,
            text,
            getFormatDate(date),
            getAuthorPostName(),
            postAuthor.photo50
        )
    }
}

fun mapPostToPostCache(post: Post): PostCache {
    return with(post) {
        PostCache(postId, date, likes, sourceId, text, mapToAuthorLocal(this))
    }
}

fun mapPostCacheToPost(postCache: PostCache): Post {
    return with(postCache) {
        Post(null, date, likes, postId, sourceId, text, mapToPostAuthor(sourceId, postAuthor))
    }
}

fun mapToPostAuthor(id: Int, author: PostAuthorLocal): PostAuthor {
    return if (id < 0) with(author) {
        PostAuthor(id, photo50, "", "", name)
    } else with(author) {
        PostAuthor(id, photo50, name.split(" ")[0], name.split(" ")[1], "")
    }
}

fun mapToAuthorLocal(post: Post): PostAuthorLocal {
    return with(post.postAuthor) {
        PostAuthorLocal(
            id,
            photo50,
            post.getAuthorPostName()
        )
    }
}
