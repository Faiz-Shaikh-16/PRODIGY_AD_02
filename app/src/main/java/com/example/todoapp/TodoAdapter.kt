package com.example.todoapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.ItemTodoBinding
import java.text.SimpleDateFormat
import java.util.*

class TodoAdapter(val list: List<TodoModel>) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemId(position: Int): Long {
        return list[position].id
    }

    class TodoViewHolder(private val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todoModel: TodoModel) {
            with(binding) {
                binding.tvTitle.text = todoModel.title
                binding.tvTask.text = todoModel.description
                binding.tvCategory.text = todoModel.category
                updateTime(todoModel.time)
                updateDate(todoModel.date)
            }
        }

        private fun updateTime(time: String) {
            val myFormat = "h:mm a"
            val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
            binding.tvTime.text = sdf.format(SimpleDateFormat(myFormat, Locale.getDefault()).parse(time))
        }

        private fun updateDate(date: String) {
            val myFormat = "EEE, d MMM yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
            binding.tvDate.text = sdf.format(SimpleDateFormat(myFormat, Locale.getDefault()).parse(date))
        }
    }
}
