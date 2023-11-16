package av.kochekov.playlistmaker.playlist.presentation

import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.databinding.FragmentPlaylistBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class
PlaylistFragment : Fragment() {
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<PlaylistViewModel>()

    private val pickMediaLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            changeArtwork()
        }
        else {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(activity, R.string.playlist_rationale, Toast.LENGTH_LONG).show()
            }
        }
    }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.setArtwork(uri.toString())
                setArtwork(uri)
            } else {
                Toast.makeText(activity,R.string.playlist_imageNotSelected, Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    confirmExit()
                }
            }
            )

        viewModel.fragmentState().observe(viewLifecycleOwner, Observer {
            setArtwork(Uri.parse(it.artwork))
            binding.playlistName.setText(it.name)
            binding.playlistDescription.setText(it.description)
        })

        binding.toolbar.setNavigationOnClickListener {
            confirmExit()
        }

        binding.playlistName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int) {
                viewModel.setName(s.toString())
                binding.createPlaylist.isEnabled = s.toString().isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.playlistName.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus || binding.playlistName.text?.isNotEmpty() == true){
                binding.playlistNameLayout.setBoxStrokeColorStateList(AppCompatResources.getColorStateList(requireContext(), R.color.box_stroke_color_blue))
                binding.playlistNameLayout.defaultHintTextColor = AppCompatResources.getColorStateList(requireContext(), R.color.box_stroke_color_blue)
            } else {
                binding.playlistNameLayout.setBoxStrokeColorStateList(AppCompatResources.getColorStateList(requireContext(), R.color.box_stroke_color))
                binding.playlistNameLayout.defaultHintTextColor = AppCompatResources.getColorStateList(requireContext(), R.color.box_stroke_color)
            }
        }

        binding.playlistDescription.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus || binding.playlistName.text?.isNotEmpty() == true){
                binding.playlistDescriptionLayout.setBoxStrokeColorStateList(AppCompatResources.getColorStateList(requireContext(), R.color.box_stroke_color_blue))
                binding.playlistDescriptionLayout.hintTextColor = AppCompatResources.getColorStateList(requireContext(), R.color.box_stroke_color_blue)
            } else {
                binding.playlistDescriptionLayout.setBoxStrokeColorStateList(AppCompatResources.getColorStateList(requireContext(), R.color.box_stroke_color))
                binding.playlistDescriptionLayout.hintTextColor = AppCompatResources.getColorStateList(requireContext(), R.color.box_stroke_color)
            }
        }

        binding.playlistDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int) {
                viewModel.setDescription(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.artwork.setOnClickListener {
            pickMediaLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        binding.createPlaylist.let {
            it.isEnabled = binding.playlistName.text?.isNotEmpty() ?: false
            it.setOnClickListener {
                viewModel.savePlayList()

                showSnackBar("Плейлист ${binding.playlistName.text} создан")

                findNavController().navigateUp()
            }
        }
    }

    private fun changeArtwork(){
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun confirmExit(){
        if (viewModel.isChanged()) {
            activity?.let {
                MaterialAlertDialogBuilder(it)
                    .setTitle(R.string.playlist_confirmExit_title)
                    .setMessage(R.string.playlist_confirmExit_message)
                    .setPositiveButton(R.string.playlist_confirmExit_positiveButton) { dialog, which ->
                        findNavController().navigateUp()
                    }
                    .setNegativeButton(R.string.playlist_confirmExit_negativeButton) { dialog, which ->
                        // Do nothing
                    }
                    .show()
            }
        } else {
            findNavController().navigateUp()
        }
    }

    private fun setArtwork(uri: Uri){
        binding.artwork?.let { it ->
            Glide.with(this)
                .load(uri)
                .transform(RoundedCorners(it.resources.getDimensionPixelSize(R.dimen.playlist_artworkRadius)))
                .fitCenter()
                .into(it)
        }
        binding.artwork?.tag = uri.toString()
    }

    private fun showSnackBar(text: String){
        var snackbar = Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG)
//        snackbar.view.setBackgroundColor(resources.getColor(R.color.snackbarBgColor))
//        snackbar.setTextColor(resources.getColor(R.color.snackbarTextColor))
        snackbar.show()
    }

    private fun getFieldColor(isEmpty: Boolean): ColorStateList {
        val colorRes = if (isEmpty){
            R.color.YP_black
        } else {
            R.color.YP_blue
        }
        return AppCompatResources.getColorStateList(requireContext(), colorRes)
    }
}