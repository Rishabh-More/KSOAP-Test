package com.qwerty.ksoaptest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import com.qwerty.ksoaptest.databinding.ActivityMainBinding
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<List<String>> {

    private lateinit var binding: ActivityMainBinding

    private val LOADER_ID = 132
    private val NAMESPACE = "http://www.webserviceX.NET"
    private val METHOD_NAME = "GetCitiesByCountry"

    private val SOAP_ACTION = "$NAMESPACE/$METHOD_NAME"
    private val URL = "http://www.webservicex.net/globalweather.asmx?WSDL"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<String>> {
        return object : AsyncTaskLoader<List<String>>(this){
            override fun onStartLoading() {
                super.onStartLoading()
                forceLoad()
            }

            override fun loadInBackground(): List<String>? {
                val request = SoapObject(NAMESPACE, METHOD_NAME)
                request.addProperty("CountryName","")

                val envelope = SoapSerializationEnvelope(SoapEnvelope.VER12)
                envelope.dotNet = true
                envelope.setOutputSoapObject(request)

                val httpTransport = HttpTransportSE(URL)
                try {
                    httpTransport.call(SOAP_ACTION, envelope)
                } catch(e: Exception){
                    e.printStackTrace()
                }
                Log.i("SOAP_REQUEST", envelope.bodyOut.toString())
                Log.i("SOAP_RESPONSE", envelope.bodyIn.toString())

                return null
            }
        }
    }

    override fun onLoadFinished(loader: Loader<List<String>>, data: List<String>?) {
        binding.testMainTextview.text = "Soap Data Fetched Successfully"
    }

    override fun onLoaderReset(loader: Loader<List<String>>) {

    }
}