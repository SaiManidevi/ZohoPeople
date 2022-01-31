package com.zohointerviewtest.zohopeople.detailscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.zohointerviewtest.zohopeople.R
import com.zohointerviewtest.zohopeople.databinding.FragmentDetailBinding
import com.zohointerviewtest.zohopeople.utils.Utils
import com.zohointerviewtest.zohopeople.utils.Utils.getCoordinateString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val args by navArgs<DetailFragmentArgs>()
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Bind the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        setUpPersonImage(args.imageUrl)
        binding.tvPersonName.text = args.personName
        binding.tvDetailEmail.text = getString(R.string.email_string, args.email)
        binding.tvDetailPhone.text = getString(R.string.phone_string, args.phoneNum)
        val dob = args.dob.substringBefore("T")
        binding.tvDetailDob.text = getString(R.string.dob_string, dob)
        binding.tvDetailGender.text = getString(R.string.gender_string, args.gender)
        viewModel.getLatestWeather(
            getCoordinateString(
                args.latitude.toDouble(),
                args.logitutde.toDouble()
            )
        )
        observeWeather()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbarDetail)
        setHasOptionsMenu(true)
    }

    private fun observeWeather() {
        viewModel.currentWeather.observe(viewLifecycleOwner) { weather ->
            weather?.let {
                // Set the temperature text
                binding.tvDetailTemp.text = getString(R.string.temp_cel, it.celsius)
                // Set the AirQuality Index text
                binding.tvDetailAqi.text = getString(
                    R.string.aqi_string,
                    Utils.getAirQualityMeasure(it.aqi_index),
                    it.aqi_index
                )
                // Set the image icon
                Glide.with(this)
                    .load(Utils.getIconUrl(it.icon_url))
                    .placeholder(R.drawable.ic_night)
                    .error(R.drawable.ic_sad_cloud)
                    .into(binding.ivDetailWeatherIcon)
            }
        }
    }

    private fun setUpPersonImage(imageUrl: String) {
        Glide.with(requireContext())
            .load(imageUrl)
            .placeholder(R.drawable.placeholder_male)
            .error(R.drawable.placeholder_male)
            .into(binding.ivPersonLargeImage)
    }

}