package utils

import java.net.URL

class RoutesResolver {
  lazy val conf = play.api.Play.current.configuration
  lazy val baseUrl = conf.getString("baseurl").getOrElse("http://localhost:9000")

  /**
   * Builds an URL to the related abstracts from a given object ID.
   *
   * @param id an ID of a Conference object to insert into the URL
   *
   * @return URL for related abstracts, like "/conferences/HNOPSADMHV/abstracts"
   */
  def ownersUrl(id: String) = {
    new URL(baseUrl + s"/api/abstracts/$id/owners")
  }

  /**
   * Builds an URL to the related abstracts from a given object ID.
   *
   * @param id an ID of a Conference object to insert into the URL
   *
   * @return URL for related abstracts, like "/api/conferences/HNOPSADMHV/abstracts"
   */
  def abstractsUrl(id: String) = {
    new URL(baseUrl + s"/api/conferences/$id/abstracts")
  }

  /**
   * Builds an URL to all related abstracts from a given object ID.
   *
   * @param id an ID of a Conference object to insert into the URL
   *
   * @return URL for related abstracts, like "/api/conferences/HNOPSADMHV/allAbstracts"
   */
  def allAbstractsUrl(id: String) = {
    new URL(baseUrl + s"/api/conferences/$id/allAbstracts")
  }

  /**
   * Builds an URL to figure file.
   *
   * @param id an ID of a Figure object to insert into the URL
   *
   * @return URL for file, like "/api/figures/HNOPSADMHV/image"
   */
  def figureFileUrl(id: String) = {
    new URL(baseUrl + s"/api/figures/$id/image")
  }

  /**
   * Builds an URL to state log of a given abstract.
   *
   * @param id an ID of an Abstract object
   *
   * @return URL for file, like " /api/abstracts/:id/stateLog"
   */
  def stateLogUrl(id: String) = {
    new URL(baseUrl + s"/api/abstracts/$id/stateLog")
  }

}

object DefaultRoutesResolver {

  implicit val resolver = new RoutesResolver()

}