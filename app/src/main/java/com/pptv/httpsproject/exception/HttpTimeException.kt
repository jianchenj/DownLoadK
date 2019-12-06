package com.pptv.httpsproject.exception

//参照 https://www.jianshu.com/p/2a27c52f7811
//class ScrollMenuViewK @JvmOverloads constructor(private val mContext: Context, attr: AttributeSet? = null, defStyle: Int = 0) : RecyclerView(mContext, attr, defStyle), ICoexistable, IInteractiveTypedWidget
class HttpTimeException(detailMessage: String) : RuntimeException() {

    constructor(resultCode: Int) : this(getApiExceptionMessage(resultCode)) {}

    companion object {
        //伴生对象
        @JvmStatic//该注解会为伴生对象的成员生成为真正的静态方法和字段
        val NO_DATA = 0x2//public static final

        /**
         *  转换村务数据
         *
         *  @param code 错误码
         *  @return 错误信息
         *
         *  static 方法 直接声明在伴生对象中
         */
        fun getApiExceptionMessage(code: Int): String {
            return when (code) {
                NO_DATA -> "无数据"
                else -> "error"
            }
        }
    }
}