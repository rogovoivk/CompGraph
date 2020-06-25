//import com.sun.media.sound.FFT
import hageldave.ezfftw.dp.*

//import hageldave.ezfftw.fp.*

fun sinePlusCosine(samplesLis : Array<Float>, numSamples: Int):ComplexArr {
    val second = 2 * Math.PI // interval of one second
    // create samples
    val samples = DoubleArray(numSamples)
    for (i in 0..numSamples-1){
        samples[i] = samplesLis[i].toDouble()
    }
    // execute fft
    val realPart = DoubleArray(numSamples)
    val imagPart = DoubleArray(numSamples)
    FFT.fft(samples, realPart, imagPart, numSamples.toLong())
    // print result (omit conjugated complex results)
//    for (i in 0 until 1 + numSamples / 2) {
//        System.out.format("%dHz | % .2f%+.2fi%n", i, realPart[i], imagPart[i])
//    }

    var imag = Array<Float>(imagPart.size, {0f})
    for (i in 0..numSamples-1) {
        imag[i] = imagPart[i].toFloat()
        samplesLis[i] = realPart[i].toFloat()
    }
    var ans = ComplexArr(samplesLis, imag)
    return  ans

}