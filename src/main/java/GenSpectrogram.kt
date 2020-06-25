import kotlin.math.*

fun calculationSpect(arr: Array<Float>, weight: Float, height: Float, coeff_n: Float): Array<Array<Float>>{
    var ans = Array<Array<Float>>(weight.toInt(), { Array(height.toInt(), {0f}) })
    val Pi = 3.1415926535f
    var workArr = Array<Array<Float>>(weight.toInt(), { Array(height.toInt() * 2, {0f}) })

    var Section_Base : Double = (arr.size.toFloat()/weight).toDouble() //шаг 2
    var Section_BaseInt : Double = Math.ceil(Section_Base)
    var Section_N : Int = (Section_BaseInt * coeff_n).toInt() //шаг 3

    var L = 1                        //шаг 4
    var N = height.toInt()
    if (Section_N > height * 2){
        while (N <= Section_N){
            L++
            N = height.toInt() * L
        }
        workArr = Array<Array<Float>>(weight.toInt(), { Array(N, {0f}) })

    }
    if (Section_N <= height * 2){
        workArr = Array<Array<Float>>(weight.toInt(), { Array(N * 2, {0f}) })
    }

    var ansI = 0
    var workI = -1
    //var cloneS_B =Section_Base.toInt()
    for (i in 0..arr.size-1 step Section_BaseInt.toInt()){ //шаг 5 и 5.1
        var ansJ = 0
        var sum = 0f
        var workJ = 0
        workI++

        for (j in i..(Section_N+i-1)){
            if (j < arr.size-1) {
                print("" + i + " " + j + " " +workArr.size)
                print(" "+ workI +" " + workJ +" " + arr.size)
                println(" " + workArr[workI].size)
                if (i > 9500)
                    print("") //for debug
                workArr[workI][workJ] = arr[j]        //шаг 5.2
                sum += arr[j]
                workJ++
            }

        }
        var s = sum/Section_N            //шаг 5.3
        for (j in 0..Section_N-1) {
                workArr[workI][j] -= s
                workArr[workI][j] *= 0.54f - 0.46f * cos((2 * Pi * j) / (Section_N - 1))  //шаг 5.4

        }
        //шаг 5.5 уже выполнен
        val Complex = sinePlusCosine(workArr[workI], workArr[workI].size)
        var trans = Complex.Module()
        if (L > 1){
            for(j in 0..height.toInt() step L){
                for (k in j..j+L-1){
                    if (k < trans.size)
                        ans[ansI][ansJ] += trans[k]
                }
                ans[ansI][ansJ] /= L.toFloat()
                ansJ++
            }
        }
        else ans[ansI] = trans.copyOfRange(0, trans.size/2)
        ansI++
    }

    return ans
}