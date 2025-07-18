package com.example.app_xml.presentation.news_detail

import android.content.DialogInterface
import android.graphics.Paint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.app_xml.R
import com.example.app_xml.databinding.FragmentNewsDetailBinding
import com.example.app_xml.presentation.utils.openChromeCustomTab
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailDialogFragment : DialogFragment() {

    private val viewModel: NewsDetailSharedViewModel by activityViewModels()

    private var _binding: FragmentNewsDetailBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding NewsDetailFragment null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setGravity(Gravity.CENTER)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvReadMore.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        binding.btnBack.setOnClickListener {
            dismiss()
        }

        viewModel.selectedArticle.observe(viewLifecycleOwner) { article ->
            article?.let {
                binding.tvTitle.text = it.title
                binding.tvDescription.text = it.description

                if (it.author.isNotBlank()) {
                    binding.tvAuthor.visibility = View.VISIBLE
                    binding.tvAuthor.text = getString(R.string.author, it.author)
                } else {
                    binding.tvAuthor.visibility = View.GONE
                }

                binding.tvPublishedAt.text = it.publishedAt
                Glide.with(binding.imgNews)
                    .load(article.imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade(300))
                    .transform(CenterCrop(), RoundedCorners(16))
                    .into(binding.imgNews)

                binding.tvReadMore.setOnClickListener {
                    val url = article.url
                    if (url.isNotEmpty()) {
                        openChromeCustomTab(requireContext(), url)
                    } else {
                        Toast.makeText(requireContext(), "Ссылка недоступна", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                binding.btnFavorite.setOnClickListener {
                    viewModel.toggleFavorite(article)
                }

                binding.btnFavorite.setImageResource(
                    if (article.isFavorite) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark
                )
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}