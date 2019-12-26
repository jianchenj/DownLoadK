最近项目中遇到系统OTA升级需要下载OTA包，并且升级的需求。
老项目中使用的是原生接口 HttpClient实现，代码量大，逻辑复杂。
其大致思路是，请求后台接口，接口返回一个JSON数据，其中包含了版本号，升级包下载路径，升级包下载的md5值。下载前先比较版本号，然后比较JSON中的MD5和之前下载后安装保存的MD5。
如果版本号大，并且MD5值不一致，则开启下载，下载完成后本地计算下载好的升级包MD5，然后比较json中的MD5值是否一致，一致则安装升级包。
本次OTA定制的需求没有这么多要求，系统会传递一个下载url然后我去下载就行了。借此机会练习一下基本知识。

在网上看到一个demo https://www.jianshu.com/p/2a27c52f7811
使用 retrofit2 + rxjava2 + okhttp3 完成下载
另外尝试使用 Coroutines+Retrofit+Okhttp 完成下载


rxkotlin https://www.jianshu.com/c/a76810434359
Coroutines+Retrofit+Okhttp  https://www.jianshu.com/p/b58555b47991