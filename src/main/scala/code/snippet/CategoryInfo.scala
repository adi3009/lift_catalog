package code.snippet

import net.liftweb.util.Helpers._

import code.model.Category

class CategoryInfo(category: Category) {
  
  def render = ".category-name" #> category.name &
    ".description" #> category.description
}