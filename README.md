# android_sdk_practice

Reference

Retrofit & coroutines

1. https://json2kt.com/
2. https://github.com/TouhidApps/Android-Modern-API-Call-Kotlin-MVVM
3. https://ithelp.ithome.com.tw/m/articles/10273143

Area calculate

1. https://blog.csdn.net/ChrisSen/article/details/84065243
2. https://stackoverflow.com/questions/39503595/how-to-find-percentage-of-each-visible-item-in-recycleview

Timer

1. https://stackoverflow.com/questions/54095875/how-to-create-a-simple-countdown-timer-in-kotlin

My Flow

1. Create new project and fix IDE problem before start coding.
2. Need testing data -> Use Retrofit & coroutine to get news data.
3. Area calculate.
4. Setup Timer rule.
5. How to build SDK.
6. Finish

How to Use Utility.checkAdState(recyclerView, mListItems)

1. Build a data class extends BaseAdData, like
   data class DemoData(
   var title: String? = "",
   var summary: String? = "",
   override var percent: Int = 0,
   override var isAd: Boolean = false
   ) : BaseAdData(percent, isAd)
2. use it (DemoData) in custom class RecyclerViewAdapter extends RecyclerView.Adapter, like
   MainListRecyclerViewAdapter(private val dataList: List<DemoData>)
3. Call Utility.checkAdState(recyclerView, mListItems) in Showing ListView's activity's recyclerview
   addOnScrollListener. Use Utility.checkAdState(recyclerView, mListItems) in override fun
   onScrolled

   mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
   override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
   Utility.checkAdState(recyclerView, mListItems) // This
   } })
