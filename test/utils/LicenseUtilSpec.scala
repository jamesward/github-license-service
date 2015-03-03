package utils

import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.json.{JsArray, JsString, Json}
import play.api.libs.ws.{WS, WSAuthScheme}
import play.api.test.Helpers._

import scala.concurrent.ExecutionContext.Implicits.global


class LicenseUtilSpec extends PlaySpec with OneAppPerSuite {

  val licenseUtil = LicenseUtil(app)

  "LicenseUtil allLicenses" must {
    "not have a fake license" in {
      licenseUtil.allLicenses.get("ASDF") must be ('empty)
    }
    "define MIT" in {
      licenseUtil.allLicenses.get("MIT").flatten must be ('defined)
    }
    "work with ISO8859" in {
      licenseUtil.allLicenses("CPOL-1.02").get.contains("CPOL") must be (true)
    }
    "define all keys" in {
      licenseUtil.allLicenses.values.exists(_.isEmpty) must be (false)
    }
  }
  "LicenseUtil licenses" must {
    "define MIT" in {
      licenseUtil.licenses.get("MIT") must be ('defined)
    }
  }
  "LicenseUtil detect" must {
    "detect the MIT license from a Twitter license" in {
      val twitterLicense =
        """The MIT License (MIT)
          |
          |Copyright (c) 2011-2015 Twitter, Inc
          |
          |Permission is hereby granted, free of charge, to any person obtaining a copy
          |of this software and associated documentation files (the "Software"), to deal
          |in the Software without restriction, including without limitation the rights
          |to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
          |copies of the Software, and to permit persons to whom the Software is
          |furnished to do so, subject to the following conditions:
          |
          |The above copyright notice and this permission notice shall be included in
          |all copies or substantial portions of the Software.
          |
          |THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
          |IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
          |FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
          |AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
          |LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
          |OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
          |THE SOFTWARE.""".stripMargin
      licenseUtil.detect(twitterLicense) must be (Some("MIT"))
    }
    "detect the MIT license from a jQuery license" in {
      val jqueryLicense =
        """Copyright jQuery Foundation and other contributors, https://jquery.org/
          |
          |This software consists of voluntary contributions made by many
          |individuals. For exact contribution history, see the revision history
          |available at https://github.com/jquery/jquery
          |
          |The following license applies to all parts of this software except as
          |documented below:
          |
          |====
          |
          |Permission is hereby granted, free of charge, to any person obtaining
          |a copy of this software and associated documentation files (the
          |"Software"), to deal in the Software without restriction, including
          |without limitation the rights to use, copy, modify, merge, publish,
          |distribute, sublicense, and/or sell copies of the Software, and to
          |permit persons to whom the Software is furnished to do so, subject to
          |the following conditions:
          |
          |The above copyright notice and this permission notice shall be
          |included in all copies or substantial portions of the Software.
          |
          |THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
          |EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
          |MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
          |NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
          |LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
          |OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
          |WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
          |
          |====
          |
          |All files located in the node_modules and external directories are
          |externally maintained libraries used by this software which have their
          |own licenses; we recommend you read them, as their terms may differ from
          |the terms above.""".stripMargin
      licenseUtil.detect(jqueryLicense) must be (Some("MIT"))
    }
    "not detect a license from a non-license" in {
      licenseUtil.detect("asdf") must be (None)
    }
  }

}