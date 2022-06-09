package com.example.medic.Presentation.View

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medic.DI.ServiceLocator
import com.example.medic.Domain.Model.Post
import com.example.medic.Domain.Model.User
import com.example.medic.MainActivity
import com.example.medic.Presentation.View.Adapters.PostListAdapter
import com.example.medic.Presentation.ViewModel.PostListViewModel
import com.example.medic.R
import com.example.medic.databinding.PostListFragmentBinding

class PostList : Fragment() {
    lateinit var postListViewModel: PostListViewModel

    var userVerified: Boolean = false

    lateinit var recyclerView: RecyclerView
    lateinit var postListAdapter: PostListAdapter

    var mainMenu: Menu? = null
    lateinit var search: MenuItem
    lateinit var searchView: SearchView
    lateinit var binding: PostListFragmentBinding

    var RC_SIGN_IN: Int = 6

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PostListFragmentBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)
        recyclerView = binding.postListRecycler
        recyclerView.layoutManager = GridLayoutManager(context, 1)
        postListViewModel = ViewModelProvider(this).get(PostListViewModel::class.java)
        postListAdapter = PostListAdapter()
        postListViewModel.getPostList()!!
            .observe(viewLifecycleOwner) { postList: List<Post?>? ->
                postListAdapter.setDataAndActivity(postList)
                recyclerView.adapter = postListAdapter
            }
        if (ServiceLocator.user.role !== User.Role.Guest) {
            binding.buttonPanel.visibility = View.VISIBLE
        } else {
            binding.buttonPanel.visibility = View.GONE
        }
        binding.buttonPanel.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                Navigation.findNavController(v).navigate(R.id.action_postList_to_addPost)
            }
        })
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        mainMenu = menu
        if (userVerified) mainMenu!!.findItem(R.id.miProfile)
            .setIcon(R.drawable.ic_baseline_assignment_return_24)
        search = menu.findItem(R.id.search)
        searchView = search.actionView as SearchView
        search.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                postListViewModel.getPostList()!!
                    .observe(viewLifecycleOwner) { postList: List<Post?>? ->
                        postListAdapter.setDataAndActivity(
                            postList
                        )
                        recyclerView.adapter = postListAdapter
                    }
                return true
            }
        })
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                postListViewModel.getAllPostsLike(query)!!
                    .observe(viewLifecycleOwner) { postList: List<Post?>? ->
                        postListAdapter.setDataAndActivity(
                            postList
                        )
                        recyclerView.adapter = postListAdapter
                    }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miProfile -> {
                if (ServiceLocator.user.role === User.Role.Guest) {
                    authWithLoginAndPassword()
                } else {
                    goToProfileFragment()
                }
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*if (ServiceLocator.getInstance().getGoogleLogic().auth
                .requestCodeCheck(requestCode, RC_SIGN_IN, data)){*/
        if (ServiceLocator.user.role !== User.Role.Guest
        ) {
            mainMenu!!.findItem(R.id.miProfile).setIcon(R.drawable.ic_baseline_assignment_return_24)
            binding.buttonPanel.visibility = View.VISIBLE
            userVerified = true
            ServiceLocator.googleLogic!!.auth.getLastAuthInfo(activity as MainActivity?)
        }
    }

    fun auth() {
        val signInIntent: Intent = ServiceLocator.googleLogic!!.auth.intent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun authWithLoginAndPassword() {
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_postList_to_authFragment)
    }

    fun goToProfileFragment() {
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_postList_to_profileFragment)
        /*ServiceLocator.getInstance().getGoogleLogic().auth.signOut((MainActivity)getActivity());
        binding.buttonPanel.setVisibility(View.GONE);*/
    }
}