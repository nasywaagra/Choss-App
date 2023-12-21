package com.project.chossapp.ui.recommendation

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.project.chossapp.data.model.Cloth
import com.project.chossapp.data.model.Clothes
import com.project.chossapp.databinding.ActivityDetailRecommendationBinding
import com.project.chossapp.ui.camera.CameraActivity
import com.project.chossapp.ui.shop.ShopActivity
import com.project.chossapp.util.Dimen
import com.project.chossapp.util.hide
import com.project.chossapp.util.toast
import com.project.chossapp.util.visible
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class DetailRecommendationActivity : AppCompatActivity() {

    private var _binding: ActivityDetailRecommendationBinding? = null
    private val binding get() = _binding!!
    private var clothes: Clothes? = null
    private var color: Int? = null
    private val viewModel by viewModels<RecommendationViewModel>()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }


    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            CameraActivity.REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(CameraActivity.REQUIRED_PERMISSION)
        }

        setupPage()
        setColorAction()

        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnCamera.setOnClickListener {
            startActivity(
                Intent(
                    this, CameraActivity::class.java
                )
            )
        }
        binding.tvClickHere.setOnClickListener {
            startActivity(
                Intent(this, ShopActivity::class.java).also {
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
        }
    }

    private fun setColorAction() {

    }

    private fun setupPage() {
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Dimen.CLOTH, Clothes::class.java)
        } else {
            intent.getParcelableExtra(Dimen.CLOTH)
        }

        val index = intent.getIntExtra("index", 0)

        data?.let { clothes1 ->
            clothes = clothes1
            with(binding) {
                ivCloth.setImageResource(clothes1.photos!![index-1])
                tvClothName.text = clothes1.name
                tvColorTemperature.text = clothes1.colorTemperature

                cvColor1.setOnClickListener {
                    checkHandling(1)
                    color = it.solidColor
                    toast(color.toString())
                }
                cvColor2.setOnClickListener {
                    checkHandling(2)
                    color = it.solidColor
                    toast(color.toString())
                }
                cvColor3.setOnClickListener {
                    checkHandling(3)
                    color = it.solidColor
                    toast(color.toString())
                }
                cvColor4.setOnClickListener {
                    checkHandling(4)
                    color = it.solidColor
                    toast(color.toString())
                }
                cvColor5.setOnClickListener {
                    checkHandling(5)
                    color = it.solidColor
                    toast(color.toString())
                }

                var isPressed = false
                cvAddToMycloth.setOnClickListener {
                    if (!isPressed) {
                        viewModel.addToMyCloth(
                            Cloth(
                                name = clothes1.name!!,
                                photo = clothes1.photos[index-1]
                            )
                        )
                        binding.tvAddCloth.text = ""
                        binding.icCheck.visible()
                        isPressed = true
                    }
                }
            }
        }
    }

    private fun checkHandling(index: Int) {
        when (index) {
            1 -> {
                binding.icCheck1.visible()
                binding.icCheck2.hide()
                binding.icCheck3.hide()
                binding.icCheck4.hide()
                binding.icCheck5.hide()
            }
            2 -> {
                binding.icCheck1.hide()
                binding.icCheck2.visible()
                binding.icCheck3.hide()
                binding.icCheck4.hide()
                binding.icCheck5.hide()
            }
            3 -> {
                binding.icCheck1.hide()
                binding.icCheck2.hide()
                binding.icCheck3.visible()
                binding.icCheck4.hide()
                binding.icCheck5.hide()
            }
            4 -> {
                binding.icCheck1.hide()
                binding.icCheck2.hide()
                binding.icCheck3.hide()
                binding.icCheck4.visible()
                binding.icCheck5.hide()
            }
            5 -> {
                binding.icCheck1.hide()
                binding.icCheck2.hide()
                binding.icCheck3.hide()
                binding.icCheck4.hide()
                binding.icCheck5.visible()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}