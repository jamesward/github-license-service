package utils

import org.scalatest.concurrent.ScalaFutures
import play.api.test.Helpers._
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import scala.concurrent.ExecutionContext


class GithubUtilSpec extends PlaySpec with OneAppPerSuite with ScalaFutures {

  val githubUtil = GithubUtil(app, ExecutionContext.global)

  "GithubUtil" must {
    "fetch a license from twbs/bootstrap" in {
      val license = await(githubUtil.license("twbs", "bootstrap", "master"))
      license must include ("MIT")
    }
    "fetch a license with a version" in {
      await(githubUtil.license("twbs", "bootstrap", "v3.3.0")) must include ("MIT")
    }
    "fail to fetch a license from webjars/webjars" in {
      intercept[Exception] {
        await(githubUtil.license("webjars", "webjars", "master"))
      }
    }
  }

}