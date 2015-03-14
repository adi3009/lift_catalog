package code.snippet

import net.liftweb.util.Helpers._
import code.model.{ Catalog, Category }

class Products(category: Category) {

  def render = ".product" #> {
    Catalog.products.filter(_.categoryId == category.id).map { product =>
      ".product-name *" #> product.name
    }
  }

}