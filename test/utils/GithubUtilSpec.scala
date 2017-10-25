package utils

import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.Helpers._

class GithubUtilSpec extends PlaySpec with GuiceOneAppPerSuite with ScalaFutures {

  lazy val githubUtil = app.injector.instanceOf[GithubUtil]

  "GithubUtil" must {
    "fetch a license from twbs/bootstrap" in {
      val license = await(githubUtil.license("twbs", "bootstrap", "master"))
      license must include ("MIT")
    }
    "figure out a license from angular/bower-angular" in {
      val license = await(githubUtil.license("angular", "bower-angular", "master"))
      license must include ("MIT")
    }
    "fetch a license with a version" in {
      await(githubUtil.license("twbs", "bootstrap", "v3.3.0")) must include ("MIT")
    }
    "fail to fetch a license from webjars/jquery" in {
      intercept[Exception] {
        await(githubUtil.license("webjars", "jquery", "master"))
      }.getMessage must equal ("No LICENSE, LICENSE.md, LICENSE.txt, license.txt, licenses.txt, License, LICENSE-MIT or README.md containing a license found")
    }
    "fetch a license for n3-charts/line-chart" in {
      await(githubUtil.license("n3-charts", "line-chart", "master")) must include ("MIT")
    }
    "fetch a license for cowboy/javascript-sync-async-foreach" in {
      await(githubUtil.license("cowboy", "javascript-sync-async-foreach", "master")) must include ("MIT")
    }
  }

}
