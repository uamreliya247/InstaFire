package udit.com.instafireapp.models

data class Posts(
    var description: String = "",
    var creationTime: Long = 0,
    var image: String = "",
    var user: Users? = null
)