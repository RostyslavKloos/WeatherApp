
# WeatherApp


A Weather app that loads information from [OpenWeatherMap API](http://api.openweathermap.org/) to show one approach to using some of the best practices in Android Development.

Including:  
 * ViewModel
 * LiveData
 * Hilt (for dependency injection)
 * Kotlin Coroutines
 * Retrofit
 * Room
 * Navigation

<p align="center">
<img src="https://github.com/RostyslavKloos/WeatherApp/blob/master/assets/currentFragment.PNG" width="190">
<img src="https://github.com/RostyslavKloos/WeatherApp/blob/master/assets/listFragment.PNG" width="190">
<img src="https://github.com/RostyslavKloos/WeatherApp/blob/master/assets/detailFragment.PNG" width="190">
<img src="https://github.com/RostyslavKloos/WeatherApp/blob/master/assets/mapFragment.PNG" width="190">
 </p>

#### The app has following packages:
1. **data**: It contains all the data accessing and manipulating components.
2. **di**: Dependency providing classes using Dagger2.
3. **ui**: View classes along with their corresponding ViewModel.
4. **utils**: Utility classes.
