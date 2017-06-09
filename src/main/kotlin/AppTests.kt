
import org.hamcrest.CoreMatchers.equalTo
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters
import org.junit.Assert.assertThat
import org.junit.Test

class AppTests {
    @Test fun `test echo`() {
        val request = Request(GET, "/echo/hello")
        val response = DebuggingFilters.PrintRequestAndResponse().then(app).invoke(request)
        assertThat(response.body.toString(), equalTo("hello"))
    }

    @Test fun `repeat message`() {
        val request = Request(GET, "/repeat/3/http4k")
        val response = DebuggingFilters.PrintRequestAndResponse().then(app).invoke(request)
        assertThat(response.body.toString(), equalTo("http4k\nhttp4k\nhttp4k"))
    }
}