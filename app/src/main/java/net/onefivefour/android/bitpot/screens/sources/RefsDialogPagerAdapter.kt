package net.onefivefour.android.bitpot.screens.sources

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.paging.ExperimentalPagingApi
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.onefivefour.android.bitpot.data.model.RefType
import net.onefivefour.android.bitpot.screens.sources.RefTypeFragment.Companion.ARG_REF_TYPE_ORDINAL

/**
 * This the pager adapter to cycle between source [RefType]s
 * E.g. Branches or Tags of a repository.
 */
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class RefsDialogPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = RefType.values().size

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
        val fragment = RefTypeFragment()

        bundle.putInt(ARG_REF_TYPE_ORDINAL, position)
        fragment.arguments = bundle

        return fragment
    }
}