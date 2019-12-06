package com.pptv.httpsproject

import kotlinx.coroutines.*


//思考 是否可以用协程 来代替rxjava
//参照 https://www.jianshu.com/p/b58555b47991
fun main() = runBlocking {
    GlobalScope.launch {
        //后台启动一个新的协程并继续
        delay(1000L)//非阻塞 的等待1秒钟,只能在协程中使用，挂起协程
        println("World!")//延迟后打印输出
    }
    println("Hello,")//协程已在等待时主线程还在继续
    delay(2000)
}


fun main(str: Array<String>) {
    GlobalScope.launch {
        //后台启动一个新的协程并继续
        delay(1000L)//非阻塞 的等待1秒钟,只能在协程中使用，挂起协程
        println("World!")//延迟后打印输出
    }
    println("Hello,")//协程已在等待时主线程还在继续
    //Thread.sleep(2000L)//阻塞 主线程2秒，保证JVM存活
    runBlocking { delay(2000) }//与sleep 效果一样，runBlocking 会阻塞到runBlocking内部代码执行完毕
}

fun main1() = runBlocking {
    //顶层主协程的适配器
    val job = GlobalScope.launch {
        //启动一个新的协程并保持对这个作业的引用
        delay(1000L)
        println("World!")
    }
    println()
    job.join()//等待直到子协程执行结束
}

fun main2() = runBlocking {
    launch {
        // 在 runBlocking 作用域中启动一个新协程
        delay(1000L)
        println("World!")
    }
    println("Hello,")//等同于 main1 不需要保持job 然后join
}

fun main3() = runBlocking {

    //    Task form coroutine scope
//    Task from runBlocking
//    Task from nested launch
//    Coroutine scope is over

    // coroutineScope ，runBlocking 都是作用域构建起，区别在意 runBlocking 在等待所有子协程执行完毕时会阻塞当前线程
    // corcoutineScope 不会阻塞当前线程

    launch {
        delay(2000L)
        println("Task from runBlocking ")
    }

    coroutineScope {
        launch {
            delay(5000L)
            println("Task from nested launch")
        }

        delay(100L)
        println("Task form coroutine scope ")
    }

    println("Coroutine scope is over")
}

fun main4() = runBlocking {
    launch { doWorld() }
    println("Hello,")
}

suspend fun doWorld() {
    delay(1000)
    println("World!")
}

fun main5() = runBlocking {
    repeat(100000) {
        launch {
            delay(1000)
            println(".")
        }
    }
}
