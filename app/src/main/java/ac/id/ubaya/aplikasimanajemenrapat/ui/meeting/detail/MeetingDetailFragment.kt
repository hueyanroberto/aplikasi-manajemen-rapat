package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Attachment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Meeting
import ac.id.ubaya.aplikasimanajemenrapat.databinding.FragmentMeetingDetailBinding
import ac.id.ubaya.aplikasimanajemenrapat.utils.getFile
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class MeetingDetailFragment(
    private val meeting: Meeting,
    private val token: String
) : Fragment() {

    private var _binding: FragmentMeetingDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MeetingDetailViewModel by viewModels()

    private val files = arrayListOf<MultipartBody.Part>()
    private lateinit var adapterAttachment: AttachmentAdapter
    private val attachments = arrayListOf<Attachment>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeetingDetailBinding.inflate(layoutInflater, container, false)
        attachments.addAll(meeting.attachments)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textMeetingDetailDescription.text = meeting.description
        adapterAttachment = AttachmentAdapter(attachments)
        binding.recyclerAttachment.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapterAttachment
        }

        binding.textAttachmentAdd.visibility = View.GONE
        binding.viewEmpty.root.visibility = if (attachments.isEmpty()) View.VISIBLE else View.GONE

        if (meeting.userRole == 1) {
            binding.textAttachmentAdd.visibility = View.VISIBLE
            binding.textAttachmentAdd.setOnClickListener {
                getFile()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun prepareFilePart(partName: String, fileUri: Uri): MultipartBody.Part {
        val file = getFile(requireActivity().applicationContext, fileUri)
        val requestFile = file.asRequestBody()
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    private fun getFile() {
        val intent = Intent().apply {
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            action = Intent.ACTION_GET_CONTENT
            type = "*/*"
        }
        val chooser = Intent.createChooser(intent, "Choose File")
        launcherIntent.launch(chooser)
    }

    private val launcherIntent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            val data = it.data
            if (data != null) {
                if (data.clipData != null) {
                    data.clipData?.let { clipData ->
                        val count = clipData.itemCount
                        for (i in 0 until count) {
                            val uri = clipData.getItemAt(i).uri
                            files.add(prepareFilePart("file$i", uri))
                        }
                    }
                } else {
                    val uri = data.data as Uri
                    files.add(prepareFilePart("file", uri))
                }

                uploadFile()
            }
        }
    }

    private fun uploadFile() {
        val meetingId = meeting.id.toString().toRequestBody("text/plain".toMediaType())
        lifecycleScope.launch {
            viewModel.uploadFile(token, files, meetingId).collect { attachmentResource ->
                when(attachmentResource) {
                    is Resource.Loading -> {
                        binding.progressBarAttachment.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBarAttachment.visibility = View.GONE
                        Toast.makeText(context, getString(R.string.file_uploaded), Toast.LENGTH_SHORT).show()
                        val listAttachment = attachmentResource.data
                        listAttachment?.let {
                            val size = attachments.size
                            attachments.addAll(it)
                            adapterAttachment.notifyItemInserted(size)
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBarAttachment.visibility = View.GONE
                        Snackbar.make(binding.recyclerAttachment, resources.getString(R.string.internal_error_message), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(requireActivity().getColor(R.color.secondary_dark))
                            .setTextColor(requireActivity().getColor(R.color.white))
                            .setAction(getString(R.string.refresh)) {
                                uploadFile()
                            }
                            .show()
                    }
                }
            }
        }
    }
}