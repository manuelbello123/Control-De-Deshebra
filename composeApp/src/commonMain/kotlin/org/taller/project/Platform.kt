package org.taller.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform