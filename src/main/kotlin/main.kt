
import org.http4k.core.*
import org.http4k.core.Status.Companion.OK
import org.http4k.lens.Path
import org.http4k.lens.int
import org.http4k.lens.string
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer

val timesLens = Path.int().of("times")
val messageLens = Path.string().of("message")
val body = Body.string(ContentType.APPLICATION_JSON).toLens()

val app: HttpHandler = routes(
    "/echo/{name}" to Method.GET bind { request ->
        Response(OK).body(request.path("name").toString())
    },
    "/repeat/{times}/{message}" to Method.GET bind { request ->
        val s = 0.until(timesLens(request))
            .map{ messageLens(request) }
            .joinToString("\n")
        Response(OK).with(body of s)
    }
)

val jettyServer = app.asServer(Jetty(9000))

fun main(args: Array<String>) {
    jettyServer.start()
}
