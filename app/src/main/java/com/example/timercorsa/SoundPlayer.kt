import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri

class SoundPlayer(private val context: Context) {
    private val mediaPlayer: MediaPlayer = MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )
    }

    fun playSound(soundUri: Uri) {
        try {
            mediaPlayer.reset() // Resetta il MediaPlayer per caricare un nuovo suono
            mediaPlayer.setDataSource(context, soundUri)
            mediaPlayer.prepare()
            mediaPlayer.start()

            // Rilascia automaticamente al termine
            mediaPlayer.setOnCompletionListener {
                it.reset() // Puoi scegliere di resettare per caricare nuovi suoni in futuro
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun release() {
        mediaPlayer.release()
    }
}
