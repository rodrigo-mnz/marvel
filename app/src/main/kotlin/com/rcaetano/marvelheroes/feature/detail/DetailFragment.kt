package com.rcaetano.marvelheroes.feature.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.transition.*
import com.rcaetano.marvelheroes.R
import com.rcaetano.marvelheroes.data.model.Character
import com.rcaetano.marvelheroes.data.model.Item
import com.rcaetano.marvelheroes.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.*
import org.koin.android.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {

    companion object {
        private const val ARG_CHARACTER = "ARG_CHARACTER"

        fun newInstance(character: Character): DetailFragment {
            val args = Bundle()
            args.putParcelable(ARG_CHARACTER, character)

            val fragment = DetailFragment()

            val anim = TransitionSet()
                .addTransition(ChangeBounds())
                .addTransition(ChangeTransform())
                .addTransition(ChangeImageTransform())
            anim.ordering = TransitionSet.ORDERING_TOGETHER

            fragment.sharedElementEnterTransition = anim
            fragment.exitTransition = Fade()

            fragment.arguments = args

            return fragment
        }
    }

    private val viewModel by viewModel<DetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val character = arguments?.getParcelable<Character>(ARG_CHARACTER)

        character?.let {
            bindCharacter(character)
        }
    }

    private fun bindCharacter(character: Character) {
        Picasso.get().load(buildThumbnailUri(character))
            .into(img_character)

        txt_name.text = character.name
        if (character.description.isEmpty()) {
            txt_description.visibility = GONE
        } else {
            txt_description.text = character.description
        }
        txt_uri.text = character.urls.firstOrNull {
            it.type == "detail"
        }?.url

        showFirstThree(character.comics.items, txt_comics_title, container_comics)
        showFirstThree(character.events.items, txt_events_title, container_events)
        showFirstThree(character.stories.items, txt_stories_title, container_stories)
        showFirstThree(character.series.items, txt_series_title, container_series)
    }

    private fun showFirstThree(list: List<Item>, title: TextView, container: LinearLayout) {
        if (list.isNotEmpty()) {
            title.visibility = VISIBLE
            container.visibility = VISIBLE

            list.take(3).forEach { item ->
                val txtItem = container.inflate(R.layout.item_detail) as TextView
                txtItem.text = item.name
                container.addView(txtItem)
            }
        }
    }

    private fun buildThumbnailUri(character: Character) =
        "${character.thumbnail.path}/portrait_xlarge.${character.thumbnail.extension}"
}
