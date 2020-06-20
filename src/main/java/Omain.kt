

import java.awt.*
import java.awt.FlowLayout.*
import java.awt.event.*
import java.beans.PropertyVetoException
import java.io.File
import java.util.*
import javax.swing.*
import javax.swing.border.Border
import javax.swing.border.EmptyBorder
import javax.swing.event.InternalFrameEvent
import javax.swing.event.InternalFrameListener
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
    fun MyFileDialog (): File{ //String{
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

    fun SaveChannal ():File{
        var fileChooser = JFileChooser()
        fileChooser.setDialogTitle("Выбор директории")
        // Определение режима - только каталог
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
        var result: Int = fileChooser.showOpenDialog(this)
        // Если директория выбрана, покажем ее в сообщении
        if (result == JFileChooser.APPROVE_OPTION ) {
            JOptionPane.showMessageDialog(this, fileChooser.getSelectedFile())
        }
        return fileChooser.selectedFile
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
        lateinit var modelWind: ItemWindow
        lateinit var FourierWind: ItemWindow
        lateinit var StatWind: ItemWindow
        lateinit var SignalWind: ItemWindow
        lateinit var GlobalSignal : Signal
        var heightOscillogrammGraph = 200
        var oscillogramList: ArrayList<SuperChannel> = ArrayList()
        var statList: ArrayList<SuperChannel> = ArrayList()
        var FourierList: ArrayList<SuperChannel> = ArrayList()
        lateinit var oscilogramWind: ItemWindow
        var MainLineForHistogram = 5

        /// GLOBAL VARIABLES FOR SPEKTERS @Bar ///
        var isAmplitude = true
        var smoothCoeff = 5
        var isLinearShowing = true
        var NulElem = 1
        ///

        /**тут описываю окно Фурье  */
        fun CreateFourierWind(){
            var FourierContents = JPanel(VerticalLayout())
            try {
                //println(oscilogramWind.isClosed)
                FourierWind.setContentPane(FourierContents)
                if (FourierWind.isClosed) {

                    FourierWind = ItemWindow("Спектр Фурье", true, true, false, false)
                    FourierWind.setBounds(25, 25, 700, 520)
                    FourierWind.addInternalFrameListener(MDIInternalFrameListener())
                    FourierWind.addComponentListener(MDIResizeListener())
                    FourierWind.setContentPane(FourierContents)
                    descPan.add(FourierWind)
                    FourierWind.isVisible = true
                }
            } catch (e: UninitializedPropertyAccessException) { //я знаю, что тут один и тот же код, мне похуй, так лучше!!!
                println("тут сработало исключение")
                FourierWind = ItemWindow("Спектр Фурье", true, true, false, false)
                FourierWind.setBounds(25, 25, 420, 520)
                FourierWind.addInternalFrameListener(MDIInternalFrameListener())
                FourierWind.addComponentListener(MDIResizeListener())
                FourierWind.setContentPane(FourierContents)
                descPan.add(FourierWind)
                FourierWind.isVisible = true

            }
            FourierContents.removeAll()

            FourierWind.setBounds(FourierWind.x, FourierWind.y, 700, FourierList.size*210 + 70)
            var clearBut: JButton = JButton("Очистить")
            var paramLLabel = JLabel("L")
            var paramLText : JTextField = JTextField("0")
            val a : Array<String> = arrayOf("x(0) = 0","x(0) = |x(1)|", " - ")
            val b : Array<String> = arrayOf("Амплитудный спектр", "СПМ")
            var curSpectrum = JComboBox(b)
            var initialParam = JComboBox(a)
            val c : Array<String> = arrayOf("Линейный", "Логарифмический")
            var lgOrLin = JComboBox(c)
            var update = JButton("Обновить спектры")

            /*
      * Определение последовательного расположения
      * с выравниванием компонентов по центру
      */ FourierContents.layout = FlowLayout(FlowLayout.LEFT)
            if (isAmplitude == false) curSpectrum.selectedItem = "СПМ"
            if(isAmplitude == true) curSpectrum.selectedItem = "Амплитудный спектр"
            paramLText.text = smoothCoeff.toString()
            if (isLinearShowing == true) lgOrLin.selectedItem = "Линейный"
            if (isLinearShowing == false) lgOrLin.selectedItem = "Логарифмический"
            if (NulElem == 0) initialParam.selectedItem = "x(0) = 0"
            if (NulElem == 1) initialParam.selectedItem = "x(0) = |x(1)|"
            if (NulElem == 2) initialParam.selectedItem = " - "

                update.addActionListener {
                //if (curSpectrum.toolTipText == "СПМ") {println("СПМ")}
                if (curSpectrum.selectedItem == "СПМ"){
                    print("выбран СПМ - ")
                    isAmplitude = false
                }
                if (curSpectrum.selectedItem == "Амплитудный спектр"){
                    print("выбран Амплитудный спектр - ")
                    isAmplitude = true
                }

                smoothCoeff = paramLText.text.toInt()
                print(smoothCoeff)

                if (lgOrLin.selectedItem == "Линейный"){
                    print(" - выбран Линейный")
                    isLinearShowing = true
                }

                if (lgOrLin.selectedItem == "Логарифмический"){
                    print(" - выбран Логарифмический")
                    isLinearShowing = false
                }

                    if (initialParam.selectedItem == "x(0) = 0"){
                        println(" x(0) = 0")
                        NulElem = 0
                    }

                    if (initialParam.selectedItem == "x(0) = |x(1)|"){
                        println(" x(0) = |x(1)|")
                        NulElem = 1
                    }
                    if (initialParam.selectedItem == " - "){
                        println("  - ")
                        NulElem = 2
                    }
                    CreateFourierWind()
            }
            clearBut.addActionListener{
                FourierList.clear()
                FourierContents.removeAll()
            }

            FourierContents.add(clearBut)
            FourierContents.add(paramLLabel)
            FourierContents.add(paramLText)
            FourierContents.add(initialParam)
            FourierContents.add(curSpectrum)
            FourierContents.add(lgOrLin)
            FourierContents.add(update)

            for (i in 0..FourierList.size-1){
                //statList[i].channelNum = i
//                var text = TextArea (GenStatistics(statList[i], GlobalSignal))
//                text.preferredSize = Dimension(400, 200)
//                FourierContents.add(text)
                //var Complex: ComplexArr = sinePlusCosine(FourierList[i].sgn.arraChannels[FourierList[i].channelNum], FourierList[i].sgn.samplesnumber)
                //FourierList[i].FourierArrDot = Complex.Module()
                var transferedArr : Array<Float>
                var transferSamlesnumber = GlobalSignal.vision[1] - GlobalSignal.vision[0]
                var copyOrigArr = FourierList[i].sgn.arraChannels[FourierList[i].channelNum].copyOfRange(GlobalSignal.vision[0], GlobalSignal.vision[1])
                if (isAmplitude) {
                    if (smoothCoeff == 0) {
                        transferedArr = countAmplitudeSpekter(copyOrigArr,
                            transferSamlesnumber, FourierList[i].sgn.samplingrate.toFloat())
                    }
                    else
                        transferedArr = countSmoothingAmplitude(copyOrigArr,
                            transferSamlesnumber, FourierList[i].sgn.samplingrate.toFloat(), smoothCoeff)
                }
                else {
                    if (smoothCoeff == 0) {
                        transferedArr = countSPM(copyOrigArr,
                            transferSamlesnumber, FourierList[i].sgn.samplingrate.toFloat())
                    }
                    else
                        transferedArr = countSmoothingSPM(copyOrigArr,
                            transferSamlesnumber, FourierList[i].sgn.samplingrate.toFloat(), smoothCoeff)
                }
                if (!isLinearShowing) {
                    if (isAmplitude)
                        countAmplitudeSpekterInLg(transferedArr)
                    else
                        countSPMInLg(transferedArr)
                }

//                FourierList[i].FourierArrDot = //countAmplitudeSpekter(FourierList[i].sgn.arraChannels[FourierList[i].channelNum],
//                        //FourierList[i].sgn.samplesnumber, FourierList[i].sgn.samplingrate.toFloat())
//                    transferedArr
//                FourierList[i].IsFourier = true
//                FourierList[i].isCoordinates = true
//                FourierList[i].FourierChangeDot()

                if (NulElem == 0) transferedArr[0] = 0f
                if (NulElem == 1) transferedArr[0] = transferedArr[1]
                FourierList[i].GenFourierCanv(700, 200, true, 0, transferSamlesnumber/2, transferedArr)
                FourierList[i].FourieCanv.preferredSize = Dimension(700, 200)
                FourierContents.add(FourierList[i].FourieCanv)
                //sinePlusCosine()

            }

        }

        fun UpdateFourierWind(){

        }







        /**тут описываю окно статистик */
        fun CreateStatWind(){
            var StatContents = JPanel(VerticalLayout())
            try {
                //println(oscilogramWind.isClosed)
                StatWind.setContentPane(StatContents)
                if (StatWind.isClosed) {

                    StatWind = ItemWindow("Статистики", false, true, false, false)
                    StatWind.setBounds(25, 25, 700, 450)
                    StatWind.addInternalFrameListener(MDIInternalFrameListener())
                    StatWind.addComponentListener(MDIResizeListener())
                    StatWind.setContentPane(StatContents)
                    descPan.add(StatWind)
                    StatWind.isVisible = true
                }
            } catch (e: UninitializedPropertyAccessException) { //я знаю, что тут один и тот же код, мне похуй, так лучше!!!
                println("тут сработало исключение")
                StatWind = ItemWindow("Статистики", false, true, false, false)
                StatWind.setBounds(25, 25, 420, 450)
                StatWind.addInternalFrameListener(MDIInternalFrameListener())
                StatWind.addComponentListener(MDIResizeListener())
                StatWind.setContentPane(StatContents)
                descPan.add(StatWind)
                StatWind.isVisible = true

            }

            StatWind.setBounds(250, 250, 420, 350 * statList.size + 30)
            var clearBut: JButton = JButton("Очистить")
            StatContents.add(clearBut)

            for (i in 0..statList.size-1){
                //statList[i].channelNum = i
                var text = TextArea (GenStatistics(statList[i], GlobalSignal))
                text.preferredSize = Dimension(400, 200)
                StatContents.add(text)

                statList[i].start = GlobalSignal.vision[0]
                statList[i].finish = GlobalSignal.vision[1]
                statList[i].LineForHistogram = MainLineForHistogram
                statList[i].Histogram.preferredSize = Dimension(400, 106)
                StatContents.add(statList[i].Histogram)
                statList[i].Histogram.repaint()
                statList[i].Histogram.paint(statList[i].Histogram.graphics)
                println("кол во при создании окна - " + statList.size)
            }
            //StatWind.contentPane = StatContents

            clearBut.addActionListener{
                for (i in 0..statList.size-1){
                    //statList.remove(statList[0])
                    //statList.dropLast(1)
                    statList = ArrayList()
                    println("размер после удаления - " + statList.size)
                    StatContents.removeAll()
                }
            }

        }

        fun UpdateStatWind(){
            var StatContents = JPanel(VerticalLayout())
            try {
                //println(oscilogramWind.isClosed)
                StatWind.setContentPane(StatContents)



            var clearBut: JButton = JButton("Отчистить")
            StatContents.add(clearBut)
            for (i in 0..statList.size-1){
                var text = TextArea (GenStatistics(statList[i], GlobalSignal))
                text.preferredSize = Dimension(400, 200)
                StatContents.add(text)

                statList[i].start = GlobalSignal.vision[0]
                statList[i].finish = GlobalSignal.vision[1]
                statList[i].LineForHistogram = MainLineForHistogram
                statList[i].Histogram.preferredSize = Dimension(400, 106)
                StatContents.add(statList[i].Histogram)
                statList[i].Histogram.paint(statList[i].Histogram.graphics)
                println("кол во при создании окна - " + statList.size)
            }
            clearBut.addActionListener{
                for (i in 0..statList.size-1){
                    statList.remove(statList[0])
                }
                StatContents.removeAll()
            }

            } catch (e: UninitializedPropertyAccessException) { //вот так пишут код идиоты, дай ПЯТЬ если такой же)
            }

        }


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

                    /** эти строчки нужны для глобального контроля видимости**/

                            GlobalSignal.vision[0] = oscillogramList[i].start
                            GlobalSignal.vision[1] = oscillogramList[i].finish

                    //oscillogramList[i].canv.paint(oscillogramList[i].canv.graphics)
                }
                UpdateStatWind()
                //CreateStatWind()
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

                        /** эти строчки нужны для глобального контроля видимости**/
                        GlobalSignal.vision[0] = first.text.toInt()
                        GlobalSignal.vision[1] = last.text.toInt()

                    }
                    UpdateStatWind()
                    //CreateStatWind()
                    println(oscillogramList[0].start)
                    println(oscillogramList[0].finish)
                } else {
                    println("User canceled / closed the dialog, result = $result")
                }
            }


            //oscilogramWind.paint(oscilogramWind.graphics)
        }



        fun createSignalWind(sgn: Signal){
            var internalFrame = ItemWindow("Сигналы", true, true, false, false)
            try {
                SignalWind.dispose()
            }
            catch (e: UninitializedPropertyAccessException){}
            SignalWind = internalFrame
            GlobalSignal = sgn
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

                var FourierItem: JMenuItem

                init {
                    FourierItem = JMenuItem("Cпектр Фурье")
                    FourierItem.addActionListener {
                        //var channel_ = SuperChannel(channel.sgn, channel.channelNum, channel.wight, channel.hight, channel.start, channel.finish)
                        if (FourierList.size > 0){
                            if (channel.sgn == FourierList[0].sgn) {
                                FourierList.add(SuperChannel(channel.sgn, channel.channelNum, 600f, 200f, channel.start, channel.finish, true)) // надо потом поменять константные параметры константа - размер канваса
                            }
                            else {
                                FourierList = ArrayList()
                                FourierList.add(SuperChannel(channel.sgn, channel.channelNum, 600f, 200f, channel.start, channel.finish, true)) // надо потом поменять константные параметры константа - размер канваса

                            }
                        }
                        else{
                            FourierList.add(SuperChannel(channel.sgn, channel.channelNum, 600f, 200f, channel.start, channel.finish, true)) // надо потом поменять константные параметры константа - размер канваса
                        }
                        CreateFourierWind()
                    }
                    add(FourierItem)
                }

                var deleteItem: JMenuItem

                init {
                    deleteItem = JMenuItem("Удалить канал")
                    deleteItem.addActionListener {
                    }
                    add(deleteItem)
                }

                var statItem: JMenuItem

                init {
                    statItem = JMenuItem("Статистики")
                    statItem.addActionListener {
                        if (statList.size > 0){
                            if (channel.sgn == statList[0].sgn) {
                                statList.add(SuperChannel(channel.sgn, channel.channelNum, 600f, 200f, channel.start, channel.finish, true)) // надо потом поменять константные параметры константа - размер канваса
                            }
                            else {
                                statList = ArrayList()
                                statList.add(SuperChannel(channel.sgn, channel.channelNum, 600f, 200f, channel.start, channel.finish, true)) // надо потом поменять константные параметры константа - размер канваса

                            }
                        }
                        else{
                            statList.add(SuperChannel(channel.sgn, channel.channelNum, 600f, 200f, channel.start, channel.finish, true)) // надо потом поменять константные параметры константа - размер канваса
                        }
                        CreateStatWind()
                    }
                    add(statItem)
                }

                var saveHowItem: JMenuItem
                init {
                    saveHowItem = JMenuItem("Сохранить как...")
                    saveHowItem.addActionListener {
                        SignalToFile(MyFileDialog(), GlobalSignal)
                    }
                    add(saveHowItem)
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
                contents.add(arrChannel[i].canv)
                arrChannel[i].canv.addMouseListener(PopClickListener(arrChannel[i]))
            }
            contents.mouseListeners
            internalFrame.contentPane = contents

            descPan.add(internalFrame)
            internalFrame.size = Dimension(230, 700)
            internalFrame.isVisible = true
            internalFrame.setLocation(1150,1)
        }

        fun CreateModelWindow(v: String) {
            lateinit var sgn: Signal
            try {
                sgn = InitModel(v, GlobalSignal.starttime, GlobalSignal.startdate, GlobalSignal.samplingrate, GlobalSignal.samplesnumber.toString())
            }
            catch (e: UninitializedPropertyAccessException){
                sgn = InitModel(v, "00:00:00", "01-01-2020", "1", "10000")

            }

            try {
                if ((sgn.startdate == GlobalSignal.startdate) && (sgn.starttime == GlobalSignal.starttime) && (sgn.samplesnumber == GlobalSignal.samplesnumber) && (sgn.samplingrate == GlobalSignal.samplingrate)) {
                    var array = Array(GlobalSignal.channels + 1, { Array(sgn.samplesnumber, { 0f }) })
                    var channelsnames = arrayOfNulls<String>(GlobalSignal.channels + 1)
                    GlobalSignal.channels += 1
                    for (i in 0..GlobalSignal.channels - 2) {
                        channelsnames[i] = GlobalSignal.channelsnames[i]
                    }
                    channelsnames[GlobalSignal.channels - 1] = sgn.channelsnames[0]
                    GlobalSignal.channelsnames = channelsnames

                    for (i in 0..GlobalSignal.samplesnumber - 1) {
                        for (j in 0..GlobalSignal.channels - 1) {
                            if (j == GlobalSignal.channels - 1) {
                                array[j][i] = sgn.arraChannels[0][i]
                            } else
                                array[j][i] = GlobalSignal.arraChannels[j][i]
                        }
                    }
                    GlobalSignal.arraChannels = array
                } else
                    GlobalSignal = sgn
            }
            catch (e: UninitializedPropertyAccessException){
                GlobalSignal = sgn
            }

            createSignalWind(GlobalSignal)
        }


        menuBar = JMenuBar()
        val fileMenu = JMenu("Файл")
        val modelMenu = JMenu("Моделирование")
        val filtrMenu = JMenu("Фильтрация")
        val analMenu = JMenu("Анализ")
        val sittingMenu = JMenu("Настойка")
        val windowMenu = JMenu("Окна")
        val WeMenu = JMenuItem("О Разработчиках")

        val G_K = JMenuItem("Кол-во разбиений Гистограммы")
        analMenu.add(G_K)
        G_K.addActionListener {
            val K = JTextField(MainLineForHistogram)

            val inputs = arrayOf<JComponent>(
                JLabel("Введите кол-во разбиений"),
                K
            )
            val result =
                JOptionPane.showConfirmDialog(null, inputs, "Кол-во разбиений", JOptionPane.PLAIN_MESSAGE)
            if (result == JOptionPane.OK_OPTION) {
                MainLineForHistogram = K.text.toInt()
            } else {
                println("User canceled / closed the dialog, result = $result")
            }
        }

        val discretMenu = JMenu("Дискретные")
        val randomMenu = JMenu("Случайные")
        val superPositionMenu = JMenu("Суперпозиция")

        val v1 = JMenuItem("1)задержанный единичный импульс")
        val v2 = JMenuItem("2)задержанный единичный скачок ")
        val v3 = JMenuItem("3)дискретизированная убывающая экспонента")
        val v4 = JMenuItem("4)синусоид с заданными амплитудой ")
        val v5 = JMenuItem("5)«меандр»")
        val v6 = JMenuItem("6)“пила”")
        val v7 = JMenuItem("7)“экспоненциальная огибающая ”")
        val v8 = JMenuItem("8)балансная огибающая")
        val v9 = JMenuItem("9)тональная огибающая")
        val superPosition1 = JMenuItem("Линейная суперпозиция")
        val superPosition2 = JMenuItem("Мультипликативная суперпозиция")

        val randomFunc1 = JMenuItem("1)сигнал белого шума, равномерно распределенного в интервале [a,b]")
        val randomFunc2 = JMenuItem("2)сигнал белого шума, распределенного по нормальному закону с заданными средним и дисперсией")
        val randomFunc3 = JMenuItem("3)случайный сигнал авторегрессии-скользящего среднего порядка (p,q) – АРСС (p,q)")

        superPositionMenu.add(superPosition1)
        superPositionMenu.add(superPosition2)

        discretMenu.add(v1)
        discretMenu.add(v2)
        discretMenu.add(v3)
        discretMenu.add(v4)
        discretMenu.add(v5)
        discretMenu.add(v6)
        discretMenu.add(v7)
        discretMenu.add(v8)
        discretMenu.add(v9)

        randomMenu.add(randomFunc1)
        randomMenu.add(randomFunc2)
        randomMenu.add(randomFunc3)

        v1.addActionListener{
            CreateModelWindow("v1")
        }
        v2.addActionListener{
            CreateModelWindow("v2")
        }
        v3.addActionListener{
            CreateModelWindow("v3")
        }
        v4.addActionListener{
            CreateModelWindow("v4")
        }
        v5.addActionListener{
            CreateModelWindow("v5")
        }
        v6.addActionListener{
            CreateModelWindow("v6")
        }
        v7.addActionListener{
            CreateModelWindow("v7")
        }
        v8.addActionListener{
            CreateModelWindow("v8")
        }
        v9.addActionListener{
            CreateModelWindow("v9")
        }
        randomFunc1.addActionListener{
            CreateModelWindow("randomFunc1")
        }
        randomFunc2.addActionListener{
            CreateModelWindow("randomFunc2")
        }
        randomFunc3.addActionListener{
            CreateModelWindow("randomFunc3")
        }
        superPosition1.addActionListener{
            CreateModelWindow("superPosition1")
        }
        superPosition2.addActionListener{
            CreateModelWindow("superPosition2")
        }

        modelMenu.add(discretMenu)
        modelMenu.add(randomMenu)
        modelMenu.add(superPositionMenu)

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
            contents.add(JTextField("Валентина Савченко"))

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
            val channelFile = MyFileDialog()
            var sgn: Signal = FileToSignal(channelFile)



            createSignalWind(sgn)
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
                //sinePlusCosine()
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