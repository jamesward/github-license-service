package utils

import org.apache.commons.lang3.StringUtils
import play.api.{Play, Application}

import scala.io.{Codec, Source}
import scala.util.Try

class LicenseUtil(implicit app: Application) {

  def detect(contents: String): Option[String] = {

    // only pick licenses that are around the same length as the input
    val possibleLicensesBasedOnLength = licenses.filter {
      case (name, licenseText) =>
        val high = Math.max(licenseText.length, contents.length)
        val low = Math.min(licenseText.length, contents.length)
        val distance = (high - low) / low
        distance < 2
    }

    // get the Levenshtein Distance for each license
    val licensesWithScores = possibleLicensesBasedOnLength.mapValues { licenseTemplate =>
      (licenseTemplate, StringUtils.getLevenshteinDistance(licenseTemplate, contents))
    }

    // pick the lowest scoring license if there are any
    Try {
      val (lowestScoringLicense, _) = licensesWithScores.minBy {
        case (name, (licenseTemplate, score)) => score
      }
      lowestScoringLicense
    }.toOption
  }

  type MaybeLicense = Option[String]

  implicit def stringToLicenseText(value: String): MaybeLicense = {
    Play.resource("licenses/" + value).flatMap { url =>
      Try(Source.fromURL(url)(Codec.ISO8859).mkString).toOption
    }
  }

  // originally from: https://bintray.com/docs/api/#_footnote_1
  // removed licenses not found in: http://git.spdx.org/?p=license-list.git;a=tree
  val allLicenses: Map[String, MaybeLicense] = Map(
    "AFL-3.0" -> "AFL-3.0.txt",
    "AGPL-V3" -> "AGPL-3.0.txt",
    "Apache-1.0" -> "Apache-1.0.txt",
    "Apache-1.1" -> "Apache-1.1.txt",
    "Apache-2.0" -> "Apache-2.0.txt",
    "APL-1.0" -> "APL-1.0.txt",
    "APSL-2.0" -> "APSL-2.0.txt",
    "Artistic-License-2.0" -> "Artistic-2.0.txt",
    "BSD 2-Clause" -> "BSD-2-Clause.txt",
    "BSD 3-Clause" -> "BSD-3-Clause.txt",
    "BSL-1.0" -> "BSL-1.0.txt",
    "CA-TOSL-1.1" -> "CATOSL-1.1.txt",
    "CC0-1.0" -> "CC0-1.0.txt",
    "CDDL-1.0" -> "CDDL-1.0.txt",
    "CPAL-1.0" -> "CPAL-1.0.txt",
    "CPL-1.0" -> "CPL-1.0.txt",
    "CPOL-1.02" -> "CPOL-1.02.txt",
    "CUAOFFICE-1.0" -> "CUA-OPL-1.0.txt",
    "ECL2" -> "ECL-2.0.txt",
    "Entessa-1.0" -> "Entessa.txt",
    "EPL-1.0" -> "EPL-1.0.txt",
    "EUDATAGRID" -> "EUDatagrid.txt",
    "EUPL-1.1" -> "EUPL-1.1.txt",
    "Fair" -> "Fair.txt",
    "Frameworx-1.0" -> "Frameworx-1.0.txt",
    "GPL-2.0" -> "GPL-2.0.txt",
    "GPL-3.0" -> "GPL-3.0.txt",
    "IBMPL-1.0" -> "IBM-pibs.txt",
    "ISC" -> "ISC.txt",
    "JSON" -> "JSON.txt",
    "LGPL-2.1" -> "LGPL-2.1.txt",
    "LGPL-3.0" -> "LGPL-3.0.txt",
    "Lucent-1.02" -> "LPL-1.02.txt",
    "MirOS" -> "MirOS.txt",
    "MIT" -> "MIT.txt",
    "Motosoto-0.9.1" -> "Motosoto.txt",
    "Mozilla-1.1" -> "MPL-1.1.txt",
    "MPL-2.0" -> "MPL-2.0.txt",
    "MS-PL" -> "MS-PL.txt",
    "MS-RL" -> "MS-RL.txt",
    "Multics" -> "Multics.txt",
    "NASA-1.3" -> "NASA-1.3.txt",
    "NAUMEN" -> "Naumen.txt",
    "Nethack" -> "NGPL.txt",
    "Nokia-1.0a" -> "Nokia.txt",
    "NOSL-3.0" -> "NOSL.txt",
    "NTP" -> "NTP.txt",
    "NUnit-2.6.3" -> "Nunit.txt",
    "OCLC-2.0" -> "OCLC-2.0.txt",
    "Openfont-1.1" -> "OFL-1.1.txt",
    "Opengroup" -> "OGTSL.txt",
    "OpenSSL" -> "OpenSSL.txt",
    "OSL-3.0" -> "OSL-3.0.txt",
    "PHP-3.0" -> "PHP-3.0.txt",
    "PostgreSQL" -> "PostgreSQL.txt",
    "RPL-1.5" -> "RPL-1.5.txt",
    "SimPL-2.0" -> "SimPL-2.0.txt",
    "Sleepycat" -> "Sleepycat.txt",
    "TMate" -> "TMate.txt",
    "Unlicense" -> "Unlicense.txt",
    "W3C" -> "W3C.txt",
    "WTFPL" -> "WTFPL.txt",
    // "Xnet" -> "Xnet.txt", // too many false positives with MIT
    "ZPL-2.0" -> "ZPL-2.0.txt"
  )

  val licenses: Map[String, String] = allLicenses.filter(_._2.isDefined).map { case (name, maybeLicense) =>
    name -> maybeLicense.get
  }
}

object LicenseUtil {
  def apply(implicit app: Application): LicenseUtil = new LicenseUtil()
}
