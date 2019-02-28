/*
 * Copyright (c) 2013-2018 Snowplow Analytics Ltd. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package com.snowplowanalytics.forex

import java.math.RoundingMode

import cats.effect.IO
import org.joda.money._
import org.specs2.mutable.Specification

/** Testing method for getting the live exchange rate */
class ForexNowSpec extends Specification {
  args(skipAll = sys.env.get("OER_KEY").isEmpty)

  val key = sys.env.getOrElse("OER_KEY", "")
  val fx  = Forex.getForex[IO](ForexConfig(key, DeveloperAccount))
  val fxWithBaseGBP =
    Forex.getForex[IO](ForexConfig(key, EnterpriseAccount, baseCurrency = CurrencyUnit.GBP))

  /** Trade 10000 USD to JPY at live exchange rate */
  "convert 10000 USD dollars to Yen now" should {
    "be > 10000" in {
      val tradeInYenNow = fx.flatMap(_.convert(10000).to(CurrencyUnit.JPY).now)
      tradeInYenNow
        .unsafeRunSync() must beRight(
        (m: Money) => m.isGreaterThan(Money.of(CurrencyUnit.JPY, 10000, RoundingMode.HALF_EVEN)))
    }
  }

  /** GBP -> SGD with USD as base currency */
  "GBP to SGD with base currency USD live exchange rate" should {
    "be greater than 1 SGD" in {
      val gbpToSgdWithBaseUsd = fx.flatMap(_.rate(CurrencyUnit.GBP).to(CurrencyUnit.of("SGD")).now)
      gbpToSgdWithBaseUsd
        .unsafeRunSync() must beRight((m: Money) => m.isGreaterThan(Money.of(CurrencyUnit.of("SGD"), 1)))
    }
  }

  /** GBP -> SGD with GBP as base currency */
  "GBP to SGD with base currency GBP live exchange rate" should {
    "be greater than 1 SGD" in {
      val gbpToSgdWithBaseGbp = fxWithBaseGBP.flatMap(_.rate.to(CurrencyUnit.of("SGD")).now)
      gbpToSgdWithBaseGbp
        .unsafeRunSync() must beRight((m: Money) => m.isGreaterThan(Money.of(CurrencyUnit.of("SGD"), 1)))
    }
  }

  /** GBP with GBP as base currency */
  "Do not throw JodaTime exception on converting identical currencies" should {
    "be equal 1 GBP" in {
      val gbpToGbpWithBaseGbp = fxWithBaseGBP.flatMap(_.rate.to(CurrencyUnit.GBP).now)
      gbpToGbpWithBaseGbp
        .unsafeRunSync() must beRight((m: Money) => m.isEqual(Money.of(CurrencyUnit.of("GBP"), 1)))
    }
  }
}
