package audio

import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory
import bufferSize
import sampleRate
import java.io.File

/**
 * Created by christian.henry on 12/15/16.
 */
object Dispatchers {

    fun fromDefaultMicrophone(): AudioDispatcher {
        val fromDefaultMicrophone = AudioDispatcherFactory.fromDefaultMicrophone(sampleRate.toInt(), bufferSize, bufferSize * 3 / 4)
        println("Format used is ${fromDefaultMicrophone.format}")
        return fromDefaultMicrophone
    }

    fun fromFile(fileLocation: String): AudioDispatcher {
        val file = File(fileLocation)
        return AudioDispatcherFactory.fromFile(file, bufferSize, bufferSize / 2)
    }

}