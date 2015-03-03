package controllers

import org.scalatest._
import play.api.http.Status
import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play._

class ApplicationSpec extends PlaySpec with OneAppPerSuite {

  "Application.license" must {
    "return MIT for twbs/bootstrap" in {
      val result = controllers.Application.license("twbs", "bootstrap")(FakeRequest())

      status(result) must be (Status.OK)
      contentAsString(result) must equal ("MIT")
    }
    "return not found for webjars/webjars" in {
      val result = controllers.Application.license("webjars", "webjars")(FakeRequest())
      status(result) must be (Status.NOT_FOUND)
    }
  }

}