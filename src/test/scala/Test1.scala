import org.junit.Test
import org.junit.Assert._

import java.awt.Robot
import com.darichey.minesweeper.Matrix

import java.nio.file._

class Test1 {
  @Test def test(): Unit = {
    Paths.get("./images").toFile().listFiles().foreach(_.delete)

    val robot = new Robot

    val controller = new GameController(robot, GameOptions.BEGINNER)
    controller.startGame()
    Thread.sleep(15)

    println(solve(controller))

    // val robot = new Robot
    // println(robot.getPixelColor(-660, 423).getRGB)
    // println(robot.getPixelColor(990, 515))
  }

  def solve(controller: GameController): String = {
    val board = controller.getBoard()

    if (board.values.flatMap(array => array).count(_ == Board.State.Unknown) == 0) "Complete!"
    else {
      val definites = board.definiteTiles()

      println("safe = " + definites._1.mkString(" "))
      println("bombs = " + definites._2.mkString(" "))

      if (definites._1.isEmpty && definites._2.isEmpty) "Stuck!"
      else {
        controller.revealAll(definites._1)
        controller.flagAll(definites._2)
        solve(controller)
      }
    }
  }
}