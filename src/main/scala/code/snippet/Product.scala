package code.snippet

import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.util.Helpers._
import net.liftweb.sitemap._
import scala.xml._
import code.model.{ Catalog, Product => ProductModel }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Product {

  val menu = Menu.param[ProductModel]("Product", Loc.LinkText(p => Text(p.id.toString)), productById _, _.id.get.toString) /
    "product" >> Loc.Template(() => Templates("product" :: "view" :: Nil).openOr(NodeSeq.Empty)) >> Loc.Title(p => Text(p.name))

  lazy val loc = menu.toLoc

  def render = ".product-name *" #> loc.currentValue.map(_.name) & ".product-description *" #> loc.currentValue.map(_.description)

  private def productById(id: String): Box[ProductModel] = Await.result[Option[ProductModel]](Catalog.productById(id), Duration.Inf)
}
