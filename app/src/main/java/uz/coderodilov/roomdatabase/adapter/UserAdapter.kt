package uz.coderodilov.roomdatabase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.coderodilov.roomdatabase.databinding.UserItemBinding
import uz.coderodilov.roomdatabase.model.User

class UserAdapter(private val userList: MutableList<User>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(user: User) {
            binding.tvUserFullName.text = user.name.plus(" " + user.lastName)
            binding.tvUserLastActiveTime.text = user.time
            binding.imageViewUserItem.setImageBitmap(user.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(userList[position])
    }

}