package com.example.finaldemo

import android.app.Application
import androidx.lifecycle.*
import com.example.finaldemo.database.Hotel
import com.example.finaldemo.database.HotelDatabase
import kotlinx.coroutines.launch

class MyViewModel(application: Application) : AndroidViewModel(application) {

    // get the reference to the database dao
    val database = HotelDatabase.getInstance(application).hotelDatabaseDao
    // get the list of all hotels (livedata)
    val hotelList = MediatorLiveData<List<Hotel>>()
    // get the selected hotel(livedata for easy observing)
    val selectedHotel = MutableLiveData<Hotel>()

    init {
        getAllHotels()
    }

    fun getAllHotels() { // set the livedata source of the hotelList to be all hotels
        hotelList.addSource(database.loadAllHotels()) { hotels ->
            hotelList.setValue(hotels)
        }
    }

    fun searchHotel(name: String) { // set the livedata source of the hotelList to be matched hotels
        hotelList.addSource(database.findHotels(name)) { hotels ->
            hotelList.setValue(hotels)
        }
    }

    fun initDB() {  //setup initial database
        viewModelScope.launch {
            repeat(3) {
                database.insertHotel(
                    Hotel(
                        "花蓮縣新城鄉順安村草林10之6號",
                        "煙波花蓮太魯閣",
                        R.drawable.photo1_0,
                        "煙波花蓮太魯閣將全新開幕，目前正值暖開幕階段，座落於山海交會處，鄰近花蓮太魯閣國家公園與湛藍無垠的太平洋，背山面海，旅人在此以最佳視野欣賞海面日出冉冉升起、高處遠眺七星潭月牙灣，美景盡收眼底。設計的構思走高端不冷硬的現代簡約風格，沉穩內斂的裝潢與得天獨厚的山海美景，打造靜心的旅宿空間，更提供餐飲、泳池、水療設施及各項山海活動等全方位服務。",
                        R.drawable.map1_0,
                        "03 861 2000"
                    )
                )
                database.insertHotel(
                    Hotel(
                        "花蓮市中美路142號",
                        "煙波大飯店花蓮店",
                        R.drawable.photo1_1,
                        "煙波大飯店花蓮館位於美崙市區，東望海天一色太平洋，西眺洄瀾重巒蒼翠，為市區最近山、海、心的交會點。得天獨厚之的地理位置，透過飯店與所處的自然人文相互輝映，邀請旅人在此用最純粹的感官與自然零距離相會。館內以分齡享受的設計概念，提供多元化的設施服務，為家庭旅行下榻的上乘之選。",
                        R.drawable.map1_1,
                        "03 822 2666"
                    )
                )
                database.insertHotel(
                    Hotel(
                        "花蓮縣壽豐鄉理想路1號",
                        "花蓮理想大地渡假飯店",
                        R.drawable.photo1_2,
                        "花蓮理想大地，為台灣唯一入選《100 Hotels + Resorts》世界百大飯店，並被美譽「提升心靈之地」。園區坐擁西班牙式環河景觀別墅、2.2公里運河、萬坪綠野、古董傢俱、藝術藏品、有機建材等六大原創特色，湖光山色共構如同峇里島及威尼斯般的浪漫風情，獲得全國最浪漫渡假飯店之美譽。",
                        R.drawable.map1_2,
                        "03 865 6789"
                    )
                )
                database.insertHotel(
                    Hotel(
                        "花蓮縣吉安鄉干城二街100號",
                        "秧悦美地度假酒店",
                        R.drawable.photo0_0,
                        "坐落於中央山脈山腳下的有機村落中，隨風起舞的香草芬芳，悄然無息地漫佈兩萬坪的無毒土地，遇見一片純淨的自然生態，生生不息的喜悅穿透了一幢幢綠建築，高度私密的空間讓身心於山海天地間得以歇息、悠然自在放逐自我，深沉融入與萬物對話，沉醉於內心的寧靜平和。日日揚帆，隨時為您停泊，沉浸靜謐的頂級度假新標杆，開啟您別具一格的專屬體驗。",
                        R.drawable.map0_0,
                        "03 812 9168"
                    )
                )
                database.insertHotel(
                    Hotel(
                        "花蓮縣秀林鄉天祥路18號",
                        "太魯閣晶英酒店 Silks Place Taroko",
                        R.drawable.photo0_1,
                        "以傲人之姿，座落於花蓮太魯閣國家公園境內的國際五星級度假酒店，為晶華國際酒店集團旗下飯店。飯店座落於東西橫貫公路之天祥地區，伴立霧溪貫穿其間，秀麗山色即是天然藝術品；全館擁有160間客房，規劃行館樓層與休閒樓層，提供多樣化料理服務，滿足您崇尚在地、美味與視覺等多重饗宴。任何季節來訪，太魯閣皆有不同的景色，面峽谷、望山嵐、聆聽溪水協奏曲，讓能量在千年峽谷裡轉化昇華，恣意享受來自於太魯閣迴響的清新與暖意。",
                        R.drawable.map0_1,
                        "03 869 1155"
                    )
                )
                database.insertHotel(
                    Hotel(
                        "花蓮縣花蓮市林森路33號",
                        "花蓮力麗華美達安可酒店",
                        R.drawable.photo2_0,
                        "花蓮力麗華美達安可酒店為溫德姆酒店集團在台灣的第一家酒店。對於人文休憩觀念的新訴求，亦或在飯店的服務設施上，都將擷取商旅及休閒會館經營之服務精華，以其細膩精緻、客製化為服務導向，結合週邊景點，在地活動之旅，『 立足東海岸 、 望眼全台灣 』，再開啟東部另一頁新休憩旅遊史！",
                        R.drawable.map2_0,
                        "03 835 0666"
                    )
                )
                database.insertHotel(
                    Hotel(
                        "花蓮縣花蓮市永興路2號",
                        "花蓮翰品酒店",
                        R.drawable.photo2_1,
                        "翰品酒店花蓮為五星級親子城市度假酒店，駐足於花蓮的童話王國。貫穿幾米繪本「擁抱」的精神，透過不同主題帶給旅人不同的感動。",
                        R.drawable.map2_1,
                        "03 823 5388"
                    )
                )
            }
        }
    }

    fun getHotel(hotelId: Long) {  //get the hotel data by given its id in the database
        hotelList.value?.let {
            selectedHotel.value = it.find { it.id == hotelId }
        }
    }

    fun insertHotel(newHotel: Hotel) {  //add a new hotel data inti the database
        viewModelScope.launch {
            database.insertHotel(newHotel)
        }
    }

    fun updateHotel(oldHotel: Hotel) {  //add a new scene into the database
        viewModelScope.launch {
            database.updateHotel(oldHotel)
        }
    }

    fun deleteHotel(oldHotel: Hotel) {  //add a new hotel data inti the database
        viewModelScope.launch {
            database.deleteHotel(oldHotel)
        }
    }
}