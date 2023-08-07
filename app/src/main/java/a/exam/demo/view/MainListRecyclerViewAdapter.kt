package a.exam.demo.view

import a.exam.demo.databinding.ItemListBinding
import a.exam.demo.model.NewsData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MainListRecyclerViewAdapter(private val dataList: List<NewsData>) :
    RecyclerView.Adapter<MainListRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bean: NewsData = dataList[position]
        holder.bind(bean)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(var binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bean: NewsData) {
            binding.newsTitle.text = bean.title
            binding.newsContent.text = bean.summary
            binding.newsPercent.text = bean.percent.toString()
        }
    }
}