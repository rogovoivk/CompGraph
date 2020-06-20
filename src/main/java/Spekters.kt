import kotlin.math.abs
import kotlin.math.sqrt

fun countAmplitudeSpekter(samplesLis : Array<Float>, numSamples: Int, sampleRate: Float) :Array<Float> {
    val Complex: ComplexArr =
        sinePlusCosine(samplesLis, numSamples)
    val res = Complex.Module()
    for (i in 0..res.size-1){
        res[i] *= (1f/sampleRate)
    }
    return res
}

fun countSPM(samplesLis : Array<Float>, numSamples: Int, sampleRate: Float) :Array<Float> {
    val Complex: ComplexArr =
        sinePlusCosine(samplesLis, numSamples)
    val res = Complex.Module()
    for (i in 0..res.size-1){
        res[i] *= Math.pow((1f/sampleRate).toDouble(), 2.toDouble()).toFloat() * res[i]
    }
    return res
}

fun countSmoothingAmplitude(samplesLis : Array<Float>, numSamples: Int, sampleRate: Float, smoothCoeff: Int):Array<Float> {
    var ans: Array<Float> = countAmplitudeSpekter(samplesLis, numSamples, sampleRate)
    val ansCopy = ans.copyOf()
    for (i in 0..ans.size-1) {
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
    for (i in 0..samplesLis.size-1) {
        for (j in -smoothCoeff..smoothCoeff) {
            samplesLis[i] = samplesLis[i] + j
        }
        ans += countSPM(samplesLis, numSamples, sampleRate)
    }

    for (i in 0..ans.size-1) {
        ans[i] = ans[i] * 1f/(2*smoothCoeff + 1)
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

fun countAmplitudeSpekterInLg(transeredSamplesLis : Array<Float>) {
    for (i in 0..transeredSamplesLis.size-1) {
        transeredSamplesLis[i] = 20f * Math.log10(transeredSamplesLis[i].toDouble()).toFloat()
    }
}

fun countSPMInLg(samplesLis : Array<Float>, numSamples: Int, sampleRate: Float): Array<Float> {
    val res = countSPM(samplesLis, numSamples, sampleRate)
    for (i in 0..res.size-1) {
        res[i] = 10f * Math.log10(res[i].toDouble()).toFloat()
    }
    return res
}

fun countSPMInLg(transeredSamplesLis : Array<Float>) {
    for (i in 0..transeredSamplesLis.size-1) {
        transeredSamplesLis[i] = 10f * Math.log10(transeredSamplesLis[i].toDouble()).toFloat()
    }
}