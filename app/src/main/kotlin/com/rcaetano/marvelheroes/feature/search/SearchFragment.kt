package com.rcaetano.marvelheroes.feature.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.rcaetano.marvelheroes.R
import com.rcaetano.marvelheroes.data.model.ScreenState.*
import com.rcaetano.marvelheroes.feature.common.CharacterAdapter
import com.rcaetano.marvelheroes.hideKeyboard
import com.rcaetano.marvelheroes.showToast
import com.rcaetano.marvelheroes.subscribe
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.android.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val viewModel by viewModel<SearchViewModel>()
    private val adapter = CharacterAdapter(::loadNextPage)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupLiveData()
    }

    private fun setupLiveData() {
        viewModel.screenState.subscribe(this) { screenState ->
            when (screenState) {
                SUCCESS -> {
                    progress_bar.visibility = GONE
                    recycler_view.visibility = VISIBLE
                }
                LOADING -> {
                    progress_bar.visibility = VISIBLE
                    recycler_view.visibility = INVISIBLE
                }
                ERROR -> {
                    progress_bar.visibility = GONE
                    showToast(R.string.unknown_error)
                }
            }
        }

        viewModel.characterList.subscribe(this) { characterList ->
            adapter.setData(characterList)
            adapter.notifyDataSetChanged()
        }

        viewModel.shouldLoadMore.subscribe(this) { shouldLoadMore ->
            adapter.showLoadMore = shouldLoadMore
        }
    }

    private fun setupViews() {
        val manager = GridLayoutManager(context, 4)
        recycler_view.layoutManager = manager
        recycler_view.adapter = adapter
        recycler_view.setHasFixedSize(true)

        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) =
                if (adapter.isLoadMore(position)) 4 else 1
        }

        search_view.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                viewModel.searchCharacters(search_view.text.toString())
                search_view.hideKeyboard()
                return@setOnKeyListener true
            }

            return@setOnKeyListener false
        }
    }

    private fun loadNextPage() {
        viewModel.loadNextPage()
    }
}
