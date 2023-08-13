package a.exam.demo.view

import a.exam.coresdk.init.ExamCoreSDK
import a.exam.coresdk.model.AdListener
import a.exam.coresdk.network.AdRequest
import a.exam.coresdk.utility.Utility
import a.exam.coresdk.view.AdView
import a.exam.demo.databinding.ActivityMainBinding
import a.exam.demo.model.DemoData
import a.exam.demo.model.MovieResponse
import a.exam.demo.model.Results
import a.exam.demo.networkservice.ApiState
import a.exam.demo.viewmodel.MainActivityVM
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mMainActivityVM: MainActivityVM
    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mMainListAdapter: MainListRecyclerViewAdapter
    private var mListItems: MutableList<DemoData> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ExamCoreSDK.initialize(this, object : ExamCoreSDK.OnInitializationCompleteListener {
            override fun onInitializationComplete(state: ExamCoreSDK.InitState) {
                when (state) {
                    ExamCoreSDK.InitState.SUCCESS -> {
                        initView()
                        initViewModel()
                        collects()
                    }

                    ExamCoreSDK.InitState.FAILED -> {
                        // Maybe re-init
                    }

                    ExamCoreSDK.InitState.OTHER -> {
                        // Maybe check other setting
                    }

                    else -> {}
                }
            }
        })
    }

    override fun onDestroy() {
        Utility.onDestroy()

        super.onDestroy()
    }

    private fun initView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mRecyclerView = binding.recyclerView
        mRecyclerView.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(
            mRecyclerView.context,
            layoutManager.orientation
        )
        mRecyclerView.addItemDecoration(dividerItemDecoration)

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Utility.checkRecyclerViewAdState(recyclerView, mListItems)
            }
        })

        binding.swipeLayout.setOnRefreshListener {
            mMainActivityVM.getMovieList()
        }

        val adView = AdView(this, binding.adViewBanner)
        adView.setAdSize(320, 100)
        adView.loadAd(AdRequest(), mListener)
    }

    private val mListener = object : AdListener {
        override fun onAdClicked() {

        }

        override fun onAdClosed() {

        }

        override fun onAdFailedToLoad() {

        }

        override fun onAdImpression() {

        }

        override fun onAdLoaded() {

        }

        override fun onAdOpened() {

        }
    }

    private fun initViewModel() {
        mMainActivityVM = ViewModelProvider(this)[MainActivityVM::class.java]

        mMainActivityVM.getMovieList()
    }

    private fun collects() {
        lifecycleScope.launchWhenCreated {
            mMainActivityVM.wMessage.collect {
                when (it) {
                    is ApiState.Loading -> {
                        binding.swipeLayout.isRefreshing = true
                    }

                    is ApiState.Failure -> {
                        it.e.printStackTrace()
                        binding.swipeLayout.isRefreshing = false
                    }

                    is ApiState.Success -> {
                        binding.swipeLayout.isRefreshing = false

                        val movieResponse = it.data as MovieResponse
                        mListItems.clear()
                        handleInsertAd(movieResponse.results)

                        mMainListAdapter = MainListRecyclerViewAdapter(mListItems)
                        mRecyclerView.adapter = mMainListAdapter

                        mMainListAdapter.notifyDataSetChanged()
                    }

                    is ApiState.Empty -> {
                        println("Empty...")
                    }
                }
            }
        }
    }

    private fun handleInsertAd(resultList: List<Results>) {
        var counter = 0
        val distance = 2
        for (item in resultList) {
            if (counter == distance) {
                mListItems.add(DemoData("AD", "This is AD", 100, true))
                counter = 0
            }
            mListItems.add(DemoData(item.title, item.overview, 100, false))
            ++counter
        }
    }
}