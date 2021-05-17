package com.hillywave.calmdown

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color

import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Vibrator
import android.preference.PreferenceManager
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdView
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
	private var v: Vibrator? = null
	private var curntTimer: CountDownTimer? = null
	private var curntTimerB = false
	private var BOOKSHELF_ROWS = 0
	private var BOOKSHELF_COLUMNS = 0
	private var linkToImage: String? = null
	private var linkToImageSize: String? = null
	private var duration_vibrate = 0
	private var cnt_hint_number = 0
	private var color_name: String? = null
	private var numClick = 0
	private var allNumClick = 0
	private lateinit var tableLayout: TableLayout
	private lateinit var linearLayout: LinearLayout
	private lateinit var tvCountHintText: TextView
	private var currentTime: Long = 0
	private var currentTime2: Long = 0
	private var stopWatch: Timer? = null
	private var dialogMainMenu: Dialog? = null
	private var styleDialog: Dialog? = null
	private var mAdView: AdView? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		v = getSystemService(VIBRATOR_SERVICE) as Vibrator
		val prefs = PreferenceManager.getDefaultSharedPreferences(this)
		numClick = 0
		allNumClick = prefs.getInt("all_click", 0)
		tableLayout = findViewById(R.id.table)
		tvCountHintText = findViewById<TextView>(R.id.cnt_hint)
		tvCountHintText.setOnClickListener { mainMenuDialogShow(2) }
		val menuBtn = findViewById<TextView>(R.id.menuBtn)
		menuBtn.setOnClickListener { mainMenuDialogShow(1) }
		val menuBtn2 = findViewById<TextView>(R.id.menuBtn2)
		menuBtn2.setOnClickListener { showStyleDialog() }
		val menuBtn3 = findViewById<TextView>(R.id.menuBtn3)
		menuBtn3.setOnClickListener { showInfo() }
		currentTime = System.currentTimeMillis()
		currentTime2 = 0
		duration_vibrate = prefs.getInt("duration_vibrate", 25)
		loadRewardedVideoAd()
		mAdView = findViewById(R.id.adView)
		//AdRequest adRequest = new AdRequest.Builder().addTestDevice("AA6A32E3DAB63751E5ED3035550DF5D1").build();
		//mAdView.loadAd(adRequest);
		drawTable()
		curntTimer = object : CountDownTimer(3000, 1000) {
			override fun onTick(l: Long) {}
			override fun onFinish() {
				curntTimerB = false
				tableLayout.removeAllViews()
				drawTable()
			}
		}
	}

	private fun mainMenuDialogShow(i: Int) {
		dialogMainMenu = Dialog(this@MainActivity)
		val view1 = View.inflate(this, R.layout.menu_dialog, null)
		linearLayout = view1.findViewById(R.id.linear_layout)
		Objects.requireNonNull(dialogMainMenu!!.window)!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
		when (i) {
			1 -> {

				//dialogMainMenu.setContentView(R.layout.menu_dialog);
				val btnResume = Button(this)
				btnResume.setText(R.string.menu_text_resume)
				//btnResume.setWidth(270);
				//btnResume.setHeight(60);
				btnResume.background = ContextCompat.getDrawable(this, R.drawable.buttonshape)
				val btnStyle = Button(this)
				btnStyle.setText(R.string.menu_text_setstyle)
				btnStyle.background = ContextCompat.getDrawable(this, R.drawable.buttonshape)
				val btnAboutUs = Button(this)
				btnAboutUs.setText(R.string.menu_text_about)
				btnAboutUs.background = ContextCompat.getDrawable(this, R.drawable.buttonshape)
				val btnInfo = Button(this)
				btnInfo.setText(R.string.menu_text_information)
				btnInfo.background = ContextCompat.getDrawable(this, R.drawable.buttonshape)
				val btnRateUs = Button(this)
				btnRateUs.setText(R.string.menu_text_rateus)
				btnRateUs.background = ContextCompat.getDrawable(this, R.drawable.buttonshape)
				btnResume.setOnClickListener { dialogMainMenu!!.cancel() }
				btnAboutUs.setOnClickListener { showAboutUs() }
				btnInfo.setOnClickListener { showInfo() }
				btnStyle.setOnClickListener { showStyleDialog() }
				btnRateUs.setOnClickListener {
					startActivity(
						Intent(
							Intent.ACTION_VIEW,
							Uri.parse("market://details?id=com.hillywave.calmdown")
						)
					)
				}
				linearLayout.addView(
					btnResume, LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT
					)
				)
				linearLayout.addView(
					btnStyle, LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT
					)
				)
				linearLayout.addView(
					btnAboutUs, LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT
					)
				)
				linearLayout.addView(
					btnInfo, LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT
					)
				)
				linearLayout.addView(
					btnRateUs, LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT
					)
				)
			}
			2 -> {
				val btnReset = Button(this)
				btnReset.setText(R.string.menu_text_reset)
				btnReset.setOnClickListener {
					tvCountHintText.text = "0"
					cnt_hint_number = 0
					dialogMainMenu!!.cancel()
				}
				linearLayout.addView(
					btnReset, LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT
					)
				)
				btnReset.setPadding(100, 75, 100, 75)
				btnReset.setBackgroundColor(-0x1)
				btnReset.textSize = 20f
			}
			else -> {
			}
		}
		dialogMainMenu!!.setContentView(linearLayout)
		dialogMainMenu!!.show()
	}

	private fun showInfo() {
		val prefs = PreferenceManager.getDefaultSharedPreferences(this)
		val dialog = Dialog(this@MainActivity)
		val view = View.inflate(this, R.layout.info_dialog, null)
		val linearLayout = view.findViewById<LinearLayout>(R.id.linear_layout_info)
		val animationDrawable = linearLayout.background as AnimationDrawable
		animationDrawable.setEnterFadeDuration(2000)
		animationDrawable.setExitFadeDuration(4000)
		animationDrawable.start()
		dialog.setContentView(view)
		val alltimeClickedTextView = view.findViewById<TextView>(R.id.allTimeClickedTextView)
		val alltimeSelectedColorTextView = view.findViewById<TextView>(R.id.allTimeSelectedColorTextView)
		val alltimeSelectedImageTextView = view.findViewById<TextView>(R.id.allTimeSelectedImageTextView)
		val alltimeSelectedImageSizeTextView = view.findViewById<TextView>(R.id.allTimeSelectedImageSizeTextView)
		val alltimeSelectedVibrationTextView = view.findViewById<TextView>(R.id.allTimeSelectedVibrationTextView)
		val alltimeInAppTextView = view.findViewById<TextView>(R.id.allTimeInAppTextView)
		alltimeClickedTextView.append(Integer.toString(prefs.getInt("all_click", 0) + numClick))
		alltimeSelectedColorTextView.append(Integer.toString(prefs.getInt("all_select_color", 0)))
		alltimeSelectedImageTextView.append(Integer.toString(prefs.getInt("all_select_image", 0)))
		alltimeSelectedImageSizeTextView.append(Integer.toString(prefs.getInt("all_select_image_size", 0)))
		alltimeSelectedVibrationTextView.append(Integer.toString(prefs.getInt("all_select_vibration", 0)))
		val time = prefs.getLong("all_time_in_app", 0) + currentTime2
		val hours = time / 3600
		val minutes = time / 60
		@SuppressLint("DefaultLocale") val curTime = String.format("%02d : %02d", hours, minutes)
		alltimeInAppTextView.append(curTime)
		dialog.show()
	}

	private fun showStyleDialog() {
		val prefs = PreferenceManager.getDefaultSharedPreferences(this)
		val editor = prefs.edit()
		linkToImage = prefs.getString("link_to_image", "ic_launcher_foreground")
		linkToImageSize = prefs.getString("link_to_image_size", "30")
		duration_vibrate = prefs.getInt("duration_vibrate", 25)
		color_name = prefs.getString("color_name", "#000000")
		val view = View.inflate(this, R.layout.style_dialog, null)
		styleDialog = Dialog(this)
		val imgBtn = view.findViewById<ImageButton>(R.id.imgbtn)
		imgBtn.setImageResource(getImageId(this, linkToImage, linkToImageSize))
		imgBtn.setOnClickListener { showSelectImage() }
		val setColorView = view.findViewById<View>(R.id.set_color_view)
		setColorView.setBackgroundColor(Color.parseColor(color_name))
		val setColorLayout = view.findViewById<LinearLayout>(R.id.set_color_layout)
		setColorLayout.setOnClickListener { showSelectColor() }
		val seekBarSize = view.findViewById<SeekBar>(R.id.seekBar_size)
		seekBarSize.progress = linkToImageSize!!.toInt() / 30 - 1
		seekBarSize.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
			override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {}
			override fun onStartTrackingTouch(seekBar: SeekBar) {}
			override fun onStopTrackingTouch(seekBar: SeekBar) {
				val sizeImg = seekBar.progress
				if (sizeImg < 1) {
					editor.putString("link_to_image_size", "30")
					linkToImageSize = "30"
				} else if (sizeImg >= 1 && sizeImg < 2) {
					editor.putString("link_to_image_size", "60")
					linkToImageSize = "60"
				} else if (sizeImg >= 2 && sizeImg < 3) {
					editor.putString("link_to_image_size", "90")
					linkToImageSize = "90"
				} else if (sizeImg >= 3) {
					editor.putString("link_to_image_size", "120")
					linkToImageSize = "120"
				}
				editor.putInt("all_select_image_size", prefs.getInt("all_select_image_size", 0) + 1)
				editor.apply()
				styleDialog!!.cancel()
				showStyleDialog()
			}
		})
		val seekBarVibrate = view.findViewById<SeekBar>(R.id.seekBar_durationVibrate)
		seekBarVibrate.progress = duration_vibrate
		seekBarVibrate.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
			override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {}
			override fun onStartTrackingTouch(seekBar: SeekBar) {}
			override fun onStopTrackingTouch(seekBar: SeekBar) {
				val duration = seekBar.progress
				v!!.vibrate(duration.toLong())
				duration_vibrate = duration
				editor.putInt("duration_vibrate", duration)
				editor.putInt("all_select_vibration", prefs.getInt("all_select_vibration", 0) + 1)
				editor.apply()
			}
		})
		styleDialog!!.setContentView(view)
		styleDialog!!.show()
		styleDialog!!.setOnDismissListener {
			tableLayout!!.removeAllViews()
			drawTable()
		}
	}

	private fun showAboutUs() {
		val dialogAboutUs = Dialog(this@MainActivity)
		val view = View.inflate(this, R.layout.about_us, null)
		view.setOnClickListener { dialogAboutUs.cancel() }
		val linearLayout = view.findViewById<LinearLayout>(R.id.linear_layout_about_us)
		val textAboutUs = view.findViewById<TextView>(R.id.textAboutUs)
		textAboutUs.append(getString(R.string.aboutus_text) + (allNumClick + numClick))
		//LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
		val animationDrawable = linearLayout.background as AnimationDrawable
		animationDrawable.setEnterFadeDuration(2000)
		animationDrawable.setExitFadeDuration(4000)
		animationDrawable.start()
		dialogAboutUs.setContentView(view)
		dialogAboutUs.show()
	}

	private fun showSelectColor() {
		val allColorMassive: List<String>
		allColorMassive = ArrayList()
		allColorMassive.add(0, "#ffffff")
		allColorMassive.add(0, "#800080")
		allColorMassive.add(0, "#0000ff")
		allColorMassive.add(0, "#00ff00")
		allColorMassive.add(0, "#ffff00")
		allColorMassive.add(0, "#ff0000")
		allColorMassive.add(0, "#00ffff")
		allColorMassive.add(0, "#ffa500")
		allColorMassive.add(0, "#000000")
		dialogMainMenu = Dialog(this@MainActivity)
		val view1 = View.inflate(this, R.layout.select_image, null)
		val linearView = view1.findViewById<LinearLayout>(R.id.linear_layout_scroll)
		val layoutParams = LinearLayout.LayoutParams(100, 100)
		for (i in allColorMassive.indices) {
			val image = ImageView(this)
			val colorName = allColorMassive[i]
			image.layoutParams = layoutParams
			image.setBackgroundColor(Color.parseColor(colorName))
			linearView.addView(image)
			image.setOnClickListener {
				val pref = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
				val editor = pref.edit()
				editor.putInt("all_select_color", pref.getInt("all_select_color", 0) + 1)
				editor.apply()
				setNewColor(colorName)
				drawTable()
				styleDialog!!.cancel()
				dialogMainMenu!!.cancel()
				showStyleDialog()
			}
		}
		dialogMainMenu!!.setContentView(view1)
		dialogMainMenu!!.show()
	}

	private fun setNewColor(nameColor: String) {
		val prefs = PreferenceManager.getDefaultSharedPreferences(this)
		val editor = prefs.edit()
		editor.putString("color_name", nameColor)
		editor.apply()
	}

	private fun showSelectImage() {
		val allImgMassive: MutableMap<String, Array<String?>>
		allImgMassive = LinkedHashMap()
		val a = arrayOfNulls<String>(2)
		a[0] = "1"
		a[1] = "1"
		allImgMassive["sad_frog"] = a
		a[0] = "1"
		a[1] = "1"
		allImgMassive["bird"] = a
		a[0] = "1"
		a[1] = "1"
		allImgMassive["twitter"] = a
		a[0] = "1"
		a[1] = "1"
		allImgMassive["apple"] = a
		a[0] = "1"
		a[1] = "1"
		allImgMassive["heart"] = a
		a[0] = "1"
		a[1] = "1"
		allImgMassive["broken_heart"] = a
		a[0] = "1"
		a[1] = "1"
		allImgMassive["trollface"] = a
		a[0] = "1"
		a[1] = "1"
		allImgMassive["fourchan"] = a
		a[0] = "1"
		a[1] = "1"
		allImgMassive["dog_meme"] = a
		a[0] = "1"
		a[1] = "1"
		allImgMassive["bellisimo"] = a
		a[0] = "1"
		a[1] = "1"
		allImgMassive["meme_blin"] = a
		a[0] = "1"
		a[1] = "1"
		allImgMassive["black_cat_icon"] = a
		a[0] = "1"
		a[1] = "1"
		allImgMassive["sonya_swarm_cat"] = a
		a[0] = "1"
		a[1] = "1"
		allImgMassive["dog"] = a
		a[0] = "1"
		a[1] = "1"
		allImgMassive["digital_resistance"] = a
		a[0] = "1"
		a[1] = "1"
		allImgMassive["dog_second"] = a
		a[0] = "1"
		a[1] = "1"
		allImgMassive["sonik"] = a
		a[0] = "1"
		a[1] = "1"
		allImgMassive["shifer"] = a
		a[0] = "1"
		a[1] = "1"
		allImgMassive["ic_launcher_foreground"] = a
		a[0] = "1"
		a[1] = "1"
		allImgMassive["mem_ded"] = a
		dialogMainMenu = Dialog(this@MainActivity)
		val view1 = View.inflate(this, R.layout.select_image, null)
		val linearView = view1.findViewById<LinearLayout>(R.id.linear_layout_scroll)
		for ((key, value) in allImgMassive) {
			val image = ImageView(this)
			image.setImageResource(resources.getIdentifier(key + "_120", "drawable", applicationContext.packageName))
			linearView.addView(image)
			image.setOnClickListener {
				val pref = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
				val editor = pref.edit()
				editor.putInt("all_select_image", pref.getInt("all_select_image", 0) + 1)
				editor.apply()
				setNewImage(key, value[0], value[1])
				drawTable()
				styleDialog!!.cancel()
				dialogMainMenu!!.cancel()
				showStyleDialog()
			}
		}
		dialogMainMenu!!.setContentView(view1)
		dialogMainMenu!!.show()
	}

	private fun setNewImage(nameImg: String, height: String?, width: String?) {
		val prefs = PreferenceManager.getDefaultSharedPreferences(this)
		val editor = prefs.edit()
		editor.putString("link_to_image", nameImg)
		editor.putString("height_image", height)
		editor.putString("width_image", width)
		editor.apply()
	}

	private fun drawTable() {
		tableLayout!!.removeAllViews()
		val prefs = PreferenceManager.getDefaultSharedPreferences(this)
		linkToImage = prefs.getString("link_to_image", "ic_launcher_foreground")
		linkToImageSize = prefs.getString("link_to_image_size", "30")
		val height_image = java.lang.Float.valueOf(prefs.getString("height_image", "1"))
		val width_image = java.lang.Float.valueOf(prefs.getString("width_image", "1"))
		color_name = prefs.getString("color_name", "#000000")
		val color = Color.parseColor(color_name)
		val displayMetrics = DisplayMetrics()
		windowManager.defaultDisplay.getMetrics(displayMetrics)
		val width = displayMetrics.widthPixels
		val height = displayMetrics.heightPixels - 300
		BOOKSHELF_ROWS = (height / (convertDpToPixels(linkToImageSize!!.toFloat(), this) * (height_image / width_image))).toInt()
		BOOKSHELF_COLUMNS = width / convertDpToPixels(linkToImageSize!!.toFloat(), this)

		//Log.d("Test", "Columns: " + String.valueOf(BOOKSHELF_COLUMNS));
		//Log.d("Test", "Rows: " + String.valueOf(BOOKSHELF_ROWS));
		for (i in 0 until BOOKSHELF_ROWS) {
			val tableRow = TableRow(this)
			tableRow.layoutParams = TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.WRAP_CONTENT
			)
			tableRow.setBackgroundResource(R.drawable.ic_launcher_background_30)
			for (j in 0 until BOOKSHELF_COLUMNS) {
				val imageView = ImageView(this)
				imageView.setImageResource(getImageId(this, linkToImage, linkToImageSize))
				imageView.setOnClickListener(this)
				imageView.setColorFilter(color)
				tableRow.addView(imageView, j)
			}
			tableLayout!!.addView(tableRow, i)
		}
	}

	override fun onClick(view: View) {
		numClick++

		//every 125-th click with 40% chance show ad
		if (numClick % 100 == 0) {
			val rand = Math.random()
			Log.d("Test", "Show AD. Chance: $rand")
			if (rand <= 0.4) {
				//if (mRewarderVideoAd.isLoaded()) {
				//	mRewarderVideoAd.show();
				//	loadRewardedVideoAd();
				//}
			}
		}
		view.setOnClickListener(null)
		//Log.d("Test", "Click");
		cnt_hint_number++
		 tvCountHintText!!.text = cnt_hint_number.toString()
		v!!.vibrate(duration_vibrate.toLong())
		view.animate()
			.alpha(0f)
			.setDuration(200)
			.setListener(object : AnimatorListenerAdapter() {
				override fun onAnimationEnd(animation: Animator) {
					view.visibility = View.INVISIBLE
				}
			})
		if (!curntTimerB) {
			curntTimer!!.start()
			curntTimerB = true
		} else {
			curntTimer!!.cancel()
			curntTimer!!.start()
		}
	}

	override fun onStop() {
		val prefs = PreferenceManager.getDefaultSharedPreferences(this)
		allNumClick = prefs.getInt("all_click", 0)
		allNumClick = numClick + allNumClick
		numClick = 0
		val editor = prefs.edit()
		editor.putInt("all_click", allNumClick)
		editor.putLong("all_time_in_app", prefs.getLong("all_time_in_app", 0) + currentTime2)
		currentTime2 = 0
		stopWatch!!.cancel()
		editor.apply()
		super.onStop()
	}

	public override fun onResume() {
		//mRewarderVideoAd.resume(this);
		super.onResume()
	}

	public override fun onPause() {
		//mRewarderVideoAd.pause(this);
		super.onPause()
	}

	override fun onStart() {
		super.onStart()
		stopWatch = Timer()
		stopWatch!!.schedule(object : TimerTask() {
			override fun run() {
				runOnUiThread { currentTime2++ }
			}
		}, 1000, 1000)
	}

	public override fun onDestroy() {
		super.onDestroy()
		//mRewarderVideoAd.destroy(this);
	}

	private fun loadRewardedVideoAd() {
		//mRewarderVideoAd = MobileAds.getRewardedVideoAdInstance(this);
		//mRewarderVideoAd.setRewardedVideoAdListener(this);
		//mRewarderVideoAd.loadAd("ca-app-pub-8505706241717212/6163392753", new AdRequest.Builder().build());
		//Test ad video mRewarderVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
	}

	//@Override
	//public void onRewarded(RewardItem reward) {
	//	Toast.makeText(this, R.string.thanks_ad_text, Toast.LENGTH_SHORT).show();
	//}
	//
	//@Override
	//public void onRewardedVideoAdLeftApplication() {
	//}
	//
	//@Override
	//public void onRewardedVideoAdClosed() {
	//	showCryDialog();
	//}
	//
	//@Override
	//public void onRewardedVideoAdFailedToLoad(int errorCode) {
	//}
	//
	//@Override
	//public void onRewardedVideoAdLoaded() {
	//}
	//
	//@Override
	//public void onRewardedVideoAdOpened() {
	//}
	//
	//@Override
	//public void onRewardedVideoStarted() {
	//}
	private fun showCryDialog() {
		val dialog = Dialog(this)
		val view = View.inflate(this, R.layout.cry_dialog, null)
		dialog.setContentView(view)
		dialog.show()
		view.setOnClickListener { dialog.cancel() }
	}

	companion object {
		fun getImageId(context: Context, imageName: String?, imageSize: String?): Int {
			return context.resources.getIdentifier("drawable/" + imageName + "_" + imageSize, null, context.packageName)
		}

		fun convertDpToPixels(dp: Float, context: Context): Int {
			return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
		}
	}
}