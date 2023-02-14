package ac.id.ubaya.aplikasimanajemenrapat.ui.main

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Organization
import ac.id.ubaya.aplikasimanajemenrapat.databinding.ItemOrganizationBinding
import ac.id.ubaya.aplikasimanajemenrapat.utils.BASE_ASSET_URL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class OrganizationAdapter(private val organizations: List<Organization>): RecyclerView.Adapter<OrganizationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_organization, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = organizations.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = organizations[position]
        holder.bind(data)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = ItemOrganizationBinding.bind(view)

        fun bind(data: Organization) {
            binding.textOrganizationName.text = data.name
            Glide.with(itemView.context)
                .load(BASE_ASSET_URL)
                .error(R.drawable.blank_profile)
                .into(binding.imageOrganization)
        }
    }
}