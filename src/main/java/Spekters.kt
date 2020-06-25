import kotlin.math.abs
import kotlin.math.sqrt

fun countAmplitudeSpekter(samplesLis : Array<Float>, numSamples: Int, sampleRate: Float, Zero0: Boolean = false) :Array<Float> {
//    val Complex: ComplexArr =
//        sinePlusCosine(samplesLis, numSamples)
//    val res = Complex.Module()
//    if (Zero0 == true)
//        res[0] = 0f
    for (i in 0..samplesLis.size-1){
        samplesLis[i] *= (1f/sampleRate)
    }
    return samplesLis
}

fun countSPM(samplesLis : Array<Float>, numSamples: Int, sampleRate: Float, Zero0: Boolean = false) :Array<Float> {
//    val Complex: ComplexArr =
//        sinePlusCosine(samplesLis, numSamples)
//    val res = Complex.Module()
//    if (Zero0 == true)
//        res[0] = 0f
    for (i in 0..samplesLis.size-1){
        samplesLis[i] *= Math.pow((1f/sampleRate).toDouble(), 2.toDouble()).toFloat() * samplesLis[i]
    }
    return samplesLis
}

fun countSmoothingAmplitude(samplesLis : Array<Float>, numSamples: Int, sampleRate: Float, smoothCoeff: Int, Zero0: Boolean = false):Array<Float> {
    var ans: Array<Float> = countAmplitudeSpekter(samplesLis, numSamples, sampleRate)
    val ansCopy = ans.copyOf()
    for (i in 0..ans.size-1) {

        ans[i] = 0f
        for (j in -smoothCoeff..smoothCoeff) {

            println((i + j)%ansCopy.size)
            ans[i] += ansCopy[(i + j + ansCopy.size)%ansCopy.size]

        }
        ans[i] = ans[i] / (2 * smoothCoeff + 1)
    }
    return ans
}

fun countSmoothingSPM(samplesLis : Array<Float>, numSamples: Int, sampleRate: Float, smoothCoeff: Int):Array<Float> {
    var ans: Array<Float> = Array<Float>(samplesLis.size-1,{0f})
    val ansCopy = ans.copyOf()
    for (i in 0..ans.size-1) {

        ans[i] = 0f
        for (j in -smoothCoeff..smoothCoeff) {

            println((i + j)%ansCopy.size)
            ans[i] += ansCopy[(i + j + ansCopy.size)%ansCopy.size]

        }
        ans[i] = ans[i] / (2 * smoothCoeff + 1)
    }
    return ans
}

fun countAmplitudeSpekterInLg(samplesLis : Array<Float>, numSamples: Int, sampleRate: Float): Array<Float> {
    val res = countAmplitudeSpekter(samplesLis, numSamples, sampleRate)
    for (i in 0..res.size-1) {
        res[i] = 20f * Math.log10(res[i].toDouble()).toFloat()
    }
    return res
}

fun countAmplitudeSpekterInLg(transeredSamplesLis : Array<Float>): Array<Float> {
    var res = transeredSamplesLis.copyOf()
    for (i in 0..transeredSamplesLis.size-1) {
        if (transeredSamplesLis[i] != 0f)
            res[i] = 20f * Math.log10(transeredSamplesLis[i].toDouble()).toFloat()
    }
    return res
}

fun countSPMInLg(samplesLis : Array<Float>, numSamples: Int, sampleRate: Float): Array<Float> {
    val res = countSPM(samplesLis, numSamples, sampleRate)
    for (i in 0..res.size-1) {
        res[i] = 10f * Math.log10(res[i].toDouble()).toFloat()
    }
    return res
}

fun countSPMInLg(transeredSamplesLis : Array<Float>): Array<Float> {
    var res = transeredSamplesLis.copyOf()
    for (i in 0..transeredSamplesLis.size-1) {
        if (transeredSamplesLis[i] != 0f)
            res[i] = 10f * Math.log10(transeredSamplesLis[i].toDouble()).toFloat()
    }
    return res
}