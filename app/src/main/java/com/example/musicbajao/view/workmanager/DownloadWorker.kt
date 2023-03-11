package com.example.musicbajao.view.workmanager

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.work.Worker
import androidx.work.WorkerParameters


// To run worker class without coroutine
class DownloadWorker(val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val uri = inputData.getString("uri")
        val songName = inputData.getString("nameOfSong")
        Log.d("Values", uri.toString())
        Log.d("Values", songName.toString())
        downloadSongs(context, uri, songName)
        Log.d("Work Manager", "Running")
        return Result.success()
    }

    private fun downloadSongs(context: Context, uri: String?, songName: String?) {
        val uriToDownload =
        Uri.parse(uri)
//            Uri.parse("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf")



        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
        val request = DownloadManager.Request(uriToDownload)

        request.setTitle("Music Downloaded")
        request.setDescription("Android Data download using DownloadManager.")

        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)


        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            songName
        )

        val extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        request.setMimeType(mimeType)
//        request.setMimeType("application/")
        downloadManager?.enqueue(request)
    }
}