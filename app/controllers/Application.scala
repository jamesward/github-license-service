package controllers

import play.api.Play
import play.api.mvc.Results.EmptyContent
import play.api.mvc._
import utils.{LicenseUtil, GithubUtil}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.{ExecutionContext, Future}


object Application extends Controller {

  lazy val githubUtil = GithubUtil(Play.current, ExecutionContext.global)
  lazy val licenseUtil = LicenseUtil(Play.current)

  def license(org: String, repo: String, version: String) = Action.async {
    githubUtil.license(org, repo, version).map { licenseText =>
      licenseUtil.detect(licenseText).fold(NotFound("License Not Detected"))(Ok(_))
    } recover {
      case e: Exception =>
        NotFound("License Not Found")
    }
  }

}