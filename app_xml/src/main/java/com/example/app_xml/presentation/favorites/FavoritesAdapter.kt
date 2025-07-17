package com.example.app_xml.presentation.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.app_xml.R
import com.example.app_xml.databinding.ItemNewsCardBinding
import com.example.domain.model.Article

class FavoritesAdapter(
    private val onFavoriteClick: (Article) -> Unit,
    private val onArticleClick: (Article) -> Unit
) : ListAdapter<Article, FavoritesAdapter.FavoriteViewHolder>(ArticleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemNewsCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FavoriteViewHolder(private val binding: ItemNewsCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

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

            binding.btnAddFavorite.setImageResource(R.drawable.ic_bookmark_filled)

            binding.btnAddFavorite.setOnClickListener {
                onFavoriteClick(article)
            }

            binding.itemNewsCard.setOnClickListener{
                onArticleClick(article)
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