import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.hellohasan.weatherappmvvm.features.weather_info_show.model.data_class.City
import com.sachinrana.openweatherapp.R

class SearchAdapter(
    context: Context,
    val layout: Int,
    val city: List<City>,
    val cityClickListener: CityClickListener?
) : ArrayAdapter<City>(context, layout, city) {
    override fun getCount(): Int {
        return city.size
    }

    override fun getItem(position: Int): City? {
        return city.get(position)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var retView: View
        var vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (convertView == null) {
            retView = vi.inflate(layout, null)
        } else {
            retView = convertView
        }
        var cityItem = getItem(position)
        val parentView = retView.findViewById(R.id.ll_tv_search) as LinearLayout
        val cityName = retView.findViewById(R.id.tvSearch) as TextView
        cityName.text = cityItem!!.name
        parentView.setOnClickListener {
            cityClickListener?.onCityClick(city[position])
        }
        return retView
    }
}

interface CityClickListener {
    fun onCityClick(city: City)
}