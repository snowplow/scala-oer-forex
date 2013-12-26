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

import org.joda.money.CurrencyUnit

sealed trait EodRounding
object EodRoundDown extends EodRounding
object EodRoundUp extends EodRounding

/**
 * Configure class for Forex
 *
 * @pvalue appId - key for the api
 * @pvalue nowishCacheSize -  cache for nowish look up
 * @pvalue nowishSecs - time range for nowish look up
 * @pvalue historicalCacheSize -  cache for historical lookup
 * @pvalue getNearestDay - flag for deciding whether to get the exchange rate on closer day or previous day
 * @pvalue baseCurrency  - base currency does not have default value 
 */
case class ForexConfig(
  appId: String,
  nowishCacheSize: Int         = 13530, // TODO show calc briefly
  nowishSecs: Int              = 300,
  historicalCacheSize: Int     = 405900, 
  getNearestDay: EodRounding   = EodRoundDown,
  // there is no default value for base currency
  baseCurrency: Option[CurrencyUnit] = None
) 