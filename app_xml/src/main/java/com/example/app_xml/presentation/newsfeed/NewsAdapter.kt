package com.example.app_xml.presentation.newsfeed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.app_xml.R
import com.example.app_xml.databinding.ItemNewsCardBinding
import com.example.domain.model.Article

class NewsAdapter(
    private val onFavoriteClick: (Article) -> Unit
) : PagingDataAdapter<Article, NewsAdapter.NewsViewHolder>(ArticleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = getItem(position) ?: return
        holder.bind(article)
    }

    inner class NewsViewHolder(private val binding: ItemNewsCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.tvTitle.text = article.title
            binding.tvDescription.text = article.description
            binding.tvAurhor.text = article.author
            binding.tvPublishedAt.text = article.publishedAt

            Glide.with(binding.imgNews)
                .load(article.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .transform(CenterCrop(), RoundedCorners(16))
                .into(binding.imgNews)

            binding.btnAddFavorite.setImageResource(
                if (article.isFavorite) R.drawable.ic_bookmark_filled
                else R.drawable.ic_bookmark
            )

            binding.btnAddFavorite.setOnClickListener {
                onFavoriteClick(article)
            }
        }
    }

    class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
            oldItem.url == newItem.url

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
            oldItem == newItem
    }
}