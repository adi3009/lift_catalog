package code.snippet

import net.liftweb.util.Helpers._
import code.model.Category
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import net.liftweb.util.CanBind
import scala.xml.{ NodeSeq, Text }
import scala.util.{ Success, Failure }

/*class CategoryInfo(category: Future[Category]) {

  implicit val canBindFuture = new CanBind[Future[String]] {
    def apply(fstr: => Future[String])(ns: NodeSeq): Seq[NodeSeq] = {
      var str = ""
      fstr.onComplete {
        case Success(s) => str = s
        case Failure(s) => println(s)
      }

      List(Text(str))
    }
  }

  def render = ".description" #> category.map(_.description)
}*/

class CategoryInfo(category: Category) {
  def render = ".description" #> category.description
}
