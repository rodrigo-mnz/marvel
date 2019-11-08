package com.rcaetano.marvelheroes.feature.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.rcaetano.marvelheroes.R
import com.rcaetano.marvelheroes.data.model.Character
import com.rcaetano.marvelheroes.feature.detail.DetailFragment
import kotlinx.android.synthetic.main.fragment_favourite.*
import kotlinx.android.synthetic.main.fragment_home.*

class FavouriteFragment : Fragment() {

    private val adapter = FavouriteAdapter(::onItemClick, ::loadList)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favourite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadList()
    }

    private fun setupRecyclerView() {
        recycler_view.layoutManager = GridLayoutManager(context, 4)
        recycler_view.adapter = adapter
        recycler_view.setHasFixedSize(true)
    }

    fun loadList() {
        adapter.list = FavouriteHelper.getFavourites(context)
        adapter.notifyDataSetChanged()
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
}
