package com.imyvm.community.util

import com.imyvm.community.infra.CommunityConfig
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun getFormattedMillsHour(mills: Long): String {
    val timezone = CommunityConfig.TIMEZONE.value
    val zoneId = ZoneId.of(timezone)

    val dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(mills), zoneId)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hha (XXX)")
    return dateTime.format(formatter)
}