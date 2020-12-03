package com.qwerty.ksoaptest

import android.util.Log
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

object QueryUtils {

    private val LOG_TAG = QueryUtils::class.java.simpleName

    private const val NAMESPACE = "http://www.webserviceX.NET"
    private const val METHOD_NAME = "GetCitiesByCountry"

    private const val SOAP_ACTION = "$NAMESPACE/$METHOD_NAME"

    private const val URL = "http://www.webservicex.net/globalweather.asmx?WSDL"

    fun fetchCitiesData(userInput: String): List<String>? {
        val request = SoapObject(NAMESPACE, METHOD_NAME)
        request.addProperty("CountryName", userInput)

        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER12)
        envelope.dotNet = true
        envelope.setOutputSoapObject(request)

        val httpTransport = HttpTransportSE(URL)
        try {
            httpTransport.call(SOAP_ACTION, envelope)
            return extractDataFromXmlResponse(envelope)
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.toString())
        }

        return null
    }

    @Throws(Exception::class)
    private fun extractDataFromXmlResponse(envelope: SoapSerializationEnvelope): List<String> {

        val citiesList = mutableListOf<String>()

        val docBuildFactory = DocumentBuilderFactory.newInstance()
        val docBuilder = docBuildFactory.newDocumentBuilder()
        val doc = docBuilder.parse(InputSource(StringReader(envelope.response.toString())))

        val nodeList = doc.getElementsByTagName("Table")
        for (i in 0 until nodeList.length) {

            val node = nodeList.item(i)
            if (node.nodeType == Node.ELEMENT_NODE) {
                val element = node as Element
                citiesList.add(element.getElementsByTagName("City").item(0).textContent)
            }
        }

        return citiesList
    }
}