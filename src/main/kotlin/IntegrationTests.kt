
import org.hamcrest.CoreMatchers.equalTo
import org.http4k.client.ApacheClient
import org.http4k.core.*
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.junit.Assert.assertThat
import org.junit.Test

class IntegrationTests {
    private val filter = object : Filter {
        override fun invoke(delegateHandler: HttpHandler) = object : HttpHandler {
            override fun invoke(request: Request): Response {
                val updatedRequest = request.header("Referer", "http://127.0.0.1:1234")
                return delegateHandler(updatedRequest)
            }
        }
    }

    @Test fun `test echo`() {
        val server = app.asServer(Jetty(1234)).start()

        val httpClient = filter.then(ApacheClient())
        val response = httpClient(Request(Method.GET, "http://localhost:8000/1/run"))

        assertThat(response.status, equalTo(Status.OK))
        assertThat(response.body.toString(), equalTo(""))

        server.stop()
    }
}