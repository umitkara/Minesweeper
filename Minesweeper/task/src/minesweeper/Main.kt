package minesweeper

const val GRID_SIZE: Int = 9

fun main() {
    // Variable declarations
    val grid: MutableList<MutableList<Char>> = MutableList(GRID_SIZE) { MutableList(GRID_SIZE) { '.' } }
    val mineSet: MutableSet<Pair<Int, Int>> = mutableSetOf()
    val markedCoordinates: MutableSet<Pair<Int, Int>> = mutableSetOf()
    //
    print("How many mines do you want on the field?")
    val mineCount = readln().toInt()
    initMines(mineCount, mineSet)

    // Game loop
    while (true) {
        printGrid(grid)
        print("Set/unset mine marks or claim a cell as free:")
        val line = readln().split(" ")
        val coordinates = Pair(line[1].toInt() - 1, line[0].toInt() - 1)
        val command = line[2]
        when(command) {
            "free" -> freeCell(grid, coordinates, mineSet, markedCoordinates)
            "mine" -> markCellAsMine(grid, coordinates, markedCoordinates)
        }
        if (markedCoordinates == mineSet) {
            printGrid(grid)
            println("Congratulations! You found all mines!")
            break
        }
    }
}

fun markCellAsMine(
    grid: MutableList<MutableList<Char>>,
    coordinates: Pair<Int, Int>,
    markedCoordinates: MutableSet<Pair<Int, Int>>
) {
    if (grid[coordinates.first][coordinates.second].isDigit() || grid[coordinates.first][coordinates.second] == '/') {
        println("There is a number here!")
    } else if (grid[coordinates.first][coordinates.second] == '*') {
        grid[coordinates.first][coordinates.second] = '.'
        markedCoordinates.remove(coordinates)
    } else {
        grid[coordinates.first][coordinates.second] = '*'
        markedCoordinates.add(coordinates)
    }
}

fun freeCell(
    grid: MutableList<MutableList<Char>>,
    coordinates: Pair<Int, Int>,
    mineSet: MutableSet<Pair<Int, Int>>,
    markedCoordinates: MutableSet<Pair<Int, Int>>
) {
    if (mineSet.contains(coordinates)) {
        printGrid(grid)
        println("You stepped on a mine and failed!")
        System.exit(0)
    } else {
        val adjacentCells = adjacentCells(coordinates)
        var mineCount = 0
        for (cell in adjacentCells) {
            if (mineSet.contains(cell)) {
                mineCount++
            }
        }
        if (mineCount == 0) {
            // if cell is marked as mine
            grid[coordinates.first][coordinates.second] = '/'
            for (cell in adjacentCells) {
                // if cell is marked as mine
                if (grid[cell.first][cell.second] == '*') {
                    grid[cell.first][cell.second] = '.'
                    markedCoordinates.remove(cell)
                }
                if (grid[cell.first][cell.second] == '.') {
                    freeCell(grid, cell, mineSet, markedCoordinates)
                }
            }
        } else {
            grid[coordinates.first][coordinates.second] = mineCount.toString().toCharArray()[0]
        }
    }
}

fun initMines(mineCount: Int, mineSet: MutableSet<Pair<Int, Int>>) {
    for (i in 0 until mineCount) {
        var x = (0 until GRID_SIZE).random()
        var y = (0 until GRID_SIZE).random()
        while (mineSet.contains(Pair(x, y))) {
            x = (0 until GRID_SIZE).random()
            y = (0 until GRID_SIZE).random()
        }
        mineSet.add(Pair(y, x))
    }
}

fun printGrid(grid: MutableList<MutableList<Char>>) {
    println(" │123456789│")
    println("—│—————————│")
    for (i in 0 until GRID_SIZE) {
        print("${i + 1}│")
        print(grid[i].joinToString(""))
        println("│")
    }
    println("—│—————————│")
}

fun adjacentCells(cell: Pair<Int, Int>): List<Pair<Int, Int>> {
    // return list of valid adjacent cells
    val x = cell.first
    val y = cell.second
    val adjCells = mutableListOf<Pair<Int, Int>>()
    for (i in -1..1) {
        for (j in -1..1) {
            if (x + i in 0 until GRID_SIZE && y + j in 0 until GRID_SIZE) {
                adjCells.add(Pair(x + i, y + j))
            }
        }
    }
    return adjCells
}