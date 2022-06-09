package com.example.medic.Presentation.View

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medic.DI.ServiceLocator
import com.example.medic.Domain.Model.Comment
import com.example.medic.Domain.Model.Post
import com.example.medic.Domain.Model.User
import com.example.medic.MainActivity
import com.example.medic.Presentation.View.Adapters.CommentListAdapter
import com.example.medic.Presentation.View.CustomViews.CustomRecycleView
import com.example.medic.Presentation.ViewModel.CommentViewModel
import com.example.medic.Presentation.ViewModel.PostViewModel
import com.example.medic.R
import com.example.medic.databinding.PostFragmentBinding
import java.time.LocalDateTime

class PostFragment: Fragment() {
    private var mViewModel: PostViewModel? = null
    private lateinit var cViewModel: CommentViewModel
    private lateinit var mBinding: PostFragmentBinding

    private var recyclerView: RecyclerView? = null

    private var deleteButtonBoolean: Boolean? = null
    private lateinit var currentPost: Post

    private var commentListAdapter: CommentListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(PostViewModel::class.java)
        cViewModel = ViewModelProvider(this).get(CommentViewModel::class.java)
        commentListAdapter = CommentListAdapter()
        if (arguments != null) {
            currentPost = ServiceLocator.gson!!
                .fromJson<Post>(requireArguments().getString("Post"), Post::class.java)
            mViewModel!!.post = currentPost
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = PostFragmentBinding.inflate(layoutInflater, container, false)
        recyclerView = mBinding.commentRecycler
        (recyclerView as CustomRecycleView).layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        cViewModel.getCommentsByPostId(currentPost.id)!!
            .observe(viewLifecycleOwner, Observer { commList: List<Comment?>? ->
                commentListAdapter!!.setDataAndActivity(
                    commList
                )
                (recyclerView as CustomRecycleView).adapter = commentListAdapter
                (recyclerView as CustomRecycleView).setHasFixedSize(true)
            })
        mBinding.buttonSend.setOnClickListener {
            if (!(mBinding.editTextComment.text.toString() == "")) {
                cViewModel.addComment(
                    mBinding.editTextComment.text.toString(),
                    ServiceLocator.user.firstName,
                    LocalDateTime.now().toString(),
                    currentPost.id
                )!!.observe(viewLifecycleOwner, Observer { aBoolean: Boolean? ->
                    cViewModel.getCommentsByPostId(currentPost.id)!!.observe(
                        viewLifecycleOwner,
                        Observer { commList: List<Comment?>? ->
                            commentListAdapter!!.setDataAndActivity(
                                commList
                            )
                            Toast.makeText(
                                requireActivity().applicationContext,
                                "Comment added",
                                Toast.LENGTH_SHORT
                            ).show()
                            (recyclerView as CustomRecycleView).adapter = commentListAdapter
                            mBinding.editTextComment.text.clear()
                        }
                    )
                })
            }
        }
        setHasOptionsMenu(true)
        deleteButtonBoolean = false
        when (ServiceLocator.user.role) {
            User.Role.Administrator, User.Role.Moderator -> deleteButtonBoolean = true
            User.Role.User, User.Role.Guest -> deleteButtonBoolean = false
            else -> {}
        }
        if (mViewModel!!.post != null) {
            mBinding.postTitle.text = mViewModel!!.post!!.title
            mBinding.postBody.text = mViewModel!!.post!!.body
        }
        commentListAdapter = CommentListAdapter()
        return mBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.post_fragment, menu)
        val item: MenuItem = menu.findItem(R.id.miDelete)
        item.isVisible = (deleteButtonBoolean)!!
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miDelete -> {
                mViewModel!!.deletePost(currentPost)!!
                    .observe(viewLifecycleOwner) {
                        Log.d("LOGS", "Deleted post result: " + it)
                        Navigation.findNavController((activity as MainActivity?)!!.mBinding!!.navHostFragment)
                            .popBackStack()
                    }
            }
        }
        return true
    }

    fun sendIntentLogic() {
        /*mBinding.fab.setOnClickListener((View v) -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "https://rf.medic_post/" + mViewModel.getPost().getId());
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });*/
    }
}