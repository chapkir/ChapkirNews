package com.example.app_xml.presentation.favorites

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_xml.R
import com.example.app_xml.databinding.FragmentFavoritesBinding
import com.example.app_xml.databinding.ToolbarFavoritesBinding
import com.example.app_xml.presentation.news_detail.NewsDetailDialogFragment
import com.example.app_xml.presentation.news_detail.NewsDetailSharedViewModel
import com.example.app_xml.presentation.utils.applyWindowInsets
import com.example.domain.model.Article
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding FavoritesFragment null")

    private lateinit var toolbarBinding: ToolbarFavoritesBinding

    private val viewModel: FavoritesViewModel by viewModels()
    private val sharedViewModel: NewsDetailSharedViewModel by activityViewModels()

    private var newsDialog: NewsDetailDialogFragment? = null

    private lateinit var favoritesAdapter: FavoritesAdapter

    private var deletedFavArticle: Article? = null
    private var deletedFavPosition: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        toolbarBinding = ToolbarFavoritesBinding.bind(
            binding.appBarFavoritesLayout.findViewById(R.id.favoritesToolbar)
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbarBinding.favoritesToolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        applyWindowInsets(
            activity = requireActivity(),
            targetView = toolbarBinding.favoritesToolbar,
            insetTypes = WindowInsetsCompat.Type.statusBars(),
            applyTop = true
        )

        favoritesAdapter = FavoritesAdapter(
            onFavoriteClick = { article -> deleteArticle(article) },
            onArticleClick = { article ->
                sharedViewModel.selectArticle(article)
                newsDialog = NewsDetailDialogFragment()
                newsDialog?.show(parentFragmentManager, "NewsDetailDialog")
            }
        )

        binding.recyclerFavorites.adapter = favoritesAdapter
        binding.recyclerFavorites.layoutManager = LinearLayoutManager(requireContext())

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val article = favoritesAdapter.currentList[position]
                deleteArticle(article)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerFavorites)

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            favoritesAdapter.submitList(state.favorites)

            binding.progressBarFavorites.visibility =
                if (state.isLoading) View.VISIBLE else View.GONE

            if (state.error != null) {
                binding.errorBlockLayout.visibility = View.VISIBLE
                binding.tvErrorMessage.text = state.error
            } else {
                binding.errorBlockLayout.visibility = View.GONE
            }
        }
    }

    private fun deleteArticle(article: Article) {
        deletedFavArticle = article
        deletedFavPosition = favoritesAdapter.currentList.indexOf(article)

        val currentList = favoritesAdapter.currentList.toMutableList()
        currentList.remove(article)
        favoritesAdapter.submitList(currentList)

        val snackbar = Snackbar.make(binding.root, "Новость удалена", Snackbar.LENGTH_LONG)
            .setAnchorView(requireActivity().findViewById<View>(R.id.bottomNavBar)!!)
            .setAction("Отменить") {
                deletedFavArticle?.let {
                    val newList = favoritesAdapter.currentList.toMutableList()
                    if (deletedFavPosition in 0..newList.size) {
                        newList.add(deletedFavPosition, it)
                    } else {
                        newList.add(0, it)
                    }
                    favoritesAdapter.submitList(newList)
                    deletedFavArticle = null
                    deletedFavPosition = -1
                }
            }

        snackbar.view.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.secondary)
        )

        val snackbarTextView =
            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)

        with(snackbarTextView) {
            setTextColor(ContextCompat.getColor(requireContext(), R.color.on_background))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        }

        snackbar.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.primary))

        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                if (event != DISMISS_EVENT_ACTION) {
                    deletedFavArticle?.let {
                        viewModel.toggleFavorite(it)
                        deletedFavArticle = null
                        deletedFavPosition = -1
                    }
                }
            }
        })

        snackbar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}