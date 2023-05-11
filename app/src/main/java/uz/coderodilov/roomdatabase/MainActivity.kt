package uz.coderodilov.roomdatabase

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import uz.coderodilov.roomdatabase.adapter.UserAdapter
import uz.coderodilov.roomdatabase.databinding.ActivityMainBinding
import uz.coderodilov.roomdatabase.databinding.CustomDialogAddUserBinding
import uz.coderodilov.roomdatabase.model.User
import uz.coderodilov.roomdatabase.room.UserDatabase
import java.io.FileNotFoundException

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var dialogBinding:CustomDialogAddUserBinding

    private lateinit var userAdapter: UserAdapter
    private  var userList:MutableList<User> = ArrayList()

    private lateinit var dbHelper:UserDatabase
    private val requestCodeImage = 100
    private var bitmap: Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListSetAdapter()
        rvSetSwipeCallBack()

    }

    private fun rvSetSwipeCallBack() {
        val swipeCallback = object :ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                dbHelper.userDao().deleteUser(userList[viewHolder.adapterPosition])
                userList.removeAt(viewHolder.adapterPosition)
                userAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            }

        }

        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvUser)
    }


    private fun initListSetAdapter() {

        dbHelper = UserDatabase.getDatabaseInstance(this)
        userList = dbHelper.userDao().getAllUsers().toMutableList()

        userAdapter = UserAdapter(userList)
        binding.rvUser.adapter = userAdapter

        binding.btnAddUser.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(this)
            dialogBinding = CustomDialogAddUserBinding.inflate(layoutInflater)

            builder.setView(dialogBinding.root)
            val dialog = builder.create()
            dialog.show()

            dialogBinding.imageChooser.setOnClickListener{
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivityForResult(Intent.createChooser(intent, "SELECT A PICTURE" ), 100)
            }

            dialogBinding.btnSaveAndExit.setOnClickListener{
                val name = dialogBinding.edUserName.text.toString()
                val lastName = dialogBinding.edUserLastName.text.toString()

                if (bitmap != null){
                    saveUser(name, lastName, "20:55 PM", bitmap!!)
                    bitmap = null
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Please select image...", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == requestCodeImage){
            try {
                val inputStream = contentResolver.openInputStream(data?.data!!)
                bitmap = BitmapFactory.decodeStream(inputStream)
                dialogBinding.imageChooser.setImageBitmap(bitmap)
            } catch (e: FileNotFoundException){
                e.printStackTrace()
            }
        }
    }


    private fun saveUser(name:String, lastName:String, time:String, image: Bitmap){
        val user = User(name, lastName, time, image)

        userList.add(0, user)
        userAdapter.notifyItemInserted(0)
        dbHelper.userDao().insertUser(user)

        Toast.makeText(this, "User inserted successfully", Toast.LENGTH_SHORT).show()
    }



}