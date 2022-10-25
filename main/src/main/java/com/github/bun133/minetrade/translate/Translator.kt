package com.github.bun133.minetrade.translate

import com.google.gson.Gson
import java.io.InputStream

class Translator(jsonFile: InputStream) {
    private val entries = mutableMapOf<String, String>()

    init {
        val gson = Gson()
        gson.fromJson(jsonFile.reader(), Map::class.java).forEach {
            entries[it.key as String] = it.value as String
        }
    }

    fun getTranslated(key: String): String? {
        return entries[key]
    }
}