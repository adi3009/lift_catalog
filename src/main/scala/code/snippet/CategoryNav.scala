package code.snippet

import code.model._
import net.liftweb.sitemap.Menu
import net.liftweb.sitemap.Loc
import net.liftweb.util.Helpers._
import net.liftweb.http._
import scala.xml.{ NodeSeq, Text }
import net.liftweb.common._
import scala.concurrent.ExecutionContext.Implicits.global
import net.liftweb.util.CanBind
import scala.concurrent.Future

object CategoryNav {

  val menu = Menu.param[Category]("Category", Loc.LinkText(c => Text(c.urlKey)), parser, encoder) /
    "category" >> Loc.Template(() => Templates("category" :: "list" :: Nil).openOr(NodeSeq.Empty)) >> Loc.Title(c => Text(c.name))

  private def parser(urlKey: String): Box[Category] = {
    println("category menu parser")
    Catalog.categoryByUrlKey(urlKey)
  }

  private def encoder(c: Category): String = {
    println("category menu encoder")
    println("Category" + c)
    c.urlKey
  }

  def render = "#categories-nav *" #> renderFutureCategory()

  private def renderFutureCategory(): Future[Seq[NodeSeq]] = Catalog.categoryHierarchy.map(s => s.map(_ match {
    case (parent, children) => {
      <li class="has-dropdown level-1-category">
        <a href={ menu.calcHref(parent) } title={ parent.name } class="category-name">{ parent.name }</a>
        <ul class="dropdown sub-menu">
          { children.map(categoryMenuItem(_, "level-2-category")) }
        </ul>
      </li>
    }
  }))

  private def categoryMenuItem(category: Category, cssClass: String) =
    <li class={ cssClass }>
      <a href={ menu.calcHref(category) } title={ category.name } class="category-name">{ category.name }</a>
    </li>
}
