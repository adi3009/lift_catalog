package code.snippet

import code.model._
import net.liftweb.sitemap.Menu
import net.liftweb.sitemap.Loc
import net.liftweb.util.Helpers._
import net.liftweb.http._
import scala.xml.{ NodeSeq, Text }

object CategoryNav {

  val menu = Menu.param[Category]("Category", Loc.LinkText(c => Text(c.urlKey)), Catalog.categoryByUrlKey _, _.urlKey) /
    "category" >> Loc.Template(() => Templates("category" :: "list" :: Nil).openOr(NodeSeq.Empty)) >> Loc.Title(c => Text(c.name))

  def render = "#categories-nav *" #> {
    for {
      (parent, children) <- Catalog.categoryHierarchy
    } yield {
      <li class="has-dropdown level-1-category">
        <a href={ menu.calcHref(parent) } title={ parent.name } class="category-name">{ parent.name }</a>
        <ul class="dropdown sub-menu">
          { children.map(categoryMenuItem(_, "level-2-category")) }
        </ul>
      </li>
    }
  }

  private def categoryMenuItem(category: Category, cssClass: String) =
    <li class={ cssClass }>
      <a href={ menu.calcHref(category) } title={ category.name } class="category-name">{ category.name }</a>
    </li>
}
