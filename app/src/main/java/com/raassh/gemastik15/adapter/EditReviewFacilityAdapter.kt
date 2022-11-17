package com.raassh.gemastik15.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raassh.gemastik15.R
import com.raassh.gemastik15.api.response.FacilityQualityItem
import com.raassh.gemastik15.databinding.EditFacilityReviewItemBinding
import com.raassh.gemastik15.local.db.Facility
import java.lang.reflect.Field

class EditReviewFacilitiesAdapter(private val reviewFacilities: List<FacilityQualityItem>)
    : ListAdapter<Facility, EditReviewFacilitiesAdapter.EditReviewFacilitiesViewHolder>(DIFF_CALLBACK) {
    var onButtonCheckedListener: ((Facility, Int, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = EditReviewFacilitiesViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.edit_facility_review_item, parent, false)
    )

    override fun onBindViewHolder(holder: EditReviewFacilitiesViewHolder, position: Int) {
        val facility = getItem(position)
        holder.bind(facility)
    }

    inner class EditReviewFacilitiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = EditFacilityReviewItemBinding.bind(itemView)
        private val context = itemView.context

        init {
            binding.toggleGroupFacility.addOnButtonCheckedListener() { _, checkedId, isChecked ->
                onButtonCheckedListener?.invoke(getItem(adapterPosition), checkedId, isChecked)
            }
        }

        fun bind(facility: Facility) {
            binding.apply {
                tvFacilityName.text = context.getString(getResId(facility.name, R.string::class.java))
                tvFacilityDescription.text = context.getString(getResId(facility.description, R.string::class.java))
                imgFacilityIcon.setImageResource(getResId(facility.icon, R.drawable::class.java))

                toggleGroupFacility.apply {
                    when (reviewFacilities.find { it.facility == facility.name }?.quality) {
                        0 -> check(R.id.btn_facility_review_none)
                        1 -> check(R.id.btn_facility_review_bad)
                        2 -> check(R.id.btn_facility_review_good)
                    }
                }
            }
        }
    }

    private fun getResId(resName: String, c: Class<*>): Int {
        return try {
            val idField: Field = c.getDeclaredField(resName)
            idField.getInt(idField)
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Facility>() {
            override fun areItemsTheSame(oldItem: Facility, newItem: Facility): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Facility, newItem: Facility): Boolean {
                return oldItem == newItem
            }
        }
    }
}