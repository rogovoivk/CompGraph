import java.awt.Canvas
import java.awt.Color
import java.awt.Graphics
import java.awt.RenderingHints
import kotlin.math.abs
import java.awt.Component
import java.awt.Container
import java.awt.Dimension
import java.awt.LayoutManager
import javafx.scene.control.*
import javafx.scene.Scene
import com.sun.javafx.robot.impl.FXRobotHelper.getChildren
import java.util.Collections.addAll
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.util.Optional
import javafx.scene.control.TextInputDialog
import com.jfoenix.controls.*
import javafx.stage.FileChooser
import javax.*
import javax.swing.*
import javax.swing.Timer;
import javax.swing.border.Border
import javax.swing.border.EmptyBorder
import javax.swing.event.InternalFrameEvent
import javax.swing.event.InternalFrameListener
import java.*
import java.awt.*
import java.awt.event.*
import java.beans.PropertyVetoException
import java.io.File

import javax.swing.JFileChooser;
import TestMDI.ItemWindow
import com.jfoenix.controls.JFXButton
import kotlin.math.abs
import javax.swing.JPanel
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener
import javax.swing.text.StyleConstants.getComponent
import java.awt.event.MouseAdapter
import javax.swing.JTextField
import javax.swing.JButton

class SuperChannel(sgn_: Signal, channelNum_: Int, wight_: Float, hight_: Float, start_: Int, finish_: Int){
    var sgn: Signal = sgn_
    var channelNum: Int = channelNum_
    var wight: Float = wight_
    var hight: Float = hight_
    var start: Int = start_
    var arrDot: Array<Float> = sgn.arraChannels[channelNum].copyOf()
    var finish: Int = finish_
    var cardinalityOnCanvas: Float = wight_
    var arrCandle = arrayOfNulls<Int>(wight.toInt() * 2)

    fun CreateCandleArr (cardinality: Int){
        ChangeDot()
        arrCandle = arrayOfNulls<Int>(cardinality * 2)
        var candleFilling: Int = (cardinality / wight).toInt()
        var cout = 0
        for (i in 0..arrDot.size-candleFilling step candleFilling) {
            arrDot.sort(i, candleFilling + i - 1)
            arrCandle[cout] = arrDot[i].toInt()
            arrCandle[cout+1] = arrDot[candleFilling + i].toInt()
            cout += 2
        }
        //arrDot[i].toInt(), x1, arrDot[candleFilling
    }

    fun ChangeDot(){
        arrDot = sgn.arraChannels[channelNum].copyOf()//.copy(sgn.arraChannels[channelNum])// = sgn.arraChannels[channelNum]
//        for (i in 0..sgn.arraChannels[channelNum].size-1){
//            arrDot[i] = sgn.arraChannels[channelNum][i]
//        }
        //if (finish > arrDot.size-1) {finish = arrDot.size-1}
        //if (start < arrDot.size-1) {finish = arrDot.size-1}
//        start = stert_
//        finish = finish_
        var max: Float = arrDot[1]
        var min: Float = arrDot[1]
        var top: Float = hight
        //var candleFilling: Int = 0
        var right: Float = 0f
        for (i in 0..arrDot.size-1) {
            if (max < arrDot[i]) {
                max = arrDot[i]
            }
            if (min > arrDot[i]) {
                min = arrDot[i]
            }
        }
        //var hightSpace = max - min

        if (min < 0 ){
            for (i in 0..arrDot.size-1) {
                arrDot[i] += abs(min)
            }
            max += abs(min)
            min = 0f

        }
        else {
            for (i in 0..arrDot.size-1) {
                arrDot[i] -= min
            }
            max -= min
            min = 0f
        }

//        if (max > top) {top = max/top}
//        else
//            if(max <= top) {top = 1f}
        top = max/top
        for (i in 0..arrDot.size-1){
            arrDot[i] /= top
            arrDot[i] = hight - arrDot[i]
        }
    }


    var canv = object : Canvas() {
        override
        fun paint(g: Graphics) {
            ChangeDot()
            var x1 = 0
            g.color = Color.BLUE
            for (i in 0..arrCandle.size step 2){
                g.drawLine(x1, arrCandle[i]!!, x1, arrCandle[i + 1]!!)
                x1++
            }

//            var candleFilling: Int = ((finish - start) / wight).toInt()
//
//
//
//            for (i in start..finish-candleFilling step candleFilling) {
//                arrDot.sort(i, candleFilling + i - 1)
//                g.drawLine(x1, arrDot[i].toInt(), x1, arrDot[candleFilling + i - 1].toInt() )
//                x1++
//            }


//            var cout = 0
//            var max = 0f
//            var min = 100f
//            for (i in start..finish){
//                if (cout == candleFilling){
//                    g.drawLine(x1, min.toInt(), x1, max.toInt())
//                    x1++
//                    max = 0f
//                    min = 100f
//                    cout = 0
//                }
//                else{
//                    cout++
//                    if (arrDot[i] > max) {max = arrDot[i]}
//                    if (arrDot[i] < min) {min = arrDot[i]}
//                }
//            }

        }
    }
}