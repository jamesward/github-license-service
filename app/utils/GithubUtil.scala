package utils

import play.api.Application
import play.api.http.Status
import play.api.libs.ws.WS

import scala.concurrent.{ExecutionContext, Future}

class GithubUtil(implicit app: Application, ec: ExecutionContext) {

  def license(org: String, repo: String, version: String): Future[String] = {
    Future.find(Seq("LICENSE", "LICENSE.md", "LICENSE.txt").map(file(org, repo, version, _)))(_.isDefined).flatMap { a =>
      a.flatten.fold(Future.failed[String](new Exception("No license found")))(Future.successful)
    }
  }

  def file(org: String, repo: String, version: String, path: String): Future[Option[String]] = {
    WS.url(s"https://raw.githubusercontent.com/$org/$repo/$version/$path").get().map { response =>
      response.status match {
        case Status.OK =>
          Some(response.body)
        case _ =>
          None
      }
    }
  }

}

object GithubUtil {
  def apply(implicit app: Application, ec: ExecutionContext) = new GithubUtil()
}