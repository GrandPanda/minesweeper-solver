case class GameOptions(startPos: (Int, Int), newGamePos: (Int, Int), tileLength: Int, boardWidth: Int, boardHeight: Int, colors: ColorOptions) {
  val boardWidthPixels = boardWidth * tileLength
  val boardHeightPixels = boardHeight * tileLength
}
case class ColorOptions(zero: Int, one: Int, two: Int, three: Int, four: Int, five: Int, six: Int, seven: Int, eight: Int, unknown: Int, flag: Int)

object GameOptions {
  val DEFAULT_COLORS = ColorOptions(-4342339, -16776961, -16745728, -65536, -16777093, -8716288, -16745605, -16777216, -8684677, -1, -65536)

  val BEGINNER = GameOptions((563, 226), (708, 171), 32, 9, 9, DEFAULT_COLORS)
  val INTERMEDIATE = GameOptions((519, 226), (772, 172), 32, 16, 16, DEFAULT_COLORS)
  val EXPERT = GameOptions((295, 226), (774, 174), 32, 30, 16, DEFAULT_COLORS)
}