# Marvel Characters App

This is a test exercise app using the Marvel API https://developer.marvel.com/

#### Features:
* List characters
* See details of character
* Search a character by name
* Mark characters as favourite

#### Architecture:
The project is built using architecture components with coroutines, theses frameworks were chosen thinking on readability and performance. Architecture components offers a good readability and help to work with the android lifecycle, and Coroutines also has a excellent readability and performance.

#### Third party libraries:
* Retrofit to help with HTTP requests
* Koin to dependency injection
* Picasso to load images from web and manage caching and avoid OOM
* Mockito
* Requestmatcher and Mockwebserver help to mock and assert server requests

#### Known issues / Things to correct:
* The fragment stack needs a better solution, when on character detail and change tabs the stack is messed up
* The way favourite character is persisted isn`t performance friendly, a better solution would be work with ROM from architecture components to save the data
* More tests
