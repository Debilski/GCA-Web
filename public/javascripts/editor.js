require(["lib/models", "lib/tools"], function(models, tools) {
    "use strict";

    /**
     * Editor view model.
     *
     *
     * @param confId
     * @param abstrId
     * @returns {EditorViewModel}
     * @constructor
     */
    function EditorViewModel(confId, abstrId) {


        if (! (this instanceof EditorViewModel)) {
            return new EditorViewModel(confId, abstrId);
        }

        var self = this;

        self.abstractSaved = ko.observable(false);
        self.abstract = ko.observable(null);
        self.conference = ko.observable(null);


        self.init = function() {

            if (confId) {
                self.getConference(confId);
                self.abstract(models.ObservableAbstract());
            } else if (abstrId) {
                self.getAbstract(abstrId);
                self.abstractSaved(true);
            } else {
                throw "Conference id or abstract id must be defined";
            }

            ko.applyBindings(window.editor);
        };


        self.getConference = function(confId) {

            $.ajax({
                async: false,
                url: "/api/conferences/" + confId,
                type: "GET",
                success: success,
                error: fail,
                dataType: "json"
            });

            function success(obj, stat, xhr) {
                self.conference(models.Conference.fromObject(obj));
            }

            function fail(xhr, stat, msg) {
                console.log("Error while requesting the conference: uuid = " + confId);
            }

        };

        self.getAbstract = function(abstrId) {

            $.ajax({
                async: false,
                url: "/api/abstracts/" + abstrId,
                type: "GET",
                success: success,
                error: fail,
                dataType: "json"
            });

            function success(obj, stat, xhr) {
                self.conference(models.ObservableAbstract.fromObject(obj));
            }

            function fail(xhr, stat, msg) {
                console.log("Error while requesting the abstract: uuid = " + abstrId);
            }

        };


        self.saveAbstract = function() {

            if (self.abstractSaved()) {

                $.ajax({
                    async: false,
                    url: "/api/abstracts/" + self.abstract().uuid,
                    type: "PUT",
                    success: success,
                    error: fail,
                    contentType: "application/json",
                    dataType: "json",
                    data: self.abstract().toJSON()
                });

            } else if (confId) {

                $.ajax({
                    async: false,
                    url: "/api/conferences/" + confId + "/abstracts",
                    type: "POST",
                    success: success,
                    error: fail,
                    contentType: "application/json",
                    dataType: "json",
                    data: self.abstract().toJSON()
                });

            } else {
                throw "Conference id or abstract id must be defined";
            }

            function success(obj, stat, xhr) {
                self.abstract(models.ObservableAbstract.fromObject(obj));
                self.abstractSaved(true);
            }

            function fail(xhr, stat, msg) {
                console.log("Error while saving the abstract");
            }
        };


        self.refresh = function() {

            if (self.abstractSaved()) {
                console.log("save abstract");
                self.saveAbstract();
            } else {
                console.log("refresh abstract");
                //self.abstract(self.abstract());
            }
        };


        self.addAuthor = function() {
            var author = models.ObservableAuthor();
            author.position(self.abstract().authors().length);
            self.abstract().authors.push(author);
        };


        self.removeAuthor = function(author) {
            var position = author.position(),
                authors = self.abstract().authors();

            authors.splice(position, 1);
            authors.forEach(function(a, i) {
                a.position(i);
            });

            self.abstract().authors(authors);
        };


        self.addAffiliation = function() {
            var affiliation = models.ObservableAffiliation();
            affiliation.position(self.abstract().affiliations().length);
            self.abstract().affiliations.push(affiliation);
        };


        self.removeAffiliation = function(affiliation) {
            var position = affiliation.position(),
                affiliations = self.abstract().affiliations(),
                authors = self.abstract().authors();

            affiliations.splice(position, 1);
            affiliations.forEach(function(a, i) {
                a.position(i);
            });

            authors.forEach(function(author) {
                var affiliationPositions = author.affiliations(),
                    removePos = affiliationPositions.indexOf(position);

                if (removePos >= 0) {
                    affiliationPositions.splice(removePos, 1);
                }

                for (var i = 0; i < affiliationPositions.length; i++) {
                    if (affiliationPositions[i] >= removePos) {
                        affiliationPositions[i] = affiliationPositions[i] - 1;
                    }
                }

                author.affiliations(affiliationPositions);
            });

            self.abstract().affiliations(affiliations);
        };

        self.authorsForAffiliation = function(affiliation) {
            var authors = [];

            self.abstract().authors().forEach(function(author) {
               if (author.affiliations().indexOf(affiliation.position() >= 0)) {
                   authors.push(author);
               }
            });

            return authors;
        };

        self.affiliateAuthor = function(affiliation) {
            var authorPosition = $("#author-select-" + affiliation.position()).find("select").val(),
                authors = self.abstract().authors();

            if (authorPosition >= 0 && authorPosition < authors.length) {
                var author = authors[authorPosition];
                author.affiliations.push(affiliation.position());

                console.log("Add author '" + author.formatName() + "' to affiliation '" + affiliation.format() + "'");
            } else {
                throw "Unable to add author to affiliation: " + affiliation.format();
            }

        };

    }

    // start the editor
    $(document).ready(function() {

        var data = tools.hiddenData();

        console.log(data.conferenceUuid);
        console.log(data.abstractUuid);

        window.editor = EditorViewModel(data.conferenceUuid, data.abstractUuid);
        window.editor.init();
    });

});
