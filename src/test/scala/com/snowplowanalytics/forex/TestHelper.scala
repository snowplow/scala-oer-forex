/* 
 * Copyright (c) 2013-2014 Snowplow Analytics Ltd. All rights reserved.
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

import org.joda.money._
/**
 * make all tests share one forex object
 */
object TestHelper {

  val key = sys.env("OER_KEY") // Warning: this will give nasty errors if env var not exported
  val conf = new OerClientConfig(key, false) // with default base currency USD
  val fx = new Forex(new ForexConfig(), conf) // forex object with USD as base currency
  val confWithBaseGBP = new OerClientConfig(key, true) // set base currency to GBP
  val fxWithBaseGBP = new Forex(new ForexConfig(baseCurrency = CurrencyUnit.GBP), confWithBaseGBP) // forex object with GBP as base currency
}
