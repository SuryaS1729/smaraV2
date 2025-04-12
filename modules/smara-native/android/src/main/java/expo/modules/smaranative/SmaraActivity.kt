package expo.modules.smaranative

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import android.media.AudioManager
import android.media.ToneGenerator

class SmaraActivity : Activity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val receivedText = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)?.toString()
    if (receivedText != null) {
      try {
        val dbPath = "/data/user/0/com.surya7314.smaraV2/files/SQLite/smara.db"
        val db = SQLiteDatabase.openOrCreateDatabase(dbPath, null)

        db.execSQL("""
          CREATE TABLE IF NOT EXISTS Words (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            word TEXT,
            createdAt INTEGER
          );
        """)

        val values = ContentValues().apply {
          put("word", receivedText)
          put("createdAt", System.currentTimeMillis())
        }

        val cursor = db.rawQuery("SELECT COUNT(*) FROM Words WHERE word = ?", arrayOf(receivedText))
        cursor.moveToFirst()
        val exists = cursor.getInt(0) > 0
        cursor.close()

       if (!exists) {
             db.insert("Words", null, values)
             vibrateSuccess()
             playToneSaved()
             Toast.makeText(this, "Saving: $receivedText", Toast.LENGTH_SHORT).show()
      } else {
            vibrateDuplicatePattern()
            playToneDuplicate()
            Toast.makeText(this, "Already saved: $receivedText", Toast.LENGTH_SHORT).show()
      }

        db.close()
      } catch (e: Exception) {
        Toast.makeText(this, "Error saving: ${e.message}", Toast.LENGTH_LONG).show()
      }
    }

    finish()
  }

  private fun playToneSaved() {
  val toneGen = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)
  toneGen.startTone(ToneGenerator.TONE_SUP_RADIO_ACK, 200)
}

  private fun playToneDuplicate() {
    val toneGen = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)
    toneGen.startTone(ToneGenerator.TONE_PROP_NACK, 220)
  }  


  private fun vibrateSuccess() {
    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    val effect = VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE)
    vibrator.vibrate(effect)
  }

  private fun vibrateDuplicatePattern() {
    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    val pattern = longArrayOf(0, 40, 60, 40) // wait 0ms, vibrate 40ms, pause 60ms, vibrate 40ms
    val effect = VibrationEffect.createWaveform(pattern, -1) // -1 = don't repeat
    vibrator.vibrate(effect)
  }
}