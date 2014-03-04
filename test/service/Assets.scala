// Copyright © 2014, German Neuroinformatics Node (G-Node)
//                   A. Stoewer (adrian.stoewer@rz.ifi.lmu.de)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package service

import scala.{Option => ?}
import collection.JavaConversions._
import models._
import service.util.DBUtil
import javax.persistence.EntityManagerFactory


class Assets(val emf: EntityManagerFactory) extends DBUtil {

  var abstracts : Array[Abstract] = Array(
    Abstract(
      None,
      ?("Title of abstract one"),
      ?("topic one"),
      ?("text one"),
      ?("doi one"),
      ?("coi one"),
      ?("acc one"),
      approved = true,
      published = true,
      authors = Seq(
        Author(None, ?("one@foo.bar"), ?("One"), ?("Middle"), ?("Name")),
        Author(None, ?("two@foo.bar"), ?("Two"), ?("Middle"), ?("Name")),
        Author(None, ?("three@foo.bar"), ?("The"), None, ?("Name"))
      ),
      affiliations = Seq(
        Affiliation(None, ?("One address"), ?("Andorra"), ?("One department"), None, None, None),
        Affiliation(None, ?("Two address"), ?("Andorra"), ?("Two department"), None, None, None)
      ),
      references = Seq(
        Reference(None, ?("Authorone et al."), ?("Title One"), ?(2000), None),
        Reference(None, ?("Authortwo et al."), ?("Title Two"), ?(1999), None),
        Reference(None, ?("Authortwo et al."), ?("Title Three"), ?(2006), None)
      )
    ),
    Abstract(
      None,
      ?("Title of abstract two"),
      ?("topic two"),
      ?("text two"),
      ?("doi two"),
      ?("coi two"),
      ?("acc two"),
      approved = true,
      published = false,
      authors = Seq(
        Author(None, ?("four@foo.bar"), ?("Four"), ?("Middle"), ?("Name")),
        Author(None, ?("five@foo.bar"), ?("Five"), ?("Middle"), ?("Name")),
        Author(None, ?("six@foo.bar"), ?("The"), None, ?("Name"))
      ),
      affiliations = Seq(
        Affiliation(None, ?("Four address"), ?("Andorra"), ?("Four department"), None, None, None),
        Affiliation(None, ?("Five address"), ?("Andorra"), ?("Five department"), None, None, None)
      ),
      references = Seq(
        Reference(None, ?("Authorfour et al."), ?("Title Six"), ?(2000), None),
        Reference(None, ?("Authorfive et al."), ?("Title Seven"), ?(1998), None),
        Reference(None, ?("Authorfive et al."), ?("Title Nine"), ?(2009), None)
      )
    ),
    Abstract(
      None,
      ?("Title of abstract three"),
      ?("topic three"),
      ?("text three"),
      ?("doi three"),
      ?("coi three"),
      ?("acc three"),
      approved = true,
      published = false,
      authors = Seq(
        Author(None, ?("four@foo.bar"), ?("Four"), ?("Middle"), ?("Name")),
        Author(None, ?("five@foo.bar"), ?("Five"), ?("Middle"), ?("Name")),
        Author(None, ?("six@foo.bar"), ?("The"), None, ?("Name"))
      ),
      affiliations = Seq(
        Affiliation(None, ?("Four address"), ?("Andorra"), ?("Four department"), None, None, None),
        Affiliation(None, ?("Five address"), ?("Andorra"), ?("Five department"), None, None, None)
      ),
      references = Seq(
        Reference(None, ?("Authorfour et al."), ?("Title Six"), ?(2000), None),
        Reference(None, ?("Authorfive et al."), ?("Title Seven"), ?(1998), None),
        Reference(None, ?("Authorfive et al."), ?("Title Nine"), ?(2009), None)
      )
    ),
    Abstract(
      None,
      ?("Title of abstract four"),
      ?("topic four"),
      ?("text four"),
      ?("doi four"),
      ?("coi four"),
      ?("acc four"),
      approved = false,
      published = false,
      authors = Seq(
        Author(None, ?("four@foo.bar"), ?("Four"), ?("Middle"), ?("Name")),
        Author(None, ?("five@foo.bar"), ?("Five"), ?("Middle"), ?("Name")),
        Author(None, ?("six@foo.bar"), ?("The"), None, ?("Name"))
      ),
      affiliations = Seq(
        Affiliation(None, ?("Four address"), ?("Andorra"), ?("Four department"), None, None, None),
        Affiliation(None, ?("Five address"), ?("Andorra"), ?("Five department"), None, None, None)
      ),
      references = Seq(
        Reference(None, ?("Authorfour et al."), ?("Title Six"), ?(2000), None),
        Reference(None, ?("Authorfive et al."), ?("Title Seven"), ?(1998), None),
        Reference(None, ?("Authorfive et al."), ?("Title Nine"), ?(2009), None)
      )
    )
  )

  def createAbstract() : Abstract = {
    Abstract(
      None,
      ?("Title of new abstract"),
      ?("Completely new topic"),
      ?("Cool new text"),
      ?("new doi"),
      ?("No conflict at all"),
      ?("Thanks for all the fish!"),
      approved = false,
      published = false,
      authors = Seq(
        Author(None, ?("new@mail.bar"), ?("New"), ?("Cool"), ?("Author")),
        Author(None, ?("foo@mail.bar"), ?("Second"), None, ?("Author"))
      ),
      affiliations = Seq(
        Affiliation(None, ?("New Street 5"), ?("New York"), ?("New Department"), None, None, None)
      ),
      references = Seq(
        Reference(None, ?("E. Albert et al."), ?("New is always better"), ?(2000), None)
      )
    )
  }

  var alice : Account = Account(None, ?("alice@foo.net"))

  var bob: Account = Account(None, ?("bob@bar.net"))

  var eve: Account = Account(None, ?("dont@be-evil.com"))

  def accounts : Array[Account] = {
    Array(alice, bob, eve)
  }

  var conferences : Array[Conference] = Array(
    Conference(None, ?("The first conference")),
    Conference(None, ?("The second conference")),
    Conference(None, ?("The third conference"))
  )

  def fillDB() : Unit = {
    dbTransaction { (em, tx) =>
      // merge accounts
      alice = em.merge(alice)
      bob = em.merge(bob)
      eve = em.merge(eve)

      // add alice to conference owners and merge conferences
      conferences = conferences.map { conf =>
        conf.owners.add(alice)
        em.merge(conf)
      }

      // add alice and bob to abstract owners and merge abstracts
      // add all abstracts to conference one
      abstracts = abstracts.map { abstr =>
        abstr.conference = conferences(0)

        abstr.owners.add(alice)
        abstr.owners.add(bob)

        abstr.authors.foreach { author =>
          author.abstr = abstr
        }

        abstr.affiliations.foreach { affiliation =>
          affiliation.abstr = abstr
        }

        abstr.references.foreach { reference =>
          reference.abstr = abstr
        }

        em.merge(abstr)
      }


    }
  }

  def killDB() : Unit = {
    dbTransaction { (em, tx) =>
      em.createQuery("DELETE FROM Affiliation").executeUpdate()
      em.createQuery("DELETE FROM Author").executeUpdate()
      em.createQuery("DELETE FROM Reference").executeUpdate()
      em.createQuery("DELETE FROM Figure").executeUpdate()
      em.createQuery("DELETE FROM Abstract").executeUpdate()
      em.createQuery("DELETE FROM Conference").executeUpdate()
      em.createQuery("DELETE FROM Account").executeUpdate()
    }
  }

}