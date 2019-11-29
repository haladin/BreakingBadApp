package com.exmaple.breakingbadcharacters.data

import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.exmaple.breakingbadcharacters.R
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Character() : Parcelable {
    @SerializedName("char_id")
    @Expose
    var charId: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("birthday")
    @Expose
    var birthday: String? = null
    @SerializedName("occupation")
    @Expose
    var occupation: List<String>? = null
    @SerializedName("img")
    @Expose
    var img: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("nickname")
    @Expose
    var nickname: String? = null
    @SerializedName("appearance")
    @Expose
    var appearance: List<Int>? = null
    @SerializedName("portrayed")
    @Expose
    var portrayed: String? = null
    @SerializedName("category")
    @Expose
    var category: String? = null
    @SerializedName("better_call_saul_appearance")
    @Expose
    var betterCallSaulAppearance: List<Int>? = null

    fun getOccupations(): String = this.occupation?.let { it.joinToString() } ?: run { "" }
    fun getSeasons(): String = this.appearance?.let { it.joinToString() } ?: run { "" }
    fun getStatusColor(): Int {
        return if (status == "Alive"){
            Color.parseColor("#2E7D32")
        } else {
            Color.parseColor("#ff1744")
        }
    }

    constructor(parcel: Parcel) : this() {
        charId = parcel.readValue(Int::class.java.classLoader) as? Int
        name = parcel.readString()
        birthday = parcel.readString()
        occupation = parcel.createStringArrayList()
        appearance = parcel.createIntArray()?.toList()
        img = parcel.readString()
        status = parcel.readString()
        nickname = parcel.readString()
        portrayed = parcel.readString()
        category = parcel.readString()
    }

    override fun toString(): String {
        return "Character(name=$name, category=$category)"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(charId)
        parcel.writeString(name)
        parcel.writeString(birthday)
        parcel.writeStringList(occupation)
        parcel.writeIntArray(appearance?.toIntArray())
        parcel.writeString(img)
        parcel.writeString(status)
        parcel.writeString(nickname)
        parcel.writeString(portrayed)
        parcel.writeString(category)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Character> {
        override fun createFromParcel(parcel: Parcel): Character {
            return Character(parcel)
        }

        override fun newArray(size: Int): Array<Character?> {
            return arrayOfNulls(size)
        }

        @BindingAdapter("actorImage")
        @JvmStatic
        fun loadImage(imageView: ImageView, imageURL: String?) {
            Glide.with(imageView.context)
                .setDefaultRequestOptions(
                    RequestOptions()
                        .centerCrop()
                )
                .load(imageURL)
                .placeholder(R.drawable.ic_aspect_ratio_black_24dp)
                .into(imageView)
        }

        @BindingAdapter("actorImageBig")
        @JvmStatic
        fun loadImageBig(imageView: ImageView, imageURL: String?) {
            Glide.with(imageView.context)
                .setDefaultRequestOptions(
                    RequestOptions()
                        .centerCrop()
                        .transform(RoundedCorners(16))
                )
                .load(imageURL)
                .placeholder(R.drawable.ic_aspect_ratio_black_24dp)
                .into(imageView)
        }
    }


}