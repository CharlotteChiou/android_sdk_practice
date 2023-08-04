package a.exam.demo.view

import a.exam.demo.databinding.ItemListBinding
import a.exam.demo.model.Articles
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MainListRecyclerView(private val dataList: List<Articles>) :
    RecyclerView.Adapter<MainListRecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bean: Articles = dataList[position]
        holder.bind(bean)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(var binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bean: Articles) {
            binding.newsTitle.text = bean.title
            binding.newsContent.text = bean.summary
        }
    }
}