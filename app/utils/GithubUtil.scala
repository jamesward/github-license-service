package utils

import play.api.Application
import play.api.http.Status
import play.api.libs.ws.WS

import scala.concurrent.{ExecutionContext, Future}

class GithubUtil(implicit app: Application, ec: ExecutionContext) {

  def license(org: String, repo: String, version: String): Future[String] = {
    val licenseFutures = Seq("LICENSE", "LICENSE.md", "LICENSE.txt", "license.txt", "licenses.txt", "LICENCE").map(file(org, repo, version, _))
    Future.find(licenseFutures)(!_.isEmpty).flatMap { maybeLicense =>
      maybeLicense.fold(Future.failed[String](new Exception("Could not find LICENSE, LICENSE.md, LICENSE.txt, license.txt, licenses.txt")))(Future.successful)
    } fallbackTo {
      licenseFromReadmeMd(org, repo, version)
    } recoverWith {
      case e: Exception => Future.failed(new Exception("No LICENSE, LICENSE.md, LICENSE.txt, license.txt, licenses.txt or README.md containing a license found"))
    }
  }

  def licenseFromReadmeMd(org: String, repo: String, version: String): Future[String] = {
    file(org, repo, version, "README.md").flatMap { readmemd =>
      val licenseStart = readmemd.lastIndexOf("# License")
      if (licenseStart >= 0) {
        Future.successful(readmemd.substring(licenseStart + 9))
      }
      else {
        Future.failed(new Exception("License was not found in the README.md"))
      }
    }
  }

  def file(org: String, repo: String, version: String, path: String): Future[String] = {
    val url = s"https://raw.githubusercontent.com/$org/$repo/$version/$path"
    WS.url(url).get().flatMap { response =>
      response.status match {
        case Status.OK =>
          Future.successful(response.body)
        case _ =>
          Future.failed(new Exception(s"Could not get $url"))
      }
    }
  }

}

object GithubUtil {
  def apply(implicit app: Application, ec: ExecutionContext) = new GithubUtil()
}
