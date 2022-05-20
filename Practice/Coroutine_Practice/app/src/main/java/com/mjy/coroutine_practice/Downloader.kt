package com.mjy.coroutine_practice

interface Downloader {
    suspend fun download()
}