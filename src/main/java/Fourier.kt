//import com.sun.media.sound.FFT
import hageldave.ezfftw.dp.*

//import hageldave.ezfftw.fp.*

fun sinePlusCosine() {

    val numSamples = 16
    val second = 2 * Math.PI // interval of one second
    // create samples
    val samples = DoubleArray(numSamples)
    for (i in 0 until numSamples) {
        samples[i] = Math.sin(i * second / numSamples)
        samples[i] += Math.cos(i * second / numSamples)
    }
    // execute fft
    val realPart = DoubleArray(numSamples)
    val imagPart = DoubleArray(numSamples)
    FFT.fft(samples, realPart, imagPart, numSamples.toLong())
    // print result (omit conjugated complex results)
    for (i in 0 until 1 + numSamples / 2) {
        System.out.format("%dHz | % .2f%+.2fi%n", i, realPart[i], imagPart[i])
    }
}