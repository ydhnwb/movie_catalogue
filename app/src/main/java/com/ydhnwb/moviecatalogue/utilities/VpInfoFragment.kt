package com.ydhnwb.moviecatalogue.utilities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ydhnwb.moviecatalogue.R
import kotlinx.android.synthetic.main.vp_detail_info.view.*

class VpInfoFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.vp_detail_info, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            view.vp_detail_desc.text = it.getString("DESC")
        }
    }

    companion object {
        fun instance(desc : String) : VpInfoFragment{
            val bundle = Bundle()
            val vpinfo = VpInfoFragment()
            bundle.putString("DESC", desc)
            vpinfo.arguments = bundle
            return vpinfo
        }
    }
}