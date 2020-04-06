package udit.com.instafireapp.models

data class Post(
    var description: String = "",
    var creationTime: Long = 0,
    var image: String = "",
    var user: User? = null
)