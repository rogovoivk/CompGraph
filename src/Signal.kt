class Signal(chan: Int, samplesnumber_: Int, samplingrate_: String, startdate_: String, starttime_: String, arraChannels: Array<Array<Float>>, from_: String, channelsnames_: Array<String?> ) {
    init  {
        //var chanals: Int = chan
    }
    var channels = chan
    var staticarray = Array(chan,{emptyArray<Float>()}) //надеюсь это работает
    var samplesnumber: Int = samplesnumber_
    var samplingrate: String = samplingrate_
    var startdate: String = startdate_
    var starttime: String = starttime_
    var arraChannels: Array<Array<Float>> = arraChannels
    var from: String = from_                                               //это источник
    var channelsnames: Array<String?> = channelsnames_
    //var channelsnames = emptyArray<String>()
    var vision: Array<Array<Int>> = Array(channels, { arrayOf(0, samplesnumber) })


    fun WhatTime(dot: Int, samplerate_: Float): String{
//        var h1 = starttime[0].toString().toInt() * 10 + starttime[1].toString().toInt()
//        var h2 = h1.toInt()
        var h = (starttime[0].toString().toInt() * 10 + starttime[1].toString().toInt()).toFloat()
        var m = (starttime[3].toString().toInt() * 10 + starttime[4].toString().toInt()).toFloat()
        var s = (starttime[6].toString().toInt() * 10 + starttime[7].toString().toInt()).toFloat()
        for (i in 0..dot){
            s += samplerate_
            if (s >= 60){
                s -= 60
                m++
                if(m == 60f){
                    m = 0f
                    h++
                }
            }
        }
        var res: String = h.toString() + ":" + m + ":" + s
        return res
    }
}