import java.awt.Canvas
import java.awt.Graphics
import java.io.File
import java.util.*
import javax.swing.JButton
import javax.swing.JFileChooser

//функции

fun FileToSignal(f: File) : Signal{
    val listSTR: List<String> = f.readLines()
    val channelsNumber: Int = listSTR[1].toInt()
    val samplesNumber: Int = listSTR[3].toInt()
    val samplingRate: String = listSTR[5].toString()
    val startDate: String = listSTR[7]
    val startTime: String = listSTR[9]
    var channelsnames = arrayOfNulls<String>(channelsNumber)
    channelsnames[0] = ""
    var cout: Int = 0
    for (i in 0..listSTR[11].length-1){
        if (listSTR[11][i] != ';' && listSTR[11][i] != '\n') {channelsnames[cout] = channelsnames[cout] + listSTR[11][i]}
        if (listSTR[11][i] == ';') {
            cout++
            channelsnames[cout] = ""
        }
    }
//    for (i in 0..channelsnames.size-1){
//        println(channelsnames[i])
//    }
    //val arraChannels: Array<FloatArray> = Array(channelsNumber){FloatArray(samplesNumber){0f} }
    val arraChannels: Array<Array<Float>> = Array(channelsNumber, { Array(samplesNumber, {0f}) })
    for (i in 12..listSTR.size-1){
        var valueF: String = ""
        var cout: Int = 0
        for (j in 0..listSTR[i].length-1){
            if (listSTR[i][j] != ' '){
                valueF += listSTR[i][j]
            }
            if (listSTR[i][j] == ' '){
                //println(valueF)
                val float1: Float = valueF.toFloat()
                arraChannels[cout][i-12] = float1
                cout++
                valueF = ""
            }
        }
    }


    var sgn: Signal = Signal(channelsNumber, samplesNumber, samplingRate, startDate, startTime, arraChannels, f.name, channelsnames)
    return sgn
}


class OscillogramList{
    //var buttonArr: java.lang.reflect.Array
//    var buttonList = LinkedList<JButton>()
//    var canvasList = LinkedList<Canvas>()
    var channelList = LinkedList<Canvas>()

//    fun Add(but: JButton, canv: Canvas){
//        buttonList.add(but)
//        canvasList.add(canv)
//    }
}


