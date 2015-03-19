package code.model

import net.liftweb.common.Box
import code.repository.{ CatalogRepository => repository }
import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global
case class Category(id: Option[Long], name: String, urlKey: String, description: String)

case class Product(id: Option[Long], name: String, description: String, categoryId: Option[Long])

object Catalog {

  lazy val categories = repository.allCategories

  def categoryByUrlKey(urlKey: String): Box[Category] = repository.categoryByUrlKey(urlKey)

  def productById(id: String): Future[Option[Product]] = repository.productById(id.toLong)

  def categoryProducts(id: Long): Seq[Product] = repository.categoryProducts(id)

  lazy val categoryHierarchy = categories.map(_.take(3).map((_, Nil)))

}
