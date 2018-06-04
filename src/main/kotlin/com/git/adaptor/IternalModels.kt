package com.git.adaptor

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.ZonedDateTime

data class AuditLog(@JsonIgnore val id: Long,
    val org: String,
    val application: String,
    val openPrCount: Int,
    val queryAt: ZonedDateTime)