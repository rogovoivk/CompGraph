


import java.awt.Canvas
import java.awt.Color
import java.awt.Graphics
import java.awt.event.MouseEvent
import kotlin.math.abs
import kotlin.math.ceil


class SuperChannel(sgn_: Signal, channelNum_: Int, wight_: Float, hight_: Float, start_: Int, finish_: Int, isCoordinates_: Boolean = false ){
    var sgn: Signal = sgn_
    var channelNum: Int = channelNum_
    var width: Float = wight_
    var height: Float = hight_
    var start: Int = start_
    var arrDot: Array<Float> = sgn.arraChannels[channelNum].copyOf()
    var finish: Int = finish_
    var cardinalityOnCanvas: Float = wight_
    var isCoordinates = isCoordinates_
    var isPaintApproach = true
    var LocalMaxMin = false

    val histContainerWidth = 350 //не нашла, как получить длину контейнера для гистограммы. Опытным путем выбрала произвольную чиселку

    var isSmallVision = false

    var isPaint = false
    var PaintStart = 0
    var PaintFinish = 0


    var LineForHistogram = 5
    var MaxForHistogram = 0f
    var hightOfHist = 200//200

    var pal: Array<Array<Int>> = Array(256, { Array(3, {0}) })


    /** всякие переменные для спектра Фурье **/
    var FourierArrDot : Array<Float> = sgn.arraChannels[channelNum].copyOf()
    //var IsFourier = false
    private var FWeight: Int = 0
    private var FHight: Int = 0
    private var FIsCoordinates = false
    private var FStart: Int = 0
    private var FFinish: Int = 0
    private var FCoordinates: Array<Float> = sgn.arraChannels[channelNum].copyOf()
    private var FisSmallVision = false
    /**тут заканчиваются всякие переменные для спектра Фурье **/
    /** всякие переменные для спектраграмм **/
    private var SpectragramMatrix = Array<Array<Float>>(width.toInt(), { Array(height.toInt(), {0f}) })
    private var SpecHight = 0
    private var SpecWeight = 0
    private var lvlArr = Array<Float>(6, {0f})
    private var load = false
    private var brith : Int = 1
    /**тут заканчиваются всякие переменные для спектраграмм **/



    fun ChangePainDot(){
        if ( PaintStart > PaintFinish){
            var swap = PaintStart
            PaintStart = PaintFinish
            PaintFinish = swap
        }

        var fix = (sgn.samplesnumber / width)
        finish = (start + (PaintFinish * fix)).toInt()
        start = (start + (PaintStart * fix)).toInt()
    }



    fun ChangeDot(){

        if((finish - start) < width){
            println("ЗАДАННА СУПЕР МАЛЕНЬКАЯ ОБЛАСТЬ")
            isSmallVision = true
        }
        else{isSmallVision = false}

        var zero = 0
        var length = arrDot.size-1
        if (LocalMaxMin == true){
            zero = start
            length = finish-1
        }


        arrDot = sgn.arraChannels[channelNum].copyOf()//.copy(sgn.arraChannels[channelNum])// = sgn.arraChannels[channelNum]
        var max: Float = arrDot[1]
        var min: Float = arrDot[1]
        var top: Float = height
        var right: Float = 0f
        for (i in zero..length) {
            if (max < arrDot[i]) {
                max = arrDot[i]
            }
            if (min > arrDot[i]) {
                min = arrDot[i]
            }
        }

        if (min < 0 ){
            for (i in zero..length) {
                arrDot[i] += abs(min)
            }
            max += abs(min)
            min = 0f

        }
        else {
            for (i in zero..length) {
                arrDot[i] -= min
            }
            max -= min
            min = 0f
        }
        top = max/top
        for (i in zero..length){
            arrDot[i] /= top
            arrDot[i] = height - arrDot[i]
        }

        MaxForHistogram = max
    }


    var canv = object : Canvas() {
        override
        fun paint(g: Graphics) {
            ChangeDot()
            var x1 = 0
            if (isCoordinates == true) x1 = 50
            g.color = Color.BLUE
            var candleFilling: Int = ((finish - start) / this@SuperChannel.width).toInt()
            if (isCoordinates == true) candleFilling = ((finish - start) / (this@SuperChannel.width - 50)).toInt()



            if (candleFilling == 0) candleFilling = 1
            if (isSmallVision == false) {
                for (i in start..finish - candleFilling step candleFilling) {
                    arrDot.sort(i, candleFilling + i - 1)
                    if (x1 <= this@SuperChannel.width) {
                        if (arrDot[i].toInt() >= this@SuperChannel.height) arrDot[i] = this@SuperChannel.height - 1
                        //if (arrDot[i].toInt() = hight) arrDot[i] = hight - 1
                        g.drawLine(x1, arrDot[i].toInt(), x1, arrDot[candleFilling + i - 1].toInt())
                        if (i > start+1){
                            var min0 = 0
                            var max0 = 0
                            if (arrDot[i - candleFilling].toInt() <= arrDot[candleFilling + i - 1 - candleFilling].toInt()) {
                                min0 = arrDot[i - candleFilling].toInt()
                                max0 = arrDot[candleFilling + i - 1 - candleFilling].toInt()
                            } else {
                                max0 = arrDot[i - candleFilling].toInt()
                                min0 = arrDot[candleFilling + i - 1 - candleFilling].toInt()
                            }
                            var min1 = 0
                            var max1 = 0
                            if (arrDot[i].toInt() <= arrDot[candleFilling + i - 1].toInt()) {
                                min1 = arrDot[i].toInt()
                                max1 = arrDot[candleFilling + i - 1].toInt()
                            } else {
                                max1 = arrDot[i].toInt()
                                min1 = arrDot[candleFilling + i - 1].toInt()
                            }
                            if (max1 < min0) {
                                g.drawLine(x1 - 1, max1, x1, min0)
                            }
                            if (min1 > max0) {
                                g.drawLine(x1 - 1, min1, x1, max0)
                            }
                        }
                    } else {
                        println("график вылез за границу")
                    }
                    x1++
                }
            }
            else{
                var stepX = this@SuperChannel.width /(finish - start)
                for (i in start..finish - 1) {
                    //arrDot.sort(i, candleFilling + i - 1)
                    if (x <= this@SuperChannel.width) {
                        g.drawOval(x1, arrDot[i].toInt(), 1, 4)
                        if (i > start){g.drawLine(x1, arrDot[i].toInt(), x1-stepX.toInt(), arrDot[i-1].toInt())}
                    } else {
                        println("график вылез за границу")
                    }
                    x1 += stepX.toInt()
                }
            }

            g.color = Color.BLACK
            val fm = g.fontMetrics
            g.drawString(sgn.channelsnames[channelNum], (this@SuperChannel.width /2).toInt(), 15)


            if (isCoordinates == true) {
                var coordinates = sgn.arraChannels[channelNum].copyOf()
                var start_ = start
                var finish_ = finish
                if (LocalMaxMin == false) {
                    coordinates.sort(0, coordinates.size-1)
                    start_ = 0
                    finish_ = coordinates.size-1
                }
                if (LocalMaxMin == true) {
                    coordinates.sort(start, finish)
                }

                g.color = Color.BLACK
                g.drawLine(0, this@SuperChannel.height.toInt() - 2, this@SuperChannel.width.toInt(), this@SuperChannel.height.toInt() - 2)
                g.drawLine(2, 2, 2, this@SuperChannel.height.toInt())

                g.drawString(coordinates[start_].toString(), 5, hight_.toInt() - 5)
                g.drawString(coordinates[start_ + (finish_ - start_) / 4].toString(), 5, (hight_.toInt() / 4) * 3)
                g.drawString(coordinates[start_ + (finish_ - start_) / 2].toString(), 5, hight_.toInt() / 2)
                g.drawString(coordinates[start_ + ((finish_ - start_) / 4) * 3].toString(), 5, hight_.toInt() / 4)
                g.drawString(coordinates[finish_ - 1].toString(), 5, 10)

                g.drawString(sgn.WhatTime((finish - start)/6 * 1 + start, sgn.samplingrate.toFloat()), (this@SuperChannel.width /6).toInt() * 1 - 15, this@SuperChannel.height.toInt() + 15)
                g.drawString(sgn.WhatTime((finish - start)/6 * 2 + start,sgn.samplingrate.toFloat()), (this@SuperChannel.width /6).toInt() * 2 - 15, this@SuperChannel.height.toInt() + 15)
                g.drawString(sgn.WhatTime((finish - start)/6 * 3 + start, sgn.samplingrate.toFloat()), (this@SuperChannel.width /6).toInt() * 3 - 15, this@SuperChannel.height.toInt() + 15)
                g.drawString(sgn.WhatTime((finish - start)/6 * 4 + start, sgn.samplingrate.toFloat()), (this@SuperChannel.width /6).toInt() * 4 - 15, this@SuperChannel.height.toInt() + 15)
                g.drawString(sgn.WhatTime((finish - start)/6 * 5 + start, sgn.samplingrate.toFloat()), (this@SuperChannel.width /6).toInt() * 5 - 15, this@SuperChannel.height.toInt() + 15)

                g.color = Color.GRAY
                //g.drawLine(0, hight.toInt() - 5, wight.toInt(), hight.toInt() - 5)
                g.drawLine(5, this@SuperChannel.height.toInt()/2, this@SuperChannel.width.toInt(), this@SuperChannel.height.toInt()/2)
                g.drawLine(5, 2, this@SuperChannel.width.toInt(), 2)

                g.drawLine((this@SuperChannel.width /6).toInt() * 1, 2, (this@SuperChannel.width /6).toInt() * 1, this@SuperChannel.height.toInt())
                g.drawLine((this@SuperChannel.width /6).toInt() * 2, 2, (this@SuperChannel.width /6).toInt() * 2, this@SuperChannel.height.toInt())
                g.drawLine((this@SuperChannel.width /6).toInt() * 3, 2, (this@SuperChannel.width /6).toInt() * 3, this@SuperChannel.height.toInt())
                g.drawLine((this@SuperChannel.width /6).toInt() * 4, 2, (this@SuperChannel.width /6).toInt() * 4, this@SuperChannel.height.toInt())
                g.drawLine((this@SuperChannel.width /6).toInt() * 5, 2, (this@SuperChannel.width /6).toInt() * 5, this@SuperChannel.height.toInt())
            }

            if (isPaintApproach == true){
                if (MouseEvent.BUTTON1_DOWN_MASK == 1){
                    println("nagata")

                }
            }

        }
    }



    public fun GenHist(): IntArray{
        ChangeDot()
        var CandleHight = IntArray(LineForHistogram)



//        var cloneArrDor = arrayOfNulls<Float>(finish-start)
//        for (i in start..finish-1){
//            cloneArrDor[i] = arrDot[i + start]
//        }

        var cloneArrDor = arrDot.copyOfRange(start, finish)

        var min = cloneArrDor[0]
        try {
            for (i in 0..cloneArrDor.size-1){
                for (j in 0..LineForHistogram - 1) {
                    if (j == 0 && cloneArrDor[i] - 0f < 0.000001) {
                        CandleHight[0]++
                    }
//                if ((j == 0) and (cloneArrDor[i] < (hightOfHist / LineForHistogram))){CandleHight[0]++}
//                else{
//                    if((cloneArrDor[i] < ((hightOfHist / LineForHistogram) * j)) and (cloneArrDor[i] > ((hightOfHist / LineForHistogram) * (j - 1))))
//                        {CandleHight[j-1]++}
//                    if ((j == LineForHistogram - 1) and (cloneArrDor[i] > (hightOfHist - (hightOfHist / LineForHistogram))))
//                        {CandleHight[j]++}
//                }
                    if ((cloneArrDor[i] <= ((hightOfHist / LineForHistogram) * (j + 1))) && (cloneArrDor[i] > ((hightOfHist / LineForHistogram)*j))) {
                        CandleHight[j]++
                    }
                }
            }
        } catch (e: Exception){}


        return CandleHight
    }

    var Histogram  = object : Canvas() {
        override
        fun paint(g: Graphics) {
            var arr = GenHist()
            g.color = Color.BLUE
            var x = 0
            var max = 0
            for (i in 0..arr.size-1){
                if (max < arr[i]) max = arr[i]
            }
            var top = ceil(max/100f).toInt()
            for (i in 0..arr.size-1){
                arr[i] /= top
            }

            val a = this.width
            var histBarWidth = 20
            if (20f * LineForHistogram > histContainerWidth) {
                histBarWidth = histContainerWidth / LineForHistogram
            }
            for (i in 0..arr.size-1){
                //g.drawLine(x, arr[i], x, 0)
                //if (arr[i] < 50) arr[i] = 100 - arr[i]
                //if (arr[i] > 100) arr[i] = 100
                g.drawRect(x, 100 - arr[i], histBarWidth, arr[i])
                x += histBarWidth //2
            }
        }
    }


    fun FourierChangeDot(){

        if((FFinish - FStart) < FWeight){
            println("ЗАДАННА СУПЕР МАЛЕНЬКАЯ ОБЛАСТЬ")
            FisSmallVision = true
        }
        else{FisSmallVision = false}

        var zero = 0
        var length = FourierArrDot.size-1


        //arrDot = sgn.arraChannels[channelNum].copyOf()//.copy(sgn.arraChannels[channelNum])// = sgn.arraChannels[channelNum]
        var max: Float = FourierArrDot[1]
        var min: Float = FourierArrDot[1]
        var top: Float = FHight.toFloat()
        var right: Float = 0f
        for (i in zero..length) {
            if (max < FourierArrDot[i]) {
                max = FourierArrDot[i]
            }
            if (min > FourierArrDot[i]) {
                min = FourierArrDot[i]
            }
        }

        if (min < 0 ){
            for (i in zero..length) {
                FourierArrDot[i] += abs(min)
            }
            max += abs(min)
            min = 0f

        }
        else {
            for (i in zero..length) {
                FourierArrDot[i] -= min
            }
            max -= min
            min = 0f
        }
        top = max/top
        for (i in zero..length){
            FourierArrDot[i] /= top
            FourierArrDot[i] = FHight - FourierArrDot[i]
        }
    }

    fun GenFourierCanv(weight_: Int, hight_: Int, FourierCordinates_: Boolean, start_: Int, finish_: Int, arr: Array<Float>){
        FourierArrDot = arr.copyOf()
        FCoordinates = arr.copyOf()
        if (FourierCordinates_ == true)
            FHight = hight_ - 10
        else
            FHight = hight_
        FWeight = weight_
        FIsCoordinates = FourierCordinates_

        //start и finish это индексы отсчетов
        FStart = start_
        FFinish = finish_
        FourierChangeDot()
    }


    var FourieCanv = object : Canvas() {
        override
        fun paint(g: Graphics) {

            //FourierChangeDot()
            var x1 = 0
            if (FIsCoordinates == true) x1 = 50
            g.color = Color.BLUE
            var candleFilling: Int = ((FFinish - FStart) / FWeight).toInt() //((finish - start) / wight).toInt()
            //if (isCoordinates == true) candleFilling = ((finish - start) / (wight- 50)).toInt()

            if (candleFilling == 0) candleFilling = 1
            if (FisSmallVision == false) {
                for (i in FStart..FFinish - candleFilling step candleFilling) {
                    FourierArrDot.sort(i, candleFilling + i - 1)
                    if (x1 <= FWeight) {
                        if (FourierArrDot[i].toInt() >= this@SuperChannel.height) FourierArrDot[i] = FHight.toFloat() - 1
                        //if (arrDot[i].toInt() = hight) arrDot[i] = hight - 1
                        if (FIsCoordinates == true && x1 != 50) /**сюда я вставил кастыль + 1**/
                            g.drawLine(x1, FourierArrDot[i].toInt(), x1, FourierArrDot[candleFilling + i - 1].toInt())
                        if (i > FStart+1){
                            var min0 = 0
                            var max0 = 0
                            if (FourierArrDot[i - candleFilling].toInt() <= FourierArrDot[candleFilling + i - 1 - candleFilling].toInt()) {
                                min0 = FourierArrDot[i - candleFilling].toInt()
                                max0 = FourierArrDot[candleFilling + i - 1 - candleFilling].toInt()
                            } else {
                                max0 = FourierArrDot[i - candleFilling].toInt()
                                min0 = FourierArrDot[candleFilling + i - 1 - candleFilling].toInt()
                            }
                            var min1 = 0
                            var max1 = 0
                            if (FourierArrDot[i].toInt() <= FourierArrDot[candleFilling + i - 1].toInt()) {
                                min1 = FourierArrDot[i].toInt()
                                max1 = FourierArrDot[candleFilling + i - 1].toInt()
                            } else {
                                max1 = FourierArrDot[i].toInt()
                                min1 = FourierArrDot[candleFilling + i - 1].toInt()
                            }
                            if (max1 < min0) {
                                g.drawLine(x1 - 1, max1, x1, min0)
                            }
                            if (min1 > max0) {
                                g.drawLine(x1 - 1, min1, x1, max0)
                            }
                        }
                    } else {
                        println("график вылез за границу")
                    }
                    x1++
                }
            }
            else{
                var stepX = FWeight/(FFinish - FStart)
                for (i in FStart..FFinish - 1) {
                    //arrDot.sort(i, candleFilling + i - 1)
                    if (x1 <= FWeight) {
                        g.drawOval(x1, FourierArrDot[i].toInt(), 1, 4)
                        if (i > FStart){g.drawLine(x1, FourierArrDot[i].toInt(), x1-stepX.toInt(), FourierArrDot[i-1].toInt())}
                    } else {
                        println("график вылез за границу")
                    }
                    x1 += stepX.toInt()
                }
            }

            g.color = Color.BLACK
            val fm = g.fontMetrics
            g.drawString(sgn.channelsnames[channelNum], (FWeight/2).toInt(), 15)

            if (FIsCoordinates == true) {
                //FCoordinates = sgn.arraChannels[channelNum].copyOf()
                var start_ = FStart
                var finish_ = FFinish

                FCoordinates.sort(FStart, FFinish+1)
                start_ = FStart
                finish_ = FFinish//FCoordinates.size - 1


                g.color = Color.BLACK
                g.drawLine(0, FHight.toInt() - 2, FWeight.toInt(), FHight.toInt() - 2)
                g.drawLine(2, 2, 2, FHight.toInt())

                g.drawString(FCoordinates[start_].toString(), 5, FHight.toInt() - 5)
                g.drawString((FCoordinates[finish_]/4).toString(), 5, (FHight.toInt() / 4) * 3)
                g.drawString((FCoordinates[finish_]/4 * 2).toString(), 5, FHight.toInt() / 2)
                g.drawString((FCoordinates[finish_]/4 * 3).toString(), 5, FHight.toInt() / 4)
                g.drawString(FCoordinates[finish_].toString(), 5, 10)

                g.drawString(((sgn.samplingrate.toFloat()/FourierArrDot.size)*(FourierArrDot.size/10)).toString(), (FWeight/6).toInt() * 1 - 15, FHight.toInt()+10 )
                g.drawString(((sgn.samplingrate.toFloat()/FourierArrDot.size)*((FourierArrDot.size/10)*2)).toString(), (FWeight/6).toInt() * 2 - 15, FHight.toInt()+10 )
                g.drawString(((sgn.samplingrate.toFloat()/FourierArrDot.size)*((FourierArrDot.size/10)*3)).toString(), (FWeight/6).toInt() * 3 - 15, FHight.toInt()+10 )
                g.drawString(((sgn.samplingrate.toFloat()/FourierArrDot.size)*((FourierArrDot.size/10)*4)).toString(), (FWeight/6).toInt() * 4 - 15, FHight.toInt()+10 )
                g.drawString(((sgn.samplingrate.toFloat()/FourierArrDot.size)*((FourierArrDot.size/10)*5)).toString(), (FWeight/6).toInt() * 5 - 15, FHight.toInt() +10)

                g.color = Color.GRAY
                //g.drawLine(0, hight.toInt() - 5, wight.toInt(), hight.toInt() - 5)
                g.drawLine(5, FHight.toInt()/2, FWeight.toInt(), FHight.toInt()/2)
                g.drawLine(5, 2, FWeight.toInt(), 2)

                g.drawLine((FWeight/6).toInt() * 1, 2, (FWeight/6).toInt() * 1, FHight.toInt())
                g.drawLine((FWeight/6).toInt() * 2, 2, (FWeight/6).toInt() * 2, FHight.toInt())
                g.drawLine((FWeight/6).toInt() * 3, 2, (FWeight/6).toInt() * 3, FHight.toInt())
                g.drawLine((FWeight/6).toInt() * 4, 2, (FWeight/6).toInt() * 4, FHight.toInt())
                g.drawLine((FWeight/6).toInt() * 5, 2, (FWeight/6).toInt() * 5, FHight.toInt())
            }

            if (isPaintApproach == true){
                if (MouseEvent.BUTTON1_DOWN_MASK == 1){
                    println("nagata")

                }
            }

        }
    }


    fun GenLoad(isLoad: Boolean){
        load = isLoad
    }

    fun GenSpectCanv(a: Array<Array<Float>>, width_: Float, height_: Float, brith_: Int){
        brith = brith_
        load = false
        SpecWeight = width_.toInt()
        SpecHight = height_.toInt()
        SpectragramMatrix = a.copyOf()
        for (i in 0..pal.size - 1) {
            for (j in 0..pal[i].size - 1) {
                pal[i][j] = i
            }
        }

        var transLvlArr = Array<Float>(SpecWeight * SpecHight, {0f})
        var k = 0
        for (i in 0..SpectragramMatrix.size-1){
            for (j in 0..SpectragramMatrix[i].size-1){
                transLvlArr[k] = SpectragramMatrix[i][j]
                k++
            }
        }
        transLvlArr.sort()
        lvlArr[0] = transLvlArr[0]
        lvlArr[1] = transLvlArr[transLvlArr.size/5]
        lvlArr[2] = transLvlArr[(transLvlArr.size/5) * 2]
        lvlArr[3] = transLvlArr[(transLvlArr.size/5) * 3]
        lvlArr[4] = transLvlArr[(transLvlArr.size/5) * 4]
        lvlArr[5] = transLvlArr[transLvlArr.size-1]





        var max = SpectragramMatrix[0][0]
        var min = SpectragramMatrix[0][0]
        for (i in 0..SpectragramMatrix.size-1){
            for (j in 0..SpectragramMatrix[i].size-1){
                if(max < SpectragramMatrix[i][j])
                    max = SpectragramMatrix[i][j]
                if(min > SpectragramMatrix[i][j])
                    min = SpectragramMatrix[i][j]
            }
        }
        max -= min
        max = 254/max
        for (i in 0..SpectragramMatrix.size-1){
            for (j in 0..SpectragramMatrix[i].size-1) {
                SpectragramMatrix[i][j] -= min
                SpectragramMatrix[i][j] *= max
            }
        }
    }

    var SpectrogramCanv = object : Canvas() {
        override
        fun paint(g: Graphics) {
            if(load == false) {


                for (i in 0..SpectragramMatrix.size - 1) {
                    for (j in 0..SpectragramMatrix[i].size - 1) {
                        //g.color = Color(0, 0, SpectragramMatrix[i][j].toInt())
                        var L = 255
                        if ((SpectragramMatrix[i][j]/lvlArr[5]*brith*256).toInt() < 255){
                            L = (SpectragramMatrix[i][j]/lvlArr[5]*brith*256).toInt()
//                            g.color = Color(pal[L][0], pal[L][L], pal[L][2])
                        }
                        g.color = Color(pal[L][0], pal[L][1], pal[L][2])
//                        else {g.color = Color(pal[L][0], pal[L][L], pal[L][2])}
                        //g.drawLine(i , j, i, j)
                        g.drawOval(i, SpecHight - j, 1, 1)
                    }
                }

                /** отрисовка уровня **/
                var b: Float = 0f
                for (i in 0..SpecHight.toInt()) {
                    var go = SpecHight.toInt()
                    if (SpecHight > 254) go = 254
                    var coef: Float = (255 / SpecHight).toFloat()
                    g.drawLine(SpecWeight + 50, i, SpecWeight + 80, i)
                    g.color = Color(pal[b.toInt()][0], pal[b.toInt()][1], pal[b.toInt()][2])
                    b += coef
                }
                g.color = Color.BLACK
                g.drawString(lvlArr[0].toString(), SpecWeight + 1, 10)
                g.drawString(lvlArr[1].toString(), SpecWeight + 1, SpecHight / 5)
                g.drawString(lvlArr[2].toString(), SpecWeight + 1, (SpecHight / 5) * 2)
                g.drawString(lvlArr[3].toString(), SpecWeight + 1, (SpecHight / 5) * 3)
                g.drawString(lvlArr[4].toString(), SpecWeight + 1, (SpecHight / 5) * 4)
                g.drawString(lvlArr[5].toString(), SpecWeight + 1, SpecHight - 5)








                g.color = Color.WHITE
                val fm = g.fontMetrics
                g.drawString(sgn.channelsnames[channelNum], (FWeight / 2).toInt(), 15)
                //val font = g.font.deriveFont(50.0f)
                //g.font = font
                //g.drawString("loading", (width/2), (height/2))


            }else {
                g.color = Color(0, 0, 255)
                val fm = g.fontMetrics
                val font = g.font.deriveFont(50.0f)
                //g.font = font
                println(g.font)
                g.drawString("Loading...", (width / 2).toInt(), 15)
                g.font.deriveFont(13.0f)
            }

        }
    }
}