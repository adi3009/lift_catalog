package code.snippet

import net.liftweb.util.Helpers._

import code.model.Category

class CategoryInfo(category: Category) {
  
  def render = ".description" #> category.description
}