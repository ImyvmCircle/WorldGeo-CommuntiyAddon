package com.imyvm.community.domain

class PendingOperation {
    val expireAt: Long = System.currentTimeMillis() + 60 * 1000
}