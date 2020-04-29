


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
import com.sun.org.apache.xml.internal.security.Init
import javax.swing.text.StyleConstants.getComponent
import java.awt.event.MouseAdapter
import javax.swing.JTextField
import javax.swing.JButton
import jdk.nashorn.internal.runtime.GlobalFunctions.anonymous
import java.io.FileFilter
import java.io.ObjectInputStream
import java.util.*
import javax.swing.JOptionPane
import javax.swing.JLabel
import javax.swing.JComponent
import javax.swing.JPasswordField
import javax.swing.filechooser.FileNameExtensionFilter
//import javafx.scene.input.MouseEvent


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

    var WhereWasFile = File(System.getProperty("user.home"))
    fun LoadChannal (): File{ //String{
//        val fileChooser = JFileChooser()
//        fileChooser.setCurrentDirectory(File(System.getProperty("user.home")));
//        val result = fileChooser.showOpenDialog(parent)
//        val selectedFile: File = fileChooser.selectedFile
        var inputString: String = String()
        val fileChooser = JFileChooser()
        fileChooser.currentDirectory = WhereWasFile
        fileChooser.setFileFilter(FileNameExtensionFilter("Text files", "txt"))
        val result = fileChooser.showOpenDialog(this)
        val selectedFile = fileChooser.selectedFile
        if (result == JFileChooser.APPROVE_OPTION) {
            WhereWasFile = fileChooser.currentDirectory
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
        /**тут описываю окно моделирования */
        lateinit var modelWind: ItemWindow
        var heightOscillogrammGraph = 200

        fun CreateModelWindow(v: String) {
            var modelContents = JPanel(VerticalLayout())
            modelWind = ItemWindow("Модели", true, true, true, false)
            modelWind.setBounds(25, 25, 700, 450)
            modelWind.addInternalFrameListener(MDIInternalFrameListener())
            modelWind.addComponentListener(MDIResizeListener())

            var sgn: Signal = InitModel(v)
            var ch: SuperChannel = SuperChannel(sgn, 0, 600f, 200f, 0, 600, false)
            ch.canv.preferredSize = Dimension(modelWind.width - 50, 200)
            modelContents.add(ch.canv)

            modelWind.setContentPane(modelContents)
            descPan.add(modelWind)
            modelWind.isVisible = true
        }

        /**тут описываю окно осциллограмм */
        var oscillogramList: ArrayList<SuperChannel> = ArrayList()
        lateinit var oscilogramWind: ItemWindow
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
                if (oscilogramWind.isClosed) {
                    oscilogramWind = ItemWindow("Осциллограммы", true, true, true, true)
                    oscilogramWind.setBounds(25, 25, 700, 450)
                    oscilogramWind.addInternalFrameListener(MDIInternalFrameListener())
                    oscilogramWind.addComponentListener(MDIResizeListener())
                    oscilogramWind.setContentPane(oscillogramContents)
                    descPan.add(oscilogramWind)
                    oscilogramWind.isVisible = true
                }
            } catch (e: UninitializedPropertyAccessException) { //я знаю, что тут один и тот же код, мне похуй, так лучше!!!
                //var sc = SuperChannel(sgn, channel.channelNum , 600f, 300f, 0, sgn.samplesnumber-1)
                oscilogramWind = ItemWindow("Осциллограммы", true, true, true, true)
                oscilogramWind.setBounds(25, 25, 700, 450)
                oscilogramWind.addInternalFrameListener(MDIInternalFrameListener())
                oscilogramWind.addComponentListener(MDIResizeListener())
                oscilogramWind.setContentPane(oscillogramContents)
                descPan.add(oscilogramWind)
                oscilogramWind.isVisible = true

            }
            var refreshBut: JButton = JButton("Обновить графики")
            refreshBut.addActionListener{ createOscilogram() }
            oscillogramContents.add(refreshBut)

            var scBar: JScrollBar = JScrollBar()

//            var scbMin = 0
//            var scbMax = 0
//            var scbNewValue = 0
            var scbParamArray = arrayOf(0, 0, 0) // [0] - scbMin; [1] - scbMax; [2] - scbNewValue

            var changeBut: JButton = JButton("Параметры")
            scBar.setValues(0, 20, 0, 100) //начало, жористость, старт, фин
            scBar.preferredSize = Dimension(oscilogramWind.width - 50, 20)
            oscillogramContents.add(changeBut)
            for (i in 0..oscillogramList.size - 1) {

                println(oscillogramList[i].channelNum)
                oscillogramList[i].hight = heightOscillogrammGraph.toFloat()
                //oscillogramList[i].wight = 600f
                //oscillogramList[i].ChangeDot(0, oscillogramList[i].arrDot.size - 1)
                oscillogramList[i].start = oscillogramList[0].start
                oscillogramList[i].finish = oscillogramList[0].finish

                /**Canvas and add comp  */
                //oscillogramList[i].canv.preferredSize = Dimension(600, 200)
                oscillogramList[i].canv.preferredSize = Dimension(oscilogramWind.width, heightOscillogrammGraph + 20)
                oscillogramList[i].wight = (oscilogramWind.width).toFloat()
                //var oscilloCanvas: Canvas = oscillogramList[i].canv
                oscillogramList[i].canv.addMouseListener(PopClickListener(oscillogramList[i]))
                oscillogramContents.add(oscillogramList[i].canv)
                oscillogramContents.add(scBar)

                if (oscillogramList[i].isPaint == true) {
                    var ml = MosL(oscillogramList, scBar, scbParamArray, i, changeBut)
                    oscillogramList[i].canv.addMouseListener(ml)
                    oscillogramList[i].canv.addMouseMotionListener(ml)
                }

//                oscillogramContents.add(oscillogramList[i].canv)
                //oscilogramWind.height = oscillogramList.size * 250
                oscilogramWind.setBounds(25, 25, oscilogramWind.width, oscillogramList.size * 220 + 150)
            }

            /**ScrollBar  */
            //var scBar: JScrollBar = JScrollBar()
//            scBar.setValues(0, 20, 0, 100) //начало, жористость, старт, фин
//            scBar.preferredSize = Dimension(oscilogramWind.width - 50, 20)
            scBar.orientation = JScrollBar.HORIZONTAL
            var winListener: AdjustmentListener = AdjustmentListener {
                println(" " + scBar.value + "  min:" + scbParamArray[0]+ "  max:" + (oscillogramList[0].arrDot.size - 1 -scbParamArray[0]))
                for (i in 0..oscillogramList.size - 1) {
                    //oscillogramList[i].canv.repaint()
                    oscillogramList[i].start = scBar.value - scbParamArray[0]
                    oscillogramList[i].finish = scBar.value + scbParamArray[0]
                    //oscillogramList[i].ChangeDot(scBar.value - scbMin, scBar.value + scbMin)
                    oscillogramList[i].canv.repaint()
                    //oscillogramList[i].canv.paint(oscillogramList[i].canv.graphics)
                }
            }
            scBar.addAdjustmentListener(winListener)
            if ((oscillogramList[0].start == 0) or (oscillogramList[0].finish == oscillogramList[0].sgn.samplesnumber-1))
            scBar.isVisible = false

            /**Button  */
//            var changeBut: JButton = JButton("Изменить")
            changeBut.addActionListener {
                val first = JTextField(oscillogramList[0].start.toString())
                val last = JTextField(oscillogramList[0].finish.toString())
                val checkG = JCheckBox()
                if (oscillogramList[0].isCoordinates == true) {
                    checkG.isSelected = true
                }
                if (oscillogramList[0].isCoordinates == false) {
                    checkG.isSelected = false
                }
                val checkPaint = JCheckBox()
                if (oscillogramList[0].isPaint == true) {
                    checkPaint.isSelected = true
                }
                if (oscillogramList[0].isPaint == false) {
                    checkPaint.isSelected = false
                }
                val checkLocal = JCheckBox()
                if (oscillogramList[0].LocalMaxMin == true) {
                    checkLocal.isSelected = true
                }
                if (oscillogramList[0].LocalMaxMin == false) {
                    checkLocal.isSelected = false
                }
                var heighGr = JTextField(heightOscillogrammGraph.toString())
                val inputs = arrayOf<JComponent>(
                    JLabel("От"),
                    first,
                    JLabel("До"),
                    last,
                    JLabel("Включить графики"),
                    checkG,
                    JLabel("Локальные Max/Min"),
                    checkLocal,
                    JLabel("Масштабирование ЛКМ"),
                    checkPaint,
                    JLabel("Высота графиков (менять отдельно))"),
                    heighGr
                )
                val result =
                    JOptionPane.showConfirmDialog(null, inputs, "Параметры", JOptionPane.PLAIN_MESSAGE)
                if (result == JOptionPane.OK_OPTION) {
                    //oscillogramList[i].ChangeDot(first.text.toInt(), last.text.toInt())
                    //oscillogramList[i].canv.repaint()
                    //oscillogramList[i].canv.paint(oscillogramList[i].canv.graphics)
                    heightOscillogrammGraph = heighGr.text.toInt()
                    scbParamArray[0] = (last.text.toInt() - first.text.toInt()) / 2
                    scbParamArray[1] = oscillogramList[0].arrDot.size - 1 - scbParamArray[0]
                    scbParamArray[2] = last.text.toInt() - (last.text.toInt() - first.text.toInt()) / 2
                    scBar.setValues(0 + scbParamArray[2], 1 * scbParamArray[0], 0 + scbParamArray[0], oscillogramList[0].arrDot.size - 1)
                    scBar.isVisible = true
                    for (i in 0..oscillogramList.size - 1) {
                        oscillogramList[i].LocalMaxMin = checkLocal.isSelected
                        oscillogramList[i].isCoordinates = checkG.isSelected
                        oscillogramList[i].isPaint = checkPaint.isSelected
                        oscillogramList[i].start = first.text.toInt()
                        oscillogramList[i].finish = last.text.toInt()
                        oscillogramList[i].canv.repaint()
                        oscillogramList[i].canv.paint(oscillogramList[i].canv.graphics)
                        if (oscillogramList[i].isPaint == true) {
                            var ml = MosL(oscillogramList, scBar, scbParamArray, i, changeBut)
                            oscillogramList[i].canv.addMouseListener(ml)
                            oscillogramList[i].canv.addMouseMotionListener(ml)
                        }
                        if (oscillogramList[i].isPaint == false) {
                            if (oscillogramList[i].canv.mouseListeners.size-1 == 1)
                            oscillogramList[i].canv.mouseListeners[oscillogramList[i].canv.mouseListeners.size-1] = PopClickListener(oscillogramList[i])
                            //oscillogramList[i].canv.addMouseMotionListener(ml)
                            //oscillogramList[i].canv.addMouseListener(PopClickListener(oscillogramList[i]))
                        }
                        oscillogramList[i].wight = oscilogramWind.width.toFloat()
                    }
                } else {
                    println("User canceled / closed the dialog, result = $result")
                }
            }


            //oscilogramWind.paint(oscilogramWind.graphics)
        }


        menuBar = JMenuBar()
        val fileMenu = JMenu("Файл")
        val modelMenu = JMenu("Моделирование")
        val filtrMenu = JMenu("Фильтрация")
        val analMenu = JMenu("Анализ")
        val sittingMenu = JMenu("Настойка")
        val windowMenu = JMenu("Окна")
        val WeMenu = JMenuItem("О Разработчиках")

        val discretMenu = JMenu("Дискретные")
        val randomMenu = JMenu("Случайные")
        val v1 = JMenuItem("1)задержанный единичный импульс")
        val v2 = JMenuItem("2)задержанный единичный скачок ")
        val v3 = JMenuItem("3)дискретизированная убывающая экспонента")
        val v4 = JMenuItem("4)синусоид с заданными амплитудой ")
        val v5 = JMenuItem("5)«меандр»")
        val v6 = JMenuItem("6)“пила”")
        discretMenu.add(v1)
        discretMenu.add(v2)
        discretMenu.add(v3)
        discretMenu.add(v4)
        discretMenu.add(v5)
        discretMenu.add(v6)
        v1.addActionListener{
            CreateModelWindow("v1")
        }
        modelMenu.add(discretMenu)
        modelMenu.add(randomMenu)


        val newFrame = JMenuItem("new MDI")
        val loadSignal = JMenuItem("Загрузить сигнал")
        val oscillogramOpen = JMenuItem("Открыть осцилограммы")
        WeMenu.addActionListener{
            println("нажата - информация о нас")
            val infAboutInternalFrame = ItemWindow("Делали: ", true, true, true, false)
            infAboutInternalFrame.setBounds(25, 25, 300, 300) //width = 200
            infAboutInternalFrame.addInternalFrameListener(MDIInternalFrameListener())
            infAboutInternalFrame.addComponentListener(MDIResizeListener())
            val contents = JPanel(VerticalLayout())
            // Добавим кнопки и текстовое поле в панель
            contents.add(JTextField("Анна Распутная"))
            contents.add(JTextField("Маша Ярушина"))
            contents.add(JTextField("Влад Роговой"))
            contents.add(JTextField("Никита Баранов"))
            contents.add(JTextField("Тина Савченко"))

            // Размещаем панель в контейнере
            infAboutInternalFrame.setContentPane(contents)
            // Открываем окно
            infAboutInternalFrame.isVisible = true
            descPan.add(infAboutInternalFrame)
            infAboutInternalFrame.topLevelAncestor
        }
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
            var hihiHeight = sgn.channels * 110
            internalFrame.setBounds(25, 25, 200, hihiHeight) //width = 200
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
                        if (oscillogramList.size > 0){
                            if (channel.sgn == oscillogramList[0].sgn) {
                                oscillogramList.add(SuperChannel(channel.sgn, channel.channelNum, 600f, 200f, channel.start, channel.finish, true)) // надо потом поменять константные параметры константа - размер канваса
                            }
                            else {
                                oscillogramList = ArrayList()
                                oscillogramList.add(SuperChannel(channel.sgn, channel.channelNum, 600f, 200f, channel.start, channel.finish, true)) // надо потом поменять константные параметры константа - размер канваса

                            }
                        }
                        else{
                                oscillogramList.add(SuperChannel(channel.sgn, channel.channelNum, 600f, 200f, channel.start, channel.finish, true)) // надо потом поменять константные параметры константа - размер канваса
                            }
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
                        infAboutInternalFrame.setBounds(25, 25, 300, 300) //width = 200
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
        windowMenu.add(newFrame)
        windowMenu.add(oscillogramOpen)
        menuBar.add(fileMenu)
        menuBar.add(modelMenu)
        menuBar.add(filtrMenu)
        menuBar.add(analMenu)
        menuBar.add(sittingMenu)
        menuBar.add(windowMenu)
        menuBar.add(WeMenu)


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
        title = "Название нашей программы"
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

