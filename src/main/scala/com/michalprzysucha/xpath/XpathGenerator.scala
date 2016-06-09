package com.michalprzysucha.xpath

import scala.xml.{Node, Elem}

object XpathGenerator {

  def createXPathForAttribute(root: Elem, attribute: String, value: String): String =
    createXPathForAttribute(root, attribute, value, "", 0, 1) match {
      case Some(e) => e
      case None => ""
    }

  private def createXPathForAttribute(elem: Elem, attribute: String, value: String, xpath: String, indexOfElemWithClass: Int, elemIndex: Int): Option[String] =
    if (elem.attribute(attribute).isDefined && elem.attribute(attribute).get.text == value)
      createXPathWhenAttributeIsFound(elem, attribute, xpath, indexOfElemWithClass, elemIndex)
    else
      searchForAttributeDeeperInTree(elem, attribute, value, xpath, indexOfElemWithClass, elemIndex)

  private def createXPathWhenAttributeIsFound(elem: Elem, attribute: String, xpath: String, indexOfElemWithClass: Int, elemIndex: Int): Option[String] =
    if (elem.attribute("id").isDefined)
      Some(xpathWhenElemContainsIdAttr(elem) + "/@" + attribute)
    else if (elem.attribute("class").isDefined)
      if (indexOfElemWithClass > 0)
        Some(xpathWhenElemAndXpathBothContainClassAttr(elem, xpath.substring(indexOfElemWithClass)) + "/@" + attribute)
      else
        Some(xpathWhenElemContainsClassAttrButXpathDoesNot(elem, xpath) + "/@" + attribute)
    else
      Some(xpathWhenElemAndXpathDoNotContainsClassAttr(elem, xpath, elemIndex) + "/@" + attribute)

  private def searchForAttributeDeeperInTree(elem: Elem, attribute: String, value: String, xpath: String, indexOfElemWithClass: Int, elemIndex: Int): Option[String] = {
    if (elem.attribute("id").isDefined)
      elementDeeper(elem, attribute, value, xpathWhenElemContainsIdAttr(elem), indexOfElemWithClass)
    else if (elem.attribute("class").isDefined)
      if (indexOfElemWithClass > 0) {
        val xpathFromElemWithClass: String = xpath.substring(indexOfElemWithClass)
        val newIndexOfElemWithClass = 3 + xpathFromElemWithClass.length
        elementDeeper(elem, attribute, value, xpathWhenElemAndXpathBothContainClassAttr(elem, xpathFromElemWithClass), newIndexOfElemWithClass)
      } else
        elementDeeper(elem, attribute, value, xpathWhenElemContainsClassAttrButXpathDoesNot(elem, xpath), xpath.length + 1)
    else
      elementDeeper(elem, attribute, value, xpathWhenElemAndXpathDoNotContainsClassAttr(elem, xpath, elemIndex), indexOfElemWithClass)
  }

  private def elementDeeper(elem: Elem, attribute: String, value: String, xpath: String, classes: Int): Option[String] = {
    val child: List[Node] = elem.child.toList
    val onlyElems: List[Elem] = child.collect({ case e: Elem => e })
    val groupedByLabel: Map[String, List[Elem]] = onlyElems.groupBy(_.label)
    val zippedWithIndexes: Map[String, List[(Elem, Int)]] = groupedByLabel.mapValues(elems => elems.zipWithIndex)
    val mergedToOneList: List[(Elem, Int)] = zippedWithIndexes.foldLeft(List[(Elem, Int)]())(_ ++ _._2)
    val potentialXPaths: List[Option[String]] = mergedToOneList.map({ case (e, i) => createXPathForAttribute(e, attribute, value, xpath, classes, i + 1) })
    potentialXPaths.collectFirst({ case Some(xpath) => xpath })
  }

  private def xpathWhenElemContainsIdAttr(elem: Elem): String =
    "//" + elem.label + "[@id='" + elem.attribute("id").get.text + "']"

  private def xpathWhenElemAndXpathBothContainClassAttr(elem: Elem, xpathFromElemWithClass: String): String =
    "//" + xpathFromElemWithClass + "/" + elem.label + "[@class='" + elem.attribute("class").get.text + "']"

  private def xpathWhenElemContainsClassAttrButXpathDoesNot(elem: Elem, currentXPath: String): String =
    currentXPath + "/" + elem.label + "[@class='" + elem.attribute("class").get.text + "']"

  private def xpathWhenElemAndXpathDoNotContainsClassAttr(elem: Elem, currentXPath: String, elemIndex: Int): String =
    currentXPath + "/" + elem.label + "[" + elemIndex + "]"

}
