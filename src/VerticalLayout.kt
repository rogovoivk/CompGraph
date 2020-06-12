import javax.*
import javax.swing.*

import java.awt.Component
import java.awt.Container
import java.awt.Dimension
import java.awt.LayoutManager
import javafx.scene.control.*
import javafx.scene.control.TextInputDialog



// Менеджер вертикального расположения компонентов
internal class VerticalLayout : LayoutManager {
    private val size = Dimension()

    // Следующие два метода не используются
    override fun addLayoutComponent(name: String, comp: Component) {}

    override fun removeLayoutComponent(comp: Component) {}

    // Метод определения минимального размера для контейнера
    override fun minimumLayoutSize(c: Container): Dimension {
        return calculateBestSize(c)
        //size.height = 100
        //size.width = 200
        //return size
    }

    // Метод определения предпочтительного размера для контейнера
    override fun preferredLayoutSize(c: Container): Dimension {
        return calculateBestSize(c)
        //size.height = 100
        //size.width = 200
        //return size
    }

    // Метод расположения компонентов в контейнере
    override fun layoutContainer(container: Container) {
        // Список компонентов
        val list = container.components
        var currentY = 5
        for (i in list.indices) {
            // Определение предпочтительного размера компонента
            val pref = list[i].preferredSize
            // Размещение компонента на экране
            list[i].setBounds(5, currentY, pref.width, pref.height)
            // Учитываем промежуток в 5 пикселов
            currentY += 5
            // Смещаем вертикальную позицию компонента
            currentY += pref.height
        }
    }

    // Метод вычисления оптимального размера контейнера
    private fun calculateBestSize(c: Container): Dimension {
        // Вычисление длины контейнера
        val list = c.components
        var maxWidth = 0
        for (i in list.indices) {
            val width = list[i].width
            // Поиск компонента с максимальной длиной
            if (width > maxWidth)
                maxWidth = width
        }
        // Размер контейнера в длину с учетом левого отступа
        size.width = maxWidth + 5
        // Вычисление высоты контейнера
        var height = 0
        for (i in list.indices) {
            height += 5
            height += list[i].height
        }
        size.height = height
        return size
    }
}

//fun Distancy(){
//    var dialog: JDialog = JDialog()
//
//    var content: JPanel = JPanel()
//
//    var start: TextArea = TextArea()
//    var finish: TextArea = TextArea()
//    var lab1: JLabel = JLabel("от")
//    var lab2: JLabel = JLabel("до")
//    var finBut = JFXButton("Все !!!")
//    finBut.setOnAction{
//        dialog.dispose()
//        return
//    }
//
//
//    dialog.contentPane = content
//
//}

public var label: Label? = null

public fun showInputTextDialog() {

    val dialog = TextInputDialog("Tran")

    dialog.title = "o7planning"
    dialog.headerText = "Enter your name:"
    dialog.contentText = "Name:"

    val result = dialog.showAndWait()

    result.ifPresent { name -> label!!.setText(name) }
}
