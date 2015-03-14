package code.snippet

import net.liftweb.util.Helpers._
import code.model.Catalog

object Products {
  
  def render = ".product" #> {
    Catalog.products.map { product =>
      ".product-name *" #> product.name
    }
  }
  
}