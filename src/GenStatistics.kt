import javax.xml.soap.Text
import kotlin.math.pow
import kotlin.math.sqrt

fun GenStatistics(channel : SuperChannel, GlobalSignal: Signal):String {
    var text = String()
    text = "видимость графика на Осциллограммах от - "
    text += "" + GlobalSignal.vision[channel.channelNum][0] + " до - " + GlobalSignal.vision[channel.channelNum][1] + "\n"

    var sortedArr = GlobalSignal.arraChannels[channel.channelNum].copyOf()
    sortedArr.sort()

    text += "Минимум = " +sortedArr[0] + "\n"
    text += "Среднее = " +sortedArr[sortedArr.size / 2] + "\n"
    text += "Максимум= " +sortedArr[sortedArr.size-1] + "\n"

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