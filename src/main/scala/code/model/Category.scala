package code.model

import net.liftweb.common.Box

case class Category(id: Long, parentId: Option[Long], name: String, urlKey: String, description: String)

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
}