
import java.awt.Canvas
import java.awt.Color
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.ArrayList
import javax.swing.JButton
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.JScrollBar

class MosL(channel: ArrayList<SuperChannel>, scBar_: JScrollBar, scbParamArray_: Array<Int>, indexInList: Int, changeBut_: JButton) : MouseAdapter() {
    var changeBut = changeBut_
    //var arrDot: Array<Float> = arrDot_
    var index = indexInList
    var scbParamArray = scbParamArray_
    var scBar = scBar_
    var chan = channel
    internal var sx = 0
    internal var sy = 0
    internal var onDrag = false


    class PopUpDemo(channel: SuperChannel, chanArr: ArrayList<SuperChannel>) : JPopupMenu() {
        var oscillogramItem: JMenuItem

        init {
            oscillogramItem = JMenuItem("Удалить канал")
            oscillogramItem.addActionListener {
                //var channel_ = SuperChannel(channel.sgn, channel.channelNum, channel.wight, channel.hight, channel.start, channel.finish)
                //oscillogramList.add(SuperChannel(channel.sgn, channel.channelNum, 600f, 300f, channel.start, channel.finish)) // надо потом поменять константные параметры константа - размер канваса
                chanArr.remove(channel)
                //createOscilogram()
            }
            add(oscillogramItem)
        }
    }


    override
    fun mouseDragged(e: MouseEvent) {
        if (e.isPopupTrigger) {
            doPop(e)
        }
        else{
            if (onDrag) {
                val x = e.getX()
                val y = e.getY()

                val comp = e.getSource() as Canvas
                val g = comp.getGraphics()
                g.color = Color.RED
                // comp.repaint(); << for cleaning up the intermediate lines : doesnt work :(
                /**  отрисовываем по контурам  **/
                var candleFilling: Int = ((chan[index].finish - chan[index].start) / chan[index].wight).toInt()



                var x1 = 0
                if (chan[index].isCoordinates == true) {x1 = 34}  //30 - 35
                if (candleFilling == 0) candleFilling = 1
                if (chan[index].isSmallVision == false) {
                    for (i in chan[index].start..chan[index].finish - candleFilling step candleFilling) {
                        chan[index].arrDot.sort(i, candleFilling + i - 1)
                        if ((x1 <= chan[index].wight) and (x1 == x)) {
                            g.drawLine(x1, chan[index].arrDot[i].toInt() - 5, x1, chan[index].arrDot[candleFilling + i - 1].toInt() + 5 )
                            g.drawLine(x1+1, chan[index].arrDot[i].toInt() - 5, x1 +1, chan[index].arrDot[candleFilling + i - 1].toInt() + 5)
                            g.drawLine(x1-1, chan[index].arrDot[i].toInt() - 5, x1 -1, chan[index].arrDot[candleFilling + i - 1].toInt() + 5)
                        } else {
                            println("график вылез за границу")
                        }
                        x1++
                    }
                }
                else{
                    g.drawLine(x, 0, x, 200)
                }
                //g.drawLine(x, chan[index].start + x*)
                //g.drawLine(x, 0, x, 200)
                return
            }
            onDrag = true
            sx = e.getX()
            sy = e.getY()

            for (i in 0..chan.size - 1) {
                chan[i].PaintStart = sx
            }
            println("Draggg  " + sx + "  " + sy)
        }
    }

    override
    fun mousePressed(e: MouseEvent) {
        println("Pressed")
    }

    override
    fun mouseReleased(e: MouseEvent) {
        /** вариант 1**/
//            println("Released  " + e.getX() + "  " + e.getY())
//            for (i in 0..chan.size - 1) {
//                chan[i].PaintFinish = e.getX()
//                chan[i].ChangePainDot()
//                chan[i].canv.repaint()
//                chan[i].canv.paint(chan[i].canv.graphics)
//
//                var fix = (chan[i].sgn.samplesnumber / chan[i].wight)
//                //finish = (start + (PaintFinish * fix)).toInt()
//                //start = (start + (PaintStart * fix)).toInt()
//                scbParamArray[0] = (chan[i].finish - chan[i].start) / 2
//                scbParamArray[1] = chan[0].arrDot.size - 1 - scbParamArray[0]
//                scbParamArray[2] = chan[i].finish - (chan[i].finish - chan[i].start) / 2
//                scBar.setValues(scbParamArray[2], 2 * scbParamArray[0], scbParamArray[0], scbParamArray[1])
//                scBar.isVisible = true
//
//            }
//            if (onDrag)
//                onDrag = false


        /** вариант 2**/
        for (i in 0..chan.size - 1) {
            chan[i].PaintFinish = e.getX()
            chan[i].ChangePainDot()
        }
        changeBut.doClick()


    }

    private fun doPop(e: MouseEvent) {
        val menu = PopUpDemo(chan[index], chan)
        menu.show(e.component, e.x, e.y)
    }
}