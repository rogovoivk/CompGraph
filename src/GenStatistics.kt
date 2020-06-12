import javax.xml.soap.Text

fun GenStatistics(channel : SuperChannel, GlobalSignal: Signal):String {
    var text = String()
    text = "видимость графика на Осциллограммах от - "
    text += "" + GlobalSignal.vision[channel.channelNum][0] + " до - " + GlobalSignal.vision[channel.channelNum][1] + "\n"

    var sortedArr = GlobalSignal.arraChannels[channel.channelNum].copyOf()
    sortedArr.sort()

    text += "Минимум = " +sortedArr[0] + "\n"
    text += "Среднее = " +sortedArr[sortedArr.size / 2] + "\n"
    text += "Максимум= " +sortedArr[sortedArr.size-1] + "\n"

    return text
}