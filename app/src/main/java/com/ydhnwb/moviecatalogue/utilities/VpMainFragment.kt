package com.ydhnwb.moviecatalogue.utilities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.api.load
import com.ydhnwb.moviecatalogue.R
import kotlinx.android.synthetic.main.vp_detail_main.view.*

class VpMainFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.vp_detail_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            view.vp_main_book_image.load(Constants.API_IMAGE_ENDPOINT+"${it.getString("POSTER")}")
            view.vp_main_rating.rating = it.getFloat("RATING")/2
            view.vp_main_book_title.text = it.getString("TITLE")
            view.vp_main_vote_count.text = "(${it.getLong("VOTE_COUNT")})"
        }
    }

    companion object {
        fun instance(poster : String,rating : Float,title : String, vote_count : Long) : VpMainFragment{
            val bundle = Bundle()
            val vpmain = VpMainFragment()
            bundle.putString("POSTER", poster)
            bundle.putFloat("RATING", rating)
            bundle.putString("TITLE", title)
            bundle.putLong("VOTE_COUNT", vote_count)
            vpmain.arguments = bundle
            return vpmain
        }
    }
}
