package com.example.app_xml.presentation.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_xml.databinding.FragmentFavoritesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding FavoritesFragment null")


    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesAdapter = FavoritesAdapter(
            onFavoriteClick = { article -> viewModel.toggleFavorite(article) }
        )

        binding.recyclerFavorites.adapter = favoritesAdapter
        binding.recyclerFavorites.layoutManager = LinearLayoutManager(requireContext())

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            favoritesAdapter.submitList(state.favorites)

            binding.progressBarFavorites.visibility =
                if (state.isLoading) View.VISIBLE else View.GONE

            binding.tvError.apply {
                text = state.error.orEmpty()
                visibility = if (state.error != null) View.VISIBLE else View.GONE
            }

            binding.tvEmpty.visibility =
                if (!state.isLoading && state.favorites.isEmpty() && state.error == null)
                    View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}