package example

import java.util.concurrent.ExecutionException
import org.gfccollective.guava.future.FutureConverters._
import com.google.common.util.concurrent.SettableFuture
import scala.concurrent.Future
import org.scalatest.concurrent.ScalaFutures._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class GuavaFutureTest extends AnyFlatSpec with Matchers {

  "Guava ListenableFuture conversion" should "work" in {
    val guavaFuture = SettableFuture.create[String]
    guavaFuture.set("Hello world")

    val future = guavaFuture.asScala
    future.isCompleted should be (true)
    future.futureValue should be ("Hello world")
    future.asListenableFuture.get should be ("Hello world")

    val failedFuture = Future.failed[String](new IllegalStateException("OMG WTF"))
    failedFuture.isCompleted should be (true)

    val failedGuavaFuture = failedFuture.asListenableFuture
    failedGuavaFuture.isDone should be (true)
    val cause = intercept[ExecutionException] { failedGuavaFuture.get }.getCause
    cause.isInstanceOf[IllegalStateException] should be (true)
    cause.getMessage should be ("OMG WTF")
  }

}
