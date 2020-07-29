package controllers

import play.api.http.Status
import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneAppPerSuite

class ApplicationSpec extends PlaySpec with GuiceOneAppPerSuite {

  lazy val applicationController = app.injector.instanceOf[Application]

  "Application.license" must {
    "return MIT for twbs/bootstrap" in {
      val result = applicationController.license("twbs", "bootstrap", "main")(FakeRequest())

      status(result) must be (Status.OK)
      contentAsString(result) must equal ("MIT")
    }
    "return not found for webjars/webjars" in {
      val result = applicationController.license("webjars", "jquery", "main")(FakeRequest())
      status(result) must be (Status.NOT_FOUND)
    }
  }

}
