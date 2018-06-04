package com.git.adaptor.response

data class PullRequest(val url: String,
    val createdBy: String,
    val createdAt: String,
    val reviewedBy: String = "NO ONE",
    val action: String = "NO ACTION")

data class ErrorResponse(val status: String,
    val message: String)

