import java.awt.Robot
import java.awt.Rectangle
import java.awt.image.DataBufferInt
import java.awt.event._
import javax.imageio.ImageIO
import java.nio.file.Paths
import com.darichey.minesweeper.Matrix

import scala.collection._

class GameController(robot: Robot, options: GameOptions) {

  private val ROBOT_PAUSE: Int = 15

  def startGame() = {
    robot.mouseMove(options.newGamePos._1, options.newGamePos._2)
    robot.mousePress(InputEvent.BUTTON1_MASK)
    Thread.sleep(ROBOT_PAUSE)
    robot.mouseRelease(InputEvent.BUTTON1_MASK)
    robot.mouseMove(options.startPos._1, options.startPos._2)
    robot.mousePress(InputEvent.BUTTON1_MASK)
    Thread.sleep(ROBOT_PAUSE)
    robot.mouseRelease(InputEvent.BUTTON1_MASK)
  }

  def flagAt(y: Int, x: Int) = {
    robot.mouseMove(options.startPos._1 + x * options.tileLength, options.startPos._2 + y * options.tileLength)
    robot.mousePress(InputEvent.BUTTON3_MASK)
    Thread.sleep(ROBOT_PAUSE)
    robot.mouseRelease(InputEvent.BUTTON3_MASK)
    Thread.sleep(ROBOT_PAUSE)
  }

  def revealAt(y: Int, x: Int) = {
    robot.mouseMove(options.startPos._1 + x * options.tileLength, options.startPos._2 + y * options.tileLength)
    robot.mousePress(InputEvent.BUTTON1_MASK)
    Thread.sleep(ROBOT_PAUSE)
    robot.mouseRelease(InputEvent.BUTTON1_MASK)
    Thread.sleep(ROBOT_PAUSE)
  }

  def flagAll(bombs: Array[(Int, Int)]) = {
    for (t <- bombs) flagAt(t._1, t._2)
  }

  def revealAll(safe: Array[(Int, Int)]) = {
    for (t <- safe) revealAt(t._1, t._2)
  }
  
  var temp = 0
  def getBoard(): Board = {
    val image = robot.createScreenCapture(new Rectangle(options.startPos._1, options.startPos._2, options.boardWidthPixels, options.boardHeightPixels))
    ImageIO.write(image, "png", Paths.get("./images/image" + temp + ".png").toFile())
    temp = temp + 1

    val values: mutable.ArrayBuffer[Array[Board.State]] = mutable.ArrayBuffer.empty
    for (y <- 0 until options.boardHeight) {
      val row: mutable.ArrayBuffer[Board.State] = mutable.ArrayBuffer.empty
      for (x <- 0 until options.boardWidth) {
        row += stateAt(image, y, x)
      }
      values += row.toArray
    }

    new Board(values.toArray)
  }

  private def stateAt(image: java.awt.image.BufferedImage, y: Int, x: Int): Board.State = {
    // println(x + " " + y)
    if (image.getRGB(x * options.tileLength, y * options.tileLength) == options.colors.unknown) {
      if (image.getRGB(x * options.tileLength + (options.tileLength / 4), y * options.tileLength + (options.tileLength / 4)) == options.colors.flag) {
        Board.State.Flag
      } else {
        Board.State.Unknown
      }
    }
    else image.getRGB(x * options.tileLength + (options.tileLength / 2), y * options.tileLength + (options.tileLength / 8)) match {
      case options.colors.zero => Board.State.Revealed(0)
      case options.colors.one => Board.State.Revealed(1)
      case options.colors.two => Board.State.Revealed(2)
      case options.colors.three => Board.State.Revealed(3)
      case options.colors.four => Board.State.Revealed(4)
      case options.colors.five => Board.State.Revealed(5)
      case options.colors.six => Board. State.Revealed(6)
      case options.colors.seven => Board.State.Revealed(7)
      case options.colors.eight => Board.State.Revealed(8)
      case options.colors.flag => Board.State.Flag
      case options.colors.unknown => Board.State.Unknown
    }
  }
}