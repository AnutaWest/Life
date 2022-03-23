class Game(
    field: GameField
) {

    var field = field
        private set

    private var newField = GameField(field.width, field.height)

    private val preferences = Preferences()
    private var generation: Int = 0

    private val lookupList: MutableSet<GameField.Coordinates> = HashSet()
    private val newLookupList: MutableSet<GameField.Coordinates> = HashSet()

    fun nextGeneration() {
        if(generation == 0) {
            for(x in 0 until field.width) {
                for(y in 0 until field.height) {
                    newLookupList.add(field.Coordinates(x,y))
                }
            }
        }
        lookupList.clear()
        lookupList.addAll(newLookupList)
        newLookupList.clear()
        for(tile in lookupList) {
            processTile(tile)
        }
        field = newField.copy()
        generation++
    }

    private fun getNeighboursCount(tile: GameField.Coordinates): Int {
        var count = if(field[tile]) -1 else 0
        for(x in tile.x-1..tile.x+1) {
            for(y in tile.y-1..tile.y+1) {
                if(field[x,y]) {
                    count++
                }
            }
        }
        return count
    }

    private fun addNeighborhoodsToNextLookupList(tile: GameField.Coordinates) {
        for(x in tile.x-1..tile.x+1) {
            for(y in tile.y-1..tile.y+1) {
                newLookupList.add(field.Coordinates(x,y))
            }
        }
    }

    private fun processTile(tile: GameField.Coordinates) {
        val neighbours = getNeighboursCount(tile)
        if(neighbours in preferences.neighboursToBecomeAlive) {
            newField[tile] = true
            addNeighborhoodsToNextLookupList(tile)
        } else if(neighbours !in preferences.neighboursToStayAlive) {
            newField[tile] = false
            addNeighborhoodsToNextLookupList(tile)
        } else {
            newField[tile] = field[tile]
        }
    }
}