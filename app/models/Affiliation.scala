// Copyright © 2014, German Neuroinformatics Node (G-Node)
//                   A. Stoewer (adrian.stoewer@rz.ifi.lmu.de)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package models

import java.util.{Set => JSet, TreeSet => JTreeSet}
import javax.persistence.{ManyToOne, ManyToMany, Entity}

/**
 * Model for affiliation
 */
@Entity
class Affiliation extends Model {

  var address: String = _
  var country: String = _
  var department: String = _
  var name: String = _
  var section: String = _

  @ManyToOne
  var abstr: Abstract = _
  @ManyToMany(mappedBy = "affiliations")
  var authors: JSet[Author] = new JTreeSet[Author]()

}


object Affiliation {

  def apply() : Affiliation = new Affiliation()

  def apply(uuid: String,
            address: String,
            country: String,
            department: String,
            name: String,
            section: String,
            abstr: Abstract,
            authors: JSet[Author] = null) : Affiliation = {

    val affiliation = new Affiliation()

    affiliation.uuid = uuid
    affiliation.address = address
    affiliation.country = country
    affiliation.department = department
    affiliation.name = name
    affiliation.section = section
    affiliation.abstr = abstr

    if (authors != null)
      affiliation.authors = authors

    affiliation
  }

}
