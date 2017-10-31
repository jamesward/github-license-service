package utils

import javax.inject.Inject

import play.api.Environment
import play.api.http.Status
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

class LicenseUtil @Inject() (ws: WSClient, environment: Environment) (implicit ec: ExecutionContext) {

  def detect(contents: String): Future[Option[String]] = {
    ws.url("http://oss-license-detector.herokuapp.com/").post(contents).map { response =>
      response.status match {
        case Status.OK =>
          Some(response.body)
        case _ =>
          None
      }
    }
  }

}
