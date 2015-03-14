package code.model

import net.liftweb.common.Box

case class Category(id: Long, parentId: Option[Long], name: String, urlKey: String, description: String)

case class Product(id: Long, name: String, categoryId: Long)

object Catalog {

  lazy val womens = Category(1, None, "Womens", "womens", "Womens category")

  lazy val mens = Category(2, None, "Mens", "mens", "Mens category")

  lazy val fashion = Category(3, None, "Fashion", "fashion", "Fashion category")

  lazy val bags = Category(4, Some(1), "Bags", "womens-bags", "Womens bags category")

  lazy val boots = Category(5, Some(1), "Boots", "womens-boots", "Womens boots category")

  lazy val shoes = Category(5, Some(2), "Shoes", "mens-shoes", "Mens shoes category")

  lazy val categories = List(womens, mens, fashion, bags, boots, shoes)

  lazy val categoryHierarchy = List(womens -> List(bags, boots), mens -> List(shoes), fashion -> Nil)

  def categoryByUrlKey(urlKey: String): Box[Category] = categories.find(urlKey == _.urlKey)

  def productById(id: String): Box[Product] = products.find(id == _.id.toString)

  lazy val products: Seq[Product] = for {
    i <- 1 to 99
  } yield {
    val id = randomCategoryId
    val category = categories.find(_.id == id)
    category match {
      case Some(c) => Product(i, s"Product ${i} ${c.name}", c.id)
      case None    => Product(i, s"Product ${i}", id)
    }
  }

  // not the best, but fits the purpose here
  val randomGen = new scala.util.Random(5)

  def randomCategoryId = {
    val n = randomGen.nextInt(6)
    if (n <= 0 || n > 5) 1L else n.toLong
  }
}