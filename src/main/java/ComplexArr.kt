import kotlin.math.sqrt

class ComplexArr(realArr_: Array<Float>, imagArr_: Array<Float>) {
    var realArr: Array<Float> = realArr_
    var imagArr: Array<Float> = imagArr_

    fun Module ():Array<Float>{
        val ans: Array<Float> = Array<Float>(imagArr.size,{0f})

        for (i in 0..ans.size-1){
            ans[i] =  sqrt((realArr[i] * realArr[i]) + (imagArr[i] * imagArr[i]))
            print(ans[i].toString() + " ")
        }
        return ans
    }
}