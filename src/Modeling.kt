import javax.swing.*
import java.util.Date

fun InitModel(v: String): Signal{

    lateinit var sgn : Signal

    if (v == "v1"){
            val time = JTextField("00:00:00")
            val date = JTextField("01-01-2020")
            val N0 = JTextField("24:10:10")
            var samplenumber_ = JTextField("1000")
            val inputs = arrayOf<JComponent>(JLabel("start date :"), date, JLabel("start time :"), time, JLabel("N (0)"), N0, JLabel("Кол-во элементов"), samplenumber_)
            val result =
                JOptionPane.showConfirmDialog(null, inputs, "Взодные параметры", JOptionPane.PLAIN_MESSAGE)
            if (result == JOptionPane.OK_OPTION) {
                sgn = v1(date.text, time.text, samplenumber_.text.toInt(), N0.text)
            } else {
                println("User canceled / closed the dialog, result = $result")
            }
    }

    return sgn
}

fun v1(date: String, time: String, samplenumber_: Int, N0: String): Signal{
    val arraChannels: Array<Array<Float>> = Array(1, { Array(samplenumber_, {0f}) })
    var cout = 0
    var channelsnames = Array<String?>(1,{ i -> "delayed_single_pulse"})
    var sgn: Signal = Signal(1, samplenumber_, "1", date, time, arraChannels, "modeling", channelsnames)
    for (i in 0..samplenumber_-1){
        val str = sgn.WhatTime(i)
        if (str == N0){
            sgn.arraChannels[0][i] = 100f
        }
    }
    return sgn
}
