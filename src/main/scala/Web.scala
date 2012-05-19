import org.jboss.netty.handler.codec.http._
import com.twitter.finagle.http.path.Path
import org.jboss.netty.handler.codec.http.HttpMethod._
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1
import org.jboss.netty.buffer.ChannelBuffers.copiedBuffer
import org.jboss.netty.util.CharsetUtil.UTF_8
import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.http.{Http, Response}
import com.twitter.finagle.Service
import com.twitter.util.Future
import java.net.InetSocketAddress
import util.Properties
import twitter4j._
import com.twitter.finagle.http.ParamMap

object Web {
  def main(args: Array[String]) {
    val port = Properties.envOrElse("PORT", "8080").toInt
    println("Starting on port:"+port)
    ServerBuilder()
      .codec(Http())
      .name("mururu-server")
      .bindTo(new InetSocketAddress(port))
      .build(new Respond)
  }
}

class Respond extends Service[HttpRequest, HttpResponse] {
  def apply(req: HttpRequest): Future[HttpResponse] = {
    (req.getMethod, req.getUri) match {
      case (GET, "/") => Future.value {
        val response = new DefaultHttpResponse(HTTP_1_1, OK)
          response.setContent(copiedBuffer("<html><head><title>finagle_hack</title></head><body><form method='post' action='/search'><input tyoe='text' name='name'><input type='submit' value='search'></form></body></html>", UTF_8))
        response.addHeader("charset", UTF_8)
        response
      }

      case(POST, "/search") => Future.value {
        val content = req.getContent.toString(UTF_8)
        val twitter = new TwitterFactory().getInstance()
        println(content)
        val re = """(.+)=(.+)""".r
        val t = content match {
          case re(k,v) => (k,v)
          case _       => ("","")
        }
        val name = t._2
        println(name)
        val statuses = twitter.getUserTimeline(name)
        val str = statuses.get(0).getText
        val response = new DefaultHttpResponse(HTTP_1_1, OK)
        response.addHeader("charset",UTF_8)
        response.setContent(copiedBuffer(str, UTF_8))
        response
      }
    }
  }
}
