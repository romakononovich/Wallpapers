package xyz.romakononovich.wallcano.mvp.model

import com.google.gson.annotations.SerializedName

data class Wallpapers(

	@field:SerializedName("hits")
	val hits: ArrayList<HitsItem>,

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("totalHits")
	val totalHits: Int
)