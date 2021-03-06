import java.lang.Exception

import kotlin.math.pow
import kotlin.math.sqrt

fun GenStatistics(channel : SuperChannel, GlobalSignal: Signal):String {
    var text = String()
    var sortedArr = GlobalSignal.arraChannels[channel.channelNum].copyOf()
    sortedArr.sort(GlobalSignal.vision[0], GlobalSignal.vision[1])
    try {
        text += "видимость графика на Осциллограммах от " + GlobalSignal.vision[0] + " до " + GlobalSignal.vision[1] + "\n"
//        sortedArr = Array<Float>(GlobalSignal.vision[channel.channelNum][1] - GlobalSignal.vision[channel.channelNum][0])
//        for (i in 0..(GlobalSignal.vision[channel.channelNum][1] - GlobalSignal.vision[channel.channelNum][0])){
//            sortedArr[i] = GlobalSignal.arraChannels[channel.channelNum][i + GlobalSignal.vision[channel.channelNum][0]]
//        }
    }catch (e: Exception){
//        sortedArr = GlobalSignal.arraChannels[channel.channelNum].copyOf()
//        sortedArr.sort()
    }


    text += "Минимум = " +sortedArr[0] + "\n"
    text += "Среднее = " +sortedArr[sortedArr.size / 2] + "\n"
    text += "Максимум= " +sortedArr[sortedArr.size-1] + "\n"

//    var sum = 0f
//    for (i in 0..sortedArr.size){
//        sum += sortedArr[i]
//    }
//    sum /= sortedArr.size
//    text += "Среднее = " +sum + "\n"

    ///TEST///
    val testArr = arrayOf(1.0f, 2.0f, 3.0f, 4.0f, 5.0f)
    println(getAverage(testArr))
    println(getAverage(testArr, -1.0f))
    println(getAverage(testArr, -2.0f, 2))
    //////////

    val avg = getAverage(sortedArr)

    text += "Среднее арифметическое = " +avg + "\n"

    val disp = getAverage(sortedArr, -avg, 2)

    text += "Дисперсия = " +disp + "\n"

    val standardDeviation = sqrt(disp)

    text += "Среднеквадратичное отклонениe = " + standardDeviation + "\n"

    val varCoeff = standardDeviation / avg

    text += "Коэффициент вариации = " + varCoeff + "\n"

    val assymetryCoeff = getAverage(sortedArr, -avg, 3) / standardDeviation.pow(3)

    text += "Коэффициент асимметрии = " + assymetryCoeff + "\n"

    val excessRatio = getAverage(sortedArr, -avg, 4) / standardDeviation.pow(4) - 3

    text += "Коэффициент эксцесса = " + excessRatio + "\n"

    val quantOne = (0.05 * sortedArr.size).toInt()

    text += "Квантиль порядка 0.05 = " + sortedArr[quantOne] + "\n"

    val quantTwo = (0.95 * sortedArr.size).toInt()

    text += "Квантиль порядка 0.95 = " + sortedArr[quantTwo] + "\n"

    val median = sortedArr[(sortedArr.size / 2).toInt()]

    text += "Медиана = " + median + "\n"

    return text
}

/**
 * Функция в которой выисляется среднее арифметическое
 * @param array массив, в котором будут братся элементы и вычилсяться СА
 * @param num число, которое будет прибавляться к i-ому элемента array
 * @param power степень, в которую будет возноситься элемент array - num
 * @return 1/n * sum (array{i} - num)^pow where i from 0 to array.size - 1
 */
fun getAverage(array: Array<Float>, num: Float = 0.0f, power: Int = 1): Float {
    var res:Float = 0.0f
    for (el in array) {
        val tmp = el + num
        val tmp2 = tmp.pow(power)
        res += tmp2
    }
    return res/array.size
}