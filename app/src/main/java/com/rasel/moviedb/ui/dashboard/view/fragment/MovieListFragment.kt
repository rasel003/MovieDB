package com.rasel.moviedb.ui.dashboard.view.fragment

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.rasel.moviedb.ui.dashboard.adapter.MoviesLoadStateAdapter
import com.rasel.moviedb.R
import com.rasel.moviedb.data.preference.AppPrefsManager
import com.rasel.moviedb.databinding.FragmentMovieListBinding
import com.rasel.moviedb.ui.dashboard.adapter.MovieListAdapter
import com.rasel.moviedb.ui.dashboard.view_model.HomeViewModel
import com.rasel.moviedb.utils.changeStatusBarColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private lateinit var adapter: MovieListAdapter

    @Inject
    lateinit var preferences: AppPrefsManager
    private lateinit var binding: FragmentMovieListBinding

    private var searchJob: Job? = null
    private val viewModel: HomeViewModel by viewModels()

    private fun search(query: String) {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchRepo(query).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().changeStatusBarColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.backgroundColor
            ), true
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: ""
        search(query)
        initSearch(query)
        binding.retryButton.setOnClickListener { adapter.retry() }

        binding.imgFilter.setOnClickListener {
            showMenu(it, R.menu.filter_menu)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, binding.searchRepo.text?.trim().toString())
    }

    private fun initAdapter() {

        adapter = MovieListAdapter { id, checked, position ->
            viewModel.addAsFavorite(id, checked)
            adapter.notifyItemChanged(position)
        }


        binding.recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = MoviesLoadStateAdapter { adapter.retry() },
            footer = MoviesLoadStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener { loadState ->

            // show empty list
            val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
            showEmptyList(isListEmpty)

            // Only show the list if refresh succeeds, either from the the local db or the remote.
            // binding.recyclerView.isVisible =  loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.progressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails and there are no items.
            binding.retryButton.isVisible =
                loadState.mediator?.refresh is LoadState.Error && adapter.itemCount == 0
            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    context,
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun initSearch(query: String) {
        binding.searchRepo.setText(query)

        binding.btnSearch.setOnClickListener { updateRepoListFromInput() }
        binding.searchRepo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
        binding.searchRepo.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }

        // Scroll to top when the list is refreshed from network.
        lifecycleScope.launch {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.recyclerView.scrollToPosition(0) }
        }
    }

    private fun updateRepoListFromInput() {
        binding.searchRepo.text?.trim().let {
            if (!it.isNullOrEmpty()) {
                search(it.toString())
            }
        }
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            binding.emptyList.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.emptyList.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(v.context, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            // Respond to menu item click.
            when(menuItem.itemId){
                R.id.option_favorite -> { return@setOnMenuItemClickListener true}
                R.id.option_rating -> { return@setOnMenuItemClickListener true}
                else -> {return@setOnMenuItemClickListener false}
            }
        }
        popup.setOnDismissListener {
            // Respond to popup being dismissed.
        }
        // Show the popup menu.
        popup.show()
    }

    companion object {

        private const val LAST_SEARCH_QUERY: String = "last_search_query"

        @JvmStatic
        fun newInstance(): MovieListFragment {
            return MovieListFragment()
        }
    }
}