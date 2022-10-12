package com.raassh.gemastik15.utils

import android.util.Xml
import com.raassh.gemastik15.local.db.Facility
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

private val ns: String? = null

class FacilityDataXmlParser {
    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream): List<Facility> {
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readFeed(parser)
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readFeed(parser: XmlPullParser): List<Facility> {
        val facilities = mutableListOf<Facility>()

        parser.require(XmlPullParser.START_TAG, ns, "data")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            if (parser.name == "facility") {
                facilities.add(readFacility(parser))
            } else {
                skip(parser)
            }
        }
        return facilities
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readFacility(parser: XmlPullParser): Facility {
        parser.require(XmlPullParser.START_TAG, ns, "facility")
        var name: String = ""
        var description: String = ""
        var icon: String = ""
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "name" -> name = readText(parser, "name")
                "description" -> description = readText(parser, "description")
                "icon" -> icon = readText(parser, "icon")
                else -> skip(parser)
            }
        }
        return Facility(name, description, icon)
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readText(parser: XmlPullParser, tag: String): String {
        parser.require(XmlPullParser.START_TAG, ns, tag)
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        parser.require(XmlPullParser.END_TAG, ns, tag)
        return result
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}