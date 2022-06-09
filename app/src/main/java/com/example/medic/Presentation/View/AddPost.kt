package com.example.medic.Presentation.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.medic.Domain.Model.Post
import com.example.medic.Presentation.ViewModel.AddPostViewModel
import com.example.medic.databinding.AddPostFragmentBinding

class AddPost constructor() : Fragment() {
    var operationChecker: Int = 0
    var correctTitle: String? = null
    var correctBody: String? = null
    var correctTags: String? = null
    var images: List<String?> = ArrayList()
    var addPostViewModel: AddPostViewModel? = null
    var mBinding: AddPostFragmentBinding? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addPostViewModel = ViewModelProvider(this).get(AddPostViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = AddPostFragmentBinding.inflate(getLayoutInflater(), container, false)
        mBinding!!.buttonAdd.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if ((mBinding!!.title.getText().length == 0) || (
                            mBinding!!.body.getText().length == 0) || (
                            mBinding!!.tags.getText().length == 0)
                ) {
                    Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_LONG).show()
                } else {
                    operationChecker = 0
                    addPostViewModel!!.getCorrectedText(mBinding!!.title.getText().toString())
                        .observe(getViewLifecycleOwner(), object : Observer<String?> {
                            override fun onChanged(s: String?) {
                                if (operationChecker == 2) {
                                    correctTitle = s
                                    addPostViewModel!!.addPost(
                                        correctTitle,
                                        correctBody,
                                        correctTags,
                                        images
                                    )!!.observe(getViewLifecycleOwner(), object : Observer<Post?> {
                                        override fun onChanged(post: Post?) {
                                            Navigation.findNavController(v).popBackStack()
                                        }
                                    })
                                    operationChecker = 0
                                } else {
                                    correctTitle = s
                                    operationChecker++
                                }
                            }
                        })
                    addPostViewModel!!.getCorrectedText(mBinding!!.body.getText().toString())
                        .observe(getViewLifecycleOwner(), object : Observer<String?> {
                            override fun onChanged(s: String?) {
                                if (operationChecker == 2) {
                                    correctBody = s
                                    addPostViewModel!!.addPost(
                                        correctTitle,
                                        correctBody,
                                        correctTags,
                                        images
                                    )!!.observe(getViewLifecycleOwner(), object : Observer<Post?> {
                                        override fun onChanged(post: Post?) {
                                            Navigation.findNavController(v).popBackStack()
                                        }
                                    })
                                    operationChecker = 0
                                } else {
                                    correctBody = s
                                    operationChecker++
                                }
                            }
                        })
                    addPostViewModel!!.getCorrectedText(mBinding!!.tags.getText().toString())
                        .observe(getViewLifecycleOwner(), object : Observer<String?> {
                            override fun onChanged(s: String?) {
                                if (operationChecker == 2) {
                                    correctTags = s
                                    addPostViewModel!!.addPost(
                                        correctTitle,
                                        correctBody,
                                        correctTags,
                                        images
                                    )!!.observe(getViewLifecycleOwner(), object : Observer<Post?> {
                                        override fun onChanged(post: Post?) {
                                            Navigation.findNavController(v).popBackStack()
                                        }
                                    })
                                    operationChecker = 0
                                } else {
                                    correctTags = s
                                    operationChecker++
                                }
                            }
                        })
                }
            }
        })

        /*mBinding.imagesLayout.setOnClickListener((View v) -> {
            if (mBinding.imageSlider.getVisibility() == View.GONE) {
                mBinding.imageSlider.setVisibility(View.VISIBLE);
                //mBinding.imageDropdownArrow.setImageResource(R.drawable.arrow_up);
            } else {
                mBinding.imageSlider.setVisibility(View.GONE);
                //mBinding.imageDropdownArrow.setImageResource(R.drawable.arrow_down);
            }
        });

        mBinding.imageSlider.setAdapter(new ImageSliderAdapter(images, true, ((MainActivity) requireActivity())));
        mBinding.imageSlider.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);*/return mBinding!!.getRoot()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
        addPostViewModel = null
    }

    companion object {
        fun newInstance(): AddPost {
            return AddPost()
        }
    }
}