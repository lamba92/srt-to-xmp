package com.github.lamba92.srttoexif

import kotlinx.serialization.encodeToString
import nl.adaptivity.xmlutil.serialization.XML
import kotlin.streams.asSequence
import kotlin.test.Test

class XMPSerializationTest {

    val metadataString
        get() = Thread.currentThread()
            .contextClassLoader
            .getResourceAsStream("DJI_0588.SRT")
            ?.bufferedReader()
            ?.lines()
            ?.asSequence()
            ?.filter { it.startsWith("[") }
            ?.map { parseMetadata(it) }
            ?.first()
            ?: error("No metadata foud!")
    
    val xml
        get() = XML {
            indentString = "  "
        }
    
    @Test
    fun encode() {
        println(xml.encodeToString(metadataString))
    }
}
