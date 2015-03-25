package code.snippet

import net.liftweb.util.Helpers._
import code.model.{ Catalog, Category }

class Products(category: Category) {

  def render = ".product" #>
    Catalog.categoryProducts(category.id.get).map { product =>
      ".product-name *" #> <a href={ Product.menu.calcHref(product) }>{ product.name }</a>
    }

}
