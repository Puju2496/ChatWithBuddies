package com.example.chatwithbuddies.dialogs

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import com.example.chatwithbuddies.R
import com.example.chatwithbuddies.databinding.LayoutVoiceRecordBinding
import com.example.chatwithbuddies.viewmodel.ChatViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber
import java.io.IOException
import java.util.*

class VoiceRecordingDialog(private val context: Context, private val channelType: String, private val channelId: String, private val viewModel: ChatViewModel) {

    private val binding: LayoutVoiceRecordBinding = LayoutVoiceRecordBinding.inflate(LayoutInflater.from(context))

    private val dialog = MaterialAlertDialogBuilder(context)

    private var recorder: MediaRecorder? = null
    private var recordFile: String = ""
    private var isRecording = false

    init {
        dialog.setView(binding.root).setPositiveButton("SEND"
        ) { p0, p1 ->
            p0.dismiss()
            viewModel.sendVoiceMessage(channelType, channelId, recordFile)

        }.setNegativeButton("CANCEL"
        ) { p0, p1 ->
            recorder?.release()
            recorder = null
            p0.dismiss()
        }

        binding.record.setOnClickListener {
            if (isRecording) {
                isRecording = false
                binding.record.setImageResource(R.drawable.voice_icon_white)
                recorder?.pause()
            } else {
                isRecording = true
                if (recorder != null) {
                    recorder?.resume()
                } else {
                    val uuid = UUID.randomUUID().toString()
                    recordFile = "${context.externalCacheDir?.absolutePath}/$uuid.3pg"

                    recorder = MediaRecorder()
                    recorder?.apply {
                        setAudioSource(MediaRecorder.AudioSource.MIC)
                        setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                        setOutputFile(recordFile)
                        setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

                        try {
                            prepare()
                        } catch (e: IOException) {
                            Timber.e("$TAG media prepare exception ${e.message}")
                        }

                        start()
                    }
                }
                binding.record.setImageResource(R.drawable.pause_icon)
            }
        }

        binding.stop.setOnClickListener {
            binding.record.setImageResource(R.drawable.voice_icon_white)
            recorder?.release()
            recorder = null
        }
    }

    fun showDialog() {
        dialog.show()
    }

    companion object {
        private val TAG = VoiceRecordingDialog::class.java.name
    }
}