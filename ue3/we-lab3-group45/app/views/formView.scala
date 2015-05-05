package views

import views.html.helper._
import views.html.myTextInput
import views.html.myDate

/*
* Some examples of different fields we can create using FieldConstructor.
* - For text input group we give the constructor the character(s) that we want to display.
* - For icon input group we give the constructor the glyphicon class of the icon that we want to display.
*/

object formView {
  implicit val textInput = new FieldConstructor {
    def apply(elements: FieldElements) = myTextInput(elements)
  }
  implicit val dateInput = new FieldConstructor {
    def apply(elements: FieldElements) = myDate(elements)
  }
}