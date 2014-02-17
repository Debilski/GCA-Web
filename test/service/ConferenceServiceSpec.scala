package test.service

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import service._
import models._

/**
 * Test for ConferenceService
 */
@RunWith(classOf[JUnitRunner])
class ConferenceServiceSpec extends Specification {

  var srv = new ConferenceService()
  var account = Account(Model.makeUUID(), "foo@foo")
  var conf = Conference(Model.makeUUID(), "foo")

  "service.ConferenceService" should {

    "throw NotImplementedError for unimplemented methods" in {

      srv.list() must throwA[NotImplementedError]
      srv.listOwn(account) must throwA[NotImplementedError]

      srv.get(conf.uuid) must throwA[NotImplementedError]

      srv.create(conf, account) must throwA[NotImplementedError]
      srv.update(conf, account) must throwA[NotImplementedError]

      srv.delete(conf.uuid, account) must throwA[NotImplementedError]

    }

  }

}