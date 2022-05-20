package mjy.bookreviewapp.model

import androidx.room.*

@Entity
data class Review(
    @PrimaryKey val id: Int?,
    @ColumnInfo(name = "review") val review: String?
)
