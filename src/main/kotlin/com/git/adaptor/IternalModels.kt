package com.git.adaptor

import java.time.ZonedDateTime

data class AuditLog(val id: Long,
    val application: String,
    val org: String,
    val openPrCount: Int,
    val queryAt: ZonedDateTime)