


import javafx.scene.input.MouseDragEvent
import kotlin.math.abs
import sun.swing.SwingUtilities2.stringWidth
import java.awt.*
import java.awt.Font.ITALIC
import java.awt.Font.BOLD
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener


class SuperChannel(sgn_: Signal, channelNum_: Int, wight_: Float, hight_: Float, start_: Int, finish_: Int, isCoordinates_: Boolean = false ){
    var sgn: Signal = sgn_
    var channelNum: Int = channelNum_
    var wight: Float = wight_
    var hight: Float = hight_
    var start: Int = start_
    var arrDot: Array<Float> = sgn.arraChannels[channelNum].copyOf()
    var finish: Int = finish_
    var cardinalityOnCanvas: Float = wight_
    var isCoordinates = isCoordinates_
    var isPaintApproach = true
    var LocalMaxMin = false

    var isSmallVision = false

    var isPaint = false
    var PaintStart = 0
    var PaintFinish = 0


    fun ChangePainDot(){
        if ( PaintStart > PaintFinish){
            var swap = PaintStart
            PaintStart = PaintFinish
            PaintFinish = swap
        }

        var fix = (sgn.samplesnumber / wight)
        finish = (start + (PaintFinish * fix)).toInt()
        start = (start + (PaintStart * fix)).toInt()
    }



    fun ChangeDot(){

        if((finish - start) < wight){
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
        var top: Float = hight
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
            arrDot[i] = hight - arrDot[i]
        }
    }


    var canv = object : Canvas() {
        override
        fun paint(g: Graphics) {
            ChangeDot()
            var x1 = 0
            if (isCoordinates == true) x1 = 50
            g.color = Color.BLUE
            var candleFilling: Int = ((finish - start) / wight).toInt()
            if (isCoordinates == true) candleFilling = ((finish - start) / (wight- 50)).toInt()



            if (candleFilling == 0) candleFilling = 1
            if (isSmallVision == false) {
                for (i in start..finish - candleFilling step candleFilling) {
                    arrDot.sort(i, candleFilling + i - 1)
                    if (x1 <= wight) {
                        g.drawLine(x1, arrDot[i].toInt(), x1, arrDot[candleFilling + i - 1].toInt())
                    } else {
                        println("график вылез за границу")
                    }
                    x1++
                }
            }
            else{
                var stepX = wight/(finish - start)
                for (i in start..finish - 1) {
                    //arrDot.sort(i, candleFilling + i - 1)
                    if (x <= wight) {
                        g.drawOval(x1, arrDot[i].toInt(), 1, 4)
                        if (i > 0){g.drawLine(x1, arrDot[i].toInt(), x1-stepX.toInt(), arrDot[i-1].toInt())}
                    } else {
                        println("график вылез за границу")
                    }
                    x1 += stepX.toInt()
                }
            }

            g.color = Color.BLACK
            val fm = g.fontMetrics
            g.drawString(sgn.channelsnames[channelNum], (wight/2).toInt(), 15)


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
                g.drawLine(0, hight.toInt() - 2, wight.toInt(), hight.toInt() - 2)
                g.drawLine(2, 2, 2, hight.toInt())

                g.drawString(coordinates[start_].toString(), 5, hight_.toInt() - 5)
                g.drawString(coordinates[start_ + (finish_ - start_) / 4].toString(), 5, (hight_.toInt() / 4) * 3)
                g.drawString(coordinates[start_ + (finish_ - start_) / 2].toString(), 5, hight_.toInt() / 2)
                g.drawString(coordinates[start_ + ((finish_ - start_) / 4) * 3].toString(), 5, hight_.toInt() / 4)
                g.drawString(coordinates[finish_ - 1].toString(), 5, 10)

                g.drawString(sgn.WhatTime((finish - start)/6 * 1 + start, sgn.samplingrate.toFloat()), (wight/6).toInt() * 1 - 15, hight.toInt() + 15)
                g.drawString(sgn.WhatTime((finish - start)/6 * 2 + start,sgn.samplingrate.toFloat()), (wight/6).toInt() * 2 - 15, hight.toInt() + 15)
                g.drawString(sgn.WhatTime((finish - start)/6 * 3 + start, sgn.samplingrate.toFloat()), (wight/6).toInt() * 3 - 15, hight.toInt() + 15)
                g.drawString(sgn.WhatTime((finish - start)/6 * 4 + start, sgn.samplingrate.toFloat()), (wight/6).toInt() * 4 - 15, hight.toInt() + 15)
                g.drawString(sgn.WhatTime((finish - start)/6 * 5 + start, sgn.samplingrate.toFloat()), (wight/6).toInt() * 5 - 15, hight.toInt() + 15)

                g.color = Color.GRAY
                //g.drawLine(0, hight.toInt() - 5, wight.toInt(), hight.toInt() - 5)
                g.drawLine(5, hight.toInt()/2, wight.toInt(), hight.toInt()/2)
                g.drawLine(5, 2, wight.toInt(), 2)

                g.drawLine((wight/6).toInt() * 1, 2, (wight/6).toInt() * 1, hight.toInt())
                g.drawLine((wight/6).toInt() * 2, 2, (wight/6).toInt() * 2, hight.toInt())
                g.drawLine((wight/6).toInt() * 3, 2, (wight/6).toInt() * 3, hight.toInt())
                g.drawLine((wight/6).toInt() * 4, 2, (wight/6).toInt() * 4, hight.toInt())
                g.drawLine((wight/6).toInt() * 5, 2, (wight/6).toInt() * 5, hight.toInt())
            }

            if (isPaintApproach == true){
                if (MouseEvent.BUTTON1_DOWN_MASK == 1){
                    println("nagata")

                }
            }

        }
    }



}