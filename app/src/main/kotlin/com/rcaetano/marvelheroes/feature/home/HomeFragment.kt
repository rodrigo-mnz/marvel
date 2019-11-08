package com.rcaetano.marvelheroes.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.rcaetano.marvelheroes.R
import com.rcaetano.marvelheroes.data.model.Character
import com.rcaetano.marvelheroes.data.model.ScreenState
import com.rcaetano.marvelheroes.feature.common.CharacterAdapter
import com.rcaetano.marvelheroes.feature.detail.DetailFragment
import com.rcaetano.marvelheroes.showToast
import com.rcaetano.marvelheroes.subscribe
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val viewModel by viewModel<HomeViewModel>()
    private val adapter = CharacterAdapter(::loadNextPage, ::onItemClick)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupLiveData()
    }

    private fun setupLiveData() {
        viewModel.screenState.subscribe(this) { screenState ->
            when (screenState) {
                ScreenState.SUCCESS -> {
                    progress_bar.visibility = View.GONE
                    recycler_view.visibility = View.VISIBLE
                }
                ScreenState.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                    recycler_view.visibility = View.INVISIBLE
                }
                ScreenState.ERROR -> {
                    progress_bar.visibility = View.GONE
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

        if (viewModel.characterList.value.isNullOrEmpty()) {
            viewModel.fetchCharacters()
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
    }

    private fun onItemClick(character: Character, imageView: ImageView) {
        val detailFragment = DetailFragment.newInstance(character)

        fragmentManager?.apply {
            beginTransaction()
                .setReorderingAllowed(true)
                .addSharedElement(imageView, "CharacterImage")
                .replace(
                    R.id.fragment_container,
                    detailFragment,
                    DetailFragment::class.java.simpleName
                )
                .addToBackStack(null)
                .commit()
        }
    }

    private fun loadNextPage() {
        viewModel.loadNextPage()
    }
}
