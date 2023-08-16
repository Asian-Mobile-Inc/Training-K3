package com.example.asian.ex01_use_coroutines.ui.component.home

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asian.R
import com.example.asian.databinding.ActivityEx01UserManagementBinding
import com.example.asian.ex01_use_coroutines.data.model.User
import com.example.asian.ex01_use_coroutines.ui.component.home.adapters.UserAdapter
import com.example.asian.ex01_use_coroutines.ui.component.launch.UserViewModel

class Ex01UserManagementActivity : AppCompatActivity() {
    private lateinit var mUserViewModel: UserViewModel
    private lateinit var mUserAdapter: UserAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mCaseAction: CaseAction = CaseAction.ALL
    private lateinit var mBindingMain: ActivityEx01UserManagementBinding
    private lateinit var mMutableListUser: MutableList<User>

    enum class CaseAction {
        SHOW_ALL, ALL
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVariable()
        initRecyclerView()
        handleClick()
        displayUsers()
        showErrorState()
        progessloadingData()
    }

    private fun progessloadingData() {
        mUserViewModel.mDataLoading.observe(this) { isLoading ->
            if (isLoading) {
                mBindingMain.pbLoading.visibility = View.VISIBLE
            } else {
                mBindingMain.pbLoading.visibility = View.GONE
            }
        }
    }

    private fun showErrorState() {
        mUserViewModel.mToastMessage.observe(this) { message ->
            message?.let {
                makeToast(it)
            }
        }
    }

    private fun initRecyclerView() {
        mUserAdapter = UserAdapter { user ->
            deleteUser(user)
        }
        mRecyclerView = mBindingMain.rcvUsers
        mLayoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = mUserAdapter
    }

    private fun deleteUser(user: User) {
        mUserViewModel.deleteOneUser(user.mIdUser)
        displayUsers()
    }

    private fun initVariable() {
        mBindingMain = DataBindingUtil.setContentView(this, R.layout.activity_ex01_user_management)
        mUserViewModel = UserViewModel(this)
        mMutableListUser = mutableListOf()
    }

    private fun handleClick() {
        mBindingMain.btnAdd.setOnClickListener {
            insertUserData()
        }

        mBindingMain.btnDeleteAll.setOnClickListener {
            showDialog()
        }

        mBindingMain.btnShowAll.setOnClickListener {
            showAllFindData()
        }
    }

    private fun showDialog() {
        val dialogClickListener =
            DialogInterface.OnClickListener { _: DialogInterface?, which: Int ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        deleteAllData()
                    }
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete all data?")
            .setPositiveButton("Yes", dialogClickListener)
            .setNegativeButton("No", dialogClickListener)
            .show()
    }

    private fun showAllFindData() {
        mCaseAction = CaseAction.SHOW_ALL
        mUserViewModel.findAllUsers(mBindingMain.edtUserName.text.toString())
            .observe(this) { users ->
                mUserAdapter.setData(users)
            }
    }

    private fun deleteAllData() {
        mCaseAction = CaseAction.ALL
        mUserViewModel.deleteAllUsers()
    }

    private fun insertUserData() {
        mCaseAction = CaseAction.ALL
        val userName: String = mBindingMain.edtUserName.text.toString()
        val userAge: String = mBindingMain.edtAge.text.toString()
        mUserViewModel.insertUserData(
            userName, userAge
        )
        clearEdt()
    }

    private fun displayUsers() {
        if (mCaseAction == CaseAction.ALL) {
            mUserViewModel.getAllUsers().observe(this) { users ->
                mMutableListUser.clear()
                mMutableListUser.addAll(users)
                mUserAdapter.setData(mMutableListUser)
            }
        }
    }

    private fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun clearEdt() {
        mBindingMain.edtUserName.text.clear()
        mBindingMain.edtAge.text.clear()
    }
}
