### Meetmax is a Demo Social Media App.
<br></br>
#### Platform: Android
#### Language/Framework: Kotlin
#### Software Architecture Pattern: MVVM
#### Database: REST API, SharedPreferences
<br></br>
#### Authentication & Users:
* A mock API provider "https://reqres.in/" is used for user registration and login.
* Base-URL: "https://reqres.in/api"
* Its limitation is, it allows only the given email-address in their official documentation to authenticate. Their given sample email is "eve.holt@reqres.in"
* With this email user can register as a new user. [ Endpoint: "/register" ; Method: "@POST" ]
* And login too. [ Endpoint: "/login" ; Method: "@POST" ]
* Also in the top of the home page there is a list of some users, which is also getting using their API. [ Endpoint: "/users" ; Method: "@GET" ]
<br></br>
#### User Posts:
* Another mock API provider "https://jsonplaceholder.typicode.com" is used for posts.
* Base-URL: "https://jsonplaceholder.typicode.com"
* For showing posts on home page. [ Endpoint: "/posts" ; Method: "@GET" ]
* To create a post. Endpoint: [ "/posts" ; Method: "@POST" ]
<br></br>
#### Feel free to suggest any improvements.
