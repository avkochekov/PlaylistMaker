package av.kochekov.playlistmaker.playlist_editor.presentation

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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.databinding.FragmentPlaylistEditorBinding
import av.kochekov.playlistmaker.playlist_editor.presentation.models.PlaylistEditorState
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistEditorFragment : Fragment() {
    private var _binding: FragmentPlaylistEditorBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<PlaylistEditorViewModel>()

    companion object {
        const val PLAYLIST_UUID = "CurrentTrackId"
        fun createArgs(uuid: String = ""): Bundle =
            bundleOf(PLAYLIST_UUID to uuid)
    }

    private val pickMediaLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            changeArtwork()
        } else {
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
                Toast.makeText(activity, R.string.playlist_imageNotSelected, Toast.LENGTH_LONG)
                    .show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(requireActivity()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        confirmExit()
                    }
                }
            )
        }

        requireArguments().getString(PlaylistEditorFragment.PLAYLIST_UUID)?.let { data ->
            viewModel.load(data as String)
        }


        viewModel.fragmentState().observe(viewLifecycleOwner, Observer {
            when (it) {
                is PlaylistEditorState.Data -> {
                    val data = (it as PlaylistEditorState.Data).data
                    setArtwork(Uri.parse(data.artwork))
                    binding.playlistName.setText(data.name)
                    binding.playlistDescription.setText(data.description)

                    if (data.uuid.isEmpty())
                        binding.createPlaylist.text = getString(R.string.playlist_create)
                    else
                        binding.createPlaylist.text = getString(R.string.playlist_edite)

                    updateInputFields(
                        binding.playlistName,
                        binding.playlistNameLayout
                    )
                    updateInputFields(
                        binding.playlistDescription,
                        binding.playlistDescriptionLayout
                    )
                }
                is PlaylistEditorState.Saved -> {
                    val data = (it as PlaylistEditorState.Saved).data
                    if (viewModel.isNewPlaylist())
                        showSnackBar(
                            getString(
                                R.string.playlist_created,
                                data.name
                            )
                        )
                    findNavController().navigateUp()
                }
            }
        })

        with(binding) {
            playlistArtwork.setOnClickListener {
                pickMediaLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            toolbar.setNavigationOnClickListener {
                confirmExit()
            }
            with(createPlaylist) {
                isEnabled = binding.playlistName.text?.isNotEmpty() ?: false
                setOnClickListener {
                    viewModel.savePlayList()
                }
            }
            setInputFieldListener(
                field = playlistName,
                fieldLayout = playlistNameLayout
            )

            setInputFieldListener(
                field = playlistDescription,
                fieldLayout = playlistDescriptionLayout
            )
            setInputFieldsTextChangedListeners()
        }

    }

    private fun changeArtwork() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun confirmExit() {
        if (viewModel.isChanged() && viewModel.isNewPlaylist()) {
            activity?.let { activity ->
                MaterialAlertDialogBuilder(activity)
                    .setTitle(R.string.playlist_confirmExit_title)
                    .setMessage(R.string.playlist_confirmExit_message)
                    .setPositiveButton(R.string.playlist_confirmExit_positiveButton) { _, _ ->
                        findNavController().navigateUp()
                    }
                    .setNegativeButton(R.string.playlist_confirmExit_negativeButton) { _, _ ->
                        // Do nothing
                    }
                    .show()
            }
        } else {
            findNavController().navigateUp()
        }
    }

    private fun setArtwork(uri: Uri) {
        binding.playlistArtwork.let { artwork ->
            Glide.with(this)
                .load(uri)
                .transform(RoundedCorners(artwork.resources.getDimensionPixelSize(R.dimen.playlist_artworkRadius)))
                .fitCenter()
                .into(artwork)
        }
        binding.playlistArtwork.tag = uri.toString()
    }

    private fun showSnackBar(text: String) {
        var snackbar = Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateInputFields(
        field: TextInputEditText,
        fieldLayout: TextInputLayout
    ) {
        updateInputField(
            fieldLayout,
            field.isFocused || field.text?.isNotEmpty() == true
        )
    }

    private fun updateInputField(
        fieldLayout: TextInputLayout,
        isActive: Boolean
    ) {
        if (isActive) {
            fieldLayout.setBoxStrokeColorStateList(
                AppCompatResources.getColorStateList(
                    requireContext(),
                    R.color.box_stroke_color_blue
                )
            )
            fieldLayout.hintTextColor = AppCompatResources.getColorStateList(
                requireContext(),
                R.color.box_hint_color_blue
            )
            fieldLayout.defaultHintTextColor = AppCompatResources.getColorStateList(
                requireContext(),
                R.color.YP_blue
            )
        } else {
            fieldLayout.setBoxStrokeColorStateList(
                AppCompatResources.getColorStateList(
                    requireContext(),
                    R.color.box_stroke_color
                )
            )
            fieldLayout.hintTextColor = AppCompatResources.getColorStateList(
                requireContext(),
                R.color.box_hint_color
            )
            fieldLayout.defaultHintTextColor = AppCompatResources.getColorStateList(
                requireContext(),
                R.color.box_stroke_default_color
            )
        }
    }

    private fun setInputFieldListener(
        field: TextInputEditText,
        fieldLayout: TextInputLayout
    ) {
        field.setOnFocusChangeListener { _, hasFocus ->
            updateInputField(fieldLayout, hasFocus || field.text?.isNotEmpty() == true)
        }
    }

    private fun setInputFieldsTextChangedListeners() {
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
                count: Int
            ) {
                viewModel.setName(s.toString())
                binding.createPlaylist.isEnabled = s.toString().isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

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
                count: Int
            ) {
                viewModel.setDescription(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}