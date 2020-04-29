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
import java.awt.Canvas

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
import jdk.nashorn.internal.runtime.GlobalFunctions.anonymous
import java.io.ObjectInputStream
import java.util.*
import javax.swing.JOptionPane
import javax.swing.JLabel
import javax.swing.JComponent
import javax.swing.JPasswordField



//import TestMDI.ItemWindow




/**
 * Created by IntelliJ IDEA.
 * User: DanilovAF
 * Date: 12.01.11
 * Time: 13:50
 * MDI приложение Фишка в том, что при максимизации окна управление переходит в menuBar
 * Не анализируется закрываемое это окно или нет. Важен сам принцип
 */
class TestMDI : JFrame() {
    private var descPan: JDesktopPane = JDesktopPane()
    private val menuBar: JMenuBar

    internal var buttClose = JButton("\uf072")
    internal var buttRestore = JButton("\uf032")
    internal var menuGlue = Box.createHorizontalGlue()

    fun LoadChannal (): File{ //String{
//        val fileChooser = JFileChooser()
//        fileChooser.setCurrentDirectory(File(System.getProperty("user.home")));
//        val result = fileChooser.showOpenDialog(parent)
//        val selectedFile: File = fileChooser.selectedFile
        var inputString: String = String()
        val fileChooser = JFileChooser()
        fileChooser.currentDirectory = File(System.getProperty("user.home"))
        val result = fileChooser.showOpenDialog(this)
        val selectedFile = fileChooser.selectedFile
        if (result == JFileChooser.APPROVE_OPTION) {
            val selectedFile = fileChooser.selectedFile
            println("Selected file: " + selectedFile.absolutePath)
            inputString = selectedFile.readText()
            //println(inputString)
        }
        //return inputString
        return  selectedFile

    }


    /**
     * Свой класс MDI окна для добавления свойств
     */
    internal inner class ItemWindow
    /***
     * Добавим стандартный конструктор
     */
        (title: String, resizable: Boolean, closable: Boolean, maximizable: Boolean, iconifiable: Boolean) :
        JInternalFrame(title, resizable, closable, maximizable, iconifiable) {
        var bakBorder: Border? = null        // Сохранить предыдущий бордер, чтобы его можно было поменять
    }

    /**
     * Функция в которой проходит весь анализ надо ли добавить в текущее JMenuBar кнопки управления активным окном
     * @param ob само окно
     */
    private fun UpdateWindowsControl(ob: ItemWindow) {
        //		ItemWindow ob = (ItemWindow) e.getComponent();
        if (ob.isMaximum) {
            if (ob.bakBorder == null) {   // Окно максимизировано, но заголовок не спрятан
                ob.bakBorder = ob.border
                val rec2 = ob.rootPane.bounds    // Получим то, что занимает MDI окно
                val iH = (rec2.getY() - rec2.getX()).toInt()     // уберем из высотызаголовка рамку окна
                ob.border = EmptyBorder(-iH, 0, 0, 0)    // закинем заголовок окна вверх, чтобы его не было видно

                menuBar.add(menuGlue)
                menuBar.add(buttRestore)
                menuBar.add(buttClose)
                menuBar.repaint()
            } else {   // Проверка на то, что если окно активно - надо добавить кнопки, иначе удалить кнопки управления окном
                if (!ob.isSelected) {   // Убрать из меню свое управление
                    menuBar.remove(buttClose)
                    menuBar.remove(buttRestore)
                    menuBar.remove(menuGlue)
                    menuBar.repaint()
                } else {   // Добавить в меню свое управление
                    menuBar.add(menuGlue)
                    menuBar.add(buttRestore)
                    menuBar.add(buttClose)
                    menuBar.repaint()
                }
            }
        } else {
            if (ob.bakBorder != null) {   // Уберем из меню кнопки управления окном, если эти кнопки там есть, судим об этом по непустому bakBorder
                ob.border = ob.bakBorder // Вернем старый бордер
                ob.bakBorder = null
                menuBar.remove(buttClose)
                menuBar.remove(buttRestore)
                menuBar.remove(menuGlue)
                menuBar.repaint()
            }
        }
    }

    /**
     * Слушатель событий трансформации размеров MDI окна
     */
    internal inner class MDIResizeListener : ComponentListener {
        override fun componentResized(e: ComponentEvent) {
            UpdateWindowsControl(e.component as ItemWindow)
        }

        override fun componentMoved(e: ComponentEvent) {}
        override fun componentShown(e: ComponentEvent) {}
        override fun componentHidden(e: ComponentEvent) {}
    }

    /**
     * Слушатель событий какое окно активно
     */
    private inner class MDIInternalFrameListener : InternalFrameListener {
        override fun internalFrameOpened(e: InternalFrameEvent) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        override fun internalFrameClosing(e: InternalFrameEvent) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        override fun internalFrameClosed(e: InternalFrameEvent) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        override fun internalFrameIconified(e: InternalFrameEvent) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        override fun internalFrameDeiconified(e: InternalFrameEvent) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        override fun internalFrameActivated(e: InternalFrameEvent) {
            UpdateWindowsControl(e.internalFrame as ItemWindow)
        }

        override fun internalFrameDeactivated(e: InternalFrameEvent) {
            UpdateWindowsControl(e.internalFrame as ItemWindow)
        }
    }

    init {   // Создадим меню приложения
        var oscillogramList: ArrayList <SuperChannel> = ArrayList()
        lateinit var oscilogramWind: ItemWindow
        /**тут описываю окно осциллограмм */

        fun createOscilogram() {
            /**PopUpListener begin*/
            class PopUpDemo(channel: SuperChannel) : JPopupMenu() {
                var oscillogramItem: JMenuItem

                init {
                    oscillogramItem = JMenuItem("Удалить канал")
                    oscillogramItem.addActionListener {
                        //var channel_ = SuperChannel(channel.sgn, channel.channelNum, channel.wight, channel.hight, channel.start, channel.finish)
                        //oscillogramList.add(SuperChannel(channel.sgn, channel.channelNum, 600f, 300f, channel.start, channel.finish)) // надо потом поменять константные параметры константа - размер канваса
                        oscillogramList.remove(channel)
                        createOscilogram()
                    }
                    add(oscillogramItem)
                }
            }

            class PopClickListener(channel_: SuperChannel) : MouseAdapter() {
                var channel: SuperChannel = channel_

                override
                fun mousePressed(e: MouseEvent) {
                    if (e.isPopupTrigger)
                        doPop(e)
                }

                override
                fun mouseReleased(e: MouseEvent) {
                    if (e.isPopupTrigger)
                        doPop(e)
                }

                private fun doPop(e: MouseEvent) {
                    val menu = PopUpDemo(channel)
                    menu.show(e.component, e.x, e.y)
                }
            }
            /**PopUpListener end*/

            var oscillogramContents = JPanel(VerticalLayout())
            try {
                //println(oscilogramWind.isClosed)
                oscilogramWind.setContentPane(oscillogramContents)
                if (oscilogramWind.isClosed){
                    oscilogramWind = ItemWindow("Осциллограммы", true, true, true, true)
                    oscilogramWind.setBounds(25, 25, 650, 450)
                    oscilogramWind.addInternalFrameListener(MDIInternalFrameListener())
                    oscilogramWind.addComponentListener(MDIResizeListener())
                    oscilogramWind.setContentPane(oscillogramContents)
                    descPan.add(oscilogramWind)
                    oscilogramWind.isVisible = true
                }
            } catch (e: UninitializedPropertyAccessException) { //я знаю, что тут один и тот же код, мне похуй, так лучше!!!
                //var sc = SuperChannel(sgn, channel.channelNum , 600f, 300f, 0, sgn.samplesnumber-1)
                oscilogramWind = ItemWindow("Осциллограммы", true, true, true, true)
                oscilogramWind.setBounds(25, 25, 650, 450)
                oscilogramWind.addInternalFrameListener(MDIInternalFrameListener())
                oscilogramWind.addComponentListener(MDIResizeListener())
                oscilogramWind.setContentPane(oscillogramContents)
                descPan.add(oscilogramWind)
                oscilogramWind.isVisible = true

            }
            for (i in 0..oscillogramList.size - 1) {
                var scbMin = 0
                var scbMax = 0
                var scbNewValue = 0
//                var scbF = Int
//                var scbL = Int

                println(oscillogramList[i].channelNum)
                oscillogramList[i].hight = 300f
                oscillogramList[i].wight = 600f
                //oscillogramList[i].ChangeDot(0, oscillogramList[i].arrDot.size - 1)
                oscillogramList[i].start = 0
                oscillogramList[i].finish = oscillogramList[i].sgn.samplesnumber


                /**ScrollBar  */
                var scBar: JScrollBar = JScrollBar()
                scBar.setValues(0, 20, 0, 100) //начало, жористость, старт, фин
                scBar.preferredSize = Dimension(600, 20)
                scBar.orientation = JScrollBar.HORIZONTAL
                var winListener: AdjustmentListener = AdjustmentListener {
                    println(scBar.value)
                    //oscillogramList[i].canv.repaint()
                    oscillogramList[i].start = scBar.value - scbMin
                    oscillogramList[i].finish = scBar.value + scbMin
                    //oscillogramList[i].ChangeDot(scBar.value - scbMin, scBar.value + scbMin)
                    oscillogramList[i].canv.repaint()
                    //oscillogramList[i].canv.paint(oscillogramList[i].canv.graphics)
                }
                scBar.addAdjustmentListener(winListener)
                scBar.isVisible = false
                //oscillogramContents.addWindowListener(winListener)

                /**Button  */
                var changeBut: JButton = JButton("Изменить")
                changeBut.addActionListener({
                    val first = JTextField(oscillogramList[i].start.toString())
                    val last = JTextField(oscillogramList[i].finish.toString())
                    val inputs = arrayOf<JComponent>(JLabel("От"), first, JLabel("До"), last)
                    val result =
                        JOptionPane.showConfirmDialog(null, inputs, "My custom dialog", JOptionPane.PLAIN_MESSAGE)
                    if (result == JOptionPane.OK_OPTION) {
                        //oscillogramList[i].ChangeDot(first.text.toInt(), last.text.toInt())
                        oscillogramList[i].start = first.text.toInt()
                        oscillogramList[i].finish = last.text.toInt()
                        //oscillogramList[i].canv.repaint()
                        //oscillogramList[i].canv.paint(oscillogramList[i].canv.graphics)
                        scbMin = (last.text.toInt() - first.text.toInt())/2
                        scbMax = oscillogramList[i].arrDot.size-1 - scbMin
                        scbNewValue = last.text.toInt() - (last.text.toInt() - first.text.toInt())/2
                        scBar.setValues(scbNewValue, 2*scbMin, scbMin, scbMax)
                        scBar.isVisible = true
                    } else {
                        println("User canceled / closed the dialog, result = $result")
                    } })
                oscillogramContents.add(changeBut)

                /**Canvas and add comp  */
                oscillogramList[i].canv.preferredSize = Dimension(600, 200)
                //var oscilloCanvas: Canvas = oscillogramList[i].canv
                oscillogramList[i].canv.addMouseListener(PopClickListener(oscillogramList[i]))
                oscillogramContents.add(oscillogramList[i].canv)
                oscillogramContents.add(scBar)
//                oscillogramContents.add(oscillogramList[i].canv)
            }


            //oscilogramWind.paint(oscilogramWind.graphics)
        }

        menuBar = JMenuBar()
        val fileMenu = JMenu("Файл")
        val newFrame = JMenuItem("new MDI")
        val loadSignal = JMenuItem("Загрузить сигнал")
        val oscillogramOpen = JMenuItem("Открыть осцилограммы")
        newFrame.addActionListener {
            val internalFrame = ItemWindow("Can Do All", true, true, true, true)
            internalFrame.setBounds(25, 25, 200, 100)
            internalFrame.addInternalFrameListener(MDIInternalFrameListener())
            internalFrame.addComponentListener(MDIResizeListener())

            //internalFrame.add(JButton("Проба пера"))

            descPan.add(internalFrame)
            internalFrame.isVisible = true
        }
        oscillogramOpen.addActionListener {
            createOscilogram()
        }
        loadSignal.addActionListener {
            val channelFile = LoadChannal()
            var sgn: Signal = FileToSignal(channelFile)
            var internalFrame = ItemWindow("Сигналы", true, true, false, false)
            internalFrame.setBounds(25, 25, 200, 700) //width = 200
            internalFrame.addInternalFrameListener(MDIInternalFrameListener())
            internalFrame.addComponentListener(MDIResizeListener())

            var arrChannel_ = arrayOfNulls<SuperChannel>(sgn.channels)
            var arrChannel = Array(sgn.channels, {SuperChannel(sgn, 0, 200f, 100f, 0, sgn.samplesnumber-1)})
            //lateinit var arrChannel: Array<SuperChannel>
            for (i in 0..sgn.channels-1)
            {
                arrChannel[i] = SuperChannel(sgn, i, 200f, 100f, 0, sgn.samplesnumber-1)
                //arrChannel[i].ChangeDot(0, sgn.samplesnumber-1)
            }

            //////тут поп ап меню листнер
            class PopUpDemo(channel: SuperChannel) : JPopupMenu() {
                var oscillogramItem: JMenuItem
                init {
                    oscillogramItem = JMenuItem("Осцилограмма")
                    oscillogramItem.addActionListener {
                        //var channel_ = SuperChannel(channel.sgn, channel.channelNum, channel.wight, channel.hight, channel.start, channel.finish)
                        oscillogramList.add(SuperChannel(channel.sgn, channel.channelNum, 600f, 300f, channel.start, channel.finish)) // надо потом поменять константные параметры константа - размер канваса
                        createOscilogram()
                    }
                    add(oscillogramItem)
                }

                var anItem: JMenuItem
                init {
                    anItem = JMenuItem("Информация о сигнале")
                    anItem.addActionListener {
                        println("нажата - информация о сигнале")
                        val infAboutInternalFrame = ItemWindow("Channels", true, true, true, false)
                        infAboutInternalFrame.setBounds(25, 25, 200, 300) //width = 200
                        infAboutInternalFrame.addInternalFrameListener(MDIInternalFrameListener())
                        infAboutInternalFrame.addComponentListener(MDIResizeListener())
                        val contents = JPanel(VerticalLayout())
                        // Добавим кнопки и текстовое поле в панель
                        contents.add(JTextField("Общее число каналов - " + sgn.channels))
                        contents.add(JTextField("Общее количество отсчетов - " + sgn.samplesnumber))
                        contents.add(JTextField("Частота дискретизации - " + sgn.samplingrate))
                        contents.add(JTextField("Дата и время начала записи - " + sgn.startdate))
                        contents.add(JTextField("Дата и время окончания записи - " + sgn.starttime))
                        for (i in 0..sgn.channels-1){contents.add(JTextField("Канал - " + sgn.channelsnames[i] + "- Источник -" + sgn.from))}

                        // Размещаем панель в контейнере
                        infAboutInternalFrame.setContentPane(contents)
                        // Открываем окно
                        infAboutInternalFrame.isVisible = true
                        println("закончилась - информация о сигнале")
                        descPan.add(infAboutInternalFrame)
                        infAboutInternalFrame.topLevelAncestor
                    }
                    add(anItem)
                }
            }
            class PopClickListener(channel_: SuperChannel) : MouseAdapter() {
                var channel: SuperChannel = channel_

                override
                fun mousePressed(e: MouseEvent) {
                    if (e.isPopupTrigger)
                        doPop(e)
                }
                override
                fun mouseReleased(e: MouseEvent) {
                    if (e.isPopupTrigger)
                        doPop(e)
                }

                private fun doPop(e: MouseEvent) {
                    val menu = PopUpDemo(channel)
                    menu.show(e.component, e.x, e.y)
                }
            }

// Then on your component(s)
            val contents = JPanel(VerticalLayout())
            for (i in 0..sgn.channels-1){
                arrChannel[i].canv.preferredSize = Dimension(200, 100)
                contents.add(arrChannel[i]?.canv)
                arrChannel[i].canv.addMouseListener(PopClickListener(arrChannel[i]))
            }
            contents.mouseListeners
            internalFrame.contentPane = contents

            descPan.add(internalFrame)
            internalFrame.size = Dimension(230, 700)
            internalFrame.isVisible = true
            internalFrame.setLocation(1150,1)
        }

        fileMenu.add(loadSignal) //newFrame,
        fileMenu.add(newFrame)
        fileMenu.add(oscillogramOpen)
        menuBar.add(fileMenu)

        jMenuBar = menuBar


        run {
            // Инициализация кнопок, которые будкт помещаться в меню для управления MDI окном в развернутом виде
            // Делается один раз, чтобы кнопки не создавать  - они одинаковы для всех MDI окон
            buttRestore.isBorderPainted = false
            buttRestore.isFocusPainted = false
            var iSize = buttRestore.font.size
            var iStyle = buttRestore.font.style
            buttRestore.font = Font("Webdings", iStyle, iSize)
            buttRestore.margin = Insets(1, 1, 1, 1)
            buttRestore.addActionListener {
                // Получить текущее окно и скомандовать ему Restore
                try {
                    descPan.selectedFrame.isMaximum = false
                } catch (e1: PropertyVetoException) {
                    e1.printStackTrace()  //To change body of catch statement use File | Settings | File Templates.
                }
            }


            buttClose.isBorderPainted = false
            buttClose.isFocusPainted = false
            iSize = buttClose.font.size
            iStyle = buttClose.font.style
            buttClose.font = Font("Webdings", iStyle, iSize)
            buttClose.margin = Insets(1, 1, 1, 1)
            buttClose.addActionListener {
                // Получить текущее окно и скомандовать ему закрытие
                descPan.desktopManager.closeFrame(descPan.selectedFrame)
            }
        }

        defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        descPan = JDesktopPane()

        contentPane.add(descPan, "Center")
        setSize(500, 300)
        descPan.desktopManager = DefaultDesktopManager()
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel")
            } catch (e: Exception) {
                e.printStackTrace()
            }

            java.awt.EventQueue.invokeLater {
                val frm = TestMDI()
                frm.setSize(1400, 800)
                frm.isVisible = true
                //frm.pack();
            }
        }
    }
}
