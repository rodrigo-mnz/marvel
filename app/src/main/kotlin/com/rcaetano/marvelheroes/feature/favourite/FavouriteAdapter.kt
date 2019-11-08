package com.rcaetano.marvelheroes.feature.favourite

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.rcaetano.marvelheroes.R
import com.rcaetano.marvelheroes.data.model.Character
import com.rcaetano.marvelheroes.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_character.view.*

class FavouriteAdapter(
    private val onItemClick: (character: Character, imageView: ImageView) -> Unit,
    private val refreshList: () -> Unit
) :
    RecyclerView.Adapter<CharacterItem>() {

    var list: List<Character> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CharacterItem(
            parent.inflate(R.layout.item_character),
            onItemClick,
            refreshList
        )

    override fun getItemCount() = list.size


    override fun onBindViewHolder(holder: CharacterItem, position: Int) {
        holder.bind(list[position])
    }
}

class CharacterItem(
    private val view: View,
    private val onItemClick: (character: Character, imageView: ImageView) -> Unit,
    private val refreshList: () -> Unit
) : RecyclerView.ViewHolder(view) {

    fun bind(character: Character) {
        view.setOnClickListener {
            ViewCompat.setTransitionName(view.img_character, "image$adapterPosition")
            onItemClick(character, view.img_character)
        }

        Picasso.get().load(buildThumbnailUri(character))
            .placeholder(R.drawable.portrait_thumb_placeholder)
            .into(view.img_character)

        view.txt_name.text = character.name
        view.img_favourite.setImageResource(R.drawable.ic_favorite_full)

        view.img_favourite.setOnClickListener {
            FavouriteHelper.removeFavouriteCharacter(character, view.context)
            refreshList()
        }
    }

    private fun buildThumbnailUri(character: Character) =
        "${character.thumbnail.path}/portrait_xlarge.${character.thumbnail.extension}"
}