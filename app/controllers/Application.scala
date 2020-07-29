package controllers

import javax.inject.Inject

import play.api.mvc._
import utils.{GithubUtil, LicenseUtil}

import scala.concurrent.ExecutionContext


class Application @Inject() (githubUtil: GithubUtil, licenseUtil: LicenseUtil) (implicit executionContext: ExecutionContext) extends InjectedController {

  def licenseWithDefaultBranch(org: String, repo: String) = Action.async {
    githubUtil.defaultBranch(org, repo).map { defaultBranch =>
      Redirect(routes.Application.license(org, repo, defaultBranch))
    }
  }

  def license(org: String, repo: String, version: String) = Action.async {
    githubUtil.license(org, repo, version).flatMap { licenseText =>
      licenseUtil.detect(licenseText).map { maybeLicense =>
        maybeLicense.fold(NotFound("License Not Detected"))(Ok(_))
      }
    } recover {
      case e: Exception =>
        NotFound("License Not Found")
    }
  }

}
