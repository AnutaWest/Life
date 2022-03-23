import android.os.Parcel
import android.os.Parcelable
import java.util.*


class GameField(
    val width: Int = 6,
    val height: Int = 8,
    private val matrix: Array<Array<Boolean>> = Array(width) {Array(height) {false} }
) : Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readArray(ClassLoader.getSystemClassLoader()) as Array<Array<Boolean>>
    )

    operator fun get(x: Int, y: Int): Boolean {
        val correctY = (y + height) % height
        val correctX = (x + width) % width
        return matrix[correctX][correctY]
    }
    operator fun get(tile: Coordinates): Boolean {
        return get(tile.x, tile.y)
    }

    operator fun set(tile: Coordinates, value: Boolean) {
        set(tile.x, tile.y, value)
    }

    operator fun set(x: Int, y: Int, value: Boolean) {
        val correctY = (y + height) % height
        val correctX = (x + width) % width
        matrix[correctX][correctY] = value
    }

    inner class Coordinates(x: Int, y: Int) {
        val x = (x + width) % width
        val y = (y + height) % height

        override fun equals(other: Any?): Boolean {
            return when(other) {
                is Coordinates -> (x == other.x && y == other.y)
                else -> false
            }
        }

        override fun hashCode(): Int {
            return Objects.hash(x,y)
        }

        override fun toString(): String {
            return "{x=$x y=$y}"
        }
    }

    fun copy(): GameField {
        return GameField(width, height).also {
            for(x in 0 until width) {
                for(y in 0 until height) {
                    it[x,y] = this[x,y]
                }
            }
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(width)
        dest?.writeInt(height)
        dest?.writeArray(matrix)
    }

    companion object CREATOR : Parcelable.Creator<GameField> {
        override fun createFromParcel(parcel: Parcel): GameField {
            return GameField(parcel)
        }

        override fun newArray(size: Int): Array<GameField?> {
            return arrayOfNulls(size)
        }
    }
}