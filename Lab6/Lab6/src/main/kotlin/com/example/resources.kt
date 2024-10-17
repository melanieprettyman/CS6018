package com.example

import io.ktor.server.resources.*
import io.ktor.server.resources.Resources
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.delete
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction


fun Application.configureResources() {
    install(Resources)
    routing {
        // Fetch all posts from the database and responds with a list of serialized GetPostData objects.
        get<Posts> {
            call.respond(
                newSuspendedTransaction(Dispatchers.IO) {
                    Post
                        .selectAll()
                        .map { GetPostData(it[Post.content], it[Post.time], it[Post.id].value) }
                }
            )
        }
        //Fetch all posts where the time field is greater than or equal to the specified timestamp
        get<Posts.GetTimeStamp> {
            call.respond(
                newSuspendedTransaction(Dispatchers.IO) {
                    Post
                        .select(Post.time greaterEq it.time)
                        .map { GetPostData(it[Post.content], it[Post.time], it[Post.id].value) }
                }
            )
        }
        //Fetch a specific post by its ID and returns its details.
        get<Posts.GetPostId> {
            call.respond(
                newSuspendedTransaction(Dispatchers.IO) {
                    Post
                        .select( Post.id eq it.id)
                        .map {GetPostData(it[Post.content], it[Post.time], it[Post.id].value) }
                }
            )
        }
        //Receives a PostData object from the request body, inserts a new post into the database with the current
        // timestamp, and responds with a confirmation
        post<Posts> {
            val contentInput = call.receive<PostData>()
            newSuspendedTransaction(Dispatchers.IO, DBConfig.db) {
                Post.insert {
                    it[content] = contentInput.text
                    it[time] = System.currentTimeMillis()
                } get Post.id
            }
            call.respondText("Posted: $contentInput")
        }
        // Returns a success or failure message depending on whether the post was found and deleted.
        delete<Posts.Delete> {
            val postId = it.id
            val rowsDeleted = newSuspendedTransaction(Dispatchers.IO) {
                Post.deleteWhere { Post.id eq postId }
            }

            if (rowsDeleted > 0) {
                call.respondText("Post with ID $postId has been deleted")
            } else {
                call.respondText("No post found with ID $postId")
            }

        }
    }
}


@Resource("/posts") // base route for all endpoints related to posts
class Posts {

    @Resource("{time}/time") //corresponds to /posts/{time}/time
    class GetTimeStamp(val parent: Posts = Posts(), val time: Long)


    @Resource("{id}/id") //corresponds to /posts/{id}/id
    class GetPostId(val parent: Posts = Posts(), val id: Int)

    @Resource("{id}/delete") // /posts/{id}/delete
    class Delete(val parent: Posts = Posts(), val id: Int)

}

@Serializable
data class GetPostData(val post: String, val time: Long, val ID: Int)

@Serializable
data class PostData(val text: String)