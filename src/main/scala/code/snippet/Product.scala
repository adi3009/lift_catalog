package code.snippet

import net.liftweb.http._
import net.liftweb.util.Helpers._
import net.liftweb.sitemap._
import scala.xml._
import code.model.{Catalog, Product => ProductModel }

object Product {
  
  val menu = Menu.param[ProductModel]("Product", Loc.LinkText(p => Text(p.id.toString)), Catalog.productById _, _.id.toString) /
    "product" >> Loc.Template(() => Templates("product" :: "view" :: Nil).openOr(NodeSeq.Empty)) >> Loc.Title(p => Text(p.name.toString))
  
  lazy val loc = menu.toLoc
  
  def render = ".product-name *" #> loc.currentValue.map(_.name.toString)
}